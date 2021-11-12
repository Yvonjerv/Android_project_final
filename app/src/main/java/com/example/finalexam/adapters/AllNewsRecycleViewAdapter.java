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

public class AllNewsRecycleViewAdapter extends RecyclerView.Adapter<AllNewsRecycleViewAdapter.NewsAllItemViewHolder> {
    Context context;
    private List<News> list;

    String Root_image_url=Constants.SERVER_LINK+"/photo/";
    //String imageUrl=Constants.SERVER_LINK+"/imagesNews/";
    public AllNewsRecycleViewAdapter(Context context, List<News> list){
        this.context=context;
        this.list=list;
}

    @NonNull
    @NotNull
    @Override
    public NewsAllItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_all_item, parent, false);
        NewsAllItemViewHolder holder= new NewsAllItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewsAllItemViewHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvOverview.setText(list.get(position).getOverview());
        holder.tvDate.setText(list.get(position).getDate_created());
        holder.tvCreator.setText(list.get(position).getCreator());
        holder.ivNews.setHttpImage(Root_image_url+list.get(position).getImage());
        holder.ivCreat.setHttpImage(Root_image_url+list.get(position).getCreatorImage());
    }

    @Override
    public int getItemCount() {
        Log.i("MSG", list.size()+"");
        return list.size();
    }

    public class NewsAllItemViewHolder extends RecyclerView.ViewHolder{
        public TextView tvTitle;
        public TextView tvOverview;
        public TextView tvDate;
        public TextView tvCreator;
        public HttpCircleImageView ivNews;
        public HttpCircleImageView ivCreat;

        public NewsAllItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitl);
            tvOverview=itemView.findViewById(R.id.tvOver);
            ivNews=itemView.findViewById(R.id.ivCreator);
            ivCreat=itemView.findViewById(R.id.ivCreat);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvCreator=itemView.findViewById(R.id.tvCreator);
        }
    }
}
