package com.astral.animasea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {

    private List<String> sectionTitles;
    private Map<String, List<Movie>> sectionData;
    private Context context;

    public SectionAdapter(Context context, List<String> titles, Map<String, List<Movie>> data) {
        this.context = context;
        this.sectionTitles = titles;
        this.sectionData = data;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        String sectionTitle = sectionTitles.get(position);
        List<Movie> movies = sectionData.get(sectionTitle);

        holder.sectionTitle.setText(sectionTitle);

        ItemAdapter movieAdapter = new ItemAdapter(context,movies);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(movieAdapter);
    }

    @Override
    public int getItemCount() {
        return sectionTitles.size();
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTitle;
        RecyclerView recyclerView;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.sectionTitle);
            recyclerView = itemView.findViewById(R.id.recyclerViewMovies);
        }
    }
}
