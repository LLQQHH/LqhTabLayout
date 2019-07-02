package com.lqh.lqhtablayoutmaster;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class SimpleCardFragment extends Fragment {
    private  int position;
    private String mTitle;

    public static SimpleCardFragment getInstance(int position,String title) {
        SimpleCardFragment sf = new SimpleCardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putString("title",title);
        sf.setArguments(bundle);
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
             position = getArguments().getInt("position");
            mTitle = getArguments().getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_simple_card, null);
        TextView card_title_tv = (TextView) v.findViewById(R.id.card_title_tv);
        card_title_tv.setText("第"+(position)+"界面"+mTitle);
        return v;
    }
}