package com.vaankdeals.newsapp.Class;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.vaankdeals.newsapp.R;

import cn.jzvd.JzvdStd;

public class JzvdStdTikTok extends JzvdStd {
    public JzvdStdTikTok(Context context) {
        super(context);
    }

    public JzvdStdTikTok(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        bottomContainer.setVisibility(GONE);
        topContainer.setVisibility(GONE);
        bottomProgressBar.setVisibility(GONE);
        posterImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }


    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
        bottomContainer.setVisibility(GONE);
        topContainer.setVisibility(GONE);
    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro,
                                        int posterImg, int bottomPro, int retryLayout) {
        topContainer.setVisibility(topCon);
        bottomContainer.setVisibility(bottomCon);
        startButton.setVisibility(startBtn);
        loadingProgressBar.setVisibility(loadingPro);
        posterImageView.setVisibility(posterImg);
        bottomProgressBar.setVisibility(GONE);
        mRetryLayout.setVisibility(retryLayout);
    }

    @Override
    public void dissmissControlView() {
        if (state != STATE_NORMAL
                && state != STATE_ERROR
                && state != STATE_AUTO_COMPLETE) {
            post(() -> {
                bottomContainer.setVisibility(View.INVISIBLE);
                topContainer.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.INVISIBLE);
                if (clarityPopWindow != null) {
                    clarityPopWindow.dismiss();
                }
                if (screen != SCREEN_TINY) {
                    bottomProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    public void pauseVid() {
        if (startButton.getVisibility()==View.INVISIBLE) {
            startButton.performClick();
        }
    }
    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
        startButton.performClick();
        bottomContainer.setVisibility(GONE);
        topContainer.setVisibility(GONE);
    }

    public void updateStartImage() {
        if (state == STATE_PLAYING) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            replayTextView.setVisibility(GONE);
        } else if (state == STATE_ERROR) {
            startButton.setVisibility(INVISIBLE);
            replayTextView.setVisibility(GONE);
        } else if (state == STATE_AUTO_COMPLETE) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            replayTextView.setVisibility(VISIBLE);
        } else {
            startButton.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            replayTextView.setVisibility(GONE);
        }
    }
}