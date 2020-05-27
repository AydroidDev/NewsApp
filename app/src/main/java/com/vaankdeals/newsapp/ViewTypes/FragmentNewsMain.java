package com.vaankdeals.newsapp.ViewTypes;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vaankdeals.newsapp.R;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

public class FragmentNewsMain extends Fragment {

    public FragmentNewsMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.newsitem, container, false);
       
        String news_head =getArguments().getString("news_head");
        String news_image =getArguments().getString("news_image");
        String news_desc =getArguments().getString("news_desc");
        String news_url =getArguments().getString("news_url");
        String news_source =getArguments().getString("news_source");
        String news_day =getArguments().getString("news_day");
        String news_extra = "click on title to read more on " + news_source + " / " + news_day;

        TextView mNewsHead = view.findViewById(R.id.news_head);
        TextView mNewsDesc = view.findViewById(R.id.news_desc);
        ImageView mNewsImage = view.findViewById(R.id.news_image);
        TextView mNewsExtra = view.findViewById(R.id.news_extra);
        Button mShareButton = view.findViewById(R.id.sharecard);
        Button mWhatsButton = view.findViewById(R.id.sharewhats);
        Button mBookmarkButton = view.findViewById(R.id.bookmark_button);

        mNewsHead.setText(news_head);
        mNewsDesc.setText(news_desc);
        mNewsExtra.setText(news_extra);
        Glide.with(getActivity()).load(news_image).into(mNewsImage);

        mNewsHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                builder.setToolbarColor(24);
                customTabsIntent.launchUrl(getContext(), Uri.parse(news_url));
            }
        });

        return view;
    }

}
