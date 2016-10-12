package com.luo.project.nohttp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.luo.project.entity.ApiMsg;
import com.luo.project.entity.ErrMsg;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.RestRequest;
import com.yolanda.nohttp.tools.LinkedMultiValueMap;
import com.yolanda.nohttp.tools.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * GsonRequest
 * <p/>
 * Created by luoyingxing on 16/10/10.
 */
public class GsonRequest<T> extends RestRequest<T> {
    /**
     * TAG
     */
    private static final String TAG = "GsonRequest";
    /**
     * ACCEPT
     */
    protected static final String ACCEPT = "application/json";
    /**
     * Request method.
     */
    private RequestMethod mRequestMethod;
    /**
     * URL of this request.
     */
    private String mUrl;
    /**
     * Param collection.
     */
    private MultiValueMap<String, Object> mParamKeyValues;

    public GsonRequest(String url) {
        super(url);
        mUrl = url;
        mParamKeyValues = new LinkedMultiValueMap<>();
    }

    public GsonRequest(String url, Map<String, Object> params) {
        super(url, RequestMethod.GET);
        mUrl = url;
        mParamKeyValues = new LinkedMultiValueMap<>();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                mParamKeyValues.add(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public String getAccept() {
        return ACCEPT;
    }

    @Override
    public String url() {
        return mUrl;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return mRequestMethod;
    }

    @Override
    public MultiValueMap<String, Object> getParamKeyValues() {
        return mParamKeyValues;
    }

    private String paramsToString(MultiValueMap<String, Object> params) {
        if (params == null) {
            return null;
        }

        StringBuilder encodedParams = new StringBuilder();

        for (String key : params.keySet()) {
            encodedParams.append(key);
            encodedParams.append('=');
            encodedParams.append(params.getValues(key).get(0));
            encodedParams.append('&');
        }

        if (encodedParams.length() > 0) {
            encodedParams = encodedParams.deleteCharAt(encodedParams.length() - 1);
        }
        return encodedParams.toString();
    }

    public GsonRequest<T> addHeaders(String key, String value) {
        addHeader(key, value);
        return this;
    }

    public GsonRequest<T> addParam(String key, Object value) {
        if (mParamKeyValues == null) {
            mParamKeyValues = new LinkedMultiValueMap<>();
        }
        mParamKeyValues.add(key, value);
        return this;
    }

    public GsonRequest<T> get() {
        mRequestMethod = RequestMethod.GET;

        if (mParamKeyValues != null) {
            mUrl = mParamKeyValues.keySet().size() == 0 ? mUrl : mUrl + (mUrl.contains("?") ? "&" : "?") + paramsToString(mParamKeyValues);
        }

        Log.i(TAG, "Get: " + mUrl);
        return send();
    }

    public GsonRequest<T> post() {
        mRequestMethod = RequestMethod.POST;
        Log.i(TAG, "Post: " + mUrl);
        Log.i(TAG, "Body: " + paramsToString(mParamKeyValues));

        setRequestBody(paramsToString(mParamKeyValues));
        return send();
    }

    private GsonRequest<T> send() {
        MyNoHttp.addRequest(this);
        return this;
    }

    @Override
    public T parseResponse(String url, Headers responseHeaders, byte[] responseBody) {
        if (responseBody == null || responseBody.length == 0) {
//            onFinish();
            return null;
        }

        String json = null;

        try {
            json = new String(responseBody, getParamsEncoding());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (json == null) {
//            onFinish();
            return null;
        }

        onResponse(responseHeaders, responseBody);
        Log.e(TAG, "Response= " + json);

        if (ApiMsg.isApiMsg(json)) {
//            onError(new Gson().fromJson(json, ApiMsg.class));
//            onFinish();
            return null;
        } else {
            try {
                T obj = new Gson().fromJson(json, getType());
//                onSuccess(obj);
//                onFinish();
                return obj;

            } catch (JsonSyntaxException e) {
//                onError(ErrMsg.parseError());
//                onFinish();
//                return null;
            }
        }

        return null;
    }

    /**
     * 结果回调方法
     *
     * @param response 服务器返回的结果
     * @return 实体类型
     */
    public void parseResponse(Response<T> response) {
        Headers responseHeaders = response.getHeaders();
        byte[] responseBody = response.getByteArray();

        if (responseBody == null || responseBody.length == 0) {
            onFinish(0);
        } else {
            String json = null;

            try {
                json = new String(responseBody, getParamsEncoding());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (json == null) {
                onFinish(0);
            } else {
                onResponse(responseHeaders, responseBody);
                Log.e(TAG, "Response= " + json);

                if (ApiMsg.isApiMsg(json)) {
                    onError(new Gson().fromJson(json, ApiMsg.class));
                    onFinish(0);
                } else {
                    try {
                        T obj = new Gson().fromJson(json, getType());
                        onSuccess(obj);
                        onFinish(0);
                    } catch (JsonSyntaxException e) {
                        onError(ErrMsg.parseError());
                        onFinish(0);
                    }
                }
            }
        }
    }

    protected void onStart(int what) {
    }

    protected void onSuccess(T result) {
    }

    protected void onError(ApiMsg apiMsg) {
    }

    protected void onFinish(int what) {
    }

    protected void onResponse(Headers headers, byte[] responseBody) {
    }

    protected void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
    }

    @SuppressWarnings("unchecked")
    protected Type getType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType genericSuperclassType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = genericSuperclassType.getActualTypeArguments();
        return actualTypeArguments[0];
    }
}