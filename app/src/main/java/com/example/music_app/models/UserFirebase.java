package com.example.music_app.models;

public class UserFirebase {
    private int userId;

    public int getId() {
        return userId;
    }

    public void setId(int userId) {
        this.userId = userId;
    }

    public UserFirebase() {
    }

    public UserFirebase(int userId) {
        this.userId = userId;
    }
}
