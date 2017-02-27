package com.dqqdo.dochart.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.dqqdo.dobase.DoLog;
import com.dqqdo.dochart.data.ChartValueBean;
import com.dqqdo.dochart.ui.view.listener.IPieClickListener;

import java.util.ArrayList;

/**
 * 绘制饼状图的自定义组件
 * 作者：duqingquan
 * 时间：2017/2/14 14:36
 */
public class PieChartView extends View implements View.OnTouchListener {


    // 绘图使用的主画笔对象
    private Paint mPaint;
    // 绘制背景区域
    private RectF bigRect = new RectF();

    // 绘制描述用到的line画笔对象
    private Paint mPaintLine;
    // 绘制圆心，分割线用到的画笔
    private Paint mClearPaint;
    // 擦除模式
    private PorterDuffXfermode clearMode;


    // 当前组件宽高.
    private int width, height;
    // 主绘制区域的边距控制
    private float left, top, right, bottom;
    // 圆心坐标
    private float[] center = new float[2];
    // 圆半径数值
    private float circleRadius = 0f;
    // 有效绘制区域半径
    private float radius = 0f;
    // 外圆半径（这里的外圆是用来绘制描述文字用到的虚拟圆）
    private float bigRadius = 0f;


    // 圆的绘制进度
    private float mSweep = 0;
    // 绘制单元下标
    private int index = 0;
    // 每次绘制圆的 开始弧度数值
    private float startPercent = 0;
    // 当前正在绘制单元百分比，如果动画已经绘制完完成，则为0.
    private float nowUnitPercent = 0;
    // 单元下标最大值
    private int maxIndex;


    // 每个绘制单元的百分比
    private ArrayList<Float> percents = new ArrayList<>();
    // 近圆点坐标
    private float[][] circleLineF;
    // 近圆点转折处坐标
    private float[][] circleLineT;
    // 分割线切点坐标
    private float[][] circleLineStart;



    // 响应点击事件 down事件下标
    private int downIndex = -1;
    // 响应点击事件 up事件下标
    private int upIndex = -1;

    // 对应的数据实体类
    private ArrayList<ChartValueBean> beans = new ArrayList<>();

    /***************************** 配置开关 ******************************/
    // 是否开启标签说明
    private boolean hasLabel;
    // 是否展示动画
    private boolean isAnim;
    // 带圆指示线上圆点的半径
    private float textLineCircleRadius = 6f;
    // 饼状图点击事件监听器
    private IPieClickListener pieClickListener;
    //文字底部靠近圆环短线的长度
    private float textLineCircleDistance = 60;
    // 描述文本颜色
    private int descTextColor = Color.WHITE;
    // 描述文本线段宽度
    private float descTextLineWidth = 170;

    public IPieClickListener getPieClickListener() {
        return pieClickListener;
    }

    public void setPieClickListener(IPieClickListener pieClickListener) {
        this.pieClickListener = pieClickListener;
    }

    public boolean isHasLabel() {
        return hasLabel;
    }

    public void setHasLabel(boolean hasLabel) {
        this.hasLabel = hasLabel;
    }

    public boolean isAnim() {
        return isAnim;
    }

    public void setAnim(boolean anim) {
        isAnim = anim;
        if(isAnim){
            index = 0;
        }else{
            index = maxIndex;
        }
    }

    public float getTextLineCircleRadius() {
        return textLineCircleRadius;
    }

    public void setTextLineCircleRadius(float textLineCircleRadius) {
        this.textLineCircleRadius = textLineCircleRadius;
    }

    public float getTextLineCircleDistance() {
        return textLineCircleDistance;
    }

    public void setTextLineCircleDistance(float textLineCircleDistance) {
        this.textLineCircleDistance = textLineCircleDistance;
    }

    public int getDescTextColor() {
        return descTextColor;
    }

    public void setDescTextColor(int descTextColor) {
        this.descTextColor = descTextColor;
    }

    public float getDescTextLineWidth() {
        return descTextLineWidth;
    }

    public void setDescTextLineWidth(float descTextLineWidth) {
        this.descTextLineWidth = descTextLineWidth;
    }

