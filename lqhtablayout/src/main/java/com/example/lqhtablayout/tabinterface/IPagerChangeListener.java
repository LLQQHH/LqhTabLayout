package com.example.lqhtablayout.tabinterface;

import com.example.lqhtablayout.model.PositionData;

import java.util.List;

/**
 * Created by Linqh on 2019/6/24.
 *
 * @describe:
 */
public interface IPagerChangeListener {
    ///////////////////////// ViewPager的3个回调
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
    void onPageSelected(int position);
    void onPageScrollStateChanged(int state);
    void onPositionDataProvide(List<PositionData> dataList);
}
