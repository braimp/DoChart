package com.dqqdo.dochart.ui.view.stock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 指标效果 加载组件
 * Created by duqingquan on 2017/7/5.
 */
final public class StockArrowView extends View {

    /********************** 配置量***********************/
    // 组件的背景颜色
    private int bgColor = Color.WHITE;

    // 主题图案的第一颜色
    private int firstIndexColor = Color.GREEN;
    // 主题图案的第二颜色
    private int secondIndexColor = Color.RED;

    // 箭头主体宽度，会动态改变
    private int lineWidth = 20;
    // 组件整体宽高
    private int width, height;
    // 箭头长度，会动态改变
    private int arrowLength = 50;
    // 箭头宽度会动态改变
    private int arrowWidth = 10;

    // 设置动画速度，这里需要注意不要设置边界数字(0-20)
    private int animSpeed = 5;
    // TODO 这里还是有问题，需要考虑小于整百的组件宽度，但是目前满足需求了。
    // 色块动态速度，不要这里边界数字(100-500 整百数字)
    private int rectAnimSpeed = 200;

    /*********************************************/
    // 动画下标
    private int animIndex = 100;
    // 绘制过程中用到的主要画笔
    private Paint mPaint;
    private Paint candlePaint;
    // 用来擦除的画笔
    private Paint erasurePaint;

    // 开始点位置
    PointF startPoint = new PointF();
    // 节点一位置
    PointF nodeOnePoint = new PointF();
    // 节点二位置
    PointF nodeTwoPoint = new PointF();
    // 结束点位置
    PointF endPoint = new PointF();
    // 第三条线段的边界左侧位置点
    PointF lineStartLeft = new PointF();
    // 第三条线段的边界右侧位置点
    PointF lineStartRight = new PointF();

    // 直线一公式
    private float k1, b1;
    // 直线二公式
    private float k2, b2;
    // 直线三公式
    private float k3, b3;
    // 直线四公式，这里指的是垂直于直线三的虚拟线段，用来绘制从宽变窄效果
    private float k4, b4;

    // 箭头尖端坐标
    PointF arrowEndPoint = new PointF();
    // 箭头左侧坐标
    PointF arrowLeftPoint = new PointF();
    // 箭头的右侧坐标
    PointF arrowRightPoint = new PointF();

    // 动画左上角红色标示，默认是第一个色块
    private RectF redRect = new RectF();
    // 动画左上角绿色标示，默认是第二个色块
    private RectF greenRect = new RectF();

    // 变窄效果的,两侧X坐标，总体变化量
    float animPathX;


    public StockArrowView(Context context) {
        super(context);
        init();
    }