    /***************************** 内部实现 ******************************/


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int eventType = event.getAction();

        switch (eventType) {

            case MotionEvent.ACTION_DOWN:
                downIndex = getPointUnitIndex(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:

                upIndex = getPointUnitIndex(event.getX(), event.getY());

                if (downIndex == upIndex && upIndex >= 0) {

                    if(pieClickListener != null){
                        pieClickListener.onItemClick(upIndex);
                    }

                }

                downIndex = -1;

                break;
        }

        return true;
    }


    /**
     * 获取坐标对应的单元下标
     *
     * @return 单元下标
     */
    private int getPointUnitIndex(float x, float y) {

        // 第一步，判断当前点是否位于圆内
        double absX = (int) Math.abs((center[0] - x));
        double absY = (int) Math.abs((center[1] - y));

        double result = Math.hypot(absX, absY);
        if (result > radius) {
            // 大于半径，在圆外不作处理
            return -1;
        }

        // 小于半径，在圆内部，进一步判断角度
        double num;
        double degrees = 0;
        int quadrant = getPointQuadrant(x, y);
        if (quadrant == 1) {
            num = Math.atan2(absY, absX);
            degrees = Math.toDegrees(num);
        } else if (quadrant == 2) {
            num = Math.atan2(absX, absY);
            degrees = 90 + Math.toDegrees(num);
        } else if (quadrant == 3) {
            num = Math.atan2(absX, absY);
            degrees = 270 - Math.toDegrees(num);
        } else if (quadrant == 4) {
            num = Math.atan2(absY, absX);
            degrees = 360 - Math.toDegrees(num);
        }

        float totalDegrees = 0;
        for (int i = 0; i < percents.size(); i++) {
            float nowDegrees = percents.get(i);
            totalDegrees += nowDegrees;
            if (degrees <= totalDegrees) {
                return i;
            }
        }

        return -1;

    }


    /**
     * 返回象限 (这里返回的是几何象限，不是数学象限)
     *
     * @param x 坐标x
     * @param y 坐标y
     * @return 象限名称
     */
    private int getPointQuadrant(float x, float y) {

        if (x > center[0]) {
            if (y > center[1]) {
                return 1;
            } else {
                return 4;
            }
        } else {
            if (y > center[1]) {
                return 2;
            } else {
                return 3;
            }
        }

    }


    /**
     * 绘制单元标签
     * @param canvas 画板对象
     */
    private void drawLabel(Canvas canvas){

        mPaint.setXfermode(null);
        int colorSize = beans.size();
        for(int i = 0; i < colorSize;i++){

            ChartValueBean valueBean = beans.get(i);

            RectF rectF = new RectF();
            int perNowY = (i + 1) * 50;
            rectF.set(200,perNowY,500,perNowY + 30);
            mPaint.setColor(valueBean.getColor());
            canvas.drawRect(rectF,mPaint);

            canvas.drawText(valueBean.getName(),600,perNowY + 25,mPaintLine);
        }

    }


    /**
     * 组件初始化方法
     */
    private void init() {

        // 初始化组件相关
        initView();
        // 初始化绘制相关
        initPaint();
        // 初始化数据相关
        initData();

    }

    /**
     * 初始化组件相关
     */
    private void initView() {
        this.setOnTouchListener(this);
    }




