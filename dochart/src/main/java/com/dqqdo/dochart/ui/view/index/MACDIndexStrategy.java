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

        private long EMA12 = 0;

        public long getEMA12() {
            return EMA12;
        }

        public void setEMA12(long EMA12) {
            this.EMA12 = EMA12;
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
                long EMA12;
                if (i == 0) {
                    EMA12 = IndexCalculator.getEma(close, close, 12);
                } else {
                    MACDDO preDO = macddos.get(i - 1);
                    long preEMA12 = preDO.getEMA12();
                    EMA12 = IndexCalculator.getEma(close, preEMA12, 12);
                }

                MACDDO macddo = new MACDDO();
                macddo.setEMA12(EMA12);
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
        for(int i = 0; i < dataSize;i++){
            CandleBean candleBean = portData.get(i);
            if(candleBean != null){
                String date = candleBean.getDateStr();
                MACDDO macddo = macddos.get(i);
                LogUtil.d("date  ----   " + date);
                LogUtil.d("macddo  ----   " + macddo.getEMA12());

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
