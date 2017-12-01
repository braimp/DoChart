package com.dqqdo.dochart.resolver;

import com.dqqdo.dochart.resolver.syntax.sentence.FormulaLine;
import com.dqqdo.dochart.util.LogUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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


    }


    public boolean submitResolver(ResolverDTO resolverDTO) {

        if(resolverDTO == null){
            return false;
        }else{
            executorService.submit(new ResolverTask(resolverDTO));
            return true;
        }

    }



    class ResolverTask implements Runnable{

        private String formula;
        private StockInfo stockInfo;


        private ResolverTask(ResolverDTO resolverDTO){
            this.formula = resolverDTO.getFormula();
            this.stockInfo = resolverDTO.getStockInfo();
        }

        @Override
        public void run() {

            // 所有的命令行 打包成一个数组，验证合法后，依次执行数组
            String[] lines = formula.split(";");
            FormulaLine[] formulaLines = new FormulaLine[lines.length];
            if(lines.length > 0){
                // 有数据
                for(int i = 0; i < lines.length;i++){
                    LogUtil.d("lines[i]" +lines[i]);
                    FormulaLine formulaLine = new FormulaLine(lines[i]);
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


        }
    }



}
