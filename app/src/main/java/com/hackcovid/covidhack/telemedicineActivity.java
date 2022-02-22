package com.hackcovid.covidhack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class telemedicineActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telemedicine);

        webView = (WebView) findViewById(R.id.telemedicineId);

       WebSettings webSettings=webView.getSettings();
         webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());


        webView.loadUrl("https://corona.gov.bd/telemedicine");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}