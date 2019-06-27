package com.example.lqhtablayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.lqhtablayout.model.PositionData;
import com.example.lqhtablayout.model.TabItemData;
import com.example.lqhtablayout.tabinterface.IPagerChangeListener;
import com.example.lqhtablayout.utils.DataUtils;
import com.example.lqhtablayout.utils.UIUtils;
import com.example.lqhtablayout.widget.lqhHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linqh on 2019/5/30.
 *
 * @describe:
 */
public class LqhTabLayout extends FrameLayout implements IPagerChangeListener, lqhHorizontalScrollView.OnDrawListener {
    private static final String TAG = "LqhTabLayout";
    //是否为滑动模式
    private boolean isScrollMode;
    //滑动模式下才存在
    private lqhHorizontalScrollView mScrollView;
    //存放内容主布局,用来加TabItemView的
    private LinearLayout linContent;
    //tab的个数
    private final ArrayList<Tab> tabs = new ArrayList<>();
    //item的位置信息
    private List<PositionData> mPositionDataList = new ArrayList<PositionData>();
    //当前的tab的位置
    private int mCurrentTabPosition;//
    //上一次位置
    private int mLstTabPosition;//
    //是否绑定Viewpager
    private ViewPager mViewPager;
    //监听器
    private ArrayList<LqhTabLayout.BaseOnTabSelectedListener> selectedListeners = new ArrayList<>();
    OnInterruptListener onInterruptListener;

    // 控制动画
    private ValueAnimator mScrollAnimator;
    private float mScrollPivotX = 0.5f; // 滚动中心点 0.0f - 1.0f


    //正常文本颜色
    private int normalColor;
    //选中的文本颜色
    private int selectedColor;
    //正常文本大小
    private int normalTextSize = 10; //10sp
    //选择的文本大小
    private int selectedTextSize = 10; //10sp
    //文字和图标的距离
    private int iconMargin=0;//默认距离dp
    //小红点字体大小
    private int unReadTextSize=8;//默认8Sp
    //小红点文字颜色
    private int unreadTextColor;
    //小红点背景颜色
    private int unreadTextBg;
    private int itemPadTB;
    private int itemPadLR;
    /**
     * 用于绘制显示器
     */
    private RectF mIndicatorRect = new RectF();
    private Interpolator mStartInterpolator = new LinearInterpolator();
    private Interpolator mEndInterpolator = new LinearInterpolator();
    private Paint mPaint=new Paint();
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private float mIndicatorCornerRadius;
    private int mIndicatorColor=Color.parseColor("#ffffff");
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private boolean mIndicatorAnimEnable;
    private boolean isSpaceEqual;
    private LinearLayout lin_indicator;

    public LqhTabLayout(Context context) {
        this(context, null);
    }

