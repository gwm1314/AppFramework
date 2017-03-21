package com.gwm.base;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.gwm.android.Handler;
import com.gwm.manager.SizeOf;
import com.gwm.mvc.BaseEntry;
import com.gwm.mvc.HttpObserver;
import com.gwm.util.MyLogger;
/**
 * 如果启动该类。数据会优化到极致，整个项目只会有一个Activity.Fragment  剩下的全是以该类作为基类的入口
 * @author gwm
 */
public abstract class BasePage extends SizeOf implements BaseCommon.EventListener,Handler.HandlerListener,HttpObserver {
	private View view;
	public MyLogger Log;
	private Context context;

    protected SharedPreferences sp;

    private BaseAttribute attr;
    private BaseCommon common;
  	/**
	 * @param context 当前Page所依附的Fragment
	 */
	public BasePage(Context context){
		if(context == null){
			throw new NullPointerException("当前并不能找到它所依附的Fragment");
		}
		this.context = context;
		sp = BaseAppcation.getInstance().getSharedPreferences();
		Log = MyLogger.kLog();
        attr = new BaseAttribute();
        common = BaseCommon.getCommon(context);
        onInitAttribute(attr);
		view = initView(sp);
		findViews();
		setListener();
		initData();
	}
    public void onInitAttribute(BaseAttribute attr){
        common.initAttribute(attr);
    }
	public View initView(SharedPreferences sp){
        View view = null;
        if(attr.mSetView){
            view = setContentView();
        }
        return view;
    }
	public abstract void findViews();
	public abstract void initData();
	/**
	 * 获取当前页所依附的Fragment
	 * @return
	 */
	public Context getContext(){
		return context;
	}
	private void setListener() {
		common.setListener(view,this);
	}
    public final View setContentView(){
        return common.loadView(getClass().getName(),BaseCommon.PAGE);
    }

    public View findViewById(int resId){
		return view.findViewById(resId);
	}
	public View getView(){
		return view;
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id){
		
	}
	/**
	 * ListView的item长按事件监听
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) { 
		return false;
	}
	/**
	 * Spinner控件的事件监听
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	}
	public BasePage newInstance(){
		return this;
	}
	@Override
	public void handlerIntent(BaseEntry model, int urlId) throws Exception{
	}
	@Override
	public void handlerIntent(List<? extends BaseEntry> models,
                              int urlId) throws Exception {
	}
	@Override
	public void handlerIntent(SparseArray<? extends BaseEntry> models,
			int urlId) throws Exception {
	}
	@Override
	public void handlerIntent(File file, int urlId) throws Exception {
	}
	@Override
	public void handlerIntent(String result, int urlId) throws Exception {
		
	}

    @Override
    public void handlerIntent(boolean result, int urlId) throws Exception {

    }

    public void setArguments(Bundle data) {
		if(data == null || data.isEmpty()){
            Log.i("未接收到数据");
		}
	}
    @Override
    public void handleMessage(Message msg) {

    }
}