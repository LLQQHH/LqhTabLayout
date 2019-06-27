package com.example.lqhtablayout.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Linqh on 2019/6/27.
 *
 * @describe:
 */
public class LqhLinearLayout extends LinearLayout {


        public LqhLinearLayout(Context context) {
            this(context,null);
        }

        public LqhLinearLayout(Context context,  AttributeSet attrs) {
            this(context, attrs,0);
        }

        public LqhLinearLayout(Context context,  AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            this.setWillNotDraw(false);
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
