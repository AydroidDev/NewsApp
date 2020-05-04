package com.vaankdeals.newsapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;
import spencerstudios.com.bungeelib.Bungee;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.r0adkll.slidr.Slidr;
import com.vaankdeals.newsapp.Adapter.AllNewsAdapter;
import com.vaankdeals.newsapp.Adapter.NewsAdapter;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.Class.DepthPageTransformer;
import com.vaankdeals.newsapp.Model.AllNewsModel;
import com.vaankdeals.newsapp.Model.NewsBook;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AllNewsActivity extends AppCompatActivity implements AllNewsAdapter.videoClickListenerAll,AllNewsAdapter.newsOutListenerAll,AllNewsAdapter.whatsClickListenerAll,AllNewsAdapter.shareClickListenerAll,AllNewsAdapter.bookmarkListenerAll,AllNewsAdapter.actionbarListenerAll {
   
    private AllNewsAdapter newsAdapter;
    private RequestQueue mRequestQueue;
    private List<Object> mNewsList = new ArrayList<>() ;
    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";
    private File imagePathz;
    String news_cat;
    ViewPager2 newsViewpager;
    ProgressBar progress_all;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    RelativeLayout retry_box;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news);

        Slidr.attach(this);
        mRequestQueue = Volley.newRequestQueue(this);
        retry_box= findViewById(R.id.retry_box_all);

        swipeRefreshLayout= findViewById(R.id.activity_all_refresh);
        Bundle bundle = getIntent().getExtras();
        news_cat = bundle.getString("news_cat");
         newsViewpager = findViewById(R.id.news_swipe_all);
        newsAdapter = new AllNewsAdapter(this,mNewsList);
         toolbar = findViewById(R.id.tool_bar_all);
         setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(news_cat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().hide();
        progress_all=findViewById(R.id.progress_all);
        newsViewpager.setPageTransformer(new DepthPageTransformer());
        newsViewpager.setAdapter(newsAdapter);
        newsAdapter.setvideoClickListenerAll(AllNewsActivity.this);
        newsAdapter.setnewsOutListenerAll(AllNewsActivity.this);
        newsAdapter.setshareClickListenerAll(AllNewsActivity.this);
        newsAdapter.setwhatsClickListenerAll(AllNewsActivity.this);
        newsAdapter.setbookmarkListenerAll(AllNewsActivity.this);
        swipeRefreshLayout.setOnRefreshListener(this::refresh_now);
        newsViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(positionOffsetPixels>0){

                    toolbar.animate()
                            .setDuration(150)
                            .translationY(0);
                    getSupportActionBar().hide();
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                ((NewsAdapter)newsViewpager.getAdapter()).pauseYtVid();
            }
        });
        newsAdapter.setactionbarListenerAll(AllNewsActivity.this);
        parseJson(news_cat);
    }
    private void refresh_now(){

        progress_all.setVisibility(View.VISIBLE);
        retry_box.setVisibility(View.GONE);
        newsAdapter.notifyDataSetChanged();
        parseJson(news_cat);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_up_home:
                newsViewpager.setCurrentItem(0,true);
                return true;
            case R.id.refresh:
                refresh_now();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void parseJson(String newsCat) {
        retry_box.setVisibility(View.GONE);
        mNewsList.clear();
        String url = getString(R.string.server_website_all)+"?news_cat="+newsCat;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("server_response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ser = jsonArray.getJSONObject(i);
                            String news_head = ser.getString("news_head");
                            String news_desc = ser.getString("news_desc");
                            String news_image = ser.getString("news_image");
                            String news_source = ser.getString("news_source");
                            String news_day = ser.getString("news_day");
                            String news_id = ser.getString("news_id");
                            String news_link = ser.getString("news_link");
                            String news_type = ser.getString("news_type");
                            String news_video = ser.getString("news_video");

                            mNewsList.add(new AllNewsModel(news_head,news_desc,news_image,news_source,news_day,news_id,news_link,news_type,news_video));
                        }
                        // Just call notifyDataSetChanged here

                        progress_all.setVisibility(View.GONE);
                        newsAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, volleyError ->{
                    swipeRefreshLayout.setRefreshing(false);
                    retry_box.setVisibility(View.VISIBLE);
                });
        mRequestQueue.add(request);
    }
    public void videoActivityAll(int position) {
        AllNewsModel clickeditem = (AllNewsModel) mNewsList.get(position);
        String url = clickeditem.getmNewsVideo();
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (!url.isEmpty() && url.matches(pattern))
        {
            youtubeActivity(position);
        }
        else
        {
            // Not Valid youtube URL
            exoPlayerActivity(position);
        }
    }
    private void youtubeActivity(int position){
        AllNewsModel clickeditem = (AllNewsModel) mNewsList.get(position);
        Intent detailintent = new Intent(this, VideoActivity.class);
        detailintent.putExtra("yt_url", clickeditem.getmNewsVideo());
        startActivity(detailintent);
        Bungee.shrink(this);
    }
    private void exoPlayerActivity(int position){
        AllNewsModel clickeditem = (AllNewsModel) mNewsList.get(position);
        Intent exoIntent = new Intent(this, ExoActivity.class);
        exoIntent.putExtra("st_url", clickeditem.getmNewsVideo());
        startActivity(exoIntent);
        Bungee.shrink(this);
    }
    public void newsDetailActivityAll(int position){
        AllNewsModel clickeditem = (AllNewsModel) mNewsList.get(position);
        String newsLink =clickeditem.getmNewslink();
        if (!newsLink.isEmpty()){
            Intent newsIntent = new Intent(this, NewsActivity.class);
            newsIntent.putExtra("ns_url", clickeditem.getmNewslink());
            newsIntent.putExtra("ns_title", clickeditem.getmNewsHead());
            startActivity(newsIntent);
            Bungee.zoom(this);
        }
        else {
            Toast.makeText(this,"Full News Not Available",Toast.LENGTH_SHORT).show();
        }
    }
    public void shareNormalAll(int position,Bitmap bitmap){
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        final Bitmap b = drawToBitmap(this,R.layout.news_share, metrics.widthPixels,
                metrics.heightPixels,position,bitmap);
        saveBitmap(b);
        normalShareIntent(position);
    }
    public void shareWhatsAll(int position,Bitmap bitmap){
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        final Bitmap b = drawToBitmap(this,R.layout.news_share, metrics.widthPixels,
                metrics.heightPixels,position,bitmap);
        saveBitmap(b);
        whatsappShareIntent(position);
    }
    private void normalShareIntent(int position){

        Uri imgUri = Uri.parse(imagePathz.getAbsolutePath());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(shareIntent);
    }
    private void whatsappShareIntent(int position){
        Uri imgUri = Uri.parse(imagePathz.getAbsolutePath());
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        whatsappIntent.setType("image/jpeg");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,"Whatsapp have not been installed.",Toast.LENGTH_SHORT).show(); }

    }
    public  Bitmap drawToBitmap(Context context, int layoutResId,
                                int width, int height, int position, Bitmap bitmap)
    {
        final Bitmap bmp = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        final LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(layoutResId,null);
        ImageView newsimage= layout.findViewById(R.id.news_image_share);
        TextView newshead = layout.findViewById(R.id.news_head_share);
        TextView newsdesc= layout.findViewById(R.id.news_desc_share);
        NewsModel currentItem= (NewsModel) mNewsList.get(position);
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
    public void saveBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Random rnd = new Random();
        int nameshare = 100000 + rnd.nextInt(900000);
        Bitmap result = Bitmap.createBitmap(w, h, bitmap.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, 0, 0, null);

        Bitmap waterMark = BitmapFactory.decodeResource(this.getResources(), R.drawable.waterbottom);
        canvas.drawBitmap(waterMark, 0, 1100, null);
        String folderpath = Environment.getExternalStorageDirectory() + "/NewsApp";
        File folder = new File(folderpath);
        if(!folder.exists()){
            File wallpaperDirectory = new File(folderpath);
            wallpaperDirectory.mkdirs();
        }
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
    }
    public void bookmarkAllAll(int position){
        AllNewsModel clickeditem = (AllNewsModel) mNewsList.get(position);
        if(clickeditem.getmNewsType().equals("1")){
            final Button buttonNews = findViewById(R.id.bookmark_button);
            buttonNews.setBackgroundResource(R.drawable.bookmark_button_clicked);
        }
        else if(clickeditem.getmNewsType().equals("5")) {
            final Button buttonVideo = findViewById(R.id.video_bookmark_button);
            buttonVideo.setBackgroundResource(R.drawable.bookmark_button_clicked);

        }
        DatabaseHandler db = new DatabaseHandler(this);

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
            Toast.makeText(this,"News Saved", Toast.LENGTH_SHORT).show();
        }
        else {
            if(clickeditem.getmNewsType().equals("1")){
                final Button buttonNews = findViewById(R.id.bookmark_button);
                buttonNews.setBackgroundResource(R.drawable.bookmark_button);
            }
            else if(clickeditem.getmNewsType().equals("5")) {
                final Button buttonVideo = findViewById(R.id.video_bookmark_button);
                buttonVideo.setBackgroundResource(R.drawable.bookmark_button);

            }
            dbsw.delete(TABLE_NEWS, NEWS_ID + " = ?",
                    new String[] { String.valueOf(fieldValue) });
            Toast.makeText(this,"News Removed", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }
    public void actionBarViewAll(){

        ActionBar actionBar = Objects.requireNonNull(this).getSupportActionBar();

        assert actionBar != null;
        if (actionBar.isShowing()) {

            actionBar.hide();
        } else {

            actionBar.show();
        }
    }
}
