package com.dqqdo.dochart.resolver.syntax;

import com.dqqdo.dochart.resolver.syntax.shape.IShape;

import java.util.List;

/**
 * 逻辑上的图元
 * 作者：duqingquan
 * 时间：2017/12/4 17:16
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
        this.name = name;
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
        this.name = name;
        this.color = color;
        this.shape = shape;
        this.value = value;
    }

    public LogicPrimitive() {
    }
}
