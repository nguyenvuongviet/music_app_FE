package com.example.music_app.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

public class PlaylistSong {
    @SerializedName("playlistSongId")
    private PlaylistSongId playlistSongId;
    @SerializedName("dayAdded")
    private List<Integer> dayAdded;

    public PlaylistSongId getPlaylistSongId() {
        return playlistSongId;
    }

    public void setPlaylistSongId(PlaylistSongId playlistSongId) {
        this.playlistSongId = playlistSongId;
    }

    public LocalDateTime getDayAdded() {
        LocalDateTime dateTime = LocalDateTime.of(
                dayAdded.get(0),
                dayAdded.get(1),
                dayAdded.get(2),
                dayAdded.get(3),
                dayAdded.get(4),
                dayAdded.get(5));
        return dateTime;
    }

    public void setDayAdded(List<Integer> dayAdded) {
        this.dayAdded = dayAdded;
    }
}
