package com.dqqdo.dochart.resolver.syntax.sentence;

import com.dqqdo.dochart.resolver.syntax.parser.ShapeFactory;
import com.dqqdo.dochart.resolver.syntax.shape.IShape;

/**
 * 作者：duqingquan
 * 时间：2017/12/1 16:13
 */
public class ShapeSentence extends FormulaSentence{

    private IShape shape;

    public ShapeSentence(String line) {
        super(line);

        // 解析图形函数
        Class<? extends IShape> classz = ShapeFactory
                .getInstance()
                .getShapeClass(line);

        if(classz != null){
            try {
                shape = classz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                shape = null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                shape = null;
            }
        }


    }

    public IShape getShape(){
        return shape;
    }


    @Override
    public String toString() {
        return "ShapeSentence{" +
                "shape=" + shape +
                '}';
    }
}
