package com.dqqdo.demo.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.dqqdo.demo.R;
import com.dqqdo.dobase.BaseActivity;
import com.dqqdo.dobase.DoLog;
import com.dqqdo.dobase.DoToast;
import com.dqqdo.dochart.ui.view.PieChartView;
import com.dqqdo.dochart.ui.view.listener.IPieClickListener;

import java.util.List;

/**
 * 作者：duqingquan
 * 时间：2017/2/21 18:16
 */
public class PieChartActivity extends BaseActivity {


    PieChartView pieChartView;

    @Override
    protected void initView() {
        pieChartView = (PieChartView) findViewById(R.id.pie_chart_view);
        pieChartView.setHasLabel(false);
        pieChartView.setAnim(true);
        pieChartView.setTextLineCircleRadius(6);
        pieChartView.setPieClickListener(new IPieClickListener() {
            @Override
            public void onItemClick(int index) {
                DoToast.shortToast(PieChartActivity.this,"当前位置:" + index);
            }
        });
    }

    @Override
    protected void initData() {


    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pie_chart;
    }
}
