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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.vaankdeals.newsapp.Activity.ExoActivity;
import com.vaankdeals.newsapp.Activity.MainActivity;
import com.vaankdeals.newsapp.Activity.NewsActivity;
import com.vaankdeals.newsapp.Activity.VideoActivity;
import com.vaankdeals.newsapp.Adapter.PagerAdapter;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.Class.DepthPageTransformer;
import com.vaankdeals.newsapp.Class.NetworkChangeReceiver;
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
import java.util.concurrent.ExecutionException;


public class NewsFragment extends Fragment implements NetworkChangeReceiver.ConnectionChangeCallback{

    private PagerAdapter newsAdapter;
    private static final int NUMBER_OF_ADS = 2;
    private final List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private List<Object> mNewsList = new ArrayList<>() ;
    private static final String TABLE_NEWS = "newsbook";
    private static final String NEWS_ID = "newsid";
    public static ViewPager2 newsViewpager;
    private File imagePathz;
    private boolean isReceiverRegistered = false;
    private NetworkChangeReceiver networkChangeReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout retry_box;
    private ProgressBar progressBar;
    private ArrayList<Integer> posToLoad= new ArrayList<>();

    public NewsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        mRequestQueue = Volley.newRequestQueue((Objects.requireNonNull(requireActivity())));


        retry_box= rootView.findViewById(R.id.retry_box);
        progressBar = rootView.findViewById(R.id.news_progress);

        ImageButton button_up = rootView.findViewById(R.id.button_up);
        ImageButton button_refresh = rootView.findViewById(R.id.button_refresh);

        MobileAds.initialize(requireActivity(), getString(R.string.admob_app_id));
         newsViewpager = rootView.findViewById(R.id.news_swipe);
        IntentFilter intentFilter = new
                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        Objects.requireNonNull(requireActivity()).registerReceiver(networkChangeReceiver, intentFilter);
        isReceiverRegistered = true;
        networkChangeReceiver.setConnectionChangeCallback(this);

        newsAdapter = new PagerAdapter(this,mNewsList);
        //newsViewpager.setPageTransformer(new DepthPageTransformer());
         swipeRefreshLayout= rootView.findViewById(R.id.newsSwipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this::refresh_now);
        newsViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if(mNewsList.size()!=0){
               for(int i=position;i<position+3;i++) {
                   Object model = mNewsList.get(i);
                   if (model instanceof NewsModel && !posToLoad.contains(i)) {
                       NewsModel currentitem = (NewsModel) mNewsList.get(i);
                       if (!currentitem.getmNewsImage().isEmpty()) {
                           new Thread(() -> Glide.with(requireContext())
                                   .downloadOnly()
                                   .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))
                                   .load(currentitem.getmNewsImage())
                                   .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL));
                           posToLoad.add(i);
                           Log.e("added", "onPageSelected: Added" + currentitem.getmNewsId() + " " + currentitem.getmNewsHead());
                       }
                   }
               }
               }
                if(position==0){
                    button_refresh.setVisibility(View.VISIBLE);
                    button_up.setVisibility(View.GONE);
                }
                else{
                    button_refresh.setVisibility(View.GONE);
                    button_up.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);


            }
        });
        newsViewpager.setAdapter(newsAdapter);
        newsViewpager.setOffscreenPageLimit(1);
        button_up.setOnClickListener(v -> newsViewpager.setCurrentItem(0,true));
        button_refresh.setOnClickListener(v -> refresh_now());
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

        progressBar.setVisibility(View.VISIBLE);
        retry_box.setVisibility(View.GONE);
        newsAdapter.notifyDataSetChanged();
        parseJson();
        loadNativeAds();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        requireActivity().getMenuInflater().inflate(R.menu.menu_home, menu);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {

        if(isReceiverRegistered){
            Objects.requireNonNull(requireActivity()).unregisterReceiver(networkChangeReceiver);
            isReceiverRegistered = false;// set it back to false.
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((MainActivity)requireActivity()).swipeoptions();
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
            newsAdapter.notifyItemInserted(index);
            index = index + offset;
        }

    }

    private void loadNativeAds() {
        mNativeAds.clear();
        AdLoader.Builder builder = new AdLoader.Builder(Objects.requireNonNull(requireActivity()), getString(R.string.ad_unit_id_news_main));

        AdLoader adLoader = builder.forUnifiedNativeAd(
                unifiedNativeAd -> {
                    mNativeAds.add(unifiedNativeAd);
                    if (mNativeAds.size() == NUMBER_OF_ADS) {
                        insertAdsInMenuItems();
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
        newsAdapter.notifyDataSetChanged();
        String url = getString(R.string.server_website);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
            mNewsList.clear();
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
                            String news_data1 = ser.getString("news_data1");
                            String news_data2 = ser.getString("news_data2");
                            String news_data3 = ser.getString("news_data3");

                            mNewsList.add(new NewsModel(news_head,news_desc,news_image,news_source,news_day,news_id,news_link,news_type,news_video,news_data1,news_data2,news_data3));
                        }

                        NewsModel currentitem = (NewsModel) mNewsList.get(0);
                        if(!currentitem.getmNewsImage().isEmpty()) {
                            new Thread(() -> Glide.with(requireContext())
                                    .downloadOnly()
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))
                                    .load(currentitem.getmNewsImage())
                                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL));
                            posToLoad.add(0);
                        }
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    newsAdapter.notifyDataSetChanged();
                }, volleyError -> {
            swipeRefreshLayout.setRefreshing(false);
            retry_box.setVisibility(View.VISIBLE);


        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                4000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

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
        Intent detailintent = new Intent(requireActivity(), VideoActivity.class);
        detailintent.putExtra("yt_url", clickeditem.getmNewsVideo());
        startActivity(detailintent);
        Bungee.shrink(requireActivity());
    }
    private void exoPlayerActivity(int position){
        NewsModel clickeditem = (NewsModel) mNewsList.get(position);
        Intent exoIntent = new Intent(requireActivity(), ExoActivity.class);
        exoIntent.putExtra("st_url", clickeditem.getmNewsVideo());
        startActivity(exoIntent);
        Bungee.shrink(requireActivity());
    }
    public void newsDetailActivity(int position){
        NewsModel clickeditem = (NewsModel) mNewsList.get(position);
        String newsLink =clickeditem.getmNewslink();
        if (!newsLink.isEmpty()){
            Intent newsIntent = new Intent(requireActivity(), NewsActivity.class);
            newsIntent.putExtra("ns_url", clickeditem.getmNewslink());
            newsIntent.putExtra("ns_title", clickeditem.getmNewsHead());
            startActivity(newsIntent);
            Bungee.zoom(requireActivity());
        }
        else {
            Toast.makeText(requireActivity(),"Full News Not Available",Toast.LENGTH_SHORT).show();
        }


    }

    public void shareNormal(int position,Bitmap bitmap){
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        final Bitmap b = drawToBitmap(requireActivity(),R.layout.news_share, metrics.widthPixels,
                metrics.heightPixels,position,bitmap);
        saveBitmap(b);
        normalShareIntent(position);

    }
    public void shareWhats(int position,Bitmap bitmap){
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        final Bitmap b = drawToBitmap(requireActivity(),R.layout.news_share, metrics.widthPixels,
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
            Toast.makeText(requireActivity(),"Whatsapp have not been installed.",Toast.LENGTH_SHORT).show(); }

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


}


