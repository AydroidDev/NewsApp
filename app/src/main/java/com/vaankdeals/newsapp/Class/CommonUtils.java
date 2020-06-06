package com.vaankdeals.newsapp.Class;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.vaankdeals.newsapp.Model.NewsBook;
import com.vaankdeals.newsapp.Model.NewsModel;

public class CommonUtils {
    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";
    public static boolean bookmarkAll(NewsModel clickeditem, Context mContext){

        boolean isAdded=false;
        DatabaseHandler db = new DatabaseHandler(mContext);

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
        }
        else {
            dbsw.delete(TABLE_NEWS, NEWS_ID + " = ?",
                    new String[] {fieldValue});
            isAdded=true;
        }

        cursor.close();
        return isAdded;
    }
}
