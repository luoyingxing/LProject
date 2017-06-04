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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.luo.project.ViewGroup.ViewGroupActivity;
import com.luo.project.adapter.AdapterActivity;
import com.luo.project.adapter.CommonAdapter;
import com.luo.project.adapter.ViewHolder;
import com.luo.project.entity.NewsDetial;
import com.luo.project.event.EventActivity;
import com.luo.project.flow.FloatWindowService;
import com.luo.project.intent.IntentActivity;
import com.luo.project.nohttp.NoHttpActivity;
import com.luo.project.recycler.RecyclerViewActivity;
import com.luo.project.rx.RxJavaActivity;
import com.luo.project.thread.ThreadActivity;
import com.luo.project.view.ViewActivity;
import com.luo.project.wifi.WifiActivity;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ListView listView;
    private CommonAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, ViewActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, ViewGroupActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, IntentActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, NoHttpActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, com.luo.project.layout.ViewActivity.class));
                        break;
                    case 5:
                        Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                        startService(intent);
                        finish();
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this, ThreadActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(MainActivity.this, AdapterActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(MainActivity.this, WifiActivity.class));
                        break;
                    case 9:
                        startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
                        break;
                    case 10:
                        startActivity(new Intent(MainActivity.this, EventActivity.class));
                        break;
                    case 11:
                        startActivity(new Intent(MainActivity.this, RxJavaActivity.class));
                        break;
                    case 12:
                        break;
                }
            }
        });


        adapter = new CommonAdapter<String>(this, new ArrayList<String>(), R.layout.item_main_list) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.tv_item_main_list, item);
            }
        };

        listView.setAdapter(adapter);


        List<String> list = new ArrayList<>();
        list.add("RecyclerView");
        list.add("ViewGroup");
        list.add("Intent");
        list.add("NoHttp");
        list.add("View");
        list.add("flow button");
        list.add("Thread pool");
        list.add("Adapter");
        list.add("Wifi");
        list.add("RecyclerView");
        list.add("View Event");
        list.add("RxJava");

        adapter.addAll(list);

        setActionBar();

        rx();

        try {
            Class<?> clazz = Class.forName(NewsDetial.class.getName());

            Method[] methods = clazz.getMethods();

            for (Method method : methods) {
                Log.e("-- Method -- ", method.getName());
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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

    private void rx() {

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error!");
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error!");
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        };

        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });

        observable.subscribe(observer);
//            或者：
//            observable.subscribe(subscriber);


        String[] names = new String[]{"李白", "杜甫", "陶渊明", "李煜"};
        Observable.from(names)
                .subscribeOn(Schedulers.newThread()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        Log.d("Observable ", name);
                    }
                });

    }
}
