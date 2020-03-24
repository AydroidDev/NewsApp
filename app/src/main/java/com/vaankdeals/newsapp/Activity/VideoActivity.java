package com.vaankdeals.newsapp.Activity;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.r0adkll.slidr.Slidr;
import com.vaankdeals.newsapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VideoActivity extends YouTubeBaseActivity {
    private YouTubePlayer youTubePlayer;
    private boolean isYouTubePlayerFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Slidr.attach(this);

        // Find the Ad Container




        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String mUrl = bundle.getString("yt_url");
        String vId =getVideoIdFromYoutubeUrl(mUrl);
        final YouTubePlayerView youtubePlayerView = findViewById(R.id.youtubePlayerView);
        playVideo(vId,youtubePlayerView);
    }
    public String getVideoIdFromYoutubeUrl(String url){
        String videoId = null;
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if(matcher.find()){
            videoId = matcher.group(1);
        }
        return videoId;
    }
    private void playVideo(final String videoId, YouTubePlayerView youTubePlayerView) {
        //initialize youtube player view
        youTubePlayerView.initialize("AIzaSyBw3nazuqRNMy2t3RzGGCnQJOSQ7vChBn0",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer player, boolean b) {
                        youTubePlayer = player;
                        youTubePlayer.loadVideo(videoId);

                        youTubePlayer.setShowFullscreenButton(true);
                        player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                            @Override
                            public void onFullscreen(boolean b1) {
                                isYouTubePlayerFullScreen = b1;
                                if (isYouTubePlayerFullScreen) {
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                                } else {
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                    youTubePlayer.setFullscreen(false);
                                }


                            }
                        });

                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });




    }

    @Override
    public void onBackPressed() {
        if (youTubePlayer != null && isYouTubePlayerFullScreen){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            youTubePlayer.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

}
