package com.example.lqhtablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lqhtablayout.utils.UIUtils;

/**
 * Created by Linqh on 2019/5/30.
 *
 * @describe:
 */
public class LqhTabItemView extends FrameLayout {
    private Context context;
    //正常图标
    private Drawable normalIcon;
    //选中图标
    private Drawable selectedIcon;
    //文字
    private String itemText;
    //正常文本颜色
    private int normalColor;
    //选中的文本颜色
    private int selectedColor;
    //正常文本大小
    private int normalTextSize = 10; //10sp
    //选择的文本大小
    private int selectedTextSize = 10; //10sp
    //文字和图标的距离
    private int iconMargin=2;//默认距离dp
    //小红点字体大小
    private int unReadTextSize=8;//默认8Sp
    //小红点文字颜色
    private int unreadTextColor;
    //小红点背景颜色
    private int unreadTextBg;
    private int itemPadTB;
    private int itemPadLR;
    private ImageView ivTabIcon;
    private TextView tvTabTitle;
    private TextView tvTabUnread;
    private LinearLayout llTabContent;

    //不能直接创建
    private LqhTabItemView(Context context) {
        this(context,null);
    }

    public LqhTabItemView( Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LqhTabItemView( Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initAttrs(context,attrs);
    }
    private LqhTabItemView build( LqhTabItemView.Builder builder){
            this.normalIcon=builder.normalIcon;
            this.selectedIcon=builder.selectedIcon;
            this.itemText=builder.itemText;
            this.normalColor=builder.normalColor;
            this.selectedColor=builder.selectedColor;
            this.normalTextSize=builder.normalTextSize;
            this.selectedTextSize=builder.selectedTextSize;
            this.iconMargin=builder.iconMargin;
            this.unReadTextSize=builder.unReadTextSize;
            this.unreadTextColor=builder.unreadTextColor;
            this.unreadTextBg=builder.unreadTextBg;
            this.itemPadTB=builder.itemPadTB;
            this.itemPadLR=builder.itemPadLR;
            initView();
            return this;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if(attrs!=null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LqhTabItemView);
            normalIcon = ta.getDrawable(R.styleable.LqhTabItemView_itemNormalIcon);
            selectedIcon = ta.getDrawable(R.styleable.LqhTabItemView_itemSelectedIcon);
            itemText = ta.getString(R.styleable.LqhTabItemView_itemText);
            normalColor = ta.getColor(R.styleable.LqhTabItemView_itemNormalTextColor, UIUtils.getColor(context,R.color.default_999999));
            selectedColor = ta.getColor(R.styleable.LqhTabItemView_itemSelectedColor,UIUtils.getColor(context,R.color.selected_ff0000));
             normalTextSize = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemNormalTextSize, UIUtils.sp2px(context, normalTextSize));
            selectedTextSize = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemSelectedTextSize, UIUtils.sp2px(context, selectedTextSize));
            iconMargin = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemIconMargin, UIUtils.dip2Px(context, iconMargin));
            unReadTextSize = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemUnreadTextSize, UIUtils.sp2px(context, unReadTextSize));
            unreadTextColor = ta.getColor(R.styleable.LqhTabItemView_itemUnreadTextColor, UIUtils.getColor(context, R.color.white));
            unreadTextBg = ta.getColor(R.styleable.LqhTabItemView_itemUnreadTextBg,UIUtils.getColor(context,R.color.selected_ff0000));
            itemPadTB = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemContentPadTB,0);
            itemPadLR = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemPadContentLR,0);
            ta.recycle();
            initView();
        }
    }
    private void initView() {
        View view = getView();
        this.addView(view);
        initialize();
    }

    private View  getView() {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.tab_item_layout, null);
        llTabContent = itemView.findViewById(R.id.ll_tabContent);
        ivTabIcon = itemView.findViewById(R.id.iv_tab_icon);
        tvTabTitle = itemView.findViewById(R.id.tv_tab_title);
        tvTabUnread = itemView.findViewById(R.id.tv_tab_unread);
        return itemView;
    }
    public void refreshTabUI(boolean isSelected) {
       if(normalIcon!=null&&selectedIcon!=null){
           ivTabIcon.setVisibility(View.VISIBLE);
           ivTabIcon.setImageDrawable(isSelected?selectedIcon:normalIcon);
       }else{
           ivTabIcon.setVisibility(View.GONE);
       }
        tvTabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,(isSelected?selectedTextSize:normalTextSize));
        tvTabTitle.setTextColor(isSelected?selectedColor:normalColor);
    }
    public void setUnReadMes(String text,int dpRXOffset,int dpTYOffset){
        tvTabUnread.setVisibility(View.VISIBLE);
        UIUtils.show(tvTabUnread,text,unreadTextBg,8,0, Color.parseColor("#ffffff"));
        UIUtils.setUnReadLocation(tvTabUnread,dpRXOffset,dpTYOffset);

    }
    public void setUnReadNum(int num,int dpRXOffset,int dpTYOffset){
        tvTabUnread.setVisibility(View.VISIBLE);
           UIUtils.show(tvTabUnread,num,unreadTextBg,8,0, Color.parseColor("#ffffff"));
           UIUtils.setUnReadLocation(tvTabUnread,dpRXOffset,dpTYOffset);

    }
    public void hideUnread() {
        tvTabUnread.setVisibility(View.GONE);
    }



    public void initialize(){
        llTabContent.setPadding(itemPadLR,itemPadTB,itemPadLR,itemPadTB);
        tvTabTitle.setText(itemText);
        tvTabTitle.setTextColor(normalColor);
        tvTabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,normalTextSize);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvTabTitle.getLayoutParams();
        layoutParams.topMargin=iconMargin;
        if(normalIcon!=null){
            ivTabIcon.setVisibility(View.VISIBLE);
            ivTabIcon.setImageDrawable(normalIcon);
        }else{
            ivTabIcon.setVisibility(View.GONE);
        }
        tvTabUnread.setTextSize(TypedValue.COMPLEX_UNIT_PX,unReadTextSize);
        tvTabUnread.setTextColor(unreadTextColor);
        tvTabUnread.setBackgroundColor(unreadTextBg);
    }






    public ImageView getIvTabIcon() {
        return ivTabIcon;
    }

    public TextView getTvTabTitle() {
        return tvTabTitle;
    }

    public TextView getTvTabUnread() {
        return tvTabUnread;
    }

    public static  class Builder {
        private Context context;
        //正常图标
        private Drawable normalIcon;
        //选中图标
        private Drawable selectedIcon;
        //文字
        private String itemText;
        //正常文本颜色
        private int normalColor;
        //选中的文本颜色
        private int selectedColor;
        //正常文本大小
        private int normalTextSize = 10; //10sp
        //选择的文本大小
        private int selectedTextSize = 10; //10sp
        //文字和图标的距离
        private int iconMargin;//默认距离2dp
        //小红点字体大小
        private int unReadTextSize=8;//默认8Sp
        //小红点文字颜色
        private int unreadTextColor;
        //小红点背景颜色
        private int unreadTextBg;
        //上下边距
        private int itemPadTB;
        //左右边距
        private int itemPadLR;
        public Builder(Context context){
            this.context = context;
            normalColor = UIUtils.getColor(context,R.color.default_999999);
            selectedColor = UIUtils.getColor(context,R.color.selected_ff0000);
            normalTextSize = UIUtils.sp2px(context,10);
            selectedTextSize = UIUtils.sp2px(context,10);
            iconMargin = UIUtils.dip2Px(context,2);
            unReadTextSize =  UIUtils.sp2px(context,8);
            unreadTextColor = UIUtils.getColor(context,R.color.white);
            unreadTextBg = UIUtils.getColor(context,R.color.selected_ff0000);
        }

        public Builder setNormalIcon(Drawable normalIcon) {
            this.normalIcon = normalIcon;
            return this;
        }

        public Builder setSelectedIcon(Drawable selectedIcon) {
            this.selectedIcon = selectedIcon;
            return this;
        }

        public Builder setItemText(String itemText) {
            this.itemText = itemText;
            return this;
        }

        public Builder setNormalColor(int normalColor) {
            this.normalColor = normalColor;
            return this;
        }

        public Builder setSelectedColor(int selectedColor) {
            this.selectedColor = selectedColor;
            return this;
        }

        public Builder setNormalTextSize(int normalTextSize) {
            this.normalTextSize = normalTextSize;
            return this;
        }

        public Builder setSelectedTextSize(int selectedTextSize) {
            this.selectedTextSize = selectedTextSize;
            return this;
        }

        public Builder setIconMargin(int iconMargin) {
            this.iconMargin = iconMargin;
            return this;
        }

        public Builder setUnReadTextSize(int unReadTextSize) {
            this.unReadTextSize = unReadTextSize;
            return this;
        }

        public Builder setUnreadTextColor(int unreadTextColor) {
            this.unreadTextColor = unreadTextColor;
            return this;
        }

        public Builder setUnreadTextBg(int unreadTextBg) {
            this.unreadTextBg = unreadTextBg;
            return this;
        }

        public Builder setItemPadTB(int itemPadTB) {
            this.itemPadTB = itemPadTB;
            return this;
        }

        public Builder setItemPadLR(int itemPadLR) {
            this.itemPadLR = itemPadLR;
            return this;
        }
        public LqhTabItemView build(){
          return   new LqhTabItemView(context).build(this);
        }
    }

}
