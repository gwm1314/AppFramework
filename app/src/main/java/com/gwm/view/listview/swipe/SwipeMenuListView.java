package com.gwm.view.listview.swipe;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 *
 * 创建一个继承listview的类
 *
 */
public class SwipeMenuListView extends ListView {

    private static final int TOUCH_STATE_NONE = 0;//
    private static final int TOUCH_STATE_X = 1;
    private static final int TOUCH_STATE_Y = 2;

    private int MAX_Y = 5;
    private int MAX_X = 3;
    private float mDownX;
    private float mDownY;
    private int mTouchState;// 点击的状态
    private int mTouchPosition;// 点击的位置
    private SwipeMenuLayout mTouchView;// 获得我点击的那个layout
    private OnSwipeListener mOnSwipeListener;// 监听事件

    private SwipeMenuCreator mMenuCreator;// 创建一个listviewitem 的menu
    private OnMenuItemClickListener mOnMenuItemClickListener;// menu的点击事件
    private Interpolator mCloseInterpolator;// Interpolator
    // 被用来修饰动画效果，定义动画的变化率，
    // 可以使存在的动画效果accelerated(加速)，decelerated(减速),repeated(重复),bounced(弹跳)等。
    private Interpolator mOpenInterpolator;

    // 构造函数1
    public SwipeMenuListView(Context context) {
        super(context);
        init();
    }

    public SwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SwipeMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // /////////////////////////////////////////////////
    // 初始化
    private void init() {
        MAX_X = dp2px(MAX_X);
        MAX_Y = dp2px(MAX_Y);
        mTouchState = TOUCH_STATE_NONE;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////
    // 设置适配器的方法
    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
            @Override
            public void createMenu(SwipeMenu menu) {
                if (mMenuCreator != null) {
                    mMenuCreator.create(menu);
                }
            }

            @Override
            public void onItemClick(SwipeMenuView view, SwipeMenu menu,
                                    int index) {
                if (mOnMenuItemClickListener != null) {
                    mOnMenuItemClickListener.onMenuItemClick(
                            view.getPosition(), menu, index);
                }
                if (mTouchView != null) {
                    mTouchView.smoothCloseMenu();
                }
            }
        });
    }

    // ///////////////////////////////////////////////////////
    // 设置关闭菜单的动画
    public void setCloseInterpolator(Interpolator interpolator) {
        mCloseInterpolator = interpolator;
    }

    // 设置开启的动画
    public void setOpenInterpolator(Interpolator interpolator) {
        mOpenInterpolator = interpolator;
    }

    // 获得开启menu的动画
    public Interpolator getOpenInterpolator() {
        return mOpenInterpolator;
    }

    // 获得close的动画
    public Interpolator getCloseInterpolator() {
        return mCloseInterpolator;
    }

    // 1. down事件首先会传递到onInterceptTouchEvent()方法
    // 2. 如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return
    // false，那么后续的move,
    // up等事件将继续会先传递给该ViewGroup，之后才和down事件一样传递给最终的目标view的onTouchEvent()处理。
    // 3. 如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return
    // true，那么后续的move,
    // up等事件将不再传递给onInterceptTouchEvent()，而是和down事件一样传递给该ViewGroup的onTouchEvent()处理，注意，目标view将接收不到任何事件。
    // 4.
    // 如果最终需要处理事件的view的onTouchEvent()返回了false，那么该事件将被传递至其上一层次的view的onTouchEvent()处理。
    // 5. 如果最终需要处理事件的view
    // 的onTouchEvent()返回了true，那么后续事件将可以继续传递给该view的onTouchEvent()处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {// 屏幕事件
        if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null)
            return super.onTouchEvent(ev);
        int action = MotionEventCompat.getActionMasked(ev);// 获得action
        action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:// 获得按下的位置
                mDownX = ev.getX();// 获得x轴的值
                mDownY = ev.getY();// 获得y轴的值
                mTouchState = TOUCH_STATE_NONE;// 状态为没有任何情况
                // /////////////////////////////////////////////////////////////////////////////////
                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());// 我把它理解为通过x和y的位置来确定这个listView里面这个item的位置
                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());// 获得点击的listview在list中的位置
                //////////////////////////////////////////////////////////////////////
                //
                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                    mTouchView = null;
                    return super.onTouchEvent(ev);
                }
                if (view instanceof SwipeMenuLayout) {// java 中的instanceof
                    // 运算符是用来在运行时指出对象是否是特定类的一个实例。instanceof通过返回一个布尔值来指出，这个对象是否是这个特定类或者是它的子类的一个实例
                    mTouchView = (SwipeMenuLayout) view;
                }
                if (mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                    }
                    getSelector().setState(new int[] { 0 });
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                } else if (mTouchState == TOUCH_STATE_NONE) {
                    if (Math.abs(dy) > MAX_Y) {
                        mTouchState = TOUCH_STATE_Y;
                    } else if (dx > MAX_X) {
                        mTouchState = TOUCH_STATE_X;
                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onSwipeStart(mTouchPosition);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                    }
                    if (mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipeEnd(mTouchPosition);
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    // /////////////////////////////
    // 转化
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    // 设置menu
    public void setMenuCreator(SwipeMenuCreator menuCreator) {
        this.mMenuCreator = menuCreator;
    }

    // 设置监听事件
    public void setOnMenuItemClickListener(
            OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.mOnSwipeListener = onSwipeListener;
    }

    public static interface OnMenuItemClickListener {
        void onMenuItemClick(int position, SwipeMenu menu, int index);
    }

    public static interface OnSwipeListener {
        void onSwipeStart(int position);
        void onSwipeEnd(int position);
    }

    /**
     *
     * @author baoyz
     * @date 2014-8-24
     *
     */
    public interface SwipeMenuCreator {

        void create(SwipeMenu menu);
    }

}
