package com.dqqdo.demo.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dqqdo.demo.R;
import com.dqqdo.demo.activity.stock.BasicKLineActivity;
import com.dqqdo.demo.activity.stock.IndexParseActivity;
import com.dqqdo.demo.activity.stock.StockIndexActivity;
import com.dqqdo.demo.activity.stock.WaveAnimActivity;
import com.dqqdo.dochart.util.LogUtil;

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
    @Bind(R.id.btn_stock_index)
    Button btnStockIndex;

    @Bind(R.id.btn_index_parse)
    Button btnIndexParse;

    @Bind(R.id.btn_wave_anim)
    Button btnWaveAnim;



    @Bind(R.id.btn_anim)
    Button btnAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



    }

    @OnClick({R.id.btn_pie_test, R.id.btn_bar_test,R.id.btn_line_test,R.id.btn_KLine_test
            ,R.id.btn_stock_index,R.id.btn_anim,R.id.btn_matrix,R.id.btn_index_parse
            ,R.id.btn_wave_anim})
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
                Intent klineIntent = new Intent(StartupActivity.this, BasicKLineActivity.class);
                startActivity(klineIntent);
                break;
            case R.id.btn_stock_index:
                Intent indexIntent = new Intent(StartupActivity.this, StockIndexActivity.class);
                startActivity(indexIntent);
                break;
            case R.id.btn_anim:
                Intent animIntent = new Intent(StartupActivity.this, LoadAnimActivity.class);
                startActivity(animIntent);
                break;
            case R.id.btn_matrix:
                Intent matrixIntent = new Intent(StartupActivity.this, MatrixActivity.class);
                startActivity(matrixIntent);
                break;
            case R.id.btn_index_parse:
                Intent indexParseIntent = new Intent(StartupActivity.this, IndexParseActivity.class);
                startActivity(indexParseIntent);
                break;
            case R.id.btn_wave_anim:
                Intent waveIntent = new Intent(StartupActivity.this, WaveAnimActivity.class);
                startActivity(waveIntent);
                break;

            default:
                break;

        }
    }


}
