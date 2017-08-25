package com.luo.project.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.luo.project.entity.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * InfoDB
 * <p/>
 * Created by luoyingxing on 16/11/15.
 */
public class InfoDB {

    public static InfoDB getInstance() {
        return SingleHolder.singleObject;
    }

    private static class SingleHolder {
        private static InfoDB singleObject = new InfoDB();
    }

    public void insert(Context context, List<Info> list) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        String sql = "insert into info (title,url) values (?,?)";
        for (int i = 0; i < list.size(); i++) {
            Object[] object = {list.get(i).getTitle(), list.get(i).getUrl()};
            database.execSQL(sql, object);
        }
        database.close();
    }

    public List<Info> selectAll(Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        String sql = "select * from info";
        Cursor cursor = database.rawQuery(sql, null);

        List<Info> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int infoId = cursor.getInt(cursor.getColumnIndex("infoId"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            list.add(new Info(infoId, title, url));
        }
        cursor.close();
        database.close();

        mLog("ColumnCount： " + cursor.getColumnCount());
        mLog("数据库的数据总数： " + list.size());
        return list;
    }

    private void mLog(Object obj) {
        Log.i("InfoDB", "" + obj.toString());
    }
}