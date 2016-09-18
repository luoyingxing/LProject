package com.luo.project;

import android.app.Application;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * com.luo.project.MainApplication
 * <p/>
 * Created by luoyingxing on 16/8/30.
 */
public class MainApplication extends Application {
    public static RequestQueue mQueue = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化NoHttp
        NoHttp.init(this);
        //初始化请求队列
        mQueue = NoHttp.newRequestQueue();
    }
}
