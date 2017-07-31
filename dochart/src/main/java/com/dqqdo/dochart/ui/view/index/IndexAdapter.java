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
 * 指标数据 适配器
 * 作者：duqingquan
 * 时间：2017/7/21 16:44
 */
public class IndexAdapter {

    // 指标算法策略
    private VolIndexStrategy volIndexStrategy;
    // 当屏数据
    private List<CandleBean> portData;
    // 视窗
    RectF mViewPort;


    public IndexAdapter(VolIndexStrategy strategy){
        this.volIndexStrategy = strategy;
    }

    /**
     * 更新窗口信息
     * @param viewport 可视区域
     */
    public void updateViewPort(RectF viewport){
        mViewPort = viewport;
    }

    /**
     * 更新区域数据
     * @param startIndex 开始选中位置坐标
     * @param endIndex   结束坐标
     */
    public void updateByFrameChange(int startIndex, int endIndex){

        ArrayList<CandleBean> allData = volIndexStrategy.getData();

        if(allData != null && allData.size() > 0){
            portData = allData.subList(startIndex,endIndex);
            if(mViewPort != null){
                volIndexStrategy.calcFormulaPoint(portData,mViewPort);
            }
        }
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
