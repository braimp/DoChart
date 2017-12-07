package com.dqqdo.dochart.resolver;

/**
 * 作者：duqingquan
 * 时间：2017/11/30 14:03
 */
public class ResolverTaskDO {


    /**
     * 公式
     */
    private String formula;

    /**
     * 对应的股票id
     */
    private Long stockId;

    /**
     * 当前解析任务的开始时间
     */
    private Long startTime;

    /**
     * 当前解析任务的结束时间
     */
    private Long endTime;


    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }


    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }


    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

}
