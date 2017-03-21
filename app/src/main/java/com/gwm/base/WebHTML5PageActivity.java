package com.gwm.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.gwm.R;
import com.gwm.android.PreferencesCookieStore;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 带有WebView的Activity,可定制标题栏和底部
 *      1.加载网页，带有历史记录，会自动缓存。
 *      2.支持JS <---> Java的相互调用。调用方法：
 *            在JS中通过   java.onJavaScriptToJava("方法的名称","方法的参数");
 *            说明：不支持多参数的形式，如果想传多个参数，需统一封装在一个字符串中，且只支持字符串，其他类型一概不支持
 *      3.java---->js  :  runJS("JS代码块");
 *      4.自动同步Cookie数据到下一个网页中
 */
public abstract class WebHTML5PageActivity extends BaseActivity{
    private FrameLayout footerView,titleView;
    private WebView webView;
    private ProgressBar web_progress;

    private List<String> titles = new ArrayList<String>();

    @Override
    public void onInitAttribute(BaseAttribute attr) {
        attr.mSetView = false;
    }

    @Override
    public final void onCreate(SharedPreferences sp, FragmentManager manager, Bundle savedInstanceState) {
        setContentView(R.layout.activity_html5_web);
        footerView = (FrameLayout) findViewById(R.id.frame_footer);
        titleView = (FrameLayout) findViewById(R.id.frame_title);
        webView = (WebView) findViewById(R.id.web);
        web_progress = (ProgressBar) findViewById(R.id.web_progress);
        titleView.addView(getTitleView());
        footerView.addView(getFooterView());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                web_progress.setProgress(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                CookieManager cookieManager = CookieManager.getInstance();
                String CookieStr = cookieManager.getCookie(url);
                String[] cookies = CookieStr.split("&");
                for(int i = 0 ; i < cookies.length ; i++){
                    String[] keyValue = cookies[i].split("=");
                    Cookie cookie = new BasicClientCookie(keyValue[0],keyValue[1]);
                    PreferencesCookieStore.getCookie(getApplicationContext()).addCookie(cookie);
                }

                if(TextUtils.isEmpty(getJavaScript())){
                    webView.loadUrl("javascript:"+getJavaScript());
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                web_progress.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                setTitleString(webView, title);
                titles.add(title);
            }
        });
        webView.addJavascriptInterface(this,"java");
    }

    /**
     * 重写该方法返回底部的View视图
     * @return
     */
    public abstract View getFooterView();

    /**
     * 重写该方法返回标题栏对应的视图
     * @return
     */
    public abstract View getTitleView();

    public void loadUrl(String url){
        syncCookie(getApplicationContext(),url);
        webView.loadUrl(url);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            goBack();
            return true;
        }else
            return super.onKeyDown(keyCode, event);
    }

    /**
     * 重写该方法设置标题
     * @param view
     * @param title
     */
    public abstract void setTitleString(WebView view, String title);

    /**
     * 返回键处理
     */
    public void goBack(){
        webView.goBack();
        int size = titles.size();
        if(size > 1) {
            titles.remove(size - 1);
            size--;
        }
        setTitleString(webView,titles.get(titles.size() - 1));
    }

    public abstract String getJavaScript();

    @JavascriptInterface
    public Object onJavaScriptToJava(String methodName,String str){
        try {
            Method method = WebHTML5PageActivity.this.getClass().getMethod(methodName,String.class);
            return method.invoke(WebHTML5PageActivity.this,str);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 运行JS代码
     * @param code JS代码块。只支持纯JS
     */
    public void runJS(String code){
        webView.loadUrl("javascript:"+code);
    }

    /**
     * 同步Cookie
     * @param context
     * @param url
     */
    private void syncCookie(Context context,String url){
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
        cookieSyncManager.sync();
        CookieManager cookieManager = CookieManager.getInstance();
        List<Cookie> cookies = PreferencesCookieStore.getCookie(context).getCookies();
        StringBuffer sb = new StringBuffer();
        for (int i = 0 ; i < cookies.size() ; i++){
            String key = cookies.get(i).getName();
            String value = cookies.get(i).getValue();
            sb.append(key+"="+value+"&");
        }
        sb.deleteCharAt(sb.lastIndexOf("&"));
        cookieManager.setCookie(url, sb.toString());
        CookieSyncManager.getInstance().sync();
    }
}
