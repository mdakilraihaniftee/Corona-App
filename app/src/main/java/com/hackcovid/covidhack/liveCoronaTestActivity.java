package com.hackcovid.covidhack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class liveCoronaTestActivity extends AppCompatActivity {
    private  WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_corona_test);
        webView=(WebView)findViewById(R.id.liveCoronaTestId);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //webView.setWebViewClient(new WebViewClient());


        webView.loadUrl("https://livecoronatest.com/chat.php?city=Unnamed%20Road&lat=23.820264&lng=90.417367&addr_dist=Unknown&addr_div=Unknown");
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
        {
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
}
}