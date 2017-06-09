package com.luo.project.aidl;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.luo.project.CastielProgressConnection;
import com.luo.project.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * LocalCastielService
 *
 * @Description: 本地服务
 * <p>
 * Created by luoyingxing on 2017/6/9.
 */

public class LocalCastielService extends Service {
    MyBinder myBinder;
    private PendingIntent pintent;
    MyServiceConnection myServiceConnection;

    @Override
    public void onCreate() {
        super.onCreate();
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        myServiceConnection = new MyServiceConnection();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this, RemoteCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);
        pintent = PendingIntent.getService(this, 0, intent, 0);

        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Ticker")
                .setContentTitle("ContentTitle")
                .setContentText("正在运行…")
                .setContentIntent(pintent)
                .setWhen(System.currentTimeMillis())
                .build();

        // 设置service为前台进程，避免手机休眠时系统自动杀掉该服务
        startForeground(startId, notification);

        new Thread(new TaskRunnable()).start();

        return START_STICKY;
    }

    private int flag = 1;

    private class TaskRunnable implements Runnable {
        private Timer mTimer;
        private TimerTask mTimerTask;

        @Override
        public void run() {
            mTimer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.v("本地服务", "flag = " + flag);
                    flag++;
                }
            };
            mTimer.schedule(mTimerTask, 0, 5000);
        }
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            Log.i("castiel", "远程服务连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // 连接出现了异常断开了，RemoteService被杀掉了
            Toast.makeText(LocalCastielService.this, "远程服务Remote被干掉", Toast.LENGTH_LONG).show();
            // 启动RemoteCastielService
            LocalCastielService.this.startService(new Intent(LocalCastielService.this, RemoteCastielService.class));
            LocalCastielService.this.bindService(new Intent(LocalCastielService.this, RemoteCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);
        }

    }

    class MyBinder extends CastielProgressConnection.Stub {

        @Override
        public String getProName() throws RemoteException {
            return "Local猴子搬来的救兵 http://blog.csdn.net/mynameishuangshuai";
        }

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return myBinder;
    }

}