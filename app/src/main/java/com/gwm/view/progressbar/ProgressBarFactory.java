package com.gwm.view.progressbar;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwm.R;

/**
 * Created by hukui on 15-10-21.
 */
public class ProgressBarFactory {
    public static final int NETWORK_NO_SCALE = 1; //常用与网络访问，无刻度
    public static final int NOMAL_SCALE = 2;      //可拖动，水平，有刻度，但无精准值
    public static final int NETWORK_NO_SCALE_MSG = 4;  //网络访问 自定义消息提示，无刻度
    public static final int NOMAL_SCALE_NUMBER = 8;    //可拖动，水平，有刻度，有精准值
    public static final int NETWORK_SCALE = 16;    //旋转，有精准值
    public static final int NETWORK_SCALE_MSG = 32;    //旋转，有精准值

    private static Dialog dialog;
    public static void show(int style,Context context,String msg){
        switch (style){
            case NETWORK_NO_SCALE:
                networkNoScale(context);
                break;
            case NOMAL_SCALE:
                nomalScale();
                break;
            case NETWORK_NO_SCALE_MSG:
                networkNoScaleMsg(context,msg);
                break;
            case NOMAL_SCALE_NUMBER:
                nomalScaleNumber();
                break;
            case NETWORK_SCALE:
                networkScale();
                break;
            case NETWORK_SCALE_MSG:
                networkScaleMsg();
                break;
        }
    }
    public static void networkNoScale(Context context){
        networkNoScaleMsg(context,"正在加载...");
    }
    public static void nomalScale(){

    }
    public static void networkNoScaleMsg(Context context,String msg){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.view_progress_netaccess, null);  //得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView); //提示文字
        //加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading);
        //使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if(!TextUtils.isEmpty(msg))
            tipTextView.setText(msg);//设置加载信息

        dialog = new Dialog(context, R.style.loading_dialog);//创建自定义样式dialog

        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        dialog.show();
    }
    public static void nomalScaleNumber(){

    }
    public static void networkScale(){

    }
    public static void networkScaleMsg(){

    }
    public static void close(){
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
