package com.example.zjq.my_app.HomePage.Chat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.zjq.my_app.R;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ChatDetails extends AppCompatActivity {

    private List<Message> msgList = new ArrayList<>();
    private EditText mEt;
    private Button send;
    private RecyclerView mRv;
    private MessageAdapter adapter;
    private String line = "";
    private Thread mThread = null;
    String content = null;
    //点击item获取的人物名
    String Name = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.chat_list_details );
        //顶部处理
        Top();
        //消息处理
        MessageS();
        //连接服务器
        connect();
    }

    //---------------------------
    Socket socket = null;
    BufferedWriter writer = null;
    BufferedReader reader = null;

    private void connect() {
        //未连接
        if (socket == null) {
            AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
                @Override
                protected Void doInBackground(Void... arg0) {
                    try {
                        //192.168.137.1 127.0.0.1  10.0.2.2
                        socket = new Socket("192.168.137.1",12345 );
                        String s1 = socket.getLocalAddress().toString()+"";
                        String s2 = socket.getInetAddress().toString()+"";

                        Log.e( "Ip",s1+"\n"+s2 );
                      /*  E/Ip: /192.168.232.2
                                /192.168.137.1*/
                        writer = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream() ) );
                        reader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
                        publishProgress( "@success" );
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                        Looper.prepare();
                        Toast.makeText( ChatDetails.this, "连接服务器失败", Toast.LENGTH_SHORT ).show();
                        finish();
                        Looper.loop();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Looper.prepare();
                        Toast.makeText( ChatDetails.this, "连接服务器失败", Toast.LENGTH_SHORT ).show();
                        finish();
                        Looper.loop();
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    if (values[0].equals( "@success" )) {
                        Toast.makeText( ChatDetails.this, "连接成功！", Toast.LENGTH_SHORT ).show();

                    } else {
                        Toast.makeText( ChatDetails.this, "连接失败！", Toast.LENGTH_SHORT ).show();
                    }
                    super.onProgressUpdate( values );
                }
            };
            read.execute();
        } else {
            Toast.makeText( ChatDetails.this, "已连接", Toast.LENGTH_SHORT ).show();
        }
    }

    private void MessageS() {


        mEt = findViewById( R.id.mEt );
        send = findViewById( R.id.send );
        mRv = findViewById( R.id.mRv );

        //建立数据和RecyclerView之间的联系
        LinearLayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        mRv.setLayoutManager( layoutManager );//制定RecyclerView的布局方式
        adapter = new MessageAdapter( msgList );//将数据传递到MsgAdapter构造函数中
        mRv.setAdapter( adapter );

        send.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socket == null) {
                    //未连接返回消息列表
                    Toast.makeText( ChatDetails.this, "未连接，无法发送", Toast.LENGTH_LONG ).show();
                    finish();
                }

                content = mEt.getText().toString();
                Message msg = new Message( content, Message.TYPE_SENT );
                msgList.add( msg );
                mRv.scrollToPosition( msgList.size() - 1 );

                //输入完成后键盘消失
                ((InputMethodManager) getSystemService( INPUT_METHOD_SERVICE ))
                        .hideSoftInputFromWindow( ChatDetails.this.getCurrentFocus()
                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
                mEt.setText( "" );

                mThread = new Thread( mRunnable );
                mThread.start();
            }
        } );

    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (writer != null) {
                    writer.write( content + "\n" );
                    writer.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    if (reader != null) {
                        while ((line = reader.readLine()) != null) {
                            line += "\n";
                            Log.e( "收到：", line );
                            mHander.sendMessage( mHander.obtainMessage() );
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    };

    Handler mHander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage( msg );

            Message msgToGet = new Message( line.toString(), Message.TYPE_RECEIVED );
            msgList.add( msgToGet );
            mRv.scrollToPosition( msgList.size() - 1 );

            //输入完成后键盘消失
            ((InputMethodManager) getSystemService( INPUT_METHOD_SERVICE ))
                    .hideSoftInputFromWindow( ChatDetails.this.getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );

        }
    };

   /* private void initMsg() {
        Message message1 = new Message( "在吗？", Message.TYPE_RECEIVED );
        msgList.add( message1 );

        Message message2 = new Message( "在,你是哪位？", Message.TYPE_SENT );
        msgList.add( message2 );
    }*/

    private void Top() {

        Name = getIntent().getStringExtra( "name" );
        TextView textView = findViewById( R.id.chat_username );
        textView.setText( Name );

        ImageView imageView = findViewById( R.id.details_back );
        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
    }

}
