package com.gwm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.gwm.R;

/**
 * 按照字母排序的View
 */
public class AssortView extends View{
    // 分类
    private String buffer = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Paint paint = new Paint();
    // 选择的索引
    private int selectIndex = -1;
    // 字母监听器
    private OnTouchAssortListener onTouch;


    public AssortView(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public AssortView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public AssortView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }


    public void setOnTouchAssortListener(OnTouchAssortListener onTouch) {
        this.onTouch = onTouch;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int interval = height / buffer.length();

        for (int i = 0, length = buffer.length(); i < length; i++) {
            // 抗锯齿
            paint.setAntiAlias(true);
            // 白色
            paint.setColor(Color.parseColor("#828282"));
            paint.setTextSize(35);
            if (i == selectIndex) {
                // 被选择的字母改变颜色和粗体
                paint.setColor(Color.parseColor("#828282"));
                paint.setFakeBoldText(true);
                paint.setTextSize(60);
            }
            // 计算字母的X坐标
            float xPos = width / 2 - paint.measureText(String.valueOf(buffer.charAt(i))) / 2;
            // 计算字母的Y坐标
            float yPos = interval * i + 24;
            canvas.drawText(String.valueOf(buffer.charAt(i)), xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getY();
        int index = (int) (y / getHeight() * buffer.length());
        if (index >= 0 && index < buffer.length()) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    // 如果滑动改变
                    if (selectIndex != index) {
                        selectIndex = index;
                        if (onTouch != null) {
                            onTouch.onTouchAssortListener(buffer.charAt(selectIndex));
                        }
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    selectIndex = index;
                    if (onTouch != null) {
                        onTouch.onTouchAssortListener(buffer.charAt(selectIndex));
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (onTouch != null) {
                        onTouch.onTouchAssortUP();
                    }
                    selectIndex = -1;
                    break;
            }
        } else {
            selectIndex = -1;
            if (onTouch != null) {
                onTouch.onTouchAssortUP();
            }
        }
        invalidate();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public static interface OnTouchAssortListener {
        void onTouchAssortListener(char s);
        void onTouchAssortUP();
    }

}
