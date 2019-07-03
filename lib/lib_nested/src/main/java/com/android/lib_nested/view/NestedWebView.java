package com.android.lib_nested.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.webkit.WebView;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * Created by wentao *_*
 * on 2019-06-22
 */
public class NestedWebView extends WebView implements NestedScrollingChild2 {

    private final int mMaxFlingVelocity;
    private final int mTouchSlop;
    private NestedScrollingChildHelper mHelper;
    private VelocityTracker mVelocityTracker;
    private int mLastTouchY;
    private int mDownTouchY;
    private int[] mScrollConsumed = new int[2];

    public NestedWebView(Context context) {
        super(context);
        init();
        mMaxFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mTouchSlop = dip2px(3);
    }

    public NestedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        mMaxFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mTouchSlop = dip2px(3);
    }

    public NestedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        mMaxFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mTouchSlop = dip2px(3);
    }

    public NestedWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        mMaxFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mTouchSlop = dip2px(3);
    }

    private void init() {
        setNestedScrollingEnabled(true);
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mVelocityTracker.clear();
                mLastTouchY = (int) (event.getRawY() + 0.5f);
                mDownTouchY = mLastTouchY;
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, TYPE_TOUCH);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                final int y = (int) (event.getRawY() + 0.5f);
                int dy = mLastTouchY - y;
                if (Math.abs(mDownTouchY - y) > mTouchSlop) {
                    event.setAction(MotionEvent.ACTION_CANCEL);
                }
                mLastTouchY = y;
                if (dispatchNestedPreScroll(0, dy, mScrollConsumed, null, TYPE_TOUCH)) {
                    dy -= mScrollConsumed[1];
                }
                event.offsetLocation(0, -mScrollConsumed[1]);
                scrollBy(0, dy);
                break;
            case MotionEvent.ACTION_UP:
                mLastTouchY = 0;
                mDownTouchY = 0;
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                dispatchNestedPreFling(0, -mVelocityTracker.getYVelocity());
                stopNestedScroll(TYPE_TOUCH);
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.clear();
                mLastTouchY = 0;
                mDownTouchY = 0;
                stopNestedScroll(TYPE_TOUCH);
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getHelper().dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return getHelper().startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        getHelper().stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return getHelper().hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return getHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return getHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getHelper().startNestedScroll(axes);
    }

    private NestedScrollingChildHelper getHelper() {
        if (mHelper == null) mHelper = new NestedScrollingChildHelper(this);
        return mHelper;
    }

    public boolean isBottom() {
        final int offset = getScrollY();
        final int range = (int) (getContentHeight() * getResources().getDisplayMetrics().density - getHeight());
        if (range == 0) {
            return true;
        }
//        Log.d("csdcsdcsc","offset="+offset+"#range="+range+"#Math="+Math.abs(offset - range));

        Log.d("csdcsdcsc", canScrollVertically(1) ? "true" : "false");

        return Math.abs(offset - range) < 3 * getResources().getDisplayMetrics().density;
    }

    public int getScrollRange() {
        return computeVerticalScrollRange();
    }

    @Override
    public void scrollTo(int x, int y) {
        final int maxScrollY = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (y > maxScrollY) y = maxScrollY;
        if (y < 0) {
            y = 0;
        }
        super.scrollTo(x, y);
    }

    private static int dip2px(float dipValue) {
        final float scale = Resources.getSystem()
                .getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
