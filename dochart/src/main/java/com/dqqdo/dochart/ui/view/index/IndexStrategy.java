package com.dqqdo.dochart.ui.view.index;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.dqqdo.dochart.ui.view.stock.CandleBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：duqingquan
 * 时间：2017/7/21 17:04
 */
public abstract class IndexStrategy {

    // 数据集合
    ArrayList<CandleBean> candles;


    /**
     * 设置集合数据
     * @param data 数据集合
     */
    public void setData(ArrayList<CandleBean> data){
        this.candles = data;
    }

    /**
     * 获取数据集合
     * @return 数据集合
     */
    public ArrayList<CandleBean> getData(){
        return candles;
    }


    /**
     * 计算公式关键点
     * @return
     */
    abstract boolean calcFormulaPoint(int startIndex, int endIndex, RectF viewPort);

    /**
     *  获取Y轴描述字符串
     * @return 字符串描述数组
     */
    abstract String[] getDescYStr();

    /**
     * 获取x轴描述文本
     * @return 文本数字
     */
    abstract String[] getDescXStr();

    /**
     * 获取x轴描述文本对应坐标
     * @return x坐标数组
     */
    abstract Float[] getDescX();


    /**
     * 绘制指标数据
     * @param canvas 画板
     * @param paint 画笔
     */
    abstract void drawIndex(Canvas canvas, Paint paint);


    /**
     * 获取指标名称
     * @return 指标名称
     */
    abstract String getIndexName();


    /**
     * 获取指标公式
     * @return 指标公式
     */
    abstract String getIndexFormula();

    /**
     * 获取选中的指标
     * @return
     */
    abstract int getSelectIndex(PointF pointF);


    abstract void drawSelectIndex(Canvas canvas, Paint paint, int index);

}
