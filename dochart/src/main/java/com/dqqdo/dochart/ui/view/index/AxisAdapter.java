package com.dqqdo.dochart.ui.view.index;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextUtils;

import com.dqqdo.dochart.util.LogUtil;

/**
 * 坐标轴 适配器
 * 作者：duqingquan
 * 时间：2017/7/24 16:48
 */
public class AxisAdapter {


    // 坐标轴画笔
    Paint axisPaint;
    // 坐标轴的绘制区域
    RectF mViewPort;
    // 状态区域绘制文本
    RectF statusPort;
    // 状态栏画笔
    Paint statusPaint;

    // 指标计算策略
    private IndexStrategy volIndexStrategy;

    // Y轴描述的五档 坐标
    private float descY[];
    // Y轴描述的五档 文本
    private String descYStr[];

    // Y轴描述的五档 坐标
    private Float descX[];
    // Y轴描述的五档 文本
    private String descXStr[];


    /**
     * 构造器
     * @param strategy 指标策略
     */
    public AxisAdapter(IndexStrategy strategy){

        this.volIndexStrategy = strategy;

        axisPaint = new Paint();
        axisPaint.setColor(Color.GRAY);

        statusPaint = new Paint();
        statusPaint.setColor(Color.BLACK);
        statusPaint.setTextSize(26);

    }

    /**
     * 更新绘制区域
     * @param viewport 绘制区域范围
     */
    public void updateViewPort(RectF viewport,RectF statusRectF){
        mViewPort = viewport;
        statusPort = statusRectF;
    }


    /**
     * 当界面因素发生改变之后，响应更新界面数据
     */
    public void updateByFrameChange(){
        // 计算数据部分
        // 计算Y轴五档
        descY = new float[5];
        float perYSpace = mViewPort.height() / 5;

        for (int i = 0; i < 5; i++) {
            descY[i] = statusPort.bottom + perYSpace * i;
        }
        descYStr = volIndexStrategy.getDescYStr();
        descX = volIndexStrategy.getDescX();
        descXStr = volIndexStrategy.getDescXStr();
    }


    /**
     * 绘制坐标轴包括顶部的显示区域
     *
     * @param canvas
     */
    public void drawAxis(Canvas canvas) {

        canvas.drawLine(mViewPort.left, mViewPort.top, mViewPort.right, mViewPort.top, axisPaint);
        canvas.drawLine(mViewPort.left, mViewPort.top, mViewPort.left, mViewPort.bottom, axisPaint);
        canvas.drawLine(mViewPort.left, mViewPort.bottom, mViewPort.right, mViewPort.bottom, axisPaint);
        canvas.drawLine(mViewPort.right, mViewPort.bottom, mViewPort.right, mViewPort.top, axisPaint);

    }



    /**
     * 绘制描述区域
     * @param canvas 画板
     */
    public void drawAxisDesc(Canvas canvas){


        if(descXStr == null || descX == null || descY == null || descYStr == null){
            return ;
        }

        if(descYStr.length != descY.length){
            return ;
        }
        if(descXStr.length != descX.length){
            return ;
        }


        // 绘制Y轴描述文本
        // 绘制五档数值
        for (int i = 0; i < descY.length; i++) {
            float y = descY[i];
            String desc = descYStr[i];
            if(TextUtils.isEmpty(desc)){
                continue;
            }
            float textWidth = statusPaint.measureText(desc);
            float x = mViewPort.left - textWidth;
            canvas.drawText(desc, x, y, statusPaint);
        }

        float textHeight = statusPaint.getFontMetrics().top - statusPaint.getFontMetrics().bottom;
        textHeight = Math.abs(textHeight);

        // 绘制X轴描述文本
        for(int i = 0; i < descX.length;i++){
            float x = descX[i];
            String desc = descXStr[i];
            if(TextUtils.isEmpty(desc)){
                continue;
            }
            float y = mViewPort.bottom + textHeight;
            canvas.drawText(desc, x, y, statusPaint);
            // 绘制背景线段
            canvas.drawLine(x,mViewPort.bottom,x,mViewPort.top,statusPaint);
        }


        // 绘制描述文本
        canvas.drawText(getIndexDesc() ,statusPort.left,statusPort.bottom,statusPaint);

    }


    /**
     * 获取指标描述
     * @return 描述文本
     */
    public String getIndexDesc(){

        if(volIndexStrategy == null){
            return "";
        }

        String desc = "   " +  volIndexStrategy.getIndexName() + "： " + volIndexStrategy.getIndexFormula();
        return desc;
    }


}
