package com.gwm.android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
/**
 * 手机摇一摇监听管理器
 * @author gwm
 */
public class Awave implements SensorEventListener{
	private SensorManager sm;
	private Vibrator vibrator;
	private AwaveListener listener;
	private boolean isVibrator;
	public Awave(Context context){
		sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setListener(AwaveListener listener){
		this.listener = listener;
	}
	/**
	 * 设置是否伴随震动
	 */
	public void setVibrator(boolean isVibrator){
		this.isVibrator = isVibrator;
	}
	public static interface AwaveListener {
		void awave(SensorEvent event);
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		//values[0]:X轴，values[1]：Y轴，values[2]：Z轴
		float[] values = event.values;
		if(sensorType == Sensor.TYPE_ACCELEROMETER){
			if((Math.abs(values[0]) > 12 || Math.abs(values[1]) > 12 || Math.abs(values[2]) > 12)){
				if(isVibrator){
					long[] pattern = {500,500};
					vibrator.vibrate(pattern,-1);
				}
				if(listener != null){
					listener.awave(event);
				}
			}
		}
	}
	@Override
	public void onAccuracyChanged(Sensor sensor,int accuracy){
	}
}