package com.gwm.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.gwm.R;
import com.gwm.manager.PageManager;
import com.gwm.view.ViewPagerIndicator.TabPageIndicator;

import java.util.List;

/**
 * 带有Tab标题的Fragment
 */
public abstract class BaseTabFragment extends BaseFragment{
    private ViewPager pager;
    private TabPageIndicator indicator;
    @Override
    public View onCreateView(LayoutInflater inflater, SharedPreferences sp, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_base,null);
    }

    @Override
    public void findViews() {
        pager = (ViewPager)findViewById(R.id.pager);
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
    }
    private FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()){
        private String[] titles = getTiltles();
        private List<BaseFragment> frags = getFrags();
        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int i) {
            return frags.get(i);
        }
    };

    /**
     * 重写该方法返回标题栏
     * @return
     */
    public abstract String[] getTiltles();

    /**
     * 重写该方法返回每个标题栏对应的Fragment，注意与返回标题栏方法的顺序
     * @return
     */
    public abstract List<BaseFragment> getFrags();

}
