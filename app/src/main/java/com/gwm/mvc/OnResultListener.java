package com.gwm.mvc;


/**
 * 网络数据处理
 * @author Administrator
 * 
 */
public interface OnResultListener {
	/**
	 * 结果处理
	 * @param iError
	 *            :结果的状态码
	 */
	public void onGetResult(HttpParams url, int iError) throws Exception;
}
