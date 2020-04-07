package com.vaankdeals.newsapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import spencerstudios.com.bungeelib.Bungee;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.r0adkll.slidr.Slidr;
import com.vaankdeals.newsapp.Adapter.AllNewsAdapter;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.Class.ZoomOutPageTransformer;
import com.vaankdeals.newsapp.Model.AllNewsModel;
import com.vaankdeals.newsapp.Model.NewsBook;

import com.vaankdeals.newsapp.Model.AllNewsModel;
import com.vaankdeals.newsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllNewsActivity extends AppCompatActivity implements AllNewsAdapter.videoClickListenerAll,AllNewsAdapter.newsOutListenerAll,AllNewsAdapter.whatsClickListenerAll,AllNewsAdapter.shareClickListenerAll,AllNewsAdapter.bookmarkListenerAll,AllNewsAdapter.actionbarListenerAll {
   
    private AllNewsAdapter newsAdapter;
    private RequestQueue mRequestQueue;
    private List<Object> mNewsList = new ArrayList<>() ;
    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news);

        Slidr.attach(this);
        getSupportActionBar().hide();
        mRequestQueue = Volley.newRequestQueue(this);

        Bundle bundle = getIntent().getExtras();
        String news_cat = bundle.getString("news_cat");




        ViewPager2 newsViewpager = findViewById(R.id.news_swipe_all);
        newsAdapter = new AllNewsAdapter(this,mNewsList);

        newsViewpager.setPageTransformer(new ZoomOutPageTransformer());
        newsViewpager.setAdapter(newsAdapter);
        newsAdapter.setvideoClickListenerAll(AllNewsActivity.this);
        newsAdapter.setnewsOutListenerAll(AllNewsActivity.this);
        newsAdapter.setshareClickListenerAll(AllNewsActivity.this);
        newsAdapter.setwhatsClickListenerAll(AllNewsActivity.this);
        newsAdapter.setbookmarkListenerAll(AllNewsActivity.this);
        newsAdapter.setactionbarListenerAll(AllNewsActivity.this);
        parseJson(news_cat);
    }




    private void parseJson(String newsCat) {
        String url = getString(R.string.server_website_all)+"?news_cat="+newsCat;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("server_response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ser = jsonArray.getJSONObject(i);
                            String news_head = ser.getString("news_head");
                            String news_desc = ser.getString("news_desc");
                            String news_image = ser.getString("news_image");
                            String news_source = ser.getString("news_source");
                            String news_day = ser.getString("news_day");
                            String news_id = ser.getString("news_id");
                            String news_link = ser.getString("news_link");
                            String news_type = ser.getString("news_type");
                            String news_video = ser.getString("news_video");

                            mNewsList.add(new AllNewsModel(news_head,news_desc,news_image,news_source,news_day,news_id,news_link,news_type,news_video));
                        }
                        // Just call notifyDataSetChanged here

                        newsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }




    public void videoActivityAll(int position) {
        AllNewsModel clickeditem = (AllNewsModel) mNewsList.get(position);
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
        AllNewsModel clickeditem = (AllNewsModel) mNewsList.get(position);
        Intent detailintent = new Intent(this, VideoActivity.class);
        detailintent.putExtra("yt_url", clickeditem.getmNewsVideo());
        startActivity(detailintent);
        Bungee.shrink(this);
    }
    private void exoPlayerActivity(int position){
        AllNewsModel clickeditem = (AllNewsModel) mNewsList.get(position);
        Intent exoIntent = new Intent(this, ExoActivity.class);
        exoIntent.putExtra("st_url", clickeditem.getmNewsVideo());
        startActivity(exoIntent);
        Bungee.shrink(this);
    }
    public void newsDetailActivityAll(int position){
        AllNewsModel clickeditem = (AllNewsModel) mNewsList.get(position);
        String newsLink =clickeditem.getmNewslink();
        if (!newsLink.isEmpty()){
            Intent newsIntent = new Intent(this, NewsActivity.class);
            newsIntent.putExtra("ns_url", clickeditem.getmNewslink());
            newsIntent.putExtra("ns_title", clickeditem.getmNewsHead());
            startActivity(newsIntent);
            Bungee.zoom(this);
        }
        else {
            Toast.makeText(this,"Full News Not Available",Toast.LENGTH_SHORT).show();
        }


    }
    public void shareNormalAll(int position){
        Toast.makeText(this,"Share Normal",Toast.LENGTH_SHORT).show();
    }
    public void shareWhatsAll(int position){
        Toast.makeText(this,"Share Whatsapp",Toast.LENGTH_SHORT).show();
    }

    public void bookmarkAllAll(int position){
        AllNewsModel clickeditem = (AllNewsModel) mNewsList.get(position);
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
    public void actionBarViewAll(){

        ActionBar actionBar = Objects.requireNonNull(this).getSupportActionBar();

        assert actionBar != null;
        if (actionBar.isShowing()) {

            actionBar.hide();
        } else {

            actionBar.show();
        }
    }
}
