package com.dqqdo.dochart.ui.view.stock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.dqqdo.dochart.util.LogUtil;


/**
 * 蜡烛图对象
 * 作者：duqingquan
 * 时间：2017/7/12 13:42
 */
public class CandleBean {

    // 时间
    private long time;
    // 前收盘
    private long lastClose;
    // 开盘价
    private long open;
    // 收盘价
    private long close;
    // 最高价
    private long mostHigh;
    // 最低价
    private long mostLow;
    // 成交量
    private long volume;
    // 成交额
    private long amount;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLastClose() {
        return lastClose;
    }

    public void setLastClose(long lastClose) {
        this.lastClose = lastClose;
    }

    public long getOpen() {
        return open;
    }

    public void setOpen(long open) {
        this.open = open;
    }

    public long getClose() {
        return close;
    }

    public void setClose(long close) {
        this.close = close;
    }

    public long getMostHigh() {
        return mostHigh;
    }

    public void setMostHigh(long mostHigh) {
        this.mostHigh = mostHigh;
    }

    public long getMostLow() {
        return mostLow;
    }

    public void setMostLow(long mostLow) {
        this.mostLow = mostLow;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }


    public String getDateStr(){
        //20150930000000
        return String.valueOf(time / 1000000);
    }

    /***********************绘制部分的相关属性**************************/
    // 颜色坐标
    private int flagColor;
    // 蜡烛芯顶部坐标
    private PointF wickTopPoint;
    // 蜡烛芯顶部坐标
    private PointF wickBottomPoint;
    // 蜡烛柄顶部坐标（左上角）
    private PointF holderLeftTopPoint;
    // 蜡烛柄底部坐标（右下角）
    private PointF holderRightBottomPoint;


    public int getFlagColor() {
        return flagColor;
    }

    public void setFlagColor(int flagColor) {
        this.flagColor = flagColor;
    }

    public PointF getWickTopPoint() {
        return wickTopPoint;
    }

    public void setWickTopPoint(PointF wickTopPoint) {
        this.wickTopPoint = wickTopPoint;
    }

    public PointF getWickBottomPoint() {
        return wickBottomPoint;
    }

    public void setWickBottomPoint(PointF wickBottomPoint) {
        this.wickBottomPoint = wickBottomPoint;
    }

    public PointF getHolderLeftTopPoint() {
        return holderLeftTopPoint;
    }

    public void setHolderLeftTopPoint(PointF holderLeftTopPoint) {
        this.holderLeftTopPoint = holderLeftTopPoint;
    }

    public PointF getHolderRightBottomPoint() {
        return holderRightBottomPoint;
    }

    public void setHolderRightBottomPoint(PointF holderRightBottomPoint) {
        this.holderRightBottomPoint = holderRightBottomPoint;
    }



    /**
     * 绘制
     * @param canvas
     * @param paint
     */
    public void drawSelf(Canvas canvas, Paint paint){
        // 脏刷新，不能做其他操作
        paint.setColor(flagColor);
        // rect
        RectF rectF = new RectF();
        rectF.left = holderLeftTopPoint.x;
        rectF.top = holderLeftTopPoint.y;
        rectF.bottom = holderRightBottomPoint.y;
        rectF.right = holderRightBottomPoint.x;
        canvas.drawRect(rectF,paint);
        float startX = wickTopPoint.x;
        canvas.drawLine(startX,wickTopPoint.y,startX,wickBottomPoint.y,paint);
    }

    /**
     * 输出日志描述当前属性
     */
    public void describeSelf(){
        LogUtil.d("----------------------------------- ");
        LogUtil.d("time === " + time);
        LogUtil.d("wickTopPoint === " + wickTopPoint.toString());
        LogUtil.d("wickBottomPoint === " + wickBottomPoint.toString());
        LogUtil.d("holderLeftTopPoint === " + holderLeftTopPoint.toString());
        LogUtil.d("holderRightBottomPoint === " + holderRightBottomPoint.toString());

    }


    /**
     * 判断一个点是否属于当前的蜡烛图元
     * @param pointF 点坐标
     * @return 判断结果
     */
    public boolean isContains(PointF pointF){

        if(pointF.x >= holderLeftTopPoint.x && pointF.x < holderRightBottomPoint.x){
            return true;
        }
        return false;
    }

    /**
     * 绘制选中的焦点线段
     * @param canvas
     * @param paint
     * @param startX
     * @param endX
     * @param startY
     * @param endY
     */
    public void drawSelectedLine(Canvas canvas, Paint paint, float startX, float endX, float startY, float endY){
        if(flagColor == Color.RED){
            canvas.drawLine(startX,holderLeftTopPoint.y,endX,holderLeftTopPoint.y,paint);
        }else{
            canvas.drawLine(startX,holderRightBottomPoint.y,endX,holderRightBottomPoint.y,paint);
        }
        canvas.drawLine(wickTopPoint.x,startY,wickTopPoint.x,endY,paint);
    }

    public float getFocusY(){
        if(flagColor == Color.RED){
            return holderLeftTopPoint.y;
        }else{
            return holderRightBottomPoint.y;
        }
    }

    /******************** M5-M10等非自有属性部分  ****************************/

    private long m5;
    private long m10;
    private long m15;

    private PointF m5Point;
    private PointF m10Point;
    private PointF m15Point;


    public PointF getM5Point() {
        return m5Point;
    }

    public void setM5Point(PointF m5Point) {
        this.m5Point = m5Point;
    }

    public PointF getM10Point() {
        return m10Point;
    }

    public void setM10Point(PointF m10Point) {
        this.m10Point = m10Point;
    }

    public PointF getM15Point() {
        return m15Point;
    }

    public void setM15Point(PointF m15Point) {
        this.m15Point = m15Point;
    }

    public long getM5() {
        return m5;
    }

    public void setM5(long m5) {
        this.m5 = m5;
    }

    public long getM10() {
        return m10;
    }

    public void setM10(long m10) {
        this.m10 = m10;
    }

    public long getM15() {
        return m15;
    }

    public void setM15(long m15) {
        this.m15 = m15;
    }

}
