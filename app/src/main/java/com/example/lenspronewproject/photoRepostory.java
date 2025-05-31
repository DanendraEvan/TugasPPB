package com.example.lenspronewproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class photoRepostory {

    private static final String PREFS_NAME = "photo_prefs";
    private static final String KEY_PHOTOS = "photo_uris";

    private static photoRepostory instance;
    private final SharedPreferences preferences;
    private final Gson gson = new Gson();
    private List<Uri> photoUris;

    private photoRepostory(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        photoUris = loadPhotos();
    }

    public static synchronized photoRepostory getInstance(Context context) {
        if (instance == null) {
            instance = new photoRepostory(context.getApplicationContext());
        }
        return instance;
    }

    public List<Uri> getPhotoUris() {
        return new ArrayList<>(photoUris);
    }

    public void addPhoto(Uri uri) {
        photoUris.add(uri);
        savePhotos();
    }

    private void savePhotos() {
        List<String> uriStrings = new ArrayList<>();
        for (Uri uri : photoUris) {
            uriStrings.add(uri.toString());
        }
        String json = gson.toJson(uriStrings);
        preferences.edit().putString(KEY_PHOTOS, json).apply();
    }

    private List<Uri> loadPhotos() {
        String json = preferences.getString(KEY_PHOTOS, null);
        if (json != null) {
            Type type = new TypeToken<List<String>>() {}.getType();
            List<String> uriStrings = gson.fromJson(json, type);
            List<Uri> uris = new ArrayList<>();
            for (String uriStr : uriStrings) {
                uris.add(Uri.parse(uriStr));
            }
            return uris;
        }
        return new ArrayList<>();
    }
}
