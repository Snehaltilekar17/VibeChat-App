package com.example.vibechat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Meetsfragement extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_meetfragement, container, false);
      //  recyclerView = view.findViewById(R.id.recyler_view);
      //  setupRecyclerView();

        return view;
    }



}