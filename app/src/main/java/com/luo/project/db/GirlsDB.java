package com.luo.project.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.luo.project.entity.Girls;

/**
 * GirlsDB
 * <p/>
 * Created by luoyingxing on 16/11/15.
 */
public class GirlsDB {

    public static void insert(Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        String sql = "insert into girls (ctime,title,description,picUrl,url) values (?,?,?,?,?)";
        Object[] object = {"2015-02-12", "视频播放打饭中", "描述：该识破是卡到那看偏要打打卡打卡卡顿安定", "http://wwww/baidu.com", "http://wwww/baidu.com/iamges"};
        database.execSQL(sql, object);
        database.close();
    }

    public static void select(Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        String sql = "select * from girls";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String picUrl = cursor.getString(cursor.getColumnIndex("picUrl"));
            Log.e("GirlsDB", " ----------------");
            Log.e("GirlsDB", "title = " + title);
            Log.e("GirlsDB", "description = " + description);
            Log.e("GirlsDB", "picUrl = " + picUrl);
            Log.e("GirlsDB", " ----------------");
        }


        cursor.close();
        database.close();
    }
}
