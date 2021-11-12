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
import com.example.finalexam.entities.News;
import com.example.finalexam.entities.User;
import com.example.finalexam.util.HttpClientUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PRIVATE_MODE = 0;
    private String POST_URL= Constants.SERVER_LINK+"/userLogin";
    private Button btn_login, btn_sign;
private EditText etEmail, etPassword;
private TextView tv_validatePwd;
    private String sessionId="" , sessionPwd="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btn_login=findViewById(R.id.btn_login);
        btn_sign=findViewById(R.id.btn_sign);
        tv_validatePwd=findViewById(R.id.tv_validatePwd);
        btn_sign.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_login){

            sessionId=etEmail.getText().toString();
            sessionPwd=etPassword.getText().toString();
            //SharedPreferences pref=this.getSharedPreferences("Your PREF_NAME",Private_Mode);

            if(!sessionId.equals("") && !sessionPwd.equals("")){
                SharedPreferences pref=this.getSharedPreferences("SESSION", PRIVATE_MODE);

                SharedPreferences.Editor editor=pref.edit();
                editor.putString("SESSION_ID", sessionId);
                editor.putString("SESSION_PWD",sessionPwd);
            editor.commit();
            String email= etEmail.getText().toString();
            String password=etPassword.getText().toString();

            sendToServer(email, password);
            Log.i("MSG", sessionId);
            Intent intent= new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            }else {
                tv_validatePwd.setText("Wrong input");
            }
        }
        if(v.getId()==R.id.btn_sign){
            Intent intent= new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
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
                    Log.i("MSG", "success");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void sendToServer(String email, String password){
        User user= new User();
        user.setEmail(email);
        user.setPassword(password);
        Gson gson= new Gson();
        String json=gson.toJson(user);
        postJson(json);
    }
}