package com.luo.project.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.luo.project.R;

import java.util.ArrayList;

/**
 * AdapterActivity
 * <p>
 * Created by Administrator on 2016/12/26.
 */

public class AdapterActivity extends AppCompatActivity {
    private ListView listView;
    private CommonAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter);

        listView = (ListView) findViewById(R.id.list_view);


        adapter = new CommonAdapter<String>(getApplicationContext(),
                new ArrayList<String>(), R.layout.adapter_item) {

            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.tv_text, item);
            }

        };

        listView.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        for (int i = 0; i < 20; i++) {
            adapter.add("this is a adapter" + i * 2);
        }
    }
}
