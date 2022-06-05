package com.example.ezee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;

public class SpdfActivity extends AppCompatActivity {

    String fileurl,title;
    WebView spdfWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spdf);

        Intent intent = getIntent();
        fileurl = intent.getStringExtra("fileurl");
        title = intent.getStringExtra("title");

        System.out.println("spdfActivity ---->"+ fileurl);
        System.out.println("spdfActivity ---->"+ title);

        spdfWebView = findViewById(R.id.spdfWebView);

        ProgressDialog pd = new ProgressDialog(SpdfActivity.this);

        pd.setMessage("Opening...!!");

        spdfWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });
        spdfWebView.getSettings().setJavaScriptEnabled(true);
        String url="";

        try{
            url = URLEncoder.encode(fileurl,"UTF-8");
        }catch (Exception e){

        }

        spdfWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+ url);


    }
}