package com.dqqdo.dochart.resolver.syntax.function;

import com.dqqdo.dochart.resolver.DoIndexResolver;
import com.dqqdo.dochart.resolver.ResolverTaskDO;
import com.dqqdo.dochart.resolver.StockInfo;
import com.dqqdo.dochart.resolver.ViewPortInfo;
import com.dqqdo.dochart.resolver.stock.StockManager;
import com.dqqdo.dochart.util.LogUtil;
import com.dqqdo.dochart.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * COST函数
 * 股票平均成本函数，根据输入的N，返回N%的获利股票的平均成本
 * 时间：2017/11/30 17:44
 * @author duqingquan
 */
public class CostFunction implements IFunction{

    /**
     * 函数名称
     */
    private final String COST_KEY_WORD = "COST";

    /**
     * 比例参数
     */
    private int radio;

    @Override
    public String getFunctionKeyword() {
        return COST_KEY_WORD;
    }

    @Override
    public double getFunctionResult(String line,StockInfo stockInfo) {
        // TODO 筹码分布算法实现
        // 计算结果先忽略。因为需要筹码分布算法(后续添加)
        ArrayList<String> params = new ArrayList<>(2);
        // eg. 50,EAST(10,NEW(5))
        int size = line.length();
        StringBuilder stringBuilder = new StringBuilder();

        // TODO  防止递归过深的处理,尚未添加

        for(int i = 0; i < size; i++){
            char nowChar = line.charAt(i);
            if(nowChar != ','){
                stringBuilder.append(nowChar);
            }else{
                String param = stringBuilder.toString();
                params.add(param);
                stringBuilder.delete(0,stringBuilder.length());
            }
            if(i == size - 1){
                if(stringBuilder.length() > 0){
                    String param = stringBuilder.toString();
                    params.add(param);
                }
                stringBuilder.delete(0,stringBuilder.length());
            }
        }

        double value = 0;

        // 判断参数个数
        if(params.size() == 1){
            // 只有一个参数合法，进行处理
            String param = params.get(0);
            if(FunctionManager.getInstance().isFunc(param)){
                // 参数中包含函数，遍历解析
                value = FunctionManager.getInstance().getFuncValue(param,stockInfo);
            }else{

                if(StringUtil.isNumeric(param)){
                    // 是数字
                    value = Double.parseDouble(param);
                }else{
                    // 还有一种可能是，参数使用本地变量
                    // TODO
                }
            }
        }

        // 成功过滤出参数,接下来根据参数去计算当天的数据。 这里只计算一天。
        // TODO 现在先只是返回最高价,实际需要计算成本函数 巴拉巴拉
        double price = stockInfo.getPrice();
        double randomValue = value + price;

        return randomValue;

    }





}
