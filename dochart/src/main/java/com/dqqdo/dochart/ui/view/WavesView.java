package com.dqqdo.dochart.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author duqingquan
 * @date 2017/11/19
 */

public class WavesView extends View {

    private Handler handler;
    private Path path;
    private Paint paint;
    private float step, startX, offsetX;
    private int dir = 1;
    private boolean isAttached, isStarting;

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {

            if (!isAttached) {
                return;
            }

            step += 10;
            if (step > -startX) {
                step = step + startX;
                dir = -dir;
            }
            offsetX += 10;
            if (offsetX + step > -startX) {
                offsetX = 2 * startX + offsetX;
            }
            invalidate();
            if (isStarting) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 40);
            }
        }
    };

    public WavesView(Context context) {
        super(context);
        initView();
    }

    public WavesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WavesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttached = false;
        stop();
    }

    void initView() {
        if (handler != null) {
            return;
        }

        handler = new Handler();
        path = new Path();
        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.WHITE);
    }

    public void start() {
        isStarting = true;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 100);
    }

    public void stop() {
        isStarting = false;
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            startX = -getWidth() / 2f;
            offsetX = startX;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWave(canvas);
    }

    private void drawWave(Canvas canvas) {
        final float hw = getWidth() / 2f;
        final float hh = getHeight() / 2f;
        final float x = startX + step;

        path.reset();
        path.moveTo(x, hh * 2);
        path.lineTo(x, hh);

        float h = dir * hh + hh;
        path.quadTo(hw / 2 + x, h, x + hw, hh);
        h = -dir * hh + hh;
        path.quadTo(hw * 1.5f + x, h, hw * 2 + x, hh);
        h = dir * hh + hh;
        path.quadTo(hw * 2.5f + x, h, hw * 3 + x, hh);
        h = -dir * hh + hh;
        path.quadTo(hw * 3.5f + x, h, hw * 4 + x, hh);
        path.lineTo(hw * 4 + x, hh * 2);
        path.close();
        path.offset(0, hh * 0.25f);
        paint.setColor(0xffffffff);
        canvas.drawPath(path, paint);

        path.offset(offsetX, 0);
        paint.setColor(0x44ffffff);
        canvas.drawPath(path, paint);
    }
}
