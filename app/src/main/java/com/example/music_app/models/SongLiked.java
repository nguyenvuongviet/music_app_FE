package com.example.music_app.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

public class SongLiked {
    @SerializedName("songLikedId")
    private SongLikedId songLikedId;
    @SerializedName("dayLiked")
    private List<Integer> dayLiked;

    public SongLikedId getSongLikedId() {
        return songLikedId;
    }

    public void setSongLikedId(SongLikedId songLikedId) {
        this.songLikedId = songLikedId;
    }

    public LocalDateTime getDayLiked() {
        LocalDateTime dateTime = LocalDateTime.of(
                dayLiked.get(0),
                dayLiked.get(1),
                dayLiked.get(2),
                dayLiked.get(3),
                dayLiked.get(4),
                dayLiked.get(5));
        return dateTime;
    }

    public void setDayLiked(List<Integer> dayLiked) {
        this.dayLiked = dayLiked;
    }
}
