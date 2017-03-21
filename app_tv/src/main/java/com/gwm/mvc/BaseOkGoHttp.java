package com.gwm.mvc;

import android.content.Context;

import com.lzy.okgo.OkGo;

/**
 * Created by dell on 2017/3/9.
 */

public abstract class BaseOkGoHttp extends BaseHttp {

    public BaseOkGoHttp(Context context){
        super(context);
    }
    @Override
    protected void get(HttpParams params) {
        
    }

    @Override
    protected void post(HttpParams params) {

    }

    @Override
    protected abstract Object parseJson(String json, int urlId);
}
