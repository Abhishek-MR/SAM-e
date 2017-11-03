package com.example.anuj.e_co;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class Tips extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        WebView browser = (WebView) findViewById(R.id.webview);
        browser.loadUrl("https://abhishek-mr.github.io/");
    }
}
