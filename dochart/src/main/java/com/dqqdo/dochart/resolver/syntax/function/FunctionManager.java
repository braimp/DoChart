package com.dqqdo.dochart.resolver.syntax.function;

import com.dqqdo.dochart.util.LogUtil;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 包管理器
 * 时间：2017/12/5 10:26
 * @author duqingquan
 */
public class FunctionManager {

    private volatile static FunctionManager instance;

    private HashMap<String,IFunction> functions = new HashMap<>();


    private FunctionManager() {
        // TODO 后续完善 库函数加载机制。现在只是简单实现。

        CostFunction costFunction = new CostFunction();
        functions.put(costFunction.getFunctionKeyword(),costFunction);

    }

    public static FunctionManager getInstance() {

        if (instance == null) {
            synchronized (FunctionManager.class) {
                if (instance == null) {
                    instance = new FunctionManager();
                }
            }
        }
        return instance;

    }

    public boolean isFunc(String expression){

        String pattern = "\\D+\\(.*\\)";

        boolean isMatch = Pattern.matches(pattern, expression);

        return isMatch;
    }

    /**
     * 获取函数的结果值
     * @param expression 函数表达式
     * @return 结果值
     */
    public FuncVarDO getFuncValue(String expression){

        FuncVarDO varDO = new FuncVarDO();
        // 这里只有返回值
        varDO.setName(null);
        //COST(---50,EAST(10,NEW(5))---)
        // 按指定模式在字符串查找
        String pattern = "(\\D+)\\((.*)\\)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(expression);
        if (m.find( )) {
            LogUtil.d(" size ---- " + m.group().length());
            LogUtil.d(" 1 ---- " + m.group(1));
            LogUtil.d(" 2 ---- " + m.group(2));
            String funcName = m.group(1);

            IFunction function = functions.get(funcName);
            if(function == null){
                // 函数名不存在
                LogUtil.e("函数名不存在 " + function);
                return null;
            }

            String funcParam = m.group(2);
            // eg. 50,EAST(10,NEW(5))
            double value = function.getFunctionResult(funcParam);
            varDO.setValue(0.0);
        } else {

        }

        // TODO 这里好麻烦，需要实现等式的解析



        return varDO;
    }

    /**
     * 函数返回的数据对象
     */
    public class FuncVarDO{

        private String name;
        private Double value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "FuncVarDO{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

}
