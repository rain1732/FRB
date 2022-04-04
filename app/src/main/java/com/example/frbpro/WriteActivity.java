package com.example.frbpro;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.os.Bundle;
import android.os.FileObserver;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WriteActivity extends Activity implements OnClickListener{

    private Button read, write,delete,show;
    private EditText et1,et2;
    //声明一个要写入的文件名,默认存储路径为data文件夹下
    final String FILE_NAME = "myFile.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        read = (Button)findViewById(R.id.btn1);
        write = (Button)findViewById(R.id.btn2);
        delete = (Button)findViewById(R.id.btn3);
        show = (Button)findViewById(R.id.btn4);
        read.setOnClickListener(this);
        write.setOnClickListener(this);
        delete.setOnClickListener(this);
        show.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                try {
                    //读取文件通过openFileInput
                    BufferedReader bufr = new BufferedReader(new InputStreamReader(openFileInput(FILE_NAME),"utf-8"));
                    String line = "";
                    String result = "";
                    while((line = bufr.readLine()) != null){
                        result += line;
                    }
                    //把读取的内容显示到Activity中
                    et2.setText(result);
                    bufr.close();
                    Toast.makeText(WriteActivity.this, "读取文件成功", 0).show();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                } catch (FileNotFoundException e1) {
                    //如果文件不存在,在FileNotFoundException中说明
                    Toast.makeText(WriteActivity.this, "文件不存在", 0).show();
                    e1.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                break;
            case R.id.btn2:
                try {
                    //写入文件通过openFileOutput
                    FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    fos.write(et1.getText().toString().getBytes());
                    fos.close();
                    Toast.makeText(WriteActivity.this, "写入文件成功", 0).show();
                    et1.setText("");
                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                break;
            case R.id.btn3:
                //删除文件
                deleteFile(FILE_NAME);
                Toast.makeText(WriteActivity.this, "删除文件成功", 0).show();
                break;
            case R.id.btn4:
                //列出文件中内容的名字
                String[] file = fileList();
                for (int i = 0; i < file.length; i++) {
                    Log.i("main", file[i]);
                    Toast.makeText(WriteActivity.this, file[i].toString(), 0).show();
                }
                break;

            default:
                break;
        }
    }



}