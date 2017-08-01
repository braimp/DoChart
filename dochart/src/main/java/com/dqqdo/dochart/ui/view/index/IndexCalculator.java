package com.dqqdo.dochart.ui.view.index;

import com.dqqdo.dochart.util.LogUtil;

/**
 * 作者：duqingquan
 * 时间：2017/7/31 18:50
 */
public class IndexCalculator {


    /**
     * 获取EMA(Exponential Moving Average)
     * @return ema (12-26)
     */
    public static long getEma(double nowClose,double preEma,double dayNum){
        //平滑系数w
        double w =  2 / (dayNum + 1);
        double value = (w * (nowClose * 1.0) + (preEma * 1.0) * (1 - w));
        long ema = (long)value;
        return ema;
    }



}
