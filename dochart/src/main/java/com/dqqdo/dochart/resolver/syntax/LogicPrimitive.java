package com.dqqdo.dochart.resolver.syntax;

import android.text.TextUtils;

import com.dqqdo.dochart.resolver.syntax.shape.IShape;

import java.util.List;

/**
 * 逻辑上的图元
 * 时间：2017/12/4 17:16
 * @author duqingquan
 */
public class LogicPrimitive {

    /**
     * 图元名称
     */
    private String name;

    /**
     * 图元颜色
     */
    private int color;

    /**
     * 图元形状
     */
    private IShape shape;

    /**
     * 图元数值
     */
    private List<Double> value;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(!TextUtils.isEmpty(name)){
            this.name = name.trim();
        }

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public IShape getShape() {
        return shape;
    }

    public void setShape(IShape shape) {
        this.shape = shape;
    }

    public List<Double> getValue() {
        return value;
    }

    public void setValue(List<Double> value) {
        this.value = value;
    }

    public LogicPrimitive(String name, int color, IShape shape, List<Double> value) {
        if(!TextUtils.isEmpty(name)){
            this.name = name.trim();
        }
        this.color = color;
        this.shape = shape;
        this.value = value;
    }

    public LogicPrimitive() {

    }

}
