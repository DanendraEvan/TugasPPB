package com.astral.animasea;

public class Movie {
    private String title;
    private int imageResId;
    private String videoUrl; // Tambahan

    public Movie(String title, int imageResId, String videoUrl) {
        this.title = title;
        this.imageResId = imageResId;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
