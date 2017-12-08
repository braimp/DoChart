package com.dqqdo.dochart.resolver.draw;

import android.graphics.Canvas;

/**
 * 可绘制对象抽象接口
 * 时间：2017/11/29 14:01
 * @author duqingquan
 */
public interface IDrawItem {

    /**
     * 绘制自身
     * @param canvas 画板对象
     */
    void drawSelf(Canvas canvas);

}
