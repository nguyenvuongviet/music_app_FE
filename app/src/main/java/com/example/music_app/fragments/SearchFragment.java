package com.example.music_app.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.music_app.R;
import com.example.music_app.internals.SharePrefSearchHistory;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.models.User;

public class SearchFragment extends Fragment {

    SearchView searchView;
    FragmentManager fragmentManager;
    private SharePrefSearchHistory sharePrefSearchHistory;
    TextView title;
    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        sharePrefSearchHistory = new SharePrefSearchHistory(requireContext());
        User user = SharePrefManagerUser.getInstance(requireContext()).getUser();
        title.setText(getContext().getString(R.string.label_hello_user, user.getLastName()));
        searchView.clearFocus();
        fragmentManager.beginTransaction()
                .replace(R.id.search_frame_layout, SearchInitFragment.newInstance())
                .commit();

//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.search_frame_layout, SearchingFragment.newInstance())
//                        .commit();
//            }
//        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("onCreateView", "Code chạy vào đây 3");
                SearchedFragment searchedFragment = SearchedFragment.newInstance(query);
                fragmentManager.beginTransaction()
                        .replace(R.id.search_frame_layout, searchedFragment)
                        .commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                fragmentManager.beginTransaction()
                        .replace(R.id.search_frame_layout, SearchInitFragment.newInstance())
                        .commit();
                return false;
            }
        });
        return view;
    }
    private void initView(View view) {
        searchView = (SearchView) view.findViewById(R.id.searchView);
        fragmentManager = getChildFragmentManager();
        title = view.findViewById(R.id.title_appbar_home);
    }
}