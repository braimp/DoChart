package com.dqqdo.dochart.resolver.syntax.parser;

import com.dqqdo.dochart.resolver.ResolverTaskDO;
import com.dqqdo.dochart.resolver.syntax.function.draw.DrawFunctionUtils;
import com.dqqdo.dochart.resolver.syntax.sentence.ColorSentence;
import com.dqqdo.dochart.resolver.syntax.sentence.EvaluationSentence;
import com.dqqdo.dochart.resolver.syntax.sentence.FormulaSentence;
import com.dqqdo.dochart.resolver.syntax.sentence.ShapeSentence;

/**
 * 作者：duqingquan
 * 时间：2017/12/1 15:43
 */
public class DrawItemParser {

    private volatile static DrawItemParser instance;

    public static DrawItemParser getInstance(){
        if(instance == null){
            instance = new DrawItemParser();
        }
        return instance;
    }

    private DrawItemParser(){

    }



}
