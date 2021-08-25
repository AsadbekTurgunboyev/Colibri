package com.example.colibri.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.colibri.R;
import com.example.colibri.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    ArrayList<ChatModel> chatList;
    Context context;
    FirebaseUser mUser;
    public final int RIGT_MESSAGE = 0;
    public final int LEFT_MESSAGE = 1;


    public ChatAdapter(ArrayList<ChatModel> list, Context context){
        this.chatList = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
    if(RIGT_MESSAGE == viewType){
        view = LayoutInflater.from(context).inflate(R.layout.right_layout,parent,false);
    }else {
        view = LayoutInflater.from(context).inflate(R.layout.left_layout,parent,false);

    }
    return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        long currentTime = Long.parseLong(chatList.get(position).getTime());
        Date date = new Date(currentTime);
        String time = simpleDateFormat.format(date);

        holder.textChat.setText(chatList.get(position).getMessage());
            holder.timeChat.setHint(time);
    }

    @Override
    public int getItemViewType(int position) {
       mUser = FirebaseAuth.getInstance().getCurrentUser();
       if(mUser.getPhoneNumber().equals(chatList.get(position).getSender())){
           return RIGT_MESSAGE;
       }else {
           return LEFT_MESSAGE;
       }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textChat, timeChat;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textChat = itemView.findViewById(R.id.textChat);
            timeChat = itemView.findViewById(R.id.timeChat);
        }
    }

}
