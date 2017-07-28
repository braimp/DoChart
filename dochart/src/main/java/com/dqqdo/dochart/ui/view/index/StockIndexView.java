package com.dqqdo.dochart.ui.view.index;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dqqdo.dochart.ui.view.stock.CandleBean;
import com.dqqdo.dochart.util.LogUtil;

import java.util.ArrayList;

/**
 * 股票指标 展示组件
 * 作者：duqingquan
 * 时间：2017/7/12 10:26
 */
public class StockIndexView extends View {

    // 上下文数据对象
    private Context mContext;



    // 蜡烛线宽度
    public static final int candleWidth = 15;
    // 蜡烛线间隔量
    public static final int candleSpace = 15;
    // 左侧间距
    public static final int axisLeftSpace = 80;
    // 顶部间距
    public static final int axisTopSpace = 10;
    // 底部间距
    public static final int axisBottomSpace = 10;
    // 右侧间距
    public static final int axisRightSpace = 30;
    // 顶部的状态展示区域
    public static final int topStatusSpace = 50;


    // Candle数据
    private ArrayList<CandleBean> beans;


    // 组件整体宽高
    private int viewWidth, viewHeight;
    // 可绘制区域整体
    private int drawWidth, drawHeight;
    // 顶部边界距离绘制Top边界的绝对数
    private float leftHeightNum;


    // 绘制KLine的主画笔
    Paint mPaint = new Paint();
    // 状态栏画笔
    Paint statusPaint = new Paint();

    // status左上角坐标点
    PointF statusLeftTopPoint;
    // status右上角坐标点
    PointF statusRightTopPoint;


    // 蜡烛线开始下标
    private int startCandleIndex;
    // 蜡烛线结束下标
    private int endCandleIndex;
    // 每个绘制单元的宽度
    private int perUnitWidth;
    // 当前展示区域，总绘制单元数量
    private int unitNum;



    // 每像素对应的数值（有效绘制区域）
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


    // Y轴描述的五档 坐标
    private float descY[];
    // Y轴描述的五档 文本
    private String descYStr[];

    // Y轴描述的五档 坐标
    private Float descX[];
    // Y轴描述的五档 文本
    private String descXStr[];


    IndexAdapter indexAdapter = new IndexAdapter();

    // 最外层的有效区域，除此以外的区域都是边角料
    RectF validRect = new RectF();

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

    AxisAdapter axisAdapter = new AxisAdapter();


    public StockIndexView(Context context) {
        super(context);
        mContext = context;
    }

    public StockIndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public StockIndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }


    public void setBeans(ArrayList<CandleBean> beans) {
        this.beans = beans;
        indexAdapter.setData(beans);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 准备数据
        viewWidth = getWidth();
        viewHeight = getHeight();
        // 根据设定的比率，确认屏幕的展示范围
        if(w > h){
            // 宽大于高，假设横屏
            leftHeightNum = 0;
        }else{
            // 假设竖屏
            leftHeightNum = (viewHeight - viewWidth) / 2;
        }

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

        statusPaint = new Paint();
        statusPaint.setColor(Color.BLACK);
        statusPaint.setTextSize(26);

    }



    //


    /**
     * 初始化关键点相关
     */
    private void initPoint() {

        // 计算绘制区域的四个角，以及包括状态区域在内的两个关键点
        validRect.left = axisLeftSpace;
        validRect.right = viewWidth - axisRightSpace;
        validRect.top = leftHeightNum + axisTopSpace;
        validRect.bottom = viewHeight - leftHeightNum - axisBottomSpace;
        // status 关键点
        statusLeftTopPoint = new PointF(axisLeftSpace, leftHeightNum + axisTopSpace + topStatusSpace);
        statusRightTopPoint = new PointF(viewWidth - axisRightSpace, leftHeightNum + axisTopSpace + topStatusSpace);


        drawWidth = (int) validRect.width();
        drawHeight = (int) validRect.height();


        // 每个单元宽度
        perUnitWidth = candleWidth + candleSpace;
        // 单元数量
        unitNum = drawWidth / perUnitWidth;

        // 计算当前展示位置的数据 起始，终止下标
        endCandleIndex = beans.size() - 1;
        startCandleIndex = endCandleIndex - unitNum;

        // 计算Y轴五档
        descY = new float[5];

        float perYSpace = drawHeight / 5;

        for (int i = 0; i < 5; i++) {
            descY[i] = statusLeftTopPoint.y + perYSpace * i;
        }


        // axis rect
        RectF axisRectF = new RectF(validRect);

        // index rect
        RectF viewport = new RectF();
        viewport.top = statusLeftTopPoint.y + 100;
        viewport.left = statusLeftTopPoint.x;
        viewport.bottom = validRect.bottom;
        viewport.right = statusRightTopPoint.x;

        indexAdapter.updateViewPort(viewport);
        axisAdapter.updateViewPort(axisRectF);

        updateAreaCandle();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 判断左滑还是右滑
                lastX = event.getX();
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
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




    /**
     * 更新展示窗口的蜡烛图
     */
    private void updateAreaCandle() {

        indexAdapter.updateArea(startCandleIndex,endCandleIndex);
        descYStr = indexAdapter.getDescYStr();
        descX = indexAdapter.getDescX();
        descXStr = indexAdapter.getDescXStr();

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
        axisAdapter.drawAxis(canvas);
        axisAdapter.drawAxisDesc(descY,descYStr,descX,descXStr,canvas);
        // 绘制指标
        indexAdapter.drawIndex(canvas,mPaint);


        // 绘制模式反馈
        if (viewModel == ViewModel.CHOOSE_MODEL) {
            indexAdapter.drawSelectIndex(canvas,mPaint,clickPoint);
        }

        // 绘制描述文本
        canvas.drawText(indexAdapter.getIndexDesc(),statusLeftTopPoint.x,statusLeftTopPoint.y,statusPaint);


    }




}
