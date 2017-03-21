package com.gwm.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by John on 2016/3/24.
 */
public class BaseBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<BaseBroadcastReceiver> receivers = BaseAppcation.getInstance().getReceivers();
        receivers.add(this);

    }
}
