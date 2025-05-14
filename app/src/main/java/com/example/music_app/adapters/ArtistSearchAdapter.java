package com.example.music_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app.R;
import com.example.music_app.models.Artist;
import com.example.music_app.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistSearchAdapter extends RecyclerView.Adapter<ArtistSearchAdapter.MyViewHolder> {
    private final Context context;
    private List<Artist> artists;
    private OnItemClickListener onItemClickListener;

    public ArtistSearchAdapter(Context context, List<Artist> artists) {
        this.context = context;
        this.artists = artists;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void updateArtists(List<Artist> newArtists) {
        this.artists = newArtists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArtistSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ArtistSearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistSearchAdapter.MyViewHolder holder, int position) {
        Artist artist = artists.get(position);
        holder.textView.setText(artist.getNickname());
        Glide.with(context)
                .load(artist.getAvatar())
                .into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public CircleImageView circleImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_notification);
            circleImageView = itemView.findViewById(R.id.img_notification);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Artist artist = artists.get(position);
                            onItemClickListener.onItemClick(artist);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Artist artist);
    }
}
