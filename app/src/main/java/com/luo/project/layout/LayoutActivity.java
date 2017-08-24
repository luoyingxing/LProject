package com.luo.project.layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.luo.project.R;

public class LayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        findViewById(R.id.test_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("LayoutActivity", "layout onTouch()");
                return false;
            }
        });

        findViewById(R.id.test_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LayoutActivity", "layout onClick()");
            }
        });

        findViewById(R.id.test_view).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("LayoutActivity", "view onTouch()");
                return false;
            }
        });

        findViewById(R.id.test_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LayoutActivity", "view onClick()");
            }
        });

    }
}
