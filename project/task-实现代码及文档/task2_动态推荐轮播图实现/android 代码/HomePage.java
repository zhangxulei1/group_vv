package com.example.zjq.my_app.HomePage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.zjq.my_app.HomePage.Chat.ChatList;
import com.example.zjq.my_app.HomePage.Rolling_picture.ViewPager_home.Images;
import com.example.zjq.my_app.HomePage.Rolling_picture.ViewPager_home.ViewPagerAdapter;
import com.example.zjq.my_app.HomePage.Top.FadingScrollView;
import com.example.zjq.my_app.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomePage extends Fragment  {

    //top
    private View layout;
    private FadingScrollView fadingScrollView;

    private final static int XIAOXI_ITEM=1;

    private ViewPager vp;
    private ViewPagerAdapter viewPagerAdapter;



    //方法二
    //private int[] datas =new int[Images.imageArray.length+2];

    //自动轮播定时器
    private ScheduledExecutorService scheduledExecutorService;
    //当前图片的索引号
    private int currentIndex;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState ){
        View view=  inflater.inflate( R.layout.home_page,container,false );


        return view;
    }

    public void onViewCreated(View view,
                              Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );

        //消息
        ImageView imageView=getActivity().findViewById( R.id.chat );
        Listener listener=new Listener();
        imageView.setOnClickListener( listener );


        //二 初始化数据
       // initDatas();

        //轮播图
        vp=view.findViewById( R.id.vp );

        //适配器初始化
        viewPagerAdapter=new ViewPagerAdapter( getContext(), Images.imageArray );
       // viewPagerAdapter=new ViewPagerAdapter( getContext(), datas );
        //绑定
        vp.setAdapter( viewPagerAdapter );

           //绑定监听器
       //vp.addOnPageChangeListener(new ViewPageChangeListener() );
        //方法一,4的倍数，往左边滚动，
        vp.addOnPageChangeListener( new ViewPagerChangeListener() );

        currentIndex= Images.imageArray.length*1000;
        vp.setCurrentItem( Images.imageArray.length*1000,true );

        //方法二 3- 0-1-2-3 -0  多设置俩个view 最初在0位置
       // vp.setCurrentItem( 1,true );

        //沉浸式顶部状态栏
        Top();


    }

    private void Top() {
        //5.0以后
        if (Build.VERSION.SDK_INT>=21){

            //隐藏状态栏， actionBar
            View  decorView=getActivity().getWindow().getDecorView();
            int option=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            /*| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*/
            decorView.setSystemUiVisibility( option );
            //getActivity().getWindow().setNavigationBarColor( Color.TRANSPARENT );
            getActivity().getWindow().setStatusBarColor( Color.TRANSPARENT );

        }


        layout =getView().findViewById( R.id.nac_layout );
        layout.setAlpha( 0 );

        fadingScrollView=getView().findViewById( R.id.nac_root );
        fadingScrollView.setFadingView( layout );
        fadingScrollView.setFadingHeightView( getView().findViewById( R.id.image ) );
    }


    //自定义监听器类
    private class Listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                //弹出消息列表，并连接聊天服务器
                case R.id.chat:
                {

                    //跳转
                    Intent intent=new Intent( getContext(), ChatList.class );
                    startActivityForResult( intent,XIAOXI_ITEM);

                }
                break;

            }

        }
    }


    //自动轮播
    //当界面可见的时候每隔2秒切换一次图片
    @Override
    public void onStart() {
        super.onStart();
        //初始化自动轮播定时器
        scheduledExecutorService =Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate( new Runnable() {
            @Override
            public void run() {
                //切换图片
                currentIndex++;
                //刷新控件
               /* 不可以在,这是子线程，不能直接调用主线程UI，想调用用handler
                vp.setCurrentItem( currentIndex );*/
                handler.sendEmptyMessage( 1 );
            }
        } ,3,3, TimeUnit.SECONDS);

    }

    private Handler handler =new Handler(  ){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            switch (msg.what){
                case 1:
                    vp.setCurrentItem( currentIndex );

            }
        }
    };

   class ViewPagerChangeListener implements ViewPager.OnPageChangeListener{

       @Override
       public void onPageScrolled(int i, float v, int i1) {

       }

       @Override
       public void onPageSelected(int position) {
           currentIndex=position;

       }
       @Override
       public void onPageScrollStateChanged(int i) {

       }
   }

    //当界面不可见的时候停止
    @Override
    public void onStop() {
        super.onStop();
        //? !=
        if (scheduledExecutorService!=null){
            scheduledExecutorService.shutdown();
        }
    }


   /* //方法二 构造数据源
    private void initDatas(){
        //第一项
        datas[0]=Images.imageArray[Images.imageArray.length-1];

        //中间
        for (int i=0;i<Images.imageArray.length;i++){

            datas[i+1]=Images.imageArray[i];
        }

        //第6项
        datas[datas.length-1]=Images.imageArray[0];
    }

    //二 设置监听器
    class ViewPageChangeListener implements ViewPager.OnPageChangeListener{
        //当前应该选中的页面的索引
        int pageIndex;
        //当前在0

        @Override
        public void onPageSelected(int position) {

         pageIndex=position;
               //3-  0-1-2-3  -0
            //往左划
            if (position==0){
                //当 当前视图为第一个时（3） 用户向左边滑动 则将当前页面设置为数据源的倒数第二张 （3）
                pageIndex=datas.length-2;
            }
            //划到最后一个 往右划
            else if (position==datas.length-1){
                pageIndex=1;
            }
            if (position!=pageIndex){
                vp.setCurrentItem( pageIndex,true);
            }


        }

    }*/
}

