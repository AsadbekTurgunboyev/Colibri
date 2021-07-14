package com.example.colibri.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.colibri.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private List<String> list;
    private Context context;
    protected PhotoListener photoListener;

    public GalleryAdapter(List<String> list, Context context,PhotoListener photo) {
        this.list = list;
        this.context = context;
        this.photoListener = photo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pic,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String image = list.get(position);
        Glide.with(context).load(image).into(holder.imgFromGallery);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        photoListener.onPhotoClick(image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFromGallery;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFromGallery = itemView.findViewById(R.id.imgFromGallery);
        }
    }
    public interface PhotoListener{
        void onPhotoClick(String path);
    }
}
