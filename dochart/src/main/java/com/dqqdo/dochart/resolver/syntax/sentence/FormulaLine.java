package com.dqqdo.dochart.resolver.syntax.sentence;

import com.dqqdo.dochart.resolver.syntax.parser.SentenceParser;
import com.dqqdo.dochart.resolver.syntax.parser.ShapeFactory;
import com.dqqdo.dochart.util.LogUtil;

import java.util.ArrayList;

import static com.dqqdo.dochart.resolver.syntax.DoConstants.LINE_END_CHAR;

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


        char[] chars = line.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();

        boolean isInBracket = false;

        for(int i = 0 ; i < chars.length; i++){

            if(chars[i] == '('){
                isInBracket = true;
            }

            if(chars[i] == ')'){
                isInBracket = false;
            }

            if(chars[i] == ',' && !isInBracket){
                // 不是函数内的逗号,则认为是断句符号
                String sentence = stringBuilder.toString();
                FormulaSentence formulaSentence =
                        SentenceParser.getInstance().parseSentence(sentence);

                if(!formulaSentence.isValid()){
                    isValid = false;
                    return ;
                }
                sentences.add(formulaSentence);
                stringBuilder.delete(0,stringBuilder.length());
            }else{
                if (LINE_END_CHAR.equals(String.valueOf(chars[i]))) {
                    String sentence = stringBuilder.toString();
                    FormulaSentence formulaSentence =
                            SentenceParser.getInstance().parseSentence(sentence);
                    if(!formulaSentence.isValid()){
                        isValid = false;
                        return ;
                    }
                    sentences.add(formulaSentence);

                    stringBuilder.delete(0,stringBuilder.length());
                    break;
                }else{
                    stringBuilder.append(chars[i]);
                }
            }

        }
        // 分句解析完成
        int sSize = sentences.size();
        for(int i = 0 ; i < sSize;i++){
            FormulaSentence formulaSentence = sentences.get(i);

            if(formulaSentence instanceof ColorSentence){
                LogUtil.d("ColorSentence  ----" + formulaSentence.toString());
            }
            if(formulaSentence instanceof ShapeSentence){
                LogUtil.d("ShapeSentence  ----" + formulaSentence.toString());
            }
            if(formulaSentence instanceof EvaluationSentence){
                LogUtil.d("EvaluationSentence  ----" + formulaSentence.toString());
            }

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
