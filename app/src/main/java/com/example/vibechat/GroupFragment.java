package com.example.vibechat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//--import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vibechat.adapter.GroupRecyclerAdapter;
import com.example.vibechat.model.GroupModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class GroupFragment extends Fragment implements GroupRecyclerAdapter.OnGroupItemClickListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groupsRef = db.collection("groups");
    private GroupRecyclerAdapter adapter;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            Query query = groupsRef.whereArrayContains("members", currentUserId);

            FirestoreRecyclerOptions<GroupModel> options = new FirestoreRecyclerOptions.Builder<GroupModel>()
                    .setQuery(query, GroupModel.class)
                    .build();

            adapter = new GroupRecyclerAdapter(options, getContext(), this);
        }
    }

    // GroupFragment class definition
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_group_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_group);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onGroupItemClick(GroupModel group) {
        // Handle group item click here, e.g., navigate to group chat activity


    }
}
