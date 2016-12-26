package com.luo.project.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luo.project.R;

import java.util.List;

/**
 * MyAdapter
 * <p>
 * Created by Administrator on 2016/12/26.
 */

public class MyAdapter<T> extends CommonAdapter {


    public MyAdapter(Context context, List mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_view_refresh, position);
        TextView mTitle = viewHolder.getView(R.id.tv_title);
        mTitle.setText((String) mDatas.get(position));
        return viewHolder.getConvertView();
    }

    @Override
    public void convert(ViewHolder helper, Object item) {

    }
}
