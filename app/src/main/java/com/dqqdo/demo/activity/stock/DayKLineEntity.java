package com.dqqdo.demo.activity.stock;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：duqingquan
 * 时间：2017/7/11 11:37
 */
public class DayKLineEntity implements Serializable {


    private List<KLineBean> KLine;
    private List<List<Long>> Data;

    public List<KLineBean> getKLine() {
        return KLine;
    }

    public void setKLine(List<KLineBean> KLine) {
        this.KLine = KLine;
    }

    public List<List<Long>> getData() {
        return Data;
    }

    public void setData(List<List<Long>> Data) {
        this.Data = Data;
    }

    public static class KLineBean {
        /**
         * Time : 时间
         * LastClose : 前收盘价
         * Open : 开盘价
         * Close : 收盘价
         * High : 最高价
         * Low : 最低价
         * Volume : 成交量
         * Amount : 成交额
         */

        private String Time;
        private String LastClose;
        private String Open;
        private String Close;
        private String High;
        private String Low;
        private String Volume;
        private String Amount;

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public String getLastClose() {
            return LastClose;
        }

        public void setLastClose(String LastClose) {
            this.LastClose = LastClose;
        }

        public String getOpen() {
            return Open;
        }

        public void setOpen(String Open) {
            this.Open = Open;
        }

        public String getClose() {
            return Close;
        }

        public void setClose(String Close) {
            this.Close = Close;
        }

        public String getHigh() {
            return High;
        }

        public void setHigh(String High) {
            this.High = High;
        }

        public String getLow() {
            return Low;
        }

        public void setLow(String Low) {
            this.Low = Low;
        }

        public String getVolume() {
            return Volume;
        }

        public void setVolume(String Volume) {
            this.Volume = Volume;
        }

        public String getAmount() {
            return Amount;
        }

        public void setAmount(String Amount) {
            this.Amount = Amount;
        }
    }
}
