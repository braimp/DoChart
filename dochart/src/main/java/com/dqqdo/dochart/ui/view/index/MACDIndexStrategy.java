package com.dqqdo.dochart.ui.view.index;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.dqqdo.dochart.ui.view.stock.CandleBean;
import com.dqqdo.dochart.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：duqingquan
 * 时间：2017/7/31 17:56
 */
public class MACDIndexStrategy extends IndexStrategy {

    /**
     * MACD指标 DATA Object
     */
    class MACDDO {

        private double EMA12 = 0;
        private double EMA26 = 0;
        private double diff = 0;
        private double dea = 0;
        private double bar;


        public double getBar() {
            return bar;
        }

        public void setBar(double bar) {
            this.bar = bar;
        }

        public double getDea() {
            return dea;
        }

        public void setDea(double dea) {
            this.dea = dea;
        }

        public double getEMA12() {
            return EMA12;
        }

        public void setEMA12(double EMA12) {
            this.EMA12 = EMA12;
        }

        public double getEMA26() {
            return EMA26;
        }

        public void setEMA26(double EMA26) {
            this.EMA26 = EMA26;
        }

        public double getDiff() {
            return diff;
        }

        public void setDiff(double diff) {
            this.diff = diff;
        }
    }

    ArrayList<MACDDO> macddos;


    @Override
    public void setData(ArrayList<CandleBean> data) {
        super.setData(data);
        macddos = new ArrayList<>(candles.size());
        initMea();
    }

    /**
     * EMA需要12 和 26 天的数据，需要开始时就计算完毕
     */
    private void initMea() {


        macddos.clear();

        int dataSize = candles.size();
        for (int i = 0; i < dataSize; i++) {
            CandleBean candleBean = candles.get(i);
            if (candleBean != null) {

                long close = candleBean.getClose();
                double EMA12;
                double EMA26;
                double dea;
                double diff;
                double macd;
                if (i == 0) {
                    EMA12 = IndexCalculator.getEma(close, close, 12);
                    EMA26 = IndexCalculator.getEma(close, close, 26);
                    diff = 0;
                    dea = 0;
                    macd = 0;

                    LogUtil.d("close   ----  " + close);
                    LogUtil.d("EMA12   ----  " + EMA12);
                    LogUtil.d("EMA26   ----  " + EMA26);
                    LogUtil.d("diff   ----  " + diff);
                } else {

                    MACDDO preDO = macddos.get(i - 1);
                    double preEMA12 = preDO.getEMA12();
                    EMA12 = IndexCalculator.getEma(close, preEMA12, 12);
                    double preEMA26 = preDO.getEMA26();
                    EMA26 = IndexCalculator.getEma(close, preEMA26, 26);
                    // diff
                    diff = EMA12 - EMA26;
                    // dea
                    double preDea = preDO.getDea();
                    dea = preDea * 8 / 10 + diff * 2 / 10;
                    // macd
                    macd = 2 * (diff - dea);

//                    if(i < 10){
                        LogUtil.d("******************");
                        LogUtil.d("date   ----  " + candleBean.getDateStr());
                        LogUtil.d("close   ----  " + close);
                        LogUtil.d("diff   ----  " + diff);
                        LogUtil.d("dea   ----  " + dea);
                        LogUtil.d("macd   ----  " + macd);
//                    }


                }

                MACDDO macddo = new MACDDO();
                macddo.setEMA12(EMA12);
                macddo.setEMA26(EMA26);
                macddo.setDiff(diff);
                macddo.setDea(dea);
                macddo.setBar(macd);

                macddos.add(macddo);

            }
        }
    }


    @Override
    public boolean calcFormulaPoint(int startIndex, int endIndex, RectF viewPort) {

        List<CandleBean> portData = candles.subList(startIndex, endIndex);

//        /**
//         * 先计算EMA1和EMA2
//         * 平滑系数=2/（周期单位+1） w= 2  /  ()
//         *
//         */
        int dataSize = portData.size();
        for (int i = 0; i < dataSize; i++) {
            CandleBean candleBean = portData.get(i);
            if (candleBean != null) {
                String date = candleBean.getDateStr();
                MACDDO macddo = macddos.get(i);

//                LogUtil.d("date  ----   " + date);
//                LogUtil.d("ema12  ----   " + macddo.getEMA12());
//                LogUtil.d("ema26  ----   " + macddo.getEMA26());
//                LogUtil.d("diff  ----   " + macddo.getDiff());
//                LogUtil.d("close  ----   " + candleBean.getClose());
//                LogUtil.d("macddo  ----   " + macddo.getBar());



            }
        }


        return false;
    }

    @Override
    public String[] getDescYStr() {
        return new String[0];
    }

    @Override
    public String[] getDescXStr() {
        return new String[0];
    }

    @Override
    public Float[] getDescX() {
        return new Float[0];
    }

    @Override
    public void drawIndex(Canvas canvas, Paint paint) {

    }

    @Override
    public String getIndexName() {
        return "MACD 指标";
    }

    @Override
    public String getIndexFormula() {
        return "太复杂，就不列出来了";
    }

    @Override
    public int getSelectIndex(PointF pointF) {
        return 0;
    }

    @Override
    public void drawSelectIndex(Canvas canvas, Paint paint, int index) {

    }
}
