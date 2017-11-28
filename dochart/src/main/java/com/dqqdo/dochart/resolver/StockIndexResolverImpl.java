package com.dqqdo.dochart.resolver;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 股票的指标解析器实现类
 * 作者：duqingquan
 * 时间：2017/10/10 17:44
 * @author hexun
 */
public class StockIndexResolverImpl implements IndexResolver{

    class ResolverTask implements Runnable{

        private String formula;

        private ResolverTask(String formula){
            this.formula = formula;
        }

        @Override
        public void run() {

        }
    }

    @Override
    public int setFormula(String strFormula) {

        executorService.submit(new ResolverTask(strFormula));
        return 0;
    }

    @Override
    public int setParams(String strParam) {
        return 0;
    }

    @Override
    public int setData(String strData) {
        return 0;
    }

    @Override
    public String execute() {
        return null;
    }

    @Override
    public String executeEx() {
        return null;
    }

    @Override
    public String getResult(int nStart, int nCount) {
        return null;
    }

    /**
     * 自定义线程池
     */
    private ExecutorService executorService;

    public StockIndexResolverImpl(){
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return null;
            }
        };

        executorService = new ThreadPoolExecutor(1, 100,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());



    }



}
