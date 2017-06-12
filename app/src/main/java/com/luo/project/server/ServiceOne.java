package com.luo.project.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * ServiceOne
 * <p>
 * Created by luoyingxing on 2017/6/12.
 */

public class ServiceOne extends Service {
    public static final String TAG = "ServiceOne";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}