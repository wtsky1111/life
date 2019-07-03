package com.android.lib_lifelistener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.lib_lifelistener.listener.LifeFragment;

/**
 * Created by wentao *_*
 * on 2019-06-20
 */
public class LifeListenerActivity extends AppCompatActivity implements LifeFragment.FragmentLifeCycler {
    TextView tv;

    public static void start(Context context) {
        context.startActivity(new Intent(context, LifeListenerActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listener_activity_main);
        tv = findViewById(R.id.tv);
        LifeFragment lifeFragment = new LifeFragment();
        lifeFragment.setFragmentLifecycler(this);
        getSupportFragmentManager().beginTransaction()
                .add(lifeFragment,"life")
                .commitAllowingStateLoss();
    }

    @Override
    public void onStartL() {
        appendStr("onStart");
    }

    @Override
    public void onStopL() {
        appendStr("onStop");
    }

    @Override
    public void onDestroyL() {
        appendStr("onDestroy");
    }

    @Override
    public void onResumeL() {
        appendStr("onResume");
    }

    @Override
    public void onPauseL() {
        appendStr("onPause");
    }

    StringBuilder stringBuilder = new StringBuilder("监听生命周期").append("\n");

    private void appendStr(String str) {
        stringBuilder.append(str).append("\n");
        tv.setText(stringBuilder);
    }
}
