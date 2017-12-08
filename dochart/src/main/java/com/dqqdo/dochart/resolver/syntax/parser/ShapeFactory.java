package com.dqqdo.dochart.resolver.syntax.parser;

import com.dqqdo.dochart.resolver.syntax.shape.IShape;
import com.dqqdo.dochart.resolver.syntax.shape.LineThick2;
import com.dqqdo.dochart.resolver.syntax.shape.LineThick3;
import com.dqqdo.dochart.resolver.syntax.shape.LineThick4;

import java.util.HashMap;

/**
 * 作者：duqingquan
 * 时间：2017/12/4 14:18
 */
public class ShapeFactory {

    private HashMap<String,Class<? extends IShape>> shapes = new HashMap<>();

    {
        shapes.put("LINETHICK2", LineThick2.class);
        shapes.put("LINETHICK3", LineThick3.class);
        shapes.put("LINETHICK4", LineThick4.class);
    }

    private volatile static ShapeFactory instance;

    public static ShapeFactory getInstance(){

        if(instance == null){
            instance = new ShapeFactory();
        }
        return instance;
    }

    private ShapeFactory(){

    }

    public Class<? extends IShape> getShapeClass(String str){

        if(!shapes.containsKey(str)){
            return null;
        }
        return shapes.get(str);

    }

}
