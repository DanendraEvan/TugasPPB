package com.astral.animasea;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class deskripsiAnime extends AppCompatActivity {

    TextView descriptionText;
    String videoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_anime);

        ImageView imagePoster = findViewById(R.id.imagePoster);
        TextView titleText = findViewById(R.id.titleText);
        descriptionText = findViewById(R.id.descriptionText);
        ImageButton playButton = findViewById(R.id.playButton); // Tambahkan tombol di layout XML

        String title = getIntent().getStringExtra("title");
        int posterResId = getIntent().getIntExtra("posterResId", 0);
        videoUrl = getIntent().getStringExtra("videoUrl");

        titleText.setText(title);
        imagePoster.setImageResource(posterResId);

        fetchAnimeDescription(title); // Ambil deskripsi dari API

        // Saat tombol Play ditekan, buka video
        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(deskripsiAnime.this, PlayVideoActivity.class);
            intent.putExtra("video_url", videoUrl);
            startActivity(intent);
        });
    }

    private void fetchAnimeDescription(String title) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
            String queryUrl = "https://api.jikan.moe/v4/anime?q=" + encodedTitle + "&limit=5";

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, queryUrl, null,
                    response -> {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            boolean found = false;
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject anime = data.getJSONObject(i);
                                String apiTitle = anime.optString("title_english", anime.getString("title"));

                                if (apiTitle.equalsIgnoreCase(title)) {
                                    String description = anime.getString("synopsis");
                                    descriptionText.setText(description);
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                if (data.length() > 0) {
                                    String description = data.getJSONObject(0).getString("synopsis");
                                    descriptionText.setText(description);
                                } else {
                                    descriptionText.setText("Deskripsi tidak ditemukan.");
                                }
                            }
                        } catch (Exception e) {
                            descriptionText.setText("Gagal memuat deskripsi.");
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        descriptionText.setText("Gagal terhubung ke API.");
                        error.printStackTrace();
                    });

            queue.add(request);

        } catch (Exception e) {
            descriptionText.setText("Terjadi kesalahan pada encoding.");
            e.printStackTrace();
        }
    }
}
