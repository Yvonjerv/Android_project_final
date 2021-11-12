package com.example.finalexam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.finalexam.adapters.Constants;
import com.example.finalexam.entities.News;
import com.example.finalexam.util.HandleOSImagePath;
import com.example.finalexam.util.HttpClientUtils;
import com.google.gson.Gson;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AddNewsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PRIVATE_MODE = 0;
    private EditText edTitle, edOverview;
private Button btnSaveNews, btnCancel;
    private static final int GET_IMG_OK = 11;
private ImageView iv_newsPic;
    private String path="";
    private String suffix="";
    public final static int GET_JSON_SUCCESS=10;
    public   static final String UPLOAD_URL= Constants.SERVER_LINK+"/uploadImage";
    public   static final String POST_URL=Constants.SERVER_LINK+"/postNews";
    private String sessionData="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        edTitle=findViewById(R.id.edTitle);
        edOverview=findViewById(R.id.edOverview);
        btnSaveNews=findViewById(R.id.btnSaveNews);
        btnSaveNews.setOnClickListener(this);
        btnCancel=findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        iv_newsPic=findViewById(R.id.iv_newsPic);
        iv_newsPic.setOnClickListener(this);
        //session
        SharedPreferences prefs = this.getSharedPreferences("SESSION", PRIVATE_MODE);
        sessionData= prefs.getString("SESSION_ID", null);
        Log.i("MSG", sessionData);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_newsPic:
                Log.i("TAG", "OK");
                Intent intent = new Intent();
//                intent.setType("image/*");
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, GET_IMG_OK);
                break;
            case R.id.btnCancel:
                Log.i("TAG", "OK");
                Intent intent2 = new Intent(AddNewsActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
            case R.id.btnSaveNews:
                News news= new News();
                news.setTitle(edTitle.getText().toString());
                news.setOverview(edOverview.getText().toString());
                news.setCreator(sessionData);
                news.setImage(edTitle.getText().toString()+suffix);
                Gson gson= new Gson();
                String json=gson.toJson(news);
                postJson(json);
                if(!path.equals("") && !edTitle.getText().toString().equals("")){
                    uploadImage(path, edTitle.getText().toString());
                }
                Intent intent1=new Intent(AddNewsActivity.this, MainActivity.class);
                startActivity(intent1);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_IMG_OK && resultCode == RESULT_OK) {
            path = HandleOSImagePath.ImagePath(this, data);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            iv_newsPic.setImageBitmap(bitmap);
            int index = path.indexOf(".");
            suffix = path.substring(index);
            Log.i("MSG", path);
        }
    }
    private void uploadImage(String path, String fname) {
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                Map<String, File> fileMap= new HashMap<>();
                fileMap.put("image", new File(path));
                try {
                    HttpClientUtils.HttpMultipartPost(UPLOAD_URL+"?fname="+fname, fileMap, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}
