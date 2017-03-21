package com.gwm.view.TextView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gwm.R;

/**
 * 带有删除线的TextView
 */
public class RemoveTextView extends RelativeLayout{
    private TextView text;
    private View line;

    private float textSize;
    private int textColor,removeLineColor,removeLineHeight;
    private String textStr;
    public RemoveTextView(Context context){
        super(context);
        initView(null);
    }
    public RemoveTextView(Context context,AttributeSet attrSet){
        super(context,attrSet);
        initView(attrSet);
    }
    public RemoveTextView(Context context,AttributeSet attrSet,int style){
        super(context,attrSet,style);
        initView(attrSet);
    }
    private void initView(AttributeSet attr){
        View view = View.inflate(getContext(), R.layout.text_remove,null);
        text = (TextView) view.findViewById(R.id.text);
        line = view.findViewById(R.id.line);
        addView(view,new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        if(attr != null){
            TypedArray array = getContext().obtainStyledAttributes(attr,R.styleable.RemoveTextView);
            textSize = array.getDimension(R.styleable.RemoveTextView_textSize, 0);
            textColor = array.getColor(R.styleable.RemoveTextView_textColor, android.R.color.black);
            removeLineColor = array.getColor(R.styleable.RemoveTextView_removeLineColor,android.R.color.darker_gray);
            textStr = array.getString(R.styleable.RemoveTextView_text);
            removeLineHeight = (int) array.getDimension(R.styleable.RemoveTextView_removeLineHeight, 1);
            text.setText(textStr);
            text.setTextColor(textColor);
            text.setTextSize(textSize);
            line.setBackgroundColor(removeLineColor);
            line.setLayoutParams(new ViewGroup.LayoutParams(text.getWidth(),removeLineHeight));
            array.recycle();
        }
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        text.setTextSize(textSize);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        text.setTextColor(textColor);
    }


    public void setRemoveLineColor(int removeLineColor) {
        this.removeLineColor = removeLineColor;
        line.setBackgroundColor(removeLineColor);
    }

    public String getText() {
        return textStr;
    }

    public void setText(String textStr) {
        this.textStr = textStr;
        text.setText(textStr);
    }
    public void setText(int resId) {
        this.textStr = getContext().getString(resId);
        text.setText(textStr);
    }

    public int getRemoveLineHeight() {
        return removeLineHeight;
    }

    public void setRemoveLineHeight(int removeLineHeight) {
        this.removeLineHeight = removeLineHeight;
        line.setLayoutParams(new ViewGroup.LayoutParams(text.getWidth(),removeLineHeight));
    }
    public void append(String str){
        this.textStr += str;
        text.append(str);
    }
}
