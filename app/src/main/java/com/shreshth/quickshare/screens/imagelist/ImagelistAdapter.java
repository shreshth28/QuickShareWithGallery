package com.shreshth.quickshare.screens.imagelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shreshth.quickshare.R;

import java.util.ArrayList;

public class ImagelistAdapter extends RecyclerView.Adapter<ImagelistAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> imagePaths=new ArrayList<>();
    public ImagelistAdapter(ArrayList<String> imagePaths,Context context) {
        this.imagePaths.addAll(imagePaths);
        this.context=context;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Toast.makeText(context, position+" ", Toast.LENGTH_SHORT).show();
        Glide.with(context).load(imagePaths.get(position)).into(holder.galleryImageView);
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView galleryImageView;
        public ViewHolder(View view){
            super(view);
            galleryImageView=view.findViewById(R.id.gallery_image_view);
        }
    }
}
