package com.dqqdo.dochart.resolver.syntax;

import com.dqqdo.dochart.util.LogUtil;

import java.util.ArrayList;

/**
 * 公式的组成部分，公式行
 * 作者：duqingquan
 * 时间：2017/11/30 17:20
 * @author duqingquan
 */
public class FormulaLine {

    /**
     * 公式每行的内容
     */
    private String content;

    private FormulaLineType formulaLineType;

    private ArrayList<FormulaSentence> sentences = new ArrayList<>();

    /**
     * 是否是有效的
     */
    private boolean isValid = true;

    public FormulaLine(String line){
        this.content = line;
        String[] subLines = line.split(",");
        for(int i = 0; i < subLines.length;i++){
            FormulaSentence formulaSentence = new FormulaSentence(subLines[i]);
            LogUtil.d("formulaSentence  ----  " + formulaSentence);
            if(!formulaSentence.isValid()){
                isValid = false;
            }
            sentences.add(formulaSentence);
        }

    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public FormulaLineType getFormulaLineType() {
        return formulaLineType;
    }

    public void setFormulaLineType(FormulaLineType formulaLineType) {
        this.formulaLineType = formulaLineType;
    }

    /**
     * 校验公式行是否有效
     * @return
     */
    public boolean isValid(){
        // TODO 校验算法
        return isValid;
    }
}
