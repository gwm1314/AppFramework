package com.gwm.view.listview.swipe;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnDrawListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 *
 * @author baoyz
 * @date 2014-8-23
 *
 */
public class SwipeMenuLayout extends FrameLayout {

    private static final int CONTENT_VIEW_ID = 1;
    private static final int MENU_VIEW_ID = 2;

    private static final int STATE_CLOSE = 0;
    private static final int STATE_OPEN = 1;

    private View mContentView;
    private SwipeMenuView mMenuView;
    private int mDownX;
    private int state = STATE_CLOSE;// 先是关闭的状态
    private GestureDetectorCompat mGestureDetector;// 通过这个类我们可以识别很多复杂的手势
    // OnGestureListener有下面的几个动作：
    // 按下（onDown）： 刚刚手指接触到触摸屏的那一刹那，就是触的那一下。
    // 抛掷（onFling）： 手指在触摸屏上迅速移动，并松开的动作。
    // 长按（onLongPress）： 手指按在持续一段时间，并且没有松开。
    // 滚动（onScroll）： 手指在触摸屏上滑动。
    // 按住（onShowPress）： 手指按在触摸屏上，它的时间范围在按下起效，在长按之前。
    // 抬起（onSingleTapUp）：手指离开触摸屏的那一刹那。
    private OnGestureListener mGestureListener;//
    private boolean isFling;
    private int MIN_FLING = dp2px(15);// 最少的 抛掷距离
    private int MAX_VELOCITYX = -dp2px(500);
    private ScrollerCompat mOpenScroller;
    private ScrollerCompat mCloseScroller;
    private int mBaseX;
    private int position;
    private Interpolator mCloseInterpolator;
    private Interpolator mOpenInterpolator;

    public SwipeMenuLayout(View contentView, SwipeMenuView menuView) {
        this(contentView, menuView, null, null);
    }

    public SwipeMenuLayout(View contentView, SwipeMenuView menuView,
                           Interpolator closeInterpolator, Interpolator openInterpolator) {
        super(contentView.getContext());
        mCloseInterpolator = closeInterpolator;
        mOpenInterpolator = openInterpolator;
        mContentView = contentView;
        mMenuView = menuView;
        mMenuView.setLayout(this);
        init();
    }

    // private SwipeMenuLayout(Context context, AttributeSet attrs, int
    // defStyle) {
    // super(context, attrs, defStyle);
    // }

    private SwipeMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private SwipeMenuLayout(Context context) {
        super(context);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        mMenuView.setPosition(position);
    }

