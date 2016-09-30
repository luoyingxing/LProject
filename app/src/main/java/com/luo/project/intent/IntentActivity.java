package com.luo.project.intent;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.luo.project.R;

import java.io.IOException;

/**
 * IntentActivity
 * <p/>
 * Created by luoyingxing on 16/9/30.
 */
public class IntentActivity extends AppCompatActivity {

    private Button buttonOne;
    private Button buttonTwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);


        buttonOne = (Button) findViewById(R.id.intent_btn_open_other);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cn = new ComponentName("com.mck.renwoxue", "com.mck.renwoxue.StartActivity");
                i.setComponent(cn);
                i.setAction("android.intent.action.MAIN");
                startActivityForResult(i, RESULT_OK);
            }
        });

        buttonTwo = (Button) findViewById(R.id.intent_btn_close);
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot -p"});  //关机
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(IntentActivity.this, "没有Root权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
