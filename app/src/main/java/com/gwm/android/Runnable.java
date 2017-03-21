package com.gwm.android;

/**
 * 线程常用Runnable类  run方法扩展可传递参数
 * 调用setObjs()方法即可为run()设置参数
 */
public class Runnable implements java.lang.Runnable {
    private Object[] objs;
    protected void setObjs(Object... objs){
        this.objs = objs;
    }
    @Override
    public void run() {
        run(objs);
    }
    public void run(Object... objs){

    }

}
