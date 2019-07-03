package com.android.lib_nested.view;

/**
 * Created by wentao *_*
 * on 2019-06-23
 */
public interface INestedView {

    /**
     * 是否滑动到顶部
     * @param dy
     * @return
     */
    boolean isTop(int dy);

    /**
     * 是否滑动到底不
     * @param dx
     * @return
     */
    boolean isBottom(int dx);
}
