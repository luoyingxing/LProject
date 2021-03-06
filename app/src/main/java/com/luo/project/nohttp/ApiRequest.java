package com.luo.project.nohttp;

import com.luo.project.MainApplication;
import com.yolanda.nohttp.Headers;

import java.util.Map;

/**
 * ApiRequest
 * <p/>
 * Created by luoyingxing on 16/10/11.
 */
public class ApiRequest<T> extends GsonRequest<T> {

    public ApiRequest(String url) {
        super(url);
    }

    public ApiRequest(String url, boolean needApiKey) {
        this(url, null, needApiKey);
    }

    public ApiRequest(String url, Map<String, Object> params, boolean needApiKey) {
        super(url, params);
        if (needApiKey) {
            addHeader("apikey", "87dd5b309735c00e1cc37bb52c97b7a0");
        }
    }

    @Override
    public Headers headers() {
        String cookie = MainApplication.getApp().getLoginCookie();
        if (cookie == null) {
            return super.headers();
        }

        Headers headers = super.headers();
        headers.add("Cookie", cookie);
        return headers;
    }
}