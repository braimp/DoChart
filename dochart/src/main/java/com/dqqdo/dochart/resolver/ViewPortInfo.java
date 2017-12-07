package com.dqqdo.dochart.resolver;

import android.graphics.RectF;

/**
 * 证券展示组件的 视口对象
 * 作者：duqingquan
 * 时间：2017/12/5 14:22
 */
public class ViewPortInfo {


    /**
     * 组件视窗范围对象
     */
    private RectF viewRectF;


    public void setViewRectF(RectF viewRectF) {
        this.viewRectF = viewRectF;
    }

    public ViewPortInfo() {

    }

    /**
     * 当屏最大值
     */
    private double maxValue;

    /**
     * 当屏最小值
     */
    private double minValue;

    /**
     * 每一单位对应的像素值
     */
    private double perValuePixel;

    /**
     * x方向 每单元的宽度
     */
    private double perUnitWidth;

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public RectF getViewRectF() {
        return viewRectF;
    }

    public double getPerValuePixel() {
        return perValuePixel;
    }

    public void setPerValuePixel(double perValuePixel) {
        this.perValuePixel = perValuePixel;
    }

    public double getPerUnitWidth() {
        return perUnitWidth;
    }

    public void setPerUnitWidth(double perUnitWidth) {
        this.perUnitWidth = perUnitWidth;
    }
}
