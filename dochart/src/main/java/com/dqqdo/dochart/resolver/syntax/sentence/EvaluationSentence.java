package com.dqqdo.dochart.resolver.syntax.sentence;


import com.dqqdo.dochart.resolver.syntax.ExpressParser;
import com.dqqdo.dochart.util.LogUtil;

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
            // 定义(自定义的图元对象)
            String[] expressions = line.split(EXPRESSION_DEFINE);
        }else if(line.contains(EXPRESSION_EQUAL)){
            // TODO
            // 赋值
            String[] expressions = line.split(EXPRESSION_EQUAL);
            if(expressions.length > 1){
                // 至少有两部分，则合法
                String variableName = expressions[0];
                String expression = expressions[1];
                double value = ExpressParser.getInstance().getExpressionValue(expression);
                LogUtil.d(expression);
                LogUtil.d("" + value);
            }else{
                // 非法，中断
            }
        }

    }

    /**
     * 返回表达式的结果数值
     * @return
     */
    public double getValue(){
        return 0.0;
    }

}
