package com.example.lqhtablayout.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import java.lang.reflect.Method;

/**
 * Created by Linqh on 2018/10/31.
 *
 * @describe:代码中创建shape和seletor
 */
public class DrawableUtils {

    /**
     * @param solidColor
     * @param radiusDp
     * @param strokeDp
     * @param strokeColor
     * @param type        0矩形，1椭圆形，3线
     * @return
     */
    public static GradientDrawable createShape(Context context,int solidColor, int radiusDp, int strokeDp, int strokeColor, int type) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if(solidColor!=-1){
            gradientDrawable.setColor(solidColor);
        }
        switch (type) {
            case 0:
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);//设置显示的样式
                break;
            case 1:
                gradientDrawable.setShape(GradientDrawable.OVAL);//设置显示的样式
                break;
            case 2:
                gradientDrawable.setShape(GradientDrawable.LINE);//设置显示的样式
                break;
            default:
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);//设置显示的样式

                break;
        }
        gradientDrawable.setGradientType(GradientDrawable.RECTANGLE);//设置渐变样式
        if (radiusDp > 0) {
            gradientDrawable.setCornerRadius(UIUtils.dp2px(context, radiusDp));
        }
        if (strokeDp > 0) {
            gradientDrawable.setStroke(UIUtils.dp2px(context, strokeDp), strokeColor);
        }
        return gradientDrawable;
    }

    public static GradientDrawable createShape(Context context,int solidColor, int radiusDp) {
        return createShape(context,solidColor, radiusDp, 0, 0xffffff, 0);
    }


    /**
     * @param normalDrawable 正常的Drawable
     * @param pressDrawable 正常的按下的Drawable
     * @return
     */
    public static StateListDrawable getSelector(Drawable normalDrawable, Drawable pressDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        //给当前的颜色选择器添加按下drawable
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
        //设置默认状态
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }
    //解决代码中设置progress无效
    @SuppressLint("NewApi")
    public static void setProgressDrawable(@NonNull ProgressBar bar, @DrawableRes int resId) {
        Drawable layerDrawable = bar.getResources().getDrawable(resId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable d = getMethod("tileify", bar, new Object[] { layerDrawable, false });
            bar.setProgressDrawable(d);
        } else {
            bar.setProgressDrawableTiled(layerDrawable);
        }
    }

    private static Drawable getMethod(String methodName, Object o, Object[] paras) {
        Drawable newDrawable = null;
        try {
            Class<?> c[] = new Class[2];
            c[0] = Drawable.class;
            c[1] = boolean.class;
            Method method = ProgressBar.class.getDeclaredMethod(methodName, c);
            method.setAccessible(true);
            newDrawable = (Drawable) method.invoke(o, paras);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newDrawable;
    }
}
