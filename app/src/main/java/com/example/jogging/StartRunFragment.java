package com.example.jogging;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.markushi.ui.CircleButton;


public class StartRunFragment extends Fragment {
    private CircleButton startRun;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start_run, container, false);
        startRun = view.findViewById(R.id.gotoRun);
        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RunActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}