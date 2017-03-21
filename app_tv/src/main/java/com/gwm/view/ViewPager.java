package com.gwm.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决了ViewPager与自身嵌套时产生的冲突。该ViewPager需要当作子控件使用
 */
public class ViewPager extends android.support.v4.view.ViewPager {
    PointF downP = new PointF();
    PointF curP = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        curP.x = arg0.getX();
        curP.y = arg0.getY();
        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
            downP.x = arg0.getX();
            downP.y = arg0.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (arg0.getAction() == MotionEvent.ACTION_UP) {
            if (downP.x == curP.x && downP.y == curP.y) {
                onSingleTouch();
                return true;
            }
        }
        return super.onTouchEvent(arg0);
    }

    public void onSingleTouch() {
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch();
        }
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch();
    }
    //点击事件监听器
    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }
}
