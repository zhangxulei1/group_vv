package com.example.ll.project_main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.ll.project_main.R;
import com.example.ll.project_main.WebServiceGet;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText userphone;
    private EditText password;
    private Button btnLogin;
    private Button btnRegister;
    private TextView infotv,regtv;
    private String info; //服务器返回的数据
    //提示框
    private ProgressDialog dialog;
    private LinearLayout mLinearLayout;
    private GestureDetector mGestureDetector;
    //QQ登录
    private static final String TAG = "MainActivity";
    private static final String APP_ID = "1105602574";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //点击忘记密码时，跳转到输入手机号页面
        TextView forget = findViewById(R.id.tv_forget);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Login.this,InputphoneActivity.class);
                startActivity(intent);
            }
        });
        //背景图片虚化
        mLinearLayout = (LinearLayout) findViewById(R.id.background);
        mLinearLayout.getBackground().setAlpha(180);
        //初始化信息
        userphone = (EditText)findViewById(R.id.et_user);
        password = (EditText)findViewById(R.id.et_password);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnRegister = (Button)findViewById(R.id.btn_register);
        infotv = (TextView)findViewById(R.id.info);


//        QQ第三方登录！！
//        传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID, Login.this.getApplicationContext());
        //获取QQ登录的控件
        ImageView qqImage = findViewById(R.id.iv_qq);
        //QQ登录绑定监听事件
        qqImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
                 官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
                 第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
                mIUiListener = new BaseUiListener();
                //all表示获取所有权限
                mTencent.login(Login.this, "all", mIUiListener);
            }
        });


        //设置按钮监听器
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        //判断手机号码格式
        userphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                }else {

                    if (userphone.getText().toString().trim().length() != 11) {
                        Toast.makeText(Login.this, "您的手机号位数不正确", Toast.LENGTH_LONG).show();
                        /*userphone.requestFocus();*/
                      //  password.clearFocus();
                    } else {
                        phone_number = userphone.getText().toString().trim();
                        String num = "[1][358]\\d{9}";
                        if (phone_number.matches(num)){
                            Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_LONG).show();

                        }
                        else {
                            Toast.makeText(Login.this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }
        });
        //密码输入框焦点事件
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if(TextUtils.isEmpty(password.getText().toString().trim())){
                        Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                //设置提示框
                dialog = new ProgressDialog(Login.this);
                dialog.setTitle("正在登录");
                dialog.setMessage("请稍后");
                dialog.setCancelable(false);//设置可以通过back键取消
                dialog.show();
                //设置子线程，分别进行Get和Post传输数据
                new Thread(new MyThread()).start();

                Intent intent = new Intent(Login.this,HeadActivity.class);
                //声明一个编辑框和布局文件中id为edit_message的编辑框链接起来。
                EditText editText =findViewById(R.id.et_user);
                //把编辑框获取的文本赋值给String类型的message
                String message = editText.getText().toString();
                //给message起一个名字，并传给另一个activity
                intent.putExtra("phone",message);

                //启动意图
                startActivity(intent);

                break;
            case R.id.btn_register:
                //跳转注册页面
                Intent intent1 = new Intent(Login.this,Register.class);
                startActivity(intent1);
                break;
        }
    }

    public  class MyThread implements Runnable{
        @Override
        public void run() {
            info = WebServiceGet.executeHttpGet(userphone.getText().toString(),password.getText().toString(),"LogLet");
            // /获取服务器返回的数据
            //更新UI，使用runOnUiThread()方法
            showResponse(info);
        }
    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            //更新UI
            @Override
            public void run() {
                if("true".equals(response)){
                    Toast.makeText(Login.this,"登录成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(  );
                    intent.setClass( Login.this,HeadActivity.class);
                    startActivity( intent );
                }else {
                    Toast.makeText(Login.this,"登录失败！", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    //QQ第三方登录
    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            Toast.makeText(Login.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG, "登录成功" + response.toString());
                        //登录成功后，跳到首页
                        startActivity(new Intent(getBaseContext(),HeadActivity.class));

                    }
                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }
                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onError(UiError uiError) {
            Toast.makeText(Login.this, "授权失败", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel() {
            Toast.makeText(Login.this, "授权取消", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
