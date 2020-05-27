package com.vaankdeals.newsapp.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.vaankdeals.newsapp.Activity.AllNewsActivity;
import com.vaankdeals.newsapp.Activity.GameActivity;
import com.vaankdeals.newsapp.Activity.SavedActivity;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.R;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import libs.mjn.scaletouchlistener.ScaleTouchListener;

public class MenuFragment extends Fragment implements View.OnClickListener{

    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.menu_new, container, false);

        ImageView game_banner=rootView.findViewById(R.id.banner_game);
        ImageButton trending = rootView.findViewById(R.id.trending_cat);
        ImageButton games = rootView.findViewById(R.id.games_cat);
        CardView business = rootView.findViewById(R.id.business);
        CardView politics = rootView.findViewById(R.id.politics);
        CardView sports = rootView.findViewById(R.id.sports);
        CardView technology = rootView.findViewById(R.id.tech);
        CardView entertainment = rootView.findViewById(R.id.entertainment);
        CardView international = rootView.findViewById(R.id.international);


        game_banner.setOnTouchListener(new ScaleTouchListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(getContext(), GameActivity.class);
                startActivity(gameIntent);
            }

        });
        games.setOnClickListener(v -> {
            Intent gameIntent = new Intent(getContext(), GameActivity.class);
            startActivity(gameIntent);
        });

        business.setOnClickListener(this);
        politics.setOnClickListener(this);
        sports.setOnClickListener(this);
        technology.setOnClickListener(this);
        entertainment.setOnClickListener(this);
        international.setOnClickListener(this);

        ImageButton saved = rootView.findViewById(R.id.saved_cat);

        saved.setOnClickListener(v -> {
            DatabaseHandler db = new DatabaseHandler(getContext());
            String countQuery = "SELECT  * FROM " + TABLE_NEWS;
            SQLiteDatabase dbsVideo = db.getReadableDatabase();
            Cursor cursorVideo = dbsVideo.rawQuery(countQuery, null);
            int recountVideo = cursorVideo.getCount();
            cursorVideo.close();
            if(recountVideo <= 0){
                Toast.makeText(getContext(),"Bookmarks Is Empty",Toast.LENGTH_SHORT).show();

            }
            else {
                Intent detailintent = new Intent(getContext(), SavedActivity.class);
                startActivity(detailintent);
            }

        });

        return rootView;
    }



    @Override
    public void onClick(View v) {

                Intent allIntent = new Intent(getContext(), AllNewsActivity.class);
                allIntent.putExtra("news_cat",v.getTag().toString());
                startActivity(allIntent);
    }



}
