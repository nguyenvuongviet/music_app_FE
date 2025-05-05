package com.example.music_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.music_app.R;
import com.example.music_app.adapters.ArtistAdapter;
import com.example.music_app.adapters.HistorySearchAdapter;
import com.example.music_app.internals.SharePrefSearchHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchingFragment extends Fragment {
    private SharePrefSearchHistory sharePrefSearchHistory;
    private HistorySearchAdapter historySearchAdapter;

    private RecyclerView rv_history;
    private TextView tv_deleteAll;

    public SearchingFragment() {

    }

    public static SearchingFragment newInstance() {
        SearchingFragment fragment = new SearchingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_searching, container, false);

        initView(view);

        sharePrefSearchHistory = new SharePrefSearchHistory(requireContext());
        Set<String> searchHistorySet = sharePrefSearchHistory.getSearchHistory();
        if (searchHistorySet.isEmpty()) {
            rv_history.setVisibility(View.GONE);
            tv_deleteAll.setVisibility(View.GONE);
        } else {
            List<String> searchHistoryList = new ArrayList<>(searchHistorySet);
            Log.e("searchHistorySet", searchHistoryList.toString());
            historySearchAdapter = new HistorySearchAdapter(requireContext(), searchHistoryList);

            rv_history.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            rv_history.setLayoutManager(layoutManager);
            rv_history.setAdapter(historySearchAdapter);
            tv_deleteAll.setVisibility(View.VISIBLE);
            tv_deleteAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharePrefSearchHistory.clearSearchHistory();
                    rv_history.setVisibility(View.GONE);
                    tv_deleteAll.setVisibility(View.GONE);
                }
            });
        }

        return view;
    }

    public void initView(View view){
        rv_history = view.findViewById(R.id.rv_lichsutimkiem);
        tv_deleteAll = view.findViewById(R.id.txt_xoalichsutimkiem);

    }
}