package com.example.music_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.music_app.R;
import com.example.music_app.activities.SongDetailActivity;
import com.example.music_app.adapters.ArtistSearchAdapter;
import com.example.music_app.adapters.SongAdapter;
import com.example.music_app.decorations.BottomOffsetDecoration;
import com.example.music_app.helpers.SongToMediaItemHelper;
import com.example.music_app.models.Artist;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.Song;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.services.ExoPlayerQueue;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchedSongsFragment extends Fragment {
    private static final String ARG_QUERY = "query";
    private String query;

    private APIService apiService;
    private List<Song> songs;

    private RecyclerView rv_song;
    private SongAdapter songAdapter;

    private ExoPlayerQueue exoPlayerQueue;

    public SearchedSongsFragment() {
    }
    public static SearchedSongsFragment newInstance(String query) {
        SearchedSongsFragment fragment = new SearchedSongsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString(ARG_QUERY);
            Log.e("SearchedSongsFragment",query);
            searchSong(query);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searched_songs, container, false);
        initView(view);
        exoPlayerQueue = ExoPlayerQueue.getInstance();
        searchSong(query);
        return view;
    }

    private void initView(View view) {
        rv_song = view.findViewById(R.id.recycler_view_songs_song);
    }
    private void searchSong(String query) {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.searchSong(query).enqueue(new Callback<GenericResponse<List<Song>>>() {
            @Override
            public void onResponse(@NonNull Call<GenericResponse<List<Song>>> call, @NonNull Response<GenericResponse<List<Song>>> response) {
                if (response.isSuccessful() && response.body() != null && isAdded()) {
                    songs = response.body().getData();
                    Log.e("Thanh1234", songs.toString());
                    if (songs.isEmpty()){
                    }
                    else {
                        songAdapter = new SongAdapter(requireContext(), songs, new SongAdapter.OnItemClickListener() {
                            @Override
                            public void onSongClick(int position) {
                                exoPlayerQueue.clear();
                                exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(songs));
                                exoPlayerQueue.setCurrentPosition(position);
                                Intent intent = new Intent(getContext(), SongDetailActivity.class);
                                startActivity(intent);

                            }

                            @Override
                            public void onPlayPlaylistClick(List<Song> songList) {
                            }
                        });
                        rv_song.setAdapter(songAdapter);
                        rv_song.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                        rv_song.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
                        rv_song.setAdapter(songAdapter);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericResponse<List<Song>>> call, @NonNull Throwable t) {
                // Handle failure
                Log.e("SearchedAllFragment", "Failed to fetch songs", t);
            }
        });
    }
}