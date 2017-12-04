package com.dqqdo.dochart.resolver.syntax.sentence;

import android.graphics.Color;

import com.dqqdo.dochart.util.LogUtil;

/**
 * 作者：duqingquan
 * 时间：2017/12/1 16:11
 */
public class ColorSentence extends FormulaSentence {

    private int contentColor;
    private String colorStr;
    private String keyWord;
    public ColorSentence(String line) {
        super(line);

        // 解析内容
        if(line.startsWith("color")){
            keyWord = "color";
        }else if(line.startsWith("COLOR")){
            keyWord = "COLOR";
        }else{
            return ;
        }

        colorStr = line.substring(keyWord.length());
        contentColor =Color.parseColor("#" + colorStr);

    }


    public int getColorInt(){
        return contentColor;
    }

    public String getColorStr(){
        return colorStr;
    }
}
