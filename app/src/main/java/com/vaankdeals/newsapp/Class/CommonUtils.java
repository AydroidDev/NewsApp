package com.vaankdeals.newsapp.Class;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.vaankdeals.newsapp.Model.NewsBook;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class CommonUtils {
    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";
    private static File imagePathz;
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
                    clickeditem.getmNewsType(),clickeditem.getmNewsVideo(),clickeditem.getmNewsData1(),clickeditem.getmNewsData2(),clickeditem.getmNewsData3()));
        }
        else {
            dbsw.delete(TABLE_NEWS, NEWS_ID + " = ?",
                    new String[] {fieldValue});
            isAdded=true;
        }

        cursor.close();
        return isAdded;
    }


    public static Bitmap drawToBitmap(Context context, int layoutResId,
                                int width, int height,Bitmap bitmap,NewsModel currentItem)
    {
        final Bitmap bmp = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        final LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(layoutResId,null);
        ImageView newsimage= layout.findViewById(R.id.news_image_share);
        TextView newshead = layout.findViewById(R.id.news_head_share);
        TextView newsdesc= layout.findViewById(R.id.news_desc_share);
        newsimage.setImageBitmap(bitmap);
        newshead.setText(currentItem.getmNewsHead());
        newsdesc.setText(currentItem.getmNewsDesc());
        layout.measure(
                View.MeasureSpec.makeMeasureSpec(canvas.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(canvas.getHeight(), View.MeasureSpec.EXACTLY));
        layout.layout(0,0,layout.getMeasuredWidth(),layout.getMeasuredHeight());
        layout.draw(canvas);
        return bmp;
    }
    public static File saveBitmap(Bitmap bitmap,Context mContext,int type) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Random rnd = new Random();
        int nameshare = 100000 + rnd.nextInt(900000);
        Bitmap result = Bitmap.createBitmap(w, h, bitmap.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, 0, 0, null);

        Bitmap waterMark = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.waterbottom);
        canvas.drawBitmap(waterMark, 0, 1100, null);
        String folderpath = Environment.getExternalStorageDirectory() + "/NewsApp";
        File folder = new File(folderpath);
        if(!folder.exists()){
            File wallpaperDirectory = new File(folderpath);
            wallpaperDirectory.mkdirs();
        }
        if(type==3)
            imagePathz = new File(Environment.getExternalStorageDirectory() +"/NewsApp/Downloads"+nameshare+".png");
        else
            imagePathz = new File(Environment.getExternalStorageDirectory() +"/NewsApp/SharedNews"+nameshare+".png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePathz);
            result.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
        return imagePathz;
    }
}
