package com.example.music_app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistSongId {
    @SerializedName("idPlaylist")
    private Long idPlaylist;
    @SerializedName("idSong")
    private Long idSong;

    public Long getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Long idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public Long getIdSong() {
        return idSong;
    }

    public void setIdSong(Long idSong) {
        this.idSong = idSong;
    }
}
