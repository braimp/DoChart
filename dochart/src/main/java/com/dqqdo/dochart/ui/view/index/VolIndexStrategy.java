package com.dqqdo.dochart.ui.view.index;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;


import com.dqqdo.dochart.ui.view.stock.CandleBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * vol 指标策略
 * 作者：duqingquan
 * 时间：2017/7/21 16:23
 */
public class VolIndexStrategy implements IndexStrategy {


    // 当前选中区域的最大数值
    private long maxHigh;
    // 当前选中区域的最小数值
    private long minLow;
    // 指标数据集合，保留一屏幕
    private List<VolDO> vols;

    // Y轴描述的五档 文本
    private String[] descYStr = new String[5];

    // Y轴描述的五档 文本
    private String[] arrayDescXStr;
    private Float[] arrayDescX;

    Paint textPaint;
    Paint bgPaint;

    VolIndexStrategy(){
        textPaint = new Paint();
        textPaint.setTextSize(30);
        textPaint.setColor(Color.BLUE);

        bgPaint = new Paint();
        bgPaint.setColor(Color.RED);
    }




    class VolDO {

        private long volValue;
        private RectF volRectF;
        private String desc;


        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public long getVolValue() {
            return volValue;
        }

        public void setVolValue(long volValue) {
            this.volValue = volValue;
        }

        public RectF getVolRectF() {
            return volRectF;
        }

        public void setVolRectF(RectF volRectF) {
            this.volRectF = volRectF;
        }
    }

    @Override
    public String[] getDescYStr() {
        return descYStr;
    }


    public String[] getDescXStr() {
        return arrayDescXStr;
    }

    public Float[] getDescX() {
        return arrayDescX;
    }


    @Override
    public void drawIndex(Canvas canvas, Paint paint) {

        int volSize = vols.size();
        for (int i = 0; i < volSize; i++) {

            VolDO volDO = vols.get(i);
            RectF rectF = volDO.getVolRectF();
            canvas.drawRect(rectF, paint);

        }


    }


    public int getSelectIndex(PointF pointF) {
        int perUnitWidth = StockIndexView.candleWidth + StockIndexView.candleSpace;
        float viewDistance = pointF.x - mViewPort.left;
        int indexNum = (int) (viewDistance / perUnitWidth);
        return indexNum;
    }


    public void drawSelectIndex(Canvas canvas, Paint paint, int index) {

        if(index < 0){
            return ;
        }

        if(index >= vols.size()){
            return;
        }

        VolDO volDO = vols.get(index);
        RectF rectF = volDO.getVolRectF();
        RectF selectRectF = new RectF(rectF);
        selectRectF.left = rectF.left - 5;
        selectRectF.right = rectF.right + 5;
        selectRectF.top = rectF.top - 5;

        canvas.drawRect(selectRectF, paint);

        // 在选中的对象上层绘制数量
        RectF volRectF = new RectF();
        String volValue = String.valueOf(volDO.getVolValue() / 100);

        Rect textRect = new Rect();
        textPaint.getTextBounds(volValue,0,volValue.length(),textRect);

        volRectF.left = selectRectF.centerX() - textRect.width() / 2;
        volRectF.right = volRectF.left + textRect.width() ;
        volRectF.bottom = selectRectF.top - 50;
        volRectF.top = volRectF.bottom - textRect.height();

        RectF bgRect = new RectF();

        bgRect.left= volRectF.left - 15;
        bgRect.right = volRectF.right + 15;
        bgRect.bottom =  volRectF.bottom + 5;
        bgRect.top = volRectF.top - 5;

        canvas.drawRect(bgRect,bgPaint);

        canvas.drawText(volValue,volRectF.left,volRectF.bottom,textPaint);

        // 选中组件的底部 事件绘制
        String descDate = volDO.getDesc();

        textPaint.getTextBounds(descDate,0,descDate.length(),textRect);
        volRectF.left = selectRectF.centerX() - textRect.width() / 2;
        volRectF.right = volRectF.left + textRect.width() ;
        volRectF.top = selectRectF.bottom + 50;
        volRectF.bottom = volRectF.top + textRect.height();

        bgRect.left= volRectF.left - 15;
        bgRect.right = volRectF.right + 15;
        bgRect.bottom =  volRectF.bottom + 5;
        bgRect.top = volRectF.top - 5;

        canvas.drawRect(bgRect,bgPaint);


        canvas.drawText(descDate,volRectF.left,volRectF.bottom,textPaint);

    }


    private RectF mViewPort;

    private ArrayList<CandleBean> candles;


    @Override
    public void setData(ArrayList<CandleBean> data) {
        this.candles = data;
    }

    @Override
    public int getDataSize() {
        if(candles == null){
            return 0;
        }
        return candles.size();
    }

    @Override
    public boolean calcFormulaPoint(List<CandleBean> portData, RectF viewPort) {

        maxHigh = 0;
        minLow = 0;

        mViewPort = viewPort;

        // 计算指标公式所需要的关键点
        int dataSize = portData.size();
        vols = new ArrayList<>(dataSize);
        vols.clear();
        for (int i = 0; i < dataSize; i++) {

            CandleBean candleBean = portData.get(i);

            long vol = candleBean.getVolume();
            if (maxHigh == 0) {
                maxHigh = vol;
            } else {
                if (vol > maxHigh) {
                    maxHigh = vol;
                }
            }

            // 最低价(VOL最高级也就是最低价)
            long mostLow = vol;
            if (minLow == 0) {
                minLow = mostLow;
            } else {
                if (mostLow < minLow) {
                    minLow = mostLow;
                }
            }

        }

        // 根据最大，最小数据，确认单位数值
        long spaceValue = maxHigh;
        float lineBoundHeight = viewPort.height();
        double perPixelValue = (spaceValue * 1.0 / lineBoundHeight);


        ArrayList<String> descXStr = new ArrayList<>();
        ArrayList<Float> descX = new ArrayList<>();

        // 计算指标业务数据集合
        for (int i = 0; i < dataSize; i++) {
            // 最高价
            CandleBean candleBean = portData.get(i);
            long vol = candleBean.getVolume();

            String descDate = candleBean.getDateStr();

            VolDO volDO = new VolDO();
            volDO.setVolValue(vol);
            volDO.setDesc(descDate);
            RectF rectF = new RectF();
            rectF.left = viewPort.left + (i * (StockIndexView.candleWidth + StockIndexView.candleSpace)) + StockIndexView.candleSpace;
            rectF.right = rectF.left + StockIndexView.candleSpace;
            rectF.bottom = viewPort.bottom;
            rectF.top = (float) (viewPort.bottom - vol / perPixelValue);


            int dayNum = getDayNum(candleBean.getTime());
            // 每个月一号，绘制月线
            if (dayNum == 1) {
                String dateStr = candleBean.getDateStr();
                descXStr.add(dateStr);
                descX.add(rectF.centerX());
            }

            volDO.setVolRectF(rectF);
            vols.add(volDO);
        }


        arrayDescXStr = descXStr.toArray(new String[descXStr.size()]);
        arrayDescX = descX.toArray(new Float[descX.size()]);


        // 计算五档数值
        for (int i = 0; i < 5; i++) {
            double yValue = (maxHigh * 1.0 - perPixelValue * i) / 100;
            DecimalFormat df = new DecimalFormat("###.00");
            descYStr[i] = df.format(yValue);
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
    public String getIndexName() {
        return "VOL";
    }

    @Override
    public String getIndexFormula() {
        return "VOL = 当天成交量";
    }

}
