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

        requestString();

    }

    private String path = "http://112.74.129.165:8001/renwoxue/school/notice";

    private void requestString() {
        Map<String, Object> map = new HashMap<>();

        map.put("pageNumber", 1);
        map.put("pageSize", 4);

        new GsonRequest<List<CollegeNotify>>(path, map) {

            @Override
            protected void onSuccess(List<CollegeNotify> result) {
                super.onSuccess(result);

                Log.e(" -onSuccess- ", "onSuccess");

                for (int i = 0; i < result.size(); i++) {
                    Log.e(" -onSuccess- ", result.get(i).getTitle());
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

        }
                .post();


    }


    private void load() {
        Request request = NoHttp.createJsonObjectRequest(Url, RequestMethod.GET);

        MainApplication.mQueue.add(1, request, new OnResponseListener() {
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
