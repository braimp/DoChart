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


    public double getFuncValue(String expression){
        return 0.0;
    }


}
