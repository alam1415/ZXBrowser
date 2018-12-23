package com.app.zxbrowser.zxbrowser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ProgressBar mybar;
    WebView myweb;
    ImageView myimg;
    LinearLayout myLinear;
    SwipeRefreshLayout myLayout;
    String myCurrentUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLayout = findViewById(R.id.myswipeLayout);
        myLinear = findViewById(R.id.myLinearLayout);
        mybar = findViewById(R.id.myProgressBar);
        myweb = findViewById(R.id.myWebView);
        myimg = findViewById(R.id.myImageView);


        mybar.setMax(100);
        myweb.loadUrl("https://www.google.com");
        myweb.getSettings().setJavaScriptEnabled(true);
        myweb.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                myLinear.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                myLinear.setVisibility(View.GONE);
                myLayout.setRefreshing(false);
                super.onPageFinished(view, url);
                myCurrentUrl = url;
            }
        });
        myweb.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mybar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                myimg.setImageBitmap(icon);
            }
        });
        myLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myweb.reload();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.super_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuBack:
                onBackPressed();
                break;

            case R.id.menuForward:
                onForwardPressed();
                break;

            case R.id.menuRefresh:
                myweb.reload();
                break;

            case R.id.menuShare:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,myCurrentUrl);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Copied URL");
                startActivity(Intent.createChooser(shareIntent, "bagikan URL dengan teman"));
        }
        return super.onOptionsItemSelected(item);

    }

    private void onForwardPressed(){
        if (myweb.canGoForward()){
            myweb.goForward();
        }
        else{
            Toast.makeText(this, "Cant go furthur", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(){
        if (myweb.canGoBack()){
            myweb.goBack();
        }
        else {
            finish();
        }
    }

}
