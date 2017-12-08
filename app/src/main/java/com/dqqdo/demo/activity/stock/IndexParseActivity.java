package com.dqqdo.demo.activity.stock;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dqqdo.demo.R;
import com.dqqdo.dochart.ui.view.parse.IndexParseView;
import com.dqqdo.dochart.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：duqingquan
 * 时间：2017/11/27 15:59
 *
 * @author hexun
 */
public class IndexParseActivity extends Activity {

    private IndexParseView indexParseView;
    private EditText etIndex;
    private Button btn_index_parse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_parse);

        indexParseView = (IndexParseView) findViewById(R.id.ipv_index_parse);
        etIndex = (EditText) findViewById(R.id.et_index);
        btn_index_parse = (Button) findViewById(R.id.btn_index_parse);

//        String formula = readAssetsTxt("formula1.do");
//
//        if(!TextUtils.isEmpty(formula)){
//            indexParseView.setFormula(formula);
//        }else{
//            LogUtil.e("formula  ----  null " + formula);
//        }

        btn_index_parse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formula = etIndex.getText().toString();
                if (!TextUtils.isEmpty(formula)) {
                    indexParseView.setFormula(formula);
                    indexParseView.execute();
                } else {
                    LogUtil.e("formula  ----  null " + formula);
                }
            }
        });

    }


    private String readAssetsTxt(String fileName) {

        try {
            //Return an AssetManager instance for your application's package
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
