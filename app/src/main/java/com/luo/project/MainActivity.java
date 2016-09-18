package com.luo.project;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.luo.project.view.ViewActivity;
import com.luo.project.entity.Vocabulary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button button_one;

    private String Url = "http://open.iciba.com/dsapi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv_main_text);
        button_one = (Button) findViewById(R.id.button_one);
        button_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewActivity.class));
            }
        });

        setActionBar();
        load();

    }

    private void setActionBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            statusBar();
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
//                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |  // View.SYSTEM_UI_FLAG_FULLSCREEN |
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | // View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.action_bar_layout);
            Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
            parent.setContentInsetsAbsolute(0, 0);
        }

        getSupportActionBar().setElevation(0);
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
                    textView.append(vocabulary.getContent());

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


//                Vocabulary vocabulary = (Vocabulary) ResponseParse.getInstance().getResponse(response);
//                Log.e("onSucceed", vocabulary.toString());

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


    private void statusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        int statusBarHeight = getStatusHeight(this);

        View mTopView = mContentView != null ? mContentView.getChildAt(0) : null;
        if (mTopView != null && mTopView.getLayoutParams() != null && mTopView.getLayoutParams().height == statusBarHeight) {
            mTopView.setBackgroundColor(getResources().getColor(R.color.theme_color));
            return;
        }
        if (mTopView != null) {
            ViewCompat.setFitsSystemWindows(mTopView, true);
        }

        mTopView = new View(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        mTopView.setBackgroundColor(getResources().getColor(R.color.theme_color));
        if (mContentView != null) {
            mContentView.addView(mTopView, 0, lp);
        }

    }


    public int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
