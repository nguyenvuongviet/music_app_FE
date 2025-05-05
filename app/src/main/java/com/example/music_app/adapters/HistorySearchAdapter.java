package com.example.music_app.adapters;

import android.content.Context;
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
import com.example.music_app.R;
import com.example.music_app.models.Song;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HistorySearchAdapter extends RecyclerView.Adapter<HistorySearchAdapter.MyViewHolder> {
    private final Context context;
    private final List<String> historyList;

    public HistorySearchAdapter(Context context, List<String> historyList) {
        this.context = context;
        this.historyList = historyList;
    }
    @NonNull
    @Override
    public HistorySearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
        return new HistorySearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorySearchAdapter.MyViewHolder holder, int position) {
        String historyItem = historyList.get(position);

        if (holder.button_history != null) {
            holder.button_history.setText(historyItem);
        } else {
            Log.e("HistorySearchAdapter", "ViewHolder or TextView is null.");
        }
    }

    @Override
    public int getItemCount() {
        return   historyList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public MaterialButton button_history;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button_history = itemView.findViewById(R.id.button_history_search);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String history = historyList.get(getAdapterPosition());
                    Toast.makeText(context.getApplicationContext(), "Bạn đã click vào bài hát" + history, Toast.LENGTH_SHORT).show();

                }
            });


        }
    }
}
