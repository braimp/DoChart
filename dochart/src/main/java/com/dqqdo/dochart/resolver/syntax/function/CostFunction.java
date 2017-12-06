package com.dqqdo.dochart.resolver.syntax.function;

/**
 * COST函数
 * 股票平均成本函数，根据输入的N，返回N%的获利股票的平均成本
 * 作者：duqingquan
 * 时间：2017/11/30 17:44
 */
public class CostFunction implements IFunction{

    /**
     * 函数名称
     */
    private final String COST_KEY_WORD = "COST";

    /**
     * 比例参数
     */
    private int radio;

    @Override
    public String getFunctionKeyword() {
        return COST_KEY_WORD;
    }

    @Override
    public double getFunctionResult(String line) {
        // TODO 筹码分布算法实现
        // 计算结果先忽略。因为需要筹码分布算法(后续添加)
        return 20;
    }





}
