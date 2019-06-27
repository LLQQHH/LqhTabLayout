package com.example.lqhtablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
    private int normalTextSize; //10sp
    //选择的文本大小
    private int selectedTextSize; //10sp
    //文字和图标的距离
    private int iconMargin;//默认距离dp
    //小红点字体大小
    private int unReadTextSize;//默认8Sp
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
    public LqhTabItemView(Context context) {
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
            normalColor = ta.getColor(R.styleable.LqhTabItemView_itemNormalTextColor,0);
            selectedColor = ta.getColor(R.styleable.LqhTabItemView_itemSelectedColor,0);
             normalTextSize = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemNormalTextSize, 0);
            selectedTextSize = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemSelectedTextSize, 0);
            iconMargin = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemIconMargin, 0);
            unReadTextSize = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemUnreadTextSize, 0);
            unreadTextColor = ta.getColor(R.styleable.LqhTabItemView_itemUnreadTextColor, 0);
            unreadTextBg = ta.getColor(R.styleable.LqhTabItemView_itemUnreadTextBg,0);
            itemPadTB = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemContentPadTB,0);
            itemPadLR = ta.getDimensionPixelSize(R.styleable.LqhTabItemView_itemPadContentLR,0);
            ta.recycle();
            initView();
        }
    }
    private void initView() {
        View view = getView();
        this.addView(view);
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
       if(normalTextSize!=0&&selectedTextSize!=0){
           tvTabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,(isSelected?selectedTextSize:normalTextSize));
       }
       if(selectedColor!=0&&normalColor!=0){
           tvTabTitle.setTextColor(isSelected?selectedColor:normalColor);
       }
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



    public void updateStyle(){
        llTabContent.setPadding(itemPadLR,itemPadTB,itemPadLR,itemPadTB);
        if(!TextUtils.isEmpty(itemText)){
            tvTabTitle.setText(itemText);
        }
        if(normalColor!=0){
            tvTabTitle.setTextColor(normalColor);
        }
        if(normalTextSize!=0){
            tvTabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,normalTextSize);
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvTabTitle.getLayoutParams();
        layoutParams.topMargin=iconMargin;
        if(normalIcon!=null){
            ivTabIcon.setVisibility(View.VISIBLE);
            ivTabIcon.setImageDrawable(normalIcon);
        }else{
            ivTabIcon.setVisibility(View.GONE);
        }
        if(unReadTextSize!=0){
            tvTabUnread.setTextSize(TypedValue.COMPLEX_UNIT_PX,unReadTextSize);
        }
        if(unreadTextColor!=0){
            tvTabUnread.setTextColor(unreadTextColor);
        }
      if(unreadTextBg!=0){
          tvTabUnread.setBackgroundColor(unreadTextBg);
      }
    }

    public Drawable getNormalIcon() {
        return normalIcon;
    }

    public void setNormalIcon(Drawable normalIcon) {
        this.normalIcon = normalIcon;
    }

    public Drawable getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(Drawable selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(int normalColor) {
        this.setNormalColor(normalColor,false);
    }
    public void setNormalColor(int normalColor, boolean needJudge) {

        if (needJudge) {
            //判断是否要用全局颜色
            if (this.normalColor == 0) {
                this.normalColor = normalColor;
            }
        } else {
            this.normalColor = normalColor;
        }
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.setSelectedColor(selectedColor,false);
    }
    public void setSelectedColor(int selectedColor, boolean needJudge) {
        if (needJudge) {
            if (this.selectedColor == 0) {
                this.selectedColor = selectedColor;
            }
        } else {
            this.selectedColor = selectedColor;
        }
    }

    public int getNormalTextSize() {
        return normalTextSize;
    }
    public void setNormalTextSize(int normalTextSize) {
        this.setNormalTextSize(normalTextSize,false);
    }
    public void setNormalTextSize(int normalTextSize, boolean needJudge) {
        if (needJudge) {
            if (this.normalTextSize == 0) {
                this.normalTextSize = normalTextSize;
            }
        } else {
            this.normalTextSize = normalTextSize;
        }
    }
    public int getSelectedTextSize() {
        return selectedTextSize;
    }
    public void setSelectedTextSize(int selectedTextSize) {
        this.setNormalTextSize(selectedTextSize,false);
    }
    public void setSelectedTextSize(int selectedTextSize, boolean needJudge) {
        if (needJudge) {
            if (this.selectedTextSize == 0) {
                this.selectedTextSize = selectedTextSize;
            }
        } else {
            this.selectedTextSize = selectedTextSize;
        }
    }

    public int getIconMargin() {
        return iconMargin;
    }
    public void setIconMargin(int iconMargin) {
        this.setNormalTextSize(iconMargin,false);
    }
    public void setIconMargin(int iconMargin, boolean needJudge) {
        if (needJudge) {
            if (this.iconMargin == 0) {
                this.iconMargin = iconMargin;
            }
        } else {
            this.iconMargin = iconMargin;
        }
    }
    public int getUnReadTextSize() {
        return unReadTextSize;
    }
    public void setUnReadTextSize(int unReadTextSize) {
        this.setNormalTextSize(unReadTextSize,false);
    }
    public void setUnReadTextSize(int unReadTextSize, boolean needJudge) {
        if (needJudge) {
            if (this.unReadTextSize == 0) {
                this.unReadTextSize = unReadTextSize;
            }
        } else {
            this.unReadTextSize = unReadTextSize;
        }
    }
    public int getUnreadTextColor() {
        return unreadTextColor;
    }
    public void setUnreadTextColor(int unreadTextColor) {
        this.setNormalTextSize(unreadTextColor,false);
    }
    public void setUnreadTextColor(int unreadTextColor, boolean needJudge) {
        if (needJudge) {
            if (this.unreadTextColor == 0) {
                this.unreadTextColor = unreadTextColor;
            }
        } else {
            this.unreadTextColor = unreadTextColor;
        }
    }
    public int getUnreadTextBg() {
        return unreadTextBg;
    }
    public void setUnreadTextBg(int unreadTextBg) {
        this.setNormalTextSize(unreadTextBg,false);
    }
    public void setUnreadTextBg(int unreadTextBg, boolean needJudge) {
        if (needJudge) {
            if (this.unreadTextBg == 0) {
                this.unreadTextBg = unreadTextBg;
            }
        } else {
            this.unreadTextBg = unreadTextBg;
        }
    }


    public int getItemPadTB() {
        return itemPadTB;
    }
    public void setItemPadTB(int itemPadTB) {
        this.setNormalTextSize(itemPadTB,false);
    }
    public void setItemPadTB(int itemPadTB, boolean needJudge) {
        if (needJudge) {
            if (this.itemPadTB == 0) {
                this.itemPadTB = itemPadTB;
            }
        } else {
            this.itemPadTB = itemPadTB;
        }
    }

    public int getItemPadLR() {
        return itemPadLR;
    }
    public void setItemPadLR(int itemPadLR) {
        this.setNormalTextSize(itemPadLR,false);
    }
    public void setItemPadLR(int itemPadLR, boolean needJudge) {
        if (needJudge) {
            if (this.itemPadLR == 0) {
                this.itemPadLR = itemPadLR;
            }
        } else {
            this.itemPadLR = itemPadLR;
        }
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
        private int normalTextSize;
        //选择的文本大小
        private int selectedTextSize;
        //文字和图标的距离
        private int iconMargin;
        //小红点字体大小
        private int unReadTextSize;
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
          return new LqhTabItemView(context).build(this);
        }
    }

}
