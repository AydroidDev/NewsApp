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


import com.vaankdeals.newsapp.Activity.SavedActivity;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.R;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

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
        Button saved = (Button)rootView.findViewById(R.id.bookmarks);



        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }

        });

        return rootView;
    }

}
