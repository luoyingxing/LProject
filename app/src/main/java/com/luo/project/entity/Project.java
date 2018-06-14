package com.luo.project.entity;

import com.luo.project.ViewGroup.ViewGroupActivity;
import com.luo.project.adapter.AdapterActivity;
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
import com.luo.project.retrofit.RetrofitActivity;
import com.luo.project.rx.RxJavaActivity;
import com.luo.project.server.ServerActivity;
import com.luo.project.support.ConstraintActivity;
import com.luo.project.thread.ThreadActivity;
import com.luo.project.vector.VectorActivity;
import com.luo.project.view.ViewActivity;
import com.luo.project.web.WebViewActivity;
import com.luo.project.wifi.WifiActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Created by luoyingxing on 2018/6/14.
 */

public class Project {
    private Class clazz;
    private String title;
    private String subtitle;

    public Project(Class clazz, String title, String subtitle) {
        this.clazz = clazz;
        this.title = title;
        this.subtitle = subtitle;
    }

    public static List<Project> getProjectList() {
        List<Project> list = new ArrayList<>();

        list.add(new Project(ViewActivity.class, "RecyclerView", ""));
        list.add(new Project(ViewGroupActivity.class, "ViewGroup", ""));
        list.add(new Project(IntentActivity.class, "Intent", ""));
        list.add(new Project(NoHttpActivity.class, "NoHttp", ""));
        list.add(new Project(LayoutActivity.class, "Layout", ""));
        list.add(new Project(FloatWindowService.class, "flow button", ""));
        list.add(new Project(ThreadActivity.class, "Thread pool", ""));
        list.add(new Project(AdapterActivity.class, "Adapter", ""));
        list.add(new Project(WifiActivity.class, "Wifi", ""));
        list.add(new Project(RecyclerViewActivity.class, "RecyclerView", ""));
        list.add(new Project(EventActivity.class, "View Event", ""));
        list.add(new Project(RxJavaActivity.class, "RxJava", ""));
        list.add(new Project(ContentProviderActivity.class, "ContentProvider", ""));
        list.add(new Project(GalleryActivity.class, "Gallery", ""));
        list.add(new Project(AnimatorActivity.class, "Animator", ""));
        list.add(new Project(AIDLActivity.class, "AIDL", ""));
        list.add(new Project(ServerActivity.class, "Server", ""));
        list.add(new Project(VectorActivity.class, "Vector", ""));
        list.add(new Project(WebViewActivity.class, "WebView", ""));
        list.add(new Project(ReflectActivity.class, "reflect", ""));
        list.add(new Project(RefreshActivity.class, "refresh", ""));
        list.add(new Project(DesignActivity.class, "design", ""));
        list.add(new Project(RetrofitActivity.class, "Retrofit", ""));
        list.add(new Project(BreakWifiActivity.class, "Break WIFI", ""));
        list.add(new Project(CoordinatorActivity.class, "CoordinatorLayout", ""));
        list.add(new Project(TitleBarActivity.class, "TitleBar", ""));
        list.add(new Project(QRActivity.class, "QR", "第三方二维码扫描库"));
        list.add(new Project(ConstraintActivity.class, "ConstraintLayout", "support包的约束布局（xml默认的布局）"));


        return list;

    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
