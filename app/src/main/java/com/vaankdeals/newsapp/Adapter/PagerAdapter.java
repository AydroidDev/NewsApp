package com.vaankdeals.newsapp.Adapter;

import android.os.Bundle;

import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.ViewTypes.FragmentCard;
import com.vaankdeals.newsapp.ViewTypes.FragmentCustomAd;
import com.vaankdeals.newsapp.ViewTypes.FragmentNewsMain;
import com.vaankdeals.newsapp.ViewTypes.FragmentReview;
import com.vaankdeals.newsapp.ViewTypes.FragmentVideoFull;
import com.vaankdeals.newsapp.ViewTypes.FragmentWeb;
import com.vaankdeals.newsapp.ViewTypes.NativeFragment;

import java.util.HashMap;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class PagerAdapter extends FragmentStateAdapter {

    private List<Object> mNewsList;

    public PagerAdapter(@NonNull Fragment fragment,List<Object> mNewsList) {
        super(fragment);
        this.mNewsList=mNewsList;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Object model = mNewsList.get(position);
        if(model instanceof UnifiedNativeAd){
            UnifiedNativeAd unifiedNativeAd = (UnifiedNativeAd) mNewsList.get(position);
            return new NativeFragment(unifiedNativeAd);
        }
        else {
            NewsModel newsModel = (NewsModel) mNewsList.get(position);
            switch (newsModel.getmNewsType()) {
                case "1":
                    FragmentNewsMain fragmentNewsMain = new FragmentNewsMain();
                    Bundle bun_news = new Bundle();

                    bun_news.putString("news_image", newsModel.getmNewsImage());
                    bun_news.putString("news_head", newsModel.getmNewsHead());
                    bun_news.putString("news_desc", newsModel.getmNewsDesc());
                    bun_news.putString("news_url", newsModel.getmNewslink());
                    bun_news.putString("news_source", newsModel.getmNewsSource());
                    bun_news.putString("news_day", newsModel.getmNewsDay());

                    fragmentNewsMain.setArguments(bun_news);
                    return fragmentNewsMain;
                case "2":
                    FragmentCard fragmentCard = new FragmentCard();
                    Bundle bun_card = new Bundle();
                    bun_card.putString("card_image", newsModel.getmNewsImage());
                    fragmentCard.setArguments(bun_card);
                    return fragmentCard;
                case "3":
                    FragmentWeb fragmentWeb = new FragmentWeb();
                    Bundle bun_web = new Bundle();
                    bun_web.putString("web_url", newsModel.getmNewslink());
                    fragmentWeb.setArguments(bun_web);
                    return fragmentWeb;
                case "4":
                    FragmentCustomAd fragmentCustomAd = new FragmentCustomAd();
                    Bundle bun_ad = new Bundle();
                    bun_ad.putString("ad_image", newsModel.getmNewsImage());
                    bun_ad.putString("ad_url", newsModel.getmNewslink());
                    fragmentCustomAd.setArguments(bun_ad);
                    return fragmentCustomAd;
                case "8":
                    FragmentVideoFull fragmentVideoFull = new FragmentVideoFull();
                    Bundle bun_vid=new Bundle();
                    bun_vid.putString("video_head",newsModel.getmNewsHead());
                    bun_vid.putString("video_desc",newsModel.getmNewsDesc());
                    bun_vid.putString("video_dest",newsModel.getmNewslink());
                    bun_vid.putString("video_url",newsModel.getmNewsVideo());
                    bun_vid.putString("video_rating",newsModel.getmNewsSource());
                    fragmentVideoFull.setArguments(bun_vid);
                    return fragmentVideoFull;
                case "7":
                    FragmentReview fragmentReview = new FragmentReview();
                    Bundle bun_rev=new Bundle();
                    bun_rev.putString("rev_head",newsModel.getmNewsHead());
                    bun_rev.putString("rev_desc",newsModel.getmNewsDesc());
                    bun_rev.putString("rev_rat",newsModel.getmNewsVideo());
                    bun_rev.putString("rev_but",newsModel.getmNewsSource());
                    bun_rev.putString("rev_image",newsModel.getmNewsImage());
                    bun_rev.putString("rev_link",newsModel.getmNewslink());
                    fragmentReview.setArguments(bun_rev);
                    return fragmentReview;
                default:
                    return null;
            }
        }
    }


    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}
