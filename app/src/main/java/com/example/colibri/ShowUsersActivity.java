package com.example.colibri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.colibri.adapter.ShowUserAdapter;
import com.example.colibri.model.UserDetailModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowUsersActivity extends AppCompatActivity {
        FirebaseDatabase database;
        DatabaseReference reference;
        RecyclerView recyclerView;
        String user;
        ArrayList<UserDetailModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        initViews();
        getIntentData();
    }

    private void getIntentData() {
        user = getIntent().getStringExtra("auth");
        Toast.makeText(this, ""+user, Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.showRec);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        arrayList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    UserDetailModel model = ds.getValue(UserDetailModel.class);
                    if(!model.getPhone().equals(user)){
                        arrayList.add(model);
                    }
                }
                ShowUserAdapter adapter = new ShowUserAdapter(arrayList,ShowUsersActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}