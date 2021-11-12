package com.example.finalexam.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.finalexam.LoginActivity;
import com.example.finalexam.MeValueActivity;
import com.example.finalexam.R;
import com.example.finalexam.adapters.Constants;
import com.example.finalexam.entities.User;
import com.example.finalexam.util.HandleOSImagePath;
import com.example.finalexam.util.HttpClientUtils;
import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class FragmentAbout extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final int GET_IMG_OK = 10;
    public   static final String UPLOAD_URL= Constants.SERVER_LINK+"/uploadImage";
    public   static final String POST_URL=Constants.SERVER_LINK+"/updateUser";
    public   static final String url=Constants.SERVER_LINK;
    private static final int BACK_VALUE = 11;
    private static final int PRIVATE_MODE = 0;
    private static final int GET_JSON_SUCCESS = 20;
    String sessionData="";
    private ListView lv_info;
    private ImageView iv_photo;
    private Button btn_save, btn_logout;
    private String path="";
    private String suffix="";
    private TextView tv_email;
    private String[] titles= new String[]{"Name","Phone","Gender"};
private String[] values=new String[]{"","",""};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_about, container, false);
        iv_photo=view.findViewById(R.id.iv_photo);
        btn_save=view.findViewById(R.id.btn_save);
        btn_logout=view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        tv_email=view.findViewById(R.id.tv_email);
        iv_photo.setOnClickListener(this);
        lv_info=view.findViewById(R.id.lv_info);
        lv_info.setAdapter(getInfoAdapter(titles, values));
        lv_info.setOnItemClickListener(this);

        //session
        SharedPreferences prefs = getContext().getSharedPreferences("SESSION", PRIVATE_MODE);
        sessionData= prefs.getString("SESSION_ID", null);
        Log.i("MSG", sessionData);

        tv_email.setText(sessionData);

        String email= sessionData;
        String password=prefs.getString("SESSION_PWD", null);
        Log.i("MSG", password);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_photo:
                Log.i("TAG", "OK");
                Intent intent = new Intent();
//                intent.setType("image/*");
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, GET_IMG_OK);
                break;
            case R.id.btn_logout:
                Intent intent1=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_save:
                User student= new User();
                student.setName(values[0]);
                student.setPhone(values[1]);
                student.setGender(values[2]);
                student.setEmail(sessionData);

                student.setImage(values[1]+suffix);
                Gson gson= new Gson();
                String json=gson.toJson(student);
                postJson(json);
                if(!path.equals("") && !values[1].equals("")){
                    uploadImage(path, values[1]);
                }
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

        if(requestCode==GET_IMG_OK && resultCode==RESULT_OK){
           path= HandleOSImagePath.ImagePath(getContext(), data);
            Bitmap bitmap= BitmapFactory.decodeFile(path);
            iv_photo.setImageBitmap(bitmap);

            int index=path.indexOf(".");
            suffix=path.substring(index);
            Log.i("MSG", path);
//            uploadImage(path, "1001" );
        }
        if(requestCode==BACK_VALUE&& resultCode==111){
        String value=data.getStringExtra("value");
        int pos= data.getIntExtra("pos",0);
        values[pos]=value;
            lv_info.setAdapter(getInfoAdapter(titles, values));
        Log.i("TAG",value);
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


    private SimpleAdapter getInfoAdapter(String[] titles,String[] values){
        List<Map<String, String>> list= new ArrayList<>();
        for(int i=0; i<3;i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", titles[i]);
            map.put("value", values[i]);
            list.add(map);
        }
        SimpleAdapter adapter= new SimpleAdapter(getContext(),list,
                R.layout.me_info_item,
                new String[]{"name","value"},
                new int[]{R.id.tv_id, R.id.tv_type});
        return adapter;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent= new Intent(getActivity(), MeValueActivity.class);
        intent.putExtra("title",titles[position]);
//        intent.putExtra("value", values[position]);
        intent.putExtra("pos", position);
        startActivityForResult(intent, BACK_VALUE);
    }

}