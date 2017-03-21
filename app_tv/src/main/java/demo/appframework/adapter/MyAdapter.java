package demo.appframework.adapter;

import android.content.Context;
import android.view.View;

import com.gwm.R;
import com.gwm.base.BaseAdapter;

import java.util.List;

/**
 * BaseAdapter的子类demo
 */
public class MyAdapter extends BaseAdapter<String> {
    public MyAdapter(Context context,List<String> strs){
        super(context,strs);
    }
    @Override
    public BaseAdapter.ViewHolder<String> getViewHolder(View view) {
        if(view == null){
            view = View.inflate(context, R.layout.activity_main,null);
        }
        return new MyViewHolder(view);
    }
    private static class MyViewHolder extends BaseAdapter.ViewHolder<String>{
        public MyViewHolder(View itemView){
            super(itemView);
        }
        @Override
        public void findView(View view) {
            //进行findViewById操作
        }

        @Override
        public void setValue(String itemData) {
            //在此处进行数据的展示操作
        }
    }
}
