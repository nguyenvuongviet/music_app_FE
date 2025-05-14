package com.example.music_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.music_app.R;
import com.example.music_app.adapters.ArtistAdapter;
import com.example.music_app.adapters.NewSongHomeAdapter;
import com.example.music_app.adapters.SongHomeAdapter;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    RecyclerView rvTopTrend, rvFavoriteSong, rvTopArtist, rvNewSong;
    SongHomeAdapter songTrendAdapter, songFavoriteAdapter;
    ArtistAdapter artistAdapter;
    NewSongHomeAdapter songNewAdapter;

    APIService apiService;
    List<Song> trendSongs, favoriteSongs, NewSongs;
    List<Artist> artists;

    TextView title, xtt_topthinhhanh, xtt_moinguoiyeuthich, xtt_nghesihangdau, xtt_moiramat;

    BottomNavigationView bottomNavigationView;
    List<Song> songTrendList, songFavoriteList, songNewList;
    int page = 0, totalPages;
    boolean isLastPage = false, isLoading = false;

    private final SongHomeAdapter.OnItemClickListener songTrendItemClick = new SongHomeAdapter.OnItemClickListener() {
        @Override
        public void onSongClick(int position) {
            exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(songTrendList));
            exoPlayerQueue.setCurrentPosition(position);
            Log.d("HomeActivity", "onSongClick: position " + exoPlayerQueue.getCurrentPosition() + "Recycler view tag: trend");
            Intent intent = new Intent(getApplicationContext(), SongDetailActivity.class);
            startActivity(intent);
        }

        @Override
        public void onPlayPlaylistClick(List<Song> songList) {

        }
    };

    private final SongHomeAdapter.OnItemClickListener songFavoriteItemClick = new SongHomeAdapter.OnItemClickListener() {
        @Override
        public void onSongClick(int position) {
            exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(songFavoriteList));
            exoPlayerQueue.setCurrentPosition(position);
            Log.d("HomeActivity", "onSongClick: position " + position + "Recycler view tag: favorite");
            Intent intent = new Intent(getApplicationContext(), SongDetailActivity.class);
            startActivity(intent);
        }

        @Override
        public void onPlayPlaylistClick(List<Song> songList) {

        }
    };

    private final NewSongHomeAdapter.OnItemClickListener songNewItemClick = new NewSongHomeAdapter.OnItemClickListener() {
        @Override
        public void onSongClick(int position, String tag) {
            exoPlayerQueue.setCurrentQueue(SongToMediaItemHelper.convertToMediaItem(songNewList));
            exoPlayerQueue.setCurrentPosition(position);
            Log.d("HomeActivity", "onSongClick: position " + position + "Recycler view tag: newReleased");
            Intent intent = new Intent(getApplicationContext(), SongDetailActivity.class);
            startActivity(intent);
        }

        @Override
        public void onPlayPlaylistClick(List<Song> songList) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initMiniPlayer();
        User user = SharePrefManagerUser.getInstance(this).getUser();
        AnhXa();

        songTrendAdapter = new SongHomeAdapter(getApplicationContext(), new ArrayList<>(), songTrendItemClick);
        songFavoriteAdapter = new SongHomeAdapter(getApplicationContext(), new ArrayList<>(), songFavoriteItemClick);
        songNewAdapter = new NewSongHomeAdapter(getApplicationContext(), new ArrayList<>(), songNewItemClick);

        title.setText("Chào " + user.getFirstName() + " " + user.getLastName() + " 👋");
        GetTopTrend();
        GetFavoriteSong();
        GetTopArtist();
        GetNewSong();



        xtt_topthinhhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TopicActivity.class);
                intent.putExtra("topic", "trending");
                startActivity(intent);
            }
        });
        xtt_moinguoiyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TopicActivity.class);
                intent.putExtra("topic", "favorite");
                startActivity(intent);
            }
        });
        xtt_nghesihangdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TopicActivity.class);
                intent.putExtra("topic", "topArtist");
                startActivity(intent);
            }
        });
        xtt_moiramat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TopicActivity.class);
                intent.putExtra("topic", "newReleased");
                startActivity(intent);
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_search:
                        break;
                    case R.id.menu_item_library:
                        Intent intent2 = new Intent(HomeActivity.this, LibraryActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
    }

    private void AnhXa() {
        rvTopTrend = findViewById(R.id.rv_toptrend);
        rvFavoriteSong = findViewById(R.id.rv_topfavorite);
        rvTopArtist = findViewById(R.id.rv_topartists);
        rvNewSong = findViewById(R.id.rv_newsongs);
        title = findViewById(R.id.title_appbar_home);
        xtt_topthinhhanh = findViewById(R.id.xemtatca_topthinhhanh);
        xtt_moinguoiyeuthich = findViewById(R.id.xemtatca_moinguoiyeuthich);
        xtt_nghesihangdau = findViewById(R.id.xemtatca_nghesihangdau);
        xtt_moiramat = findViewById(R.id.xemtatca_moiramat);
        bottomNavigationView = findViewById(R.id.navigation);
    }
    private void GetTopTrend(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getMostViewSong(0, 5).enqueue(new Callback<GenericResponse<SongResponse>>() {
            @Override
            public void onResponse(Call<GenericResponse<SongResponse>> call, Response<GenericResponse<SongResponse>> response) {
                if (response.isSuccessful()) {
                    songTrendList = new ArrayList<>();
                    songTrendList = response.body().getData().getContent();
                    songTrendAdapter.setSongList(songTrendList);
                    rvTopTrend.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false );
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
                    songFavoriteList = new ArrayList<>();
                    songFavoriteList = response.body().getData().getContent();
                    songFavoriteAdapter.setSongList(songFavoriteList);
                    rvFavoriteSong.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false );
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
        apiService.getAllArtists(0, 10).enqueue(new Callback<GenericResponse<ArtistResponse>>() {
            @Override
            public void onResponse(Call<GenericResponse<ArtistResponse>> call, Response<GenericResponse<ArtistResponse>> response) {
                if (response.isSuccessful()) {
                    artists = new ArrayList<>();
                    artists = response.body().getData().getContent();
                    artistAdapter = new ArtistAdapter(getApplicationContext(), artists, null);
                    rvTopArtist.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false );
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
        apiService.getSongNewReleased(0, 5).enqueue(new Callback<GenericResponse<SongResponse>>() {
            @Override
            public void onResponse(Call<GenericResponse<SongResponse>> call, Response<GenericResponse<SongResponse>> response) {
                if (response.isSuccessful()) {
                    songFavoriteList = new ArrayList<>();
                    songFavoriteList = response.body().getData().getContent();
                    songFavoriteAdapter.setSongList(songFavoriteList);
                    rvFavoriteSong.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false );
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


//        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
//        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
//                    if (!isLoading && !isLastPage) {
//                        loadNextPage();
//                    }
//                }
//            }
//        });
//        rvNewSong.addOnScrollListener(new PaginationScrollListener(layoutManager) {
//            @Override
//            public void loadMoreItems() {
//                isLoading = true;
//                loadNextPage();
//                Log.d("HomeActivity", "loadMoreItems: load page " + page + "/" + totalPages);
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void fetchSongs(Call<GenericResponse<SongResponse>> call) {
        call.enqueue(new Callback<GenericResponse<SongResponse>>() {
            @Override
            public void onResponse(Call<GenericResponse<SongResponse>> call, Response<GenericResponse<SongResponse>> response) {
                if (response.isSuccessful()) {
                    List<Song> newList =  response.body().getData().getContent();
                    totalPages = response.body().getData().getTotalPages();
                    songNewList.addAll(newList);
                    page++;
                    songNewAdapter.notifyDataSetChanged();
                    Log.d("HomeActivity", "Total pages: " + response.body().getData().getTotalPages());
                }
            }
            @Override
            public void onFailure(Call<GenericResponse<SongResponse>> call, Throwable t) {
                Log.d("HomeActivity", "onFailure: " + t.getMessage());
            }
        });
    }

}