package com.example.music_app.models;

import com.google.gson.annotations.SerializedName;

public class SongLikedId {
    @SerializedName("idUser")
    private Long idUser;
    @SerializedName("idSong")
    private Long idSong;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdSong() {
        return idSong;
    }

    public void setIdSong(Long idSong) {
        this.idSong = idSong;
    }
}
