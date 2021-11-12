package com.example.finalexam.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalexam.R;
import com.example.finalexam.adapters.AllNewsRecycleViewAdapter;
import com.example.finalexam.adapters.Constants;
import com.example.finalexam.adapters.NewsRecycleViewAdapter;
import com.example.finalexam.entities.News;
import com.example.finalexam.util.HttpCircleImageView;
import com.example.finalexam.util.HttpClientUtils;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class FragmentHome extends Fragment {
    public final static int GET_JSON_SUCCESS=10;
    private String url= Constants.SERVER_LINK;
    private RecyclerView rv_all;
    HttpCircleImageView ivNews, ivCreat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        rv_all=view.findViewById(R.id.rv_all);
        ivNews=view.findViewById(R.id.ivCreator);
        ivCreat=view.findViewById(R.id.ivCreat);
        GetNewsThread();
        return view;
    }
    private Handler handler=new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==GET_JSON_SUCCESS){
                String json =msg.obj.toString();
                List<News> newsList= parsingJsonToList(json);
                AllNewsRecycleViewAdapter  adapter= new AllNewsRecycleViewAdapter(getContext(), newsList);
                Log.i("MSG", msg.obj.toString());
                LinearLayoutManager manager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                rv_all.setLayoutManager(manager);
                rv_all.setAdapter(adapter);
            }
        }
    };
    private List<News> parsingJsonToList(String json){
        List<News> list=null;
        Gson gson= new Gson();
        News[] news= gson.fromJson(json, News[].class);
        list= Arrays.asList(news);
        return list;
    }

    private void GetNewsThread(){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try {
                    String json= HttpClientUtils.HttpClientGet(url+"/getAllNewsServlet", null);
                    if(!json.equals("")){
                        Message message=new Message();
                        message.what=GET_JSON_SUCCESS;
                        message.obj=json;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }
}