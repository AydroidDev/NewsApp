package com.vaankdeals.newsapp.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vaankdeals.newsapp.Model.NewsBook;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "newsDatabase";
    private static final String TABLE_NEWS = "newsbook";

    private static final String KEY_ID = "id";
    private static final String NEWS_HEAD = "newshead";
    private static final String NEWS_DESC = "newsdesc";
    private static final String NEWS_IMAGE = "newsimage";
    private static final String NEWS_SOURCE = "newssource";
    private static final String NEWS_DAY = "newsday";
    private static final String NEWS_LINK = "newslink";
    private static final String NEWS_ID = "newsid";
    private static final String NEWS_TYPE ="newstype";
    private static final String NEWS_VIDEO = "newsvideo";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NEWS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + NEWS_HEAD + " TEXT,"
                + NEWS_DESC + " TEXT," + NEWS_IMAGE + " TEXT,"+ NEWS_SOURCE + " TEXT," + NEWS_DAY + " TEXT," + NEWS_LINK + " TEXT" +
                "," + NEWS_ID + " TEXT," + NEWS_TYPE +" TEXT," + NEWS_VIDEO +" TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addContact(NewsBook contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NEWS_HEAD, contact.getmNewsHead()); // Contact Name
        values.put(NEWS_DESC, contact.getmNewsDesc());
        values.put(NEWS_IMAGE, contact.getmNewsImage());
        values.put(NEWS_SOURCE, contact.getmNewsSource());
        values.put(NEWS_DAY, contact.getmNewsDay());
        values.put(NEWS_LINK, contact.getmNewslink());
        values.put(NEWS_ID, contact.getmNewsId());
        values.put(NEWS_TYPE, contact.getmNewsType());
        values.put(NEWS_VIDEO, contact.getmNewsVideo());


        // Inserting Row
        db.insert(TABLE_NEWS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    NewsBook getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NEWS, new String[] { KEY_ID,
                        NEWS_HEAD, NEWS_DESC, NEWS_IMAGE, NEWS_SOURCE, NEWS_DAY, NEWS_LINK, NEWS_ID,NEWS_TYPE, NEWS_VIDEO
                }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        NewsBook contact = new NewsBook();
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<NewsBook> getAllContacts() {
        List<NewsBook> contactList = new ArrayList<NewsBook>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NEWS +
                " ORDER BY "+"id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NewsBook contact = new NewsBook();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setmNewsHead(cursor.getString(1));
               contact.setmNewsDesc(cursor.getString(2));
                contact.setmNewsImage(cursor.getString(3));
                contact.setmNewsSource(cursor.getString(4));
                contact.setmNewsDay(cursor.getString(5));
                contact.setmNewslink(cursor.getString(6));
                contact.setmNewsId(cursor.getString(7));
                contact.setmNewsType(cursor.getString(8));
                contact.setmNewsVideo(cursor.getString(9));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(NewsBook contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NEWS_HEAD, contact.getmNewsHead());
        values.put(NEWS_DESC, contact.getmNewsDesc());
        values.put(NEWS_IMAGE, contact.getmNewsImage());
        values.put(NEWS_SOURCE, contact.getmNewsSource());
        values.put(NEWS_DAY, contact.getmNewsDay());
        values.put(NEWS_LINK, contact.getmNewslink());
        values.put(NEWS_ID, contact.getmNewsId());
        values.put(NEWS_TYPE, contact.getmNewsType());
        values.put(NEWS_VIDEO, contact.getmNewsVideo());

        // updating row
        return db.update(TABLE_NEWS, values, KEY_ID + " = ?", new String[] { String.valueOf(contact.getId()) });
    }

    // Deleting single contact
    public void deleteContact(NewsBook contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NEWS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount(String fieldValue) {
        String countQuery = "SELECT  * FROM " + TABLE_NEWS + " where " + KEY_ID +  " = " + fieldValue;;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
