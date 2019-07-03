package com.android.lib_nested.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by wentao *_*
 * on 2019-06-24
 */
public class NestedRecycleView extends RecyclerView {

    public NestedRecycleView(Context context) {
        super(context);
    }

    public NestedRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isTop() {
        return !canScrollVertically(-1);
    }

    public boolean isBottom() {
        return !canScrollVertically(1);
    }
}
