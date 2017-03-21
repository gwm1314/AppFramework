package com.gwm.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


import com.gwm.util.MyLogger;

import java.util.List;
/**
 * 为了减轻代码的负担，创建BaseAdapter的子类时推荐继承该类
 * @author gwm
 * @param <D> 适配器所承载的数据类型
 */
public abstract class BaseAdapter<D> extends android.widget.BaseAdapter {
	public List<D> data;
    private BaseCommon common;
    private BaseAttribute attr;
    protected Context context;
    protected MyLogger Log;
    public void onInitAttribute(BaseAttribute attr){
        common.initAttribute(attr);
    }
	public BaseAdapter(Context context,List<D> data){
        Log = MyLogger.kLog();
        this.data = data;
        this.context = context;
        common = BaseCommon.getCommon(context);
        attr = new BaseAttribute();
        onInitAttribute(attr);
	}
	@Override
	public int getCount(){
        if (data == null || data.isEmpty())
            return 0;
		return data.size();
	}
	@Override
	public D getItem(int position){
        if (data == null || data.isEmpty())
            return null;
		return data.get(position);
	}
	@Override
	public long getItemId(int position){
		return position;
	}
	@Override
	public final View getView(int position, View convertView, ViewGroup parent){
		D itemData = getItem(position);
        ViewHolder holder = null;
        if(convertView == null){
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.setValue(itemData);
		return convertView;
	}

    public static abstract class ViewHolder<D>{
        private View itemView;
        public ViewHolder(View itemView){
            this.itemView = itemView;
            findView(itemView);
        }
        public abstract void findView(View view);
        public abstract void setValue(D itemData);
        public View findViewById(int resId){
            return itemView.findViewById(resId);
        }
    }
    public final ViewHolder<D> createViewHolder(View view){
        if (attr.mSetView){
            view = common.loadView(getClass().getName(),BaseCommon.ADAPTER);
        }
        return getViewHolder(view);
    }

    public abstract ViewHolder getViewHolder(View contentView);
}