package com.example.ygd.imooc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ygd.imooc.R;

public class MyCSDNActivity extends AppCompatActivity {
    private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_csdn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCSDNActivity.this.finish();
            }
        });
        wv= (WebView) findViewById(R.id.wv);
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        WebSettings settings=wv.getSettings();  //获取WebSetting对象
        settings.setJavaScriptEnabled(true);    //设置是否支持JavaScript
        settings.setSupportZoom(true);  //设置是否支持缩放
        settings.setDisplayZoomControls(true);  //设置内置缩放控制
        wv.setWebViewClient(new WebViewClient());   //使用WebViewClient作为客户端
        wv.loadUrl(url); //打开一个页面
    }
}
