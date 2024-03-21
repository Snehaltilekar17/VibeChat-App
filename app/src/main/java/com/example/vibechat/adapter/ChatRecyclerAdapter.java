package com.example.vibechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vibechat.R;
import com.example.vibechat.model.ChatMessageModel;
import com.example.vibechat.utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ChatModelViewHolder> {

    private Context context;
    private List<ChatMessageModel> selectedMessages;
    private OnMessageLongClickListener longClickListener;
    private List<ChatMessageModel> messages;

    public ChatRecyclerAdapter(Context context, List<ChatMessageModel> messages) {
        this.context = context;
        this.messages = messages;
        this.selectedMessages = new ArrayList<>();
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);
        return new ChatModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position) {
        ChatMessageModel message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChatModelViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextview, rightChatTextview;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);

            itemView.setOnLongClickListener(this);
        }

        public void bind(ChatMessageModel message) {
            if (message.getSenderId().equals(FirebaseUtil.currentUserId())) {
                leftChatLayout.setVisibility(View.GONE);
                rightChatLayout.setVisibility(View.VISIBLE);
                rightChatTextview.setText(message.getMessage());
            } else {
                rightChatLayout.setVisibility(View.GONE);
                leftChatLayout.setVisibility(View.VISIBLE);
                leftChatTextview.setText(message.getMessage());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ChatMessageModel message = messages.get(position);
                if (longClickListener != null) {
                    longClickListener.onMessageLongClick(message);
                    return true;
                }
            }
            return false;
        }
    }

    public interface OnMessageLongClickListener {
        void onMessageLongClick(ChatMessageModel message);
    }

    public void setOnMessageLongClickListener(OnMessageLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void removeMessage(ChatMessageModel message) {
        int position = messages.indexOf(message);
        if (position != -1) {
            messages.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeChat(int position) {
        if (position >= 0 && position < messages.size()) {
            messages.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addMessage(ChatMessageModel message) {
        messages.add(message);
    }
}
