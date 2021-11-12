package com.example.finalexam.ui;

import android.content.Intent;
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

import com.example.finalexam.AddContactActivity;
import com.example.finalexam.R;
import com.example.finalexam.adapters.Constants;
import com.example.finalexam.adapters.ContactRecyclerViewAdapter;
import com.example.finalexam.entities.User;
import com.example.finalexam.util.HttpCircleImageView;
import com.example.finalexam.util.HttpClientUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;


public class FragmentContacts extends Fragment implements View.OnClickListener {
    public final static int GET_JSON_SUCCESS=10;
    private String url= Constants.SERVER_LINK;
    private RecyclerView rv_contact;
    private FloatingActionButton btn_contact;

    HttpCircleImageView ivContact;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_contacts, container, false);
        rv_contact=view.findViewById(R.id.rv_contact);
        btn_contact=view.findViewById(R.id.btn_contact);
        btn_contact.setOnClickListener(this);
        ivContact=view.findViewById(R.id.ivContact);
        GetContactsThread();
        return view;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==GET_JSON_SUCCESS){
                String json =msg.obj.toString();
                List<User> contactList= parsingJsonToList(json);

                ContactRecyclerViewAdapter adapter= new ContactRecyclerViewAdapter(getContext(), contactList);
                Log.i("MSG", msg.obj.toString());
                LinearLayoutManager manager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                rv_contact.setLayoutManager(manager);
                rv_contact.setAdapter(adapter);
            }
        }
    };
    private List<User> parsingJsonToList(String json){
        List<User> list=null;
        Gson gson= new Gson();
        User[] students= gson.fromJson(json, User[].class);
        list= Arrays.asList(students);
        return list;
    }

    private void GetContactsThread(){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try {
                    String json= HttpClientUtils.HttpClientGet(url+"/getContactServlet", null);

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
        Intent intent= new Intent(getActivity(), AddContactActivity.class);
        startActivity(intent);
    }
}