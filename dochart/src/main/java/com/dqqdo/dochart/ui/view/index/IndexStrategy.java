package com.dqqdo.dochart.ui.view.index;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.dqqdo.dochart.ui.view.stock.CandleBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：duqingquan
 * 时间：2017/7/21 17:04
 */
public interface IndexStrategy {


    /**
     * 设置集合数据
     * @param candles 数据集合
     */
    void setData(ArrayList<CandleBean> candles);

    /**
     * 获取数据集合
     * @return 数据集合
     */
    ArrayList<CandleBean> getData();


    /**
     * 计算公式关键点
     * @return
     */
    boolean calcFormulaPoint(List<CandleBean> portData, RectF viewPort);

    /**
     *  获取Y轴描述字符串
     * @return 字符串描述数组
     */
    String[] getDescYStr();

    /**
     * 获取x轴描述文本
     * @return 文本数字
     */
    String[] getDescXStr();

    /**
     * 获取x轴描述文本对应坐标
     * @return x坐标数组
     */
    Float[] getDescX();


    /**
     * 绘制指标数据
     * @param canvas 画板
     * @param paint 画笔
     */
    void drawIndex(Canvas canvas, Paint paint);


    /**
     * 获取指标名称
     * @return 指标名称
     */
    String getIndexName();


    /**
     * 获取指标公式
     * @return 指标公式
     */
    String getIndexFormula();




}
