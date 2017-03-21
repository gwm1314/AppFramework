package com.gwm.webservice;

/**
 * 对应saop网络协议所编写的接口,用于获取服务器返回的状态和数据
 * @author yc
 *
 */

public interface SoapNetWorkListener {
	/**
	 * 网络请求开始
	 */
	public static final int INTELET_CONN_START = 109;
	
	/**
	 * 网络请求响应成功
	 */
	public static final int INTELET_CONN_SUCCESS = 200;
	/**
	 * 网络请求响应成功并返回list集合
	 */
	public static final int INTELET_CONNECTION_SUCCESS_RETURNLIST = 201;
	
	/**
	 * 服务器响应异常
	 */
	public static final int INTELET_CONN_FAILURE = 202;
	
	/**
	 * 连接服务器异常
	 */
	public static final int INTELET_SERVER_EXCEPTION = 203;
	
	/**
	 * 
	 * @param state	响应状态
	 * @param result	返回结果
	 */
	void OnSoapNetWorkListener(int state, Object result);
}
