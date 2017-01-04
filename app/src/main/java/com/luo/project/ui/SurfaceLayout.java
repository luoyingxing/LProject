package com.luo.project.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.luo.project.R;

/**
 * SurfaceLayout
 * <p>
 * Created by Administrator on 2017/1/4.
 */

public class SurfaceLayout extends ViewGroup {
    private static final String TAG = "SurfaceLayout";
    private Context mContext;

    private int mColumn = 1;
    private int mWidth;
    private int mHeight;

    public SurfaceLayout(Context context) {
        super(context);
        init(context, null);
    }

    public SurfaceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SurfaceLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SurfaceLayout, 0, 0);
        mColumn = array.getInteger(R.styleable.SurfaceLayout_column, 1);

        array.recycle();
    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        mLog("widthMode =" + widthMode);
        mLog("heightMode =" + heightMode);
        mLog("sizeWidth =" + sizeWidth);
        mLog("sizeHeight =" + sizeHeight);

        mWidth = sizeWidth;
        mHeight = sizeHeight;

        mLog((heightMode == MeasureSpec.UNSPECIFIED) + "," + sizeHeight + "," + getLayoutParams().height);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
//        int width = 0;
//        int height = 0;


        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */
//        for (int i = 0; i < getChildCount(); i++) {
//            View childView = getChildAt(i);
//        }

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childWidth = mWidth / mColumn;
        int childHeight = mHeight / mColumn;

        mLog("childWidth =" + childWidth);
        mLog("childHeight =" + childHeight);

        int curLeft = 0;
        int curTop = 0;
        int curRight = childWidth;
        int curBottom = childHeight;

        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         */
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);

            childView.layout(curLeft, curTop, curRight, curBottom);

            curLeft += childWidth;
            curRight += childWidth;

            if ((i + 1) % mColumn == 0) {
                curLeft = 0;
                curTop += childHeight;
                curRight = childWidth;
                curBottom += childHeight;
            }


        }

    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        Log.e(TAG, "generateDefaultLayoutParams");
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p) {
        Log.e(TAG, "generateLayoutParams p");
        return new MarginLayoutParams(p);
    }

	/*
     * if (heightMode == MeasureSpec.UNSPECIFIED)
		{
			int tmpHeight = 0 ;
			LayoutParams lp = getLayoutParams();
			if (lp.height == LayoutParams.MATCH_PARENT)
			{
				Rect outRect = new Rect();
				getWindowVisibleDisplayFrame(outRect);
				tmpHeight = outRect.height();
			}else
			{
				tmpHeight = getLayoutParams().height ;
			}
			height = Math.max(height, tmpHeight);

		}
	 */

    private void mLog(String msg) {
        Log.e(TAG, msg);
    }
}
