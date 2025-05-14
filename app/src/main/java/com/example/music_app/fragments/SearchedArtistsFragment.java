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
import com.example.music_app.activities.ArtistActivity;
import com.example.music_app.adapters.ArtistSearchAdapter;
import com.example.music_app.adapters.SongAdapter;
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


public class SearchedArtistsFragment extends Fragment {
    private static final String ARG_QUERY = "query";
    private String query;

    private APIService apiService;
    private List<Artist> artists;

    private RecyclerView  rv_artist;
    private ArtistSearchAdapter artistSearchAdapter;

    public static SearchedArtistsFragment newInstance(String query) {
        SearchedArtistsFragment fragment = new SearchedArtistsFragment();
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
            Log.e("SearchedArtistsFragment",query);
            searchArtist(query);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_artists, container, false);
        initView(view);
        searchArtist(query);
        return view;
    }
    private void initView(View view) {
        rv_artist = view.findViewById(R.id.recycler_view_artists_artist);
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