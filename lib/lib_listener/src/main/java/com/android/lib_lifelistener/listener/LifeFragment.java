package com.android.lib_lifelistener.listener;


import android.support.v4.app.Fragment;

/**
 * Created by wentao *_*
 * on 2019-06-20
 */
public class LifeFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();
        mFragmentLifecycler.onStartL();

    }

    @Override
    public void onResume() {
        super.onResume();
        mFragmentLifecycler.onResumeL();
    }

    @Override
    public void onPause() {
        super.onPause();
        mFragmentLifecycler.onPauseL();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFragmentLifecycler.onStopL();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragmentLifecycler.onDestroyL();
    }

    public interface FragmentLifeCycler {
        void onStartL();

        void onStopL();

        void onDestroyL();

        void onResumeL();

        void onPauseL();
    }

    public void setFragmentLifecycler(FragmentLifeCycler lifecycler) {
        mFragmentLifecycler = lifecycler;
    }

    FragmentLifeCycler mFragmentLifecycler;
}
