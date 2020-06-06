package com.vaankdeals.newsapp.ViewTypes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import androidx.fragment.app.Fragment;

public class FragmentReview extends Fragment {

    public FragmentReview() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView= inflater.inflate(R.layout.review_item, container, false);
        View mReviewOverlay=itemView.findViewById(R.id.review_overlay);
        ImageView mReviewImage = itemView.findViewById(R.id.reviewimage);
        TextView mReviewHead=itemView.findViewById(R.id.reviewhead);
        TextView mReviewDesc=itemView.findViewById(R.id.reviewdesc);
        TextView mReviewRating=itemView.findViewById(R.id.reviewrating);
        Button mReviewLink = itemView.findViewById(R.id.reviewlink);

        NewsModel model =(NewsModel)getArguments().getSerializable("model");
        String rev_url=model.getmNewslink();
        Glide.with(requireActivity()).load(model.getmNewsImage()).into(mReviewImage);
        mReviewHead.setText(model.getmNewsHead());
        mReviewDesc.setText(model.getmNewsDesc());
        mReviewRating.setText(model.getmNewsSource());
        mReviewLink.setText(model.getmNewsSource());

        mReviewLink.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rev_url));
            startActivity(browserIntent);
        });
        return itemView;
    }

}
