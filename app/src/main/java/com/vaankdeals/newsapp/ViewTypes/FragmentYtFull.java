package com.vaankdeals.newsapp.ViewTypes;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar;
import com.vaankdeals.newsapp.Activity.MainActivity;
import com.vaankdeals.newsapp.Fragment.NewsFragment;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class FragmentYtFull extends Fragment {

    private YouTubePlayerView youTubePlayerViewFull;
    private YouTubePlayerSeekBar youTubePlayerSeekBar;
    private ProgressBar ytprogress;
    private Button mPlay;
    private LinearLayout ytContainer;
    private FrameLayout imageoverlay;
    private Context mContext;
    private CountDownTimer ytTimer;

    private int yt_position;
    public FragmentYtFull() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fullytitem, container, false);

        assert getArguments() != null;
        NewsModel model =(NewsModel)getArguments().getSerializable("model");
        String yt_head = model.getmNewsHead();
        String yt_desc = model.getmNewsDesc();
        String yt_dest = model.getmNewslink();
        String yt_image = model.getmNewsImage();
        String yt_rating = model.getmNewsSource();
        yt_position = getArguments().getInt("yt_position");
        String yt_url =model.getmNewsVideo();

        mPlay=view.findViewById(R.id.ytplay);
        View ytOverlay = view.findViewById(R.id.yt_overlay);
        LinearLayout yttextcontainer = view.findViewById(R.id.yttextcontainer);
         imageoverlay = view.findViewById(R.id.image_black);
         ytContainer = view.findViewById(R.id.yt_container);
        ImageView mYtImage = view.findViewById(R.id.ytimage);
        Button mPlay = view.findViewById(R.id.ytplay);
        TextView mYtTitle = view.findViewById(R.id.yttitle);
        TextView mYtDesc = view.findViewById(R.id.ytdesc);
        Button mYtButton = view.findViewById(R.id.ytbutton);
        TextView mYtRating = view.findViewById(R.id.ytrating);

        youTubePlayerViewFull = view.findViewById(R.id.yt_view);
         ytprogress = view.findViewById(R.id.ytprogress);
         youTubePlayerSeekBar = view.findViewById(R.id.yt_seekbar);
        mYtTitle.setText(yt_head);
        mYtDesc.setText(yt_desc);
        mYtRating.setText(yt_rating);
        mYtButton.setText(yt_rating);

        if(yt_rating.equals(""))
            mYtButton.setVisibility(View.GONE);

        if(yt_rating.equals("")&&yt_head.equals(""))
            ytOverlay.setVisibility(View.GONE);

        Glide.with(mContext).load(yt_image).into(mYtImage);
        mPlay.setOnClickListener(v -> {
            mPlay.setVisibility(View.INVISIBLE);
            imageoverlay.setForeground(new ColorDrawable(ContextCompat.getColor(mContext, R.color.semiblack)));
            ytContainer.setVisibility(View.VISIBLE);
            youTubePlayerViewFull.getYouTubePlayerWhenReady(YouTubePlayer::play);

        });
        mYtButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(yt_dest));
            startActivity(browserIntent);
        });

        MainActivity.viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                if(position==1&& NewsFragment.newsViewpager.getCurrentItem()==yt_position) {
                    startYt();
                }
                else
                    pauseYt();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        initializeYt(yt_url);
        return view;
    }

    private void setYtprogress(){
        // timer for seekbar
        final int oneMin = 3* 1000;
       ytTimer = new CountDownTimer(oneMin, 50) {
            public void onTick(long millisUntilFinished) {

                //forward progress
                long finishedSeconds = oneMin - millisUntilFinished;
                int total = (int) (((float)finishedSeconds / (float)oneMin) * 100.0);
                ytprogress.setProgress(total);
            }

            public void onFinish() {
                // DO something when 1 minute is up
                ytprogress.setVisibility(View.INVISIBLE);
                imageoverlay.setForeground(new ColorDrawable(ContextCompat.getColor(mContext, R.color.semiblack)));
                ytContainer.setVisibility(View.VISIBLE);
                if(MainActivity.viewPagerMain.getCurrentItem()==1&& NewsFragment.newsViewpager.getCurrentItem()==yt_position)
                youTubePlayerViewFull.getYouTubePlayerWhenReady(YouTubePlayer::play);
            }
        };
       ytTimer.start();
    }
    private void initializeYt(String yt_url){

            youTubePlayerViewFull.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer initializedYoutubePlayer) {
                    initializedYoutubePlayer.cueVideo(getVideoIdFromYoutubeUrl(yt_url), 0);
                    initializedYoutubePlayer.addListener(youTubePlayerSeekBar);
                    youTubePlayerSeekBar.setYoutubePlayerSeekBarListener(initializedYoutubePlayer::seekTo);
                }
            });

    }
    private String getVideoIdFromYoutubeUrl(String url) {
        String videoId = null;
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        return videoId;
    }

    @Override
    public void onResume() {
        super.onResume();
        setYtprogress();
    }

    @Override
    public void onPause() {
        super.onPause();
       pauseYt();
       if(ytTimer!=null)
           ytTimer.cancel();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerViewFull.release();
    }

    private void pauseYt(){
        if(youTubePlayerViewFull!=null)
            youTubePlayerViewFull.getYouTubePlayerWhenReady(YouTubePlayer::pause);
    }
    private void startYt(){
        if(youTubePlayerViewFull!=null)
            youTubePlayerViewFull.getYouTubePlayerWhenReady(YouTubePlayer::play);
    }
}
