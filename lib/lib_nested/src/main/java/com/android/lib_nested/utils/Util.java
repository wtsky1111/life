package com.android.lib_nested.utils;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.android.lib_nested.view.NestedWebView;

/**
 * Created by wentao *_*
 * on 2019-06-24
 */
public class Util {
    public static boolean isAlreadyTop(@NonNull View view) {
        return !view.canScrollVertically(-1);
    }

    public static boolean isAlreadyBottom(@NonNull View view) {
        if (view instanceof NestedWebView) {
            return ((NestedWebView) view).isBottom();
        }
        return !view.canScrollVertically(1);
    }

    public static int getScrollOffset(@NonNull View view) {
        if (view instanceof NestedWebView) {
            return ((NestedWebView) view).getScrollRange();
        }
        return view.getHeight();
    }

    public static boolean isFirstChild(@NonNull View view) {
        if (view.getParent() == null) return false;
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        return viewGroup.indexOfChild(view) == 0;
    }

    public static boolean isLastChild(@NonNull View view) {
        if (view.getParent() == null) return false;
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        return viewGroup.indexOfChild(view) == viewGroup.getChildCount() - 1;
    }
}
