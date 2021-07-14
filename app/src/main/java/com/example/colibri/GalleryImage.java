package com.example.colibri;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.StringReader;
import java.util.ArrayList;

public class GalleryImage {


    public static ArrayList<String> lisofImages(Context context) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        String absolutePatchImage;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA};
//                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null,
                orderBy + " DESC");
//        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, null);
        //column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            absolutePatchImage = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            listOfAllImages.add(absolutePatchImage);
        }
        cursor.close();
        return listOfAllImages;
    }
}
