package com.android.lib_nested;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;

import com.android.lib_nested.view.NestedVerticalContainerLayout;

/**
 * Created by wentao *_*
 * on 2019-06-22
 */
public class NestedActivity extends AppCompatActivity implements View.OnClickListener {
    public static void start(Context context) {
        context.startActivity(new Intent(context, NestedActivity.class));
    }

    RecyclerView recycleview_one;
    WebView nestedWebView;
    NestedVerticalContainerLayout containerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nested_activity_main);
        recycleview_one = findViewById(R.id.recycleview_one);
        nestedWebView = findViewById(R.id.webview);
        containerLayout = findViewById(R.id.layout);

        nestedWebView.loadUrl("https://36kr.com/p/5221382");
        recycleview_one.setAdapter(new RyOneAdapter());
        recycleview_one.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.tv_scroll).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_scroll) {
            containerLayout.scrollToRy();
            containerLayout.scrollRyPositiion();
        }
    }
}
