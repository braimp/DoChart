package com.dqqdo.demo.activity.stock;

import com.dqqdo.demo.activity.MatrixActivity;
import com.dqqdo.dochart.util.LogUtil;

/**
 * 作者：duqingquan
 * 时间：2017/9/12 11:25
 */
public class TestThread extends Thread {

    TestCallback mCallback;

    public TestThread(TestCallback callback){
        this.mCallback = callback;
    }

    @Override
    public void run() {

        for(int i = 0; i < 10; i++){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            LogUtil.d(" i  ===  " + i);
        }

        if(mCallback != null){
            mCallback.onFinish();
        }

    }

}
