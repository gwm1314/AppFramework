package com.gwm.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.gwm.android.Handler;
import com.gwm.util.MyLogger;

import java.lang.reflect.Field;

/**
 * Base框架公共部分的提取
 * 自动查找UI布局layout文件和自动添加监听事件
 */
public class BaseCommon {
    public static final String ACTIVITY = "Activity";
    public static final String FRAGMENT = "Fragment";
    public static final String PAGE = "Page";
    public static final String ADAPTER = "Adapter";
    private static BaseCommon common;
    private Context context;
    private BaseCommon(Context context){
        this.context = context;
    }
    public static BaseCommon getCommon(Context context){
        if (common == null)
            common = new BaseCommon(context);
        return common;
    }
    public void initAttribute(BaseAttribute attr){
        attr.mSetView = true;
    }
    public View loadView(String className,String classLogo) {
        int nIndex = className.lastIndexOf(".");
        MyLogger.kLog().i("nIndex="+nIndex+",className="+className);
        if (nIndex != -1) {
            final String cName = className.substring(nIndex + 1);
            String strResourceName = classLogo.toLowerCase()
                    + cName.replaceFirst(classLogo, "");
            for (char num = 'A' ;num < 'A' + 26 ; num++){
                strResourceName = strResourceName.replaceAll(String.valueOf(num),String.valueOf("_"+num));
            }
            strResourceName = strResourceName.toLowerCase();
            MyLogger.kLog().i("MainLayout:"+strResourceName);
            final int nLayoutId = context.getResources().getIdentifier(
                    strResourceName, "layout", context.getPackageName());
            if (nLayoutId != 0) {
                return LayoutInflater.from(context).inflate(nLayoutId, null, false);
            }
        }
        return null;
    }
    public void setListener(View rootView,EventListener listener){
        try {
            Field field = listener.getClass().getField("ids");
            int[] ids = (int[]) field.get(this);
            if (ids != null && ids.length > 0)
                for (int id : ids) {
                    View view = rootView.findViewById(id);
                    if(view instanceof CompoundButton){
                        ((CompoundButton)view).setOnCheckedChangeListener(listener);
                        continue;
                    }
                    if(view instanceof AdapterView) {
                        if(view instanceof Spinner) {
                            ((Spinner)view).setOnItemSelectedListener(listener);
                            continue;
                        }
                        ((AdapterView<?>) view).setOnItemClickListener(listener);
                        ((AdapterView<?>) view).setOnItemLongClickListener(listener);
                        continue;
                    }
                    view.setOnClickListener(listener);
                    view.setOnLongClickListener(listener);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static interface EventListener extends View.OnClickListener,
            AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
            View.OnLongClickListener, AdapterView.OnItemSelectedListener,CompoundButton.OnCheckedChangeListener{
    }
}
