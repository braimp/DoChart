package com.dqqdo.dochart.resolver.draw;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Arrays;

/**
 * 作者：duqingquan
 * 时间：2017/12/5 18:14
 */
public class LineDrawItem implements IDrawItem {

    private int mColor;
    private Paint mPaint;
    private float[] coordinates;
    /**
     * 最小的坐标点数量
     * （8个，因为一个点是四个坐标。一条线段最少两个点）
     */
    public static final int MIN_POINTS_NUM = 4;

    public LineDrawItem(){
        // 内部构造器
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
    }

    /**
     * 设置颜色
     * @param color
     */
    public void setColor(int color){
        mPaint.setColor(color);
        this.mColor = color;
    }

    public void setData(float[] data){
        this.coordinates = data;
    }

    @Override
    public void drawSelf(Canvas canvas) {
        // 根据坐标绘制图形
        if(canvas == null){
            return ;
        }
        if(mPaint == null){
            return ;
        }
        if(coordinates != null && coordinates.length > MIN_POINTS_NUM){
            canvas.drawLines(coordinates,mPaint);
        }

    }

}
