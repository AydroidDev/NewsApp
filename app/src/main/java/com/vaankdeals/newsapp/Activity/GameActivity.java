package com.vaankdeals.newsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.r0adkll.slidr.Slidr;
import com.vaankdeals.newsapp.Adapter.GameAdapter;
import com.vaankdeals.newsapp.Model.GameModel;
import com.vaankdeals.newsapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements GameAdapter.gameListener{

    ArrayList<GameModel> GameList = new ArrayList<>();
    RequestQueue mRequestQueue;
    GameAdapter gameAdapter;
    GridLayout gamePlaceHolder;
    RecyclerView recyclerView;
    ImageView imageView1;
    ImageView imageView2;
    LinearLayout imageContainer;
    LinearLayout layAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mRequestQueue = Volley.newRequestQueue(this);
        recyclerView=findViewById(R.id.recycler_view);
        gamePlaceHolder=findViewById(R.id.game_placeholder);
        Slidr.attach(this);
        layAll =findViewById(R.id.game_main);
        imageContainer=findViewById(R.id.imageContainer);
        imageView1=findViewById(R.id.gridimage1);
        imageView2=findViewById(R.id.gridimage2);
        gameAdapter = new GameAdapter(this,GameList);
        gameAdapter.setmGameListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(gameAdapter);


        parseJson();
        recentItems();

    }
    public void sliderItem(){
        if(GameList.size()!=0) {
            Random random = new Random();
            int no = random.nextInt(GameList.size());
            GameModel clickeditem = GameList.get(no);
            String slideImage = clickeditem.getGame_image();
            ImageView slideImageView = findViewById(R.id.sliderImage);
            Button slideButton = findViewById(R.id.sliderButton);
            String slideUrl = clickeditem.getGame_url();
            if (!GameActivity.this.isFinishing()) {
                Glide.with(this).load(slideImage).into(slideImageView);
            }
            slideButton.setOnClickListener(v -> openGame(slideUrl));
        }
    }
private void recentItems(){
    SharedPreferences sharedPreferences = getSharedPreferences("recentGames", MODE_PRIVATE);


    if(sharedPreferences.contains("image1")){
        if(sharedPreferences.contains("image2")){
            imageContainer.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            String image1=sharedPreferences.getString("image1","");
            String image2=sharedPreferences.getString("image2","");
            String url1=sharedPreferences.getString("url1","");
            String url2=sharedPreferences.getString("url2","");
            if (!GameActivity.this.isFinishing()) {
                Glide.with(this).load(image1).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(20))).into(imageView1);
            }
            if (!GameActivity.this.isFinishing()) {
                Glide.with(this).load(image2).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(20))).into(imageView2);
            }
            imageView1.setOnClickListener(v -> {
                openGame(url1);
            });
            imageView2.setOnClickListener(v->{
                openGame(url2);
            });
        }
        else{
            imageContainer.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.VISIBLE);
            String image1=sharedPreferences.getString("image1","");
            if (!GameActivity.this.isFinishing()) {
                Glide.with(this).load(image1).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(20))).into(imageView1);
            }
        }
    }
}
    private void parseJson() {
        String url = getString(R.string.server_website_game);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("server_response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ser = jsonArray.getJSONObject(i);
                            String game_image = ser.getString("game_image");
                            String game_url=ser.getString("game_url");
                            GameList.add(new GameModel(game_url,game_image));
                        }
                        // Just call notifyDataSetChanged here
                        gamePlaceHolder.setVisibility(View.GONE);
                        layAll.setVisibility(View.VISIBLE);
                        gameAdapter.notifyDataSetChanged();
                        sliderItem();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, volleyError ->{
        });
        mRequestQueue.add(request);
    }
    public void gameClick(int position){
        SharedPreferences sharedPreferences = getSharedPreferences("recentGames", MODE_PRIVATE);
        GameModel clickeditem = GameList.get(position);
        if(sharedPreferences.contains("image1")){
            if(!sharedPreferences.getString("image1","").equals(clickeditem.getGame_image())) {
                String image2 = sharedPreferences.getString("image1", "");
                String url2 = sharedPreferences.getString("url1", "");

                SharedPreferences.Editor addRecent
                        = sharedPreferences.edit();
                addRecent.putString(
                        "image1", clickeditem.getGame_image());
                addRecent.putString("url1", clickeditem.getGame_url());

                addRecent.putString(
                        "image2", image2);
                addRecent.putString("url2", url2);
                addRecent.apply();
            }

        }

        else {
            SharedPreferences.Editor addRecent
                    = sharedPreferences.edit();
            addRecent.putString(
                    "image1", clickeditem.getGame_image());
            addRecent.putString("url1", clickeditem.getGame_url());
            addRecent.apply();
        }
        String url =clickeditem.getGame_url();
           openGame(url);
    }
    public void openGame(String url){
        Intent webIntent = new Intent(this, WebActivity.class);
        webIntent.putExtra("ns_url", url);
        startActivity(webIntent);
    }
}
