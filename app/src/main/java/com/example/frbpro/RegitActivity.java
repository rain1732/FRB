package com.example.frbpro;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class RegitActivity extends AppCompatActivity {
    private Button regitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regit);
        regitButton = findViewById(R.id.regitButton);
        regitButton.setOnClickListener(new ToMain());
    }

    public class ToMain implements View.OnClickListener{
        @Override
        public void onClick(View v){
            /*
            EditText editText=(EditText)findViewById(R.id.username);
            String username = editText.getText().toString();
            editText = (EditText)findViewById(R.id.code);
            String code = editText.getText().toString();
            Toast.makeText(RegitActivity.this,"",Toast.LENGTH_LONG).show();
        */
            Intent intent = new Intent(RegitActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }
}