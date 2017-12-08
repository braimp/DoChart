package com.dqqdo.dochart.ui.view.parse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dqqdo.dochart.resolver.DoIndexResolver;
import com.dqqdo.dochart.resolver.ResolverResult;
import com.dqqdo.dochart.resolver.ResolverTaskDO;
import com.dqqdo.dochart.resolver.ViewPortInfo;
import com.dqqdo.dochart.resolver.draw.IDrawItem;
import com.dqqdo.dochart.util.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 作者：duqingquan
 * 时间：2017/11/27 16:01
 * @author hexun
 */
public class IndexParseView extends View{


    private DoIndexResolver indexResolver;
    private List<IDrawItem> drawItems = new ArrayList<>();
    ResolverTaskDO resolverTaskDO = new ResolverTaskDO();
    private String formula;

    /**
     * 设置公式
     */
    public void setFormula(String formula){
        this.formula = formula;
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

    /**
     * 视窗信息对象
     */
    ViewPortInfo viewPortInfo = new ViewPortInfo();

    public void execute(){

        viewPortInfo.setMaxValue(100);
        viewPortInfo.setMinValue(10);
        viewPortInfo.setPerValuePixel(20);
        viewPortInfo.setPerUnitWidth(50);

        // 更新屏幕展示信息
        indexResolver.setViewPortInfo(viewPortInfo);

        resolverTaskDO.setFormula(formula);
        // 假装是万科A
        resolverTaskDO.setStockId(1L);
        // NOTE 解析器的最小时间单位是天。
        // 设置开始时间
        resolverTaskDO.setStartTime(20160101L);
        // 设置结束时间
        resolverTaskDO.setEndTime(20171101L);

        indexResolver.submitResolver(resolverTaskDO, new DoIndexResolver.IResolverCallback() {
            @Override
            public void onSuccess(ResolverResult result) {
                drawItems = result.getDrawItems();
                postInvalidate();
            }

            @Override
            public void onFail(int errCode, String errDesc) {
                LogUtil.e("errDesc ---  " + errDesc);
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(w != oldw || h != oldh){

            Rect viewRect = new Rect();
            IndexParseView.this.getDrawingRect(viewRect);
            RectF rectF = new RectF(viewRect);
            viewPortInfo.setViewRectF(rectF);


        }
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
