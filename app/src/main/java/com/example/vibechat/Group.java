package com.example.vibechat;

import java.util.HashMap;
import java.util.Map;

public class Group {

    private String groupName; // Group name
    private String userId; // Creator of the group
    private Map<String, Boolean> members; // Map to store members (userId and isAdmin)

    // Constructors, getters, and setters

    public Group() {
        // Default constructor required for Firestore
    }

    public Group(String groupName, String userId) {
        this.groupName = groupName;
        this.userId = userId;
        this.members = new HashMap<>();
    }

    // Getters and setters

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Boolean> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Boolean> members) {
        this.members = members;
    }

    // Add a member to the group
    public void addMember(String memberId, boolean isAdmin) {
        members.put(memberId, isAdmin);
    }

    // Remove a member from the group
    public void removeMember(String memberId) {
        members.remove(memberId);
    }
}
