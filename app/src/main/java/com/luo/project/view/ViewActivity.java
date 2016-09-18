package com.luo.project.view;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.luo.project.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewActivity
 * <p/>
 * Created by luoyingxing on 16/9/12.
 */
public class ViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Button addBtn;

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        setActionBar();
        init();

    }

    private void init() {
        addBtn = (Button) findViewById(R.id.btn_view_one);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addData(3);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));

        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
//        mRecyclerView.setHasFixedSize(true);

        //创建并设置Adapter
        mAdapter = new MyAdapter(getDummyDatas());
        mRecyclerView.setAdapter(mAdapter);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        //设置布局管理器

//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));


        //添加分割线
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(ViewActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(ViewActivity.this));


        mAdapter.setOnItemClickLitener(new MyAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ViewActivity.this, position + " click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(ViewActivity.this, position + " long click", Toast.LENGTH_SHORT).show();
            }
        });

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

    private List<String> getDummyDatas() {
        List<String> strings = new ArrayList<>();
        strings.add("离离原上草");
        strings.add("一岁一枯荣");
        strings.add("野火烧不尽");
        strings.add("春风吹又生");
        strings.add("我来自何方");
        strings.add("我来自何方");
        strings.add("我来自何方");
        strings.add("我来自何方");
        return strings;
    }
}
