package com.luo.project.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.rest.Response;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * ResponseParse
 * <p/>
 * Created by luoyingxing on 16/9/12.
 */
public class ResponseParse{

    public static ResponseParse getInstance() {
        return new ResponseParse();
    }

    public Object getResponse(Class<?> tClass , Response response) {
        String data = null;

        try {
            data = new String(response.getByteArray(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.i("ResponseParse", "Response=" + data);


        return new Gson().fromJson(data, tClass);
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
