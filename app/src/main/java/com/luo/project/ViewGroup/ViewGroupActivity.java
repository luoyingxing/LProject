package com.luo.project.ViewGroup;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.luo.project.R;
import com.luo.project.annotation.InitViewById;
import com.luo.project.annotation.InitViewParser;
import com.luo.project.annotation.OnClick;
import com.luo.project.ui.CircleBar;

/**
 * ViewGroupActivity
 * <p/>
 * Created by luoyingxing on 16/9/26.
 */
public class ViewGroupActivity extends AppCompatActivity implements View.OnClickListener {

    @InitViewById(id = R.id.tv_group_one)
    private TextView textViewOne;

    @OnClick
    @InitViewById(id = R.id.tv_group_two)
    private TextView textViewTwo;

    @InitViewById(id = R.id.circle_bar)
    private CircleBar circleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_group);
        setActionBar();

        //开始注入
        InitViewParser.inject(this);
        //这个主要是测试注入id 成功没有 成功了就不会报错~

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewOne.setText("这个主要是测试注入id 成功没有 成功了就不会报错");
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewTwo.setText("测试数据");
            }
        }, 5000);

        circleBar.setText("88");
        circleBar.setSweepAngle(200);

//        textViewOne.setOnClickListener(this);
    }


    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.action_bar_layout);
            Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
            parent.setContentInsetsAbsolute(0, 0);
        }

        getSupportActionBar().setElevation(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_group_one:
                Toast.makeText(ViewGroupActivity.this, "onClicktv_group_one", Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_group_two:
                Toast.makeText(ViewGroupActivity.this, "onClicktv_group_two", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
