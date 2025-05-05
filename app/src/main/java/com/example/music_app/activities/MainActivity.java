package com.example.music_app.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.music_app.R;
import com.example.music_app.fragments.HomeFragment;
import com.example.music_app.fragments.LibraryFragment;
import com.example.music_app.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends BaseActivity {

    FragmentManager fragmentManager;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMiniPlayer();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame_layout, HomeFragment.newInstance())
                .commit();
        navigationView = (BottomNavigationView) findViewById(R.id.main_navigation);

        int fragmentId = getIntent().getIntExtra("fragmentId", -1);
        if (fragmentId != -1) {
            navigationView.setSelectedItemId(R.id.menu_item_library);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, LibraryFragment.newInstance())
                    .commit();
        }

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_home:
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame_layout, HomeFragment.newInstance())
                                .commit();
                        return true;
                    case R.id.menu_item_search:
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame_layout, SearchFragment.newInstance())
                                .commit();
                        return true;
                    case R.id.menu_item_library:
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_frame_layout, LibraryFragment.newInstance())
                                .commit();
                        return true;
                }
                return false;
            }
        });
    }
}