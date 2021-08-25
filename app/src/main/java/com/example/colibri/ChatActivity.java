package com.example.colibri;

import static com.example.colibri.adapter.ShowUserAdapter.getName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.colibri.adapter.ChatAdapter;
import com.example.colibri.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity implements TextWatcher {
    FirebaseAuth mAuth;
    String hisUid;
    EditText writingTxtChat;
    TextView nameChat,place_holderChat;
    FirebaseDatabase database;
    DatabaseReference reference;
    String myUid;
    RecyclerView chatRec;
    ArrayList<ChatModel> chatList;
    boolean isSeen = false;
    ImageView avatarChat, sendImageChat, file_ic, voice_ic,emoji;
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth = FirebaseAuth.getInstance();

        initViews();
        EmojiPopup popup = EmojiPopup.Builder.fromRootView(findViewById(R.id.root_view)).build(writingTxtChat);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setStackFromEnd(true);
        chatRec.setHasFixedSize(true);
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.toggle();
            }
        });
        chatRec.setLayoutManager(lin);
        hisUid = getIntent().getStringExtra("hisUid");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        writingTxtChat.addTextChangedListener(this);
        sendImageChat.setOnClickListener(view -> {
            String writetext = writingTxtChat.getText().toString().trim();
            if(!writetext.isEmpty()){
                sendMessage(writetext);
            }else {
                Toast.makeText(ChatActivity.this, "Bo'sh maydon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String writetext) {
        DatabaseReference writeReference = FirebaseDatabase.getInstance().getReference();
        String time = String.valueOf(System.currentTimeMillis());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("time", time);
        hashMap.put("isSeen", isSeen);
        hashMap.put("reciver",hisUid);
        hashMap.put("message", writetext);
        writeReference.child("Chats").push().setValue(hashMap);
        writingTxtChat.setText("");


    }

    private void initViews() {
        nameChat = findViewById(R.id.textView6);
        place_holderChat = findViewById(R.id.place_holderChat);
        avatarChat = findViewById(R.id.avatarChat);
        sendImageChat = findViewById(R.id.sendImageChat);
        writingTxtChat = findViewById(R.id.writingTxtChat);
        chatRec = findViewById(R.id.chatRec);
        file_ic = findViewById(R.id.file_ic);
        voice_ic = findViewById(R.id.voice_ic);
        emoji = findViewById(R.id.emoji);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMessage();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
             myUid = user.getPhoneNumber();
            getData();

        }

    }

    private void getMessage() {
        chatList = new ArrayList<>();
        DatabaseReference messageReference = FirebaseDatabase.getInstance().getReference("Chats");
        messageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
               for (DataSnapshot ds : snapshot.getChildren()){
                    ChatModel chatModel = ds.getValue(ChatModel.class);

                   if (chatModel != null) {
                       if((chatModel.getSender().equals(myUid) && chatModel.getReciver().equals(hisUid)) || (chatModel.getReciver().equals(myUid) && chatModel.getSender().equals(hisUid))){
                           chatList.add(chatModel);


                       }else {
                       }
                   }
               }

                ChatAdapter chatAdapter = new ChatAdapter(chatList,ChatActivity.this);
                chatAdapter.notifyDataSetChanged();
               chatRec.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getData() {
        Query query = reference.child(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = ""+snapshot.child("name").getValue();
                nameChat.setText(name);
                int color = Integer.parseInt(String.valueOf(snapshot.child("avatar_color").getValue()));
               place_holderChat.setBackgroundTintList(ColorStateList.valueOf(color));
                place_holderChat.setText(getName(name));
                avatarChat.setVisibility(View.INVISIBLE);
                Glide.with(ChatActivity.this)
                        .asBitmap()
                        .circleCrop()
                        .load(snapshot.child("pic").getValue())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady( Bitmap resource,  Transition<? super Bitmap> transition) {
                                avatarChat.setImageBitmap(resource);
                                place_holderChat.setVisibility(View.INVISIBLE);
                                avatarChat.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadCleared(Drawable placeholder) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        file_ic.setVisibility(View.GONE);
        voice_ic.setVisibility(View.GONE);
        sendImageChat.setVisibility(View.VISIBLE);
        if(charSequence.length() == 0){
            sendImageChat.setVisibility(View.GONE);
            file_ic.setVisibility(View.VISIBLE);
            voice_ic.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}