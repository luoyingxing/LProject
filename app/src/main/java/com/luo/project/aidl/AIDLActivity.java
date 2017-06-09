package com.luo.project.aidl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.luo.project.R;

public class AIDLActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 启动本地服务和远程服务
                startService(new Intent(AIDLActivity.this, LocalCastielService.class));
                startService(new Intent(AIDLActivity.this, RemoteCastielService.class));
            }
        });
    }


}