package com.example.vibechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vibechat.model.GroupModel;
import com.example.vibechat.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class GroupRecyclerAdapter extends FirestoreRecyclerAdapter<GroupModel, GroupRecyclerAdapter.GroupViewHolder> {

    private Context mContext;
    private OnGroupItemClickListener mListener;

    public interface OnGroupItemClickListener {
        void onGroupItemClick(GroupModel group);
    }

    public GroupRecyclerAdapter(@NonNull FirestoreRecyclerOptions<GroupModel> options, Context context, OnGroupItemClickListener listener) {
        super(options);
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupViewHolder holder, int position, @NonNull GroupModel model) {
        holder.bind(model);
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView groupNameTextView;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupNameTextView = itemView.findViewById(R.id.group_name_text_view);
            itemView.setOnClickListener(this);
        }

        public void bind(GroupModel group) {
            groupNameTextView.setText(group.getGroupName());
        }

        @Override
        public void onClick(View view) {
            int position = getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION && mListener != null) {
                mListener.onGroupItemClick(getItem(position));
            }
        }
    }
}
