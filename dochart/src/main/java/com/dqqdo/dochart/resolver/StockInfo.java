package com.dqqdo.dochart.resolver;

import java.util.List;

/**
 * 股票基本信息，解析器需要的数据
 * 作者：duqingquan
 * 时间：2017/11/30 13:34
 */
public class StockInfo {

    /**
     * 内码
     */
    private long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 代码
     */
    private String code;
    /**
     * 当前价
     */
    private int price;
    /**
     * 涨跌
     */
    private int upDown;
    /**
     * 涨跌幅
     */
    private int upDownRate;
    /**
     * 今开盘
     */
    private int openPrice;
    /**
     * 昨收盘
     */
    private int lastPrice;
    /**
     * 成交额
     */
    private long amount;
    /**
     * 振幅
     */
    private int vibrationRatio;
    /**
     * 最高
     */
    private int high;
    /**
     * 最低
     */
    private int low;
    /**
     * 成交量
     */
    private long volume;
    /**
     * 收盘时间
     */
    private int closeTime;
    /**
     * 当前时间
     */
    private long time;
    /**
     * 开盘时间
     */
    private int openTime;
    /**
     * 换手率
     */
    private int exchangeRatio;
    /**
     * 内盘
     */
    private long inVolume;
    /**
     * 外盘
     */
    private long outVolume;
    /**
     * 市值
     */
    private long totalPrice;
    /**
     * 市盈率
     */
    private int pe;
    /**
     * 流通市值
     */
    private long circulPrice;
    /**
     * 五档买价
     */
    private List<Integer> buyPrice;
    /**
     * 五档买量
     */
    private List<Integer> buyVolume;
    /**
     * 五档卖价
     */
    private List<Integer> sellPrice;
    /**
     * 五档卖量
     */
    private List<Integer> sellVolume;
    /**
     * 涨家数,
     */
    private int riseCount;
    /**
     * 跌家数
     */
    private int fallCount;

    /**
     * 平家数
     */
    private int flatCount;
    /**
     * 是否是股票信息, 股指数据为false
     */
    private boolean isStockInfo;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUpDown() {
        return upDown;
    }

    public void setUpDown(int upDown) {
        this.upDown = upDown;
    }

    public int getUpDownRate() {
        return upDownRate;
    }

    public void setUpDownRate(int upDownRate) {
        this.upDownRate = upDownRate;
    }

    public int getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(int openPrice) {
        this.openPrice = openPrice;
    }

    public int getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(int lastPrice) {
        this.lastPrice = lastPrice;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getVibrationRatio() {
        return vibrationRatio;
    }

    public void setVibrationRatio(int vibrationRatio) {
        this.vibrationRatio = vibrationRatio;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(int closeTime) {
        this.closeTime = closeTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public int getExchangeRatio() {
        return exchangeRatio;
    }

    public void setExchangeRatio(int exchangeRatio) {
        this.exchangeRatio = exchangeRatio;
    }

    public long getInVolume() {
        return inVolume;
    }

    public void setInVolume(long inVolume) {
        this.inVolume = inVolume;
    }

    public long getOutVolume() {
        return outVolume;
    }

    public void setOutVolume(long outVolume) {
        this.outVolume = outVolume;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getPe() {
        return pe;
    }

    public void setPe(int pe) {
        this.pe = pe;
    }

    public long getCirculPrice() {
        return circulPrice;
    }

    public void setCirculPrice(long circulPrice) {
        this.circulPrice = circulPrice;
    }

    public List<Integer> getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(List<Integer> buyPrice) {
        this.buyPrice = buyPrice;
    }

    public List<Integer> getBuyVolume() {
        return buyVolume;
    }

    public void setBuyVolume(List<Integer> buyVolume) {
        this.buyVolume = buyVolume;
    }

    public List<Integer> getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(List<Integer> sellPrice) {
        this.sellPrice = sellPrice;
    }

    public List<Integer> getSellVolume() {
        return sellVolume;
    }

    public void setSellVolume(List<Integer> sellVolume) {
        this.sellVolume = sellVolume;
    }

    public int getRiseCount() {
        return riseCount;
    }

    public void setRiseCount(int riseCount) {
        this.riseCount = riseCount;
    }

    public int getFallCount() {
        return fallCount;
    }

    public void setFallCount(int fallCount) {
        this.fallCount = fallCount;
    }

    public int getFlatCount() {
        return flatCount;
    }

    public void setFlatCount(int flatCount) {
        this.flatCount = flatCount;
    }

    public boolean isStockInfo() {
        return isStockInfo;
    }

    public void setStockInfo(boolean stockInfo) {
        isStockInfo = stockInfo;
    }

}
