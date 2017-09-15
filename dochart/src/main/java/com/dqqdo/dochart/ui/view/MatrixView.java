package com.dqqdo.dochart.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.dqqdo.dochart.R;
import com.dqqdo.dochart.util.LogUtil;


/**
 * 绘制饼状图的自定义组件
 * 作者：duqingquan
 * 时间：2017/2/14 14:36
 */
public class MatrixView extends View{


    Matrix matrix = new Matrix();
    Paint mPaint = new Paint();
    Bitmap mBitmap;
    RectF srcRectF,destRectF;


    /**
     * 组件初始化方法
     */
    private void init(Context context) {
        mBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.foo);
        matrix.preTranslate(100,100);
        matrix.preScale(0.5f,0.5f);
        matrix.setSinCos(1,1);
//        matrix.preRotate(15,0,0);
//        matrix.postRotate(45);
//        matrix.preSkew(0,0.5f);

        // 判断matrix矩阵性质
//        LogUtil.d("matrix  isIdentity  ------ " + matrix.isIdentity());
//        if(Build.VERSION.SDK_INT >= 21){
//            LogUtil.d("matrix  isAffine  ------ " + matrix.isAffine());
//        }

        // rect stay rect
        srcRectF = new RectF(20,20,120,120);
        destRectF = new RectF(20,20,120,120);

        mPaint.setColor(Color.RED);
        boolean rectStaysRect = matrix.rectStaysRect();
        LogUtil.d("rectStaysRect -----  " + rectStaysRect);
//        boolean result = matrix.setRectToRect(srcRectF,destRectF, Matrix.ScaleToFit.START);
//        LogUtil.d("result -----  " + result);


        // poly to poly
//        float[] src = {0, 0};
//        int DX = 300;
//        float[] dst = {0 + DX, 0 + DX};
//        matrix.setPolyToPoly(src, 0, dst, 0, 1);

    }


    public MatrixView(Context context) {
        super(context);
        init(context);
    }

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MatrixView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MatrixView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap,matrix,mPaint);
//        mPaint.setColor(Color.GREEN);
//        canvas.drawRect(srcRectF,mPaint);
//        mPaint.setColor(Color.BLUE);
//        canvas.drawRect(destRectF,mPaint);
    }




}
