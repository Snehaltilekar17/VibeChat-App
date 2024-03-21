package com.example.vibechat;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vibechat.adapter.ChatRecyclerAdapter;
import com.example.vibechat.model.ChatMessageModel;
import com.example.vibechat.model.ChatroomModel;
import com.example.vibechat.model.UserModel;
import com.example.vibechat.utils.AndroidUtil;
import com.example.vibechat.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.bumptech.glide.Glide;


public class ChatActivity extends AppCompatActivity implements ChatRecyclerAdapter.OnMessageLongClickListener {

    UserModel otherUser;
    String chatroomId;
    ChatroomModel chatroomModel;
    ChatRecyclerAdapter adapter;
    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    TextView otherUsername;
    RecyclerView recyclerView;
    ImageView imageView;
    ImageButton call_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get UserModel
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(), otherUser.getUserId());

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);
        imageView = findViewById(R.id.profile_pic_image_view);
        call_btn = findViewById(R.id.call_btn);

        FirebaseUtil.getOtherProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(this, uri, imageView);
                    }
                });



        //-- back button click
        backBtn.setOnClickListener((v)->{
            onBackPressed();
        });
        otherUsername.setText(otherUser.getUsername());


        /*
        backBtn.setOnClickListener((v) -> onBackPressed());
        otherUsername.setText(otherUser.getUsername());

         */

        sendMessageBtn.setOnClickListener((v -> {
            String message = messageInput.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessageToUser(message);
        }));
//--to see profile picture


        imageView.setOnClickListener(v -> {
            // Get the profile picture URI
            FirebaseUtil.getOtherProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri fullSizeUri = task.getResult();
                            showFullSizeProfilePictureDialog(fullSizeUri);
                        }
                    });
        });
        getOrCreateChatroomModel();
        //--To allow user make call
        call_btn.setOnClickListener(v -> {
            // Call initiation logic
            initiateCall();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupChatRecyclerView();
        // Set profile picture for the other user
        FirebaseUtil.getOtherProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilePic(this, uri, imageView);
                    }
                });


    }



    @Override
    public void onMessageLongClick(ChatMessageModel message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteMessage(message);
                    adapter.removeMessage(message);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Inside your setupChatRecyclerView() method or wherever you retrieve messages from Firestore
    void setupChatRecyclerView() {
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<ChatMessageModel> messages = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ChatMessageModel message = document.toObject(ChatMessageModel.class);
                    message.setMessageId(document.getId()); // Set the message ID
                    messages.add(message);
                }
                adapter = new ChatRecyclerAdapter(getApplicationContext(), messages);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setReverseLayout(true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                adapter.setOnMessageLongClickListener(this);
            }
        });
    }


    void sendMessageToUser(String message) {
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtil.currentUserId(), Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            messageInput.setText("");
                            sendNotification(message);
                            //   sendNotification(message);
                        }
                    }
                });
    }

    void getOrCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatroomModel = task.getResult().toObject(ChatroomModel.class);
                if (chatroomModel == null) {
                    chatroomModel = new ChatroomModel(
                            chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(), otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
                }
            }
        });
    }

    void sendNotification(String message) {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserModel currentUser = task.getResult().toObject(UserModel.class);
                try {
                    JSONObject jsonObject = new JSONObject();

                    JSONObject notificationObj = new JSONObject();
                    notificationObj.put("title", currentUser.getUsername());
                    notificationObj.put("body", message);

                    JSONObject dataObj = new JSONObject();
                    dataObj.put("userId", currentUser.getUserId());

                    jsonObject.put("notification", notificationObj);
                    jsonObject.put("data", dataObj);
                    jsonObject.put("to", otherUser.getFcmToken());

                    callApi(jsonObject);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void callApi(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer YOUR_API_KEY")
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Handle response
            }
        });
    }

    void deleteMessage(ChatMessageModel message) {
        String messageId = message.getMessageId();
        if (messageId != null) {
            Log.d("DeleteMessage", "Deleting message with ID: " + messageId); // Log the messageId
            FirebaseUtil.getChatroomMessageReference(chatroomId).document(messageId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Update the chatroom document with the latest message information
                        FirebaseUtil.getChatroomReference(chatroomId).update(
                                "lastMessage", "Message deleted", // Update last message to indicate deletion
                                "lastMessageTimestamp", Timestamp.now() // Update timestamp to current time
                        ).addOnSuccessListener(aVoid1 -> {
                            Log.d("DeleteMessage", "Chatroom document updated successfully");
                        }).addOnFailureListener(e -> {
                            Log.e("DeleteMessage", "Error updating chatroom document: " + e.getMessage());
                        });

                        // Remove the deleted message from the adapter's dataset
                        adapter.removeMessage(message);
                        // Notify the adapter of the dataset change
                        adapter.notifyDataSetChanged();
                        Log.d("DeleteMessage", "Message deleted successfully");
                    })
                    .addOnFailureListener(e -> {
                        // Error occurred while deleting the message
                        Log.e("DeleteMessage", "Error deleting message: " + e.getMessage());
                    });
        } else {
            Log.e("DeleteMessage", "Message ID is null");
        }
    }

    private void showFullSizeProfilePictureDialog(Uri fullSizeUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_full_size_image, null);

        // Find the ImageView in the dialog layout
        ImageView fullSizeImageView = dialogView.findViewById(R.id.full_size_image_view);

        // Load the full-size profile picture into the ImageView using Glide or any other image loading library
        Glide.with(this).load(fullSizeUri).into(fullSizeImageView);

        builder.setView(dialogView);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //--Logic for allowing user make call
    private void initiateCall() {
        // Send call request using FCM
        sendCallRequest();
    }

    // Method to send a call request via FCM
    private void sendCallRequest() {
        // Construct call request message
        JSONObject callRequest = new JSONObject();
        try {
            callRequest.put("type", "call_request");
            callRequest.put("from", FirebaseUtil.currentUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send call request via FCM to the recipient
        FirebaseUtil.sendFcmMessage(otherUser.getFcmToken(), callRequest.toString());
    }



}
