package com.gwm.mvc;

import android.content.Context;
import android.util.SparseArray;

import com.gwm.android.Handler;
import com.gwm.android.ThreadManager;
import com.gwm.base.BaseRunnable;
import com.gwm.util.MyLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 框架底层扩展支持类，用于兼容各种Http请求框架，
 */
public abstract class BaseHttp implements Handler.HandlerListener{
    protected Context context;
    protected BaseHttp(Context context) {
        this.context = context;
    }
    protected OnResultListener result = new OnResultListener() {
        @Override
        public void onGetResult(final HttpParams url, final int iError){
            ThreadManager.getInstance().run(new BaseRunnable(BaseHttp.this) {
                @Override
                public void run() {
                    if(iError == 200) {
                        url.result = parseJson(url.result.toString(), url.urlId);
                        sendHandlerMessage(url.urlId,0,0,url);
                    }else{
                        onFinsh(url,iError);
                    }
                }

                @Override
                public void run(Object... objs) {

                }
            });

        }
    };
    public void notifyObserver(Object obj,HttpObserver observer,int urlId) throws Exception{
        if(obj instanceof BaseEntry)
            observer.handlerIntent((BaseEntry)obj,urlId);
        else if(obj instanceof String)
            observer.handlerIntent((String)obj,urlId);
        else if(obj instanceof List)
            observer.handlerIntent((List)obj,urlId);
        else if(obj instanceof SparseArray)
            observer.handlerIntent((SparseArray)obj,urlId);
        else if(obj instanceof File)
            observer.handlerIntent((File)obj,urlId);
    }
    public void onFinsh(HttpParams params, int iError){

    }
    protected abstract void get(HttpParams params);
    protected abstract void post(HttpParams params);
    protected abstract Object parseJson(String json,int urlId);

    protected <T> T parseJson(String json,Class<T> clazz){
        try {
            JSONObject jsonObject = new JSONObject(json);
            T obj = clazz.newInstance();
            Field[] fields = clazz.getFields();
            for(int i = 0 ; i < fields.length ; i++){
                if(fields[i].get(obj) instanceof Integer){
                    int value = jsonObject.getInt(fields[i].getName());
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof Double){
                    double value = jsonObject.getDouble(fields[i].getName());
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof String){
                    String value = jsonObject.getString(fields[i].getName());
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof Date){
                    String value = jsonObject.getString(fields[i].getName());
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    fields[i].set(obj,format.parse(value));
                }else if (fields[i].get(obj) instanceof Long){
                    long value = jsonObject.getLong(fields[i].getName());
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof Boolean){
                    boolean value = jsonObject.getBoolean(fields[i].getName());
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof List){
                    Type type = fields[i].getGenericType();
                    Class clazz1 = (Class) ((ParameterizedType)type).getActualTypeArguments()[0];
                    List value = parseJson2List(jsonObject.getJSONArray(fields[i].getName()).toString(), clazz1);
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof Object){
                    Object value = parseJson(jsonObject.getJSONObject(fields[i].getName()).toString(),fields[i].get(obj).getClass());
                    fields[i].set(obj,value);
                }
            }
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    protected <T> List<T> parseJson2List(String json,Class<T> clazz){
        try {
            JSONArray array = new JSONArray(json);
            List<T> list = new ArrayList<T>();
            for(int i = 0 ; i < array.length() ; i++){
                JSONObject obj = array.getJSONObject(i);
                T bean = parseJson(obj.toString(),clazz);
                if(bean != null)
                    list.add(bean);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void handleMessage(android.os.Message msg){
        try {
            HttpParams params = (HttpParams)msg.obj;
            notifyObserver(params.result, params.observer, params.urlId);
        }catch (Exception e){
            MyLogger.kLog().i(e);
        }

    }
}
