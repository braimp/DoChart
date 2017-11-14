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
 * @author duqingquan
 * 时间：2017/7/12 10:26
 */
public class KLineView extends View {

    /**
     * 上下文数据对象
     */
    private Context mContext;


    /**
     * 蜡烛线宽度
     */
    private static int candleWidth = 15;
    /**
     * 蜡烛线间隔量
     */
    private static int candleSpace = 15;
    /**
     * 左侧间距
     */
    private static final int AXIS_LEFT_SPACE = 80;
    /**
     * 顶部间距
     */
    private static final int AXIS_TOP_SPACE = 10;
    /**
     * 底部间距
     */
    private static final int AXIS_BOTTOM_SPACE = 10;
    /**
     * 右侧间距
     */
    private static final int AXIS_RIGHT_SPACE = 30;
    /**
     * 顶部的状态展示区域（M5-M10-M15）
     */
    private static final int TOP_STATUS_SPACE = 50;
    /**
     * mean数据线段宽度
     */
    private static final int MEAN_LINE_WIDTH = 2;


    /**
     *  Candle数据
     */
    private ArrayList<CandleBean> beans;

    /**
     *  变化过的 Candle数据
     */
    private ArrayList<CandleBean> drawBeans;

    /**
     * 组件整体宽高
     */
    private int viewWidth, viewHeight;
    /**
     * 可绘制区域整体
     */
    private int drawWidth, drawHeight;
    /**
     * 顶部边界距离绘制Top边界的绝对数
     */
    private float leftHeightNum;
    /**
     * 顶部边界距离status边界的绝对数
     */
    private float statusHeightNum;

    /**
     * 绘制KLine的主画笔
     */
    Paint mPaint = new Paint();
    /**
     *  坐标画笔
     */
    Paint axisPaint = new Paint();
    /**
     * 状态栏画笔
     */
    Paint statusPaint = new Paint();
    /**
     * toast画笔
     */
    Paint toastPaint = new Paint();
    /**
     * M5画笔
     */
    Paint meanPaint = new Paint();
    /**
     * M10画笔
     */
    Paint mean10Paint = new Paint();


    /**
     * top边界左上角坐标点
     */
    PointF leftTopPoint;
    /**
     * top边界右上角坐标点
     */
    PointF rightTopPoint;
    /**
     * status左上角坐标点
     */
    PointF statusLeftTopPoint;
    /**
     * status右上角坐标点
     */
    PointF statusRightTopPoint;
    /**
     * 左下角坐标点
     */
    PointF leftBottomPoint;
    /**
     * 右下角坐标点
     */
    PointF rightBottomPoint;

    /**
     * 蜡烛线开始下标
     */
    private int startCandleIndex;
    /**
     * 蜡烛线结束下标
     */
    private int endCandleIndex;
    /**
     * 每个绘制单元的宽度
     */
    private int perUnitWidth;
    /**
     * 当前展示区域，总绘制单元数量
     */
    private int unitNum;


    /**
     * 当前选中区域的最大数值
     */
    private long maxHigh;
    /**
     * 当前选中区域的最小数值
     */
    private long minLow;


    /**
     * 每像素对应的数值
     */
    private double perPixelValue;
    /**
     * 手势滑动过程中最小移动量
     */
    private static final int minMoveNum = 10;

    /**
     * 用来判断滑动手势，记录本次手势的起始坐标
     */
    private float lastX;
    /**
     * 用来判断点击手势，记录本地点击动作的落下坐标
     */
    private float downX;
    private float downY;
    /**
     * 点击事件的坐标位置
     */
    private PointF clickPoint = new PointF();


    /**
     * 是否已经找到了包含点击数的下标
     */
    private boolean isFindContainDone = false;
    /**
     * 点击数下标
     */
    private int clickCandleIndex = 0;
    /**
     * 选中区域的m5曲线
     */
    private Path m5Path = new Path();
    /**
     * 选中区域的m10曲线
     */
    private Path m10Path = new Path();

