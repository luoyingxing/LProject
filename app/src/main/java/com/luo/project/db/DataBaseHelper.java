package com.luo.project.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DataBaseHelper
 * <p/>
 * Created by luoyingxing on 16/11/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context) {
        super(context, "girls", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table girls(girlsId Integer primary key autoincrement, ctime varchar(20), title varchar(20), description varchar(20), picUrl varchar(20), url varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
