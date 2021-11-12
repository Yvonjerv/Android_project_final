package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MeValueActivity extends AppCompatActivity  implements View.OnClickListener {
private EditText et_value;
private Button btn_back;
private String title, value;
private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_value);

        et_value=findViewById(R.id.et_value);
        btn_back=findViewById(R.id.btn_back);
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
//        value=intent.getStringExtra("value");
        pos=intent.getIntExtra("pos",0);
        et_value.setHint(title);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("value", et_value.getText().toString());
        intent.putExtra("pos",pos);
        setResult(111, intent);
        finish();
    }

}