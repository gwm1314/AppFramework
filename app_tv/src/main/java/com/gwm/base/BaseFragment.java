package com.gwm.base;

import java.io.File;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.gwm.android.Handler;
import com.gwm.manager.PageManager;
import com.gwm.mvc.BaseEntry;
import com.gwm.mvc.HttpObserver;
import com.gwm.util.MyLogger;
/**
 * Fragment的基类
 * @author asus1
 *		无需为view设置监听操作，只需要声明一个int[]类型的ids变量，把所有要监听的控件id放到该数组中即可，同时需要继承该类
 *		对BasePager提供了支持
 */
public abstract class BaseFragment extends Fragment implements BaseCommon.EventListener,Handler.HandlerListener,HttpObserver {
	private SharedPreferences sp;
	protected FragmentManager manager;
    protected View view;
    protected MyLogger Log;

    private BaseAttribute attr;
    private BaseCommon common;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        attr = new BaseAttribute();
		Log = MyLogger.kLog();
		manager = getChildFragmentManager();
		sp = BaseAppcation.getInstance().getSharedPreferences();
        common = BaseCommon.getCommon(getActivity());
        onInitAttribute(attr);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		view = onCreateView(inflater, sp,savedInstanceState);
        return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findViews();
		setListener();
	}
	public void addToPageData(String tag,Bundle data){
		BasePage pager = PageManager.getInstance((ViewGroup)getView()).getPager(tag);
		addToPageData(pager, data);
	}
	public void addToPageData(BasePage pager,Bundle data){
		pager.setArguments(data);
	}
	@Override
	public void onResume(){
		super.onResume();
		initData();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
    public final View setContentView(){
        return common.loadView(getClass().getName(),BaseCommon.FRAGMENT);
    }

    /**
	 *监听事件重写了，只需要你手动声明一个以ids为变量名的int[] 该方法会自动为在这个数组里的控件设置上事件监听
	 */
	private void setListener() {
		common.setListener(view,this);
	}
	public View onCreateView(LayoutInflater inflater,SharedPreferences sp,Bundle savedInstanceState){
        View view = null;
        if(attr.mSetView){
            view = setContentView();
        }
        return view;
    }
	public abstract void findViews();
	public abstract void initData();
    public void onInitAttribute(BaseAttribute attr){
        common.initAttribute(attr);
    }
	public View findViewById(int resId){
		return view.findViewById(resId);
	}
	@Override
	public void onClick(View v) {
	}
	@Override
	public boolean onLongClick(View v) {
		return false;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
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
	public void onNothingSelected(AdapterView<?> parent){
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	}
	/**
	 * 显示Page视图
	 * @param page
	 */
	public void showPage(BasePage page){
		PageManager.getInstance((ViewGroup)getView()).showPage(page);
	}
	/**
	 * 获取当前显示的Page视图
	 * @return
	 */
	public BasePage getCurrentPage(){
		return PageManager.getInstance((ViewGroup)getView()).getCurrentPage();
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
                              int urlId) throws Exception{
	}
	@Override
	public void handlerIntent(File file, int urlId) throws Exception{
	}
	@Override
	public void handlerIntent(String result, int urlId) throws Exception {
	}

    @Override
    public void handlerIntent(boolean result, int urlId) throws Exception {

    }

    @Override
    public void handleMessage(Message msg) {

    }
}
