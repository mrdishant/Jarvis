package com.nearur.jarvis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class Social extends AppCompatActivity {

    WebView webView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    webView.loadUrl("https://www.facebook.com/");
                    return true;
                case R.id.navigation_dashboard:
                    webView.loadUrl("https://www.instagram.com/");
                    return true;
                case R.id.navigation_notifications:
                    webView.loadUrl("https://www.twitter.com/");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        getSupportActionBar().hide();
        webView=(WebView)findViewById(R.id.webview);
        WebViewClient webViewClient=new WebViewClient();
        webView.setWebViewClient(webViewClient);
        webView.loadUrl("https://www.facebook.com/");
        webView.getSettings().setJavaScriptEnabled(true);
       // webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        //webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
    }

}
