package com.luo.project.event;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.luo.project.R;

/**
 * EventActivity
 * <p>
 * Created by luoyingxing on 2017/4/21.
 */

public class EventActivity extends Activity {
// Log.i("EventActivity","");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Button button = (Button) findViewById(R.id.btn_event);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("EventActivity", "onTouch()" + event.getActionMasked());
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("EventActivity", "onClick()");
            }
        });
    }

    /**
     * 04-26 09:52:09.955 25150-25150/com.luo.project I/TestLayout: dispatchTouchEvent()0
     04-26 09:52:09.955 25150-25150/com.luo.project I/TestLayout: onInterceptTouchEvent()0
     04-26 09:52:09.955 25150-25150/com.luo.project I/EventActivity: onTouch()0
     04-26 09:52:10.015 25150-25150/com.luo.project I/TestLayout: dispatchTouchEvent()1
     04-26 09:52:10.015 25150-25150/com.luo.project I/TestLayout: onInterceptTouchEvent()1
     04-26 09:52:10.015 25150-25150/com.luo.project I/EventActivity: onTouch()1
     04-26 09:52:10.015 25150-25150/com.luo.project I/EventActivity: onClick()
     */

}