    public LqhTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LqhTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);//viewgroup默认不调用onDraw方法,需要调用这个方法来清除flag，会调用
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LqhTabLayout);
            isScrollMode = ta.getBoolean(R.styleable.LqhTabLayout_lqh_scroll_mode, false);
            isSpaceEqual = ta.getBoolean(R.styleable.LqhTabLayout_lqh_space_equal, true);
            //全局item设置
            normalColor = ta.getColor(R.styleable.LqhTabLayout_lqh_itemNormalTextColor, UIUtils.getColor(context,R.color.default_999999));
            selectedColor = ta.getColor(R.styleable.LqhTabLayout_lqh_itemSelectedColor,UIUtils.getColor(context,R.color.selected_ff0000));
            normalTextSize = ta.getDimensionPixelSize(R.styleable.LqhTabLayout_lqh_itemNormalTextSize, UIUtils.sp2px(context, normalTextSize));
            selectedTextSize = ta.getDimensionPixelSize(R.styleable.LqhTabLayout_lqh_itemSelectedTextSize, UIUtils.sp2px(context, selectedTextSize));
            iconMargin = ta.getDimensionPixelSize(R.styleable.LqhTabLayout_lqh_itemIconMargin, UIUtils.dip2Px(context, iconMargin));
            unReadTextSize = ta.getDimensionPixelSize(R.styleable.LqhTabLayout_lqh_itemUnreadTextSize, UIUtils.sp2px(context, unReadTextSize));
            unreadTextColor = ta.getColor(R.styleable.LqhTabLayout_lqh_itemUnreadTextColor, UIUtils.getColor(context, R.color.white));
            unreadTextBg = ta.getColor(R.styleable.LqhTabLayout_lqh_itemUnreadTextBg,UIUtils.getColor(context,R.color.selected_ff0000));
            itemPadTB = ta.getDimensionPixelSize(R.styleable.LqhTabLayout_lqh_itemContentPadTB,0);
            itemPadLR = ta.getDimensionPixelSize(R.styleable.LqhTabLayout_lqh_itemPadContentLR,0);
            //下标宽度 若为0则跟随item大小
            mIndicatorWidth = ta.getDimensionPixelSize(R.styleable.LqhTabLayout_lqh_indicator_width, -1);
            //下标高度大于0才显示指示器
            mIndicatorHeight = ta.getDimensionPixelSize(R.styleable.LqhTabLayout_lqh_indicator_height, -1);
            //下标圆角
            mIndicatorCornerRadius = ta.getDimension(R.styleable.LqhTabLayout_lqh_indicator_corner_radius, -1);
            //下标颜色,默认白色
            mIndicatorColor = ta.getColor(R.styleable.LqhTabLayout_lqh_indicator_color, mIndicatorColor);
            mIndicatorAnimEnable = ta.getBoolean(R.styleable.LqhTabLayout_lqh_indicator_anim_enable, true);
            //下标边距 这个属性只有mIndicatorWidth为0的时候才生效
            mIndicatorMarginLeft = ta.getDimension(R.styleable.LqhTabLayout_lqh_indicator_margin_left, 0);
            mIndicatorMarginTop = ta.getDimension(R.styleable.LqhTabLayout_lqh_indicator_margin_top, 0);
            mIndicatorMarginRight = ta.getDimension(R.styleable.LqhTabLayout_lqh_indicator_margin_right, 0);
            mIndicatorMarginBottom = ta.getDimension(R.styleable.LqhTabLayout_lqh_indicator_margin_bottom, 0);
            ta.recycle();
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mIndicatorColor);
        initInnerContentView();
    }

    //布局加载之后，xml解析完成
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initialise();
    }

    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        initialise();
    }

    //初始化tab
    private void initialise() {
        if (tabs.size() == 0) {
            return;
        }
        if (mViewPager != null) {
            if (mViewPager.getAdapter().getCount() != tabs.size()) {
                throw new IllegalArgumentException("tab个数必须和ViewPager条目数量一致");
            }
        }
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            if (tab.getCustomView() != null) {
                tab.getCustomView().setOnClickListener(new LqhOnClickListener(i));
            } else {
                tab.getLqhTabItemView().setOnClickListener(new LqhOnClickListener(i));
            }
        }
        if (mCurrentTabPosition < tabs.size()) {
            if (tabs.get(mCurrentTabPosition).getLqhTabItemView() != null) {
                //选中
                tabs.get(mCurrentTabPosition).getLqhTabItemView().refreshTabUI(true);
            }
        }
        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    LqhTabLayout.this.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    LqhTabLayout.this.onPageSelected(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        preparePositionData();
    }


    private void initInnerContentView() {
        View root;
        //是否为滑动模式
        if (isScrollMode) {
            root = LayoutInflater.from(getContext()).inflate(R.layout.tab_content_layout_scroll, this);
        } else {
            root = LayoutInflater.from(getContext()).inflate(R.layout.tab_content_layout_no_scroll, this);
        }
        //不滑动没有这个View
        mScrollView = findViewById(R.id.scroll_view);
        linContent = findViewById(R.id.lin_content);
        if(mScrollView!=null){
            mScrollView.setOnDrawListener(this);
        }
    }

    public void addView(View child) {
        this.addViewInternal(child);
    }

    public void addView(View child, int index) {
        this.addViewInternal(child);
    }

    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        this.addViewInternal(child);
    }

    //    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
