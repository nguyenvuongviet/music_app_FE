package com.example.music_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_app.R;
import com.example.music_app.adapters.HistorySearchAdapter;
import com.example.music_app.adapters.SearchedViewPage2Adapter;
import com.google.android.material.tabs.TabLayout;

public class SearchedFragment extends Fragment {

    private String query;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    public SearchedFragment() {

    }

    public static SearchedFragment newInstance(String query) {
        SearchedFragment fragment = new SearchedFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString("query");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_searched, container, false);
        initView(view);
        FragmentManager fragmentManager = getChildFragmentManager();
        SearchedViewPage2Adapter adapter = new SearchedViewPage2Adapter(fragmentManager, getLifecycle());
        adapter.setQuery(query);
        viewPager2.setAdapter(adapter);
        handleTabLayout();
        return view;
    }
    private void initView(View view){
        tabLayout = (TabLayout) view.findViewById(R.id.search_tab_layout);
        viewPager2 = (ViewPager2) view.findViewById(R.id.search_view_pager);
    }
    private void handleTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        }) ;

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}