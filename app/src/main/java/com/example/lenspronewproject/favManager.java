package com.example.lenspronewproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class favManager {
    private static final String PREFS_NAME = "favorites_prefs";
    private static final String KEY_FAVORITES = "favorite_uris";
    private static SharedPreferences preferences;
    private static Set<Uri> favoriteUris = new HashSet<>();

    public static void init(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadFavorites();
    }

    public static void addFavorite(Uri uri) {
        favoriteUris.add(uri);
        saveFavorites();
    }

    public static void removeFavorite(Uri uri) {
        favoriteUris.remove(uri);
        saveFavorites();
    }

    public static boolean isFavorite(Uri uri) {
        return favoriteUris.contains(uri);
    }

    public static Set<Uri> getFavorites() {
        return new HashSet<>(favoriteUris);
    }

    private static void saveFavorites() {
        List<String> uriStrings = new ArrayList<>();
        for (Uri uri : favoriteUris) {
            uriStrings.add(uri.toString());
        }
        preferences.edit().putString(KEY_FAVORITES, new Gson().toJson(uriStrings)).apply();
    }

    private static void loadFavorites() {
        String json = preferences.getString(KEY_FAVORITES, null);
        if (json != null) {
            Type type = new TypeToken<List<String>>() {}.getType();
            List<String> uriStrings = new Gson().fromJson(json, type);
            favoriteUris.clear();
            for (String uriStr : uriStrings) {
                favoriteUris.add(Uri.parse(uriStr));
            }
        }
    }
}