package com.gwm.android;

import com.gwm.util.AppUtils;
import com.gwm.util.VoiceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import android.content.Context;
/**
 * 带有语音功能，去掉重叠的Toast
 * @author gwm
 */
public class Toast {
	public static final int LENGTH_SHORT = 0;
	public static final int LENGTH_LONG = 1;
	private static android.widget.Toast toast;
	private static String text_play;
	private VoiceUtil util;
	private Toast(android.widget.Toast makeText){
		toast = makeText;
		AppUtils app = AppUtils.getInstance(toast.getView().getContext());
		SpeechUtility.createUtility(toast.getView().getContext(), SpeechConstant.APPID +"="+app.getMetaValue("com.gwm.iflytek.API_KEY"));
		util = VoiceUtil.getInstance(toast.getView().getContext());
	}
	public static Toast makeText(Context context,String text,int duration){
		text_play = text;
		if(toast == null){
			toast = android.widget.Toast.makeText(context, text, duration);
		}else{
			toast.setText(text);
		}
		return new Toast(toast);
	}
	public static Toast makeText(Context context,int resId,int duration){
		text_play = context.getResources().getString(resId);
		if(toast == null){
			toast = android.widget.Toast.makeText(context, text_play, duration);
		}else{
			toast.setText(resId);
		}
		return new Toast(toast);
	}
	public void show(){
		toast.show();
		util.voiceRead(text_play);
	}
}
