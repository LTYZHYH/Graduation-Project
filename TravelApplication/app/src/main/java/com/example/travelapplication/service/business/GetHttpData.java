package com.example.travelapplication.service.business;

import android.os.AsyncTask;

import com.example.travelapplication.service.httprequest.TransmitHttpData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetHttpData extends AsyncTask<String,Void,String> {

    private String URL;
    private TransmitHttpData transmitHttpData;
    public GetHttpData(String URL, TransmitHttpData transmitHttpData)
    {
        //其他活动在调用此类时需要传递访问的网址
        this.URL=URL;
        this.transmitHttpData=transmitHttpData;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            //传递网址
            java.net.URL url=new URL(URL);
            try {
                //打开网络链接
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                //以GET的方式访问网络
                httpURLConnection.setRequestMethod("GET");
                //设置最长等待时间为5秒
                httpURLConnection.setConnectTimeout(5000);
                //获得网络返回的代码
                int code=httpURLConnection.getResponseCode();
                //code == 200表示网络请求成功
                if (code==200)
                {
                    InputStream json = httpURLConnection.getInputStream();
                    //将网页返回的数据解析成字符串数据
                    BufferedReader reader=new BufferedReader(new InputStreamReader(json,"UTF-8"));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null)
                    {
                        response.append(line);
                    }
                    return response.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
