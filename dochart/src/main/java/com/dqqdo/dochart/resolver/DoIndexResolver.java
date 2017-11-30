package com.dqqdo.dochart.resolver;

import com.dqqdo.dochart.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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

            // 逐行解析
            BufferedReader bufferedReader = new BufferedReader(new StringReader(formula));
            String line;
            try {
                while((line =  bufferedReader.readLine()) != null){
                    LogUtil.d("line  " +  line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }



}
