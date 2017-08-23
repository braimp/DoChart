package com.dqqdo.dochart.ui.view.index;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.dqqdo.dochart.ui.view.stock.CandleBean;
import com.dqqdo.dochart.util.LogUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * KDJ
 * 作者：duqingquan
 * 时间：2017/7/31 17:56
 */
public class KDJIndexStrategy extends IndexStrategy {

    KDJIndexStrategy() {

        mPaint.setColor(Color.RED);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        deaPaint.setColor(Color.BLUE);
        deaPaint.setStrokeCap(Paint.Cap.ROUND);
        deaPaint.setStyle(Paint.Style.STROKE);
        deaPaint.setStrokeWidth(5);

        jPaint.setColor(Color.GREEN);
        jPaint.setStrokeCap(Paint.Cap.ROUND);
        jPaint.setStyle(Paint.Style.STROKE);
        jPaint.setStrokeWidth(5);

    }

    /**
     * MACD指标 DATA Object
     */
    class KDJDO {

        private double kValue = 0;
        private double dValue = 0;
        private double jValue = 0;
        // 9日 （C－L9）÷（H9－L9）×100
        private double rsvValue = 0;

        /*****************绘制部分的关键数据*********************/
        private float kY;
        private float dY;
        private float jY;
        private float x;


    }




    @Override
    public void setData(ArrayList<CandleBean> data) {
        super.setData(data);
        KDJDOs = new ArrayList<>(candles.size());
        initKDJ();
    }

