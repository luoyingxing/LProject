package com.luo.project.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DataBaseHelper
 * <p/>
 * Created by luoyingxing on 16/11/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context) {
        super(context, "infoDB", null, 2);
        Log.e("DataBaseHelper", "构造函数");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DataBaseHelper", "onCreate()");
        db.execSQL("create table info(infoId Integer primary key autoincrement, title varchar(20), url varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DataBaseHelper", "onUpgrade() oldVersion -> " + oldVersion + "  newVersion -> " + newVersion);
        db.execSQL("alter table info add name varchar(20)");
    }
}