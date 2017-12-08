package com.dqqdo.dochart.resolver;

import com.dqqdo.dochart.util.LogUtil;

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


        String trimName = varName.trim();

        if(scriptVariables.containsKey(trimName)){
            // 已经存在相关的定义，替换之并且返回false
            scriptVariables.put(trimName,varObject);
            return false;
        }else{
            // 未存在相关定义。添加并且返回false
            scriptVariables.put(trimName,varObject);
            return true;
        }


    }

    public boolean putVariables(HashMap<String,Object> varObjects){

        scriptVariables.putAll(varObjects);
        return true;
    }

}
