package com.example.shah.ilovezappos;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebview ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        String action = intent.getAction();
        Log.d("demo", action);
        Uri data = intent.getData();

        mWebview  = (WebView) findViewById(R.id.webview);

        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new WebViewClient());

        mWebview.loadUrl(String.valueOf(data));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }
        Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        return super.onKeyDown(keyCode, event);
    }
}
