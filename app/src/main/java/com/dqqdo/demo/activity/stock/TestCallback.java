package com.dqqdo.demo.activity.stock;

import java.lang.ref.WeakReference;

/**
 * 作者：duqingquan
 * 时间：2017/9/12 13:59
 */
public abstract class TestCallback {


    public abstract void setActivity(WeakReference weakReference);

    public abstract void onFinish();

}
