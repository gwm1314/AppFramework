package com.gwm.util;

import android.content.Context;

import com.gwm.R;

/**
 * 用于获取在sp_key_string.xml、handler_final.xml、http_string.xml中定义的常量
 */
public class ContanstUtil {
    private static ContanstUtil instance = null;
    protected Context context;
    private ContanstUtil(Context context){
        this.context = context;
    }
    public static ContanstUtil getInstance(Context context){
        if (instance == null)
            instance = new ContanstUtil(context);
        return instance;
    }
    public String getSpKey(int resId){
        return context.getResources().getString(resId);
    }
    public String getHttpUrl(int resId){
        String procal = context.getResources().getString(R.string.PROCAL);
        String IP = context.getResources().getString(R.string.IP);
        int port = context.getResources().getInteger(R.integer.PORT);
        String name = context.getResources().getString(resId);
        return procal + IP + ":" + port + name;
    }


}
