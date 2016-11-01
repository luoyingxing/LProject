package com.luo.project;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.luo.project.nohttp.MyNoHttp;
import com.luo.project.utils.FileUtils;

/**
 * com.luo.project.MainApplication
 * <p/>
 * Created by luoyingxing on 16/8/30.
 */
public class MainApplication extends Application {
    private static MainApplication mApp;

    private String mLoginCookie;

    public static Context getAppContext() {
        return mApp;
    }

    public static MainApplication getApp() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        MyNoHttp.initialize(this);
        Fresco.initialize(this);
    }

    public String getLoginCookie() {
        if (mLoginCookie == null) {
            mLoginCookie = FileUtils.getPref(Constant.PREFS_LOGIN_COOKIE);
        }
        return mLoginCookie;
    }

    public void setLoginCookie(String loginCookie) {
        this.mLoginCookie = loginCookie;
        if (loginCookie == null) {
            FileUtils.removePref(Constant.PREFS_LOGIN_COOKIE);
        } else {
            FileUtils.savePref(Constant.PREFS_LOGIN_COOKIE, loginCookie);
        }
    }
}
