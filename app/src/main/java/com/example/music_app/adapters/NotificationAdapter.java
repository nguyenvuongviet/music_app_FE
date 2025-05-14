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
import com.example.music_app.models.NotificationFirebase;
import com.example.music_app.models.Song;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private final Context context;
    private List<NotificationFirebase> notiList;

    private final OnItemClickListener onItemClickListener;

    public NotificationAdapter(Context context, List<NotificationFirebase> notiList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.notiList = notiList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<NotificationFirebase> notiList) {
        this.notiList = notiList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        NotificationFirebase noti = notiList.get(position);
        holder.notificaiton.setText(noti.getUserName() + " vừa phát hành ca khúc " + noti.getSongName());

        Glide.with(context)
                .load(noti.getCover())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return   notiList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public ImageView image;
        public TextView notificaiton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_notification);
            notificaiton = itemView.findViewById(R.id.tv_notification);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationFirebase noti = notiList.get(getAdapterPosition());
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onSongClick(position);
                    }
                }
            });


        }
    }

    public interface OnItemClickListener {
        void onSongClick(int position);
        void onPlayPlaylistClick(List<Song> songList);
    }
}
