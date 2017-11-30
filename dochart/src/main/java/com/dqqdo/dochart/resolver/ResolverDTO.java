package com.dqqdo.dochart.resolver;

/**
 * 作者：duqingquan
 * 时间：2017/11/30 14:03
 */
public class ResolverDTO {


    /**
     * 公式
     */
    private String formula;

    /**
     * 股票信息对象
     */
    private StockInfo stockInfo;


    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public StockInfo getStockInfo() {
        return stockInfo;
    }

    public void setStockInfo(StockInfo stockInfo) {
        this.stockInfo = stockInfo;
    }


}
