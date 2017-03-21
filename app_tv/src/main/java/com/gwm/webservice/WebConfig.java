package com.gwm.webservice;
/**
 * 
 * @author yc
 *
 */
public class WebConfig {
	public final static String WEB_URL = "http://service.android.com/";
	public final static String WEB_NAMESPACE = "http://service.android.com/";
	public final static int CONN_TIME_OUT = 1000 * 10;	//请求超时时长
	public final static String SUBMIT_MSG = "提交请求中...";
	public final static String LOADING_MSG = "数据加载中...";
	//分页大小
	public final static int LOAD_ITEM_COUNT = 15;
	
	public final static long LAST_OPERATION_TIME = 10 * 60 * 1000;	//最长有效超时操作时间
	
}
