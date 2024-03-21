package com.example.vibechat.model;

import com.google.firebase.firestore.PropertyName;

/**
 * Represents a group in the application.
 */
public class GroupModel {
    @PropertyName("groupId")
    private String groupId;

    @PropertyName("groupName")
    private String groupName;

    // Empty constructor required for Firestore
    public GroupModel() {
        // Default constructor required for Firestore
    }

    public GroupModel(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
