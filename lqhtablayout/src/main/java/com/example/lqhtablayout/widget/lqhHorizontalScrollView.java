package com.example.lqhtablayout.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by Linqh on 2019/6/26.
 *
 * @describe:
 */
public class lqhHorizontalScrollView extends HorizontalScrollView {
    public lqhHorizontalScrollView(Context context) {
        super(context);
    }

    public lqhHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public lqhHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(onDrawListener!=null){
            onDrawListener.onDrawIndicator(canvas);
        }
    }
   private OnDrawListener onDrawListener;

    public OnDrawListener getOnDrawListener() {
        return onDrawListener;
    }

    public void setOnDrawListener(OnDrawListener onDrawListener) {
        this.onDrawListener = onDrawListener;
    }

    public interface  OnDrawListener{
       void onDrawIndicator(Canvas canvas);
    }
}
