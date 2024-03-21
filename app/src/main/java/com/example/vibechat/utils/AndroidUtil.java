package com.example.vibechat.utils;

import static com.example.vibechat.utils.FirebaseUtil.CALLS_COLLECTION;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.vibechat.model.UserModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AndroidUtil {

   public static  void showToast(Context context,String message){
       Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel model){
       intent.putExtra("username",model.getUsername());
       intent.putExtra("phone",model.getPhone());
       intent.putExtra("userId",model.getUserId());
        intent.putExtra("fcmToken",model.getFcmToken());

    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        userModel.setFcmToken(intent.getStringExtra("fcmToken"));
        return userModel;
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    // to allow user make calls
    public static void createCall(String fromUserId, String toUserId, long startTime) {
        // Get reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Generate a unique ID for the call
        String callId = db.collection(CALLS_COLLECTION).document().getId();

        // Create a map to represent the call data
        Map<String, Object> callData = new HashMap<>();
        callData.put("fromUserId", fromUserId);
        callData.put("toUserId", toUserId);
        callData.put("startTime", startTime);

        // Add the call data to the Firestore database
        db.collection(CALLS_COLLECTION).document(callId)
                .set(callData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    // Call creation successful
                    // Handle success if needed
                })
                .addOnFailureListener(e -> {
                    // Call creation failed
                    // Handle failure if needed
                });
    }

    // Method to retrieve calls for a user
    public static Query getCallsForUser(String userId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference callsRef = firestore.collection(CALLS_COLLECTION);
        return callsRef.whereEqualTo("toUserId", userId);
    }
}

