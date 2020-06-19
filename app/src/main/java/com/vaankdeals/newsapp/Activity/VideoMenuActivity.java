package com.vaankdeals.newsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import spencerstudios.com.bungeelib.Bungee;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.vaankdeals.newsapp.Adapter.VideoMenuAdapter;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.Model.VideoModel;
import com.vaankdeals.newsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoMenuActivity extends AppCompatActivity  implements VideoMenuAdapter.videoListener {

    private ArrayList<VideoModel> MovieList= new ArrayList<>();
    private ArrayList<VideoModel> FoodList= new ArrayList<>();
    private ArrayList<VideoModel> TechList= new ArrayList<>();
    private ArrayList<VideoModel> SportsList= new ArrayList<>();
    private ArrayList<VideoModel> EntertainmentList= new ArrayList<>();
    private ArrayList<VideoModel> NewsList= new ArrayList<>();

    private LinearLayout news_place;
    private LinearLayout movie_place;

    RequestQueue mRequestQueue;
    VideoMenuAdapter newsVideoAdapter;
    VideoMenuAdapter techVideoAdapter;
    VideoMenuAdapter foodVideoAdapter;
    VideoMenuAdapter entertainmentVideoAdapter;
    VideoMenuAdapter sportsVideoAdapter;
    VideoMenuAdapter movieVideoAdapter;

    RecyclerView movieVideoRecycler;
    RecyclerView newsVideoRecycler;
    RecyclerView  techVideoRecycler;
    RecyclerView sportsVideoRecycler;
    RecyclerView foodVideoRecycler;
    RecyclerView entertainmentVideoRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_menu);
        mRequestQueue = Volley.newRequestQueue(this);

        ImageView mainImageSrc= findViewById(R.id.video_menu_main_image);
        String mainImage ="http://filmapp.000webhostapp.com/newsapp/media/f5a9e06ff6b9005dce709fa018019e55 (1).jpg";
        Glide.with(this).load(mainImage).into(mainImageSrc);
        newsVideoRecycler =findViewById(R.id.recycler_view_news);
        entertainmentVideoRecycler =findViewById(R.id.recycler_view_entertainment);
        foodVideoRecycler =findViewById(R.id.recycler_view_food);
        movieVideoRecycler =findViewById(R.id.recycler_view_movie);
        techVideoRecycler =findViewById(R.id.recycler_view_tech);
        sportsVideoRecycler =findViewById(R.id.recycler_view_sports);

        news_place=findViewById(R.id.news_place);
        movie_place=findViewById(R.id.movie_place);

        newsVideoAdapter = new VideoMenuAdapter(this,NewsList);
        entertainmentVideoAdapter = new VideoMenuAdapter(this,EntertainmentList);
        sportsVideoAdapter = new VideoMenuAdapter(this,SportsList);
        foodVideoAdapter = new VideoMenuAdapter(this,FoodList);
        techVideoAdapter = new VideoMenuAdapter(this,TechList);
        movieVideoAdapter = new VideoMenuAdapter(this,MovieList);
        entertainmentVideoAdapter.setmVideoListener(this);
        newsVideoAdapter.setmVideoListener(this);
        sportsVideoAdapter.setmVideoListener(this);
        techVideoAdapter.setmVideoListener(this);
        foodVideoAdapter.setmVideoListener(this);
        movieVideoAdapter.setmVideoListener(this);

        movieVideoRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        movieVideoRecycler.setAdapter(movieVideoAdapter);
        sportsVideoRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        sportsVideoRecycler.setAdapter(sportsVideoAdapter);
        entertainmentVideoRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        entertainmentVideoRecycler.setAdapter(entertainmentVideoAdapter);
        newsVideoRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        newsVideoRecycler.setAdapter(newsVideoAdapter);
        foodVideoRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        foodVideoRecycler.setAdapter(foodVideoAdapter);
        techVideoRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        techVideoRecycler.setAdapter(techVideoAdapter);
        parseJson();

    }

    private void parseJson() {
        String url = getString(R.string.server_website_video);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("server_response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ser = jsonArray.getJSONObject(i);
                            String video_image = ser.getString("news_image");
                            String video_id = ser.getString("news_id");
                            String video_type = ser.getString("news_type");
                            String video_cat = ser.getString("news_cat");
                            switch (video_cat){
                                case "food" :
                                    FoodList.add(new VideoModel(video_type,video_image,video_id,video_cat));
                                    break;
                                case "movie" :
                                    MovieList.add(new VideoModel(video_type,video_image,video_id,video_cat));
                                    break;
                                case "tech" :
                                    TechList.add(new VideoModel(video_type,video_image,video_id,video_cat));
                                    break;
                                case "news" :
                                    NewsList.add(new VideoModel(video_type,video_image,video_id,video_cat));
                                    break;
                                case "sports" :
                                    SportsList.add(new VideoModel(video_type,video_image,video_id,video_cat));
                                    break;
                                case "entertainment" :
                                    EntertainmentList.add(new VideoModel(video_type,video_image,video_id,video_cat));
                                    break;
                                    default:
                                        TechList.add(new VideoModel(video_type,video_image,video_id,video_cat));

                            }
                        }

                        news_place.setVisibility(View.GONE);
                        movie_place.setVisibility(View.GONE);
                        newsVideoAdapter.notifyDataSetChanged();
                        techVideoAdapter.notifyDataSetChanged();
                        movieVideoAdapter.notifyDataSetChanged();
                        sportsVideoAdapter.notifyDataSetChanged();
                        foodVideoAdapter.notifyDataSetChanged();
                        entertainmentVideoAdapter.notifyDataSetChanged();
                        

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }, volleyError -> {


        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                4000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    @Override
    public void vidClick(String url,String videoType) {
        //String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        Intent detailintent;
        if (!url.isEmpty() && videoType.equals("9"))
        {
            detailintent = new Intent(this, VideoActivity.class);
        }
        else
        {
            // Not Valid youtube URL
            detailintent = new Intent(this, ExoActivity.class);
        }
        detailintent.putExtra("videoId", url);
        startActivity(detailintent);
    }
}
