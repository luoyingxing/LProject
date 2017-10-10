package com.luo.project.aidl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.luo.project.R;

public class AIDLActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;

    private int temp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

        button = (Button) findViewById(R.id.btn);
        textView = (TextView) findViewById(R.id.tv);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 启动本地服务和远程服务
                if (temp == 0) {
                    temp++;
                    startService(new Intent(AIDLActivity.this, LocalService.class));
                } else {
                    startService(new Intent(AIDLActivity.this, RemoteCastielService.class));
                }
            }
        });
    }


}