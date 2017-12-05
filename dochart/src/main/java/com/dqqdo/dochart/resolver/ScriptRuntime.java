package com.dqqdo.dochart.resolver;

import java.util.HashMap;

/**
 * 脚本运行时环境
 * 作者：duqingquan
 * 时间：2017/12/4 16:50
 * @author hexun
 */
public class ScriptRuntime {


    /**
     * 变量表
     */
    private HashMap<String,Object> scriptVariables = new HashMap<>();

    /**
     * 构造器，每一个脚本运行时都需要一个独立的Runtime
     */
    public ScriptRuntime(){

    }

    /**
     * 新增变量
     * @return
     */
    public boolean putVariable(String varName,Object varObject){

        scriptVariables.put(varName,varObject);

        return true;
    }

}
