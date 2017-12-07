package com.dqqdo.dochart.resolver.stock;

import com.dqqdo.dochart.resolver.ResolverTaskDO;
import com.dqqdo.dochart.resolver.StockInfo;
import com.dqqdo.dochart.resolver.syntax.parser.SentenceParser;

import java.util.ArrayList;
import java.util.List;

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

    private StockManager(){

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

        List<StockInfo> list = new ArrayList<>();

        // TODO 这里需要完成Stock列表的数据查询与管理
        StockInfo stockInfo = new StockInfo();
        list.add(stockInfo);

        return list;
    }

}
