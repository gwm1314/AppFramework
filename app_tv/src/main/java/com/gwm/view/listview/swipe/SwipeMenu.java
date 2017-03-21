package com.gwm.view.listview.swipe;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author baoyz
 * @date 2014-8-23
 *
 */
public class SwipeMenu {

    private Context mContext;
    private List<SwipeMenuItem> mItems;

    public SwipeMenu(Context context) {
        mContext = context;
        mItems = new ArrayList<SwipeMenuItem>();
    }

    public Context getContext() {
        return mContext;
    }

    // 添加菜单元素
    public void addMenuItem(SwipeMenuItem item) {
        mItems.add(item);
    }

    // 删除菜单元素
    public void removeMenuItem(SwipeMenuItem item) {
        mItems.remove(item);
    }

    // 获得菜单所有元素
    public List<SwipeMenuItem> getMenuItems() {
        return mItems;
    }

    // 获得指定的位置的菜单元素
    public SwipeMenuItem getMenuItem(int index) {
        return mItems.get(index);
    }
}
