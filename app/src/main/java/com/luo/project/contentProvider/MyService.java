package com.luo.project.contentProvider;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MyService
 * <p>
 * Created by luoyingxing on 2017/6/5.
 */

public class MyService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 为content://sms的数据改变注册监听器
        getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, new SmsObserver(new Handler()));
    }

    // 提供自定义的ContentProvider监听器类
    private final class SmsObserver extends ContentObserver {

        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.i("MyService", "onChange()");
//            // 查询发送箱中的短信(处于正在发送状态的短信放在发送箱中)
//            Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
//            cursor.moveToFirst();
//            StringBuffer sb = new StringBuffer();
//            // 获取短信的发送地址
//            sb.append("address=").append(cursor.getString(cursor.getColumnIndex("address")));
//            // 获取短信的标题
//            sb.append(";subject=").append(cursor.getString(cursor.getColumnIndex("subject")));
//            // 获取短信的内容
//            sb.append(";body=").append(cursor.getString(cursor.getColumnIndex("body")));
//            // 获取短信的发生时间
//            sb.append(";time=").append(cursor.getLong(cursor.getColumnIndex("date")));
//            Log.i("短信：", sb.toString());
//
//            cursor.moveToLast();
//            StringBuffer sb1 = new StringBuffer();
//            // 获取短信的发送地址
//            sb1.append("address=").append(cursor.getString(cursor.getColumnIndex("address")));
//            // 获取短信的标题
//            sb1.append(";subject=").append(cursor.getString(cursor.getColumnIndex("subject")));
//            // 获取短信的内容
//            sb1.append(";body=").append(cursor.getString(cursor.getColumnIndex("body")));
//            // 获取短信的发生时间
//            sb1.append(";time=").append(cursor.getLong(cursor.getColumnIndex("date")));
//            Log.i("短信：", sb1.toString());


            // 第一遍 先执行content://sms/raw
            // 第二遍则 uri.toString :content://sms/inbox

            Uri inboxUri = Uri.parse("content://sms/inbox");
            Cursor c = getContentResolver().query(inboxUri, null, null, null, "date desc"); //按日期【排序  最后条记录应该在最上面  desc 从大到小    asc小到大
            if (c != null) {
                if (c.moveToFirst()) {
                    String address = c.getString(c.getColumnIndex("address"));
                    String body = c.getString(c.getColumnIndex("body"));

                    Log.w("服务端监听短信：", "address : " + address);
                    Log.w("服务端监听短信：", "body : " + body);
                }
                c.close();
            }
        }
    }
}
