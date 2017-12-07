package com.dqqdo.dochart.resolver.draw;

import android.graphics.Color;
import android.graphics.RectF;
import android.view.View;

import com.dqqdo.dochart.resolver.DoIndexResolver;
import com.dqqdo.dochart.resolver.ViewPortInfo;
import com.dqqdo.dochart.resolver.syntax.LogicPrimitive;
import com.dqqdo.dochart.resolver.syntax.shape.IShape;
import com.dqqdo.dochart.resolver.syntax.shape.LineThick2;
import com.dqqdo.dochart.resolver.syntax.shape.LineThick3;
import com.dqqdo.dochart.util.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
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

            List<Double> values = logicPrimitive.getValue();
            List<Float> data = new ArrayList<>();

            ViewPortInfo viewPortInfo = DoIndexResolver.getInstance().getViewPortInfo();
            // 这里的最大最小值，是外部组件计算后的结果。包含指标数据本身
            double maxValue = viewPortInfo.getMaxValue();
            double perValuePixel = viewPortInfo.getPerValuePixel();
            double perUnitWidth = viewPortInfo.getPerUnitWidth();


            int size = values.size();
            float preX = -1;
            float preY = -1;

            for(int i = 0; i < size;i++){

                double perValue = values.get(i);


                float yDistance = (float) ((maxValue - perValue) * perValuePixel);
                float y = yDistance;
                float x = (float) (i * perUnitWidth);

                if(i > 1 && preX != -1 && preY != -1){
                    data.add(preX);
                    data.add(preY);
                }

                data.add(x);
                data.add(y);

                preX = x;
                preY = y;
            }

            float[] arrayData = new float[data.size()];
            for(int i = 0; i < arrayData.length; i++){
                arrayData[i] = data.get(i);

            }


            lineDrawItem.setData(arrayData);



            // 这里填充假数据
            drawItems.add(lineDrawItem);

        }else if(shape instanceof LineThick3){
//            LineDrawItem lineDrawItem = new LineDrawItem();
//            lineDrawItem.setWidth(LineThick3.WIDTH);
//
//            lineDrawItem.setColor(logicPrimitive.getColor());
//            lineDrawItem.setData(new float[]{
//                    100,10,200,50,
//                    200,50,300,80,
//                    300,80,400,70,
//                    400,70,500,180,
//                    500,180,600,200,
//                    600,200,
//            });
//            // 这里填充假数据
//            drawItems.add(lineDrawItem);
        }


        return drawItems;

    }

}
