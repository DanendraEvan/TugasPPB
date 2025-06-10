package com.astral.animasea.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.astral.animasea.ItemAdapter;
import com.astral.animasea.Movie;
import com.astral.animasea.R;

import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment {

    private RecyclerView animeRecyclerView;
    private ItemAdapter itemAdapter;
    private List<Movie> movieList;
    private EditText searchBar;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        animeRecyclerView = view.findViewById(R.id.animeRecyclerView);
        searchBar = view.findViewById(R.id.searchBar);

        // Setup RecyclerView
        animeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));



        // Dummy data
        movieList = new ArrayList<>();
        movieList.add(new Movie("Naruto", R.drawable.poster_naruto, "https://server10.miterequest.my.id/public/86/[nimegami]-a-channel-bd-ep-01-(480p).mp4"));
        movieList.add(new Movie("Fate Stay Night Unlimited Blade Works", R.drawable.poster_fatesnubw, "https://server10.miterequest.my.id/public/86/953e24635e0b3cffcdb7f3087b45a5c436138601-NFuQyQ.mp4"));
        movieList.add(new Movie("Attack on Titan", R.drawable.poster_aot, "https://server10.miterequest.my.id/public/86/[nimegami]-shingeki-no-kyojin-movie-kanketsu-hen---the-last-attack-(480p).mp4"));
        movieList.add(new Movie("Blue Lock", R.drawable.poster_bluelock, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"));
        // Tambahkan data lainnya di sini

        itemAdapter = new ItemAdapter(requireContext(), movieList);
        animeRecyclerView.setAdapter(itemAdapter);

        // Optional: Implementasi search filter (bonus)

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Optional: Fungsi filter berdasarkan keyword

    private void filter(String text) {
        List<Movie> filteredList = new ArrayList<>();
        for (Movie movie : movieList) {
            if (movie.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(movie);
            }
        }
        itemAdapter.updateList(filteredList);
    }

}
