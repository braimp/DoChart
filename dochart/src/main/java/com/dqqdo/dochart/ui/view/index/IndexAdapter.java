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
 * 指标适配器
 * 作者：duqingquan
 * 时间：2017/7/21 16:44
 */
public class IndexAdapter {


    private VolIndexStrategy volIndexStrategy;
    private ArrayList<CandleBean> candles;
    private List<CandleBean> portData;

    RectF mViewPort;

    public IndexAdapter(VolIndexStrategy strategy){
        this.volIndexStrategy = strategy;
    }


    public void setData(ArrayList<CandleBean> data){
        this.candles = data;
    }


    public void updateViewPort(RectF viewport){
        mViewPort = viewport;
    }

    public void updateArea(int startIndex,int endIndex){

        portData = candles.subList(startIndex,endIndex);
        if(mViewPort != null){
            volIndexStrategy.calcFormulaPoint(portData,mViewPort);
        }

    }


    public String[] getDescYStr(){
        return volIndexStrategy.getDescYStr();
    }


    public String[] getDescXStr(){
        return volIndexStrategy.getDescXStr();
    }


    public Float[] getDescX(){
        return volIndexStrategy.getDescX();
    }

    public void drawIndex(Canvas canvas, Paint paint){
        volIndexStrategy.drawIndex(canvas, paint);
    }


    public void drawSelectIndex(Canvas canvas, Paint paint, PointF pointF){
        // 首先根据pointF 查找数字下标
        int indexNum = volIndexStrategy.getSelectIndex(pointF);
        volIndexStrategy.drawSelectIndex(canvas,paint,indexNum);
    }






}
