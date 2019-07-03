package com.android.lib_lottie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.airbnb.lottie.LottieAnimationView;

/**
 * Created by wentao *_*
 * on 2019-06-24
 */
public class LottieActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, LottieActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottie_activity_main);

        LottieAnimationView mLoadingView = findViewById(R.id.loading);
        mLoadingView.playAnimation();
    }
}
