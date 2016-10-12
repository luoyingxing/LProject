package com.luo.project.nohttp;

import android.app.Application;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

/**
 * MyNoHttp
 * <p/>
 * Created by luoyingxing on 16/10/11.
 */
public class MyNoHttp {
    private static MyNoHttp mInstance;
    private static RequestQueue mQueue = null;

    /**
     * Sets the read timeout time, Unit is a millisecond.
     */
    private static final int TIMEOUT = 10000;

    public static RequestQueue getRequestQueue() {
        return mQueue;
    }

    private MyNoHttp() {
    }

    public static synchronized MyNoHttp getInstance() {
        if (mInstance == null) {
            mInstance = new MyNoHttp();
        }
        return mInstance;
    }

    public static void initialize(Application application) {
        NoHttp.initialize(application);
        mQueue = NoHttp.newRequestQueue();
    }

    public static <T> void addRequest(Request<T> request) {
        request.setReadTimeout(TIMEOUT);
        addRequest(0, request);
    }

    public static <T> void addRequest(int what, final Request<T> request) {
        request.setReadTimeout(TIMEOUT);
        getInstance().getRequestQueue().add(what, request, new OnResponseListener<T>() {
            @Override
            public void onStart(int what) {
                if (request instanceof GsonRequest) {
                    ((GsonRequest) request).onStart(what);
                }
            }

            @Override
            public void onSucceed(int what, Response<T> response) {
                if (request instanceof GsonRequest) {
                    ((GsonRequest) request).parseResponse(response);
                    ((GsonRequest) request).onResponse(response.getHeaders(), response.getByteArray());
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                if (request instanceof GsonRequest) {
                    ((GsonRequest) request).onFailed(what, url, tag, exception, responseCode, networkMillis);
                }
            }

            @Override
            public void onFinish(int what) {
                if (request instanceof GsonRequest) {
                    ((GsonRequest) request).onFinish(what);
                }
            }
        });
    }

    public static <T> void addRequest(int what, Request<T> request, OnResponseListener<T> responseListener) {
        request.setReadTimeout(TIMEOUT);
        getInstance().getRequestQueue().add(what, request, responseListener);

    }

    public static void cancelAll() {
        getInstance().getRequestQueue().cancelAll();
    }

}