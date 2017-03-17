package com.luo.project.recycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.luo.project.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerViewActivity
 * <p>
 * Created by Administrator on 2017/3/13.
 */

public class RecyclerViewActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mAddBtn;
    private XRecyclerView mRecyclerView;
    private XAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        mAddBtn = (Button) findViewById(R.id.button_add);
        mAddBtn.setOnClickListener(this);
        mRecyclerView = (XRecyclerView) findViewById(R.id.rv_recycler);

        mAdapter = new XAdapter<String>(this, getDataList(), R.layout.item_recycler_view) {
            @Override
            public void convert(ViewHolder holder, String data) {
                holder.setText(R.id.text, data);
            }
        };

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //TODO 线性布局
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        //TODO 网格布局 1
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //TODO 网格布局 2
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //TODO 流式布局
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        mAdapter.setOnItemClickListener(new XAdapter.OnItemClickListeners<String>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, String item, int position) {
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
            }
        });


        mRecyclerView.setOnLoadMoreListener(new XRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mAdapter.addAll(getDataList());
            }
        });

    }

    private int temp = 0;

    private List<String> getDataList() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add("离离原上草,一岁一枯荣，野火烧不尽，传峰" + temp);
            temp++;
        }
        return strings;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                mAdapter.addAll(getDataList());
                break;
        }
    }
}
