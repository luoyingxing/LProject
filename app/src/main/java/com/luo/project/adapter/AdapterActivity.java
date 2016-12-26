package com.luo.project.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.luo.project.R;
import com.luo.project.entity.News;

import java.util.ArrayList;

/**
 * AdapterActivity
 * <p>
 * Created by Administrator on 2016/12/26.
 */

public class AdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        ListView listView = new ListView(this);
        listView.setAdapter(new CommonAdapter<News>(getApplicationContext(),
                new ArrayList<News>(), R.layout.action_bar_layout) {
            @Override
            public void convert(ViewHolder viewHolder, News item) {
                viewHolder.setText(R.id.tv_wifi_text, item.getShowapi_res_error());
            }
        });
    }
}
