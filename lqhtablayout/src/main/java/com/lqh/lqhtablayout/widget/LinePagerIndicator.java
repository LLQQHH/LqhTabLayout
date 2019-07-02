package com.lqh.lqhtablayout.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.lqh.lqhtablayout.model.PositionData;
import com.lqh.lqhtablayout.tabinterface.IPagerChangeListener;

import java.util.List;

/**
 * 直线viewpager指示器，带颜色渐变
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class LinePagerIndicator extends View implements IPagerChangeListener {
    // 控制动画
    private Interpolator mStartInterpolator = new LinearInterpolator();
    private Interpolator mEndInterpolator = new LinearInterpolator();

    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private float mIndicatorCornerRadius;
    private int mIndicatorColor= Color.parseColor("#ffffff");
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private Paint mPaint;
    private List<PositionData> mPositionDataList;

    private RectF mLineRect = new RectF();

    public LinePagerIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(mLineRect, mIndicatorCornerRadius, mIndicatorCornerRadius, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }


        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }

    public float getmIndicatorMarginLeft() {
        return mIndicatorMarginLeft;
    }

    public void setmIndicatorMarginLeft(float mIndicatorMarginLeft) {
        this.mIndicatorMarginLeft = mIndicatorMarginLeft;
    }

    public float getmIndicatorMarginTop() {
        return mIndicatorMarginTop;
    }

    public void setmIndicatorMarginTop(float mIndicatorMarginTop) {
        this.mIndicatorMarginTop = mIndicatorMarginTop;
    }

    public float getmIndicatorMarginRight() {
        return mIndicatorMarginRight;
    }

    public void setmIndicatorMarginRight(float mIndicatorMarginRight) {
        this.mIndicatorMarginRight = mIndicatorMarginRight;
    }

    public float getmIndicatorMarginBottom() {
        return mIndicatorMarginBottom;
    }

    public void setmIndicatorMarginBottom(float mIndicatorMarginBottom) {
        this.mIndicatorMarginBottom = mIndicatorMarginBottom;
    }

    public float getmIndicatorCornerRadius() {
        return mIndicatorCornerRadius;
    }

    public void setmIndicatorCornerRadius(float mIndicatorCornerRadius) {
        this.mIndicatorCornerRadius = mIndicatorCornerRadius;
    }

    public int getmIndicatorColor() {
        return mIndicatorColor;
    }

    public void setmIndicatorColor(int mIndicatorColor) {
        this.mIndicatorColor = mIndicatorColor;
    }

    public int getmIndicatorWidth() {
        return mIndicatorWidth;
    }

    public void setmIndicatorWidth(int mIndicatorWidth) {
        this.mIndicatorWidth = mIndicatorWidth;
    }

    public int getmIndicatorHeight() {
        return mIndicatorHeight;
    }

    public void setmIndicatorHeight(int mIndicatorHeight) {
        this.mIndicatorHeight = mIndicatorHeight;
    }

    public Paint getPaint() {
        return mPaint;
    }
    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }



    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public Interpolator getEndInterpolator() {
        return mEndInterpolator;
    }

    public void setEndInterpolator(Interpolator endInterpolator) {
        mEndInterpolator = endInterpolator;
        if (mEndInterpolator == null) {
            mEndInterpolator = new LinearInterpolator();
        }
    }
}
