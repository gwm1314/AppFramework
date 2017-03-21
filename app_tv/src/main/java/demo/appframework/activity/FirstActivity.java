package demo.appframework.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.gwm.R;
import com.gwm.base.BaseActivity;

/**
 * "在按一次退出程序"提示的使用
 * 直接调用addFirstToast()方法即可
 */
public class FirstActivity extends BaseActivity {
    @Override
    public void onCreate(SharedPreferences sp, FragmentManager manager, Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        addFirstToast();
    }
}
