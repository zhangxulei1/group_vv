package com.example.ll.project_main.Utils;

import android.util.Log;

import com.example.ll.project_main.bean.InteractBean;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class WebServiceInteractPost {
    public static String executeHttpPost(InteractBean interact, String address){
        HttpURLConnection connection = null;
        InputStream in = null;

        try{
            String urlIp = UrlContent.urlIp;

            String Url = urlIp+"/" + address;
            try {
                URL url = new URL(Url);
                connection = (HttpURLConnection)url.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setReadTimeout(8000);//传递数据超时

                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                String data = "userName" + URLEncoder.encode(interact.getUserName(),"UTF-8") +
                        "&userTouxiang=" + URLEncoder.encode(interact.getUserTouxiang(),"UTF-8") +
                        "&interactTime=" + URLEncoder.encode(interact.getInteractTime(),"UTF-8")+
                        "&interactContent="+URLEncoder.encode( interact.getInteractContent(),"UTF-8" )+
                        "&interactPhoto="+URLEncoder.encode( interact.getInteractPhoto(),"UTF-8" )+
                        "&interactPraise="+URLEncoder.encode( interact.getInteractPraise(),"UTF-8" );
                out.writeBytes(data);
                out.flush();
                out.close();





                int resultCode = connection.getResponseCode();
                if(HttpURLConnection.HTTP_OK == resultCode) {
                    in = connection.getInputStream();
                    return parseInfo(in);
                }
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //意外退出时，连接关闭保护
            if(connection != null){
                connection.disconnect();
            }
            if(in != null){
                try{
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    //得到字节输入流，将字节输入流转化为String类型
    public static String parseInfo(InputStream inputStream){
        BufferedReader reader = null;
        String line = "";
        StringBuilder response = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while((line = reader.readLine()) != null){
                Log.d("add_Interact",line);
                response.append(line);
            }
            Log.d("addInteract","response.toString():"+response.toString());
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
