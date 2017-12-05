package com.dqqdo.dochart.resolver.syntax.function;

import com.dqqdo.dochart.util.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 包管理器
 * 作者：duqingquan
 * 时间：2017/12/5 10:26
 */
public class FunctionManager {

    private volatile static FunctionManager instance;

    private FunctionManager() {

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
        // TODO 这里好麻烦，需要实现等式的解析
        FuncVarDO varDO = new FuncVarDO();
        varDO.setName("h1");
        varDO.setValue(0.0);
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
