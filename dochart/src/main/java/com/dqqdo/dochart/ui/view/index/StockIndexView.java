package com.dqqdo.dochart.ui.view.index;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dqqdo.dochart.ui.view.stock.CandleBean;

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


    // 组件整体宽高
    private int viewWidth, viewHeight;
    // 可绘制区域整体（在KLine模式下，习惯性的使用有效高度的一般）
    private int drawWidth, drawHeight;
    // 顶部边界距离绘制Top边界的绝对数
    private float leftHeightNum;




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

    // 点击事件的坐标位置
    private PointF clickPoint = new PointF();


    // Candle数据
    private ArrayList<CandleBean> beans;

    // 最外层的有效区域，除此以外的区域都是边角料
    RectF validRect = new RectF();


    // 算法策略
    private IndexStrategy indexStrategy;
    // 指标适配器
    IndexAdapter indexAdapter;
    // 坐标轴适配器
    AxisAdapter axisAdapter;
    // touch适配器
    private TouchAdapter touchAdapter;



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


    /**************************  构造函数  *********************************/

    public StockIndexView(Context context) {
        super(context);
        mContext = context;
        initData();
    }

    public StockIndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
    }

    public StockIndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData();
    }




    private void initData(){

//        indexStrategy = new VolIndexStrategy();
        indexStrategy = new MACDIndexStrategy();
        touchAdapter = new TouchAdapter(indexStrategy);
        indexAdapter = new IndexAdapter(indexStrategy);
        axisAdapter = new AxisAdapter(indexStrategy);

    }




    public void setBeans(ArrayList<CandleBean> beans) {
        this.beans = beans;
        indexStrategy.setData(beans);
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
            initPoint();
        }

    }

    TouchAdapter.TouchCallback touchCallback = new TouchAdapter.TouchCallback() {
        @Override
        public void onCandleIndexChanged(int startIndex, int endIndex) {
            startCandleIndex = startIndex;
            endCandleIndex = endIndex;

            updateByFrameChange();
            invalidate();
        }

        @Override
        public void onClick(PointF pointF) {

            if (viewModel == StockIndexView.ViewModel.SHOW_MODEL) {
                viewModel = StockIndexView.ViewModel.CHOOSE_MODEL;
            } else {
                viewModel = StockIndexView.ViewModel.SHOW_MODEL;
            }
            clickPoint = pointF;

            invalidate();

        }

        @Override
        public void onSelectChange(PointF pointF) {
            clickPoint = pointF;
            updateByFrameChange();
            invalidate();
        }
    };




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



        // axis rect
        RectF axisRectF = new RectF(validRect);
        RectF statusRectF = new RectF(validRect);
        statusRectF.bottom = statusLeftTopPoint.y;


        // index rect
        RectF viewport = new RectF();
        viewport.top = statusLeftTopPoint.y + 100;
        viewport.left = statusLeftTopPoint.x;
        viewport.bottom = validRect.bottom;
        viewport.right = statusRightTopPoint.x;


        indexAdapter.updateViewPort(viewport);
        axisAdapter.updateViewPort(axisRectF,statusRectF);

        updateByFrameChange();

    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return touchAdapter.doTouchEvent(event,viewModel,startCandleIndex,endCandleIndex,touchCallback);

    }




    /**
     * 更新展示窗口
     */
    private void updateByFrameChange() {
        indexAdapter.updateByFrameChange(startCandleIndex,endCandleIndex);
        axisAdapter.updateByFrameChange();
    }



    @Override
    protected void onDraw(Canvas canvas) {

        if (beans == null || beans.size() <= 0) {
            canvas.drawColor(Color.BLUE);
            return;
        }

        // 绘制背景
        canvas.drawColor(Color.WHITE);

        // 绘制坐标轴
        axisAdapter.drawAxis(canvas);
        axisAdapter.drawAxisDesc(canvas);
        // 绘制指标
        indexAdapter.drawIndex(canvas);


        // 绘制模式反馈
        if (viewModel == ViewModel.CHOOSE_MODEL) {
            indexAdapter.drawSelectIndex(canvas,clickPoint);
        }




    }




}
