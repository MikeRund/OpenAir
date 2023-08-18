package com.mastersproject.openair.util;

import android.app.Application;

public class User extends Application {

    private String username;
    private String userId;

    private static User instance;

    // Following Singleton Design Pattern

    public static User getInstance(){
        if (instance == null){
            instance = new User();
        }
        return instance;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
