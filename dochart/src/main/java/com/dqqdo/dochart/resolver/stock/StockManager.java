package com.dqqdo.dochart.resolver.stock;

import com.dqqdo.dochart.resolver.ResolverTaskDO;
import com.dqqdo.dochart.resolver.StockInfo;
import com.dqqdo.dochart.resolver.syntax.parser.SentenceParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 作者：duqingquan
 * 时间：2017/12/7 10:55
 */
public class StockManager {

    private volatile static StockManager instance;

    public static StockManager getInstance(){
        if(instance == null){
            instance = new StockManager();
        }
        return instance;
    }

    List<StockInfo> list = new ArrayList<>();

    private StockManager(){

        Random random = new Random();
        // TODO 这里需要完成Stock列表的数据查询与管理
        for(int i = 0; i < 20; i++){
            StockInfo stockInfo = new StockInfo();

            stockInfo.setPrice(random.nextInt(40) + 10);
            list.add(stockInfo);
        }
    }

    /**
     * 默认获取最新的
     * @param id
     * @return
     */
    public StockInfo getStockInfo(long id){

        // TODO 这里需要完成Stock信息的数据查询与管理
        StockInfo stockInfo = new StockInfo();
        return stockInfo;
    }

    public List<StockInfo> getStockInfos(long id,long startTime,long endTime){


        return list;
    }

}
