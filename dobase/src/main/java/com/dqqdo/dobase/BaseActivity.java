package com.dqqdo.dobase;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * 所以直接继承 FragmentActivity 的类都应该继承该类。
 */
public abstract class BaseActivity extends FragmentActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setBackgroundDrawable(null);

        setContentView(getContentView());

        initView();

        initData();

    }




    /**
     * 初始化 View。
     */
    protected abstract void initView();

    /**
     * 对 View 进行数据填充。
     */
    protected abstract void initData();

    /**
     * 设置 Activity 布局文件。
     *
     * @return 布局文件。
     */
    protected abstract int getContentView();


    /**
     * 该方法用来代替 findViewById 方法。
     *
     * @param resId view Id。
     * @param <T>   范型。
     * @return View。
     */
    protected <T extends View> T getViewById(int resId) {
        return (T) this.findViewById(resId);
    }

    /**
     * 该方法用来代替 findViewById 方法。
     * @param resId view Id。
     * @param <T>   范型。
     * @return View。
     */
    protected <T extends Fragment> T getFragmentById(int resId) {
        return (T) this.getSupportFragmentManager().findFragmentById(resId);
    }

}
