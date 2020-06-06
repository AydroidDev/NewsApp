package com.vaankdeals.newsapp.ViewTypes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import androidx.fragment.app.Fragment;

public class FragmentCustomAd extends Fragment {

    public FragmentCustomAd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.customad, container, false);

        ImageView imageView= view.findViewById(R.id.customadimage);
        NewsModel model =(NewsModel)getArguments().getSerializable("model");
        String imagUrl =model.getmNewsImage();
        String adUrl =model.getmNewslink();
        Button adButton=view.findViewById(R.id.adlinkout);
        Glide.with(requireActivity()).load(imagUrl).into(imageView);

        adButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(adUrl));
            startActivity(browserIntent);
        });

        return view;
    }

}
