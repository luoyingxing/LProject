package com.luo.project.recycler;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luo.project.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ReAdapter
 * <p>
 * Created by Administrator on 2017/3/16.
 */

public class ReAdapter extends RecyclerView.Adapter<ReAdapter.ViewHolder> {
    private List<String> mList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public ReAdapter(List<String> list) {
        mList.addAll(list);
    }

    public void addData(String string) {
        mList.add(getItemCount(), string);
        notifyItemInserted(getItemCount());
    }

    public void addAll(List<String> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mList.get(position));

        int height = 200;
        holder.mTextView.setBackgroundColor(Color.BLUE);
        if (position % 3 == 2) {
            height = 300;
            holder.mTextView.setBackgroundColor(Color.RED);
        } else if (position % 3 == 1) {
            height = 400;
            holder.mTextView.setBackgroundColor(Color.GREEN);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        holder.mTextView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}