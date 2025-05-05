package com.example.music_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.music_app.R;
import com.example.music_app.activities.ArtistActivity;
import com.example.music_app.activities.SongDetailActivity;
import com.example.music_app.adapters.ArtistAdapter;
import com.example.music_app.adapters.ArtistSearchAdapter;
import com.example.music_app.adapters.SongAdapter;
import com.example.music_app.adapters.SongAddToLibraryAdapter;
import com.example.music_app.adapters.SongHomeAdapter;
import com.example.music_app.decorations.BottomOffsetDecoration;
import com.example.music_app.helpers.SongToMediaItemHelper;
import com.example.music_app.models.Artist;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.Song;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.services.ExoPlayerQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchedAllFragment extends Fragment {

    private static final String ARG_QUERY = "query";
    private String query;

    private APIService apiService;
    private List<Song> songs;
    private List<Artist> artists;

    private RecyclerView rv_song, rv_artist;
    private SongAdapter songAdapter;
    private ArtistSearchAdapter artistSearchAdapter;
    private ExoPlayerQueue exoPlayerQueue;

    TextView tv_artist, tv_song;

    public SearchedAllFragment() {
        // Required empty public constructor
    }

    public static SearchedAllFragment newInstance(String query) {
        SearchedAllFragment fragment = new SearchedAllFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString(ARG_QUERY);
            Log.e("SearchedAllFragment", query);
            searchSong(query);
            searchArtist(query);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searched_all, container, false);
        initView(view);
        exoPlayerQueue = ExoPlayerQueue.getInstance();
        searchSong(query);
        searchArtist(query);
        return view;
    }

    private void initView(View view) {
        rv_artist = view.findViewById(R.id.recycler_view_artists_all);
        rv_song = view.findViewById(R.id.recycler_view_songs_all);
        tv_artist = view.findViewById(R.id.tv_nghesi_search_all);
        tv_song = view.findViewById(R.id.tv_baihat_search_all);
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
                        tv_song.setVisibility(View.GONE);
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

    private void searchArtist(String query) {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.searchArtist(query).enqueue(new Callback<GenericResponse<List<Artist>>>() {
            @Override
            public void onResponse(@NonNull Call<GenericResponse<List<Artist>>> call, @NonNull Response<GenericResponse<List<Artist>>> response) {
                if (response.isSuccessful() && response.body() != null && isAdded()) {
                    artists = response.body().getData();
                    Log.e("Thanh1234", artists.toString());

                    if (artists.isEmpty()) {
                        tv_artist.setVisibility(View.GONE);
                    } else {
                        // Initialize the adapter only if it's not already initialized
                        if (artistSearchAdapter == null) {
                            artistSearchAdapter = new ArtistSearchAdapter(getContext(), artists);
                            artistSearchAdapter.setOnItemClickListener(new ArtistSearchAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Artist artist) {
                                    Intent intent = new Intent(getContext(), ArtistActivity.class);
                                    intent.putExtra("artistId", artist.getIdUser());
                                    startActivity(intent);
                                }
                            });

                            rv_artist.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                            rv_artist.setLayoutManager(layoutManager);
                            rv_artist.setAdapter(artistSearchAdapter);
                        } else {
                            // Update the existing adapter with the new data
                            artistSearchAdapter.updateArtists(artists);
                        }

                        tv_artist.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericResponse<List<Artist>>> call, @NonNull Throwable t) {
                // Handle failure
                Log.e("SearchedAllFragment", "Failed to fetch artists", t);
            }
        });
    }

}