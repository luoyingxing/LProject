package com.luo.project.nohttp;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.luo.project.MainApplication;
import com.luo.project.entity.ApiMsg;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
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
    }

    public GsonRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
        mUrl = url;
        mRequestMethod = requestMethod;
    }

    public GsonRequest(String url, Map<String, Object> params) {
        super(url, RequestMethod.GET);
        mUrl = url;
        if (params != null) {

            if (mParamKeyValues == null) {
                mParamKeyValues = new LinkedMultiValueMap<>();
            }

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
    public RequestMethod getRequestMethod() {
        return mRequestMethod;
    }

    public GsonRequest<T> addParam(String key, Object value) {
        if (mParamKeyValues == null) {
            mParamKeyValues = new LinkedMultiValueMap<>();
        }

        mParamKeyValues.add(key, value);
        return this;
    }

    /**
     * The real .
     */
    private String buildUrl;

    @Override
    public String url() {
        if (TextUtils.isEmpty(buildUrl)) {
            StringBuilder urlBuilder = new StringBuilder(mUrl);
            if (!getRequestMethod().allowRequestBody() && mParamKeyValues.size() > 0) {
                StringBuffer paramBuffer = buildCommonParams(getParamKeyValues(), getParamsEncoding());
                if (mUrl.contains("?") && mUrl.contains("=") && paramBuffer.length() > 0)
                    urlBuilder.append("&");
                else if (paramBuffer.length() > 0)
                    urlBuilder.append("?");
                urlBuilder.append(paramBuffer);
            }
            buildUrl = urlBuilder.toString();
        }
        return buildUrl;
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


    public GsonRequest<T> get() {
        mRequestMethod = RequestMethod.GET;

        if (mParamKeyValues != null) {
            mUrl += (mUrl.contains("?") ? "&" : "?") + paramsToString(mParamKeyValues);
        }

        Log.i("GsonRequest", "Get: " + mUrl);
        return send();
    }

    public GsonRequest<T> post() {
        mRequestMethod = RequestMethod.POST;
        Log.i("GsonRequest", "Post: " + mUrl);
        Log.i("GsonRequest", "Body: " + paramsToString(mParamKeyValues));

        setRequestBody(paramsToString(mParamKeyValues));
        return send();
    }

    private GsonRequest<T> send() {
        MainApplication.mQueue.add(0, this, null);
        return this;
    }


    @Override
    public T parseResponse(String url, Headers responseHeaders, byte[] responseBody) {
        if (responseBody == null || responseBody.length == 0) {
            onFinish();
            return null;
        }

        String json = null;

        try {
            json = new String(responseBody, getParamsEncoding());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (json == null) {
            onFinish();
            return null;
        }

        //TODO 测试
//        json = "{\"errNum\":300205,\"errMsg\":\"Api does not exist\"}";

        Log.e("GsonRequest", "Response= " + json);

        if (ApiMsg.isApiMsg(json)) {
            onError(new Gson().fromJson(json, ApiMsg.class));
            onFinish();
            return null;
        } else {
            try {
                T obj = new Gson().fromJson(json, getType());
                onSuccess(obj);
                onFinish();
                return obj;

            } catch (JsonSyntaxException e) {
                onFinish();
                return null;
            }
        }
    }

    protected void onSuccess(T result) {
    }

    protected void onError(ApiMsg apiMsg) {
    }

    protected void onFinish() {
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