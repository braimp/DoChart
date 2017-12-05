package com.dqqdo.dochart.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：duqingquan
 * 时间：2017/12/5 11:37
 */
public class StringUtil {

    static Pattern numPattern = Pattern.compile("[0-9]*");

    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){

        Matcher isNum = numPattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
