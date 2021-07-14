package com.example.colibri;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colibri.adapter.GalleryAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    FloatingActionButton fbn;
    GalleryBottomSheet galleryBottomSheet;
    Uri resulUriImage;
    ImageView profileImage;
    RecyclerView recyclerView;
    TextView profName;
    FirebaseStorage storage;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    DatabaseReference database;
    StorageReference reference;
    GalleryAdapter adapter;
    ArrayList<String> images;
    String id;
    int MY_READ_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Users");
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        mUser = mAuth.getCurrentUser();
        id = mUser.getPhoneNumber();


        galleryBottomSheet = new GalleryBottomSheet();
        fbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
//                } else {
                galleryBottomSheet.show(getSupportFragmentManager(), "TAG");
//                }

            }
        });
        getAllData();


    }


    private void initViews() {
        fbn = findViewById(R.id.fbn);
        profileImage = findViewById(R.id.profiliImage);
        profName = findViewById(R.id.textView3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                resulUriImage = resultUri;
                profileImage.setImageURI(resultUri);
                String imgId = System.currentTimeMillis() + ".jpg";
                StorageReference ref = reference.child("images/").child(id + "/" + imgId);
                ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(SettingsActivity.this, "Yuklandi", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                database.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("pic").setValue(imgId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                galleryBottomSheet.dismiss();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("xato", error.getMessage());
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getAllData() {
        Query query = database.orderByChild("phone").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dp : snapshot.getChildren()) {
                    String name = "" + dp.child("name").getValue();
                    String pic = "" + dp.child("pic").getValue();
                    Toast.makeText(SettingsActivity.this, "" + pic, Toast.LENGTH_SHORT).show();
                    profName.setText(name);
                    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                    mStorage.child("images/" + id + "/").child(pic).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.e("rasmm", uri.toString());
                            Picasso.get().load(uri).into(profileImage);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}