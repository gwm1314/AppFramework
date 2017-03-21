package com.gwm.android;

import android.os.Bundle;

/**
 * Thread+Handler的封装(不推荐使用，请看ThreadManager.java)
 * @see ThreadManager
 */
public class Thread extends java.lang.Thread{
    private Handler handler = Handler.getHandler();
    private Handler.HandlerListener listener;

    /**
     * 监听线程的状态，设置Handler的处理对象
     * @param listener
     */
    private void setHandlerListener(Handler.HandlerListener listener){
        this.listener = listener;
    }
    public void sendHandleMessage(int what,int arg1,int arg2,Object obj){
        handler.sengMessage(what, arg1, arg2, obj,listener);

    }

    public void sendHandlerMessage(int what,Bundle bundle){
        handler.sendMessge(what, bundle,listener);
    }
    public void sendHandlerMessage(int what){
        handler.sendEmptyMessage(what,listener);
    }

    public void sendHandlerMessageDelayed(int what,int arg1,int arg2,Object obj,long delayMillis){
        handler.sendMessageDelayed(what,arg1,arg2,obj,delayMillis,listener);
    }
    public void sendHandlerMessageDelayed(int what,Bundle bundle,long delayMillis){
        handler.sendMessageDelayed(what,bundle,delayMillis,listener);
    }
    public void sendHandlerMessageDelayed(int what,long delayMillis){
        handler.sendEmptyMessageDelayed(what,delayMillis,listener);
    }
}
