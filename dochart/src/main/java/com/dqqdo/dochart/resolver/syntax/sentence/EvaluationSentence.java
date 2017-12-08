package com.dqqdo.dochart.resolver.syntax.sentence;


import android.graphics.RectF;

import com.dqqdo.dochart.resolver.DoIndexResolver;
import com.dqqdo.dochart.resolver.ResolverTaskDO;
import com.dqqdo.dochart.resolver.StockInfo;
import com.dqqdo.dochart.resolver.ViewPortInfo;
import com.dqqdo.dochart.resolver.stock.StockManager;
import com.dqqdo.dochart.resolver.syntax.function.FunctionManager;
import com.dqqdo.dochart.util.LogUtil;
import com.dqqdo.dochart.util.StringUtil;

import java.util.ArrayList;
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

    /**
     * 公式返回的一组数据
     */
    List<Double> values = new ArrayList<>();

    public EvaluationSentence(String line, ResolverTaskDO resolverTaskDO) {
        super(line);

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

            LogUtil.d("expression  ---  " + expression);

            if(FunctionManager.getInstance().isFunc(expression)){

                long stockId = resolverTaskDO.getStockId();
                long startTime = resolverTaskDO.getStartTime();
                long endTime = resolverTaskDO.getEndTime();

                values.clear();
                List<StockInfo> list = StockManager.getInstance().getStockInfos(stockId,startTime,endTime);
                if(list != null && !list.isEmpty()){
                    Iterator<StockInfo> infoIterator = list.iterator();
                    while (infoIterator.hasNext()){
                        StockInfo stockInfo = infoIterator.next();
                        double tempValue = FunctionManager
                                .getInstance()
                                .getFuncValue(expression,stockInfo);
                        values.add(tempValue);
                    }
                }


            }else{
                // 数值，直接计算.只接受数值
                if(StringUtil.isNumeric(expression)){

                    value = Double.parseDouble(expression);

                    long stockId = resolverTaskDO.getStockId();
                    long startTime = resolverTaskDO.getStartTime();
                    long endTime = resolverTaskDO.getEndTime();

                    values.clear();
                    List<StockInfo> list = StockManager.getInstance().getStockInfos(stockId,startTime,endTime);

                    if(list != null && !list.isEmpty()){
                        int listSize = list.size();
                        for(int i = 0; i < listSize; i++){
                            values.add(value);
                        }
                    }


                }else{
                    LogUtil.e("非法的表达式  expression  --- " + expression);
                }
            }

        }

    }



    /**
     * 获取一组数据
     * @return
     */
    public List<Double> getValues(){
        return values;
    }


    public String getName(){
        return variableName;
    }

}
