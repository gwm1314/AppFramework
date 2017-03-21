package com.gwm.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.gwm.android.Handler;
import com.gwm.manager.PageManager;
import com.gwm.mvc.BaseEntry;
import com.gwm.mvc.HttpObserver;
import com.gwm.util.AppUtils;
import com.gwm.util.MyLogger;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.List;

/**
 * 该类加入以下功能：
 * 1."再按一次退出程序"提示(只需要调用addFirstToast()方法把activity添加到退出通知提示的集合即可)
 * 2.无需为view设置监听操作，只需要声明一个int[]类型的ids变量，把所有要监听的控件id放到该数组中即可，同时需要继承该类
 * 3.对AsyncTask做了内存优化。
 * 4.带有MVC设计思想
 * 5.会自动根据需要是否隐藏系统键盘
 */
public abstract class BaseActivity extends FragmentActivity implements BaseCommon.EventListener,Handler.HandlerListener,HttpObserver {
	private List<String> activities;
	protected SharedPreferences sp;
	private AppUtils app_util;
	private FragmentManager manager;
	protected MyLogger Log;
	private long exitTime = 0;
    private InputMethodManager imm;

    private BaseAttribute attr;
    private BaseCommon common;
	@Override
	public final void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        attr = new BaseAttribute();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getApplication() instanceof BaseAppcation){
            activities = ((BaseAppcation) getApplication()).getUIS();
            ((BaseAppcation) getApplication()).addActivity(this);
        }
		Log = MyLogger.kLog();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        common = BaseCommon.getCommon(getApplicationContext());
		onInitAttribute(attr);
		sp = BaseAppcation.getInstance().getSharedPreferences();
		manager = getSupportFragmentManager();
		app_util = AppUtils.getInstance(getApplicationContext());
        setContentView();
        onCreate(sp, manager, savedInstanceState);
    }
    public void onInitAttribute(BaseAttribute attr){
        common.initAttribute(attr);
    }
    public final void setContentView(){
        Log.i("auto layout："+attr.mSetView);
        if (attr.mSetView){
            View view = common.loadView(getClass().getName(),BaseCommon.ACTIVITY);
            if (view != null){
                setContentView(view);
            }
        }
    }
	/**
	 *重写setContentView()方法，绑定事件监听
	 */
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		setListener();
	}
	@Override
	public void setContentView(View view){
		super.setContentView(view);
		setListener();
	}
	/**
	 * 监听事件重写了，只需要你手动声明一个以ids为变量名的int[] 该方法会自动为在这个数组里的控件设置上事件监听
	 */
	private void setListener(){
		common.setListener(getWindow().getDecorView(),this);
	}
    /**
	 * 将Activity添加到退出通知
	 */
	public void addFirstToast() {
		activities.add(getActivityName());
	}

    public void showPage(BasePage page){
        PageManager.getInstance((ViewGroup)getView()).showPage(page);
    }

    public View getView(){
        return getWindow().getDecorView();
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if((System.currentTimeMillis() - exitTime) > 2000 && (activities.indexOf(getActivityName()) != -1)){
				Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else if ((activities.indexOf(getActivityName()) != -1)) {
				if(getApplication() instanceof BaseAppcation){
					((BaseAppcation)getApplication()).exit();
				}
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK && (activities.indexOf(getActivityName()) != -1)) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK){
			if(getApplication() instanceof BaseAppcation){
				((BaseAppcation) getApplication()).delActivity(this);
			}
			return true;
		}
		return onKeyDownMethod(keyCode, event);
	}
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
	 * 无需重写onKeyDown()方法,需要时应该重写该方法 已经屏蔽了返回键，如需处理返回键，请重写onKeyDown()方法
	 * @param keyCode
	 * @param event
	 * @return
	 */
	protected boolean onKeyDownMethod(int keyCode, KeyEvent event){
		//如果启用了BasePage类，下面两行代码需要复制在activity的onKeyDown()方法中
//		BaseFragment frag = (BaseFragment) manager.findFragmentByTag("");
//		return frag.getPager().onKeyDownMethod(keyCode,event);

		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 如果启动了BasePager，请将如下代码迁移到BasePager所依附的Activity中
	 * 		但需在此之前调用setCurrentFragment()方法才会有效
	 */
//	public boolean onKeyDownMethod(int keyCode, KeyEvent event){ 
//		return current_Fragment.getCurrentPage().onKeyDownMethod(keyCode, event);
//	}
	/**
	 * 获取当前正在运行的Activity
	 * @return
	 */
	private String getActivityName() {
		return app_util.getActivityName();
	}
	/**
	 * 传递数据到指定的Fragment中，会调用对应的Fragment的setArguments()方法传递数据。Fragment需要重写该方法接收数据
	 * @param tag  Fragment的标识
	 * @param data 要传递的数据
	 */
	public void addToFragmentData(String tag,Bundle data){
		BaseFragment frag = getFragment(tag);
		addToFragmentData(frag, data);
	}
	public void addToFragmentData(BaseFragment frag,Bundle data){
		frag.setArguments(data);
	}
	/**
	 * 获取当前应用的版本号
	 * @return
	 * @throws Exception
	 */
	public double getVersionName() {
		return Double.parseDouble(app_util.getVersionName());
	}

    public ActivityManager getActivityManager(){
        return (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    }
	/**
	 * 通过Tag找Fragment
	 * @param tag
	 * @return
	 */
	public BaseFragment getFragment(String tag){
		return (BaseFragment) manager.findFragmentByTag(tag);
	}
	/**
	 * 普通控件点击事件
	 */
	@Override
	public void onClick(View v){
	}

	/**
	 * 普通控件长按点击事件
	 */
	@Override
	public boolean onLongClick(View v){
		return false;
	}
	/**
	 * ListView的item点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
	}
	/**
	 * 启动一个Activity
	 * @param clz Activity实例的Class对象
	 */
	public void startActivity(Class<? extends BaseActivity> clz) {
		Intent intent = new Intent(this, clz);
		startActivity(intent);
	}
	/**
	 * 启动一个Activity，带有启动动画
	 * @param clz Activity实例的Class对象
	 */
	public void startActivity(Class<? extends BaseActivity> clz, int enterAnim,
			int exitAnim) {
		Intent intent = new Intent(this, clz);
		startActivity(intent, enterAnim, exitAnim);
	}
	/**
	 * 启动一个Activity，带有启动动画
	 * @param action Activity实例的action动作
	 */

	public void startActivity(String action) {
		Intent intent = new Intent(action);
		startActivity(intent);
	}
	/**
	 * activity之间的切换，默认渐入渐出动画
	 */
	@Override
	public void startActivity(Intent intent){
		super.startActivity(intent);
		//overridePendingTransition(R.anim.fade, R.anim.hold);
		//overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit); //缩放动画
	}
	/**
	 * 优化过后的获取Context对象的方法
	 */
	@Override
	public Context getApplicationContext() {
		return new SoftReference<Context>(super.getApplicationContext()).get();
	}
	/**
	 * 自定义activity之间的切换动画
	 * @param intent
	 * @param enterAnim
	 *                 进入时动画
	 * @param exitAnim
	 *                 出去时动画
	 */
	public void startActivity(Intent intent, int enterAnim, int exitAnim) {
		super.startActivity(intent);
		overridePendingTransition(enterAnim, exitAnim);
	}


	/**
	 * ListView的item长按事件监听
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id){
		return false;
	}
	/**
	 * Spinner控件的事件监听
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view,
			int position, long id) {
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent){
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	}
	/**
	 * Activity的生命周期onCreate()方法
	 * @param sp
	 *                 SharedPreferences文件对象
	 * @param manager
	 *                 管理Fragment的对象，如果使用Fragment可以跟本框架做到无缝的结合
	 * @param savedInstanceState
	 *                 Activity的状态保存
	 */
	public abstract void onCreate(SharedPreferences sp,FragmentManager manager, Bundle savedInstanceState);
	/**
	 * Fragment之间的切换方法，加载Fragment方法.<br />
	 * 重写该方法时要注意的问题：
	 * <ol>
	 * <li>该方法尽量放在activity中。避免Fragment的嵌套出现id资源找不到的问题,最好Fragment里面不要在嵌套Fragment.</li>
	 * <li>在加载之前需判断是否已经加载了，如果是 请先移除，在加载。避免报该Fragment已经加载了的问题.</li>
	 * <li>该方法不能在activity的生命周期中使用。避免回退栈的问题.<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * (原因：addToBackStack()方法不能在activity的生命周期中使用)</li>
	 * </ol>
	 * @param frag 
	 * 					中间容器显示的Fragment
	 * @param tag
	 *                 中间容器的标识
	 */
	public void jumpFragment(BaseFragment frag,String tag){
	}
	/**
	 * Fragment之间的切换方法，加载Fragment方法
	 * @param frag
	 * @param tag
	 * @param isHiddenNav  是否隐藏导航栏，至于如何实现隐藏需开发者自己考虑
	 */
	public void jumpFragment(BaseFragment frag, String tag,boolean isHiddenNav){
	}
	@Override
	public void handlerIntent(BaseEntry model, int urlId) throws Exception{
	}
	@Override
	public void handlerIntent(List<? extends BaseEntry> models,
                              int urlId) throws Exception{
	}
	@Override
	public void handlerIntent(SparseArray<? extends BaseEntry> models,
                              int urlId) throws Exception{
	}
	@Override
	public void handlerIntent(File file, int urlId) throws Exception{
	}
	@Override
	public void handlerIntent(String result, int urlId) throws Exception{
	}

    @Override
    public void handlerIntent(boolean result, int urlId) throws Exception {

    }

    @Override
    public void handleMessage(Message msg) {

    }
}