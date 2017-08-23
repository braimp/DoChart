package com.dqqdo.demo.activity.stock;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dqqdo.demo.R;
import com.dqqdo.dochart.ui.view.index.StockIndexView;
import com.dqqdo.dochart.ui.view.stock.CandleBean;
import com.dqqdo.dochart.ui.view.stock.KLineView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 常用指标 功能实现
 * 作者：duqingquan
 * 时间：2017/7/11 11:36
 */
public class StockIndexActivity extends Activity {

    private StockIndexView stockIndexView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_index);

        stockIndexView = (StockIndexView) findViewById(R.id.stock_view);

        initData();

    }


    private void initData(){

        InputStream is = null;
        ArrayList<CandleBean> beans = new ArrayList<>();
        try {
            is = getAssets().open("kline.data");
            byte[] data = new byte[is.available()];

            is.read(data);
            String strData = new String(data,"UTF-8");
            JSONObject allObject = JSON.parseObject(strData);
            JSONArray arrays = allObject.getJSONArray("Data");
            int count = arrays.size();


            for(int i = 0;i < count;i++){
                JSONArray array = arrays.getJSONArray(i);
                CandleBean candleBean = new CandleBean();
                long time =  array.getLong(0);
                candleBean.setTime(time);
                candleBean.setLastClose(array.getLong(1));
                candleBean.setOpen(array.getLong(2));
                candleBean.setClose(array.getLong(3));
                candleBean.setMostHigh( array.getLong(4));
                candleBean.setMostLow( array.getLong(5));
                candleBean.setVolume(array.getLong(6));
                candleBean.setAmount(array.getLong(7));


                beans.add(candleBean);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
            }
        }
        int count = beans.size();
        for(int i = 0;i < count;i++){

            CandleBean candleBean = beans.get(i);

            long mean5 = 0;
            long mean10 = 0;
            long mean15 = 0;
            // 计算M5-M10
            if(i == 0){
                // 起始元素，直接那自己
                mean15 = mean10 = mean5 = candleBean.getClose();

            }else if(i <= 5){

                long meanValue = 0;
                //
                for(int j = 0; j < i; j++){
                    meanValue += beans.get(j).getClose();
                    if(j > 0){
                        meanValue = meanValue >> 1;
                    }
                }

                mean15 = mean10 = mean5 = meanValue;

            }else if(i <= 10){
                long meanValue = 0;
                //
                for(int j = 0; j < i; j++){
                    meanValue += beans.get(j).getClose();
                    if(j > 0){
                        meanValue = meanValue >> 1;
                    }
                    if(j == 5){
                        mean5 = meanValue;
                    }
                }
                mean15 = mean10 = meanValue;
            }else{

                long meanValue = 0;
                int j;
                int startIndex;
                if(i > 15){
                    j = i -15;
                }else{
                    j = 0;
                }

                startIndex = j;

                for(; j < i; j++){
                    meanValue += beans.get(j).getClose();
                    if(j > 0){
                        meanValue = meanValue >> 1;
                    }
                    if(j == startIndex + 5){
                        mean5 = meanValue;
                    }
                    if(j == startIndex + 10){
                        mean10 = meanValue;
                    }
                }

                mean15 = meanValue;

            }

            candleBean.setM5(mean5);
            candleBean.setM10(mean10);
            candleBean.setM15(mean15);

        }


        stockIndexView.setBeans(beans);
        stockIndexView.invalidate();
        
    }
}
