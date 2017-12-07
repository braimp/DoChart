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
}
