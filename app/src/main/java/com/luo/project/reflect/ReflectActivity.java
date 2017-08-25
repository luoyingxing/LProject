package com.luo.project.reflect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.luo.project.R;

import java.lang.reflect.Field;

public class ReflectActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflect);

        textView = (TextView) findViewById(R.id.content);

        Class<?> clazz = Runtime.class.getClass();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Log.d("ReflectActivity", "" + field.getName());

            textView.append(field.getName() + "\n");


        }
    }

}