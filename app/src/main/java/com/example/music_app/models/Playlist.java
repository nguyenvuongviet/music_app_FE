package com.example.music_app.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

public class Playlist {
    @SerializedName("idPlaylist")
    private int idPlaylist;

    @SerializedName("idUser")
    private Long idUser;
    @SerializedName("name")
    private String name;
    @SerializedName("dayCreated")
    private List<Integer> dayCreated;
    @SerializedName("image")
    private String image;
    @SerializedName("songs")
    private List<Song> songs;

    public Playlist() {
    }

    public Playlist(int idPlaylist, Long idUser, String name, List<Integer> dayCreated, String image, List<Song> songs) {
        this.idPlaylist = idPlaylist;
        this.idUser = idUser;
        this.name = name;
        this.dayCreated = dayCreated;
        this.image = image;
        this.songs = songs;
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDayCreated() {
        LocalDateTime dateTime = LocalDateTime.of(
                dayCreated.get(0),
                dayCreated.get(1),
                dayCreated.get(2),
                dayCreated.get(3),
                dayCreated.get(4),
                dayCreated.get(5));
        return dateTime;
    }

    public void setDayCreated(List<Integer> dayCreated) {
        this.dayCreated = dayCreated;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public int getSongCount() {
        return songs == null ? 0 : songs.size();
    }
}
