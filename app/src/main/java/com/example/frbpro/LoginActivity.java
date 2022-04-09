package com.example.frbpro;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private Button regitButton;
    static String sr = "3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new MyClick());
        regitButton = findViewById(R.id.regitButton);
        regitButton.setOnClickListener(new MyClick1());

        //增加一个点击事件，实现页面跳转
    }

    public class MyClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Toast.makeText(LoginActivity.this, "正在登录中", Toast.LENGTH_SHORT).show();
            EditText editText = (EditText) findViewById(R.id.username);
            String username = editText.getText().toString();
            editText = (EditText) findViewById(R.id.code);
            String code = editText.getText().toString();
            //Toast.makeText(LoginActivity.this,username+code,Toast.LENGTH_LONG).show();
            application test =(application)getApplication();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    test.setId(username);
                    LoginActivity.sr = LoginActivity.sendPost("http://10.192.81.122:8080/user/login", "userName="+username+"&password="+code);
                }
            }).start();
            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //startActivity(intent);


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            switch (sr) {
                case "1":
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case "0":
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case "-1":Toast.makeText(LoginActivity.this, "用户未注册", Toast.LENGTH_SHORT).show();
                    break;
                case "2":Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public class MyClick1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, RegitActivity.class);
            startActivity(intent);
        }
    }

//    public static String sendGet(String url, String param) {
//        String result = "";
//        BufferedReader in = null;
//        try {
//            String urlNameString = url + "?" + param;
//            URL realUrl = new URL(urlNameString);
//            // 打开和URL之间的连接
//            URLConnection connection = realUrl.openConnection();
//            // 设置通用的请求属性
//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            // 建立实际的连接
//            connection.connect();
//            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
//            // 定义 BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            System.out.println("发送GET请求出现异常！" + e);
//            e.printStackTrace();
//        }
//        // 使用finally块来关闭输入流
//        finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//        return result;
//    }


    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}