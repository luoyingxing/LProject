package com.luo.project.contentProvider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luo.project.R;
import com.luo.project.adapter.CommonAdapter;
import com.luo.project.adapter.ViewHolder;
import com.luo.project.rx.RxJavaActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Emitter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * ContentProviderActivity
 * <p>
 * Created by luoyingxing on 2017/6/4.
 */

public class ContentProviderActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private GridView gridView;
    private CommonAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_content_provider);

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        gridView = (GridView) findViewById(R.id.grid_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ContentProviderActivity", "onClick");
//                addContact();
                String result = getContactInfo();
                textView.setText(null);
                textView.setTextColor(Color.BLUE);
                textView.setText(String.format("记录\t名字\t电话\n%s", result));

            }
        });

        readSms();

        register();

        getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, new SmsObserver(new Handler()));

        adapter = new CommonAdapter<String>(this, new ArrayList<String>(), R.layout.item_provider_list) {
            @Override
            public void convert(ViewHolder helper, final String path) {
                final ImageView imageView = helper.getView(R.id.image_view);
                imageView.setImageBitmap(null);
                Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {
                        try {
                            BitmapFactory.Options opt = new BitmapFactory.Options();
                            opt.inPreferredConfig = Bitmap.Config.RGB_565;
                            opt.inPurgeable = true;
                            opt.inSampleSize = 4;
                            opt.inInputShareable = true;
                            opt.outHeight = 80;
                            opt.outHeight = 80;
                            InputStream inputStream = new FileInputStream(path);
                            subscriber.onNext(BitmapFactory.decodeStream(inputStream, null, opt));
                            subscriber.onCompleted();
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Bitmap>() {
                            @Override
                            public void onNext(Bitmap drawable) {
                                imageView.setImageBitmap(drawable);
                            }

                            @Override
                            public void onCompleted() {
                                Log.i("ContentProviderActivity", "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i("ContentProviderActivity", "onError");
                            }
                        });
            }
        };
        gridView.setAdapter(adapter);

        scanImage();
    }

    public String getContactInfo() {
        String result = "";
        ContentResolver resolver = getContentResolver();
        //查询联系人
        Log.i("ContentProviderActivity", ContactsContract.Contacts.CONTENT_URI.toString());
        //TODO  content://com.android.contacts/contacts
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i("ContentProviderActivity", i + "    " + cursor.getColumnName(i));
        }

        int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        // 取得联系人名字 (显示出来的名字)，实际内容在 ContactsContract.Contacts中
        int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        for (cursor.moveToFirst(); (!cursor.isAfterLast()); cursor.moveToNext()) {
            //获取联系人ID
            String contactId = cursor.getString(idIndex);
            result = result + contactId + "\t\t\t";
            result = result + cursor.getString(nameIndex) + "\t\t\t";
            // 根据联系人ID查询对应的电话号码
            //TODO content://com.android.contacts/data/phones
            Cursor phoneNumbers = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            // 取得电话号码(可能存在多个号码)
            while (phoneNumbers.moveToNext()) {
                String strPhoneNumber = phoneNumbers.getString(phoneNumbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                result = result + strPhoneNumber + "\t\t\t";
            }
            phoneNumbers.close();

            // 根据联系人ID查询对应的email
            Cursor emails = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
            // 取得email(可能存在多个email)
            while (emails.moveToNext()) {
                String strEmail = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                result = result + strEmail + "\t\t\t";
            }
            emails.close();
            result = result + "\n";
        }
        cursor.close();
        return result;
    }

    public void addContact() {
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();
        // 向RawContacts.CONTENT_URI执行一个空值插入，
        // 目的是获取系统返回的rawContactId
        Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(Data.RAW_CONTACT_ID, rawContactId);
        // 设置内容类型
        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
        // 设置联系人名字
        values.put(StructuredName.GIVEN_NAME, "jCuckoo");
        // 向联系人URI添加联系人名字
        getContentResolver().insert(Data.CONTENT_URI, values);
        values.clear();


        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        // 设置联系人的电话号码
        values.put(Phone.NUMBER, "1234567890");
        // 设置电话类型
        values.put(Phone.TYPE, Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        getContentResolver().insert(Data.CONTENT_URI, values);
        values.clear();


        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
        // 设置联系人的Email地址
        values.put(Email.DATA, "cc@qq.com");
        // 设置该电子邮件的类型
        values.put(Email.TYPE, Email.TYPE_WORK);
        // 向联系人Email URI添加Email数据
        getContentResolver().insert(Data.CONTENT_URI, values);
        Toast.makeText(this, "联系人数据添加成功", Toast.LENGTH_LONG).show();
    }


    private void readSms() {
        //1.利用内容提供者  中间人 获取用户的短信数据.
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://sms"); //根据分析 代表的是所有的短信的路径
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "body", "type"}, "address=10086", null, null);

        StringBuffer sb = new StringBuffer();
        while (cursor.moveToNext()) {
            String address = cursor.getString(0);
            String date = cursor.getString(1);
            String body = cursor.getString(2);
            String type = cursor.getString(3);
            System.out.println(address + "--" + date + "---" + body + "---" + type);
            sb.append(address + "--" + date + "---" + body + "---" + type);
            sb.append("\n");
        }
        cursor.close();
        textView.setText(sb.toString());

    }

    private void register() {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    private class SmsObserver extends ContentObserver {

        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.v("onChange", "onChange()");
            Cursor c = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, "date desc"); //按日期【排序  最后条记录应该在最上面  desc 从大到小    asc小到大
            Log.e("onChange", "记录数:" + c.getCount());
            if (c != null) {
                if (c.moveToFirst()) {
                    String address = c.getString(c.getColumnIndex("address"));
                    String body = c.getString(c.getColumnIndex("body"));

                    Log.i("短信：", "address : " + address);
                    Log.i("短信：", "body : " + body);
                }
                c.close();
            }
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            //content://sms/3388
            Log.v("onChange", "uri  --  " + uri.toString());

            Cursor c = getContentResolver().query(uri, null, null, null, null); //按日期【排序  最后条记录应该在最上面  desc 从大到小    asc小到大
            if (c != null) {
                if (c.moveToFirst()) {
                    String address = c.getString(c.getColumnIndex("address"));
                    String body = c.getString(c.getColumnIndex("body"));

                    Log.i("Uri短信：", "address : " + address);
                    Log.i("Uri短信：", "body : " + body);
                }
                c.close();
            }
        }

        @Override
        public boolean deliverSelfNotifications() {
            Log.v("MyService", "deliverSelfNotifications");
            return super.deliverSelfNotifications();
        }
    }

    private void scanImage() {
        Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getContentResolver();

                //只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                if (mCursor == null) {
                    return;
                }

                List<String> list = new ArrayList<>();
                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.v("scanImage", "path：" + path);
                    list.add(path);
                }

                Log.e("scanImage", "图片个数：" + list.size());

                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {

                    @Override
                    public void call(List<String> list) {
                        adapter.addAll(list);
                    }
                });


    }


}
