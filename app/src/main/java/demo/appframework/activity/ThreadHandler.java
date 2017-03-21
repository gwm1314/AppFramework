package demo.appframework.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.gwm.android.ThreadManager;
import com.gwm.R;
import com.gwm.base.BaseActivity;
import com.gwm.base.BaseRunnable;

/**
 * Created by hukui on 15-12-10.
 * ThreadManager+BaseRunable+sendHandlerMessage()
 */
public class ThreadHandler extends BaseActivity {
    @Override
    public void onCreate(SharedPreferences sp, FragmentManager manager, Bundle savedInstanceState) {
        //单次耗时任务
        ThreadManager.getInstance().run(new BaseRunnable(this) {
            @Override
            public void run() {
                //TODO 在此处进行耗时操作

                //发送Handler消息，完成子线程与主线程的交互
                sendHandlerMessage(R.integer.HANDLER_MESSAGE, 0, 0, new Object());
            }
        });
        //多次循环耗时任务
        ThreadManager.getInstance().run(new BaseRunnable(this) {
            @Override
            public void run() {
                //TODO 在此处进行耗时操作

                //发送Handler消息，完成子线程与主线程的交互
                sendHandlerMessage(R.integer.HANDLER_MESSAGE, 0, 0, new Object());
            }
        },0,500);
    }

    /**
     * 重写该方法处理子线程发送的Handler消息
     * @param msg
     */
    @Override
    public void handleMessage(Message msg) {
        if(msg.what == R.integer.HANDLER_MESSAGE){

        }
    }
}
