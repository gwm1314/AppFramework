package com.gwm.base;

import android.os.Bundle;

import com.gwm.android.*;

/**
 * 该类常与ThreadManager结合使用，用于实现需要Handler+Thread的一些耗时操作
 */
public abstract class BaseRunnable extends com.gwm.android.Runnable {
    private Handler handler = Handler.getHandler();
    protected Handler.HandlerListener listener;
    protected BaseRunnable(){}

    protected void setListener(Handler.HandlerListener listener){
        this.listener = listener;
    }
    public BaseRunnable(Handler.HandlerListener listener){
        this.listener = listener;
    }
    /**************************对Hanler的封装********************************************/
    public void sendHandlerMessage(int what, int arg1, int arg2, Object obj){
        handler.sengMessage(what, arg1, arg2, obj, listener);
    }

    public void sendHandlerMessage(int what,Bundle bundle){
        handler.sendMessge(what, bundle, listener);
    }
    public void sendHandlerMessage(int what){
        handler.sendEmptyMessage(what, listener);
    }

    public void sendHandlerMessageDelayed(int what,int arg1,int arg2,Object obj,long delayMillis){
        handler.sendMessageDelayed(what,arg1,arg2,obj,delayMillis,listener);
    }
    public void sendHandlerMessageDelayed(int what,Bundle bundle,long delayMillis){
        handler.sendMessageDelayed(what,bundle,delayMillis,listener);
    }
    public void sendHandlerMessageDelayed(int what,long delayMillis){
        handler.sendEmptyMessageDelayed(what, delayMillis, listener);
    }
}
