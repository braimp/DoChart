package com.dqqdo.dochart.resolver;

import com.dqqdo.dochart.resolver.draw.DrawItemParser;
import com.dqqdo.dochart.resolver.draw.IDrawItem;
import com.dqqdo.dochart.resolver.syntax.LogicPrimitive;
import com.dqqdo.dochart.resolver.syntax.sentence.FormulaLine;
import com.dqqdo.dochart.resolver.syntax.sentence.FormulaLineType;
import com.dqqdo.dochart.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.dqqdo.dochart.resolver.syntax.DoConstants.LINE_END_CHAR;

/**
 * 指标解析器对象
 * 作者：duqingquan
 * 时间：2017/11/30 11:18
 * @author hexun
 */
public class DoIndexResolver {


    /**
     * 解析器实例对象
     */
    public static volatile DoIndexResolver instance;

    /**
     * 自定义线程池
     */
    private ExecutorService executorService;

    /**
     * 脚本运行时环境
     */
    private ScriptEnvironment scriptEnvironment;


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
        StockInfo stockInfo = new StockInfo();
        ViewPortInfo viewPortInfo = new ViewPortInfo();
        scriptEnvironment.prepareWork(stockInfo,viewPortInfo);

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
            executorService.submit(new ResolverTask(resolverTaskDO,resolverCallback));
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

        private ResolverTask(ResolverTaskDO resolverDTO,IResolverCallback callback){
            this.formula = resolverDTO.getFormula();
            this.scriptRuntime = new ScriptRuntime();
            this.resolverCallback = callback;
        }


        @Override
        public void run() {

            if(resolverCallback == null){
                return ;
            }

            // 所有的命令行 打包成一个数组，验证合法后，依次执行数组
            String[] lines = formula.split(";");
            FormulaLine[] formulaLines = new FormulaLine[lines.length];
            if(lines.length > 0){
                // 有数据
                for(int i = 0; i < lines.length;i++){

                    FormulaLine formulaLine = new FormulaLine(lines[i] + LINE_END_CHAR);
                    if(!formulaLine.isValid()){
                        // 有任何行语法错误，当前公式存在异常，结束
                        break;
                    }
                    formulaLines[i] = formulaLine;
                }

            }else{
                // 无数据
                LogUtil.d("无数据");
            }

            // 命令行解析完毕
            ResolverResult resolverResult = new ResolverResult();
            List<IDrawItem> totalDrawItems = new ArrayList<>();

            for(int i = 0; i < formulaLines.length;i++){

                FormulaLine formulaLine = formulaLines[i];
                if(formulaLine.getFormulaLineType() == FormulaLineType.DRAW){
                    // 行为命令，则获取drawItem对象数据，返回给外部
                    LogicPrimitive logicPrimitive = formulaLine.getLogicPrimitive();
                    // TODO 将逻辑图元转换为可绘制的drawItem对象
                    List<IDrawItem> drawItems = DrawItemParser
                            .getInstance()
                            .parseLogicPrimitive(logicPrimitive);

                    totalDrawItems.addAll(drawItems);

                }else{
                    // 定义命令，则将运算后的结果，储存到Runtime，提供给内部调用
                    // TODO 数据有效性校验
                    scriptRuntime.putVariables(formulaLine.getVariables());
                }

            }

            resolverResult.setDrawItems(totalDrawItems);
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
