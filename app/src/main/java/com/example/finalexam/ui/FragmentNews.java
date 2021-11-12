package com.example.finalexam.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalexam.AddNewsActivity;
import com.example.finalexam.R;
import com.example.finalexam.adapters.Constants;
import com.example.finalexam.adapters.NewsRecycleViewAdapter;
import com.example.finalexam.entities.News;
import com.example.finalexam.util.HttpCircleImageView;
import com.example.finalexam.util.HttpClientUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;


public class FragmentNews extends Fragment implements View.OnClickListener {
    public final static int GET_JSON_SUCCESS=10;
    private String url= Constants.SERVER_LINK;
    private RecyclerView rv_news;
    HttpCircleImageView ivNews;
    private FloatingActionButton btn_news;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_news, container, false);
        rv_news=view.findViewById(R.id.rv_news);
        btn_news=view.findViewById(R.id.btn_news);
        btn_news.setOnClickListener(this);
        ivNews=view.findViewById(R.id.ivCreator);
        GetNewsThread();
        return view;
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==GET_JSON_SUCCESS){
                String json =msg.obj.toString();
                List<News> newsList= parsingJsonToList(json);

                NewsRecycleViewAdapter adapter= new NewsRecycleViewAdapter(getContext(), newsList);
                Log.i("MSG", msg.obj.toString());
             //   LinearLayoutManager manager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                GridLayoutManager manager1=new GridLayoutManager(getContext(),  2);
                rv_news.setLayoutManager(manager1);

                rv_news.setAdapter(adapter);
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
                    String json= HttpClientUtils.HttpClientGet(url+"/getNewsServlet", null);
                    if(!json.equals("")){
                        Message message=new Message();
                        message.what=GET_JSON_SUCCESS;
                        message.obj=json;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getActivity(), AddNewsActivity.class);
        startActivity(intent);
    }
}