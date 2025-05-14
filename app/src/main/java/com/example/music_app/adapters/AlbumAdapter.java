package com.example.music_app.adapters;

import static androidx.core.content.res.TypedArrayUtils.getString;
import static androidx.core.content.res.TypedArrayUtils.getText;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.models.Album;
import com.google.android.play.integrity.internal.al;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private Context context;
    private List<Album> albumList;
    private OnAlbumClickListener listener;

    public AlbumAdapter(Context context, List<Album> albumList, OnAlbumClickListener onAlbumClickListener) {
        this.context = context;
        this.albumList = albumList;
        this.listener = onAlbumClickListener;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList.clear();
        this.albumList.addAll(albumList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.tvAlbumName.setText(album.getName());
        holder.tvSongCount.setText(context.getString(R.string.label_songs, album.getSongs().size()));
        Glide.with(context).load(album.getImage()).into(holder.imgAlbum);
    }

    @Override
    public int getItemCount() {
        return albumList.isEmpty() ? 0 : albumList.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAlbum;
        TextView tvAlbumName, tvSongCount;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.img_album);
            tvAlbumName = itemView.findViewById(R.id.tv_album_name);
            tvSongCount = itemView.findViewById(R.id.tv_album_song_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAlbumClick(albumList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnAlbumClickListener {
        void onAlbumClick(Album album);
    }
}
