package com.luo.project.refresh;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luo.project.R;
import com.luo.project.adapter.CommonAdapter;
import com.luo.project.db.InfoDB;
import com.luo.project.entity.Info;

import java.util.ArrayList;
import java.util.List;

public class RefreshActivity extends AppCompatActivity {
    private RefreshLayout layout;
    private ListView listView;
    private CommonAdapter<Info> adapter;
    private TextView refreshTV;
    private TextView loadTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        layout = (RefreshLayout) findViewById(R.id.refresh_layout);
        listView = (ListView) findViewById(R.id.list_view);
        refreshTV = (TextView) findViewById(R.id.tv_refresh);
        loadTV = (TextView) findViewById(R.id.tv_load);

        layout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Log.i("RefreshActivity", "onRefresh ");
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 2000);
                adapter.clear();
            }

            @Override
            public void onLoadMore() {
                Log.i("RefreshActivity", "onLoadMore ");
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 2000);
            }
        });

        layout.setOnStatusListener(new RefreshLayout.OnStatusListener() {

            @Override
            public void onRefreshInit() {
                Log.i("RefreshActivity", "onRefreshInit() ");
                refreshTV.setText("下拉刷新");
            }

            @Override
            public void onPrepareToRefresh() {
                Log.i("RefreshActivity", "onPrepareToRefresh() ");
                refreshTV.setText("松开刷新");
            }

            @Override
            public void onRefreshing() {
                Log.i("RefreshActivity", "onRefreshing() ");
                refreshTV.setText("正在刷新");
            }

            @Override
            public void onRefreshFinish() {
                Log.i("RefreshActivity", "onRefreshFinish() ");
                refreshTV.setText("下拉刷新");
            }

            @Override
            public void onLoadInit() {
                Log.i("RefreshActivity", "onLoadInit() ");
                loadTV.setText("上拉加载更多");
            }

            @Override
            public void onPrepareToLoadMore() {
                Log.i("RefreshActivity", "onPrepareToLoadMore() ");
                loadTV.setText("松开加载更多");
            }

            @Override
            public void onLoading() {
                Log.i("RefreshActivity", "onLoading() ");
                loadTV.setText("正在加载");
            }

            @Override
            public void onLoadFinish() {
                Log.i("RefreshActivity", "onLoadFinish() ");
                loadTV.setText("上拉加载更多");
            }
        });


        adapter = new CommonAdapter<Info>(this, new ArrayList<Info>(), R.layout.item_recycler_view) {
            @Override
            public void convert(com.luo.project.adapter.ViewHolder helper, Info info) {
                helper.setText(R.id.text, info.getInfoId() + ". " + info.getTitle());
                SimpleDraweeView iamgeView = helper.getView(R.id.image);
                iamgeView.setImageURI(Uri.parse(info.getUrl()));
            }
        };
        listView.setAdapter(adapter);

        adapter.addAll(getDataList());
    }

    private List<Info> getDataList() {
        return InfoDB.getInstance().selectAll(getApplicationContext());
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.addAll(getDataList());
            layout.onRefreshComplete();
            layout.onLoadMoreComplete();
        }

    };
}
