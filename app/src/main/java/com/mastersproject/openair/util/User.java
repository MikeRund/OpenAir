package com.mastersproject.openair.util;

import android.app.Application;

public class User extends Application {

    private String username;
    private String userId;
    private String imageURL;
    private String description;


    private long waterActivities;
    private long walkActivities;
    private long exerciseActivities;
    private long hikeActivities;
    private long currentStreak;
    private long totalActivities;

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

    public long getWaterActivities() {
        return waterActivities;
    }

    public void setWaterActivities(long waterActivities) {
        this.waterActivities = waterActivities;
    }

    public long getWalkActivities() {
        return walkActivities;
    }

    public void setWalkActivities(long walkActivities) {
        this.walkActivities = walkActivities;
    }

    public long getExerciseActivities() {
        return exerciseActivities;
    }

    public void setExerciseActivities(long exerciseActivities) {
        this.exerciseActivities = exerciseActivities;
    }

    public long getHikeActivities() {
        return hikeActivities;
    }

    public void setHikeActivities(long hikeActivities) {
        this.hikeActivities = hikeActivities;
    }

    public long getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(long currentStreak) {
        this.currentStreak = currentStreak;
    }

    public long getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(long totalActivities) {
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
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static synchronized void initializeInstance(String userId, String username) {
        instance = getInstance();
        instance.setUserId(userId);
        instance.setUsername(username);
    }
}
