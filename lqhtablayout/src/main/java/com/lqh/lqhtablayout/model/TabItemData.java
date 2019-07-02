package com.lqh.lqhtablayout.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Linqh on 2019/6/27.
 *
 * @describe:
 */
public class TabItemData {
    private String text;
    private Drawable normalIcon;
    private Drawable selectedIcon;

    public TabItemData(String text) {
        this.text = text;
    }

    public TabItemData(String text, Drawable normalIcon, Drawable selectedIcon) {
        this.text = text;
        this.normalIcon = normalIcon;
        this.selectedIcon = selectedIcon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
}
