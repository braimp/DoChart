package com.dqqdo.dochart.ui.view.parse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dqqdo.dochart.resolver.DoIndexResolver;
import com.dqqdo.dochart.resolver.ResolverResult;
import com.dqqdo.dochart.resolver.ResolverTaskDO;
import com.dqqdo.dochart.resolver.draw.IDrawItem;
import com.dqqdo.dochart.util.LogUtil;

import java.util.Iterator;
import java.util.List;

/**
 * 作者：duqingquan
 * 时间：2017/11/27 16:01
 * @author hexun
 */
public class IndexParseView extends View{


    private DoIndexResolver indexResolver;
    private List<IDrawItem> drawItems;

    /**
     * 设置公式
     */
    public void setFormula(String formula){
        final ResolverTaskDO resolverTaskDO = new ResolverTaskDO();
        resolverTaskDO.setFormula(formula);
        indexResolver.submitResolver(resolverTaskDO, new DoIndexResolver.IResolverCallback() {
            @Override
            public void onSuccess(ResolverResult result) {
                LogUtil.d("result ---  " + result);
                drawItems = result.getDrawItems();
            }

            @Override
            public void onFail(int errCode, String errDesc) {

            }
        });
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
        canvas.drawColor(Color.BLUE);
        synchronized (drawItems){
            if(drawItems != null && !drawItems.isEmpty()){
                Iterator<IDrawItem> iterator = drawItems.iterator();
                while (iterator.hasNext()){
                    IDrawItem drawItem =  iterator.next();
                    drawItem.drawSelf(canvas);
                }
            }
        }

    }
}
