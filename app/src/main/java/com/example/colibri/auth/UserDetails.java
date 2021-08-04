package com.example.colibri.auth;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.colibri.MainActivity;
import com.example.colibri.R;
import com.example.colibri.model.UserDetailModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetails extends AppCompatActivity {
    DatabaseReference database;
    EditText inputName, inputUserName;
    Button sendButton1;
    Bundle bundle;
    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        bundle = getIntent().getExtras();
        database = FirebaseDatabase.getInstance().getReference();
        initView();
        sendButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeDatabase();
                Intent intent = new Intent(UserDetails.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void writeDatabase() {
       String id = bundle.get("id").toString();
        UserDetailModel detailModel = new UserDetailModel(inputName.getText().toString(),inputUserName.getText().toString(),"");
        database.child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("name").setValue(inputName.getText().toString());
                ColorGenerator generator = ColorGenerator.MATERIAL;
                String a = String.valueOf(generator.getRandomColor());
                snapshot.getRef().child("avatar_color").setValue(a);
                snapshot.getRef().child("UserName").setValue(inputUserName.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        sendButton1 = findViewById(R.id.sendButton1);
        inputName = findViewById(R.id.inputName);
        inputUserName = findViewById(R.id.inputUserName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        inputName.setTranslationX(300);
        inputUserName.setTranslationY(300);
        inputUserName.setAlpha(v);
        inputName.setAlpha(v);
        inputName.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        inputUserName.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();

    }
}