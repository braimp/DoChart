package com.dqqdo.dochart.resolver.syntax.sentence;


import com.dqqdo.dochart.resolver.ResolverTaskDO;
import com.dqqdo.dochart.resolver.StockInfo;
import com.dqqdo.dochart.resolver.stock.StockManager;
import com.dqqdo.dochart.resolver.syntax.function.FunctionManager;
import com.dqqdo.dochart.util.LogUtil;
import com.dqqdo.dochart.util.StringUtil;

import java.util.Iterator;
import java.util.List;

import static com.dqqdo.dochart.resolver.syntax.DoConstants.EXPRESSION_DEFINE;
import static com.dqqdo.dochart.resolver.syntax.DoConstants.EXPRESSION_EQUAL;

/**
 * 赋值分句
 * 作者：duqingquan
 * 时间：2017/12/1 16:11
 * @author duqingquan
 */
public class EvaluationSentence extends FormulaSentence {

    private double value;
    private String variableName;
    private String expression;
    private  ResolverTaskDO resolverTaskDO;

    public EvaluationSentence(String line, ResolverTaskDO taskDO) {
        super(line);
        this.resolverTaskDO = taskDO;

        String[] expressions ={};
        // 赋值解析 :是定义 := 是赋值
        if(line.contains(EXPRESSION_DEFINE)){
            // 定义(自定义的图元对象)
            expressions = line.split(EXPRESSION_DEFINE);
        }else if(line.contains(EXPRESSION_EQUAL)){
            // TODO
            // 赋值
            expressions = line.split(EXPRESSION_EQUAL);
        }

        if(expressions.length > 1){
            // 至少有两部分，则合法
            variableName = expressions[0];
            expression = expressions[1];

            if(FunctionManager.getInstance().isFunc(expression)){

                // 分解时间单元
                long stockId = taskDO.getStockId();
                long startTime = taskDO.getStartTime();
                long endTime = taskDO.getEndTime();

                List<StockInfo> list = StockManager.getInstance().getStockInfos(stockId,startTime,endTime);
                Iterator iterator = list.iterator();

                while (iterator.hasNext()){
                    StockInfo stockInfo = (StockInfo) iterator.next();
                    // 是函数，则交由函数管理器处理
                    value = FunctionManager
                            .getInstance()
                            .getFuncValue(expression,stockInfo);
                    // TODO 将返回的数值，转化为DrawItem对象
                }


            }else{
                // 数值，直接计算.只接受数值
                if(StringUtil.isNumeric(expression)){
                    value = Double.parseDouble(expression);
                }else{
                    LogUtil.e("非法的表达式  expression  --- " + expression);
                }
            }

        }

    }

    /**
     * 返回表达式的结果数值
     * @return
     */
    public double getValue(){
        return value;
    }

    public String getName(){
        return variableName;
    }

}
