package com.dqqdo.dochart.resolver.syntax.shape;

import com.dqqdo.dochart.resolver.draw.IDrawItem;
import com.dqqdo.dochart.resolver.syntax.LogicPrimitive;

/**
 * 时间：2017/12/4 14:20
 * @author duqingquan
 */
public interface IShape {

    /**
     * 根据逻辑图元转换为可绘制对象
     * @param logicPrimitive 逻辑图元
     * @return 可绘制对象
     */
    IDrawItem getDrawItem(LogicPrimitive logicPrimitive);
}
