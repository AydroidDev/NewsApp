package com.vaankdeals.newsapp.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.vaankdeals.newsapp.Activity.AllNewsActivity;
import com.vaankdeals.newsapp.Activity.GameActivity;
import com.vaankdeals.newsapp.Activity.NewsActivity;
import com.vaankdeals.newsapp.Activity.SavedActivity;
import com.vaankdeals.newsapp.Activity.WebActivity;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.R;

import androidx.fragment.app.Fragment;
import spencerstudios.com.bungeelib.Bungee;

/**
 * A simple {@link Fragment} subclass.
 */
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
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);


        Button trending = rootView.findViewById(R.id.trending);
        Button india = rootView.findViewById(R.id.india);
        Button business = rootView.findViewById(R.id.business);
        Button politics = rootView.findViewById(R.id.politics);
        Button sports = rootView.findViewById(R.id.sports);
        Button technology = rootView.findViewById(R.id.technology);
        Button entertainment = rootView.findViewById(R.id.entertainment);
        Button international = rootView.findViewById(R.id.international);
        Button automobile = rootView.findViewById(R.id.automobile);
        Button science = rootView.findViewById(R.id.science);
        Button fashion = rootView.findViewById(R.id.fashion);
        Button travel = rootView.findViewById(R.id.travel);

        trending.setOnClickListener(v -> {
            Intent gameIntent = new Intent(getContext(), GameActivity.class);
            startActivity(gameIntent);
        });
        india.setOnClickListener(this);
        business.setOnClickListener(this);
        politics.setOnClickListener(this);
        sports.setOnClickListener(this);
        technology.setOnClickListener(this);
        entertainment.setOnClickListener(this);
        international.setOnClickListener(this);
        automobile.setOnClickListener(this);
        science.setOnClickListener(this);
        fashion.setOnClickListener(this);
        travel.setOnClickListener(this);



        Button saved = (Button)rootView.findViewById(R.id.bookmarks);

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
