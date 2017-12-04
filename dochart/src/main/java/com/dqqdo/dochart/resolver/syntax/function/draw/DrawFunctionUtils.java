package com.dqqdo.dochart.resolver.syntax.function.draw;

import com.dqqdo.dochart.resolver.syntax.parser.ShapeFactory;

/**
 * 作者：duqingquan
 * 时间：2017/12/1 16:03
 */
public class DrawFunctionUtils {

    public static boolean isDrawFunction(String sentence){
        Class result = ShapeFactory.getInstance().getShapeClass(sentence);
        if(result != null){
            return true;
        }
        return false;
    }


}
