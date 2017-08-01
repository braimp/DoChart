package com.dqqdo.dochart.ui.view.index;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;




/**
 * 指标数据 适配器
 * 作者：duqingquan
 * 时间：2017/7/21 16:44
 */
public class IndexAdapter {

    // 指标算法策略
    private IndexStrategy volIndexStrategy;
    // 视窗
    RectF mViewPort;
    // 绘制KLine的主画笔
    Paint mPaint = new Paint();


    public IndexAdapter(IndexStrategy strategy) {
        this.volIndexStrategy = strategy;

        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLUE);

    }

    /**
     * 更新窗口信息
     *
     * @param viewport 可视区域
     */
    public void updateViewPort(RectF viewport) {
        mViewPort = viewport;
    }

    /**
     * 更新区域数据
     *
     * @param startIndex 开始选中位置坐标
     * @param endIndex   结束坐标
     */
    public void updateByFrameChange(int startIndex, int endIndex) {

        if (mViewPort != null) {
            volIndexStrategy.calcFormulaPoint(startIndex, endIndex, mViewPort);
        }
    }


    /**
     * 绘制指标
     *
     * @param canvas
     */
    public void drawIndex(Canvas canvas) {
        volIndexStrategy.drawIndex(canvas, mPaint);
    }


    /**
     * 绘制选中部分
     *
     * @param canvas
     * @param pointF
     */
    public void drawSelectIndex(Canvas canvas, PointF pointF) {
        // 首先根据pointF 查找数字下标
        int indexNum = volIndexStrategy.getSelectIndex(pointF);
        volIndexStrategy.drawSelectIndex(canvas, mPaint, indexNum);
    }


}
