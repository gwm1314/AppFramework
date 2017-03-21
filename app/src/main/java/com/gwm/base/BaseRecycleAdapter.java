package com.gwm.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gwm.util.MyLogger;

import java.util.List;

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleAdapter.ViewHolder>{
    protected List<T> mData;
    private RecyclerView view;
    protected MyLogger Log;
    protected Context mContext;
    public BaseRecycleAdapter(Context mContext,List<T> mData, RecyclerView view){
        this.mData = mData;
        this.mContext = mContext;
        this.view = view;
        Log = MyLogger.kLog();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = getView(viewGroup);
        return getViewHolder(view);
    }
    public void onBindViewHolder(final ViewHolder viewHolder, final int position){
        T data = mData.get(position);
        viewHolder.setTag(data);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onItemClick(view,v,position,getItemId(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder{

        public ViewHolder(View item){
            super(item);
        }
        public abstract void setTag(T data);
    }
    public abstract View getView(ViewGroup viewGroup);
    public abstract ViewHolder getViewHolder(View view);
    public void onItemClick(RecyclerView view,View itemView,int position,long id){
    }

}