//        this.addViewInternal(child);
//    }
    private void addViewInternal(View child) {
        if (child instanceof LqhTabItemView) {
            this.addTab(new Tab((LqhTabItemView) child));
        } else if (linContent == null) {
            super.addView(child, -1, generateDefaultLayoutParams());
        } else {
            throw new IllegalArgumentException("Only LqhTabItemView instances can be added to LqhTabLayout");
        }
    }

    public void addTab(Tab tab) {
        if (tabs.contains(tab)) {
            return;
        }
        tabs.add(tab);
        configTabItemView(tab);
        addTaItemView(tab.getCustomView() == null ? tab.getLqhTabItemView() : tab.getCustomView());
    }
    //配置全局TabItemView属性
    private void configTabItemView(Tab tab) {
        if(tab.getLqhTabItemView()!=null){
            LqhTabItemView lqhTabItemView = tab.getLqhTabItemView();
            lqhTabItemView.setNormalColor(normalColor,true);
            lqhTabItemView.setSelectedColor(selectedColor,true);
            lqhTabItemView.setNormalTextSize(normalTextSize,true);
            lqhTabItemView.setSelectedTextSize(selectedTextSize,true);
            lqhTabItemView.setIconMargin(iconMargin,true);
            lqhTabItemView.setUnReadTextSize(unReadTextSize,true);
            lqhTabItemView.setUnreadTextColor(unreadTextColor,true);
            lqhTabItemView.setUnreadTextBg(unreadTextBg,true);
            lqhTabItemView.setItemPadTB(itemPadTB,true);
            lqhTabItemView.setItemPadLR(itemPadLR,true);
            lqhTabItemView.updateStyle();
        }
    }


    private void addTaItemView(View item) {
        LinearLayout.LayoutParams itemLayoutParams;
            if (isSpaceEqual) {
                itemLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
            } else {
                itemLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            }
        linContent.addView(item, itemLayoutParams);
    }

    //这三个是自定义的
    //这里用来滑动指示器
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //这个表示不用画指示器
        if (mIndicatorHeight < 0||tabs.size()<0||!mIndicatorAnimEnable) {
            return;
        }
        calcIndicatorRect(position,positionOffset,positionOffsetPixels);
    }
    private void calcIndicatorRect(int position, float positionOffset, int positionOffsetPixels) {
        // 计算锚点位置
        PositionData current = DataUtils.getImitativePositionData(mPositionDataList, position);
        PositionData next = DataUtils.getImitativePositionData(mPositionDataList, position + 1);
        float leftX;
        float nextLeftX;
        float rightX;
        float nextRightX;
        //表示固定指示器长度
        if (mIndicatorWidth > 0) {
            leftX = current.mLeft + (current.width() - mIndicatorWidth) / 2;
            nextLeftX = next.mLeft + (next.width() - mIndicatorWidth) / 2;
            rightX = current.mLeft + (current.width() + mIndicatorWidth) / 2;
            nextRightX = next.mLeft + (next.width() + mIndicatorWidth) / 2;
        } else {
            leftX = current.mLeft + mIndicatorMarginLeft;
            nextLeftX = next.mLeft + mIndicatorMarginLeft;
            rightX = current.mRight - mIndicatorMarginRight;
            nextRightX = next.mRight - mIndicatorMarginRight;
        }
        mIndicatorRect.left = leftX + (nextLeftX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        mIndicatorRect.right = rightX + (nextRightX - rightX) * mEndInterpolator.getInterpolation(positionOffset);
        mIndicatorRect.top = getHeight() - mIndicatorHeight - mIndicatorMarginTop;
        mIndicatorRect.bottom = getHeight() - mIndicatorMarginBottom;
        if(mScrollView!=null){
            mScrollView.invalidate();
        }else{
            invalidate();
        }
         //手指跟随滚动
        if (mScrollView != null && mPositionDataList.size() > 0 && position >= 0 && position < mPositionDataList.size()) {
                int currentPosition = Math.min(mPositionDataList.size() - 1, position);
                int nextPosition = Math.min(mPositionDataList.size() - 1, position + 1);
                PositionData currentScroll = mPositionDataList.get(currentPosition);
                PositionData nextScroll = mPositionDataList.get(nextPosition);
                float scrollTo = currentScroll.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                float nextScrollTo = nextScroll.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                mScrollView.scrollTo((int) (scrollTo + (nextScrollTo - scrollTo) * positionOffset), 0);
        }
    }

    //这里判断选中
    @Override
    public void onPageSelected(int position) {
        changeSelected(LqhTabLayout.this.mCurrentTabPosition, position);
        //不需要动画的话，就在这里改变下标
        if(!mIndicatorAnimEnable){
            calcIndicatorRect(position,0,0);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //提供数据给指示器
    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {

    }

    public void changeSelected(int oldPosition, int newPosition) {
        //重置样式
        resetStateUI();
        //更改选中样式
        updateSelectedTabUI(newPosition);
        //监听都发出去
        dispatchTabListener(oldPosition, newPosition);
        //重新赋值
        mCurrentTabPosition = newPosition;
        mLstTabPosition = oldPosition;
    }

    /**
     * 重置当前按钮的状态
     */
    private void resetStateUI() {
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            if (tab.getLqhTabItemView() != null) {
                tabs.get(i).getLqhTabItemView().refreshTabUI(false);
            }
        }
    }

    private void updateSelectedTabUI(int position) {
        if (tabs.size() > position && tabs.get(position).getLqhTabItemView() != null) {
            tabs.get(position).getLqhTabItemView().refreshTabUI(true);
        }
    }

    //分发监听
    private void dispatchTabListener(int oldPosition, int newPosition) {
        if (selectedListeners.size() > 0) {
            for (LqhTabLayout.BaseOnTabSelectedListener listener : selectedListeners) {
                if (oldPosition == newPosition) {
                    listener.onTabReselected(newPosition);
                } else {
                    if (oldPosition != -1) {
                        listener.onTabUnselected(oldPosition);
                    }
                    listener.onTabSelected(newPosition);
                }
            }
        }
    }

    public void setUnReadNum(int position, int num) {
        setUnReadNum(position, num, 0, 0);
    }

    public void setUnReadNum(int position, int num, int dpRXOffset, int dpTYOffset) {
        if (position < tabs.size() && tabs.get(position).getLqhTabItemView() != null) {
            tabs.get(position).getLqhTabItemView().setUnReadNum(num, dpRXOffset, dpTYOffset);
        }
    }

    public void setUnReadMes(int position, String mes) {
        setUnReadMes(position, mes, 0, 0);
    }

    public void setUnReadMes(int position, String mes, int dpRXOffset, int dpTYOffset) {
        if (position < tabs.size() && tabs.get(position).getLqhTabItemView() != null) {
            tabs.get(position).getLqhTabItemView().setUnReadMes(mes, dpRXOffset, dpTYOffset);
        }
    }

    public void hideUnread(int position) {
        if (position < tabs.size() && tabs.get(position).getLqhTabItemView() != null) {
            tabs.get(position).getLqhTabItemView().hideUnread();
        }
    }
    //这是Scroll模式才有的
    @Override
    public void onDrawIndicator(Canvas canvas) {
        if (
            //isInEditMode()||
                tabs.size() == 0) {
            return;
        }
        if (mIndicatorHeight > 0) {
            //大于零要画下标
            canvas.drawRoundRect(mIndicatorRect,mIndicatorCornerRadius,mIndicatorCornerRadius, mPaint);
        }
    }
    public static class Tab {
        private LqhTabItemView lqhTabItemView;
        private View customView;

        public Tab(LqhTabItemView lqhTabItemView) {
            this.lqhTabItemView = lqhTabItemView;
        }

        public Tab(View customView) {
            this.customView = customView;
        }

        public LqhTabItemView getLqhTabItemView() {
            return lqhTabItemView;
        }

        public void setLqhTabItemView(LqhTabItemView lqhTabItemView) {
            this.lqhTabItemView = lqhTabItemView;
        }

        public View getCustomView() {
            return customView;
        }

        public void setCustomView(View customView) {
            this.customView = customView;
        }
    }

    public void addOnTabSelectedListener(@NonNull LqhTabLayout.BaseOnTabSelectedListener listener) {
        if (!this.selectedListeners.contains(listener)) {
            this.selectedListeners.add(listener);
        }

    }

    public void removeOnTabSelectedListener(@NonNull LqhTabLayout.BaseOnTabSelectedListener listener) {
        this.selectedListeners.remove(listener);
    }

    public void setOnInterruptListener(OnInterruptListener onInterruptListener) {
        this.onInterruptListener = onInterruptListener;
    }

    public int getmCurrentTabPosition() {
        return mCurrentTabPosition;
    }

    public int getItemCount() {
        return tabs == null ? 0 : tabs.size();
    }

    public void setmCurrentTabPosition(final int mCurrentTabPosition) {
        this.post(new Runnable() {
            @Override
            public void run() {
                judgeSelected(LqhTabLayout.this.mCurrentTabPosition,mCurrentTabPosition);
            }
        });

    }

    public class LqhOnClickListener implements OnClickListener {
        private int clickPosition;

        public LqhOnClickListener(int position) {
            this.clickPosition = position;
        }

        @Override
        public void onClick(View v) {
            //判断有没有打断
            judgeSelected(mCurrentTabPosition,clickPosition);
        }
    }
    public void  judgeSelected(int oldPosition,int newPosition){
        if (onInterruptListener == null || onInterruptListener.onInterrupt(newPosition)) {
            if (mViewPager != null) {
                //如果还是同个页签，使用setCurrentItem不会回调OnPageSelected(),所以在此处需要回调点击监听
                if (oldPosition == newPosition) {
                    onPageSelected(newPosition);
                } else {
                    //false：代表快速切换 true：表示切换速度慢
                    if(mIndicatorAnimEnable){
                        mViewPager.setCurrentItem(newPosition, true);
                    }else{
                        mViewPager.setCurrentItem(newPosition, false);
                    }
                }
            } else {
                //是否要动画
                if(!mIndicatorAnimEnable||oldPosition == newPosition){
                    onPageSelected(newPosition);
                }else{
                    //开始动画
                    starAnimator(oldPosition,newPosition);
                }

            }
        }
    }

    private void starAnimator(int oldPosition, int newPosition) {
        float currentPositionOffsetSum = oldPosition;
        if (mScrollAnimator != null) {
            currentPositionOffsetSum = (Float) mScrollAnimator.getAnimatedValue();
            mScrollAnimator.cancel();
            mScrollAnimator = null;
        }
        mScrollAnimator = new ValueAnimator();
        mScrollAnimator.setFloatValues(currentPositionOffsetSum, newPosition);// position = selectedIndex, positionOffset = 0.0f
        mScrollAnimator.addUpdateListener(mAnimatorUpdateListener);
        mScrollAnimator.addListener(mAnimatorListener);
        mScrollAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mScrollAnimator.setDuration(200);
        mScrollAnimator.start();
        onPageSelected(newPosition);
    }
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float positionOffsetSum = (Float) animation.getAnimatedValue();
            int position = (int) positionOffsetSum;
            float positionOffset = positionOffsetSum - position;
            if (positionOffsetSum < 0) {
                position = position - 1;
                positionOffset = 1.0f + positionOffset;
            }
            onPageScrolled(position, positionOffset, 0);
        }
    };
    private Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mScrollAnimator = null;
        }
    };


    public interface OnTabSelectedListener extends LqhTabLayout.BaseOnTabSelectedListener<LqhTabLayout.Tab> {
    }

    public interface BaseOnTabSelectedListener<T extends LqhTabLayout.Tab> {
        void onTabSelected(int position);

        void onTabUnselected(int position);

        void onTabReselected(int position);
    }

    //这个用来是否打断点击事件，触发的后续事件，这个无法截断Viewpager通过手势的界面转换
    public interface OnInterruptListener {
        boolean onInterrupt(int position);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (tabs.size() > 0) {
            preparePositionData();
        }
    }

    private void preparePositionData() {
        mPositionDataList.clear();
        for (int i = 0; i < tabs.size(); i++) {
            PositionData data = new PositionData();
            if (tabs.get(i).getLqhTabItemView() != null) {
                data.mLeft = tabs.get(i).getLqhTabItemView().getLeft();
                data.mTop = tabs.get(i).getLqhTabItemView().getTop();
                data.mRight = tabs.get(i).getLqhTabItemView().getRight();
                data.mBottom = tabs.get(i).getLqhTabItemView().getBottom();
                data.mContentLeft = data.mLeft;
                data.mContentTop = data.mTop;
                data.mContentRight = data.mRight;
                data.mContentBottom = data.mBottom;
            } else {
                data.mLeft = tabs.get(i).getCustomView().getLeft();
                data.mTop = tabs.get(i).getCustomView().getTop();
                data.mRight = tabs.get(i).getCustomView().getRight();
                data.mBottom = tabs.get(i).getCustomView().getBottom();
                data.mContentLeft = data.mLeft;
                data.mContentTop = data.mTop;
                data.mContentRight = data.mRight;
                data.mContentBottom = data.mBottom;
            }
            mPositionDataList.add(data);
        }
    }
    public  void addTabItemDataList(List<TabItemData> datas){
        tabs.clear();
        linContent.removeAllViews();
        for(TabItemData data:datas){
            LqhTabItemView.Builder builder = new LqhTabItemView.Builder(this.getContext());
            LqhTabItemView lqhTabItemView = builder.setSelectedIcon(data.getSelectedIcon())
                    .setNormalIcon(data.getNormalIcon())
                    .setItemText(data.getText()).build();
            Tab tab = new Tab(lqhTabItemView);
            this.addTab(tab);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mScrollView!=null){
            return;
        }
        if (
            //isInEditMode()||
                tabs.size() == 0) {
            return;
        }
        if (mIndicatorHeight > 0) {
            //大于零要画下标
            canvas.drawRoundRect(mIndicatorRect,mIndicatorCornerRadius,mIndicatorCornerRadius, mPaint);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAG, "onDetachedFromWindow");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged");
    }
}
