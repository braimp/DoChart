package com.dqqdo.dochart.ui.view.stock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.dqqdo.dochart.util.LogUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * K线界面组件
 * 作者：duqingquan
 * 时间：2017/7/12 10:26
 */
public class KLineView extends View {

    // 上下文数据对象
    private Context mContext;


    // 蜡烛线宽度
    private static int candleWidth = 15;
    // 蜡烛线间隔量
    private static int candleSpace = 15;
    // 左侧间距
    private static final int axisLeftSpace = 80;
    // 顶部间距
    private static final int axisTopSpace = 10;
    // 底部间距
    private static final int axisBottomSpace = 10;
    // 右侧间距
    private static final int axisRightSpace = 30;
    // 顶部的状态展示区域（M5-M10-M15）
    private static final int topStatusSpace = 50;

    // mean数据线段宽度
    private static final int meanLineWidth = 2;


    // Candle数据
    private ArrayList<CandleBean> beans;

    // 组件整体宽高
    private int viewWidth, viewHeight;
    // 可绘制区域整体
    private int drawWidth, drawHeight;
    // 顶部边界距离绘制Top边界的绝对数
    private float leftHeightNum;
    // 顶部边界距离status边界的绝对数
    private float statusHeightNum;

    // 绘制KLine的主画笔
    Paint mPaint = new Paint();
    // 坐标画笔
    Paint axisPaint = new Paint();
    // 状态栏画笔
    Paint statusPaint = new Paint();
    // toast画笔
    Paint toastPaint = new Paint();
    // M5画笔
    Paint meanPaint = new Paint();
    // M10画笔
    Paint mean10Paint = new Paint();


    // top边界左上角坐标点
    PointF leftTopPoint;
    // top边界右上角坐标点
    PointF rightTopPoint;
    // status左上角坐标点
    PointF statusLeftTopPoint;
    // status右上角坐标点
    PointF statusRightTopPoint;
    // 左下角坐标点
    PointF leftBottomPoint;
    // 右下角坐标点
    PointF rightBottomPoint;

    // 蜡烛线开始下标
    private int startCandleIndex;
    // 蜡烛线结束下标
    private int endCandleIndex;
    // 每个绘制单元的宽度
    private int perUnitWidth;
    // 当前展示区域，总绘制单元数量
    private int unitNum;

    // 当前选中区域的最大数值
    private long maxHigh;
    // 当前选中区域的最小数值
    private long minLow;


    // 每像素对应的数值
    private double perPixelValue;
    // 手势滑动过程中最小移动量
    private static final int minMoveNum = 10;

    // 用来判断滑动手势，记录本次手势的起始坐标
    private float lastX;
    // 用来判断点击手势，记录本地点击动作的落下坐标
    private float downX;
    private float downY;
    // 点击事件的坐标位置
    private PointF clickPoint = new PointF();


    // 是否已经找到了包含点击数的下标
    private boolean isFindContainDone = false;
    // 点击数下标
    private int clickCandleIndex = 0;
    // 选中区域的m5曲线
    private Path m5Path = new Path();
    // 选中区域的m10曲线
    private Path m10Path = new Path();

    // KLine的上层边界
    private float lineTopBoundY;
    // KLine的下层边界
    private float lineBottomBoundY;

    /**
     * 组件模式
     */
    enum ViewModel {
        // 浏览模式
        SHOW_MODEL,
        // 选中模式
        CHOOSE_MODEL
    }

    // 组件模式
    private ViewModel viewModel = ViewModel.SHOW_MODEL;

    /**
     * 当前图标的时间单位，默认支持日，月，年，60分钟线。（不关心数据格式，只负责展示）
     * 可以扩展
     */
    enum TimeUnit {
        DAY,
        WEEK,
        MONTH,
        YEAR,
        MIN60
    }

    /**
     * 时间单位
     */
    private TimeUnit timeUnit = TimeUnit.MONTH;

