package com.example.music_app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.music_app.fragments.SearchedAllFragment;
import com.example.music_app.fragments.SearchedArtistsFragment;
import com.example.music_app.fragments.SearchedSongsFragment;

public class SearchedViewPage2Adapter extends FragmentStateAdapter {
    private String query;
    public SearchedViewPage2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    public void setQuery(String query) {
        this.query = query;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return SearchedAllFragment.newInstance(query);
        } else if (position == 1) {
            return SearchedSongsFragment.newInstance(query);
        }else if (position == 2){
            return SearchedArtistsFragment.newInstance(query);
        }
        return  SearchedAllFragment.newInstance(query);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
