package com.dqqdo.demo.activity.stock;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.dqqdo.demo.R;
import com.dqqdo.dochart.ui.view.WavesView;
import com.dqqdo.dochart.ui.view.parse.IndexParseView;
import com.dqqdo.dochart.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：duqingquan
 * 时间：2017/11/27 15:59
 * @author hexun
 */
public class WaveAnimActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_anim);


        WavesView view_wave = (WavesView) findViewById(R.id.view_wave);
        view_wave.start();
    }



}
