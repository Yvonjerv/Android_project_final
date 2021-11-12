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
import com.example.finalexam.entities.News;
import com.example.finalexam.util.HttpCircleImageView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewsRecycleViewAdapter extends RecyclerView.Adapter<NewsRecycleViewAdapter.NewsItemViewHolder> {
    Context context;
    private List<News> list;
    String Root_image_url=Constants.SERVER_LINK+"/photo/";
    public NewsRecycleViewAdapter(Context context, List<News> list){
        this.context=context;
        this.list=list;
}

    @NonNull
    @NotNull
    @Override
    public NewsItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        NewsItemViewHolder holder= new NewsItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewsItemViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvDate.setText(list.get(position).getDate_created());
        holder.ivNews.setHttpImage(Root_image_url+list.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        Log.i("MSG", list.size()+"");
        return list.size();
    }

    public class NewsItemViewHolder extends RecyclerView.ViewHolder{
        public TextView tvTitle;
        public TextView tvDate;
        public HttpCircleImageView ivNews;

        public NewsItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitl);
            ivNews=itemView.findViewById(R.id.ivCreator);
            tvDate=itemView.findViewById(R.id.tvDate);
        }
    }
}
