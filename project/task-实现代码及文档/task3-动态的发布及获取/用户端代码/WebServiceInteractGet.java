package com.example.ll.project_main.Utils;

import android.util.Log;

import com.example.ll.project_main.bean.InteractBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebServiceInteractGet {
    private static List<InteractBean> interacts = new ArrayList<>(  );
    public static List<InteractBean> executeHttpInteract() {

        try {

            URL Url = new URL("http://192.168.43.9:8080/InTravel/SelectInteractServlet");

            HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
            Log.e("getzxl","zhixingle ");
            connection.setRequestProperty("contentType","UTF-8");   //解决中文字符乱码
            InputStream in = connection.getInputStream();   //字节流
            Log.e("chenggong","22");

            //字节流转字符流
            InputStreamReader inputStreamReader = new InputStreamReader(in);   //转换流
            BufferedReader reader = new BufferedReader(inputStreamReader);     //得到字符流
            String str = reader.readLine();
            Log.e("chenggong", String.valueOf(str.length()));

            //解析JSONArray字符串
            JSONArray array = new JSONArray(str);
            Log.e("length", String.valueOf(array.length()));
            for(int i = 0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                InteractBean interact = new InteractBean();
                interact.setInteractId( object.getInt( "interactId" ) );
                interact.setUserName( object.getString( "userName" ) );
                interact.setUserTouxiang( object.getString( "userTouxiang" ) );
                interact.setInteractTime( object.getString( "interactTime" ) );
                interact.setInteractContent( object.getString( "interactContent" ) );
                interact.setInteractPhoto( object.getString( "interactPhoto" ) );
                interact.setInteractPraise( object.getString( "interactPraise" ) );
                Log.e( "hhhhhjjjj",interact.getInteractContent() );
                interacts.add( interact );
            }
            Log.e("SceneList",interacts.toString());
            Log.e("SceneList2",str);
        } catch (MalformedURLException e) {
            Log.e("test", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("test", e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("test", e.toString());
            e.printStackTrace();
        }

        Log.e( "zxl","nicaicai"+interacts.toString() );
        return interacts;
    }
}