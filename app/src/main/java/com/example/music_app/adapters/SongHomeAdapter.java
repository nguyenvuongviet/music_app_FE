package com.example.music_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app.models.Song;
import com.example.music_app.R;
import java.util.List;

public class SongHomeAdapter extends RecyclerView.Adapter<SongHomeAdapter.MyViewHolder> {
    private final Context context;
    private final List<Song> songList;
    private String tag = "";
    private final OnItemClickListener onItemClickListener;

    public SongHomeAdapter(Context context, List<Song> songList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.songList = songList;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_trend, parent, false);
        return new MyViewHolder(view);
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.tenBaiHat.setText(song.getName());
        holder.tenNgheSi.setText(song.getArtistName());
        holder.position = position;

        Glide.with(context)
                .load(song.getImage())
                .into(holder.image);
    }


    @Override
    public int getItemCount() {
       return   songList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public ImageView image;
        public TextView tenBaiHat;
        public TextView tenNgheSi;
        int position;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imv_user_playlist);
            tenBaiHat = itemView.findViewById(R.id.tv_user_playlist);
            tenNgheSi = itemView.findViewById(R.id.tv_song_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onSongClick(position);
                    }
                }
            });


        }
    }
    public void setSongList(List<Song> songList) {
        this.songList.clear();
        this.songList.addAll(songList);
        notifyDataSetChanged();
    }

    public interface OnAdapterClickListener {
        void onAdapterClick();
    }

    public interface OnItemClickListener {
        void onSongClick(int position);
        void onPlayPlaylistClick(List<Song> songList);
    }
}
