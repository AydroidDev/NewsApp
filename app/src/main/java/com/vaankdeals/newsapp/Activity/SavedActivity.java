package com.vaankdeals.newsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import spencerstudios.com.bungeelib.Bungee;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vaankdeals.newsapp.Adapter.SavedAdapter;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.Model.NewsBook;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;
import java.util.List;

public class SavedActivity extends AppCompatActivity implements SavedAdapter.videoClickListener,SavedAdapter.newsOutListener,SavedAdapter.whatsClickListener,SavedAdapter.shareClickListener,SavedAdapter.bookmarkListener {

    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";
    private SavedAdapter newsAdapter;
    private List<NewsBook> mSavedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        getSupportActionBar().hide();
        DatabaseHandler db = new DatabaseHandler(this);
        mSavedList = db.getAllContacts();
        ViewPager2 newsViewpager = findViewById(R.id.news_swipe_saved);
        newsAdapter = new SavedAdapter(SavedActivity.this,mSavedList);
        newsViewpager.setAdapter(newsAdapter);
        newsAdapter.setvideoClickListener(SavedActivity.this);
        newsAdapter.setnewsOutListener(this);
        newsAdapter.setshareClickListener(this);
        newsAdapter.setwhatsClickListener(this);
        newsAdapter.setbookmarkListener(this);

    }
    @Override
    public void videoActivity(int position) {
        NewsBook clickeditem = (NewsBook) mSavedList.get(position);
        String url = clickeditem.getmNewsVideo();
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (!url.isEmpty() && url.matches(pattern))
        {
            youtubeActivity(position);
        }
        else
        {
            // Not Valid youtube URL
            exoPlayerActivity(position);
        }
    }
    private void youtubeActivity(int position){
        NewsBook clickeditem = (NewsBook) mSavedList.get(position);
        Intent detailintent = new Intent(this, VideoActivity.class);
        detailintent.putExtra("yt_url", clickeditem.getmNewsVideo());
        startActivity(detailintent);
        Bungee.shrink(this);
    }
    private void exoPlayerActivity(int position){
        NewsBook clickeditem = (NewsBook) mSavedList.get(position);
        Intent exoIntent = new Intent(this, ExoActivity.class);
        exoIntent.putExtra("st_url", clickeditem.getmNewsVideo());
        startActivity(exoIntent);
        Bungee.shrink(this);
    }
    public void newsDetailActivity(int position){
        NewsBook clickeditem = (NewsBook) mSavedList.get(position);
        Intent newsIntent = new Intent(this, NewsActivity.class);
        newsIntent.putExtra("ns_url", clickeditem.getmNewslink());
        newsIntent.putExtra("ns_title", clickeditem.getmNewsHead());
        startActivity(newsIntent);
        Bungee.zoom(this);
    }
    public void shareNormal(int position){
        Toast.makeText(this,"Share Normal",Toast.LENGTH_SHORT).show();
    }
    public void shareWhats(int position){
        Toast.makeText(this,"Share Whatsapp",Toast.LENGTH_SHORT).show();
    }

    public void bookmarkAll(int position){
        NewsBook clickeditem = (NewsBook) mSavedList.get(position);
        if(clickeditem.getmNewsType().equals("1")){
            final Button buttonNews = findViewById(R.id.bookmark_button);
            buttonNews.setBackgroundResource(R.drawable.bookmark_button_clicked);
        }
        else if(clickeditem.getmNewsType().equals("5")) {
            final Button buttonVideo = findViewById(R.id.video_bookmark_button);
            buttonVideo.setBackgroundResource(R.drawable.bookmark_button_clicked);

        }
        DatabaseHandler db = new DatabaseHandler(this);

        String fieldValue =String.valueOf(clickeditem.getmNewsId());
        String countQuery = "SELECT  * FROM " + TABLE_NEWS + " where " + NEWS_ID +  " = " + fieldValue;
        SQLiteDatabase dbs = db.getReadableDatabase();
        SQLiteDatabase dbsw = db.getWritableDatabase();
        Cursor cursor = dbs.rawQuery(countQuery, null);
        int recount = cursor.getCount();

        if(recount <= 0){


            db.addContact(new NewsBook(0,clickeditem.getmNewsHead(),clickeditem.getmNewsDesc(),
                    clickeditem.getmNewsImage(),clickeditem.getmNewsSource(),clickeditem.getmNewsDay(),
                    clickeditem.getmNewslink(),clickeditem.getmNewsId(),
                    clickeditem.getmNewsType(),clickeditem.getmNewsVideo()));
            Toast.makeText(this,"News Saved", Toast.LENGTH_SHORT).show();
        }
        else {
            if(clickeditem.getmNewsType().equals("1")){
                final Button buttonNews = findViewById(R.id.bookmark_button);
                buttonNews.setBackgroundResource(R.drawable.bookmark_button);
            }
            else if(clickeditem.getmNewsType().equals("5")) {
                final Button buttonVideo = findViewById(R.id.video_bookmark_button);
                buttonVideo.setBackgroundResource(R.drawable.bookmark_button);

            }
            dbsw.delete(TABLE_NEWS, NEWS_ID + " = ?",
                    new String[] { String.valueOf(fieldValue) });
            Toast.makeText(this,"News Removed", Toast.LENGTH_SHORT).show();
        }

        cursor.close();


    }
}
