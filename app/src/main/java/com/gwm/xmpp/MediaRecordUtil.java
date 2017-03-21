package com.gwm.xmpp;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

/**
 * 录音工具类，用作聊天时的语音支持
 * @author asus1
 *
 *	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />  
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>  
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
 */
public class MediaRecordUtil {
	private MediaRecorder media_recorder;
	private MediaPlayer player;
	private static MediaRecordUtil util = new MediaRecordUtil();
	private MediaRecordUtil(){
		media_recorder = new MediaRecorder();
		player = new MediaPlayer();
	}
	public static MediaRecordUtil getInstance(){
		return util;
	}
	/**
	 * 启动录音，
	 * @param path 录音文件的输出地址
	 */
	public void startRecorder(String path){
		if(media_recorder == null){
			media_recorder = new MediaRecorder();
		}
		media_recorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
		media_recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);  
		media_recorder.setOutputFile(path); 
		media_recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		try {
			media_recorder.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		media_recorder.start();
	}
	/**
	 * 停止录音
	 */
	public void stopRecorder(){
		if(media_recorder != null){
			media_recorder.stop();  
			media_recorder.release();  
			media_recorder = null;
		}
	}
	/**
	 * 播放录音
	 * @param path :录音文件的路径
	 */
	public void startPlayer(String path){
		if(player == null){
			player = new MediaPlayer();
		}
         try{  
        	 player.setDataSource(path);  
        	 player.prepare();  
        	 player.start();  
         }catch(Exception e){  
        	 e.printStackTrace();
         }  
	}
	public void stopPlayer(){
		if(player != null){
			player.release();  
			player = null;  
		}
	}
}
