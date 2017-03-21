package com.gwm.base;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.gwm.R;
import com.gwm.android.ThreadManager;
import com.gwm.android.Toast;
import com.gwm.util.AppUtils;
import com.gwm.util.ContanstUtil;
import com.gwm.util.MyLogger;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;

/**
 * 做了一些处理的Application的类，为了维持该框架的运行特意写的一个类
 * @author gwm
 */
public class BaseAppcation extends Application{
	private static final boolean DEVELOPER_MODE = true; //开启性能测试，检测应用程序所有有可能发生超时的操作，可以在logcat中看到此类操作
	private List<String> UIS = new ArrayList<String>(); //需要退出提示的activity集合
	private List<Activity> activitys = new ArrayList<Activity>(); //界面集合
    private List<BaseBroadcastReceiver> receivers = new ArrayList<BaseBroadcastReceiver>();
	private static BaseAppcation instance;
	public MyLogger Log;
	
	private SharedPreferences sp;

    private Activity crrentActivity;
	/**
	 * 获取该类的实例
	 * @return
	 */
	public static BaseAppcation getInstance(){
		return instance;
	}
	public SharedPreferences getSharedPreferences(){
		return sp;
	}
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
	@Override
	public void onCreate(){
		if(DEVELOPER_MODE){
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
		}
		super.onCreate();
		sp = getSharedPreferences(ContanstUtil.getInstance(this).getSpKey(R.string.SP_NAME), MODE_MULTI_PROCESS);
		Log = MyLogger.kLog();
		if(instance == null){
			instance = this;
		}
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

        ShareSDK.initSDK(this, AppUtils.getInstance(this).getMetaValue("SHARED_APP_KEY"));
	}

    /**
     * 获取当前正在显示的Activity
     * @return
     */
    public Activity getCrrentActivity(){
        return crrentActivity;
    }


    public List<BaseBroadcastReceiver> getReceivers(){
        return receivers;
    }

    /**
     * 禁用或启用所有的广播接收器
     * @param enable false:禁用   true:启用
     */
    public void enableReceivers(boolean enable){
        PackageManager pm = getPackageManager();
        for (BaseBroadcastReceiver receiver : receivers){
            ComponentName componentName = new ComponentName(this,receiver.getClass());
            int newStatus = 0;
            if(enable)
                newStatus = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
            else
                newStatus = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
            pm.setComponentEnabledSetting(componentName,newStatus,PackageManager.DONT_KILL_APP);
        }
    }
	/**
	 * 添加Activity到自定义回退栈中
	 * @param activity
	 */
	public void addActivity(Activity activity){
		if(activitys.indexOf(activity) == -1)
			activitys.add(activity);
		else{
			activitys.remove(activity);
			activitys.add(activity);
		}
	}
	/**
	 * 退出该应用程序
	 */
	public void exit(){
        ThreadManager.getInstance().close();  //关闭所有的延时任务

		for(Activity activity : activitys){
			activity.finish();
		}
		activitys.clear();
	}
	/**
	 * 从自定义回退栈中删除activity并执行finish()方法
	 * @param activity
	 */
	public void delActivity(Activity activity){
		activitys.remove(activity);
		activity.finish();
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Toast.makeText(this, "当前应用已经内存不足，请注意。。。", Toast.LENGTH_SHORT).show();
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        enableReceivers(false);

	}
	public List<String> getUIS(){
		return UIS;
	}
    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks(){
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            crrentActivity = activity;
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {
            crrentActivity = null;
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }
    };
}
