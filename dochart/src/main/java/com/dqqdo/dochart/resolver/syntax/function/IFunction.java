package com.dqqdo.dochart.resolver.syntax.function;

/**
 * 函数抽象类
 * 作者：duqingquan
 * 时间：2017/11/30 17:43
 */
public interface IFunction {

    /**
     * 获取函数的关键字
     *
     * @return
     */
    String getFunctionKeyword();

    /**
     * @return
     */
    double getFunctionResult();



}
