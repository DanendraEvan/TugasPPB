package com.astral.animasea;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.astral.animasea.fragments.AccountFragment;
import com.astral.animasea.fragments.BookmarkFragment;
import com.astral.animasea.fragments.HomeFragment;
import com.astral.animasea.fragments.WatchlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new HomeFragment())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.watchlist) {
                selectedFragment = new WatchlistFragment();
            } else if (itemId == R.id.bookmark) {
                selectedFragment = new BookmarkFragment();
            } else if (itemId == R.id.account) {
                selectedFragment = new AccountFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}
