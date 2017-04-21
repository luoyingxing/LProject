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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Button button = (Button) findViewById(R.id.btn_event);
    }

}
