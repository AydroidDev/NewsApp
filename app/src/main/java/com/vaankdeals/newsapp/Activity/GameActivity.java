package com.vaankdeals.newsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vaankdeals.newsapp.Adapter.GameAdapter;
import com.vaankdeals.newsapp.Model.GameModel;
import com.vaankdeals.newsapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements GameAdapter.gameListener{

    ArrayList<GameModel> GameList = new ArrayList<>();
    RequestQueue mRequestQueue;
    GameAdapter gameAdapter;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ImageView imageView1;
    ImageView imageView2;
    LinearLayout imageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mRequestQueue = Volley.newRequestQueue(this);
        recyclerView=findViewById(R.id.recycler_view);
        progressBar=findViewById(R.id.progress_game);

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
private void recentItems(){
    SharedPreferences sharedPreferences = getSharedPreferences("recentGames", MODE_PRIVATE);


    if(sharedPreferences.contains("image1")){
        if(sharedPreferences.contains("image2")){
            imageContainer.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            String image1=sharedPreferences.getString("image1","");
            String image2=sharedPreferences.getString("image2","");
            Glide.with(this).load(image1).apply(new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(20))).into(imageView1);
            Glide.with(this).load(image2).apply(new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(20))).into(imageView2);
        }
        else{
            imageContainer.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.VISIBLE);
            String image1=sharedPreferences.getString("image1","");
            Glide.with(this).load(image1).apply(new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(20))).into(imageView1);

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
                        progressBar.setVisibility(View.GONE);
                        gameAdapter.notifyDataSetChanged();
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
                addRecent.putString("url1", url2);
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
            Intent webIntent = new Intent(this, WebActivity.class);
        webIntent.putExtra("ns_url", url);
            startActivity(webIntent);
    }
}
