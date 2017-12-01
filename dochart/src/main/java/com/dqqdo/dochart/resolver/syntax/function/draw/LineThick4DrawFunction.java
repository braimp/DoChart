package com.dqqdo.dochart.resolver.syntax.function.draw;

import com.dqqdo.dochart.resolver.syntax.function.AbsDrawFunction;

/**
 * 作者：duqingquan
 * 时间：2017/12/1 16:02
 */
public class LineThick4DrawFunction extends AbsDrawFunction {

    private String keyword = "LINETHICK4";


    @Override
    public String getFunctionKeyword() {
        return keyword;
    }

    @Override
    public double getFunctionResult() {
        return 0;
    }
}
