package com.dqqdo.dochart.resolver.syntax.parser;

import com.dqqdo.dochart.resolver.ResolverTaskDO;
import com.dqqdo.dochart.resolver.StockInfo;
import com.dqqdo.dochart.resolver.syntax.sentence.ColorSentence;
import com.dqqdo.dochart.resolver.syntax.sentence.EvaluationSentence;
import com.dqqdo.dochart.resolver.syntax.sentence.FormulaSentence;
import com.dqqdo.dochart.resolver.syntax.sentence.SentenceType;
import com.dqqdo.dochart.resolver.syntax.function.draw.DrawFunctionUtils;
import com.dqqdo.dochart.resolver.syntax.sentence.ShapeSentence;
import com.dqqdo.dochart.util.LogUtil;

/**
 * 公式-分句 解析器
 * 时间：2017/12/1 15:43
 * @author duqingquan
 */
public class SentenceParser {

    public static final String COLOR_KEYWORD = "color";

    private static volatile SentenceParser instance;

    public static SentenceParser getInstance(){

        if(instance == null){
            instance = new SentenceParser();
        }
        return instance;
    }

    private SentenceParser(){

    }



    public FormulaSentence parseSentence(String line, ResolverTaskDO resolverTaskDO){

        FormulaSentence sentence;
        // 解析判断，当前分句的类型
        if(line.startsWith(COLOR_KEYWORD.toLowerCase())
                || line.startsWith(COLOR_KEYWORD.toUpperCase())){
            // 颜色
            sentence = new ColorSentence(line);
        }else if(DrawFunctionUtils.isDrawFunction(line)){
            // 绘图
            sentence = new ShapeSentence(line);
        }else{
            // 赋值
            sentence = new EvaluationSentence(line,resolverTaskDO);
        }

        return sentence;
    }

}
