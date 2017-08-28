package com.dqqdo.dochart.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.dqqdo.dochart.R;


/**
 * 绘制饼状图的自定义组件
 * 作者：duqingquan
 * 时间：2017/2/14 14:36
 */
public class MatrixView extends View{


    Matrix matrix = new Matrix();
    Paint mPaint = new Paint();
    Bitmap mBitmap;

    /**
     * 组件初始化方法
     */
    private void init(Context context) {
        mBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.foo);
        matrix.preScale(0.5f,0.5f);
        matrix.preTranslate(100,100);

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

    }




}
