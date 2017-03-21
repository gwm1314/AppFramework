package com.gwm.mvc;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;
import android.util.SparseArray;
import android.view.KeyEvent;

import com.gwm.android.Handler;
import com.gwm.android.ThreadManager;
import com.gwm.base.BaseRunnable;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 数据库Dao层的基类,建议单例模式
 * @author gwm
 */
public abstract class BaseDao implements Handler.HandlerListener{
    private LinkedHashMap<Integer,String> sqlMap = new LinkedHashMap<Integer, String>();
    private LinkedHashMap<String,HttpObserver> observers = new LinkedHashMap<String, HttpObserver>();
	public SQLiteDatabase db;
	public BaseDao(SqliteDB helper){
		db = helper.getWritableDatabase();
	}

    /**
     * 查询数据库时用该方法
     * @param sqlId
     * @param sql
     * @param observer
     */
    public void select(final int sqlId, final String sql,HttpObserver observer){
        sqlMap.put(sqlId,sql);
        observers.put(sql,observer);
        ThreadManager.getInstance().run(new BaseRunnable(this) {
            @Override
            public void run() {
                Cursor cursor = execCursorSql(sql,null);
                Object obj = parserCursor(cursor,sql);
                sendHandlerMessage(sqlId, 0, 0, obj);
            }

            @Override
            public void run(Object... objs) {

            }
        });
    }

    /**
     * 重写该方法实现Cursor向model的转换
     * @param cursor
     * @param sql
     * @return
     */
    public abstract Object parserCursor(Cursor cursor,String sql);
    @Override
    public void handleMessage(Message msg) {
        String sql = sqlMap.remove(msg.what);
        HttpObserver observer = observers.remove(sql);
        try {
            if(msg.obj instanceof BaseEntry)
                observer.handlerIntent((BaseEntry)msg.obj,msg.what);
            else if(msg.obj instanceof String)
                observer.handlerIntent((String)msg.obj,msg.what);
            else if(msg.obj instanceof List)
                observer.handlerIntent((List)msg.obj,msg.what);
            else if(msg.obj instanceof SparseArray)
                observer.handlerIntent((SparseArray)msg.obj,msg.what);
            else if(msg.obj instanceof File)
                observer.handlerIntent((File)msg.obj,msg.what);
            else if (msg.obj instanceof Boolean)
                observer.handlerIntent((Boolean)msg.obj,msg.what);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 非查询数据库时调用该方法
     * @param sqlId
     * @param sql
     * @param observer
     */
    public void disSelect(final int sqlId,final String sql,HttpObserver observer){
        sqlMap.put(sqlId,sql);
        observers.put(sql,observer);
        ThreadManager.getInstance().run(new BaseRunnable(this) {
            @Override
            public void run() {
                try {
                    execSql(sql);
                    sendHandlerMessage(sqlId, 0, 0, true);
                }catch (SQLException e){
                    e.printStackTrace();
                    sendHandlerMessage(sqlId, 0, 0, false);
                }

            }

            @Override
            public void run(Object... objs) {

            }
        });
    }
    protected void execSql(String sql) throws SQLException{
		db.execSQL(sql);
	}
    protected void execSql(String sql,String[] selectionArgs){
		db.execSQL(sql,selectionArgs);
	}
    protected Cursor execCursorSql(String sql,String[] selectionArgs){
        return db.rawQuery(sql,selectionArgs);
    }

    /**
     * Cursor向JavaBean对象转型
     * @param clazz JavaBean的class对象
     * @param cursor cursor对象
     *               目前可支持的类型有：Date/String/int/float/double/long/short
     * @return
     */
    protected <T> List<T> getList(Class<T> clazz,Cursor cursor){
        List<T> list = new ArrayList<T>();
        while(cursor.moveToNext()){
            T obj = getEntry(clazz, cursor);
            if (obj != null)
                list.add(obj);
        }
        return list;
    }
    /**
     * Cursor向JavaBean对象转型
     * @param clazz JavaBean的class对象
     * @param cursor cursor对象
     *               目前可支持的类型有：Date/String/int/float/double/long/short/boolean
     * @return
     */
    protected <T> T getEntry(Class<T> clazz, Cursor cursor) {
        try {
            T obj = clazz.newInstance();
            Field[] fields = clazz.getFields();
            for(int i = 0 ; i < fields.length ; i++){
                if(fields[i].get(obj) instanceof Integer){
                    int value = cursor.getInt(cursor.getColumnIndex(fields[i].getName()));
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof Float){
                    float value = cursor.getFloat(cursor.getColumnIndex(fields[i].getName()));
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof Double){
                    double value = cursor.getDouble(cursor.getColumnIndex(fields[i].getName()));
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof Short){
                    double value = cursor.getShort(cursor.getColumnIndex(fields[i].getName()));
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof String){
                    String value = cursor.getString(cursor.getColumnIndex(fields[i].getName()));
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof Date){
                    String value = cursor.getString(cursor.getColumnIndex(fields[i].getName()));
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    fields[i].set(obj,format.parse(value));
                }else if (fields[i].get(obj) instanceof Long){
                    long value = cursor.getLong(cursor.getColumnIndex(fields[i].getName()));
                    fields[i].set(obj,value);
                }else if (fields[i].get(obj) instanceof Boolean){
                    int value = cursor.getInt(cursor.getColumnIndex(fields[i].getName()));
                    fields[i].set(obj,value == 0 ? true : false);
                }
            }
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}