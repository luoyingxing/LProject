package com.luo.project;

import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;

/**
 * OnLResponseListener
 * <p/>
 * Created by luoyingxing on 16/8/31.
 */
public interface OnLResponseListener<T> extends OnResponseListener {

    @Override
    void onStart(int what);

    @Override
    void onSucceed(int what, Response response);

    void onSucceed(T response);

    @Override
    void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis);

    @Override
    void onFinish(int what);
}
