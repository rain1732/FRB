package com.example.frbpro;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class RegitActivity extends AppCompatActivity {
    private Button regitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regit);
        regitButton = findViewById(R.id.regitButton);
        regitButton.setOnClickListener(new ToMain());
        Button emailButton = findViewById(R.id.emailButton);
        emailButton.setOnClickListener(new MyClick1());
    }

    public class ToMain implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            EditText editText = (EditText) findViewById(R.id.username);
            String username = editText.getText().toString();
            editText = (EditText) findViewById(R.id.code);
            String code = editText.getText().toString();
            editText = (EditText) findViewById(R.id.code_again);
            String code_again = editText.getText().toString();

            editText = (EditText) findViewById(R.id.sex);
            String sex = editText.getText().toString();

            editText = (EditText) findViewById(R.id.age);
            String age = editText.getText().toString();

            //放入注册成功的块
            Intent intent = new Intent(RegitActivity.this, MainActivity.class);
            startActivity(intent);

            if (!code.equals(code_again)) {
                Toast.makeText(RegitActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();

            } else if (!(sex.equals("男") || sex.equals("女"))) {
                Toast.makeText(RegitActivity.this, "性别错误", Toast.LENGTH_SHORT).show();
            } else {
                //http
                Toast.makeText(RegitActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        LoginActivity.sr=LoginActivity.sendPost("http://10.192.81.122:8080/user/login", ""+username+"&");
                    }
                }).start();
            }

        }

    }

    public class MyClick1 implements View.OnClickListener{
        @Override
        public void onClick(View v){
            EditText editText = (EditText) findViewById(R.id.email);
            String email = editText.getText().toString();
            Toast.makeText(RegitActivity.this, email, Toast.LENGTH_SHORT).show();
            new Thread(new Runnable(){
                @Override
                public void run() {
                    LoginActivity.sr=LoginActivity.sendPost("http://10.192.81.122:8080/user/sendVerification","emailAdd="+email);
                }
            }).start();
        }
    }

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