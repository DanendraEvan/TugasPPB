package com.astral.animasea.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astral.animasea.Movie;
import com.astral.animasea.R;
import com.astral.animasea.SectionAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    RecyclerView recyclerViewHome;
    SectionAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Movie> actionAnime = new ArrayList<>();
        actionAnime.add(new Movie(
                "Naruto",
                R.drawable.poster_naruto,
                "https://server10.miterequest.my.id/public/86/[nimegami]-a-channel-bd-ep-01-(480p).mp4"
        ));
        actionAnime.add(new Movie(
                "Attack on Titan",
                R.drawable.poster_aot,
                "https://server10.miterequest.my.id/public/86/[nimegami]-shingeki-no-kyojin-movie-kanketsu-hen---the-last-attack-(480p).mp4"
        ));

        List<Movie> romanceAnime = new ArrayList<>();
        romanceAnime.add(new Movie(
                "Fate Stay Night Unlimited Blade Works",
                R.drawable.poster_fatesnubw,
                "https://server10.miterequest.my.id/public/86/953e24635e0b3cffcdb7f3087b45a5c436138601-NFuQyQ.mp4"
        ));
        romanceAnime.add(new Movie(
                "Blue Lock",
                R.drawable.poster_bluelock,
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
        ));

        // 🔸 Buat section data
        List<String> sectionTitles = new ArrayList<>();
        sectionTitles.add("Action");
        sectionTitles.add("Romance");

        Map<String, List<Movie>> sectionData = new java.util.HashMap<>();
        sectionData.put("Action", actionAnime);
        sectionData.put("Romance", romanceAnime);

        // 🔸 Set adapter ke RecyclerView


        SectionAdapter adapter = new SectionAdapter(getContext(), sectionTitles, sectionData);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
