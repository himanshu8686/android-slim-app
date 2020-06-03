package com.example.userwithslim.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.userwithslim.R;
import com.example.userwithslim.storage.SharedPreferenceManager;


public class HomeFragment extends Fragment {

    private TextView tv_email,tv_name,tv_school;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_email= view.findViewById(R.id.tv_email);
        tv_name=view.findViewById(R.id.tv_name);
        tv_school=view.findViewById(R.id.tv_school);

        tv_email.setText(SharedPreferenceManager.getInstance(getActivity()).getUser().getEmail());
        tv_name.setText(SharedPreferenceManager.getInstance(getActivity()).getUser().getName());
        tv_school.setText(SharedPreferenceManager.getInstance(getActivity()).getUser().getSchool());
    }
}
