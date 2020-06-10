package com.vaankdeals.newsapp.Adapter;

import android.os.Bundle;

import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.ViewTypes.FragmentCard;
import com.vaankdeals.newsapp.ViewTypes.FragmentCustomAd;
import com.vaankdeals.newsapp.ViewTypes.FragmentNewsMain;
import com.vaankdeals.newsapp.ViewTypes.FragmentNewsYt;
import com.vaankdeals.newsapp.ViewTypes.FragmentProdTag;
import com.vaankdeals.newsapp.ViewTypes.FragmentReview;
import com.vaankdeals.newsapp.ViewTypes.FragmentVideoFull;
import com.vaankdeals.newsapp.ViewTypes.FragmentWeb;
import com.vaankdeals.newsapp.ViewTypes.FragmentYtFull;
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
                    bun_news.putSerializable("model",newsModel);
                    bun_news.putInt("news_position", position);
                    fragmentNewsMain.setArguments(bun_news);
                    return fragmentNewsMain;
                case "2":
                    FragmentCard fragmentCard = new FragmentCard();
                    Bundle bun_card = new Bundle();
                    bun_card.putSerializable("model",newsModel);
                    fragmentCard.setArguments(bun_card);
                    return fragmentCard;
                case "3":
                    FragmentWeb fragmentWeb = new FragmentWeb();
                    Bundle bun_web = new Bundle();
                    bun_web.putSerializable("model",newsModel);
                    fragmentWeb.setArguments(bun_web);
                    return fragmentWeb;
                case "4":
                    FragmentCustomAd fragmentCustomAd = new FragmentCustomAd();
                    Bundle bun_ad = new Bundle();
                    bun_ad.putSerializable("model",newsModel);
                    fragmentCustomAd.setArguments(bun_ad);
                    return fragmentCustomAd;
                case "6":
                    FragmentNewsYt fragmentNewsYt = new FragmentNewsYt();
                    Bundle bun_yt_news = new Bundle();

                    bun_yt_news.putSerializable("model",newsModel);

                    fragmentNewsYt.setArguments(bun_yt_news);
                    return fragmentNewsYt;
                case "7":
                    FragmentReview fragmentReview = new FragmentReview();
                    Bundle bun_rev=new Bundle();
                    bun_rev.putSerializable("model",newsModel);
                    fragmentReview.setArguments(bun_rev);
                    return fragmentReview;
                case "8":
                    FragmentVideoFull fragmentVideoFull = new FragmentVideoFull();
                    Bundle bun_vid=new Bundle();
                    bun_vid.putSerializable("model",newsModel);
                    bun_vid.putInt("video_position",position);
                    fragmentVideoFull.setArguments(bun_vid);
                    return fragmentVideoFull;
                case "9":
                    FragmentYtFull fragmentYtFull = new FragmentYtFull();
                    Bundle bun_ytfull=new Bundle();
                    bun_ytfull.putSerializable("model",newsModel);
                    bun_ytfull.putInt("yt_position",position);
                    fragmentYtFull.setArguments(bun_ytfull);
                    return fragmentYtFull;
                case "10":
                    FragmentProdTag fragmentProdTag = new FragmentProdTag();
                    Bundle bun_prod=new Bundle();
                    bun_prod.putSerializable("model",newsModel);
                    bun_prod.putInt("yt_position",position);
                    fragmentProdTag.setArguments(bun_prod);
                    return fragmentProdTag;
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
