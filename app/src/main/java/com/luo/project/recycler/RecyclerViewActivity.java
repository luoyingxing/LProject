package com.luo.project.recycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
    private RecyclerView mRecyclerView;
    private ReAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        mAddBtn = (Button) findViewById(R.id.button_add);
        mAddBtn.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recycler);

        mAdapter = new ReAdapter(getDataList());
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


        mAdapter.setOnItemClickListener(new ReAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = -1;
                //当前RecyclerView显示出来的最后一个的item的position

                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof GridLayoutManager) {
                        //通过LayoutManager找到当前显示的最后的item的position
                        lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof LinearLayoutManager) {
                        lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                        //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                        int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                        lastPosition = findMax(lastPositions);
                    }

                    //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                    //如果相等则说明已经滑动到最后了
                    if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                        Toast.makeText(RecyclerViewActivity.this, "滑动到底了", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    //找到数组中的最大值
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int temp = 0;

    private List<String> getDataList() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add("离离原上草" + temp);
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
