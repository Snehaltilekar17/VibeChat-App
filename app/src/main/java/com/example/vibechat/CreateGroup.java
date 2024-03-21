package com.example.vibechat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGroup extends AppCompatActivity {
    private EditText groupNameEditText, friendUsernameEditText;
    private Button createGroupButton, searchButton;

    private CollectionReference groupsRef;
    private String groupId; // Variable to store the group ID
    private List<String> selectedUserIds; // List to store the selected user IDs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupNameEditText = findViewById(R.id.groupNameEditText);
        friendUsernameEditText = findViewById(R.id.friendUsernameEditText);
        createGroupButton = findViewById(R.id.createGroupButton);
        searchButton = findViewById(R.id.searchButton);

        groupsRef = FirebaseFirestore.getInstance().collection("groups");
        selectedUserIds = new ArrayList<>();

        // Set onClickListener for the createGroupButton
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });

        // Set onClickListener for the searchButton
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFriend();
            }
        });
    }

    // Method to create a group
    private void createGroup() {
        String groupName = groupNameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(groupName)) {
            Toast.makeText(this, "Please enter group name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current user ID and add it as an admin
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        selectedUserIds.add(userId); // Add current user ID as an admin

        // Create a group object with the group name and current user ID as admin
        Group group = new Group(groupName, userId);

        // Add selected friends to the group
        for (String friendUserId : selectedUserIds) {
            group.addMember(friendUserId, friendUserId.equals(userId)); // Set admin status for current user
        }

        // Add the group to the Firestore database
        groupsRef.add(group)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(CreateGroup.this, "Group created successfully", Toast.LENGTH_SHORT).show();
                    groupId = documentReference.getId(); // Store the group ID
                    Log.d("CreateGroup", "Group created successfully. Group ID: " + groupId);
                    finish(); // Finish the activity and return to the previous screen
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateGroup.this, "Failed to create group: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("CreateGroup", "Failed to create group: " + e.getMessage());
                });
    }
    // Method to search for a friend
    private void searchFriend() {
        String friendUsername = friendUsernameEditText.getText().toString().trim();

        // Perform a query in Firebase Firestore to find the user with the provided username
        FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("username", friendUsername)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Handle the retrieved user document (e.g., get user ID)
                        String friendUserId = documentSnapshot.getId();
                        // Add the friend's user ID to the selectedUserIds list
                        selectedUserIds.add(friendUserId);
                        // Notify the user that the friend is selected (e.g., display a message)
                        Toast.makeText(CreateGroup.this, "Friend added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors while searching for the user
                    Toast.makeText(CreateGroup.this, "Failed to search for user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
