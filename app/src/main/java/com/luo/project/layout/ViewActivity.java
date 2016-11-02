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
import com.luo.project.utils.ImageLoader;

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

        getSupportActionBar().hide();

        mRefreshLayout = (RefreshLayout) findViewById(R.id.refresh_layout);
        mPullAbleListView = (PullAbleListView) findViewById(R.id.lv_content);

        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout pullToRefreshLayout) {
                mAdapter.clear();
                loadImage();
//                loadImage(urls);
            }

            @Override
            public void onLoadMore(RefreshLayout pullToRefreshLayout) {
                loadImage();
//                loadImage(urls);
            }
        });

        mAdapter = new Adapter(this);
        mPullAbleListView.setAdapter(mAdapter);

        loadImage();

//        loadImage(urls);
    }

    String[] urls = new String[]{"http://i1.hexunimg.cn/2014-08-15/167580248.jpg",
            "http://desk.fd.zol-img.com.cn/t_s960x600c5/g4/M01/0D/04/Cg-4WVP_npmIY6GRAKcKYPPMR3wAAQ8LgNIuTMApwp4015.jpg",
            "http://desk.fd.zol-img.com.cn/t_s960x600c5/g5/M00/01/0E/ChMkJlbKwhKIPf_RAAweZKvhDqMAALGiQLPZ9QADB58872.jpg",
            "http://desk.fd.zol-img.com.cn/t_s960x600c5/g5/M00/02/06/ChMkJ1bKyqKIPFxLAGZe49gDZ3YAALIegJkT54AZl77897.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1610/09/c0/28134119_1476023565298_800x600.jpg",
            "http://desk.fd.zol-img.com.cn/t_s960x600c5/g5/M00/01/0E/ChMkJ1bKwhGIUJlGAA6OD8_97jgAALGiQIKBdAADo4n874.jpg",
            "http://pic76.nipic.com/file/20150824/21321671_165321269000_2.jpg",
            "http://pic2015.5442.com:82/2016/0921/10/2.jpg%21960.jpg",
            "http://pic1.win4000.com/wallpaper/2/57ec7798da967.jpg",
            "http://pic1.win4000.com/wallpaper/0/57ec76bf556c1.jpg",
            "http://pic4.nipic.com/20090823/3193830_121855091_2.jpg"};

    private void loadImage(String[] urls) {
        for (String url : urls) {
            mAdapter.add(new Girls.NewslistBean("", "图片", "图片", url, ""));
        }
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

//            viewHolder.image.setImageURI(Uri.parse(girls.getPicUrl()));
            viewHolder.title.setText(girls.getTitle());
            viewHolder.description.setText(girls.getDescription());

            ImageLoader.loadImage(Uri.parse(girls.getPicUrl()), viewHolder.image);
            return convertView;
        }

        private class ViewHolder {
            SimpleDraweeView image;
            TextView title;
            TextView description;
        }
    }


}
