package com.example.vibechat;

import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vibechat.utils.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity extends AppCompatActivity {
    protected DocumentReference documentReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());

     //   PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext()));
        FirebaseFirestore database = FirebaseFirestore.getInstance();
  //      documentReference=database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
    }
}