    public StockArrowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StockArrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

    }

    private void initPaint() {

        mPaint = new Paint();
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setColor(Color.BLUE);

        candlePaint = new Paint();
        candlePaint.setStrokeWidth(5);
        candlePaint.setColor(Color.BLACK);

        erasurePaint = new Paint();
        erasurePaint.setStrokeWidth(lineWidth);
        erasurePaint.setStrokeCap(Paint.Cap.SQUARE);
        erasurePaint.setColor(Color.WHITE);


        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 准备数据
        width = getWidth();
        height = getHeight();

        initPoint();
        initPaint();
    }


    /**
     * 初始化关键点位置坐标（如果需要改变图案形状，在这里修改）
     */
    private void initPoint() {

        rectAnimSpeed = width / 4;

        lineWidth = width / 30;
        arrowLength = width / 20;
        arrowWidth = arrowLength / 5;

        greenRect.set((float) (width * 0.15), (float) (width * 0.5), (float) (width * 0.18), (float) (width * 0.6));
        redRect.set((float) (width * 0.21), (float) (width * 0.5), (float) (width * 0.24), (float) (width * 0.6));

        // 各关键点的位置
        startPoint.set((float) (width * 0.15), (float) (width * 0.9));
        nodeOnePoint.set((float) (width * 0.4), (float) (width * 0.7));
        nodeTwoPoint.set((float) (width * 0.6), (float) (width * 0.8));
        endPoint.set((float) (width * 0.85), (float) (width * 0.6));

        // 计算三段直线公式
        float[] lineOne = calcLineFormula(startPoint, nodeOnePoint);
        k1 = lineOne[0];
        b1 = lineOne[1];

        float[] lineTwo = calcLineFormula(nodeOnePoint, nodeTwoPoint);
        k2 = lineTwo[0];
        b2 = lineTwo[1];

        float[] lineThree = calcLineFormula(nodeTwoPoint, endPoint);
        k3 = lineThree[0];
        b3 = lineThree[1];

        // 箭头宽高，这里可以动态设置
        arrowEndPoint.x = endPoint.x + arrowLength;
        arrowEndPoint.y = -(k3 * arrowEndPoint.x + b3);

        k4 = -1 / k3;
        b4 = -endPoint.y - k4 * endPoint.x;

        arrowLeftPoint.x = endPoint.x - arrowWidth;
        arrowLeftPoint.y = -(k4 * arrowLeftPoint.x + b4);
        arrowRightPoint.x = endPoint.x + arrowWidth;
        arrowRightPoint.y = -(k4 * arrowRightPoint.x + b4);

        animIndex = (int) startPoint.x;

    }

    /**
     * 传说中的"两点式"直线公式
     *
     * @return y = kx + b
     */
    private float[] calcLineFormula(PointF pointOne, PointF pointTwo) {
        float k, b;
        k = (-pointTwo.y - -pointOne.y) / (pointTwo.x - pointOne.x);
        b = -pointOne.y - k * pointOne.x;
        return new float[]{k, b};
    }


    @Override
    protected void onDraw(Canvas canvas) {

        // 刷底色
        canvas.drawColor(bgColor);

        // 刷图案背景
        mPaint.setColor(firstIndexColor);
        canvas.drawLine(startPoint.x, startPoint.y, nodeOnePoint.x, nodeOnePoint.y, mPaint);
        canvas.drawLine(startPoint.x - 10, startPoint.y, startPoint.x + lineWidth, startPoint.y, erasurePaint);
        canvas.drawLine(nodeOnePoint.x, nodeOnePoint.y, nodeTwoPoint.x, nodeTwoPoint.y, mPaint);

        calcThirdLineStart();
        drawCompleteThirdLine(canvas);


        // 刷动画
        if (animIndex < endPoint.x) {
            animIndex += animSpeed;
        }

        drawColorRect(canvas);
        mPaint.setColor(secondIndexColor);


        if (animIndex < nodeOnePoint.x) {
            float tempY = (k1 * animIndex + b1);
            canvas.drawLine(startPoint.x, startPoint.y, animIndex, -tempY, mPaint);
            canvas.drawLine(startPoint.x - 10, startPoint.y, startPoint.x + lineWidth, startPoint.y, erasurePaint);
        } else {
            canvas.drawLine(startPoint.x, startPoint.y, nodeOnePoint.x, nodeOnePoint.y, mPaint);
            canvas.drawLine(startPoint.x - 10, startPoint.y, startPoint.x + lineWidth, startPoint.y, erasurePaint);

            // 剩下的节点
            if (animIndex < nodeTwoPoint.x) {
                float tempY = (k2 * animIndex + b2);
                canvas.drawLine(nodeOnePoint.x, nodeOnePoint.y, animIndex, -tempY, mPaint);
            } else {
                canvas.drawLine(nodeOnePoint.x, nodeOnePoint.y, nodeTwoPoint.x, nodeTwoPoint.y, mPaint);

                if (animIndex < endPoint.x) {

                    // 动画份数
                    int animFrameNum = (int) (endPoint.x - nodeTwoPoint.x);
                    // 总的变化量
                    float animLineWidth = animPathX - 5;
                    float perAnimLineNum = animLineWidth / animFrameNum;
                    int nowFrameIndex = (int) (animIndex - nodeTwoPoint.x);

                    float tempY = (k3 * animIndex + b3);

                    float lineK = -1 / k3;
                    float lineB = tempY - lineK * animIndex;

                    PointF lineLeft = new PointF();
                    PointF lineRight = new PointF();
                    lineLeft.x = animIndex - (10 - perAnimLineNum * nowFrameIndex);
                    lineLeft.y = -(lineLeft.x * lineK + lineB);

                    lineRight.x = animIndex + (10 - perAnimLineNum * nowFrameIndex);
                    lineRight.y = -(lineRight.x * lineK + lineB);


                    Path linePath = new Path();

                    linePath.moveTo(lineStartLeft.x, lineStartLeft.y);
                    linePath.lineTo(lineStartRight.x, lineStartRight.y);
                    linePath.lineTo(lineRight.x, lineRight.y);
                    linePath.lineTo(lineLeft.x, lineLeft.y);

                    canvas.drawPath(linePath, mPaint);

                } else {
                    drawCompleteThirdLine(canvas);
                }
            }

        }


        if (animIndex > endPoint.x) {
            animIndex = (int) startPoint.x;
        }
        invalidate();


    }

    /**
     * 绘制色块
     * @param canvas
     */
    private void drawColorRect(Canvas canvas){

        canvas.drawLine(redRect.centerX(),redRect.bottom + 10,redRect.centerX(),redRect.top - 10,candlePaint);
        canvas.drawLine(greenRect.centerX(),greenRect.bottom + 10,greenRect.centerX(),greenRect.top - 10,candlePaint);

        // 红绿色块交替
        if (animIndex % rectAnimSpeed > rectAnimSpeed / 2) {

            mPaint.setColor(Color.RED);
            canvas.drawRect(redRect, mPaint);

            mPaint.setColor(Color.GREEN);
            canvas.drawRect(greenRect, mPaint);


        } else {

            mPaint.setColor(Color.GREEN);
            canvas.drawRect(redRect, mPaint);

            mPaint.setColor(Color.RED);
            canvas.drawRect(greenRect, mPaint);

        }
    }

    /**
     * 计算第三条线段的开始相关数据(因为这里涉及从宽变窄的效果，多处用到，单独列出来，方便修改)
     */
    private void calcThirdLineStart(){

        // 绘制最后一段，从宽到窄的特效
        float startLineK = -1 / k3;
        float startLineB = -nodeTwoPoint.y - startLineK * nodeTwoPoint.x;


        // 计算path起点x坐标
        animPathX = (float) (Math.sqrt(Math.pow(lineWidth / 2, 2) / (Math.pow(startLineK, 2) + 1)));

        lineStartLeft.x = nodeTwoPoint.x - animPathX;
        lineStartLeft.y = -(lineStartLeft.x * startLineK + startLineB);

        lineStartRight.x = nodeTwoPoint.x + animPathX;
        lineStartRight.y = -(lineStartRight.x * startLineK + startLineB);

    }


    /**
     * 绘制完整的第三条线段(因为这里涉及从宽变窄的效果，多处用到，单独列出来，方便修改)
     */
    private void drawCompleteThirdLine(Canvas canvas){


        float tempY = (k3 * endPoint.x + b3);
        float lineK = -1 / k3;
        float lineB = tempY - lineK * endPoint.x;

        PointF lineLeft = new PointF();
        PointF lineRight = new PointF();
        lineLeft.x = endPoint.x - 5;
        lineLeft.y = -(lineLeft.x * lineK + lineB);

        lineRight.x = endPoint.x + 5;
        lineRight.y = -(lineRight.x * lineK + lineB);

        Path linePath = new Path();

        linePath.moveTo(lineStartLeft.x, lineStartLeft.y);
        linePath.lineTo(lineStartRight.x, lineStartRight.y);
        linePath.lineTo(lineRight.x, lineRight.y);
        linePath.lineTo(lineLeft.x, lineLeft.y);

        canvas.drawPath(linePath, mPaint);


        Path arrowPath = new Path();
        arrowPath.moveTo(arrowEndPoint.x, arrowEndPoint.y);
        arrowPath.lineTo(arrowLeftPoint.x, arrowLeftPoint.y);
        arrowPath.lineTo(arrowRightPoint.x, arrowRightPoint.y);

        canvas.drawPath(arrowPath, mPaint);

    }
}
