package com.gwm.android;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gwm.util.MyLogger;

/**
 * Created by dell on 2017/2/18.
 * 解决RecyclerView在使用过程中出现的bug
 */

public class FrameworkLayoutManager extends StaggeredGridLayoutManager {
    private RecyclerView mRecyclerView;
    public FrameworkLayoutManager(RecyclerView mRecyclerView,int spanCount, int orientation){
        super(spanCount,orientation);
        this.mRecyclerView = mRecyclerView;
    }
    /**
     * 焦点错位的bug
     */
    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        int count = getItemCount();
        int fromPos = getPosition(getFocusedChild());
        switch (direction) {
            case View.FOCUS_RIGHT:
                fromPos++;
                break;
            case View.FOCUS_LEFT:
                fromPos--;
                break;
        }
        MyLogger.kLog().i("onInterceptFocusSearchGame , fromPos = "+fromPos + " count = " + count);
        if(fromPos >= count) {
            return focused;
        } else {
            smoothScrollToPosition(mRecyclerView,null,fromPos);
            return findViewByPosition(fromPos);
        }
    }
}
