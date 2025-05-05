package com.example.music_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.music_app.R;
import com.example.music_app.activities.ArtistActivity;
import com.example.music_app.activities.HomeActivity;
import com.example.music_app.activities.HomeSongsActivity;
import com.example.music_app.activities.LibraryActivity;
import com.example.music_app.activities.SongDetailActivity;
import com.example.music_app.activities.TopicActivity;
import com.example.music_app.adapters.ArtistAdapter;
import com.example.music_app.adapters.NewSongHomeAdapter;
import com.example.music_app.adapters.SongHomeAdapter;
import com.example.music_app.decorations.BottomOffsetDecoration;
import com.example.music_app.helpers.SongToMediaItemHelper;
import com.example.music_app.internals.SharePrefManagerUser;
import com.example.music_app.listeners.PaginationScrollListener;
import com.example.music_app.models.Artist;
import com.example.music_app.models.ArtistResponse;
import com.example.music_app.models.GenericResponse;
import com.example.music_app.models.Song;
import com.example.music_app.models.SongResponse;
import com.example.music_app.models.User;
import com.example.music_app.retrofit.RetrofitClient;
import com.example.music_app.services.APIService;
import com.example.music_app.services.ExoPlayerQueue;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView rvTopTrend, rvFavoriteSong, rvTopArtist, rvNewSong;
    SongHomeAdapter songTrendAdapter, songFavoriteAdapter;
    ArtistAdapter artistAdapter;
    NewSongHomeAdapter songNewAdapter;
    MaterialButton btnLoadMore;
    APIService apiService;
    List<Song> trendSongs, favoriteSongs, newSongs;
    List<Artist> artists;
    TextView title, xtt_topthinhhanh, xtt_moinguoiyeuthich, xtt_nghesihangdau, xtt_moiramat;
    int page = 0, size = 5, totalPages = 0;
    boolean isLoading = false, isLastPage = false;
    private ExoPlayerQueue exoPlayerQueue = ExoPlayerQueue.getInstance();
    private final SongHomeAdapter.OnItemClickListener songTrendItemClick = new SongHomeAdapter.OnItemClickListener() {
        @Override
        public void onSongClick(int position) {
            exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(trendSongs));
            exoPlayerQueue.setCurrentPosition(position);
            Log.d("HomeActivity", "onSongClick: position " + exoPlayerQueue.getCurrentPosition() + "Recycler view tag: trend");
            Intent intent = new Intent(getContext(), SongDetailActivity.class);
            startActivity(intent);
        }

        @Override
        public void onPlayPlaylistClick(List<Song> songList) {

        }
    };

    private final SongHomeAdapter.OnItemClickListener songFavoriteItemClick = new SongHomeAdapter.OnItemClickListener() {
        @Override
        public void onSongClick(int position) {
            exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(favoriteSongs));
            exoPlayerQueue.setCurrentPosition(position);
            Log.d("HomeActivity", "onSongClick: position " + position + "Recycler view tag: favorite");
            Intent intent = new Intent(getContext(), SongDetailActivity.class);
            startActivity(intent);
        }

        @Override
        public void onPlayPlaylistClick(List<Song> songList) {

        }
    };

    private final NewSongHomeAdapter.OnItemClickListener songNewItemClick = new NewSongHomeAdapter.OnItemClickListener() {
        @Override
        public void onSongClick(int position, String tag) {
            exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(newSongs));
            exoPlayerQueue.setCurrentPosition(position);
            Log.d("HomeActivity", "onSongClick: position " + position + "Recycler view tag: newReleased");
            Intent intent = new Intent(getContext(), SongDetailActivity.class);
            startActivity(intent);
        }

        @Override
        public void onPlayPlaylistClick(List<Song> songList) {

        }
    };

    private final ArtistAdapter.OnItemClickListener artistItemClick = new ArtistAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Artist artist) {
            Intent intent = new Intent(getContext(), ArtistActivity.class);
            intent.putExtra("artistId", artist.getIdUser());
            startActivity(intent);
        }
    };
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("DataRes", "Code chay vao ham onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Ánh xạ các view
        initView(view);
        Log.e("DataRes", "Code chay vao ham onCreateView");

        User user = SharePrefManagerUser.getInstance(requireContext()).getUser();
        title.setText(getContext().getString(R.string.label_hello_user, user.getLastName()));


        songTrendAdapter = new SongHomeAdapter(getContext(), new ArrayList<>(), songTrendItemClick);
        songFavoriteAdapter = new SongHomeAdapter(getContext(), new ArrayList<>(), songFavoriteItemClick);
        newSongs = new ArrayList<>();
        songNewAdapter = new NewSongHomeAdapter(getContext(), newSongs, songNewItemClick);
        artistAdapter = new ArtistAdapter(getContext(), new ArrayList<>(), artistItemClick);
        btnLoadMore = view.findViewById(R.id.btn_viewmore_newsongs);
        GetTopTrend();
        GetFavoriteSong();
        GetTopArtist();
        GetNewSong();


        xtt_topthinhhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), TopicActivity.class);
                intent.putExtra("topic", "trending");
                startActivity(intent);
            }
        });
        xtt_moinguoiyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), TopicActivity.class);
                intent.putExtra("topic", "favorite");
                startActivity(intent);
            }
        });
        xtt_nghesihangdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), TopicActivity.class);
                intent.putExtra("topic", "topArtist");
                startActivity(intent);
            }
        });

        xtt_moiramat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), TopicActivity.class);
                intent.putExtra("topic", "newReleased");
                startActivity(intent);
            }
        });

        return view;
    }

    private void initView(View view) {
        rvTopTrend = view.findViewById(R.id.rv_toptrend);
        rvFavoriteSong = view.findViewById(R.id.rv_topfavorite);
        rvTopArtist = view.findViewById(R.id.rv_topartists);
        rvNewSong = view.findViewById(R.id.rv_newsongs);
        title = view.findViewById(R.id.title_appbar_home);
        xtt_topthinhhanh = view.findViewById(R.id.xemtatca_topthinhhanh);
        xtt_moinguoiyeuthich = view.findViewById(R.id.xemtatca_moinguoiyeuthich);
        xtt_nghesihangdau = view.findViewById(R.id.xemtatca_nghesihangdau);
        xtt_moiramat = view.findViewById(R.id.xemtatca_moiramat);
    }
    private void GetTopTrend(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getMostViewSong(0, 5).enqueue(new Callback<GenericResponse<SongResponse>>() {
            @Override
            public void onResponse(Call<GenericResponse<SongResponse>> call, Response<GenericResponse<SongResponse>> response) {
                if (response.isSuccessful()) {
                    trendSongs = new ArrayList<>();
                    trendSongs = response.body().getData().getContent();
                    songTrendAdapter.setSongList(trendSongs);
                    rvTopTrend.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false );
                    rvTopTrend.setLayoutManager(layoutManager);
                    rvTopTrend.setAdapter(songTrendAdapter);
                } else {
                    Log.e("DataRes", "No Res");
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<SongResponse>> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }

        });
    }
    private void GetFavoriteSong(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getMostLikeSong(0, 5).enqueue(new Callback<GenericResponse<SongResponse>>() {
            @Override
            public void onResponse(Call<GenericResponse<SongResponse>> call, Response<GenericResponse<SongResponse>> response) {
                if (response.isSuccessful()) {
                    favoriteSongs = new ArrayList<>();
                    favoriteSongs = response.body().getData().getContent();
                    songFavoriteAdapter.setSongList(favoriteSongs);
                    rvFavoriteSong.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false );
                    rvFavoriteSong.setLayoutManager(layoutManager);
                    rvFavoriteSong.setAdapter(songFavoriteAdapter);
                } else {
                    Log.e("DataRes", "No Res");
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<SongResponse>> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }

        });
    }
    private void GetTopArtist(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllArtists(0, 5).enqueue(new Callback<GenericResponse<ArtistResponse>>() {
            @Override
            public void onResponse(Call<GenericResponse<ArtistResponse>> call, Response<GenericResponse<ArtistResponse>> response) {
                if (response.isSuccessful()) {
                    artists = new ArrayList<>();
                    artists = response.body().getData().getContent();
                    Log.d("HomeFragment", "onResponse: Artist get succesfully " + artists.size());
                    artistAdapter.setArtistList(artists);
                    rvTopArtist.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false );
                    rvTopArtist.setLayoutManager(layoutManager);
                    rvTopArtist.setAdapter(artistAdapter);
                }
            }
            @Override
            public void onFailure(Call<GenericResponse<ArtistResponse>> call, Throwable t) {
                Log.d("ErrorReponse", t.getMessage());
            }
        });
    }
    private void GetNewSong(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<GenericResponse<SongResponse>> call = apiService.getSongNewReleased(page, size);
        fetchSongs(call);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvNewSong.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset));
        rvNewSong.addItemDecoration(itemDecoration);

        rvNewSong.setHasFixedSize(true);

        rvNewSong.setAdapter(songNewAdapter);

        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    loadNextPage();
                }
                isLoading = false;
                if (isLastPage) {
                    btnLoadMore.setVisibility(View.GONE);
                }
            }
        });
    }
    private void fetchSongs(Call<GenericResponse<SongResponse>> call) {
        call.enqueue(new Callback<GenericResponse<SongResponse>>() {
            @Override
            public void onResponse(Call<GenericResponse<SongResponse>> call, Response<GenericResponse<SongResponse>> response) {
                if (response.isSuccessful()) {
                    List<Song> newList =  response.body().getData().getContent();
                    totalPages = response.body().getData().getTotalPages();
                    newSongs.addAll(newList);
                    page++;
                    songNewAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<GenericResponse<SongResponse>> call, Throwable t) {
            }
        });
    }

    private void loadNextPage() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page < totalPages) {
                    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
                    Call<GenericResponse<SongResponse>> call = apiService.getSongNewReleased(page, 10);
                    fetchSongs(call);
                }
                isLoading = false;
                if (page == totalPages) {
                    isLastPage = true;
                }
                if (isLastPage) {
                    btnLoadMore.setVisibility(View.GONE);
                }
            }
        }, 500);
    }
}