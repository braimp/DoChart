package com.dqqdo.dochart.resolver;

import com.dqqdo.dochart.resolver.draw.DrawItemParser;
import com.dqqdo.dochart.resolver.draw.IDrawItem;
import com.dqqdo.dochart.resolver.syntax.LogicPrimitive;
import com.dqqdo.dochart.resolver.syntax.sentence.FormulaLine;
import com.dqqdo.dochart.resolver.syntax.sentence.FormulaLineType;
import com.dqqdo.dochart.util.LogUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.dqqdo.dochart.resolver.syntax.DoConstants.LINE_END_CHAR;

/**
 * 指标解析器对象
 * 时间：2017/11/30 11:18
 * @author duqingquan
 */
public class DoIndexResolver {


    /**
     * 解析器实例对象
     */
    private static volatile DoIndexResolver instance;

    /**
     * 自定义线程池
     */
    private ExecutorService executorService;

    /**
     * 脚本运行时环境
     */
    private ScriptEnvironment scriptEnvironment;


    /**
     * 视窗信息
     */
    ViewPortInfo viewPortInfo = new ViewPortInfo();


    /**
     * 获取视窗信息对象
     * @return
     */
    public ViewPortInfo getViewPortInfo(){
        return viewPortInfo;
    }

    /**
     * 获取解析器实例
     * @return 解析器实例对象
     */
    public static DoIndexResolver getInstance(){
        if(instance == null){
            synchronized (DoIndexResolver.class){
                if(instance == null){
                    instance = new DoIndexResolver();
                    return instance;
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造器
     */
    private DoIndexResolver(){

        // 构造任务线程池
        executorService = new ThreadPoolExecutor(1,
                100,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());

        scriptEnvironment = new ScriptEnvironment();
        // 填充假数据
        // TODO 准备真实数据，数据来源工作量略大，等待后续实现。
        scriptEnvironment.prepareWork(viewPortInfo);
    }


    /**
     * 设置视窗信息
     * @param info
     */
    public void setViewPortInfo(ViewPortInfo info){
        this.viewPortInfo = info;
        scriptEnvironment.prepareWork(viewPortInfo);
    }


    /**
     * 提交新的解析任务
     * @param resolverTaskDO 解析任务对象
     * @return 是否提交成功
     */
    public boolean submitResolver(ResolverTaskDO resolverTaskDO,IResolverCallback resolverCallback) {

        if(resolverTaskDO == null){
            return false;
        }else{
            ResolverTask resolverTask = new ResolverTask(resolverTaskDO,resolverCallback);
            executorService.submit(resolverTask);
            return true;
        }

    }


    /**
     * 解析任务类
     */
    class ResolverTask implements Runnable{

        /**
         * 公式
         */
        private String formula;
        /**
         * 脚本运行时环境
         */
        private ScriptRuntime scriptRuntime;
        /**
         * 解析回调
         */
        private IResolverCallback resolverCallback;

        /**
         * 解析任务 对应的数据对象
         */
        private ResolverTaskDO resolverTaskDO;

        private ResolverTask(ResolverTaskDO taskDO,IResolverCallback callback){

            this.resolverTaskDO = taskDO;
            this.formula = taskDO.getFormula();
            this.scriptRuntime = new ScriptRuntime();
            this.resolverCallback = callback;

        }


        @Override
        public void run() {

            if(resolverCallback == null){
                return ;
            }

            // 1 命令行打包成一个数组，验证合法后，依次解析数组
            String[] lines = formula.split(";");
            FormulaLine[] formulaLines = new FormulaLine[lines.length];
            if(lines.length > 0){
                // 有数据
                for(int i = 0; i < lines.length;i++){

                    FormulaLine formulaLine = new FormulaLine(
                            lines[i] + LINE_END_CHAR
                            ,resolverTaskDO);

                    if(!formulaLine.isValid()){
                        // 有任何行语法错误，当前公式存在异常，结束
                        break;
                    }

                    formulaLines[i] = formulaLine;
                }

            }else{
                // 解析完成，无有效命令行
                resolverCallback.onFail(-101,"无有效命令行");
                return ;
            }


            // 2  命令行解析完毕,进入执行阶段
            ResolverResult resolverResult = new ResolverResult();
            HashMap<String,List<IDrawItem>> totalDrawItems = new HashMap<>(16);

            for(int i = 0; i < formulaLines.length;i++){

                FormulaLine formulaLine = formulaLines[i];

                if(formulaLine.getFormulaLineType() == FormulaLineType.DRAW){
                    // 行为命令，则获取drawItem对象数据，返回给外部
                    LogicPrimitive logicPrimitive = formulaLine.getLogicPrimitive();

                    String logicPrimitiveName = logicPrimitive.getName();
                    List<IDrawItem> drawItems = DrawItemParser
                            .getInstance()
                            .parseLogicPrimitive(logicPrimitive);

                    // 将逻辑图元储存到本地变量集合，以备后续使用
                    boolean result = scriptRuntime.putVariable(logicPrimitiveName,logicPrimitive);

                    if(!result){
                        // 已经存在相同的定义，简单的输出日志
                        totalDrawItems.remove(logicPrimitiveName);
                        LogUtil.d("存在相关的定义，并且已自动覆盖：  " + logicPrimitiveName);
                    }

                    if(drawItems != null){
                        totalDrawItems.put(logicPrimitiveName,drawItems);
                    }

                }else{
                    // 定义命令，则将运算后的结果，储存到Runtime，提供给内部调用
                    scriptRuntime.putVariables(formulaLine.getVariables());
                }

            }

            LogUtil.d("totalDrawItems：  " + totalDrawItems.size());

            resolverResult.setResultCode(0);
            List<IDrawItem> resultList = new ArrayList();
            Collection<List<IDrawItem>> drawListCollection = totalDrawItems.values();
            Iterator<List<IDrawItem>> listIterator = drawListCollection.iterator();
            while (listIterator.hasNext()){
                List<IDrawItem> list = listIterator.next();
                resultList.addAll(list);
            }

            resolverResult.setDrawItems(resultList);
            // 解析完成，给予成功回调
            resolverCallback.onSuccess(resolverResult);

        }


    }


    /**
     * 解析任务返回的回调
     */
    public interface IResolverCallback{

        /**
         * 解析成功回调
         * @param result 产出的解析结果对象
         */
        void onSuccess(ResolverResult result);

        /**
         * 解析失败回调
         * @param errCode 错误代码
         * @param errDesc 错误描述
         */
        void onFail(int errCode,String errDesc);

    }



}