    /**
     * KLine的上层边界
     */
    private float lineTopBoundY;
    /**
     * KLine的下层边界
     */
    private float lineBottomBoundY;



    /**
     * 组件模式
     */
    enum ViewModel {

        /**
         * 浏览模式
         */
        SHOW_MODEL,
        /**
         * 选中模式
         */
        CHOOSE_MODEL

    }


    /**
     * 除权 复权 类型
     */
    public enum RightType{

        /**
         * 前复权
         */
        RIGHT_FORWARD,

        /**
         * 后复权
         */
        RIGHT_BACKWARD,

        /**
         * 不复权
         */
        NO_RIGHT
    }

    /**
     * 除复权选择
     */
    RightType rightType = RightType.NO_RIGHT;


    public RightType getRightType() {
        return rightType;
    }

    public void setRightType(RightType rightType) {
        this.rightType = rightType;
        updateAreaCandle();
        invalidate();
    }


    /**
     * 组件模式
     */
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

    /**
     * 当前展示的下标
     */
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


    /**
     * 股票除权，复权信息
     */
    class StockRightBean{

        /**
         * 当日复权比例
         */
        private double radioRightToday = 1;

        /**
         * 累计复权比例
         */
        private double radioRightCumulative = 1;


        public double getRadioRightToday() {
            return radioRightToday;
        }

        public void setRadioRightToday(double radioRightToday) {
            this.radioRightToday = radioRightToday;
        }

        public double getRadioRightCumulative() {
            return radioRightCumulative;
        }

        public void setRadioRightCumulative(double radioRightCumulative) {
            this.radioRightCumulative = radioRightCumulative;
        }

    }

    ArrayList<StockRightBean> rightBeans = new ArrayList<>();

    public void setBeans(ArrayList<CandleBean> beans) {
        this.beans = beans;
        int size = beans.size();
        drawBeans = new ArrayList<>(size);
        rightBeans = new ArrayList<>(size);


        for(int i = 0; i < size;i++){

            CandleBean tmpBean = beans.get(i);
            drawBeans.add(tmpBean.clone());

            // 计算除权，复权相关信息
            StockRightBean stockRightBean = new StockRightBean();

            if(i == 0){
                stockRightBean.setRadioRightToday(1);
                stockRightBean.setRadioRightCumulative(1);
            }else {
                StockRightBean preRightBean = rightBeans.get(i -1);

                CandleBean preCandleBean = beans.get(i - 1);
                CandleBean nowCandleBean = beans.get(i);

                double preRadio = preRightBean.radioRightCumulative;


                double cumulativeRadio = preRadio *
                        preCandleBean.getClose() * 1.0
                        / nowCandleBean.getClose();

                double todayRadio = preCandleBean.getClose() * 1.0
                        / nowCandleBean.getClose();

                stockRightBean.setRadioRightCumulative(cumulativeRadio);
                stockRightBean.setRadioRightToday(todayRadio);

            }

            rightBeans.add(stockRightBean);

        }


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
        meanPaint.setStrokeWidth(MEAN_LINE_WIDTH);
        meanPaint.setColor(Color.BLUE);


        mean10Paint = new Paint();
        mean10Paint.setStrokeCap(Paint.Cap.ROUND);
        mean10Paint.setStyle(Paint.Style.STROKE);
        mean10Paint.setStrokeWidth(MEAN_LINE_WIDTH);
        mean10Paint.setColor(Color.BLACK);

    }


