package com.example.music_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.models.Artist;
import com.example.music_app.models.Song;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder> {
    private final Context context;
    private final List<Artist> artistList;
    private final OnItemClickListener onItemClickListener;

    public ArtistAdapter(Context context, List<Artist> artistList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.artistList = artistList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList.clear();
        this.artistList.addAll(artistList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Artist artist = artistList.get(position);
        holder.tenNgheSi.setText(artist.getNickname());

        Glide.with(context)
                .load(artist.getAvatar())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return   artistList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public CircleImageView image;
        public TextView tenNgheSi;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_artist_home);
            tenNgheSi = itemView.findViewById(R.id.tv_artist_home);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Artist artist = artistList.get(getAdapterPosition());
                    onItemClickListener.onItemClick(artist);
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Artist artist);
    }
}
