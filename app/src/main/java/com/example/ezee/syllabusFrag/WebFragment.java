package com.example.ezee.syllabusFrag;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ezee.R;

import java.net.URLEncoder;

import javax.security.auth.callback.Callback;

public class WebFragment extends Fragment {
    WebView pdfView;
    String fileurl,title;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public WebFragment() {
    }
    public WebFragment(String fileurl, String title) {
        this.fileurl = fileurl;
        this.title = title;
    }

    public static WebFragment newInstance(String param1, String param2) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);

        ProgressDialog pd = new ProgressDialog(getContext());

        pd.setMessage("Opening...!!");

        pdfView = view.findViewById(R.id.pdfView);


        Log.i("Scam",fileurl);
        WebView webview = (WebView) view.findViewById(R.id.pdfView);
        webview.getSettings().setJavaScriptEnabled(true);
//        String pdf = fileurl;
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        String url="";
        try{
            url = URLEncoder.encode(fileurl,"UTF-8");
        }catch (Exception e){

        }
        webview.loadUrl(
                "https://docs.google.com/gview?embedded=true&url="+ url);


        return  view;


//        pdfView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                pd.show();
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                pd.dismiss();
//            }
//        });

//        String url="";
//
//        try{
//            url = URLEncoder.encode(fileurl,"UTF-8");
//        }catch (Exception e){
//
//        }
//
//        pdfView.loadUrl("https://docs.google.com/gview?embedded=true&url="+ url);
//
//        return view;
    }
}