    /**
     * 初始化关键点相关
     */
    private void initPoint() {

        leftHeightNum = (viewHeight - viewWidth) / 2;

        leftTopPoint = new PointF(AXIS_LEFT_SPACE, leftHeightNum + AXIS_TOP_SPACE);
        rightTopPoint = new PointF(viewWidth - AXIS_RIGHT_SPACE, leftHeightNum + AXIS_TOP_SPACE);
        statusLeftTopPoint = new PointF(AXIS_LEFT_SPACE, leftHeightNum + AXIS_TOP_SPACE + TOP_STATUS_SPACE);
        statusRightTopPoint = new PointF(viewWidth - AXIS_RIGHT_SPACE, leftHeightNum + AXIS_TOP_SPACE + TOP_STATUS_SPACE);
        leftBottomPoint = new PointF(AXIS_LEFT_SPACE, viewHeight - leftHeightNum - AXIS_BOTTOM_SPACE);
        rightBottomPoint = new PointF(viewWidth - AXIS_RIGHT_SPACE, viewHeight - leftHeightNum - AXIS_BOTTOM_SPACE);


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

                if (pointCount == 2) {


                    if (viewModel == ViewModel.SHOW_MODEL) {
                        // 浏览模式下，且是双指操作
                        int xLen = Math.abs((int) event.getX(0) - (int) event.getX(1));


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

                    clickPoint.set(downX, downY);
                    invalidate();
                } else {
                    return false;
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


    /**
     * Y轴描述的五档 坐标
     */
    private float[] descY;
    /**
     * Y轴描述的五档 文本
     */
    private String[] descYStr;



    /**
     * 更新展示窗口的蜡烛图
     */
    private void updateAreaCandle() {

        maxHigh = 0;
        minLow = 0;

        StockRightBean lastRightBean = rightBeans.get(rightBeans.size() - 1);


        // 第一趟需要计算出最大最小数字
        for (int i = startCandleIndex; i < endCandleIndex; i++) {

            StockRightBean stockRightBean = rightBeans.get(i);
            CandleBean candleBean = beans.get(i);
            CandleBean drawCandleBean = drawBeans.get(i);



            // 如果存在除复权的情况，则提前进行处理
            if(rightType != RightType.NO_RIGHT){

                if(rightType == RightType.RIGHT_BACKWARD){

                    // 后复权  当日收盘价*当日累计复权比例
                    long open = (long) (candleBean.getOpen() * 1.0
                            * stockRightBean.radioRightCumulative);
                    long close = (long) (candleBean.getClose() * 1.0
                            * stockRightBean.radioRightCumulative);
                    long mostHigh = (long) (candleBean.getMostHigh() * 1.0
                            * stockRightBean.radioRightCumulative);
                    long mostLow = (long) (candleBean.getMostLow() * 1.0
                            * stockRightBean.radioRightCumulative);
                    long m5 = (long) (candleBean.getM5() * 1.0
                            * stockRightBean.radioRightCumulative);
                    long m10 = (long) (candleBean.getM10() * 1.0
                            * stockRightBean.radioRightCumulative);

                    LogUtil.d("i    ---------   " + drawCandleBean.getTime());
                    LogUtil.d("candleBean.getOpen()    ---------   " + candleBean.getOpen());
                    LogUtil.d("candleBean.getClose()    ---------   " + candleBean.getClose());
                    LogUtil.d("candleBean.getMostHigh()    ---------   " + candleBean.getMostHigh());
                    LogUtil.d("candleBean.getMostLow()    ---------   " + candleBean.getMostLow());
                    LogUtil.d("radioRightCumulative    ---------   " + stockRightBean.radioRightCumulative);

                    LogUtil.d("open    ---------   " + open);
                    LogUtil.d("close    ---------   " + close);
                    LogUtil.d("mostHigh    ---------   " + mostHigh);
                    LogUtil.d("mostLow    ---------   " + mostLow);

                    LogUtil.d("********************************  ");


                    drawCandleBean.setOpen(open);
                    drawCandleBean.setClose(close);
                    drawCandleBean.setMostHigh(mostHigh);
                    drawCandleBean.setMostLow(mostLow);
                    drawCandleBean.setM5(m5);
                    drawCandleBean.setM10(m10);


                }else {

                    LogUtil.d("stockRightBean.radioRightCumulative ------  "
                            + stockRightBean.radioRightCumulative);

                    LogUtil.d("lastRightBean.radioRightCumulative ------  "
                            + lastRightBean.radioRightCumulative);

                    // 前复权  当日收盘价*当日累计复权比例/最新日期的累计复权比例
                    long open = (long) (candleBean.getOpen() * 1.0
                            * stockRightBean.radioRightCumulative
                            / lastRightBean.radioRightCumulative
                            );
                    long close = (long) (candleBean.getClose() * 1.0
                            * stockRightBean.radioRightCumulative
                            / lastRightBean.radioRightCumulative
                            );
                    long mostHigh = (long) (candleBean.getMostHigh() * 1.0
                            * stockRightBean.radioRightCumulative
                            / lastRightBean.radioRightCumulative
                            );
                    long mostLow = (long) (candleBean.getMostLow() * 1.0
                            * stockRightBean.radioRightCumulative
                            / lastRightBean.radioRightCumulative
                            );
                    long m5 = (long) (candleBean.getM5() * 1.0
                            * stockRightBean.radioRightCumulative
                            / lastRightBean.radioRightCumulative
                            );
                    long m10 = (long) (candleBean.getM10() * 1.0
                            * stockRightBean.radioRightCumulative
                            / lastRightBean.radioRightCumulative
                            );

                    drawCandleBean.setOpen(open);
                    drawCandleBean.setClose(close);
                    drawCandleBean.setMostHigh(mostHigh);
                    drawCandleBean.setMostLow(mostLow);
                    drawCandleBean.setM5(m5);
                    drawCandleBean.setM10(m10);

                }

            }else{
                drawCandleBean.setOpen(candleBean.getOpen());
                drawCandleBean.setClose(candleBean.getClose());
                drawCandleBean.setMostHigh(candleBean.getMostHigh());
                drawCandleBean.setMostLow(candleBean.getMostLow());
                drawCandleBean.setM5(candleBean.getM5());
                drawCandleBean.setM10(candleBean.getM10());
            }


            // 最高价
            long mostHigh = drawCandleBean.getMostHigh();
            long m5 = drawCandleBean.getM5();
            long m10 = drawCandleBean.getM10();

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
            long mostLow = drawCandleBean.getMostLow();
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

        LogUtil.d("maxHigh  --- " + maxHigh);
        LogUtil.d("minLow  --- " + minLow);

        perPixelValue = spaceValue * 1.0 / lineBoundHeight;


        int j = 0;
        for (int i = startCandleIndex; i < endCandleIndex; i++) {

            CandleBean drawCandleBean = drawBeans.get(i);
            long candleTopValue;
            long candleBottomValue;
            // 计算展示颜色
            if (drawCandleBean.getClose() > drawCandleBean.getOpen()) {
                drawCandleBean.setFlagColor(Color.RED);
                candleTopValue = drawCandleBean.getClose();
                candleBottomValue = drawCandleBean.getOpen();
            } else {
                drawCandleBean.setFlagColor(Color.GREEN);
                candleTopValue = drawCandleBean.getOpen();
                candleBottomValue = drawCandleBean.getClose();
            }

            // 计算蜡烛线四个点
            float originX = leftTopPoint.x + j * perUnitWidth;
            float startX = originX + candleSpace;
            float endX = startX + candleWidth;
            float middleX = startX + (candleWidth / 2);


            float topY = (float) ((maxHigh - candleTopValue) / perPixelValue + lineTopBoundY);
            float bottomY = (float) ((maxHigh - candleBottomValue) / perPixelValue + lineTopBoundY);
            float wickTopY = (float) ((maxHigh - drawCandleBean.getMostHigh()) / perPixelValue + lineTopBoundY);
            float wickBottomY = (float) ((maxHigh - drawCandleBean.getMostLow()) / perPixelValue + lineTopBoundY);

            //M5 -M10用到的数值
            float m5Y = (float) ((maxHigh - drawCandleBean.getM5()) / perPixelValue + lineTopBoundY);
            float m10Y = (float) ((maxHigh - drawCandleBean.getM10()) / perPixelValue + lineTopBoundY);


            drawCandleBean.setWickTopPoint(new PointF(middleX, wickTopY));
            drawCandleBean.setWickBottomPoint(new PointF(middleX, wickBottomY));
            drawCandleBean.setHolderLeftTopPoint(new PointF(startX, topY));
            drawCandleBean.setHolderRightBottomPoint(new PointF(endX, bottomY));

            drawCandleBean.setM5Point(new PointF(middleX, m5Y));
            drawCandleBean.setM10Point(new PointF(middleX, m10Y));

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

        if (drawBeans == null || !(drawBeans.size() > 0)) {
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
            CandleBean drawCandleBean = drawBeans.get(i);
            // 判断当前的落点位置
            if (viewModel == ViewModel.CHOOSE_MODEL) {
                if (!isFindContainDone) {
                    if (drawCandleBean.isContains(clickPoint)) {
                        isFindContainDone = true;
                        clickCandleIndex = i;
                    }
                }
            }

            // 绘制主体
            drawCandleBean.drawSelf(canvas, mPaint);
            // 绘制m5-m10曲线
            if (i == startCandleIndex) {
                // 开始位置
                PointF pointF = drawCandleBean.getM5Point();
                m5Path.moveTo(pointF.x, pointF.y);
                PointF m10pointF = drawCandleBean.getM10Point();
                m10Path.moveTo(m10pointF.x, m10pointF.y);

                int dayNum = getDayNum(drawCandleBean.getTime());
                // 每个月一号，绘制月线
                if (dayNum == 1) {
                    float lineX = drawCandleBean.getWickBottomPoint().x;
                    canvas.drawLine(lineX, statusLeftTopPoint.y, lineX, leftBottomPoint.y, axisPaint);
                    // 绘制下标
                    String dateStr = drawCandleBean.getDateStr();
                    Rect rect = new Rect();
                    statusPaint.getTextBounds(dateStr, 0, dateStr.length(), rect);
                    canvas.drawText(dateStr, (lineX - rect.width() / 2), leftBottomPoint.y + rect.height(), statusPaint);
                }

            } else {
                // 不是开始位置
                PointF pointF = drawCandleBean.getM5Point();
                CandleBean preCandle = drawBeans.get(i - 1);
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


                PointF m10pointF = drawCandleBean.getM10Point();
                m10Path.lineTo(m10pointF.x, m10pointF.y);

                int dayNum = getDayNum(drawCandleBean.getTime());
                // 每个月一号，绘制月线
                if (dayNum == 1) {
                    float lineX = drawCandleBean.getWickBottomPoint().x;
                    canvas.drawLine(lineX, statusLeftTopPoint.y, lineX, leftBottomPoint.y, axisPaint);
                    // 绘制下标
                    String dateStr = drawCandleBean.getDateStr();
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
                CandleBean selectedCandle = drawBeans.get(clickCandleIndex);
                if (selectedCandle != null) {
                    // 绘制选中线
                    selectedCandle.drawSelectedLine(canvas, axisPaint,
                            AXIS_LEFT_SPACE, AXIS_LEFT_SPACE + drawWidth,
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


    /**
     * 绘制矩形
     * @param canvas
     * @param p
     * @param rect
     */
    private void drawRoundRect(Canvas canvas, Paint p, RectF rect) {
        //充满
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.LTGRAY);
        // 设置画笔的锯齿效果
        p.setAntiAlias(true);
        //第二个参数是x半径，第三个参数是y半径
        canvas.drawRoundRect(rect, 20, 15, p);
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
