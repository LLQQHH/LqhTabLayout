package com.example.lqhtablayoutmaster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.lqhtablayout.LqhTabLayout;
import com.example.lqhtablayout.model.TabItemData;
import com.example.lqhtablayout.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LqhTabLayout lqhTab0;
    private LqhTabLayout lqhTab1;
    private LqhTabLayout lqhTab2;
    private LqhTabLayout lqhTab3;
    private LqhTabLayout lqhTab4;
    private LqhTabLayout lqhTab5;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {
            "Php", "IOS", "Android"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lqhTab0 = findViewById(R.id.lqh_tab0);
        lqhTab1 = findViewById(R.id.lqh_tab1);
        lqhTab2 = findViewById(R.id.lqh_tab2);
        lqhTab3 = findViewById(R.id.lqh_tab3);
        lqhTab4 = findViewById(R.id.lqh_tab4);
        lqhTab5 = findViewById(R.id.lqh_tab5);
        addLqhTab();
        ViewPager vp = findViewById(R.id.vp);
        lqhTab1.addOnTabSelectedListener(new LqhTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                showToast("lqhTab1_onTabSelected" + position);
                if(position==0){
                    lqhTab0.setUnReadNum(0,0);
                }else if(position==1){
                    lqhTab0.setUnReadMes(1,"a");
                }else{
                    lqhTab0.setUnReadNum(0,11);
                    lqhTab0.setUnReadMes(1,"bb");
                    lqhTab0.setUnReadNum(2,100);
                }
            }

            @Override
            public void onTabUnselected(int position) {
                showToast("lqhTab1_onTabUnselected" + position);
            }

            @Override
            public void onTabReselected(int position) {
                showToast("lqhTab1_onTabReselected" + position);
            }
        });
        int itemCount = lqhTab1.getItemCount();
        mTitles = new String[itemCount];
        for (int i = 0; i < itemCount; i++) {
            if (i % 2 == 0) {
                mTitles[i] = i + "Php";
            } else if (i % 2 == 1) {
                mTitles[i] = i + "IOS";
            } else {
                mTitles[i] = i + "Android";
            }

        }
        for (int i = 0; i < mTitles.length; i++) {
            mFragments.add(SimpleCardFragment.getInstance(i, mTitles[i]));
        }
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        lqhTab0.setViewPager(vp);
        lqhTab1.setViewPager(vp);
        lqhTab2.setViewPager(vp);
        lqhTab3.setViewPager(vp);
        lqhTab4.setViewPager(vp);
        lqhTab5.setViewPager(vp);
    }

    private void addLqhTab() {
        int itemCount = lqhTab1.getItemCount();
        List<TabItemData> lists = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            TabItemData data = new TabItemData("测试" + (i + 1));
            if (i % 2 == 0) {
                data.setSelectedIcon(UIUtils.getDrawable(this, R.drawable.ic_main_msg_pitchon));
                data.setNormalIcon(UIUtils.getDrawable(this, R.drawable.ic_main_msg_normalcy));
            } else {
                data.setSelectedIcon(UIUtils.getDrawable(this, R.drawable.ic_main_address_pitchon));
                data.setNormalIcon(UIUtils.getDrawable(this, R.drawable.ic_main_address_normalcy));
            }
            lists.add(data);
//              LqhTabItemView.Builder builder = new LqhTabItemView.Builder(this);
//              builder.setItemText("测试"+(i+1));
//              builder.setItemPadTB(UIUtils.dp2px(this,5));
//              if(i%2==0){
//                  builder.setSelectedIcon(UIUtils.getDrawable(this,R.drawable.ic_main_msg_pitchon));
//                  builder.setNormalIcon(UIUtils.getDrawable(this,R.drawable.ic_main_msg_normalcy));
//              }else{
//                  builder.setSelectedIcon(UIUtils.getDrawable(this,R.drawable.ic_main_address_pitchon));
//                  builder.setNormalIcon(UIUtils.getDrawable(this,R.drawable.ic_main_address_normalcy));
//              }
//              lqhTab5.addTab(new LqhTabLayout.Tab(builder.build()));
        }
        lqhTab5.addTabItemDataList(lists);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
