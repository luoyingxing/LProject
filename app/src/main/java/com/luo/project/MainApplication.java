package com.luo.project;

import android.app.Application;

import com.luo.project.nohttp.MyNoHttp;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * com.luo.project.MainApplication
 * <p/>
 * Created by luoyingxing on 16/8/30.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化NoHttp
        MyNoHttp.initialize(this);
    }
}
