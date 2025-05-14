package com.example.music_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.music_app.R;
import com.example.music_app.adapters.LibraryViewPager2Adapter;
import com.example.music_app.databinding.ActivityLibraryBinding;
import com.google.android.material.tabs.TabLayout;

public class LibraryActivity extends AppCompatActivity {

    private ActivityLibraryBinding binding;
    private LibraryViewPager2Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLibraryBinding.inflate(getLayoutInflater());
        binding.navigation.getRoot().setSelectedItemId(R.id.menu_item_library);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new LibraryViewPager2Adapter(fragmentManager, getLifecycle());
        binding.libraryViewPager.setAdapter(adapter);

        binding.libraryTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                binding.libraryViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        }) ;

        binding.libraryViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.libraryTabLayout.selectTab(binding.libraryTabLayout.getTabAt(position));
            }
        });

    }

}