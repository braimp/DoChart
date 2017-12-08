package com.dqqdo.dochart.resolver.syntax.function;

import com.dqqdo.dochart.resolver.StockInfo;

/**
 * 函数抽象类
 * 时间：2017/11/30 17:43
 * @author duqingquan
 */
public interface IFunction {



    /**
     * 获取函数的关键字，一般为函数名
     * @return
     */
    String getFunctionKeyword();


    /**
     * 获取返回的返回结果
     * @param expression 参数字符串
     * @param stockInfo  股票信息
     * @return 计算结果
     */
    double getFunctionResult(String expression,StockInfo stockInfo);



}
