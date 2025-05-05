package com.example.music_app.models;

import android.health.connect.datatypes.units.Pressure;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class NotificationFirebase {
    private int songId;
    private String songName;
    private String userName;

    private String cover;

    public NotificationFirebase(int songId, String songName, String userName, String cover) {
        this.songId = songId;
        this.songName = songName;
        this.userName = userName;
        this.cover = cover;
    }

    public NotificationFirebase() {
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("songId", songId);
        result.put("songName", songName);
        result.put("userName", userName);
        result.put("cover",cover);

        return result;
    }

}
