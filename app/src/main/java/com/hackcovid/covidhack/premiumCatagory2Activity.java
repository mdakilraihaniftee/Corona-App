package com.hackcovid.covidhack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class premiumCatagory2Activity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_catagory2);
        webView=(WebView)findViewById(R.id.vaccineWebId);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //webView.setWebViewClient(new WebViewClient());



        webView.loadUrl("https://surokkha.gov.bd/enroll");
        //webView.loadUrl("https://newresultbd.com/covid-19-vaccine-registration");
        //webView.loadUrl("https://www.cdc.gov/coronavirus/2019-ncov/symptoms-testing/testing.html");
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