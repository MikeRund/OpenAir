package com.mastersproject.openair.util;

import android.app.Application;

public class User extends Application {

    private String username;
    private String userId;
    private int waterActivities;
    private int walkActivities;
    private int exerciseActivities;
    private int hikeActivities;
    private int currentStreak;
    private int totalActivities;

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

    public int getWaterActivities() {
        return waterActivities;
    }

    public void setWaterActivities(int waterActivities) {
        this.waterActivities = waterActivities;
    }

    public int getWalkActivities() {
        return walkActivities;
    }

    public void setWalkActivities(int walkActivities) {
        this.walkActivities = walkActivities;
    }

    public int getExerciseActivities() {
        return exerciseActivities;
    }

    public void setExerciseActivities(int exerciseActivities) {
        this.exerciseActivities = exerciseActivities;
    }

    public int getHikeActivities() {
        return hikeActivities;
    }

    public void setHikeActivities(int hikeActivities) {
        this.hikeActivities = hikeActivities;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(int totalActivities) {
        this.totalActivities = totalActivities;
    }

    public void incrementWaterActivities(){
        this.waterActivities++;
    }

    public void incrementWalkActivities(){
        this.walkActivities++;
    }

    public void incrementHikeActivities(){
        this.hikeActivities++;
    }

    public void incrementExerciseActivities(){
        this.exerciseActivities++;
    }

    public void incrementTotalActivities(){
        this.totalActivities++;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
    }
}
