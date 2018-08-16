package com.demo.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.https.HttpsUtils;
import okhttp3.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.get_text)
    AppCompatTextView textView;

    @OnClick({R.id.send_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_button:
                Log.e("TAG", "onClick: ");
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
     * 方法一：
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
    /*
     * 方法二
     * OkHttp实例
     *
     * */
    private void sendHttp() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient(); //创建OkHttpClient实例
//                    Request request = new Request.Builder()
//                            .url("http://guolin.tech/api/china/") //将地址修改为json地址
//                            .build(); //创建Request对象
//                    Response response = client.newCall(request).execute(); //newCall请求数据，Response对象储存服务器返回数据
//                    //showLineText(response.body().string());
//                    resolveJson(response.body().string());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        HttpUtil.sendOkHttp(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { //返回失败信息
                runOnUiThread(new Runnable() { //从子线程回到主线程中更新UI
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException { //返回成功信息
                Gson gson = new Gson();
                List<Cities> citiesList = gson.fromJson(response.body().string(), new TypeToken<List<Cities>>() {
                }.getType());
                String str = "";
                for (Cities cities : citiesList) {
                    Log.e("TAG",  "\n" + "resolveJson: " + "id:" + cities.getId() + "\t\t\t" + "city:" + cities.getName());
                    str = str + "id:" + cities.getId() + "\t\t\t" + "city:" + cities.getName() + "\n";
                }
                final String finalStr = str;
                runOnUiThread(new Runnable() { //从子线程回到主线程中更新UI
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        textView.setText(finalStr);
                    }
                });
            }
        });
    }

    /*private void showLineText(final String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(str);
            }
        });
    }*/

/*    private void resolveJson(String data) {
        Gson gson = new Gson();
        List<Cities> citiesList = gson.fromJson(data, new TypeToken<List<Cities>>() {
        }.getType());
        for (Cities cities : citiesList) {
//            textView.setText("id:" + cities.getId() + "\n" + "city:" + cities.getName());
            Log.e("TAG", "resolveJson: " + cities.getId() + "\n" + "city:" + cities.getName());
        }
    }*/

    static class HttpUtil {

        public static void sendOkHttp(final okhttp3.Callback callback) {
            OkHttpClient client = new OkHttpClient(); //创建OkHttpClient实例
            Request request = new Request.Builder()
                    .url("http://guolin.tech/api/china/") //将地址修改为json地址
                    .build(); //创建Request对象
            client.newCall(request).enqueue(callback); //newCall请求数据,并回调接口
        }
    }

}
