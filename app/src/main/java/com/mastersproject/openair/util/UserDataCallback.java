package com.mastersproject.openair.util;

public interface UserDataCallback {
    void onUserDataFetched(String profileImageUrl,
                           long exerciseActivities,
                           long hikeActivities,
                           long walkActivities,
                           long waterActivities,
                           long totalActivities);
}

