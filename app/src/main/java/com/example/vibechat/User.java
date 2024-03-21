package com.example.vibechat;

public class User {
    private String userId;
    private String username;

    public User() {
        // Default constructor required for Firestore
    }

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getName() {
        return username; // Or any other logic to retrieve the user's name
    }
}

