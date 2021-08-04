package com.example.colibri.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.colibri.ChatActivity;
import com.example.colibri.R;
import com.example.colibri.model.UserDetailModel;

import java.util.ArrayList;

public class ShowUserAdapter extends RecyclerView.Adapter<ShowUserAdapter.Viewholder> {
    ArrayList<UserDetailModel> mList;
    Context context;
    String hisUid;
    public ShowUserAdapter (ArrayList<UserDetailModel> list, Context context){
       this.mList = list;
       this.context = context;
    }
    @Override
    public Viewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_item,parent,false);
        return  new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowUserAdapter.Viewholder holder, int position) {
        String nameLetter = mList.get(position).getName();
        holder.user_name.setText(nameLetter);
        int color = Integer.parseInt(mList.get(position).getAvatar_color());
        holder.place_holder.setBackgroundTintList(ColorStateList.valueOf(color));
        holder.place_holder.setText(getName(nameLetter));
        holder.avatar.setVisibility(View.INVISIBLE);
        Glide.with(context)
                .asBitmap()
                .circleCrop()
                .load(mList.get(position).getPic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady( Bitmap resource,  Transition<? super Bitmap> transition) {
                        holder.avatar.setImageBitmap(resource);
                        holder.place_holder.setVisibility(View.INVISIBLE);
                        holder.avatar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {

                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                hisUid = mList.get(position).getPhone();
                intent.putExtra("hisUid",hisUid);
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView place_holder, user_name;
        ImageView avatar;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            place_holder = itemView.findViewById(R.id.place_holder);
            avatar = itemView.findViewById(R.id.avatar);
            user_name = itemView.findViewById(R.id.textView4);
        }
    }
    public static String getName(String nameLetter) {
        String letter;
        String[] asd = nameLetter.split(" ");
        if(asd.length == 1){
            letter = asd[0].substring(0,2);
        }else {
            letter = asd[0].substring(0,1) +asd[1].substring(0,1);
        }
        return letter.toUpperCase();
    }
}
