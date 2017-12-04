package com.dqqdo.dochart.resolver.syntax.sentence;


import static com.dqqdo.dochart.resolver.syntax.DoConstants.EXPRESSION_DEFINE;
import static com.dqqdo.dochart.resolver.syntax.DoConstants.EXPRESSION_EQUAL;

/**
 * 作者：duqingquan
 * 时间：2017/12/1 16:11
 */
public class EvaluationSentence extends FormulaSentence {

    public EvaluationSentence(String line) {
        super(line);

        // 赋值解析 :是定义 := 是赋值
        if(line.contains(EXPRESSION_DEFINE)){
            // 定义
            String[] expressions = line.split(EXPRESSION_DEFINE);
        }else if(line.contains(EXPRESSION_EQUAL)){
            // 赋值(可能包含运算)
            String[] expressions = line.split(EXPRESSION_EQUAL);
        }

    }
}
