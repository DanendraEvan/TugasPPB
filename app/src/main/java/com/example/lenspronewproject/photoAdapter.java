package com.example.lenspronewproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lenspronewproject.R;

import java.util.List;

public class photoAdapter extends RecyclerView.Adapter<photoAdapter.PhotoViewHolder> {

    private List<Uri> photoList;
    private final Context context;

    public photoAdapter(Context context) {
        this.context = context;
    }

    public void setPhotos(List<Uri> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged(); // agar RecyclerView langsung refresh
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri uri = photoList.get(position);

        // Load foto
        Glide.with(context).load(uri).into(holder.imageView);

        // Set status icon favorite
        if (favManager.isFavorite(uri)) {
            holder.favoriteButton.setImageResource(R.drawable.like); // ganti dengan ikon ❤️ penuh
        } else {
            holder.favoriteButton.setImageResource(R.drawable.like_border); // ikon ❤️ kosong
        }

        // Aksi klik tombol favorite
        holder.favoriteButton.setOnClickListener(v -> {
            if (favManager.isFavorite(uri)) {
                favManager.removeFavorite(uri);
                holder.favoriteButton.setImageResource(R.drawable.like_border);
            } else {
               favManager.addFavorite(uri);
                holder.favoriteButton.setImageResource(R.drawable.like);
            }
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return photoList != null ? photoList.size() : 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView favoriteButton;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView); // gambar utama
            favoriteButton = itemView.findViewById(R.id.btn_favorite); // tombol ❤️
        }
    }
}
