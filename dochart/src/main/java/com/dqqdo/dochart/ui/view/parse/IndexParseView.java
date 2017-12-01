package com.dqqdo.dochart.ui.view.parse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dqqdo.dochart.resolver.DoIndexResolver;
import com.dqqdo.dochart.resolver.ResolverDTO;
import com.dqqdo.dochart.resolver.StockInfo;

/**
 * 作者：duqingquan
 * 时间：2017/11/27 16:01
 * @author hexun
 */
public class IndexParseView extends View{


    private DoIndexResolver indexResolver;

    /**
     * 设置公式
     */
    public void setFormula(String formula){
        ResolverDTO resolverDTO = new ResolverDTO();
        /**
         *  这里构造假的股票信息
         */
        StockInfo stockInfo = new StockInfo();
        stockInfo.setCode("0000002");
        stockInfo.setId(1L);

        resolverDTO.setStockInfo(stockInfo);
        resolverDTO.setFormula(formula);


        indexResolver.submitResolver(resolverDTO);

    }

    private void init(){
        indexResolver = DoIndexResolver.getInstance();
    }

    public IndexParseView(Context context) {
        super(context);
        init();
    }

    public IndexParseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndexParseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLUE);
    }
}
