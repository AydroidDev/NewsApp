package com.vaankdeals.newsapp.ViewTypes;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import androidx.fragment.app.Fragment;

public class FragmentDeal extends Fragment {

    public FragmentDeal() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_deal, container, false);
        ImageView cardView= view.findViewById(R.id.deal_image);
        TextView deal_title = view.findViewById(R.id.deal_title);
        TextView deal_desc = view.findViewById(R.id.deal_desc);
        TextView deal_oldprice = view.findViewById(R.id.deal_oldprice);
        TextView deal_button = view.findViewById(R.id.deal_button);
        TextView deal_newprice = view.findViewById(R.id.deal_newprice);
        RatingBar deal_rating = view.findViewById(R.id.deal_rating);
        assert getArguments() != null;
        NewsModel model =(NewsModel)getArguments().getSerializable("model");
        assert model != null;

        deal_button.setText(model.getmNewsData3());
        deal_rating.setRating(Float.parseFloat(model.getmNewsSource()));
        deal_title.setText(model.getmNewsHead());
        deal_desc.setText(model.getmNewsDesc());
        deal_oldprice.setText(model.getmNewsData1());
        deal_newprice.setText(model.getmNewsData2());
        Glide.with(requireActivity()).load(model.getmNewsImage()).thumbnail(0.25f).into(cardView);

        deal_oldprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        return view;
    }

}
