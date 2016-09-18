package com.luo.project;

import com.yolanda.nohttp.rest.ImplRestParser;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * ApiRequestQueue
 * <p/>
 * Created by luoyingxing on 16/8/31.
 */
public class ApiRequestQueue extends RequestQueue {
    /**
     * Create request queue manager.
     *
     * @param implRestParser download the network task execution interface, where you need to implement the download tasks that have been implemented.
     * @param threadPoolSize number of thread pool.
     */
    public ApiRequestQueue(ImplRestParser implRestParser, int threadPoolSize) {
        super(implRestParser, threadPoolSize);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public <T> void add(int what, Request<T> request, OnResponseListener<T> responseListener) {
        super.add(what, request, responseListener);
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void cancelBySign(Object sign) {
        super.cancelBySign(sign);
    }

    @Override
    public void cancelAll() {
        super.cancelAll();
    }
}
