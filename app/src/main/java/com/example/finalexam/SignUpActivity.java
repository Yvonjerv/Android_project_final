package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalexam.adapters.Constants;
import com.example.finalexam.entities.User;
import com.example.finalexam.util.HttpClientUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String POST_URL = Constants.SERVER_LINK+"/postUser";
    private static final int PRIVATE_MODE = 0;
    private Button btn_register, btn_signin;
    private EditText etEmail, etPassword, etPasswordConfirm;
    private TextView tv_validatePassword;
    private String sessionId="" , sessionPwd="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etPasswordConfirm=findViewById(R.id.etPasswordConfirm);
        tv_validatePassword=findViewById(R.id.tv_validatePassword);
        btn_register=findViewById(R.id.btn_login);
        btn_signin=findViewById(R.id.btn_sign);

        btn_signin.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_sign){
            Intent intent=new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.btn_login){
            if(etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())) {
                sessionId=etEmail.getText().toString();
                sessionPwd=etPasswordConfirm.getText().toString();
                SharedPreferences pref=this.getSharedPreferences("SESSION", PRIVATE_MODE);

                SharedPreferences.Editor editor=pref.edit();
                editor.putString("SESSION_ID", sessionId);
                editor.putString("SESSION_PWD",sessionPwd);
                User user= new User();
                user.setEmail(etEmail.getText().toString());
                user.setPassword(etPassword.getText().toString());

                Gson gson= new Gson();
                String json=gson.toJson(user);
                postJson(json);
                Intent intent=new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                tv_validatePassword.setText("Password and confirm Password should be same");
            }
        }
    }
    private void postJson(String json) {
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map= new HashMap<>();
                map.put("json",json);
                try {
                    HttpClientUtils.HttpClientPost(POST_URL, map);
                    Log.i("MSG", "json");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}