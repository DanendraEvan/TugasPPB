package com.example.lenspronewproject.fragment;

import static java.security.AccessController.getContext;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lenspronewproject.R;
import com.example.lenspronewproject.favManager;
import com.example.lenspronewproject.photoAdapter;
import com.example.lenspronewproject.photoRepostory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class favFragment extends Fragment {

    private RecyclerView recyclerView;
    private photoAdapter adapter;
    private photoRepostory repository;

    public favFragment() {
        // Konstruktor kosong
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);

        recyclerView = view.findViewById(R.id.recycler_bookmark);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        adapter = new photoAdapter(getContext());
        recyclerView.setAdapter(adapter);

        repository = photoRepostory.getInstance(requireContext());

        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        Set<Uri> favorites = favManager.getFavorites();
        adapter.setPhotos(new ArrayList<>(favorites)); // Konversi Set ke List
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }
}

