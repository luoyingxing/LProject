package com.luo.project.layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luo.project.R;
import com.luo.project.entity.Girls;
import com.luo.project.nohttp.GsonRequest;

/**
 * ViewActivity
 * <p/>
 * Created by luoyingxing on 16/10/23.
 */
public class ViewActivity extends AppCompatActivity {
    private RefreshLayout mRefreshLayout;
    private PullAbleListView mPullAbleListView;
    private Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_view);

        mRefreshLayout = (RefreshLayout) findViewById(R.id.refresh_layout);
        mPullAbleListView = (PullAbleListView) findViewById(R.id.lv_content);

        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout pullToRefreshLayout) {
                mAdapter.clear();
                loadImage();
            }

            @Override
            public void onLoadMore(RefreshLayout pullToRefreshLayout) {
                loadImage();
            }
        });

        mAdapter = new Adapter(this);
        mPullAbleListView.setAdapter(mAdapter);

        loadImage();
    }

    private String apiKey = "87dd5b309735c00e1cc37bb52c97b7a0";

    private void loadImage() {
        String path = "http://apis.baidu.com/txapi/mvtp/meinv";
        new GsonRequest<Girls>(path) {

            @Override
            protected void onSuccess(Girls result) {
                super.onSuccess(result);
                mAdapter.addAll(result.getNewslist());
            }


            @Override
            protected void onFinish(int what) {
                super.onFinish(what);
                mRefreshLayout.refreshFinish(RefreshLayout.SUCCEED);
                mRefreshLayout.loadmoreFinish(RefreshLayout.SUCCEED);
            }

        }.addHeaders("apikey", apiKey)
                .addParam("num", 10)
                .get();

    }

    private class Adapter extends ArrayAdapter<Girls.NewslistBean> {
        Context ctx;

        public Adapter(Context context) {
            super(context, 0);
            ctx = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(ctx).inflate(R.layout.item_view_refresh, null);
                viewHolder.image = (SimpleDraweeView) convertView.findViewById(R.id.iv_image);
                viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.description = (TextView) convertView.findViewById(R.id.tv_description);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Girls.NewslistBean girls = getItem(position);

            viewHolder.image.setImageURI(Uri.parse(girls.getPicUrl()));
            viewHolder.title.setText(girls.getTitle());
            viewHolder.description.setText(girls.getDescription());
            return convertView;
        }

        private class ViewHolder {
            SimpleDraweeView image;
            TextView title;
            TextView description;
        }
    }


}
