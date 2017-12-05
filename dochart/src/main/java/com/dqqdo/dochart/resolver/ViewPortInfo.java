package com.dqqdo.dochart.resolver;

import android.graphics.RectF;

/**
 * 证券展示组件的 视口对象
 * 作者：duqingquan
 * 时间：2017/12/5 14:22
 */
public class ViewPortInfo {

    /**
     * 开始位置下标
     */
    private int startIndex;

    /**
     * 结束位置下标
     */
    private int endIndex;

    /**
     * 组件视窗范围对象
     */
    private RectF viewRectF;


    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public RectF getViewRectF() {
        return viewRectF;
    }

    public void setViewRectF(RectF viewRectF) {
        this.viewRectF = viewRectF;
    }

    public ViewPortInfo() {

    }
}
