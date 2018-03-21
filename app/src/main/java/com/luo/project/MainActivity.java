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
import com.luo.project.bar.TitleBarActivity;
import com.luo.project.breakwifi.BreakWifiActivity;
import com.luo.project.contentProvider.ContentProviderActivity;
import com.luo.project.coordinator.CoordinatorActivity;
import com.luo.project.design.DesignActivity;
import com.luo.project.event.EventActivity;
import com.luo.project.flow.FloatWindowService;
import com.luo.project.gallery.GalleryActivity;
import com.luo.project.intent.IntentActivity;
import com.luo.project.layout.LayoutActivity;
import com.luo.project.nohttp.NoHttpActivity;
import com.luo.project.qr.QRActivity;
import com.luo.project.recycler.RecyclerViewActivity;
import com.luo.project.reflect.ReflectActivity;
import com.luo.project.refresh.RefreshActivity;
import com.luo.project.rx.RxJavaActivity;
import com.luo.project.retrofit.RetrofitActivity;
import com.luo.project.server.ServerActivity;
import com.luo.project.thread.ThreadActivity;
import com.luo.project.vector.VectorActivity;
import com.luo.project.view.ViewActivity;
import com.luo.project.web.WebViewActivity;
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
                        startActivity(ViewActivity.class);
                        break;
                    case 1:
                        startActivity(ViewGroupActivity.class);
                        break;
                    case 2:
                        startActivity(IntentActivity.class);
                        break;
                    case 3:
                        startActivity(NoHttpActivity.class);
                        break;
                    case 4:
                        startActivity(LayoutActivity.class);
                        break;
                    case 5:
                        Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                        startService(intent);
                        finish();
                        break;
                    case 6:
                        startActivity(ThreadActivity.class);
                        break;
                    case 7:
                        startActivity(AdapterActivity.class);
                        break;
                    case 8:
                        startActivity(WifiActivity.class);
                        break;
                    case 9:
                        startActivity(RecyclerViewActivity.class);
                        break;
                    case 10:
                        startActivity(EventActivity.class);
                        break;
                    case 11:
                        startActivity(RxJavaActivity.class);
                        break;
                    case 12:
                        startActivity(ContentProviderActivity.class);
                        break;
                    case 13:
                        startActivity(GalleryActivity.class);
                        break;
                    case 14:
                        startActivity(AnimatorActivity.class);
                        break;
                    case 15:
                        startActivity(AIDLActivity.class);
                        break;
                    case 16:
                        startActivity(ServerActivity.class);
                        break;
                    case 17:
                        startActivity(VectorActivity.class);
                        break;
                    case 18:
                        startActivity(WebViewActivity.class);
                        break;
                    case 19:
                        startActivity(ReflectActivity.class);
                        break;
                    case 20:
                        startActivity(RefreshActivity.class);
                        break;
                    case 21:
                        startActivity(DesignActivity.class);
                        break;
                    case 22:
                        startActivity(RetrofitActivity.class);
                        break;
                    case 23:
                        startActivity(BreakWifiActivity.class);
                        break;
                    case 24:
                        startActivity(CoordinatorActivity.class);
                        break;
                    case 25:
                        startActivity(TitleBarActivity.class);
                        break;
                    case 26:
                        startActivity(QRActivity.class);
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
        list.add("Layout");
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
        list.add("Server");
        list.add("Vector");
        list.add("WebView");
        list.add("reflect");
        list.add("refresh");
        list.add("design");
        list.add("Retrofit");
        list.add("Break WIFI");
        list.add("CoordinatorLayout");
        list.add("TitleBar");
        list.add("QR");

        adapter.addAll(list);

    }

    private void startActivity(Class clazz) {
        startActivity(new Intent(MainActivity.this, clazz));
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