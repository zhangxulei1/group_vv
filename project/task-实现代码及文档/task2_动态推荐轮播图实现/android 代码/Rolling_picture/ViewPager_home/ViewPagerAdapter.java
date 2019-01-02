package com.example.zjq.my_app.HomePage.Rolling_picture.ViewPager_home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.zjq.my_app.R;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;//上下文
    private LayoutInflater layoutInflater;
    private  int[] datas;

    public ViewPagerAdapter(Context context,int[] datas){
        this.context=context;
        this.datas=datas;
        layoutInflater=LayoutInflater.from( context );

    }

    //渲染每一页的数据
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View layout=layoutInflater.inflate( R.layout.viewpager_item ,null);
        ImageView iv=layout.findViewById( R.id.item_iv );
        //设置显示的图片,取余，无限轮播
        iv.setImageResource( datas[position%datas.length] );
        //方法二
        //iv.setImageResource( datas[position] );
        container.addView( layout );

        return layout;

    }

    //返回当前数据有几条
    @Override
    public int getCount() {
        //方法一：int值取最大值，欺骗法，无限轮播
       return Integer.MAX_VALUE;
       //return datas.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }



    //
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View) object );
    }
}
