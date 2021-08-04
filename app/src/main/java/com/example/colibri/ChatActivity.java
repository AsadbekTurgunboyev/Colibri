package com.example.colibri;

import static com.example.colibri.adapter.ShowUserAdapter.getName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String hisUid;
    EditText writingTxtChat;
    TextView nameChat,place_holderChat;
    FirebaseDatabase database;
    DatabaseReference reference;
    String myUid;
    ImageView avatarChat, sendImageChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth = FirebaseAuth.getInstance();
        initViews();
        hisUid = getIntent().getStringExtra("hisUid");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
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
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
             myUid = user.getPhoneNumber();
        }

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
}