package com.dqqdo.dochart.ui.view.index;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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

    MACDIndexStrategy() {
        mPaint.setColor(Color.RED);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        deaPaint.setColor(Color.BLUE);
        deaPaint.setStrokeCap(Paint.Cap.ROUND);
        deaPaint.setStyle(Paint.Style.STROKE);
        deaPaint.setStrokeWidth(5);


    }

    /**
     * MACD指标 DATA Object
     */
    class MACDDO {

        private double EMA12 = 0;
        private double EMA26 = 0;
        private double diff = 0;
        private double dea = 0;
        private double bar;

        /*****************绘制部分的关键数据*********************/
        private float diffY;
        private float deaY;
        private float barY;
        private float barColor;
        private float x;


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


                } else {

                    MACDDO preDO = macddos.get(i - 1);
                    double preEMA12 = preDO.EMA12;
                    EMA12 = IndexCalculator.getEma(close, preEMA12, 12);
                    double preEMA26 = preDO.EMA26;
                    EMA26 = IndexCalculator.getEma(close, preEMA26, 26);
                    // diff
                    diff = EMA12 - EMA26;
                    // dea
                    double preDea = preDO.dea;
                    dea = preDea * 8 / 10 + diff * 2 / 10;
                    // macd
                    macd = 2 * (diff - dea);


                }

                MACDDO macddo = new MACDDO();
                macddo.EMA12 = EMA12;
                macddo.EMA26 = EMA26;
                macddo.diff = diff;
                macddo.dea = dea;
                macddo.bar = macd;

                macddos.add(macddo);

            }
        }
    }


    // 当前选中区域的最大数值
    private double maxHigh = 0;
    // 当前选中区域的最小数值
    private double minLow = 0;
    RectF mViewPort;
    List<CandleBean> portData;
    // 0线所在的Y值
    private float zeroY;

    private int mStartIndex,mEndIndex;

    @Override
    public boolean calcFormulaPoint(int startIndex, int endIndex, RectF viewPort) {


        mStartIndex = startIndex;
        mEndIndex = endIndex;

        mViewPort = viewPort;
        portData = candles.subList(startIndex, endIndex);

//        /**
//         * 先计算EMA1和EMA2
//         * 平滑系数=2/（周期单位+1） w= 2  /  ()
//         *
//         */
        int dataSize = portData.size();
        for (int i = mStartIndex; i < mEndIndex; i++) {

            MACDDO macddo = macddos.get(i);
            double diff = macddo.diff;
            double dea = macddo.dea;
            double macd = macddo.bar;

            if (diff < minLow) {
                minLow = diff;
            }
            if (dea < minLow) {
                minLow = dea;
            }
            if (macd < minLow) {
                minLow = macd;
            }

            if (diff > maxHigh) {
                maxHigh = diff;
            }
            if (dea > maxHigh) {
                maxHigh = dea;
            }
            if (macd > maxHigh) {
                maxHigh = macd;
            }
        }

        // 根据最大，最小数据，确认单位数值
        double spaceValue = maxHigh - minLow;
        float lineBoundHeight = viewPort.height();
        double perPixelValue = (spaceValue / lineBoundHeight);


        int j = 0;
        // 计算指标业务数据集合
        for (int i = startIndex; i < endIndex; i++,j++) {


            // 最高价
            MACDDO macddo = macddos.get(i);

            double x = viewPort.left + (j * (StockIndexView.candleWidth + StockIndexView.candleSpace)) + StockIndexView.candleSpace;
            macddo.x = (float) x;


            double diffY = viewPort.bottom - (((macddo.diff - minLow)) / perPixelValue);
            macddo.diffY = (float) diffY;
            double deaY = viewPort.bottom - (((macddo.dea - minLow)) / perPixelValue);
            macddo.deaY = (float) deaY;

            double barY;
            if (minLow < 0) {
                // 当屏最小值是负数，则计算0线相关的数据
                zeroY = (float) (viewPort.bottom - Math.abs(minLow) / perPixelValue);
                if (macddo.bar < 0) {
                    barY = zeroY + (Math.abs(macddo.bar) / perPixelValue);
                } else {
                    barY = viewPort.bottom - (((macddo.bar - minLow)) / perPixelValue);
                }

            } else {
                barY = viewPort.bottom - (((macddo.bar - minLow)) / perPixelValue);
            }

            macddo.barY = (float) barY;

        }


        return true;
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

    Path diffPath = new Path();
    Path deaPath = new Path();
    Paint mPaint = new Paint();
    Paint deaPaint = new Paint();

    @Override
    public void drawIndex(Canvas canvas, Paint paint) {

        diffPath.reset();
        deaPath.reset();

        // 计算指标业务数据集合
        for (int i = mStartIndex; i < mEndIndex; i++) {
            // 最高价
            MACDDO macddo = macddos.get(i);

            if (i == mStartIndex) {
                diffPath.moveTo(macddo.x, macddo.diffY);
                deaPath.moveTo(macddo.x, macddo.deaY);
            } else {
                diffPath.lineTo(macddo.x, macddo.diffY);
                deaPath.lineTo(macddo.x, macddo.deaY);
            }


            if (macddo.bar < 0) {
                mPaint.setColor(Color.GREEN);
                canvas.drawLine(macddo.x, zeroY, macddo.x, macddo.barY, mPaint);
            } else {
                mPaint.setColor(Color.RED);
                canvas.drawLine(macddo.x, zeroY, macddo.x, macddo.barY, mPaint);
            }


        }

        mPaint.setColor(Color.RED);
        canvas.drawLine(mViewPort.left, zeroY, mViewPort.right, zeroY, mPaint);
        canvas.drawPath(diffPath, mPaint);
        canvas.drawPath(deaPath, deaPaint);


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
