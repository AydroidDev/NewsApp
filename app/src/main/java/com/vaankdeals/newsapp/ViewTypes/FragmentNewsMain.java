package com.vaankdeals.newsapp.ViewTypes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vaankdeals.newsapp.Activity.MainActivity;
import com.vaankdeals.newsapp.Class.CommonUtils;
import com.vaankdeals.newsapp.Class.DatabaseHandler;
import com.vaankdeals.newsapp.Fragment.NewsFragment;
import com.vaankdeals.newsapp.Model.NewsBook;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class FragmentNewsMain extends Fragment {

    private String news_id;
    private Button mBookmarkButton;
    private NewsModel model;

    public FragmentNewsMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.newsitem, container, false);

        model =(NewsModel)getArguments().getSerializable("model");
        news_id =model.getmNewsId();
        int news_position =getArguments().getInt("news_position");
        String news_head =model.getmNewsHead();
        String news_image =model.getmNewsImage();
        String news_desc =model.getmNewsDesc();
        String news_url =model.getmNewslink();
        String news_source =model.getmNewsSource();
        String news_day =model.getmNewsDay();
        String news_extra = "click on title to read more on " + news_source + " / " + news_day;

        TextView mNewsHead = view.findViewById(R.id.news_head);
        TextView mNewsDesc = view.findViewById(R.id.news_desc);
        ImageView mNewsImage = view.findViewById(R.id.news_image);
        TextView mNewsExtra = view.findViewById(R.id.news_extra);
        Button mShareButton = view.findViewById(R.id.sharecard);
        Button mWhatsButton = view.findViewById(R.id.sharewhats);
        Button mDownButton = view.findViewById(R.id.downpost);
        mBookmarkButton = view.findViewById(R.id.bookmark_button);

        mNewsHead.setText(news_head);
        mNewsDesc.setText(news_desc);
        mNewsExtra.setText(news_extra);
        Glide.with(requireActivity()).load(news_image).into(mNewsImage);

        mNewsHead.setOnClickListener(v -> {

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            builder.setToolbarColor(24);
            customTabsIntent.launchUrl(requireActivity(), Uri.parse(news_url));
        });
        mDownButton.setOnClickListener(v -> {
            BitmapDrawable drawable = (BitmapDrawable) mNewsImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            shareNormal(bitmap,3);
        });

        mShareButton.setOnClickListener(v -> {
            BitmapDrawable drawable = (BitmapDrawable) mNewsImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            shareNormal(bitmap,1);
        });
        mWhatsButton.setOnClickListener(v -> {
            BitmapDrawable drawable = (BitmapDrawable) mNewsImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            shareNormal(bitmap,2);
        });
        mBookmarkButton.setOnClickListener(v -> {
            boolean isAdded= CommonUtils.bookmarkAll(model,requireContext());
            if(isAdded) {
                mBookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
                Toast.makeText(requireContext(), "Post Removed", Toast.LENGTH_SHORT).show();
            }
            else{
                mBookmarkButton.setBackgroundResource(R.drawable.bookmark_button_clicked);
            Toast.makeText(requireContext(),"Post Added",Toast.LENGTH_SHORT).show();
}
        });
        return view;
    }


    private void shareNormal( Bitmap bitmap,int type){
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        final Bitmap b = CommonUtils.drawToBitmap(requireActivity(),R.layout.news_share, metrics.widthPixels,
                metrics.heightPixels,bitmap,model);
        if(type==1) {
            File imagePathz = CommonUtils.saveBitmap(b, requireActivity(),type);
            normalShareIntent(imagePathz);
        }
        else if(type==2) {
            File imagePathz = CommonUtils.saveBitmap(b, requireActivity(),type);
            whatsappShareIntent(imagePathz);
        }
        else if(type==3){
            File imagePathz = CommonUtils.saveBitmap(b, requireActivity(),type);
            Toast.makeText(requireActivity(),"Post Downloaded in Newsapp/Downloads", Toast.LENGTH_LONG).show();
        }

    }
    private void normalShareIntent(File imagePathz){

        Uri imgUri = Uri.parse(imagePathz.getAbsolutePath());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(shareIntent);
    }
    private void whatsappShareIntent(File imagePathz){
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

}
