package com.luo.project;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.luo.project.ViewGroup.ViewGroupActivity;
import com.luo.project.adapter.AdapterActivity;
import com.luo.project.adapter.CommonAdapter;
import com.luo.project.adapter.ViewHolder;
import com.luo.project.aidl.AIDLActivity;
import com.luo.project.animator.AnimatorActivity;
import com.luo.project.contentProvider.ContentProviderActivity;
import com.luo.project.event.EventActivity;
import com.luo.project.flow.FloatWindowService;
import com.luo.project.gallery.GalleryActivity;
import com.luo.project.intent.IntentActivity;
import com.luo.project.nohttp.NoHttpActivity;
import com.luo.project.recycler.RecyclerViewActivity;
import com.luo.project.rx.RxJavaActivity;
import com.luo.project.thread.ThreadActivity;
import com.luo.project.view.ViewActivity;
import com.luo.project.wifi.WifiActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private GridView gridView;
    private CommonAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar();

        gridView = (GridView) findViewById(R.id.list_view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        startActivity(new Intent(MainActivity.this, ContentProviderActivity.class));
                        break;
                    case 13:
                        startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                        break;
                    case 14:
                        startActivity(new Intent(MainActivity.this, AnimatorActivity.class));
                        break;
                    case 15:
                        startActivity(new Intent(MainActivity.this, AIDLActivity.class));
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

        gridView.setAdapter(adapter);


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
        list.add("ContentProvider");
        list.add("Gallery");
        list.add("Animator");
        list.add("AIDL");

        adapter.addAll(list);

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

}
