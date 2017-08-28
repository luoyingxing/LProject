package com.luo.project.refresh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.luo.project.R;

public class RefreshActivity extends AppCompatActivity {
    private RefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        layout = (RefreshLayout) findViewById(R.id.refresh_layout);

        layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("RefreshActivity", "postDelayed ---- ");
                layout.onRefreshComplete();
            }
        }, 8000);

    }
}
