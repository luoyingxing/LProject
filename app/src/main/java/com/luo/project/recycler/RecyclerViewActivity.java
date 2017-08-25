package com.luo.project.recycler;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luo.project.R;
import com.luo.project.db.CrawlUtils;
import com.luo.project.db.InfoDB;
import com.luo.project.entity.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerViewActivity
 * <p>
 * Created by Administrator on 2017/3/13.
 */

public class RecyclerViewActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mGetBtn;
    private Button mAddBtn;
    private XRecyclerView mRecyclerView;
    private XAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        getSupportActionBar().hide();

        mGetBtn = (Button) findViewById(R.id.button_get);
        mAddBtn = (Button) findViewById(R.id.button_add);

        mGetBtn.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);

        mRecyclerView = (XRecyclerView) findViewById(R.id.rv_recycler);

        mAdapter = new XAdapter<Info>(this, new ArrayList<Info>(), R.layout.item_recycler_view) {
            @Override
            public void convert(ViewHolder holder, Info info) {
                holder.setText(R.id.text, info.getInfoId() + ". " + info.getTitle());
                SimpleDraweeView iamgeView = holder.getView(R.id.image);
                iamgeView.setImageURI(Uri.parse(info.getUrl()));
            }
        };

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //TODO 线性布局
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        //TODO 网格布局 1
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //TODO 网格布局 2
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //TODO 流式布局
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        mAdapter.setOnItemClickListener(new XAdapter.OnItemClickListeners<Info>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, Info item, int position) {
                Toast.makeText(getApplicationContext(), item.getUrl(), Toast.LENGTH_SHORT).show();
            }
        });


        mRecyclerView.setOnLoadMoreListener(new XRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mAdapter.addAll(getDataList());
            }
        });

    }

    private List<Info> getDataList() {
        return InfoDB.getInstance().selectAll(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_get:
                InfoDB.getInstance().insert(getApplicationContext(), CrawlUtils.getInstance().crawlInfoList());
                break;
            case R.id.button_add:
                mAdapter.addAll(getDataList());
                break;
        }
    }
}