    // 当前展示的下标
    private int currentMoth = 0;


    public KLineView(Context context) {
        super(context);
        mContext = context;
    }

    public KLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public KLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }


    public void setBeans(ArrayList<CandleBean> beans) {
        this.beans = beans;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 准备数据
        viewWidth = getWidth();
        viewHeight = getHeight();

        if (beans != null && beans.size() > 0) {
            initPaint();
            initPoint();
        }

    }


    /**
     * 初始化画笔相关
     */
    private void initPaint() {

        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLUE);

        axisPaint = new Paint();
        axisPaint.setColor(Color.GRAY);

        statusPaint = new Paint();
        statusPaint.setColor(Color.BLACK);
        statusPaint.setTextSize(26);

        toastPaint = new Paint();
        toastPaint.setColor(Color.BLACK);
        toastPaint.setTextSize(26);


        meanPaint = new Paint();
        meanPaint.setStrokeCap(Paint.Cap.ROUND);
        meanPaint.setStyle(Paint.Style.STROKE);
        meanPaint.setStrokeWidth(meanLineWidth);
        meanPaint.setColor(Color.BLUE);


        mean10Paint = new Paint();
        mean10Paint.setStrokeCap(Paint.Cap.ROUND);
        mean10Paint.setStyle(Paint.Style.STROKE);
        mean10Paint.setStrokeWidth(meanLineWidth);
        mean10Paint.setColor(Color.BLACK);

    }


    /**
     * 初始化关键点相关
     */
    private void initPoint() {

        leftHeightNum = (viewHeight - viewWidth) / 2;

        leftTopPoint = new PointF(axisLeftSpace, leftHeightNum + axisTopSpace);
        rightTopPoint = new PointF(viewWidth - axisRightSpace, leftHeightNum + axisTopSpace);
        statusLeftTopPoint = new PointF(axisLeftSpace, leftHeightNum + axisTopSpace + topStatusSpace);
        statusRightTopPoint = new PointF(viewWidth - axisRightSpace, leftHeightNum + axisTopSpace + topStatusSpace);
        leftBottomPoint = new PointF(axisLeftSpace, viewHeight - leftHeightNum - axisBottomSpace);
        rightBottomPoint = new PointF(viewWidth - axisRightSpace, viewHeight - leftHeightNum - axisBottomSpace);


        drawWidth = (int) (rightBottomPoint.x - leftTopPoint.x);
        drawHeight = (int) (leftBottomPoint.y - statusLeftTopPoint.y);

        statusHeightNum = statusLeftTopPoint.y;

        lineTopBoundY = statusHeightNum + drawHeight / 4;
        lineBottomBoundY = statusHeightNum + drawHeight / 4 * 3;

        // 每个单元宽度
        perUnitWidth = candleWidth + candleSpace;
        // 单元数量
        unitNum = drawWidth / perUnitWidth;

        // 计算当前展示位置的数据 起始，终止下标
        endCandleIndex = beans.size() - 1;
        startCandleIndex = endCandleIndex - unitNum;

        // 计算Y轴五档
        descY = new float[5];
        descYStr = new String[5];

        float perYSpace = drawHeight / 5;

        for (int i = 0; i < 5; i++) {
            descY[i] = statusLeftTopPoint.y + perYSpace * i;
        }

        updateAreaCandle();

    }

    private float startLen;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_POINTER_DOWN:

                LogUtil.d("event.getPointerCount()   ====  " + event.getPointerCount());
                if (event.getPointerCount() == 2) {
                    if (viewModel == ViewModel.SHOW_MODEL) {
                        // 浏览模式下，且是双指操作
                        startLen = Math.abs((int) event.getX(0) - (int) event.getX(1));
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:


                // 判断左滑还是右滑
                lastX = event.getX();
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int pointCount = event.getPointerCount();

//                LogUtil.d("pointCount  ===  " + pointCount);
                if (pointCount == 2) {


                    if (viewModel == ViewModel.SHOW_MODEL) {
                        // 浏览模式下，且是双指操作
                        int xLen = Math.abs((int) event.getX(0) - (int) event.getX(1));

//                        LogUtil.d("xLen  ---  " + xLen);
//                        LogUtil.d("startLen  ---  " + startLen);

                        int scaleX = (int) ((xLen - startLen) / 100);
                        if(scaleX > 0){
                            candleWidth += 1;
                            candleSpace += 1;
                        }else{
                            candleWidth -= 1;
                            candleSpace -= 1;
                        }


                        if (candleWidth > 50) {
                            candleWidth = 50;
                        }
                        if (candleSpace > 50) {
                            candleSpace = 50;
                        }

                        if (candleWidth < 5) {
                            candleWidth = 5;
                        }
                        if (candleSpace < 5) {
                            candleSpace = 5;
                        }

                        initPoint();
                        updateAreaCandle();
                        invalidate();


                    }
                } else {
                    // 判断左滑还是右滑
                    float moveX = event.getX() - lastX;

                    if (viewModel == ViewModel.SHOW_MODEL) {
                        // 浏览模式
                        if (moveX > minMoveNum) {
                            if (startCandleIndex <= 0) {
                                // 已经最大了。不做处理
                                startCandleIndex = 0;
                                Toast.makeText(mContext, "我们是有底线的", Toast.LENGTH_SHORT).show();
                                return true;
                            } else {
                                startCandleIndex -= 1;
                                endCandleIndex -= 1;
                                updateAreaCandle();
                                invalidate();
                                lastX = event.getX();
                            }


                        } else if (moveX < -minMoveNum) {

                            if (endCandleIndex >= beans.size()) {
                                // 已经最大了。不做处理
                                endCandleIndex = beans.size();
                                Toast.makeText(mContext, "我们是有底线的", Toast.LENGTH_SHORT).show();
                                return true;
                            } else {
                                startCandleIndex += 1;
                                endCandleIndex += 1;
                                updateAreaCandle();
                                invalidate();
                                lastX = event.getX();
                            }

                        } else {
                            // 未形成有效动作
                        }
                    } else {
                        // 选择模式
                        clickPoint.set(event.getX(), event.getY());
                        invalidate();
                    }
                }


                break;
            case MotionEvent.ACTION_UP:

                if (downX == event.getX() && downY == event.getY()) {
                    // 抬起和落点是一个坐标，则认为是一次点击事件
                    if (viewModel == ViewModel.SHOW_MODEL) {
                        viewModel = ViewModel.CHOOSE_MODEL;
                    } else {
                        viewModel = ViewModel.SHOW_MODEL;
                    }
                    LogUtil.d("点击事件");

                    clickPoint.set(downX, downY);
                    invalidate();
                } else {
                    LogUtil.d("不是点击事件");
                }

                // 判断左滑还是右滑
                lastX = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
                // 判断左滑还是右滑
                lastX = 0;
                break;
        }

        return true;
    }


    // Y轴描述的五档 坐标
    private float descY[];
    // Y轴描述的五档 文本
    private String descYStr[];


    /**
     * 更新展示窗口的蜡烛图
     */
    private void updateAreaCandle() {

        maxHigh = 0;
        minLow = 0;


        // 第一趟需要计算出最大最小数字
        for (int i = startCandleIndex; i < endCandleIndex; i++) {
            CandleBean candleBean = beans.get(i);
            // 最高价
            long mostHigh = candleBean.getMostHigh();
            long m5 = candleBean.getM5();
            long m10 = candleBean.getM10();
            if (maxHigh == 0) {
                maxHigh = mostHigh;
            } else {
                if (mostHigh > maxHigh) {
                    maxHigh = mostHigh;
                }
            }

            if (m5 > maxHigh) {
                maxHigh = m5;
            }

            if (m10 > maxHigh) {
                maxHigh = m10;
            }


            // 最低价
            long mostLow = candleBean.getMostLow();
            if (minLow == 0) {
                minLow = mostLow;
            } else {
                if (mostLow < minLow) {
                    minLow = mostLow;
                }
            }

            if (m5 < minLow) {
                minLow = m5;
            }

            if (m10 < minLow) {
                minLow = m10;
            }


        }

        // 根据最大，最小数据，确认单位数值
        long spaceValue = maxHigh - minLow;

        float lineBoundHeight = lineBottomBoundY - lineTopBoundY;

        perPixelValue = spaceValue * 1.0 / lineBoundHeight;


        int j = 0;
        for (int i = startCandleIndex; i < endCandleIndex; i++) {
            CandleBean candleBean = beans.get(i);
            long candleTopValue;
            long candleBottomValue;
            // 计算展示颜色
            if (candleBean.getClose() > candleBean.getOpen()) {
                candleBean.setFlagColor(Color.RED);
                candleTopValue = candleBean.getClose();
                candleBottomValue = candleBean.getOpen();
            } else {
                candleBean.setFlagColor(Color.GREEN);
                candleTopValue = candleBean.getOpen();
                candleBottomValue = candleBean.getClose();
            }

            // 计算蜡烛线四个点
            float originX = leftTopPoint.x + j * perUnitWidth;
            float startX = originX + candleSpace;
            float endX = startX + candleWidth;
            float middleX = startX + (candleWidth / 2);


            float topY = (float) ((maxHigh - candleTopValue) / perPixelValue + lineTopBoundY);
            float bottomY = (float) ((maxHigh - candleBottomValue) / perPixelValue + lineTopBoundY);
            float wickTopY = (float) ((maxHigh - candleBean.getMostHigh()) / perPixelValue + lineTopBoundY);
            float wickBottomY = (float) ((maxHigh - candleBean.getMostLow()) / perPixelValue + lineTopBoundY);

            //M5 -M10用到的数值
            float m5Y = (float) ((maxHigh - candleBean.getM5()) / perPixelValue + lineTopBoundY);
            float m10Y = (float) ((maxHigh - candleBean.getM10()) / perPixelValue + lineTopBoundY);


            candleBean.setWickTopPoint(new PointF(middleX, wickTopY));
            candleBean.setWickBottomPoint(new PointF(middleX, wickBottomY));
            candleBean.setHolderLeftTopPoint(new PointF(startX, topY));
            candleBean.setHolderRightBottomPoint(new PointF(endX, bottomY));

            candleBean.setM5Point(new PointF(middleX, m5Y));
            candleBean.setM10Point(new PointF(middleX, m10Y));

            j++;
        }

        // 计算五档数值
        for (int i = 0; i < 5; i++) {
            double yValue = (maxHigh * 1.0 - perPixelValue * i) / 100;

            DecimalFormat df = new DecimalFormat("###.00");
            descYStr[i] = df.format(yValue);
        }

    }

    /**
     * 绘制坐标轴包括顶部的显示区域
     *
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {

        canvas.drawLine(leftTopPoint.x, leftTopPoint.y, rightTopPoint.x, rightTopPoint.y, axisPaint);
        canvas.drawLine(leftTopPoint.x, leftTopPoint.y, leftBottomPoint.x, leftBottomPoint.y, axisPaint);
        canvas.drawLine(leftBottomPoint.x, leftBottomPoint.y, rightBottomPoint.x, rightBottomPoint.y, axisPaint);
        canvas.drawLine(rightBottomPoint.x, rightBottomPoint.y, rightTopPoint.x, rightTopPoint.y, axisPaint);
        canvas.drawLine(statusLeftTopPoint.x, statusLeftTopPoint.y, statusRightTopPoint.x, statusRightTopPoint.y, axisPaint);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        isFindContainDone = false;

        if (beans == null || !(beans.size() > 0)) {
            canvas.drawColor(Color.BLUE);
            return;
        }

        // 绘制背景
        canvas.drawColor(Color.WHITE);

        // 绘制坐标轴
        drawAxis(canvas);
        // 绘制蜡烛线
        m5Path = new Path();
        m10Path = new Path();

        for (int i = startCandleIndex; i < endCandleIndex; i++) {
            CandleBean candleBean = beans.get(i);
            // 判断当前的落点位置
            if (viewModel == ViewModel.CHOOSE_MODEL) {
                if (!isFindContainDone) {
                    if (candleBean.isContains(clickPoint)) {
                        isFindContainDone = true;
                        clickCandleIndex = i;
                    }
                }
            }

            // 绘制主体
            candleBean.drawSelf(canvas, mPaint);
            // 绘制m5-m10曲线
            if (i == startCandleIndex) {
                // 开始位置
                PointF pointF = candleBean.getM5Point();
                m5Path.moveTo(pointF.x, pointF.y);
                PointF m10pointF = candleBean.getM10Point();
                m10Path.moveTo(m10pointF.x, m10pointF.y);

                int dayNum = getDayNum(candleBean.getTime());
                // 每个月一号，绘制月线
                if (dayNum == 1) {
                    float lineX = candleBean.getWickBottomPoint().x;
                    canvas.drawLine(lineX, statusLeftTopPoint.y, lineX, leftBottomPoint.y, axisPaint);
                    // 绘制下标
                    String dateStr = candleBean.getDateStr();
                    Rect rect = new Rect();
                    statusPaint.getTextBounds(dateStr, 0, dateStr.length(), rect);
                    canvas.drawText(dateStr, (lineX - rect.width() / 2), leftBottomPoint.y + rect.height(), statusPaint);
                }
                LogUtil.d(currentMoth + "");
            } else {
                // 不是开始位置
                PointF pointF = candleBean.getM5Point();
                CandleBean preCandle = beans.get(i - 1);
                PointF prePoint = preCandle.getM5Point();

                float wt = (pointF.x + prePoint.x) / 2;
                PointF p3 = new PointF();
                PointF p4 = new PointF();
                p3.y = prePoint.y;
                p3.x = wt;
                p4.y = pointF.y;
                p4.x = wt;

                m5Path.moveTo(prePoint.x, prePoint.y);
                m5Path.cubicTo(p3.x, p3.y, p4.x, p4.y, pointF.x, pointF.y);


                PointF m10pointF = candleBean.getM10Point();
                m10Path.lineTo(m10pointF.x, m10pointF.y);

                int dayNum = getDayNum(candleBean.getTime());
                // 每个月一号，绘制月线
                if (dayNum == 1) {
                    float lineX = candleBean.getWickBottomPoint().x;
                    canvas.drawLine(lineX, statusLeftTopPoint.y, lineX, leftBottomPoint.y, axisPaint);
                    // 绘制下标
                    String dateStr = candleBean.getDateStr();
                    Rect rect = new Rect();
                    statusPaint.getTextBounds(dateStr, 0, dateStr.length(), rect);
                    canvas.drawText(dateStr, (lineX - rect.width() / 2), leftBottomPoint.y + rect.height(), statusPaint);
                }


            }

        }

        // 绘制模式反馈
        if (viewModel == ViewModel.CHOOSE_MODEL) {
            if (clickCandleIndex != 0) {
                // 绘制当前选择的 Candle信息
                CandleBean selectedCandle = beans.get(clickCandleIndex);
                if (selectedCandle != null) {
                    // 绘制选中线
                    selectedCandle.drawSelectedLine(canvas, axisPaint,
                            axisLeftSpace, axisLeftSpace + drawWidth,
                            statusHeightNum, statusHeightNum + drawHeight);

                    // 状态信息绘制
                    String statusDesc = "   高：" + selectedCandle.getMostHigh() * 1.0 / 100
                            + " 开：" + selectedCandle.getOpen() * 1.0 / 100
                            + " 低：" + selectedCandle.getMostLow() * 1.0 / 100
                            + " 收：" + selectedCandle.getClose() * 1.0 / 100;

                    canvas.drawText(statusDesc, leftTopPoint.x, leftTopPoint.y, statusPaint);

                    String meanDesc = "   M5：" + selectedCandle.getM5() * 1.0 / 100
                            + "  M10：" + selectedCandle.getM10() * 1.0 / 100
                            + "  time:  " + selectedCandle.getDateStr();
                    canvas.drawText(meanDesc, statusLeftTopPoint.x, statusLeftTopPoint.y, statusPaint);

                    // 绘制点位数据
                    // x轴方向
                    String axisDesc = selectedCandle.getDateStr();
                    Rect xRect = new Rect();

                    statusPaint.getTextBounds(axisDesc, 0, axisDesc.length(), xRect);
                    xRect.offset((int) (selectedCandle.getWickTopPoint().x - xRect.width() / 2), (int) (leftBottomPoint.y + xRect.height() + 10));
                    RectF xRectF = new RectF();
                    xRectF.left = xRect.left - 12;
                    xRectF.right = xRect.right + 12;
                    xRectF.bottom = xRect.bottom + 10;
                    xRectF.top = xRect.top - 10;

                    drawRoundRect(canvas, toastPaint, xRectF);
                    canvas.drawText(axisDesc, xRectF.left + 10, xRectF.bottom - 12, statusPaint);


                    double value;
                    if (Color.RED == selectedCandle.getFlagColor()) {
                        value = selectedCandle.getClose()  * 1.0 / 100;
                    } else {
                        value = selectedCandle.getOpen() * 1.0 / 100;
                    }

                    axisDesc = String.valueOf(value);
                    xRect = new Rect();
                    statusPaint.getTextBounds(axisDesc, 0, axisDesc.length(), xRect);
                    xRect.offset((int) (leftBottomPoint.x - xRect.width()), (int) (selectedCandle.getFocusY() + 10));
                    xRectF = new RectF();
                    xRectF.left = xRect.left - 12;
                    xRectF.right = xRect.right + 12;
                    xRectF.bottom = xRect.bottom + 10;
                    xRectF.top = xRect.top - 10;
                    drawRoundRect(canvas, toastPaint, xRectF);
                    canvas.drawText(axisDesc, xRectF.left + 10, xRectF.bottom - 12, statusPaint);

                }

            }

        }

        // mean5-mean10
        canvas.drawPath(m5Path, meanPaint);
        // 定位是M10路径问题
//        canvas.drawPath(m10Path,mean10Paint);


        // 绘制五档数值
        for (int i = 0; i < 5; i++) {
            float y = descY[i];
            String desc = descYStr[i];
            float textWidth = statusPaint.measureText(desc);
            float x = statusLeftTopPoint.x - textWidth;
            canvas.drawText(desc, x, y, statusPaint);
        }

    }


    private void drawRoundRect(Canvas canvas, Paint p, RectF rect) {
        p.setStyle(Paint.Style.FILL);//充满
        p.setColor(Color.LTGRAY);
        p.setAntiAlias(true);// 设置画笔的锯齿效果
        canvas.drawRoundRect(rect, 20, 15, p);//第二个参数是x半径，第三个参数是y半径
    }

    private int getMonthNum(long time) {
        // 初始化当前月份
        long monthValue = time % Long.parseLong("10000000000");
        int monthNum = (int) (monthValue / 100000000);
        return monthNum;
    }

    private int getDayNum(long time) {
        // 初始化当前月份
        long monthValue = time % Long.parseLong("100000000");
        int monthNum = (int) (monthValue / 1000000);
        return monthNum;
    }

}
