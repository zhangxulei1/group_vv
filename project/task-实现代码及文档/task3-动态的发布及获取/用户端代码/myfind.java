package com.example.ll.project_main.Activity.findActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ll.project_main.R;
import com.example.ll.project_main.Utils.WebServiceInteractGet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myfind extends Activity {
    private ListView interactListView;
    private InteractAdapter interactAdapter;
    private ImageView ivAddInteractMsg;
    private String interactContentText;
    private String discussImageView;
    private int interactIndex;
    private boolean isSetPraise;

    //初始点赞的人数
    private int praiseNumber=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.home_find );

	//刷新，动态从数据库取出来
        Button btnContect = findViewById( R.id.btn_contect );
        btnContect.setOnClickListener( new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                interactTask task = new WebServiceInteractGet.executeHttpInteract();
               task.execute();
           }
        } );

        interactListView = findViewById( R.id.lv_interact_list );
        interactAdapter = new InteractAdapter( this, R.layout.find_item, getList() );
        interactListView.setAdapter( interactAdapter );
        ivAddInteractMsg = findViewById( R.id.iv_add_interact_msg );
        ivAddInteractMsg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setClass( myfind.this, add_interact.class );
                startActivityForResult( intent1, 1 );
            }
        } );
    }


    //获取动态当前时间
    public void getInteractTime(){
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy年MM月dd日 HH时mm分" );
        Date curTime = new Date( System.currentTimeMillis() );
        String str = formatter.format( curTime );
        Log.e("当前时间",str);
    }

    public class InteractAdapter extends BaseAdapter {
        private Context context;
        private int itemid;
        private List<Map<String, Object>> list;

        public InteractAdapter(Context context, int itemid, List<Map<String, Object>> list) {
            this.context = context;
            this.itemid = itemid;
            this.list = list;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get( position );
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from( context );
                convertView = inflater.inflate( itemid, null );
            }

            //获取当前动态时间
            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy年MM月dd日 HH时mm分" );
            Date curTime = new Date( System.currentTimeMillis() );
            String str = formatter.format( curTime );

            ImageView userTouxiang = convertView.findViewById( R.id.iv_users_touxiang );
            TextView userName = convertView.findViewById( R.id.tv_users_name );
            TextView usersContent = convertView.findViewById( R.id.tv_users_content );
            TextView interactTime = convertView.findViewById( R.id.tv_interact_time );
            ImageView usersImage = convertView.findViewById( R.id.iv_users_image );
            TextView setPraiseNumber = convertView.findViewById( R.id.tv_setPraiseNumber );
            final ImageView praiseImage = convertView.findViewById( R.id.iv_dianzan_bottom );

            userTouxiang.setImageResource( (int) list.get( position ).get( "touxiang" ) );
            userName.setText( (String) list.get( position ).get( "username" ) );
            interactTime.setText(str);

            usersContent.setText( (String) list.get( position ).get( "interactContent" ) );
            //设置点赞
            final View finalConvertView = convertView;
            praiseImage.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   TextView setPraiseNumber = finalConvertView.findViewById( R.id.tv_setPraiseNumber );
                    String text = setPraiseNumber.getText().toString();
                    int number = Integer.valueOf( text );
                    if(isSetPraise==false){
                    praiseImage.setImageResource( R.drawable.dianzan_pressed );
                    isSetPraise = true;
                    number++;
                    Log.e( "现在点赞人数：", String.valueOf( praiseNumber ) );
                }else{
                    praiseImage.setImageResource( R.drawable.dianzan );
                    isSetPraise=false;
                    number--;
                    Log.e( "现在点赞人数：", String.valueOf( praiseNumber ) );
                }
                    setPraiseNumber.setText( String.valueOf( number ) );
                }

            } );
            usersImage.setImageResource( (int) list.get( position ).get( "discussImage" ) );
            setPraiseNumber.setText(( String)list.get( position ).get( "praiseNumber" ) );
            //praiseImage.setImageResource( (int)list.get( position ).get( "praiseImage" ));
            return convertView;
        }


	//添加动态信息
        public void addItem(int posi, String interactContentText) {
            int first = interactListView.getFirstVisiblePosition();
            int last = interactListView.getLastVisiblePosition();
            if (posi >= first && posi <= last) {
                View view = interactListView.getChildAt( last - first );
                Map<String, Object> map = new HashMap<>();
                map.put( "touxiang", R.drawable.item_touxiang );
                map.put( "username","用户名" );
                map.put( "interactContent", interactContentText );
                map.put( "discussImage", R.drawable.add_pictrue );
                map.put( "praiseNumber","0" );
                list.add( posi, map );
            }
        }
    }


	//动态的列表信息	
    public List<Map<String, Object>> getList() {

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> mapItem = new HashMap<>();
        mapItem.put( "touxiang", R.drawable.item_touxiang );
        mapItem.put( "username", "用户名" );
        mapItem.put( "setPraiseNumber",praiseNumber );
        mapItem.put( "interactContent", interactContentText );
        mapItem.put( "discussImage", R.drawable.add_pictrue );
        mapItem.put( "praiseNumber",String.valueOf(praiseNumber));
        list.add( mapItem );

        return list;
    }


	//接受发布的信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            if (data != null) {
                interactContentText = data.getStringExtra( "interactContent" );

                //discussImageView = data.getStringExtra( "discussImage" );
                interactIndex = data.getIntExtra( "position", 0 );
                interactAdapter.addItem( interactIndex, interactContentText );
            }
        }
    }
}
