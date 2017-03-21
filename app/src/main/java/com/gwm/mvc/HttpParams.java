package com.gwm.mvc;

import android.os.Bundle;

/**
 * Created by John on 2016/4/14.
 */
public class HttpParams {
    public int urlId;  //url的id url统一在http_string.xml文件中定义
    public HttpObserver observer; //控制器，http请求会将服务器返回的数据传递给该接口
    public Bundle params;  //http请求的参数
    public Object result;  //服务器返回的结果
    public int way;  //请求方式

    public String target; //文件的存放目录，只有下载文件时才有用

    public static final int GET_WAY = 1;
    public static final int POST_WAY = 2;
    public static final int DOWNLOAD_WAY = 3;
}
