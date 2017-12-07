package com.dqqdo.dochart.resolver.syntax.function.draw;

import com.dqqdo.dochart.resolver.StockInfo;
import com.dqqdo.dochart.resolver.syntax.function.AbsDrawFunction;

/**
 * 作者：duqingquan
 * 时间：2017/12/1 16:00
 */
public class PointDotDrawFunction extends AbsDrawFunction {

    private String keyword = "pointdot";

    @Override
    public String getFunctionKeyword() {
        return keyword;
    }

    @Override
    public double getFunctionResult(String expression, StockInfo stockInfo) {
        return 0;
    }





}
