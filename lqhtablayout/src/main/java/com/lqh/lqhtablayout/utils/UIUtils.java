package com.lqh.lqhtablayout.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * @author chaychan
 * @date 2017/3/7  17:19
 */
public class UIUtils {
    /**
     * dp-->px
     */
    public static int dp2px(Context context, int dip) {
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }

    /**
     * sp-->px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2dp(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue / fontScale + 0.5f);
    }


    public static int getColor(Context context, int colorId){
        return ContextCompat.getColor(context,colorId);
    }

    public static Drawable getDrawable(Context context, int resId){
        return  ContextCompat.getDrawable(context,resId);
    }
    public static boolean isEmpty(String string){
        if (string == null || string.trim().length() == 0){
            return true;
        }
        return false;
    }

    public static boolean isNumber(String str){
        if(UIUtils.isEmpty(str)){
            return  false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();
    }
    public static void show(TextView msgView, int num,int solidColor, int radiusDp, int strokeDp, int strokeColor) {
        if(msgView==null){
        return;
        }
        ViewGroup.LayoutParams lp = msgView.getLayoutParams();
        DisplayMetrics dm = msgView.getResources().getDisplayMetrics();
        if(num<=0){
            msgView.setText("");
            lp.width = (int) (5 * dm.density);
            lp.height = (int) (5 * dm.density);
            radiusDp=lp.width;
            GradientDrawable shape = DrawableUtils.createShape(msgView.getContext(), solidColor, radiusDp, strokeDp, strokeColor, GradientDrawable.OVAL);
            msgView.setBackground(shape);
            msgView.setLayoutParams(lp);
        }else if (num > 0 && num <=9) {//圆
                lp.width=ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.height=ViewGroup.LayoutParams.WRAP_CONTENT;
                msgView.setText(num + "");
                msgView.measure(0,0);
                int width = msgView.getMeasuredWidth();
                int height = msgView.getMeasuredHeight();
                 radiusDp=(width>=height)?width:height;
                lp.width=radiusDp;
                lp.height=radiusDp;
                msgView.setLayoutParams(lp);
                GradientDrawable shape = DrawableUtils.createShape(msgView.getContext(), solidColor, radiusDp, strokeDp, strokeColor, GradientDrawable.OVAL);
                msgView.setBackground(shape);
            }else {
            lp.width=ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height=ViewGroup.LayoutParams.WRAP_CONTENT;
            msgView.setLayoutParams(lp);
            if(num > 9 && num <=99){
                msgView.setText(""+num);
            }else{
                msgView.setText("99+");
            }
            msgView.measure(0,0);
            GradientDrawable shape = DrawableUtils.createShape(msgView.getContext(), solidColor, radiusDp, strokeDp, strokeColor, GradientDrawable.RECTANGLE);
            msgView.setBackground(shape);
        }
    }
    public static void show(TextView msgView, String mes,int solidColor, int radiusDp, int strokeDp, int strokeColor) {
        if(msgView==null){
            return;
        }
        ViewGroup.LayoutParams lp = msgView.getLayoutParams();
        DisplayMetrics dm = msgView.getResources().getDisplayMetrics();
        if(TextUtils.isEmpty(mes)){
            msgView.setText("");
            lp.width = (int) (5 * dm.density);
            lp.height = (int) (5 * dm.density);
            radiusDp=lp.width;
            GradientDrawable shape = DrawableUtils.createShape(msgView.getContext(), solidColor, radiusDp, strokeDp, strokeColor, GradientDrawable.OVAL);
            msgView.setBackground(shape);
            msgView.setLayoutParams(lp);
        }else if (mes.length()==1) {//圆
            lp.width=ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height=ViewGroup.LayoutParams.WRAP_CONTENT;
            msgView.setText(mes);
            msgView.measure(0,0);
            int width = msgView.getMeasuredWidth();
            int height = msgView.getMeasuredHeight();
            radiusDp=(width>=height)?width:height;
            lp.width=radiusDp;
            lp.height=radiusDp;
            msgView.setLayoutParams(lp);
            GradientDrawable shape = DrawableUtils.createShape(msgView.getContext(), solidColor, radiusDp, strokeDp, strokeColor, GradientDrawable.OVAL);
            msgView.setBackground(shape);
        }else{
            lp.width=ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height=ViewGroup.LayoutParams.WRAP_CONTENT;
            msgView.setLayoutParams(lp);
            msgView.setText(mes);
            GradientDrawable shape = DrawableUtils.createShape(msgView.getContext(), solidColor, radiusDp, strokeDp, strokeColor, GradientDrawable.RECTANGLE);
            msgView.setBackground(shape);
        }
    }
    public static void setUnReadLocation(TextView UnReadView, int dpRXOffset, int dpTYOffset){

        RelativeLayout.LayoutParams rll = (RelativeLayout.LayoutParams) UnReadView.getLayoutParams();
        rll.rightMargin=UIUtils.dp2px(UnReadView.getContext(),dpRXOffset);
        rll.topMargin=UIUtils.dp2px(UnReadView.getContext(),dpTYOffset);
        UnReadView.setLayoutParams(rll);
    }
}
