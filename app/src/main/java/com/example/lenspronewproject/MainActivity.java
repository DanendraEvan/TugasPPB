package com.example.lenspronewproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.lenspronewproject.fragment.HomeFragment;
import com.example.lenspronewproject.fragment.addFragment;
import com.example.lenspronewproject.fragment.akunFragment;
import com.example.lenspronewproject.fragment.favFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        favManager.init(getApplicationContext());
        // Ambil preferensi tema dari SharedPreferences
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi UI
        bottomNav = findViewById(R.id.bottomNav);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);

        // Sinkronisasi tombol drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Default fragment saat pertama kali dibuka
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Listener BottomNavigationView
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.add) {
                selectedFragment = new addFragment();
            } else if (itemId == R.id.fav) {
                selectedFragment = new favFragment();
            } else if (itemId == R.id.akun) {
                selectedFragment = new akunFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

        // Listener Navigation Drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    Toast.makeText(MainActivity.this, "Beranda dipilih", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.nav_setting) {
                    Toast.makeText(MainActivity.this, "Pengaturan dipilih", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.nav_theme) {
                    // Toggle Tema
                    boolean isDark = prefs.getBoolean("dark_mode", false);
                    SharedPreferences.Editor editor = prefs.edit();

                    if (isDark) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        editor.putBoolean("dark_mode", false);
                        Toast.makeText(MainActivity.this, "Mode Terang Aktif", Toast.LENGTH_SHORT).show();
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        editor.putBoolean("dark_mode", true);
                        Toast.makeText(MainActivity.this, "Mode Gelap Aktif", Toast.LENGTH_SHORT).show();
                    }

                    editor.apply();
                    recreate(); // restart activity untuk menerapkan tema
                }

                drawerLayout.closeDrawers(); // tutup drawer
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
