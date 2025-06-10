package com.astral.animasea;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Movie> movieList;
    private Context context;

    public ItemAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.imagePoster.setImageResource(movie.getImageResId());

        // Saat item diklik, buka aktivitas deskripsiAnime
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, deskripsiAnime.class); // BUKAN PlayVideoActivity langsung
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("posterResId", movie.getImageResId());
            intent.putExtra("videoUrl", movie.getVideoUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePoster;
        TextView title;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePoster = itemView.findViewById(R.id.imagePoster);
            title = itemView.findViewById(R.id.title);
        }
    }

    public void updateList(List<Movie> newList) {
        movieList = newList;
        notifyDataSetChanged();
    }
}
