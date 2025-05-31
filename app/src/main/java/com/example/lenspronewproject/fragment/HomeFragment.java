package com.example.lenspronewproject.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.lenspronewproject.R;
import com.example.lenspronewproject.photoAdapter;
import com.example.lenspronewproject.photoRepostory;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvPhotos;
    private photoAdapter adapter;

    public HomeFragment() {
        // Konstruktor kosong (wajib untuk Fragment)
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        rvPhotos = view.findViewById(R.id.rvPhotos);
        rvPhotos.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        adapter = new photoAdapter(requireContext());
        rvPhotos.setAdapter(adapter);

        // Muat foto sample dari internet
        adapter.setPhotos(photoRepostory.getInstance(requireContext()).getPhotoUris());
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.setPhotos(photoRepostory.getInstance(requireContext()).getPhotoUris());
    }

    private List<Uri> getSamplePhotoUris() {
        List<Uri> uris = new ArrayList<>();
        uris.add(Uri.parse("https://picsum.photos/300/300?image=10"));
        uris.add(Uri.parse("https://picsum.photos/300/300?image=20"));
        uris.add(Uri.parse("https://picsum.photos/300/300?image=30"));
        uris.add(Uri.parse("https://picsum.photos/300/300?image=40"));
        uris.add(Uri.parse("https://picsum.photos/300/300?image=50"));
        uris.add(Uri.parse("https://picsum.photos/300/300?image=60"));
        return uris;
    }
}
