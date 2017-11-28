package com.dqqdo.dochart.resolver;

/**
 * 指标解析器 接口定义
 * 作者：duqingquan
 * 时间：2017/10/10 17:43
 * @author hexun
 */
public interface IndexResolver {

    /**
     * 设置公式指标(json串)，返回错误码(返回0表示没有错误)
     * @param strFormula
     * @return
     */
    int setFormula(String strFormula);

    /**
     * 设置公式运行参数(json串)，返回错误码(返回0表示没有错误)
     * @param strParam
     * @return
     */
    int setParams(String strParam);

    /**
     * 设置数据(json串)，返回错误码(返回0表示没有错误)
     * @param strData
     * @return
     */
    int setData(String strData);

    /**
     * 执行公式，json串一次性返回所有结果集，如果有错误err_code会返回错误码。
     * @return
     */
    String execute();

    /**
     * 执行公式，json串只返回结果集数量result_num，如果有错误err_code会返回错误码。
     * @return
     */
    String executeEx();

    /**
     * 获取执行结果，json串返回指定的结果集，参数nStart表示起始位置，nCount表示取多少条结果。
     * @param nStart
     * @param nCount
     * @return
     */
    String getResult(int nStart, int nCount);

}
