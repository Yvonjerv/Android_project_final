package com.example.finalexam.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalexam.R;
import com.example.finalexam.entities.User;
import com.example.finalexam.util.HttpCircleImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactItemViewHolder>{
    Context context;
    private List<User> list;

    String imageUrl=Constants.SERVER_LINK+"/photo/";

    public ContactRecyclerViewAdapter(Context context, List<User> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @NotNull
    @Override
    public ContactItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_view, parent, false);
        ContactItemViewHolder holder= new ContactItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ContactItemViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getName());
        holder.tvPhone.setText(list.get(position).getPhone());
        holder.ivContact.setHttpImage(imageUrl+list.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        Log.i("MSG", list.size()+"");
        return list.size();
    }

    public class ContactItemViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvPhone;
        public HttpCircleImageView ivContact;

        public ContactItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvTitle);
            tvPhone=itemView.findViewById(R.id.tvOverview);
            ivContact=itemView.findViewById(R.id.ivContact);
        }
    }
}
