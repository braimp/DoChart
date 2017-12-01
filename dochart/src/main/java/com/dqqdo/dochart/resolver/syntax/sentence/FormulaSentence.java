package com.dqqdo.dochart.resolver.syntax.sentence;

/**
 * 公式分句,每一行都可以包含多个分句
 * 作者：duqingquan
 * 时间：2017/12/1 13:54
 */
public class FormulaSentence {

    private String content;

    FormulaSentence(String line){
        this.content = line;
    }

    /**
     * 校验公式行是否有效
     * @return
     */
    public boolean isValid(){
        // TODO 校验算法
        return true;
    }

    @Override
    public String toString(){
        return content;
    }

}
