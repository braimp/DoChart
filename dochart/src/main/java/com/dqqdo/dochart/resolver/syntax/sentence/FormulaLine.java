package com.dqqdo.dochart.resolver.syntax.sentence;

import com.dqqdo.dochart.resolver.ScriptRuntime;
import com.dqqdo.dochart.resolver.syntax.DoConstants;
import com.dqqdo.dochart.resolver.syntax.LogicPrimitive;
import com.dqqdo.dochart.resolver.syntax.function.FunctionManager;
import com.dqqdo.dochart.resolver.syntax.parser.SentenceParser;
import com.dqqdo.dochart.resolver.syntax.parser.ShapeFactory;
import com.dqqdo.dochart.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;

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
     * 逻辑上的图元对象，如果是普通绘制语句，那么最终返回结果就是这个。
     */
    private LogicPrimitive logicPrimitive;

    /**
     * 变量表
     */
    private HashMap<String,Object> variables = new HashMap<>();

    /**
     * 是否是有效的
     */
    private boolean isValid = true;

    public FormulaLine(String line){
        this.content = line;

        if(line.contains(DoConstants.EXPRESSION_DEFINE)){
            // 计算
            formulaLineType = FormulaLineType.NORMAL;
        }else if(line.contains(DoConstants.EXPRESSION_EQUAL)){
            // 定义
            formulaLineType = FormulaLineType.DRAW;
        }else{
            return ;
        }


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

        if(line.contains(DoConstants.EXPRESSION_EQUAL)){

            // 计算语句，需要构造产出逻辑图元对象
            logicPrimitive = new LogicPrimitive();

            // 分句解析完成
            int sSize = sentences.size();
            for(int i = 0 ; i < sSize;i++){
                FormulaSentence formulaSentence = sentences.get(i);

                if(formulaSentence instanceof ColorSentence){
                    logicPrimitive.setColor(((ColorSentence) formulaSentence).getColorInt());
                }
                if(formulaSentence instanceof ShapeSentence){
                    logicPrimitive.setShape(((ShapeSentence) formulaSentence).getShape());
                }
                if(formulaSentence instanceof EvaluationSentence){
                    EvaluationSentence evaluationSentence = (EvaluationSentence) formulaSentence;
                    logicPrimitive.setValue(evaluationSentence.getValue());
                    LogUtil.d("evaluationSentence.getName()  ----  " + evaluationSentence.getName());
                    logicPrimitive.setName(evaluationSentence.getName());
                }
            }


        }else{
            // 定义语句，只是需要计算，并且将变量储存到变量表中
            FunctionManager.FuncVarDO varDO = FunctionManager
                    .getInstance().getFuncValue(line);

            String name = varDO.getName();
            Object target = varDO.getValue();
            variables.put(name,target);
        }


    }

    /**
     * 如果是输出语句，则返回逻辑图元
     * @return  返回逻辑图元对象
     */
    public LogicPrimitive getLogicPrimitive(){
        return logicPrimitive;
    }

    /**
     * 获取变量表
     * @return 变量集合
     */
    public HashMap<String,Object> getVariables(){

        return variables;
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
