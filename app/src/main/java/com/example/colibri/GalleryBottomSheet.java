package com.example.colibri;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colibri.adapter.GalleryAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryBottomSheet extends BottomSheetDialogFragment {
    View view;
    RecyclerView recyclerView;
    List<String> listImages;
    GalleryAdapter adapter ;
    Uri imageUri;
    int MY_READ_PERMISSION_CODE = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.sheet_dialog,container,false);
        initViews(view);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        } else {
            getImages();
        }

        return view;
    }

    private void getImages() {
        listImages = new ArrayList<>();
        listImages = GalleryImage.lisofImages(getContext());
        adapter = new GalleryAdapter(listImages, getContext(), new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
            imageUri = Uri.fromFile(new File(path));
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setCropMenuCropButtonTitle("Ok")
                        .setActivityTitle("Suratni kesing")
                        .setFixAspectRatio(true)
                        .start(getActivity());
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_READ_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Suratlarni yuklash uchun ruxsat berildi", Toast.LENGTH_SHORT).show();
                getImages();
            } else {
                Toast.makeText(getContext(), "Ruxsat berilmadi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
