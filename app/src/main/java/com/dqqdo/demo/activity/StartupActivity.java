package com.dqqdo.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dqqdo.demo.R;
import com.dqqdo.demo.activity.stock.DoStockActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartupActivity extends AppCompatActivity {

    @Bind(R.id.btn_pie_test)
    Button mBtnPieTest;
    @Bind(R.id.btn_bar_test)
    Button mBtnBarTest;
    @Bind(R.id.btn_line_test)
    Button mBtnLineTest;
    @Bind(R.id.btn_KLine_test)
    Button btnKLineTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_pie_test, R.id.btn_bar_test,R.id.btn_line_test,R.id.btn_KLine_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pie_test:
                Intent pieIntent = new Intent(StartupActivity.this, PieChartActivity.class);
                startActivity(pieIntent);
                break;
            case R.id.btn_bar_test:
                Intent barIntent = new Intent(StartupActivity.this, BarChartActivity.class);
                startActivity(barIntent);
                break;
            case R.id.btn_line_test:
                Intent lineIntent = new Intent(StartupActivity.this, LineChartActivity.class);
                startActivity(lineIntent);
                break;
            case R.id.btn_KLine_test:
                Intent klineIntent = new Intent(StartupActivity.this, DoStockActivity.class);
                startActivity(klineIntent);
                break;

        }
    }


}
