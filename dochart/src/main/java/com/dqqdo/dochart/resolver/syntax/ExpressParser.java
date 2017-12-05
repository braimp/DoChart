package com.dqqdo.dochart.resolver.syntax;

import com.dqqdo.dochart.resolver.lib.LibraryManager;
import com.dqqdo.dochart.resolver.syntax.function.FunctionManager;
import com.dqqdo.dochart.util.LogUtil;
import com.dqqdo.dochart.util.StringUtil;

/**
 * 作者：duqingquan
 * 时间：2017/12/5 10:39
 */
public class ExpressParser {

    private volatile static ExpressParser instance;

    private ExpressParser() {

    }

    public static ExpressParser getInstance() {

        if (instance == null) {
            synchronized (ExpressParser.class) {
                if (instance == null) {
                    instance = new ExpressParser();
                }
            }
        }
        return instance;
    }

    public double getExpressionValue(String expression){

        // TODO
        if(FunctionManager.getInstance().isFunc(expression)){
            // 是函数，则交由函数管理器处理

        }else{
            // 数值，直接计算.只接受数值
            if(StringUtil.isNumeric(expression)){
                LogUtil.d("expression  --- " + expression);
                return Double.parseDouble(expression);
            }else{
                LogUtil.d("不合格的字符  expression  --- " + expression);
            }
        }

        return 0.0;
    }

}
