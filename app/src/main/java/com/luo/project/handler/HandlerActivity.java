package com.luo.project.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.luo.project.R;

/**
 * <p/>
 * Created by luoyingxing on 2019/1/6.
 */
public class HandlerActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handler_activity);
        textView = findViewById(R.id.tv_handler_message);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
    }

    private void click() {
        new Thread(new HandlerRunnable()).start();
    }

    private class HandlerRunnable implements Runnable {
        private Handler handler;

        @Override
        public void run() {
            handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    textView.setText("HandlerRunnable");
                    return false;
                }
            });

            handler.sendEmptyMessage(101);
        }
    }
}
