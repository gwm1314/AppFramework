package com.gwm.base;

/**
 * Activity/Fragment/Page的一些配置。
 * 1.layout文件可以与Activity进行动态绑定(例如：MainActivity --- activity_main   AutoSearchActivity --- activity_auto_search)
 */
public class BaseAttribute {
    public boolean mSetView;  //是否动态绑定视图  该技术有影响性能

    public String titleText; //标题栏文字

}
