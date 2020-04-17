package com.vaankdeals.newsapp.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;
import spencerstudios.com.bungeelib.Bungee;

import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.vaankdeals.newsapp.Activity.ExoActivity;
import com.vaankdeals.newsapp.Activity.MainActivity;
import com.vaankdeals.newsapp.Activity.NewsActivity;
import com.vaankdeals.newsapp.Activity.VideoActivity;
import com.vaankdeals.newsapp.Adapter.NewsAdapter;
import com.vaankdeals.newsapp.BuildConfig;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.Class.NetworkChangeReceiver;
import com.vaankdeals.newsapp.Class.ZoomOutPageTransformer;
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


public class NewsFragment extends Fragment implements NetworkChangeReceiver.ConnectionChangeCallback,NewsAdapter.videoClickListener,NewsAdapter.newsOutListener,NewsAdapter.whatsClickListener,NewsAdapter.shareClickListener,NewsAdapter.adClickListener,NewsAdapter.bookmarkListener,NewsAdapter.actionbarListener{

    private NewsAdapter newsAdapter;
    private static final int NUMBER_OF_ADS = 2;
    private final List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private List<Object> mNewsList = new ArrayList<>() ;
    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";
    private ViewPager2 newsViewpager;
    private File imagePathz;
    private boolean isReceiverRegistered = false;
    private NetworkChangeReceiver networkChangeReceiver;
    private Toolbar toolbar;
    private TextView mTitle;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout retry_box;
    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        mRequestQueue = Volley.newRequestQueue((getActivity()));

