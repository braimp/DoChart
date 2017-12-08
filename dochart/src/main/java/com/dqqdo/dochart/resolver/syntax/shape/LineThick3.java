package com.dqqdo.dochart.resolver.syntax.shape;

import com.dqqdo.dochart.resolver.DoIndexResolver;
import com.dqqdo.dochart.resolver.ViewPortInfo;
import com.dqqdo.dochart.resolver.draw.IDrawItem;
import com.dqqdo.dochart.resolver.draw.LineDrawItem;
import com.dqqdo.dochart.resolver.syntax.LogicPrimitive;

import java.util.ArrayList;
import java.util.List;

/**
 * 一种形状的功能实现
 * 时间：2017/12/4 14:23
 * @author duqingquan
 */
public class LineThick3 implements IShape{

    private static final int WIDTH = 20;

    @Override
    public String toString() {
        return "LineThick3{}";
    }


    @Override
    public IDrawItem getDrawItem(LogicPrimitive logicPrimitive) {

        LineDrawItem lineDrawItem = new LineDrawItem();

        lineDrawItem.setWidth(WIDTH);
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

        return lineDrawItem;
    }

}
