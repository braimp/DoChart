package com.dqqdo.dochart.resolver.draw;

import android.graphics.Color;

import com.dqqdo.dochart.resolver.syntax.LogicPrimitive;
import com.dqqdo.dochart.resolver.syntax.shape.IShape;
import com.dqqdo.dochart.resolver.syntax.shape.LineThick2;
import com.dqqdo.dochart.resolver.syntax.shape.LineThick3;
import com.dqqdo.dochart.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：duqingquan
 * 时间：2017/12/5 15:49
 */
public class DrawItemParser {

    private volatile static DrawItemParser instance;

    private DrawItemParser(){

    }

    public static DrawItemParser getInstance(){

        if(instance == null){
            synchronized (DrawItemParser.class){
                if(instance == null){
                    instance = new DrawItemParser();
                }
            }
        }

        return instance;
    }

    /**
     * 将逻辑图元转换为可绘制对象
     * @param logicPrimitive 逻辑图元对象
     * @return 可绘制对象列表
     */
    public List<IDrawItem> parseLogicPrimitive(LogicPrimitive logicPrimitive){

        if(logicPrimitive == null){
            return null;
        }

        // TODO 实现具体的逻辑图元转为具体可绘制对象的算法
        List<IDrawItem> drawItems = new ArrayList<>();

        LogUtil.d("logicPrimitive  ---  " + logicPrimitive.getName());

        IShape shape = logicPrimitive.getShape();
        if(shape instanceof LineThick2){
            LineDrawItem lineDrawItem = new LineDrawItem();
            lineDrawItem.setWidth(LineThick2.WIDTH);

            lineDrawItem.setColor(logicPrimitive.getColor());
            lineDrawItem.setData(new float[]{
                    100,10,200,50,
                    200,50,300,80,
                    300,80,400,70,
                    400,70,500,180,
                    500,180,600,200,
                    600,200,
            });
            // 这里填充假数据
            drawItems.add(lineDrawItem);
        }else if(shape instanceof LineThick3){
            LineDrawItem lineDrawItem = new LineDrawItem();
            lineDrawItem.setWidth(LineThick3.WIDTH);

            lineDrawItem.setColor(logicPrimitive.getColor());
            lineDrawItem.setData(new float[]{
                    100,10,200,50,
                    200,50,300,80,
                    300,80,400,70,
                    400,70,500,180,
                    500,180,600,200,
                    600,200,
            });
            // 这里填充假数据
            drawItems.add(lineDrawItem);
        }


        return drawItems;

    }

}
