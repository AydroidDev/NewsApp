package com.vaankdeals.newsapp.ViewTypes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vaankdeals.newsapp.R;

import androidx.fragment.app.Fragment;

public class FragmentNewsVideo extends Fragment {

    public FragmentNewsVideo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_native, container, false);


        return view;
    }

}
