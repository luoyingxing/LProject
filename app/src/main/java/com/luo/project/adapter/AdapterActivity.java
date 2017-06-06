package com.luo.project.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ListView;

import com.luo.project.R;

import java.util.ArrayList;
import java.util.List;

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


        adapter = new CommonAdapter<String>(getApplicationContext(), new ArrayList<String>(), R.layout.adapter_item) {

            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.tv_text, item);
                CheckBox checkBox = helper.getView(R.id.cb_text);
                Log.v("AdapterActivity", "Position : " + helper.getPosition());
                if (item.contains("三")) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            }

        };

        listView.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        List<String> list = new ArrayList<>();
        list.add("张三");
        list.add("张三13asd2");
        list.add("张ad123");
        list.add("张fh3ad12");
        list.add("张asd3");
        list.add("张三11d");
        list.add("张1b");
        list.add("张etsad");
        list.add("张三13");
        list.add("张tert");
        list.add("张三13asd2");
        list.add("张ad123");
        list.add("张三3ad12");
        list.add("张aad三11d");
        list.add("张1b");
        list.add("张三sad");
        list.add("张三13");
        list.add("张ret");
        list.add("张三13asd2");
        list.add("张ad123");
        list.add("张三3ad12");
        list.add("张asd3");
        list.add("张ert11d");
        list.add("张1b");
        list.add("张三sad");
        list.add("张三13");

        adapter.addAll(list);
    }
}
