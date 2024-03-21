package com.example.vibechat.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseUtil {
    private static final String GROUPS_COLLECTION = "groups";
    public static final String CALLS_COLLECTION = "calls"; // Define CALLS_COLLECTION here

    public static String currentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getUid() : null;
    }

    public static boolean isLoggedIn() {
        return currentUserId() != null;
    }

    public static DocumentReference currentUserDetails() {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatroomReference(String chatroomId) {
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId) {
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static String getChatroomId(String userId1, String userId2) {
        if (userId1.hashCode() < userId2.hashCode()) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }

    public static CollectionReference allChatroomCollectionReference() {
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds) {
        if (userIds.get(0).equals(currentUserId())) {
            return allUserCollectionReference().document(userIds.get(1));
        } else {
            return allUserCollectionReference().document(userIds.get(0));
        }
    }

    public static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getCurrentProfilePicStorageRef() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return FirebaseStorage.getInstance().getReference()
                    .child("profile_pic")
                    .child(currentUser.getUid());
        } else {
            // Handle the case where the current user is not logged in
            return null;
        }
    }

    public static StorageReference getOtherProfilePicStorageRef(String otherUserId) {
        return FirebaseStorage.getInstance().getReference()
                .child("profile_pic")
                .child(otherUserId);
    }

    public static void createGroup(String groupName, String createdByUserId) {
        // Get reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Generate a unique ID for the group
        String groupId = db.collection("groups").document().getId();

        // Create a map to represent the group data
        Map<String, Object> groupData = new HashMap<>();
        groupData.put("groupName", groupName);
        groupData.put("createdByUserId", createdByUserId);

        // Add the group data to the Firestore database
        db.collection("groups").document(groupId)
                .set(groupData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    // Group creation successful
                    // Handle success if needed
                })
                .addOnFailureListener(e -> {
                    // Group creation failed
                    // Handle failure if needed
                });
    }

    public static Query allGroupCollectionReference() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference groupsRef = firestore.collection(GROUPS_COLLECTION);
        return groupsRef;
    }

    public static void sendFcmMessage(String receiverToken, String message) {
        // Construct call to Firebase Cloud Messaging (FCM) endpoint
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject body = new JSONObject();
        try {
            body.put("to", receiverToken);
            JSONObject notification = new JSONObject();
            notification.put("title", "New Call");
            notification.put("body", message);
            body.put("notification", notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(RequestBody.create(mediaType, body.toString()))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=n")
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle success
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                // Get response body
                String responseBody = response.body().string();
                // Log response body
                System.out.println("FCM Response: " + responseBody);
            }
        });
    }
}
