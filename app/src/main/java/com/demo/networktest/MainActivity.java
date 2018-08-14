package com.demo.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.get_text)
    AppCompatTextView textView;

    @OnClick({R.id.send_button})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.send_button:
                Log.e("TAG", "onClick: " );
                sendHttp();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }
/*
*
* HttpsURLConnection方法实现网络请求
*
* */
    /*private void sendHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection connection = null; //创建HttpsURLConnection对象
                BufferedReader reader = null; //创建BufferedReader 字符缓冲输入流
                try{
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000); //设置连接超时
                    connection.setReadTimeout(8000); //设置读取超时
                    InputStream in = connection.getInputStream(); //获取服务器数据
                    reader = new BufferedReader(new InputStreamReader(in)); //将数据放入BufferedReader对象
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.e("TAG", "run: " + response.toString() );
                    showLineText(response.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (reader != null) {
                        try {
                            reader.close(); //释放内存
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();//关闭连接
                    }
                }
            }
        }).start();
    }*/

    private void sendHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient(); //创建OkHttpClient实例
                    Request request = new Request.Builder().url("https://www.baidu.com").build(); //创建Request对象
                    Response response = client.newCall(request).execute(); //newCall请求数据，Response对象储存服务器返回数据
                    showLineText(response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showLineText(final String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(str);
            }
        });
    }
}
