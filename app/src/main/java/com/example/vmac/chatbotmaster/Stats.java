package com.example.vmac.chatbotmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

public class Stats extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);


        webView= findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("https://www.covid19india.org/");
        webView.getSettings().setJavaScriptEnabled(true);


    }

//    public void previous(View view)
//    {
//        Intent intent =new Intent(Stats.this, Profile_page.class);
//        startActivity(intent);
//    }
}