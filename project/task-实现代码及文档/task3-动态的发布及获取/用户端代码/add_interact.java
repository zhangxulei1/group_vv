package com.example.ll.project_main.Activity.findActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.ll.project_main.HomeFind;
import com.example.ll.project_main.R;
import com.example.ll.project_main.Register;
import com.example.ll.project_main.Utils.WebServiceInteractPost;
import com.example.ll.project_main.bean.InteractBean;
import com.example.ll.project_main.Utils.WebServiceInteractPost;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class add_interact extends Activity {

    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private Bitmap bitmap;
    private InteractBean interactBean;

    //发动态页面的选取图片
    private ImageView addInteractPictrue;
    //发动态页面的选取内容
    private EditText edAddInteractContent;
    private String srtContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.home_find_add );

        addInteractPictrue = findViewById( R.id.iv_add_picture );
        edAddInteractContent = findViewById( R.id.ed_content );

        final Intent intent = new Intent();
        //发动态的内容
        //Object strContent = edAddInteractContent.setText( "" );
        //发动态的示例图片
        addInteractPictrue.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        } );

        //返回动态及发布动态
        ImageView interactCancel = findViewById(R.id.iv_back_find);
        ImageView addInteract = findViewById(R.id.iv_add);

        //发布
        addInteract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new AddInteractThread()).start();
            }
        });

        //返回fragment动态
        interactCancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

    }
    // 弹出PopupWindow
    protected void showPopupWindow() {
        // 1. 创建PopupWindow显示的view
        View view = View.inflate(this,R.layout.popup_window, null);
        // 2. 按钮添加监听器
        Button btnPhotoFromLocal = view.findViewById(R.id.btn_photoFromLocal);
        Button btnPhotoFromCamera = view.findViewById(R.id.btn_photoFromCamera);
        Button btnPhotoCancel = view.findViewById(R.id.btn_photoCancel);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;

        final PopupWindow popupWindow = new PopupWindow(view,weight,height);

        //点击外部popupWindow消失
        popupWindow.setOutsideTouchable(true);

        btnPhotoFromLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                choosePicture(CHOOSE_PICTURE);
                Log.e("choosepicture","执行到这里");

            }
        });

        btnPhotoFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                takeCamera(TAKE_PICTURE);
                Log.e("takecamera","执行到这里");


            }
        });

        btnPhotoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,50);
    }

    private void choosePicture(int choosePicture) {
        //从本地相册中进行选择
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
        startActivityForResult(intentFromGallery,0);
    }

    private void takeCamera(int takePicture) {
        //启动相机程序
        Intent intentFromCapture = new Intent( MediaStore.ACTION_IMAGE_CAPTURE); ;
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File( Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
        startActivityForResult(intentFromCapture, 1);
    }

    //重写onActivityResult方法
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    File tempFile = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
                    cutImage(Uri.fromFile(tempFile)); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理

                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
    //保存裁剪之后的图片数据
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo= extras.getParcelable("data");
            ImageView mImage=findViewById(R.id.iv_add_picture);
            mImage.setImageBitmap(photo);//显示图片
        }
    }
    //裁剪图片方法实现
    private void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    private class InteractThread implements Runnable {
        @Override
        public void run() {
            interactBean.setUserName( "lily" );
            interactBean.setUserTouxiang( "R.drawable.item_touxiang");
            interactBean.setInteractTime( getInteractTime());
            interactBean.setInteractContent( edAddInteractContent.getText().toString() );
            interactBean.setInteractPraise( "R.drawable.add_interact_msg" );
            interactBean.setInteractPraise( "0" );
            //获取服务器返回数据
            //String RegRet = WebServiceGet.executeHttpGet(regUserName.getText().toString(),regPassWord.getText().toString(),"RegLet");
            String InteractRet = WebServiceInteractPost.executeHttpPost( interactBean, "InteractServlet" );


            //更新UI，界面处理
            showInteract( InteractRet );
        }
    }

        private void showInteract(final String InteractRet) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e( "222", "2222" );
                    if ("true".equals( InteractRet )) {

                        Intent intent = new Intent();
                        intent.setClass( add_interact.this, addInteractSuccess.class );
                        startActivity( intent );
                    } else {
                        Intent intent = new Intent();
                        intent.setClass( add_interact.this, addInteractFailure.class );
                    }
                }
            });
        }
    //获取动态当前时间
    public String getInteractTime(){
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy年MM月dd日 HH时mm分" );
        Date curTime = new Date( System.currentTimeMillis() );
        String str = formatter.format( curTime );
        return str;
    }


    private class AddInteractThread implements Runnable {
        @Override
        public void run() {
            String strInteract = WebServiceInteractPost.executeHttpPost( interactBean,"InteractServlet" );
            showReq(strInteract);
        }
    }

	//发布动态页面的变化
    private void showReq(final String strInteract) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("222","2222");
                if("true".equals(strInteract)){
                    Log.e("222","2");
                    AlertDialog.Builder builder = new AlertDialog.Builder(add_interact.this);
                    builder.setMessage("发布成功");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(add_interact.this,myfind.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(add_interact.this);
                    builder.setMessage("发布失败");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(add_interact.this,myfind.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }
        });
    }
}