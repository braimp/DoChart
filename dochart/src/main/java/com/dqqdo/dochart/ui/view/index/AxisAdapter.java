package com.dqqdo.dochart.ui.view.index;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 坐标轴 适配器
 * 作者：duqingquan
 * 时间：2017/7/24 16:48
 */
public class AxisAdapter {


    // 坐标轴画笔
    Paint axisPaint;
    RectF mViewPort;


    // 状态栏画笔
    Paint statusPaint = new Paint();


    public AxisAdapter(){
        axisPaint = new Paint();
        axisPaint.setColor(Color.GRAY);



        statusPaint = new Paint();
        statusPaint.setColor(Color.BLACK);
        statusPaint.setTextSize(26);
    }


    public void updateViewPort(RectF viewport){
        mViewPort = viewport;
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


    public void drawAxisDesc(float[] descY, String[] descYStr, Float[] descX, String[] descXStr, Canvas canvas){

        // 绘制Y轴描述文本
        // 绘制五档数值
        for (int i = 0; i < descY.length; i++) {
            float y = descY[i];
            String desc = descYStr[i];
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
            float y = mViewPort.bottom + textHeight;
            canvas.drawText(desc, x, y, statusPaint);
            // 绘制背景线段
            canvas.drawLine(x,mViewPort.bottom,x,mViewPort.top,statusPaint);
        }

    }


}
