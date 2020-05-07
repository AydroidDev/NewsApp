package com.vaankdeals.newsapp.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.vaankdeals.newsapp.R;

import java.net.MalformedURLException;
import java.net.URL;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    @SuppressLint({"InflateParams", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        progressBar = findViewById(R.id.progressBarall);
        progressBar.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String mUrl = bundle.getString("ns_url");
        Button imageButton= findViewById(R.id.back_button_web);

        imageButton.setOnClickListener(v -> finish());
        URL url = null;
        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert url != null;
        String host = url.getHost();
        WebView mWebView = findViewById(R.id.webview_all);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
    }
}