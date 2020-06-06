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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.vaankdeals.newsapp.R;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

public class FragmentNewsYt extends Fragment {

    YouTubePlayerView youTubePlayerView;
    public FragmentNewsYt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.newsytvideoitem, container, false);


        String news_head =getArguments().getString("news_head");
        String news_video =getArguments().getString("news_video");
        String news_image =getArguments().getString("news_image");
        String news_desc =getArguments().getString("news_desc");
        String news_url =getArguments().getString("news_url");
        String news_source =getArguments().getString("news_source");
        String news_day =getArguments().getString("news_day");
        String news_extra = "click on title to read more on " + news_source + " / " + news_day;

        TextView mNewsHead = view.findViewById(R.id.news_video_head);

        youTubePlayerView=view.findViewById(R.id.youtube_player_view);
        TextView mNewsDesc = view.findViewById(R.id.news_video_desc);
        ImageView mNewsImage = view.findViewById(R.id.news_video_image);
        TextView mNewsExtra = view.findViewById(R.id.news_video_extra);
        Button mShareButton = view.findViewById(R.id.video_sharecard);
        Button mWhatsButton = view.findViewById(R.id.video_sharewhats);
        Button mBookmarkButton = view.findViewById(R.id.video_bookmark_button);

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
