package com.dqqdo.dochart.resolver;

/**
 * 脚本的数据环境，脚本解析器运行的前提是使用者必须提供相关的必要数据
 * 时间：2017/12/5 14:08
 * @author duqingquan
 */
public class ScriptEnvironment {

    /**
     * 股票信息对象
     */
    private StockInfo stockInfo;

    /**
     * 组件的视窗信息
     */
    private ViewPortInfo viewPortInfo;

    /**
     * 准备数据环境
     * @param info 股票信息
     * @param viewInfo 视窗信息
     * @return 准备工作是否成功
     */
    public boolean prepareWork(StockInfo info,ViewPortInfo viewInfo){
        // TODO 很多指标函数数据运算会用到分红、财报等数据信息。
        // TODO 数据有效性校验等待后续实现。
        this.stockInfo = info;
        this.viewPortInfo = viewInfo;

        return true;
    }

}