    /**
     * 初始化绘制相关属性
     */
    private void initPaint() {

        mPaint = new Paint();
        mPaint.setColor(Color.RED);

        mClearPaint = new Paint();
        mClearPaint.setStrokeWidth(20);

        mPaintLine = new Paint();
        mPaintLine.setStyle(Paint.Style.FILL);
        mPaintLine.setStrokeWidth(5);
        mPaintLine.setColor(descTextColor);
        mPaintLine.setTextAlign(Paint.Align.CENTER);
        mPaintLine.setTextSize(30);

        clearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);



    }


    /**
     * 初始化数据对象
     */
    private void initData() {

        // 模拟假数据
        ChartValueBean ChartValueBean1 = new ChartValueBean();
        ChartValueBean1.setName("百度贴吧");
        ChartValueBean1.setValue(35);
        ChartValueBean1.setColor(Color.RED);
        beans.add(ChartValueBean1);


        ChartValueBean ChartValueBean2 = new ChartValueBean();
        ChartValueBean2.setName("QQ空间");
        ChartValueBean2.setValue(166);
        ChartValueBean2.setColor(Color.BLUE);
        beans.add(ChartValueBean2);


        ChartValueBean ChartValueBean3 = new ChartValueBean();
        ChartValueBean3.setName("新浪微博");
        ChartValueBean3.setValue(115);
        ChartValueBean3.setColor(Color.YELLOW);
        beans.add(ChartValueBean3);


        ChartValueBean ChartValueBean4 = new ChartValueBean();
        ChartValueBean4.setName("微信");
        ChartValueBean4.setColor(Color.GRAY);
        ChartValueBean4.setValue(135);
        beans.add(ChartValueBean4);


        ChartValueBean ChartValueBean5 = new ChartValueBean();
        ChartValueBean5.setName("FaceBook");
        ChartValueBean5.setValue(155);
        ChartValueBean5.setColor(Color.GREEN);
        beans.add(ChartValueBean5);


        calcPercent();

        circleLineF = new float[percents.size()][2];
        circleLineT = new float[percents.size()][2];
        circleLineStart = new float[percents.size()][2];


    }


    /**
     * 计算各元素百分比
     */
    private void calcPercent() {

        double total = 0;

        for (ChartValueBean ChartValueBean : beans) {
            total += ChartValueBean.getValue();
        }
        float availableDegree = 360;

        for (ChartValueBean ChartValueBean : beans) {
            double perValue = ChartValueBean.getValue();
            percents.add((float) ((perValue * availableDegree / total)));
        }

        maxIndex = percents.size();

    }

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    @Override
    public void onDraw(Canvas canvas) {

        // 准备数据
        width = getWidth();
        height = getHeight();

        mPaint.setXfermode(null);

        DoLog.d("dqqdo","index  ---  " + index);

        center[0] = width / 2;
        center[1] = height / 2;

        radius = width / 4.5f;
        left = center[0] - radius;
        top = center[1] - radius;
        right = center[0] + radius;
        bottom = center[1] + radius;

        circleRadius = (right - left) / 2;

        bigRect.set(left, top, right, bottom);


        // 判断是否当前单元下标位置是否有已经绘制过的区域
        float circle = 0;




        for (int i = 0; i < maxIndex; i++) {

            // 计算相关数据
            if (index < maxIndex || !isAnim) {

                nowUnitPercent = percents.get(i);
                circle += nowUnitPercent / 2;

                // 绘制单元内部圆半径
                circleLineF[i][0] = center[0] + (float) (radius * Math.cos(circle * Math.PI / 180));
                circleLineF[i][1] = center[1] + (float) (radius * Math.sin(circle * Math.PI / 180));

                // 虚拟外圈圆半径
                bigRadius = circleRadius + textLineCircleDistance;
                circleLineT[i][0] = center[0] + (float) (bigRadius * Math.cos(circle * Math.PI / 180));
                circleLineT[i][1] = center[1] + (float) (bigRadius * Math.sin(circle * Math.PI / 180));

                // 刻度转回起点度数
                circle -= nowUnitPercent / 2;
                circle += nowUnitPercent;

                // 计算分割线切点
                circleLineStart[i][0] = center[0] + (float) (radius * Math.cos(circle * Math.PI / 180));
                circleLineStart[i][1] = center[1] + (float) (radius * Math.sin(circle * Math.PI / 180));


            } else {
                nowUnitPercent = 0;
            }


            // 进行绘制
            if (i < index) {
                // 已经绘制过的部分
                float postPercent = percents.get(i);

                // 已经超过当前绘制进度，则绘制整体单元，并继续绘制下一个单元动画
                mPaint.setColor(beans.get(i).getColor());
                canvas.drawArc(bigRect, startPercent, postPercent, true, mPaint);

                startPercent += postPercent;

                // 绘制完成，绘制描述信息
                drawDescLine(canvas, i);

            } else {
                // 设置单元对应的颜色
                mPaint.setColor(beans.get(index).getColor());
                if (mSweep <= (nowUnitPercent)) {
                    // 尚未绘制完毕当前单元，则继续按照进度绘制动画
                    canvas.drawArc(bigRect, startPercent, mSweep, true, mPaint);
                    mSweep += 1;
                } else {
                    break;
                }
            }

        }

        // 点击导致的左侧影响单元下标
        int leftDownEffect;
        // 点击导致的右侧影响单元下标
        int rightDownEffect;

        if (downIndex == 0) {
            leftDownEffect = beans.size() - 1;
            rightDownEffect = 0;
        } else {
            leftDownEffect = downIndex - 1;
            rightDownEffect = downIndex;
        }

        //mPaintLine.reset();

        // 绘制分割线，因为要绘制在最上层，所有要在每帧最后绘制
        for (int i = 0; i < maxIndex; i++) {

            if (i == 0) {
                // 第一个绘制单元会与最后一个绘制单元重叠，所以要绘制最后的间隔
                drawSplitLine(canvas, circleLineStart[maxIndex - 1][0], circleLineStart[maxIndex - 1][1], -1);
            }

            // 根据单元下标分割线
            if (i < index) {

                if (i == leftDownEffect || i == rightDownEffect) {
                    // 增加点击效果
                    drawSplitLine(canvas, circleLineStart[i][0], circleLineStart[i][1], beans.get(downIndex).getColor());
                } else {
                    drawSplitLine(canvas, circleLineStart[i][0], circleLineStart[i][1], -1);
                }

            }
        }


        // 调整数据，并判断数据有效性
        if (mSweep > nowUnitPercent) {
            // 绘制单元下标增加
            // 已经超过了限制
            if (index >= maxIndex) {
                index = maxIndex;
            } else {
                index += 1;
            }
            // 单元内部绘制动画角度重置
            mSweep = 1;

        }

        startPercent = 0;

        mPaint.setColor(Color.WHITE);

        // 绘制圆心
        mPaint.setXfermode(clearMode);
        canvas.drawCircle(center[0], center[1], 80, mPaint);

        // 绘制文字标签
        if(hasLabel){
            drawLabel(canvas);
        }

        // 通知界面更新
        invalidate();


    }

    /**
     * 绘制单元之间的分割线
     *
     * @param canvas
     * @param stopX
     * @param stopY
     */
    private void drawSplitLine(Canvas canvas, float stopX, float stopY, int writeColor) {

        if (writeColor == -1) {
            mClearPaint.setXfermode(clearMode);
        } else {
            mClearPaint.setXfermode(null);
            mClearPaint.setColor(writeColor);
        }

        canvas.drawLine(center[0], center[1], stopX, stopY, mClearPaint);

    }


    /**
     * 绘制描述线段
     * @param canvas 画布对象
     * @param index  当前描述单元下标
     */
    private void drawDescLine(Canvas canvas, int pos) {

        float fx = circleLineF[pos][0];
        float fy = circleLineF[pos][1];
        float tx = circleLineT[pos][0];
        float ty = circleLineT[pos][1];


        if (percents.get(pos) == 0) {
            return;
        }

        canvas.drawCircle(fx, fy, textLineCircleRadius, mPaintLine);
        canvas.drawLine(fx, fy, tx, ty, mPaintLine);
        float temp;
        if (tx > center[0]) {
            temp = descTextLineWidth;
        } else {
            temp = -descTextLineWidth;
        }
        canvas.drawLine(tx, ty, tx + temp, ty, mPaintLine);
        canvas.drawCircle(tx + temp, ty, textLineCircleRadius, mPaintLine);
        // 绘制单元描述文字
        ChartValueBean nowBean = beans.get(pos);
        canvas.drawText(nowBean.getName() + "-" + nowBean.getValue(), tx + temp / 2, ty - 20, mPaintLine);

    }


}
