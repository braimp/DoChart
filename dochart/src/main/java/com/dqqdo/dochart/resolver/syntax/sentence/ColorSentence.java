package com.dqqdo.dochart.resolver.syntax.sentence;

import android.graphics.Color;

import com.dqqdo.dochart.util.LogUtil;

import static com.dqqdo.dochart.resolver.syntax.parser.SentenceParser.COLOR_KEYWORD;

/**
 * 定义-颜色分句
 * 时间：2017/12/1 16:11
 * @author duqingquan
 */
public class ColorSentence extends FormulaSentence {

    private int contentColor;
    private String colorStr;
    private String keyWord;
    public ColorSentence(String line) {

        super(line);

        // 解析内容
        if(line.startsWith(COLOR_KEYWORD)){
            keyWord = COLOR_KEYWORD;
        }else if(line.startsWith(COLOR_KEYWORD.toUpperCase())){
            keyWord = COLOR_KEYWORD;
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


    @Override
    public String toString() {
        return "ColorSentence{" +
                "contentColor=" + contentColor +
                ", colorStr='" + colorStr + '\'' +
                ", keyWord='" + keyWord + '\'' +
                '}';
    }
}
