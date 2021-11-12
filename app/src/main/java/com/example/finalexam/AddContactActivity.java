package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.finalexam.adapters.Constants;
import com.example.finalexam.entities.Contact;
import com.example.finalexam.util.HttpClientUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PRIVATE_MODE = 0;
    private EditText et_contact;
private Button btn_add_user, btn_cancel_user;
    public   static final String POST_URL= Constants.SERVER_LINK+"/postContactServlet";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        et_contact=findViewById(R.id.et_contact);
        btn_add_user=findViewById(R.id.btn_add_user);
        btn_cancel_user=findViewById(R.id.btn_cancel_user);
        btn_add_user.setOnClickListener(this);
        btn_cancel_user.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_user:
                Log.i("TAG", "OK");
                Intent intent2 = new Intent(AddContactActivity.this, MainActivity.class);
                startActivity(intent2);
                break;

            case R.id.btn_add_user:
                //session
                SharedPreferences prefs = this.getSharedPreferences("SESSION", PRIVATE_MODE);
                String sessionData= prefs.getString("SESSION_ID", null);
                Log.i("MSG", sessionData);

                Contact contact= new Contact();
                contact.setUserA(sessionData);
                contact.setUserB(et_contact.getText().toString());

                Gson gson= new Gson();
                String json=gson.toJson(contact);
                postJson(json);

                Intent intent=new Intent(AddContactActivity.this, MainActivity.class);
                startActivity(intent);
                break;
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