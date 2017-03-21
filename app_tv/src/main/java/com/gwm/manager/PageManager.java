package com.gwm.manager;

import java.util.LinkedList;

import android.content.Context;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;

import com.gwm.base.BasePage;

/**
 * Page页面管理工具
 * @author gwm
 */
public class PageManager {
	private static PageManager instance;
	private static int SIZE = 50;
	private LinkedList<String> HISTORY = new LinkedList<String>();// 用户操作的历史记录
	private BasePage currentPage;// 当前正在展示
//	private Map<String, BasePager> VIEWCACHE = new LinkedHashMap<String, BasePager>(SIZE,0.75f,true);//
	// 利用手机内存空间，换应用的运行速度
	private LruCache<String, BasePage> VIEWCACHE = new LruCache<String, BasePage>(SIZE){
		@Override
		protected int sizeOf(String key, BasePage value) {
			try {
				return (int) value.newInstance().size();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
	};

	
	private PageManager(ViewGroup middle) {
		setMiddle(middle);
	}
	public static PageManager getInstance(ViewGroup middle) {
		if(instance != null)
			return instance;
		else{
			instance = new PageManager(middle);
			return instance;
		}
	}

	private ViewGroup middle;

	public void setMiddle(ViewGroup middle) {
		if(middle != null)
			this.middle = middle;
		else 
			throw new NullPointerException("");
	}

	public BasePage getPager(String tag){
		return VIEWCACHE.get(tag);
	}


	/**
	 * 获取当前正在显示的Page页面
	 * @return
	 */
	public BasePage getCurrentPage() {
		return currentPage;
	}

	/**
	 * 切换界面
	 * 
	 * @param page
	 */
	public void showPage(BasePage page){
		if(currentPage != null && currentPage == page){
			return;
		}
		String key = page.getClass().getSimpleName();
		if(VIEWCACHE.get(key) != null){
			page = VIEWCACHE.get(key);
			HISTORY.remove(key);
		}
		if(middle != null){
			// 切换界面的核心代码
			middle.removeAllViews();
			// FadeUtil.fadeOut(child1, 2000);
			View child = page.getView();
			middle.addView(child);
//			child.startAnimation(AnimationUtils.loadAnimation(getContext(),
//					R.anim.zoom_enter));
			// FadeUtil.fadeIn(child, 2000, 1000);
			VIEWCACHE.put(key, page);
			HISTORY.addFirst(key);
			currentPage = page;
		}
	}

	public Context getContext() {
		return currentPage.getContext();
	}
	/**
	 * 返回键处理
	 * @return
	 */
	public boolean goBack() {
		if (HISTORY.size() > 0) {
			if (HISTORY.size() == 1) {
				return false;
			}
			HISTORY.removeFirst();
			if (HISTORY.size() > 0 && middle != null) {
				String key = HISTORY.getFirst();
				BasePage targetUI = VIEWCACHE.get(key);
				middle.removeAllViews();
				if(targetUI != null){
					middle.addView(targetUI.getView());
					currentPage = targetUI;
					return true;
				}
			}
		}
		return false;
	}
}
