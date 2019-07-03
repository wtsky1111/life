package com.android.lib_nested.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.android.lib_nested.utils.Util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.v4.view.ViewCompat.TYPE_NON_TOUCH;

/**
 * Created by wentao *_*
 * on 2019-06-22
 */
public class NestedVerticalContainerLayout extends ViewGroup implements NestedScrollingParent2 {

    @IntDef({SCROLL_TO_NEXT, SCROLL_TO_LAST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    private static final int SCROLL_TO_LAST = -1;
    private static final int SCROLL_TO_NEXT = 1;

    NestedScrollingParentHelper mHelper;
    NestedScroller mScroller;
    ScrollXY mScrollXY = new ScrollXY();

    public NestedVerticalContainerLayout(Context context) {
        this(context, null);
    }

    public NestedVerticalContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedVerticalContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NestedVerticalContainerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        //int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = measureWidth;
        } else {
            width = getContext().getResources().getDisplayMetrics().widthPixels;
        }
        int count = getChildCount();
        int maxChildHeight = measureHeight - getPaddingTop() - getPaddingBottom();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams params = child.getLayoutParams();
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), params.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), params.height);

            //初次计算子控件高度
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            Log.d("cscsdc", child.getClass() + ":" + child.getMeasuredHeight());
            //设置子控件最大高度
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    child.getMeasuredHeight() > maxChildHeight ? maxChildHeight
                            : child.getMeasuredHeight(), MeasureSpec.EXACTLY);
            measureChild(child, childWidthMeasureSpec, childHeightMeasureSpec);
        }
        setMeasuredDimension(width, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childBottom = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (!child.isShown()) {
                child.layout(0, 0, 0, 0);
                continue;
            }

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            child.layout(0, childBottom, childWidth, childBottom + childHeight);
            childBottom += childHeight;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScroller != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            mScroller.forceFinished(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 2) {
            // TODO: 2019-07-02 后期想办法支持多控件滑动，当前多控件阻力在于：对于一个target无法得知到底需要判断底部无法滑动还是顶部无法滑动 
            throw new RuntimeException("only support 2 child");
        }
    }

    @Override
    protected int computeVerticalScrollRange() {
        int range = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            range += getChildAt(i).getHeight();
        }
        return range;
    }

    @Override
    public void scrollTo(int x, int y) {
        // TODO: 2019-06-25 后期添加横向滑动支持 
        if (x != 0) x = 0;
        if (y <= 0) y = 0;
        final int maxScrollY = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (y > maxScrollY) y = maxScrollY;
        super.scrollTo(x, y);
    }

    private void scrollToTop() {
        scrollTo(0, 0);
    }

    private void scrollToBottom() {
        scrollTo(0, computeVerticalScrollRange() - computeVerticalScrollExtent());
    }

    private NestedScrollingParentHelper getHelper() {
        if (mHelper == null) mHelper = new NestedScrollingParentHelper(this);
        return mHelper;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        getHelper().onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        getHelper().onStopNestedScroll(target, type);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return true;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        if (mScroller != null) {
            mScroller.abortAnimation();
        }
        mScrollXY.reset();
        mScroller = new NestedScroller(getContext());
        mScroller.mSourceView = target;
        mScroller.fling(0, 0, (int) velocityX, (int) velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        mScroller.computeScrollOffset();
        invalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller != null && mScroller.computeScrollOffset()) {
            final int dx = mScroller.getCurrX() - mScrollXY.mLastX;
            final int dy = mScroller.getCurrY() - mScrollXY.mLastY;
            final int[] consumed = new int[]{dx, dy};
            dispatchNestedFlingToChild(dy > 0 ? SCROLL_TO_NEXT : SCROLL_TO_LAST, consumed);
            mScrollXY.mLastY = mScroller.getCurrY();
            mScrollXY.mLastX = mScroller.getCurrX();

            if (mScroller.isFinished()) {
                stopNestedScroll();
                mScrollXY.reset();
                mScroller.mSourceView = null;
            }

            invalidate();
        } else {
            stopNestedScroll();
            mScrollXY.reset();
            if (mScroller != null)
                mScroller.mSourceView = null;
        }
    }

    private void dispatchNestedFlingToChild(@OrientationMode int orientation, int[] consumed) {
        View view = disShouldFlingView(orientation);
        if (view != null) {
            final int[] childConsumed = new int[2];
            onNestedPreScroll(view, consumed[0], consumed[1], childConsumed, TYPE_NON_TOUCH);
            consumed[0] -= childConsumed[0];
            consumed[1] -= childConsumed[1];
            view.scrollBy(consumed[0], consumed[1]);
        } else {
            scrollBy(consumed[0], consumed[1]);
        }
    }

    private View disShouldFlingView(@OrientationMode int orientation) {
        if (mScroller == null || mScroller.mSourceView == null) return null;
        if (orientation > 0) {
            if (Util.isAlreadyBottom(mScroller.mSourceView)) {
                do {
                    mScroller.mSourceView = getChildAt(indexOfChild(mScroller.mSourceView) + 1);
                    if (mScroller.mSourceView == null) break;
                } while (!mScroller.mSourceView.isShown()
                        || mScroller.mSourceView.getHeight() < getHeight());
            }
        } else if (orientation < 0) {
            if (Util.isAlreadyTop(mScroller.mSourceView)) {
                do {
                    mScroller.mSourceView = getChildAt(indexOfChild(mScroller.mSourceView) - 1);
                    if (mScroller.mSourceView == null) break;
                } while (!mScroller.mSourceView.isShown() || mScroller.mSourceView.getHeight() < getHeight());
            }
        }
        return mScroller.mSourceView;
    }

    /**
     * 在子View滑动结束后，触发该函数，通知父view剩余滑动参数
     *
     * @param target
     * @param dxConsumed
     * @param dyConsumed
     * @param dxUnconsumed
     * @param dyUnconsumed
     * @param type
     */
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        scrollBy(dxUnconsumed, dyUnconsumed);
    }

    /**
     * 在子View开始滑动的时候，触发该函数，优先父View处理滑动事件
     *
     * @param target
     * @param dx
     * @param dy       >0手指向上滑动(加载new),<0手指向下滑动(加载old)
     * @param consumed
     * @param type
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @Nullable int[] consumed, int type) {
        if (dy == 0 || target == this) return;
        final int index = indexOfChild(target);
        if (Util.isAlreadyTop(this) && dy < 0 || Util.isAlreadyBottom(this) && dy > 0) {//父控件已经处于不可滑动状态
            return;
        }
        if (index == 0) {
            if (Util.isAlreadyBottom(target)) {
                scrollBy(0, dy);
                if (consumed != null && consumed.length == 2) consumed[1] = dy;
            }
        } else if (index == 1) {
            View view_index_0 = getChildAt(0);
            if (!Util.isAlreadyBottom(view_index_0)) {
                view_index_0.scrollTo(0, Util.getScrollOffset(view_index_0));
            }
            if (Util.isAlreadyTop(target)) {
                scrollBy(0, dy);
                if (consumed != null && consumed.length == 2) consumed[1] = dy;
            }
        }
    }

    /**
     * 第二版
     *
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     * @param type
     */
    private void test01(@NonNull View target, int dx, int dy, @Nullable int[] consumed, int type) {
        if (dy == 0 || target == this) return;
        if (dy < 0) {
            if (Util.isAlreadyTop(this)) {
                scrollToTop();
                return;
            }
            if (Util.isAlreadyTop(target)) {
                scrollBy(0, dy);
                if (consumed != null && consumed.length == 2) consumed[1] = dy;
            } else if (Util.isAlreadyBottom(target) && !Util.isLastChild(target)) {
                scrollBy(0, dy);
                if (consumed != null && consumed.length == 2) consumed[1] = dy;
            }
        } else if (dy > 0) {
            if (Util.isAlreadyBottom(this)) {
                scrollToBottom();
                return;
            }

            if (Util.isAlreadyBottom(target)) {
                scrollBy(0, dy);
                if (consumed != null && consumed.length == 2) consumed[1] = dy;
            } else if (Util.isAlreadyTop(target) && !Util.isFirstChild(target)) {
                scrollBy(0, dy);
                if (consumed != null && consumed.length == 2) consumed[1] = dy;
            }
        }
    }


    /**
     * 第一版
     *
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     * @param type
     */
    private void test(@NonNull View target, int dx, int dy, @Nullable int[] consumed, int type) {
        if (target instanceof NestedWebView) {
            NestedWebView webView = (NestedWebView) target;
            if (dy < 0) {
                Log.d("onNestedPreScrolltag", "" + webView.isBottom());
                if (webView.isBottom()) {
                    scrollBy(0, dy);
                    if (consumed != null && consumed.length == 2) consumed[1] = dy;
                }
            } else if (dy > 0) {
                if (webView.isBottom()) {
                    scrollBy(0, dy);
                    if (consumed != null && consumed.length == 2) consumed[1] = dy;
                }
            }
        } else if (target instanceof NestedRecycleView) {
            NestedRecycleView recyclerView = (NestedRecycleView) target;
            if (dy < 0) {
                if (recyclerView.isTop()) {
                    scrollBy(0, dy);
                    if (consumed != null && consumed.length == 2) consumed[1] = dy;
                }
            } else if (dy > 0) {
                if (recyclerView.isTop()) {
                    if (getScrollY() < target.getTop()) {//不需要RecyclerView进行滑动的实际
                        scrollBy(0, dy);
                        if (consumed != null && consumed.length == 2) consumed[1] = dy;
                    }
                }
            }
        }
    }

    private class ScrollXY {
        public int mLastX;
        public int mLastY;

        public void reset() {
            mLastX = 0;
            mLastY = 0;
        }
    }

    private class NestedScroller extends Scroller {

        public View mSourceView;

        public NestedScroller(Context context) {
            this(context, null);
        }

        public NestedScroller(Context context, Interpolator interpolator) {
            this(context, interpolator,
                    context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.HONEYCOMB);
        }

        public NestedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }
    }

    public void scrollToRy() {
        scrollTo(0, getChildAt(0).getHeight());
    }

    public void scrollRyPositiion() {
        mScroller.forceFinished(true);
        RecyclerView recyclerView = (RecyclerView) getChildAt(1);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        LinearAlignTopSmoothScroller linearAlignTopSmoothScroller = new LinearAlignTopSmoothScroller(getContext());
        linearAlignTopSmoothScroller.setTargetPosition(3);
        linearLayoutManager.startSmoothScroll(linearAlignTopSmoothScroller);
    }

    public class LinearAlignTopSmoothScroller extends LinearSmoothScroller {

        public LinearAlignTopSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int getVerticalSnapPreference() {
            return LinearSmoothScroller.SNAP_TO_START;
        }
    }
}
