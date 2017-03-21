package com.gwm.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.gwm.R;
import java.util.List;

/**
 * 欢迎页面基类
 */
public abstract class BaseSplashActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    private LinearLayout ll_ivs;
    private ViewPager pager;

    private List<View> frags;
    private ImageView[] ivs;
    private int pressedId,normalId;

    @Override
    public void onInitAttribute(BaseAttribute attr) {
        super.onInitAttribute(attr);
        attr.mSetView = false;
    }

    @Override
    public final void onCreate(SharedPreferences sp, FragmentManager manager, Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash_base);
        ll_ivs = (LinearLayout) findViewById(R.id.ll_ivs);
        pager = (ViewPager) findViewById(R.id.pager);
        onCreate(sp, manager);
        initData();
    }

    public void initData() {
        frags = getViews();
        int size = frags.size();
        ivs = new ImageView[size];
        for(int i = 0 ; i < size ; i++){
            ImageView iv = new ImageView(BaseAppcation.getInstance().getCrrentActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    10, 10);
            layoutParams.setMargins(5, 0, 5, 0);
            iv.setLayoutParams(layoutParams);
            ivs[i] = iv;
            // 将imageviews添加到小圆点视图组
            ll_ivs.addView(iv);
        }
        ivs[0].setBackgroundResource(pressedId);
        PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return frags.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(frags.get(position));
                return frags.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(frags.get(position));
            }
        };
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < ivs.length; i++) {
            ivs[position].setBackgroundResource(pressedId);
            // 不是当前选中的page，其小圆点设置为未选中的状态
            if (position != i) {
                ivs[i].setBackgroundResource(normalId);
            }
        }
    }

    /**
     * 设置导航点的样式，
     * @param pressedId 选中时对应的点的样式图
     * @param normalId  没有选中的对应的样式图
     */
    public void setPointStyle(int pressedId,int normalId){
        this.normalId = normalId;
        this.pressedId = pressedId;
    }

    /**
     * 返回菜单栏中每一个菜单页面对应的Fragment
     * @return
     */
    public abstract List<View> getViews();

    public void onCreate(SharedPreferences sp,FragmentManager manager){}
}
