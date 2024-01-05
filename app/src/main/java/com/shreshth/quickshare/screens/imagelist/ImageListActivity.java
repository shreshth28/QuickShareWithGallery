package com.shreshth.quickshare.screens.imagelist;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import com.shreshth.quickshare.R;
import com.shreshth.quickshare.screens.common.BaseActivity;

import java.util.ArrayList;

public class ImageListActivity extends BaseActivity {

    private static final String PERMISSION_REQUEST_READ_MEDIA_IMAGES= Manifest.permission.READ_MEDIA_IMAGES;
    private static final int PERMISSION_REQ_CODE=100;
    private RecyclerView imageListRecyclerView;

    private ArrayList<String> imagePaths=new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        requestRuntimePermission();
    }

    private void requestRuntimePermission() {
        if(checkSelfPermission(PERMISSION_REQUEST_READ_MEDIA_IMAGES)==PackageManager.PERMISSION_GRANTED){
           imagePaths= fetchImages(this);
           for(String path:imagePaths){
               Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
           }


        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(this,PERMISSION_REQUEST_READ_MEDIA_IMAGES)){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("This app requires READ_MEDIA_IMAGES to show images from the gallery.")
                    .setTitle("Permission Required")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        ActivityCompat.requestPermissions(this,new String[]{PERMISSION_REQUEST_READ_MEDIA_IMAGES},PERMISSION_REQ_CODE);
                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancel",((dialog, which) -> {
                        dialog.dismiss();
                    }));
            builder.show();
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{PERMISSION_REQUEST_READ_MEDIA_IMAGES},PERMISSION_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==PERMISSION_REQ_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted. You can now use the applicaiton.", Toast.LENGTH_SHORT).show();
                requestRuntimePermission();
            }
            else if(!ActivityCompat.shouldShowRequestPermissionRationale(this,PERMISSION_REQUEST_READ_MEDIA_IMAGES)){
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMessage("Application unavailable to use because permissions have not been granted")
                        .setTitle("Permission required")
                        .setCancelable(false)
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Settings", (dialog, which) -> {
                            Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri=Uri.fromParts("package",getPackageName(),null);
                            intent.setData(uri);
                            startActivity(intent);
                            dialog.dismiss();
                        });
                builder.show();
            }
            else{
                requestRuntimePermission();
            }
        }
    }


    public ArrayList<String> fetchImages(Context context) {
        ArrayList<String> images = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String imagePath = cursor.getString(columnIndex);
                images.add(imagePath);
            }
            cursor.close();
        }
        return images;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageListRecyclerView=findViewById(R.id.imageListRecyclerView);
        imageListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImagelistAdapter imagelistAdapter=new ImagelistAdapter(imagePaths,this);
        imageListRecyclerView.setAdapter(imagelistAdapter);
        imagelistAdapter.notifyDataSetChanged();
    }
}