package com.gwm.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;


public class VoiceUtil {
//	private Context context;
	private static VoiceUtil voiceUtil=null;
	private static String TAG = VoiceUtil.class.getSimpleName(); 	
	// 语音合成对象
	private SpeechSynthesizer mTts;

	// 默认发音人
	private String voicer="xiaoyan";
	
	//private String[] cloudVoicersEntries;
	//private String[] cloudVoicersValue ;
	
	// 缓冲进度
//	private int mPercentForBuffering = 0;
	// 播放进度
//	private int mPercentForPlaying = 0;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	
	private VoiceUtil(){
	}
	
	private VoiceUtil(Context context){
//		this.context=context;
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
		setParam();
	}
	
	public static VoiceUtil getInstance(Context context){
		
		if(voiceUtil==null){
			voiceUtil=new VoiceUtil(context);
		}
		return voiceUtil;
	}
	
	/**
	 * 初始化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		//showTip("初始化失败,错误码："+code);
        	} else {
				// 初始化成功，之后可以调用startSpeaking方法
        		// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
        		// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}		
		}
	};
	
	
	public  void voiceRead(String content){
		int code = mTts.startSpeaking(content, mTtsListener);
		if (code != ErrorCode.SUCCESS) {
			if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
				//未安装则跳转到提示安装页面
				//mInstaller.install();
			}else {
				//showTip("语音合成失败,错误码: " + code);	
			}
		}
	}
	
	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
			//showTip("开始播放");
		}

		@Override
		public void onSpeakPaused() {
			//showTip("暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			//showTip("继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			// 合成进度
//			mPercentForBuffering = percent;
			//showTip(String.format(getString(R.string.tts_toast_format),mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// 播放进度
//			mPercentForPlaying = percent;
			//showTip(String.format(getString(R.string.tts_toast_format),mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				//showTip("播放完成");
				mTts.stopSpeaking();
			} else if (error != null) {
				//showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			
		}
	};
	
	public void voiceDestory(){
		// 退出时释放连接
		mTts.destroy();
	}
	
	/**
	 * 参数设置
	 * @param param
	 * @return 
	 */
	private void setParam(){
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		//设置合成
		if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			//设置发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME,voicer);
			//设置语速
			mTts.setParameter(SpeechConstant.SPEED,"50");
			//设置音调
			mTts.setParameter(SpeechConstant.PITCH,"50");
			//设置音量
			mTts.setParameter(SpeechConstant.VOLUME,"50");
			//设置播放器音频流类型
			mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
		}else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			//设置发音人 voicer为空默认通过语音+界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME,"");
		}
	}
}
