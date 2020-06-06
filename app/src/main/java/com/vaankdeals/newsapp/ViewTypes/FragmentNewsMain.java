package com.vaankdeals.newsapp.ViewTypes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vaankdeals.newsapp.Class.CommonUtils;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.Fragment.NewsFragment;
import com.vaankdeals.newsapp.Model.NewsBook;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

public class FragmentNewsMain extends Fragment {

    private String news_id;
    private Button mBookmarkButton;

    public FragmentNewsMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.newsitem, container, false);

        NewsModel model =(NewsModel)getArguments().getSerializable("model");
        news_id =model.getmNewsId();
        int news_position =getArguments().getInt("news_position");
        String news_head =model.getmNewsHead();
        String news_image =model.getmNewsImage();
        String news_desc =model.getmNewsDesc();
        String news_url =model.getmNewslink();
        String news_source =model.getmNewsSource();
        String news_day =model.getmNewsDay();
        String news_extra = "click on title to read more on " + news_source + " / " + news_day;

        TextView mNewsHead = view.findViewById(R.id.news_head);
        TextView mNewsDesc = view.findViewById(R.id.news_desc);
        ImageView mNewsImage = view.findViewById(R.id.news_image);
        TextView mNewsExtra = view.findViewById(R.id.news_extra);
        Button mShareButton = view.findViewById(R.id.sharecard);
        Button mWhatsButton = view.findViewById(R.id.sharewhats);
        mBookmarkButton = view.findViewById(R.id.bookmark_button);

        mNewsHead.setText(news_head);
        mNewsDesc.setText(news_desc);
        mNewsExtra.setText(news_extra);
        Glide.with(requireActivity()).load(news_image).into(mNewsImage);

        mNewsHead.setOnClickListener(v -> {

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            builder.setToolbarColor(24);
            customTabsIntent.launchUrl(getContext(), Uri.parse(news_url));
        });

        mBookmarkButton.setOnClickListener(v -> {
            boolean isAdded= CommonUtils.bookmarkAll(model,requireContext());
            if(isAdded) {
                mBookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
                Toast.makeText(requireContext(), "Post Removed", Toast.LENGTH_SHORT).show();
            }
            else{
                mBookmarkButton.setBackgroundResource(R.drawable.bookmark_button_clicked);
            Toast.makeText(requireContext(),"Post Added",Toast.LENGTH_SHORT).show();
}
        });
        return view;
    }

}
