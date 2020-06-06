package com.vaankdeals.newsapp.ViewTypes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import androidx.fragment.app.Fragment;

public class FragmentCard extends Fragment {

    public FragmentCard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fullphotoitem, container, false);
        ImageView cardView= view.findViewById(R.id.fullimage);
        assert getArguments() != null;
        NewsModel model =(NewsModel)getArguments().getSerializable("model");
        assert model != null;
        Glide.with(requireActivity()).load(model.getmNewsImage()).thumbnail(0.25f).into(cardView);
        return view;
    }

}
