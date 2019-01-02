package com.example.zjq.my_app.HomePage.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zjq.my_app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatList extends AppCompatActivity{

    final private  static int TO_DETAILS=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.chat_list );

        initChatList();
        ImageView imageView1=findViewById(R.id.chat_back  );
        Listener listener=new Listener();
        imageView1.setOnClickListener( listener );

    }


    //自定义监听器类
    private class Listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.chat_back:
                {
                    finish();
                }
                break;

            }

        }
    }


    private void initChatList() {
        List<Map<String,Object>> chatList=getChatList();
        MyAdapter adapter=new MyAdapter(this,R.layout.chat_list_item,chatList);
        ListView listView=findViewById( R.id.chat_list );
        listView.setAdapter( adapter );

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView Name=view.findViewById( R.id.tv_name );
                String sName= Name.getText().toString();

                Intent intent=new Intent( getApplicationContext(),ChatDetails.class );
                intent.putExtra("name",sName  );

                startActivityForResult( intent,TO_DETAILS );
            }
        } );

    }

    public List<Map<String,Object>> getChatList() {

        Map<String,Object> map1=new HashMap<>(  );
        map1.put( "name","客户聊天室" );
        map1.put( "context","大家说的话" );
        map1.put( "image",R.drawable.p2 );


        List<Map<String,Object>> list=new ArrayList<>(  );

        list.add( map1 );

        return list;
    }

    private class MyAdapter  extends BaseAdapter{

        private Context context;
        private int itemId;
        private List<Map<String,Object>> data;

        public MyAdapter(Context context,int itemId,List<Map<String,Object>> data){
            this.context=context;
            this.itemId=itemId;
            this.data=data;
        }


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get( position );
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                LayoutInflater inflater=LayoutInflater.from( context );
                convertView=inflater.inflate( itemId,null );
            }
            TextView textName=convertView.findViewById( R.id.tv_name );
            TextView context=convertView.findViewById( R.id.tv_context );
            ImageView image=convertView.findViewById( R.id.iv_image );
            TextView time=convertView.findViewById( R.id.tv_time );

            Map<String,Object> map=data.get( position );
            textName.setText( (String)map.get( "name" ) );
            context.setText( (String )map.get( "context" ));
            image.setImageResource( (int)map.get( "image" ) );

            SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd mm:ss");
            String dateStr = dateformat.format(System.currentTimeMillis());

            time.setText( dateStr);

            return convertView;
        }
    }
}
