package com.luo.project.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * MyGridLayoutManager
 * <p/>
 * Created by luoyingxing on 16/9/13.
 */
public class MyGridLayoutManager extends GridLayoutManager {
    private int mChildPerLines;
    private int[] mMeasuredDimension = new int[2];

    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        this.mChildPerLines = spanCount;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {

        View view = recycler.getViewForPosition(0);
        measureChild(view, widthSpec, heightSpec);
        int measuredWidth = View.MeasureSpec.getSize(widthSpec);
        int measuredHeight = view.getMeasuredHeight();
        setMeasuredDimension(measuredWidth, measuredHeight);
    }
}
