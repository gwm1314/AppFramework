package com.gwm.base;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.gwm.R;
import com.gwm.view.ViewPagerIndicator.TabPageIndicator;

import java.util.List;

/**
 * 带有Tab标题的Activity
 */
public abstract class BaseTabActivity extends BaseActivity{
    private ViewPager pager;
    private TabPageIndicator indicator;

    @Override
    public void onInitAttribute(BaseAttribute attr) {
        attr.mSetView = false;
    }

    @Override
    public void onCreate(SharedPreferences sp, FragmentManager manager, Bundle savedInstanceState) {
        setContentView(R.layout.activity_tab_base);
        pager = (ViewPager)findViewById(R.id.pager);
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
    }

    private FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()){
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
