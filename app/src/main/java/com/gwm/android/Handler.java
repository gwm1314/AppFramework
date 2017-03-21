package com.gwm.android;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;

/**
 * 底层内部接口，请查看ThreadManager跟BaseRunnable类，在使用时完全察觉不到有该类的存在
 * 不允许单独使用
 *
 * @see ThreadManager
 * @see com.gwm.base.BaseRunnable
 *
 */
public class Handler extends android.os.Handler {
    private static SparseArray<HandlerListener> array = new SparseArray<HandlerListener>();
    private static Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            HandlerListener listener = array.get(msg.what);
            if(listener != null){
                listener.handleMessage(msg);
                array.remove(msg.what);
            }
        }
    };
    private Handler(Looper mainLooper) {
        super(mainLooper);
    }

    public static synchronized Handler getHandler(){
        return handler;
    }
    public void sengMessage(int what,int arg1,int arg2,Object obj,HandlerListener handlerListener){
        if (handlerListener != null)
            array.put(what,handlerListener);
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        handler.sendMessage(msg);
    }
    public void sendMessge(int what,Bundle bundle,HandlerListener handlerListener){
        if (handlerListener != null)
            array.put(what,handlerListener);
        Message msg = Message.obtain();
        msg.what = what;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    public void sendEmptyMessage(int what,HandlerListener listener){
        if (listener != null)
            array.put(what,listener);
        handler.sendEmptyMessage(what);
    }

    public void sendMessageDelayed(int what,int arg1,int arg2,Object obj,long delayMillis, HandlerListener handlerListener){
        if (handlerListener != null)
            array.put(what,handlerListener);
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        handler.sendMessageDelayed(msg,delayMillis);
    }
    public void sendMessageDelayed(int what,Bundle bundle,long delayMillis, HandlerListener handlerListener){
        if (handlerListener != null)
            array.put(what,handlerListener);
        Message msg = Message.obtain();
        msg.what = what;
        msg.setData(bundle);
        handler.sendMessageDelayed(msg,delayMillis);
    }
    public void sendEmptyMessageDelayed(int what,long delayMillis, HandlerListener handlerListener){
        if (handlerListener != null)
            array.put(what,handlerListener);
        handler.sendEmptyMessageDelayed(what,delayMillis);
    }
    public interface HandlerListener{
        void handleMessage(Message msg);
    }
}
