package com.android.life01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.lib_lifelistener.LifeListenerActivity;
import com.android.lib_lottie.LottieActivity;
import com.android.lib_nested.NestedActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_lottie:
                LottieActivity.start(this);
                break;
            case R.id.tv_life:
                LifeListenerActivity.start(this);
                break;
            case R.id.tv_nested:
                NestedActivity.start(this);
                break;
        }
    }

    private void findView() {
        findViewByIdListener(R.id.tv_lottie);
        findViewByIdListener(R.id.tv_nested);
        findViewByIdListener(R.id.tv_life);
    }

    private void findViewByIdListener(int rId) {
        findViewById(rId).setOnClickListener(this);
    }
}
