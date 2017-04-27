package com.luo.project.event;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.luo.project.R;
import com.luo.project.utils.MPermissionHelper;

import java.util.Arrays;

/**
 * EventActivity
 * <p>
 * Created by luoyingxing on 2017/4/21.
 */

public class EventActivity extends Activity {
// Log.i("EventActivity","");

    private MPermissionHelper permissionHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        permissionHelper = new MPermissionHelper(this);

        Button button = (Button) findViewById(R.id.btn_event);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionHelper.requestPermission(
                        new MPermissionHelper.PermissionCallBack() {
                            @Override
                            public void permissionRegisterSuccess(String... permissions) {
                                System.out.println("permissions = " + Arrays.toString(permissions));
                                System.out.println("注册权限成功,做某些处理");
                            }

                            @Override
                            public void permissionRegisterError(String... permissions) {
                                permissionHelper.showGoSettingsDialog("定位");
                            }
                        },
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA
                );
            }
        });
    }

    /**
     * 04-26 09:52:09.955 25150-25150/com.luo.project I/TestLayout: dispatchTouchEvent()0
     * 04-26 09:52:09.955 25150-25150/com.luo.project I/TestLayout: onInterceptTouchEvent()0
     * 04-26 09:52:09.955 25150-25150/com.luo.project I/EventActivity: onTouch()0
     * 04-26 09:52:10.015 25150-25150/com.luo.project I/TestLayout: dispatchTouchEvent()1
     * 04-26 09:52:10.015 25150-25150/com.luo.project I/TestLayout: onInterceptTouchEvent()1
     * 04-26 09:52:10.015 25150-25150/com.luo.project I/EventActivity: onTouch()1
     * 04-26 09:52:10.015 25150-25150/com.luo.project I/EventActivity: onClick()
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.handleRequestPermissionsResult(requestCode, new MPermissionHelper.PermissionCallBack() {
            @Override
            public void permissionRegisterSuccess(String... permissions) {
                System.out.println("1111permissions = " + Arrays.toString(permissions));
                System.out.println("1111注册权限成功,做某些处理");
            }

            @Override
            public void permissionRegisterError(String... permissions) {
                permissionHelper.showGoSettingsDialog("定位");
            }
        }, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        permissionHelper.destroy();
    }
}
