package com.gwm.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hukui on 15-12-8.
 */
public class AppBroadcastReceiver extends BroadcastReceiver {
    private static Map<String,PkgInstallListener> maps;
    static {
        maps = new HashMap<String,PkgInstallListener>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {     // install
            String packageName = intent.getDataString();
            PkgInstallListener listener = maps.get(packageName);
            if (listener != null){
                listener.intall(packageName);
                maps.remove(packageName);
            }
        }

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {   // uninstall
            String packageName = intent.getDataString();
            PkgInstallListener listener = maps.get(packageName);
            if (listener != null){
                listener.unintall(packageName);
                maps.remove(packageName);
            }
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_RESTARTED)) {   // uninstall
            String packageName = intent.getDataString();
            PkgInstallListener listener = maps.get(packageName);
            if (listener != null){
                listener.intall(packageName);
                maps.remove(packageName);
            }
        }
    }
    public static void setListener(String pkgName,PkgInstallListener listener){
        maps.put(pkgName,listener);
    }
    public static interface PkgInstallListener{
        public void intall(String pkgName);
        public void unintall(String pkgName);
    }
}
