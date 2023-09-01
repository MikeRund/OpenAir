package com.mastersproject.openair.model;

import com.google.firebase.Timestamp;

public class Post {

    private String username;
    private String description;
    private String activityType;
    private String postImageURL;
    private String profileImageURL;
    private String userId;
    private Timestamp timestamp;


    public Post() {
    }

    public Post(String username, String description, String activityType, String postImageURL, String profileImageURL, String userId, Timestamp timestamp) {
        this.username = username;
        this.description = description;
        this.activityType = activityType;
        this.postImageURL = postImageURL;
        this.profileImageURL = profileImageURL;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getPostImageURL() {
        return postImageURL;
    }

    public void setPostImageURL(String postImageURL) {
        this.postImageURL = postImageURL;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}