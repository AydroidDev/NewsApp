package com.vaankdeals.newsapp.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.astritveliu.boom.Boom;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.vaankdeals.newsapp.Activity.AllNewsActivity;
import com.vaankdeals.newsapp.Activity.GameActivity;
import com.vaankdeals.newsapp.Activity.SavedActivity;
import com.vaankdeals.newsapp.Activity.VideoMenuActivity;
import com.vaankdeals.newsapp.Adapter.MenuTagAdapter;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MenuFragment extends Fragment implements View.OnClickListener,MenuTagAdapter.tagClickListener{

    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";
    private ArrayList<String> TagList = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private MenuTagAdapter menuTagAdapter;
    private ProgressBar tagProgress;
    private RecyclerView tagRecyclerView;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        mRequestQueue = Volley.newRequestQueue(requireActivity());
        tagRecyclerView = rootView.findViewById(R.id.recycler_view_menu);

        tagProgress=rootView.findViewById(R.id.tag_progress);
        menuTagAdapter = new MenuTagAdapter(requireActivity(),TagList);
        menuTagAdapter.setmTagClickListener(this);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        tagRecyclerView.setLayoutManager(layoutManager);
        tagRecyclerView.setAdapter(menuTagAdapter);
        parseTag();

        ImageView game_banner=rootView.findViewById(R.id.banner_game);
        CardView news_cat = rootView.findViewById(R.id.news_cat);
        CardView games_cat = rootView.findViewById(R.id.games_cat);
        CardView deals_cat = rootView.findViewById(R.id.deals_cat);
        CardView videos_cat = rootView.findViewById(R.id.videos_cat);


        new Boom(game_banner);
        game_banner.setOnClickListener(v -> {
            Intent gameIntent = new Intent(getContext(), GameActivity.class);
            startActivity(gameIntent);
        });

        games_cat.setOnClickListener(v -> {
            Intent gameIntent = new Intent(getContext(), GameActivity.class);
            startActivity(gameIntent);
        });

        news_cat.setOnClickListener(this);
        deals_cat.setOnClickListener(this);


        videos_cat.setOnClickListener(v -> {
            Intent videoIntent= new Intent(getContext(), VideoMenuActivity.class);
            startActivity(videoIntent);
        });
        CardView saved_cat = rootView.findViewById(R.id.saved_cat);

        saved_cat.setOnClickListener(v -> {
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

    private void parseTag() {
        String url = getString(R.string.server_website_menu_tag);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("server_response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ser = jsonArray.getJSONObject(i);
                            String tag_text = ser.getString("tag_text");
                            TagList.add(tag_text);
                        }
                        // Just call notifyDataSetChanged here

                        menuTagAdapter.notifyDataSetChanged();
                        tagRecyclerView.setVisibility(View.VISIBLE);
                        tagProgress.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, volleyError ->{
        });
        mRequestQueue.add(request);
    }

    @Override
    public void onClick(View v) {

                Intent allIntent = new Intent(requireContext(), AllNewsActivity.class);
                allIntent.putExtra("news_cat",v.getTag().toString());
                startActivity(allIntent);
    }


    @Override
    public void tagClick(int position) {

        Intent allIntent = new Intent(requireContext(), AllNewsActivity.class);
        allIntent.putExtra("news_cat",TagList.get(position));
        startActivity(allIntent);
    }
}
