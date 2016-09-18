package com.luo.project;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.BasicRequest;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * ApiRequest
 * <p/>
 * Created by luoyingxing on 16/8/30.
 */
public class ApiRequest<T> extends BasicRequest implements Request<T> {
    public static final String ACCEPT = "application/json";

    public ApiRequest(String url) {
        super(url);
    }

    public ApiRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    @Override
    public String getAccept() {
        return ACCEPT;
    }


//    JSONArray jsonArray = null;
//    String jsonStr = StringRequest.parseResponseString(url, responseHeaders, responseBody);
//
//    if (!TextUtils.isEmpty(jsonStr))
//            try {
//        jsonArray = new JSONArray(jsonStr);
//    } catch (JSONException e) {
//        Logger.e(e);
//    }
//    if (jsonArray == null)
//            try {
//        jsonArray = new JSONArray("[]");
//    } catch (JSONException e) {
//    }
//    return jsonArray;

    @Override
    public T parseResponse(String url, Headers responseHeaders, byte[] responseBody) {

//        if (response.data == null || response.data.length == 0) {
//            return Response.error(new ParseError());
//        }

        String jsonStr = StringRequest.parseResponseString(url, responseHeaders, responseBody);

        T obj = new Gson().fromJson(jsonStr, new TypeToken<T>() {}.getType());

        return obj;
    }

    @Override
    public void onPreResponse(int what, OnResponseListener<T> responseListener) {
        this.what = what;
        this.responseListener = responseListener;
    }

    @Override
    public int what() {
        return 0;
    }
    private int what;
    private OnResponseListener<T> responseListener;

    @Override
    public OnResponseListener<T> responseListener() {
        return responseListener;
    }

    @Override
    public void setCacheKey(String key) {

    }

    @Override
    public void setCacheMode(CacheMode cacheMode) {

    }

    @Override
    public void setRetryCount(int count) {

    }

    @Override
    public String getCacheKey() {
        return null;
    }

    @Override
    public CacheMode getCacheMode() {
        return null;
    }

    @Override
    public int getRetryCount() {
        return 0;
    }
}
