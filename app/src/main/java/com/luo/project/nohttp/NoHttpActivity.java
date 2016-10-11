package com.luo.project.nohttp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.luo.project.MainApplication;
import com.luo.project.R;
import com.luo.project.entity.ApiMsg;
import com.luo.project.entity.CollegeNotify;
import com.luo.project.entity.Girls;
import com.luo.project.entity.News;
import com.luo.project.entity.NewsDetial;
import com.luo.project.entity.Vocabulary;
import com.luo.project.utils.ResponseParse;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.RestRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NoHttpActivity
 * <p/>
 * Created by luoyingxing on 16/10/8.
 */
public class NoHttpActivity extends AppCompatActivity {

    private String Url = "http://open.iciba.com/dsapi";

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nohttp);

        setActionBar();

        textView = (TextView) findViewById(R.id.tv_http);

//        load();

//        requestString();
//
//        request();
//
//        requestRenWoXue();

        requestImage();
    }


    private String path = "http://apis.baidu.com/showapi_open_bus/channel_news/channel_news";

    private String apiKey = "87dd5b309735c00e1cc37bb52c97b7a0";

    private void requestString() {

        new GsonRequest<News>(path) {

            @Override
            protected void onSuccess(News result) {
                super.onSuccess(result);

                Log.e(" -onSuccess- ", "onSuccess");

                for (int i = 0; i < result.getShowapi_res_body().getChannelList().size(); i++) {
                    Log.e(" -onSuccess- ", result.getShowapi_res_body().getChannelList().get(i).getName());
                }


            }

            @Override
            protected void onError(ApiMsg apiMsg) {
                super.onError(apiMsg);
                Log.e("onError", apiMsg.toString());
            }


            @Override
            protected void onFinish() {
                super.onFinish();
                Log.e("onFinish", " -- onFinish -- ");
            }

        }.addHeaders("apikey", apiKey)
                .get();

    }

    private void request() {

        String urlRequest = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";
//        String urlRequest = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news?channelId=5572a109b3cdc86cf39001db";

        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "5572a108b3cdc86cf39001d6");

        new GsonRequest<NewsDetial>(urlRequest) {

            @Override
            protected void onSuccess(NewsDetial result) {
                super.onSuccess(result);

                Log.e(" -onSuccess- ", "onSuccess" + result.toString());


            }

            @Override
            protected void onError(ApiMsg apiMsg) {
                super.onError(apiMsg);
                Log.e("onError", apiMsg.toString());
            }


            @Override
            protected void onFinish() {
                super.onFinish();
                Log.e("onFinish", " -- onFinish -- ");
            }

        }.addHeaders("apikey", apiKey)
                .addParam("channelId", "5572a108b3cdc86cf39001d6")
////                .addParam("channelName", "国内最新")
////                .addParam("page", 1)
////                .addParam("needContent", 0)
////                .addParam("needHtml", 0)
                .get();

    }

    private void requestRenWoXue() {
        String path = "http://112.74.129.165:8001/renwoxue/school/notice";
        new GsonRequest<List<CollegeNotify>>(path) {

            @Override
            protected void onSuccess(List<CollegeNotify> result) {
                super.onSuccess(result);

                Log.e(" -onSuccess- ", "onSuccess" + "返回数量" + result.size());


            }

            @Override
            protected void onError(ApiMsg apiMsg) {
                super.onError(apiMsg);
                Log.e("onError", apiMsg.toString());
            }


            @Override
            protected void onFinish() {
                super.onFinish();
                Log.e("onFinish", " -- onFinish -- ");
            }

        }.addParam("pageNumber", 2)
                .addParam("pageSize", 4)
                .post();

    }

    private void requestImage() {
        String path = "http://apis.baidu.com/txapi/mvtp/meinv";
        new GsonRequest<Girls>(path) {

            @Override
            protected void onSuccess(Girls result) {
                super.onSuccess(result);

                for (int i = 0; i < result.getNewslist().size(); i++) {
                    Log.e(" -onSuccess- ", result.getNewslist().get(i).getPicUrl());
                }

            }

            @Override
            protected void onError(ApiMsg apiMsg) {
                super.onError(apiMsg);
                Log.e("onError", apiMsg.toString());
            }


            @Override
            protected void onFinish() {
                super.onFinish();
                Log.e("onFinish", "onFinish");
            }

            @Override
            protected void onResponse(Headers headers, byte[] responseBody) {
                super.onResponse(headers, responseBody);

                for (String str : headers.keySet()) {
                    if (str.equalsIgnoreCase(Headers.HEAD_KEY_SET_COOKIE)) {
                        Log.e("onSuccess", " - cookie - " + headers.getValues(str).get(0));
                    }
                }

                Log.e("onFinish", "onFinish");
            }
        }.addHeaders("apikey", apiKey)
                .addCookie("8asd818iahsd87189ei1h8asdy81827")
                .addParam("num", 6)
                .get();

    }


    private void load() {
        Request request = NoHttp.createJsonObjectRequest(Url, RequestMethod.GET);
        MyNoHttp.addRequest(1, request, new OnResponseListener() {
            @Override
            public void onStart(int what) {
                Log.e("MainActivity", "onStart");
            }

            @Override
            public void onSucceed(int what, Response response) {
                Log.e("MainActivity", "onSucceed");
                try {
                    String respon = new String(response.getByteArray(), "utf-8");
                    Log.e("MainActivity", "response=" + respon);

//                    ApiMsg apiMsg = new Gson().fromJson(json, ApiMsg.class);

                    Vocabulary vocabulary = new Gson().fromJson(respon, Vocabulary.class);

//                    List<Introduce> introduceLits = (List<Introduce>) new Gson().fromJson(respon, Introduce.class);

//                    for (int i = 0; i < vocabulary.size(); i++) {
//                        Log.e("onSucceed", "标题：" + introduceLits.get(i).getTitle() + "，时间：" + introduceLits.get(i).getCreateTime());
//                    }

                    Log.e("onSucceed", vocabulary.toString());
                    textView.setText(vocabulary.toString());

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                Vocabulary vocabulary = (Vocabulary) ResponseParse.getInstance().getResponse(Vocabulary.class, response);

//                Vocabulary vocabulary = (Vocabulary) new ResponseParse<Vocabulary>().getResponse(response);

                Log.e("LuoYingXing", vocabulary.toString());
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                Log.e("MainActivity", "onFailed");
            }

            @Override
            public void onFinish(int what) {
                Log.e("MainActivity", "onFinish");
            }
        });
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.action_bar_layout);
            Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
            parent.setContentInsetsAbsolute(0, 0);
        }

        getSupportActionBar().setElevation(0);
    }
}