        toolbar = (Toolbar) rootView.findViewById(R.id.tool_barz);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        retry_box= rootView.findViewById(R.id.retry_box);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mTitle.setText("My Deals");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        MobileAds.initialize(getActivity(), getString(R.string.admob_app_id));
         newsViewpager = rootView.findViewById(R.id.news_swipe);
        IntentFilter intentFilter = new
                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        Objects.requireNonNull(getActivity()).registerReceiver(networkChangeReceiver, intentFilter);
        isReceiverRegistered = true;
        networkChangeReceiver.setConnectionChangeCallback(this);
        newsAdapter = new NewsAdapter(getContext(),mNewsList);
        newsAdapter.setvideoClickListener(NewsFragment.this);
        newsAdapter.setnewsOutListener(NewsFragment.this);
        newsAdapter.setshareClickListener(NewsFragment.this);
        newsAdapter.setwhatsClickListener(NewsFragment.this);
        newsAdapter.setadClickListener(NewsFragment.this);
        newsAdapter.setbookmarkListener(NewsFragment.this);
        newsAdapter.setactionbarListener(NewsFragment.this);
         swipeRefreshLayout= (SwipeRefreshLayout) rootView.findViewById(R.id.newsSwipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this::refresh_now);
        newsViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if(positionOffsetPixels>0){

                    toolbar.animate()
                            .setDuration(150)
                            .translationY(0);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
                }

            }
        });

        int TIME = 4000;
        new Handler().postDelayed(() -> {
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide(); //call function!
        }, TIME);
        newsViewpager.setPageTransformer(new DepthPageTransformer());
        newsViewpager.setAdapter(newsAdapter);
        parseJson();
        loadNativeAds();
        return rootView;
    }
    @Override
    public void onConnectionChange(boolean isConnected) {
        if(isConnected){
            refresh_now();
        }
    }
    private void refresh_now(){

        retry_box.setVisibility(View.GONE);
        newsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(true);
        parseJson();
        loadNativeAds();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_home, menu);

    }
    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {

        if(isReceiverRegistered){
            Objects.requireNonNull(getActivity()).unregisterReceiver(networkChangeReceiver);
            isReceiverRegistered = false;// set it back to false.
        }
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((MainActivity)getActivity()).swipeoptions();
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

    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 0) {
            return;
        }
        if (mNewsList.size() <= 0) {
            return;
        }
        int offset = (mNewsList.size() / mNativeAds.size()) + 1;
        int index = 1;
        for (UnifiedNativeAd ad : mNativeAds) {
            mNewsList.add(index, ad);
            index = index + offset;
        }

    }

    private void loadNativeAds() {
        mNativeAds.clear();
        AdLoader.Builder builder = new AdLoader.Builder(Objects.requireNonNull(getActivity()), getString(R.string.ad_unit_id_news_main));

        AdLoader adLoader = builder.forUnifiedNativeAd(
                unifiedNativeAd -> {
                    mNativeAds.add(unifiedNativeAd);
                    if (mNativeAds.size() == NUMBER_OF_ADS) {
                        NewsFragment.this.insertAdsInMenuItems();
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (mNativeAds.size() == NUMBER_OF_ADS) {
                            insertAdsInMenuItems();
                        }
                    }
                }).build();
        // Load the Native ads.

        adLoader.loadAds(new AdRequest.Builder()
                .build(), NUMBER_OF_ADS);
    }

    private void parseJson() {
        retry_box.setVisibility(View.GONE);
        mNewsList.clear();
        String url = getString(R.string.server_website);

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

                            mNewsList.add(new NewsModel(news_head,news_desc,news_image,news_source,news_day,news_id,news_link,news_type,news_video));
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        newsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, volleyError -> {
            swipeRefreshLayout.setRefreshing(false);
            retry_box.setVisibility(View.VISIBLE);


        });
        mRequestQueue.add(request);
    }
    @Override
    public void videoActivity(int position) {
        NewsModel clickeditem = (NewsModel) mNewsList.get(position);
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
        NewsModel clickeditem = (NewsModel) mNewsList.get(position);
        Intent detailintent = new Intent(getContext(), VideoActivity.class);
        detailintent.putExtra("yt_url", clickeditem.getmNewsVideo());
        startActivity(detailintent);
        Bungee.shrink(getContext());
    }
    private void exoPlayerActivity(int position){
        NewsModel clickeditem = (NewsModel) mNewsList.get(position);
        Intent exoIntent = new Intent(getContext(), ExoActivity.class);
        exoIntent.putExtra("st_url", clickeditem.getmNewsVideo());
        startActivity(exoIntent);
        Bungee.shrink(getContext());
    }
    public void newsDetailActivity(int position){
        NewsModel clickeditem = (NewsModel) mNewsList.get(position);
        String newsLink =clickeditem.getmNewslink();
        if (!newsLink.isEmpty()){
            Intent newsIntent = new Intent(getContext(), NewsActivity.class);
            newsIntent.putExtra("ns_url", clickeditem.getmNewslink());
            newsIntent.putExtra("ns_title", clickeditem.getmNewsHead());
            startActivity(newsIntent);
            Bungee.zoom(getContext());
        }
        else {
            Toast.makeText(getContext(),"Full News Not Available",Toast.LENGTH_SHORT).show();
        }


    }
    public class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.95f;
        private static final float MIN_FADE = 0.8f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            if (position < -1) { // [-Infinity,-1)

            } else if (position <= 0) { // [-1,0]
                ViewCompat.setTranslationZ(view, position);

                view.setTranslationY(1f);
                view.setScaleX(1f);
                view.setScaleY(1f);
            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                ViewCompat.setTranslationZ(view, pageHeight *-position);

                // Counteract the default slide transition
                view.setTranslationY(pageHeight *-position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else if(position==0){
                ViewCompat.setTranslationZ(view, 0);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            }
            else{
            }
        }
    }
    public void shareNormal(int position,Bitmap bitmap){
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        final Bitmap b = drawToBitmap(getContext(),R.layout.news_share, metrics.widthPixels,
                metrics.heightPixels,position,bitmap);
        saveBitmap(b);
        normalShareIntent(position);
        Toast.makeText(getContext(),"Share Normal",Toast.LENGTH_SHORT).show();
    }
    public void shareWhats(int position,Bitmap bitmap){
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        final Bitmap b = drawToBitmap(getContext(),R.layout.news_share, metrics.widthPixels,
                metrics.heightPixels,position,bitmap);
        saveBitmap(b);
        whatsappShareIntent(position);
        Toast.makeText(getContext(),"Share Whatsapp",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(),"Whatsapp have not been installed.",Toast.LENGTH_SHORT).show(); }

    }
    public  Bitmap drawToBitmap(Context context, int layoutResId,
                                       int width, int height,int position,Bitmap bitmap)
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

    public void customAdLink(int position){

        NewsModel clickeditem = (NewsModel) mNewsList.get(position);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickeditem.getmNewslink()));
        startActivity(browserIntent);
    }
    public void bookmarkAll(int position){
        NewsModel clickeditem = (NewsModel) mNewsList.get(position);
        if(clickeditem.getmNewsType().equals("1")){
            final Button buttonNews = (Button)getActivity().findViewById(R.id.bookmark_button);
            buttonNews.setBackgroundResource(R.drawable.bookmark_button_clicked);
        }
else if(clickeditem.getmNewsType().equals("5")) {
            final Button buttonVideo = (Button) getActivity().findViewById(R.id.video_bookmark_button);
            buttonVideo.setBackgroundResource(R.drawable.bookmark_button_clicked);

        }
        DatabaseHandler db = new DatabaseHandler(getContext());

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
            Toast.makeText(getContext(),"News Saved", Toast.LENGTH_SHORT).show();
        }
        else {
            if(clickeditem.getmNewsType().equals("1")){
                final Button buttonNews = (Button)getActivity().findViewById(R.id.bookmark_button);
                buttonNews.setBackgroundResource(R.drawable.bookmark_button);
            }
            else if(clickeditem.getmNewsType().equals("5")) {
                final Button buttonVideo = (Button) getActivity().findViewById(R.id.video_bookmark_button);
                buttonVideo.setBackgroundResource(R.drawable.bookmark_button);

            }
            dbsw.delete(TABLE_NEWS, NEWS_ID + " = ?",
                    new String[] { String.valueOf(fieldValue) });
            Toast.makeText(getContext(),"News Removed", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }
    public void actionBarView(){

        ActionBar actionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();

        assert actionBar != null;
        if (actionBar.isShowing()) {

            actionBar.hide();
        } else {

            actionBar.show();
        }
    }
}


