package demo.appframework.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.gwm.base.BaseRecycleAdapter;

import java.util.List;

/**
 * BaseAdapter的子类demo
 */
public class MyAdapter extends BaseRecycleAdapter<List> {
    public MyAdapter(Context mContext, List<List> mData, RecyclerView view) {
        super(mContext, mData, view);
    }

    @Override
    public View getView(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return null;
    }
    class ViewHolder extends BaseRecycleAdapter.ViewHolder<List>{

        public ViewHolder(View item) {
            super(item);
        }

        @Override
        public void setTag(List data) {

        }
    }
}