    /**
     * EMA需要12 和 26 天的数据，需要开始时就计算完毕
     */
    private void initKDJ() {

        KDJDOs.clear();
        int dataSize = candles.size();

        for (int i = 0; i < dataSize; i++) {

            KDJDOs.add(new KDJDO());

            CandleBean candleBean = candles.get(i);

            // most low
            long ln = 0;
            // most high
            long hn = 0;

            long close = candleBean.getClose();


            if (i < 8) {

                if (i == 0) {

                    CandleBean perBean = candles.get(i);
                    long perLow = perBean.getMostLow();
                    long perHigh = perBean.getMostHigh();

                    ln = perLow;
                    hn = perHigh;
                } else {
                    // 不足9日，则取前几日
                    for (int j = 0; j <= i; j++) {

                        CandleBean perBean = candles.get(j);
                        long perLow = perBean.getMostLow();
                        long perHigh = perBean.getMostHigh();


                        if (ln == 0 || ln > perLow) {
                            ln = perLow;
                        }

                        if (hn < perHigh) {
                            hn = perHigh;
                        }
                    }
                }
            } else {

                for (int j = i - 8; j <= i; j++) {

                    CandleBean perBean = candles.get(j);
                    long perLow = perBean.getMostLow();
                    long perHigh = perBean.getMostHigh();

                    if (ln == 0 || ln > perLow) {
                        ln = perLow;
                    }

                    if (hn < perHigh) {
                        hn = perHigh;
                    }
                }
            }


            KDJDO kdjdo = KDJDOs.get(i);
            kdjdo.rsvValue = (close - ln) * 1.0 / (hn - ln) * 100;

        }


        for (int i = 0; i < dataSize; i++) {

            CandleBean candleBean = candles.get(i);

            if (candleBean != null) {

                KDJDO kdjdo = KDJDOs.get(i);
                if (i == 0) {
                    // 若无前一日K值与D值，则可以分别用50代替
                    kdjdo.kValue = 2.0 / 3 * 50 + 1.0 / 3 * kdjdo.rsvValue;
                    kdjdo.dValue = 2.0 / 3 * 50 + 1.0 / 3 * kdjdo.kValue;
                    kdjdo.jValue = 3.0 * kdjdo.kValue - 2 * kdjdo.dValue;
                } else {
                    KDJDO preDo = KDJDOs.get(i - 1);
                    kdjdo.kValue = 2.0 / 3 * preDo.kValue + 1.0 / 3 * kdjdo.rsvValue;
                    kdjdo.dValue = 2.0 / 3 * preDo.dValue + 1.0 / 3 * kdjdo.kValue;
                    kdjdo.jValue = 3.0 * kdjdo.kValue - 2.0 * kdjdo.dValue;
                }

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

    private int mStartIndex, mEndIndex;
    ArrayList<String> descX = new ArrayList<>();
    ArrayList<Float> descXValue = new ArrayList<>();
    String[] descY = new String[5];
    ArrayList<KDJDO> KDJDOs;

    @Override
    public boolean calcFormulaPoint(int startIndex, int endIndex, RectF viewPort) {

        descX.clear();
        descXValue.clear();


        mStartIndex = startIndex;
        mEndIndex = endIndex;

        mViewPort = viewPort;
        portData = candles.subList(startIndex, endIndex);


        for (int i = mStartIndex; i < mEndIndex; i++) {

            KDJDO KDJDO = KDJDOs.get(i);

            if(i == mStartIndex){
                minLow = KDJDO.kValue;
                maxHigh = KDJDO.kValue;
            }else{
                if (KDJDO.kValue < minLow) {
                    minLow = KDJDO.kValue;
                }
                if (KDJDO.dValue < minLow) {
                    minLow = KDJDO.dValue;
                }
                if (KDJDO.jValue < minLow) {
                    minLow = KDJDO.jValue;
                }

                if (KDJDO.kValue > maxHigh) {
                    maxHigh = KDJDO.kValue;
                }
                if (KDJDO.dValue > maxHigh) {
                    maxHigh = KDJDO.dValue;
                }
                if (KDJDO.jValue > maxHigh) {
                    maxHigh = KDJDO.jValue;
                }
            }


        }

        // 根据最大，最小数据，确认单位数值
        double spaceValue = maxHigh - minLow;
        float lineBoundHeight = viewPort.height();
        double perPixelValue = (spaceValue / lineBoundHeight);


        double perYUnit = spaceValue / 5;
        // 计算五档数值
        for (int i = 0; i < 5; i++) {
            double yValue = (maxHigh * 1.0 - perYUnit * i);
            DecimalFormat df = new DecimalFormat("###.00");
            descY[i] = df.format(yValue);
        }


        int j = 0;
        // 计算指标业务数据集合
        for (int i = startIndex; i < endIndex; i++, j++) {

            // 最高价
            KDJDO KDJDO = KDJDOs.get(i);

            double x = viewPort.left + (j * (StockIndexView.candleWidth + StockIndexView.candleSpace)) + StockIndexView.candleSpace;
            KDJDO.x = (float) x;

            CandleBean candleBean = candles.get(i);
            int dayNum = getDayNum(candleBean.getTime());
            // 每个月一号，绘制月线
            if (dayNum == 1) {
                String dateStr = candleBean.getDateStr();
                descX.add(dateStr);
                descXValue.add(KDJDO.x);
            }

            double dy = viewPort.bottom - (((KDJDO.dValue - minLow)) / perPixelValue);
            KDJDO.dY = (float) dy;
            double ky = viewPort.bottom - (((KDJDO.kValue - minLow)) / perPixelValue);
            KDJDO.kY = (float) ky;
            double jy = viewPort.bottom - (((KDJDO.jValue - minLow)) / perPixelValue);
            KDJDO.jY = (float) jy;


        }


        return true;
    }

    private int getDayNum(long time) {
        // 初始化当前月份
        long monthValue = time % Long.parseLong("100000000");
        int monthNum = (int) (monthValue / 1000000);
        return monthNum;
    }

    @Override
    public String[] getDescYStr() {
        return descY;
    }

    @Override
    public String[] getDescXStr() {

        return descX.toArray(new String[descX.size()]);
    }

    @Override
    public Float[] getDescX() {
        return descXValue.toArray(new Float[descXValue.size()]);
    }


    Paint mPaint = new Paint();
    Paint deaPaint = new Paint();
    Paint jPaint = new Paint();

    @Override
    public void drawIndex(Canvas canvas, Paint paint) {


        Path dPath = new Path();
        Path kPath = new Path();
        Path jPath = new Path();

        // 计算指标业务数据集合
        for (int i = mStartIndex; i < mEndIndex; i++) {

            // 最高价
            KDJDO KDJDO = KDJDOs.get(i);


            if (i == mStartIndex) {
                dPath.moveTo(KDJDO.x, KDJDO.dY);
                kPath.moveTo(KDJDO.x, KDJDO.kY);
                jPath.moveTo(KDJDO.x, KDJDO.jY);
            } else {
                KDJDO preDo = KDJDOs.get(i - 1);
                float midX = (KDJDO.x + preDo.x) / 2;
                dPath.cubicTo(midX,preDo.dY,midX,KDJDO.dY,KDJDO.x,KDJDO.dY);
                kPath.cubicTo(midX,preDo.kY,midX,KDJDO.kY,KDJDO.x,KDJDO.kY);
                jPath.cubicTo(midX,preDo.jY,midX,KDJDO.jY,KDJDO.x,KDJDO.jY);
            }

        }


        mPaint.setColor(Color.RED);
        canvas.drawPath(dPath, mPaint);
        canvas.drawPath(kPath, deaPaint);
        canvas.drawPath(jPath, jPaint);


    }

    @Override
    public String getIndexName() {
        return "KDJ 指标（9日）";
    }

    @Override
    public String getIndexFormula() {
        return "随机数指标，分为KDJ 三条曲线";
    }

    @Override
    public int getSelectIndex(PointF pointF) {
        int perUnitWidth = StockIndexView.candleWidth + StockIndexView.candleSpace;
        float viewDistance = pointF.x - mViewPort.left;
        int indexNum = (int) (viewDistance / perUnitWidth);
        return indexNum;
    }

    @Override
    public void drawSelectIndex(Canvas canvas, Paint paint, int index) {

        if(index < 0){
            return ;
        }

        if(mStartIndex + index >= KDJDOs.size()){
            return;
        }

        KDJDO kdjdo = KDJDOs.get(mStartIndex + index);


    }
}
