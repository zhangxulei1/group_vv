package com.example.zjq.my_app.HomePage.Top;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class FadingScrollView extends ScrollView {

    private static String TAG="-----------FadingScrollView----------";

    //渐变View
    private View fadingView;

    //图片上滑到完全消失，actionbar显示，过程中透明度增加
    private View fadingHeightView;//一张图
    private  int oldY;

    //滑动500时完全显示
    private  int fadingHeight=500;


    public FadingScrollView(Context context) {
        super( context );
    }

    public FadingScrollView(Context context, AttributeSet attrs) {
        super( context, attrs );
    }

    public FadingScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
    }


    public void setFadingView(View view){this.fadingView=view;}
    public void setFadingHeightView(View v){this.fadingHeightView=v;}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );
        if (fadingHeightView!=null){
            fadingHeight=fadingHeightView.getMeasuredHeight();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged( l, t, oldl, oldt );

        //l,t 滑动后的xy位置 oldl oldt 滑动前的位置
        float fading=t>fadingHeight?fadingHeight:(t>30?t:0);
        updateActionbarAlpha( fading/fadingHeight );
    }

    void updateActionbarAlpha(float alpha) {
        try {
            setActionBarAlpha(alpha);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setActionBarAlpha(float alpha) throws Exception {
        if (fadingView==null){
            throw new Exception( "fadingView is null" );

        }
        fadingView.setAlpha( alpha );
    }
}
