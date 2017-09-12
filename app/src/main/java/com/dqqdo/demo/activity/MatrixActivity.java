package com.dqqdo.demo.activity;

import com.dqqdo.demo.R;
import com.dqqdo.demo.activity.stock.TestCallback;
import com.dqqdo.demo.activity.stock.TestThread;
import com.dqqdo.dobase.BaseActivity;
import com.dqqdo.dochart.util.LogUtil;

import java.lang.ref.WeakReference;

/**
 * 作者：duqingquan
 * 时间：2017/2/21 18:16
 */
public class MatrixActivity extends BaseActivity {

    int tag = 10;

    @Override
    protected void initView() {

    }

    static TestCallback mCallback = new TestCallback() {
        WeakReference<MatrixActivity> mWeakReference;

        @Override
        public void setActivity(WeakReference weakReference) {
            mWeakReference = weakReference;
        }

        @Override
        public void onFinish() {
            LogUtil.d("111111111111"  + mWeakReference.get());
        }
    };


    @Override
    protected void initData() {


        mCallback.setActivity(new WeakReference(MatrixActivity.this));

        new TestThread(mCallback).start();

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_matrix;
    }

    @Override
    protected void onDestroy() {
        LogUtil.d("onDestroy  -------");
        super.onDestroy();
    }
}