    private void init() {
        // //////////////////////////////////////////////////////
        // 手势监听事件
        mGestureListener = new SimpleOnGestureListener() {
            // //////////////////////////////
            // 点击
            @Override
            public boolean onDown(MotionEvent e) {
                isFling = false;
                return true;
            }

            // 抛出
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                if ((e1.getX() - e2.getX()) > MIN_FLING// 看抛出的距离是不是大于要求的值
                        && velocityX < MAX_VELOCITYX) {
                    isFling = true;
                }
                // Log.i("byz", MAX_VELOCITYX + ", velocityX = " + velocityX);
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        };
        mGestureDetector = new GestureDetectorCompat(getContext(),
                mGestureListener);

        // mScroller = ScrollerCompat.create(getContext(), new
        // BounceInterpolator());
        if (mCloseInterpolator != null) {// 关闭的动画
            mCloseScroller = ScrollerCompat.create(getContext(),
                    mCloseInterpolator);// 创建关闭的动作
        } else {
            // 要是没有动画
            mCloseScroller = ScrollerCompat.create(getContext());
        }
        // ////////////////////////////////////
        // open动画同理
        if (mOpenInterpolator != null) {
            mOpenScroller = ScrollerCompat.create(getContext(),
                    mOpenInterpolator);
        } else {
            mOpenScroller = ScrollerCompat.create(getContext());
        }
        // //////////////////////////////////////////////////////////////////////
        // 这个是创建一个layoutparams一个layout的参数
        LayoutParams contentParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mContentView.setLayoutParams(contentParams);// 设置参数
        if (mContentView.getId() < 1) {
            mContentView.setId(CONTENT_VIEW_ID);// 设置内容view的id
        }
        // /////////////////////////////////////
        // 设置menuview的id
        mMenuView.setId(MENU_VIEW_ID);
        mMenuView.setVisibility(View.GONE);// 先不要显示相应的menuview
        mMenuView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));

        addView(mContentView);
        addView(mMenuView);

        // if (mContentView.getBackground() == null) {
        // mContentView.setBackgroundColor(Color.WHITE);
        // }

        // in android 2.x, MenuView height is MATCH_PARENT is not work.
        // ViewTreeObserver是用来帮助我们监听某些View的某些变化的
        getViewTreeObserver().addOnGlobalLayoutListener(// // 用于监听焦点的变化
                new OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        LayoutParams params = (LayoutParams) mMenuView
                                .getLayoutParams();
                        params.height = mContentView.getHeight();// 等高
                        mMenuView.setLayoutParams(mMenuView.getLayoutParams());// 设置
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public boolean onSwipe(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                isFling = false;
                break;
            case MotionEvent.ACTION_MOVE:// 移动
                // Log.i("byz", "downX = " + mDownX + ", moveX = " + event.getX());
                int dis = (int) (mDownX - event.getX());// 向左消去我移动的距离
                if (state == STATE_OPEN) {// 状态为打开的时候 表示menu全部显示出来了 要加上这个距离
                    dis += mMenuView.getWidth();// 获得
                }
                swipe(dis);//
                break;
            case MotionEvent.ACTION_UP:// 当我松开的时候
                if (isFling || (mDownX - event.getX()) > (mMenuView.getWidth() / 2)) {
                    // open
                    smoothOpenMenu();
                } else {
                    // close
                    smoothCloseMenu();
                    return false;
                }
                break;
        }
        return true;
    }

    public boolean isOpen() {
        return state == STATE_OPEN;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void swipe(int dis) {
        if (mMenuView.getVisibility() == View.GONE) {
            mMenuView.setVisibility(View.VISIBLE);// 设置可以显示menuitem了
        }
        // ///////////////////////////////////////////////////////////////
        // 要是大于了内容view的宽度 就设置为内容view的宽度
        if (dis > mMenuView.getWidth()) {
            dis = mMenuView.getWidth();
        }
        // //////////////////////////////////
        // 为0就设置为0
        if (dis < 0) {
            dis = 0;
        }
        // /////////////////////////////////////////////////////////////////////////
        mContentView.layout(-dis, mContentView.getTop(),// 显示的起始点为 内容显示的左上角
                mContentView.getWidth() - dis, getMeasuredHeight());// 内容显示的右下角
        // //////////////////////////////////////////////////////////
        // 同理
        mMenuView.layout(mContentView.getWidth() - dis, mMenuView.getTop(),
                mContentView.getWidth() + mMenuView.getWidth() - dis,
                mMenuView.getBottom());
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////
    // 里Scroller类是为了实现View平滑滚动的一个Helper 类。通常在自定义的View时使用，在View中定义一个私有成员mScroller
    // = new
    // Scroller(context)。设置mScroller滚动的位置时，
    // 并不会导致View的滚动，通常是用mScroller记录/计算View滚动的位置，再重写View的computeScroll()，完成实际的滚动
    // getCurrX() //获取mScroller当前水平滚动的位置
    // //获取mScroller当前竖直滚动的位置
    // //获取mScroller最终停止的水平位置
    // 获取mScroller最终停止的竖直位置
    // mScroller.setFinalX(int newX) //设置mScroller最终停留的水平位置，没有动画效果，直接跳到目标位置
    // mScroller.setFinalY(int newY) //设置mScroller最终停留的竖直位置，没有动画效果，直接跳到目标位置
    //
    // //滚动，startX, startY为开始滚动的位置，dx,dy为滚动的偏移量, duration为完成滚动的时间
    // mScroller.startScroll(int startX, int startY, int dx, int dy)
    // //使用默认完成时间250ms
    // mScroller.startScroll(int startX, int startY, int dx, int dy, int
    // duration)
    //
    // mScroller.computeScrollOffset()
    // //返回值为boolean，true说明滚动尚未完成，false说明滚动已经完成。这是一个很重要的方法，通常放在View.computeScroll()中，用来判断是否滚动是否结束。
    @Override
    public void computeScroll() {
        if (state == STATE_OPEN) {
            if (mOpenScroller.computeScrollOffset()) {// 要是没有滚动完 就启动滚动的动画
                swipe(mOpenScroller.getCurrX());
                postInvalidate();// //必须调用该方法，否则不一定能看到滚动效果
            }
        } else {
            if (mCloseScroller.computeScrollOffset()) {
                swipe(mBaseX - mCloseScroller.getCurrX());
                postInvalidate();
            }
        }
    }

    public void smoothCloseMenu() {
        state = STATE_CLOSE;//
        mBaseX = -mContentView.getLeft();//
        mCloseScroller.startScroll(0, 0, mBaseX, 0, 350);// 滚动的final位置
        postInvalidate();
    }

    // ////////////////////////////////////////////
    // 创建open动画
    public void smoothOpenMenu() {
        state = STATE_OPEN;
        mOpenScroller.startScroll(-mContentView.getLeft(), 0,
                mMenuView.getWidth(), 0, 350);
        postInvalidate();
    }

    // 创建close动画
    public void closeMenu() {
        if (mCloseScroller.computeScrollOffset()) {
            mCloseScroller.abortAnimation();
        }
        if (state == STATE_OPEN) {
            state = STATE_CLOSE;
            swipe(0);
        }
    }

    public void openMenu() {
        if (state == STATE_CLOSE) {
            state = STATE_OPEN;
            swipe(mMenuView.getWidth());
        }
    }

    public View getContentView() {
        return mContentView;
    }

    public SwipeMenuView getMenuView() {
        return mMenuView;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mContentView.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        ////////////////////////////////////////////
        //设置menuitem位置
        mMenuView.layout(getMeasuredWidth(), 0,
                getMeasuredWidth() + mMenuView.getMeasuredWidth(),
                getMeasuredHeight());
        // bringChildToFront(mContentView);
    }
}
