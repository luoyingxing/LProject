package com.luo.project.event;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * ScrollLayout  首页定制
 * 下拉超过一定距离则显示背部的布局
 * 所以，该布局支持两个布局，一个是正常显示，一个是下拉一定距离才显示。
 * <p>
 * <p>
 * ScrollLayout 包含前面和后面两部分布局，当前端的子View在屏幕可见时，才进行后面布局的显示或隐藏操作
 * <ScrollLayout>
 * ----<ViewGroup></ViewGroup>
 * ----<ScrollView></ScrollView>
 * </ScrollLayout>
 * <p>
 * <p>
 * Created by luoyingxing on 2017/4/5.
 */

public class ScrollLayoutOld extends ViewGroup {
    private static final String TAG = "ScrollLayout";
    /**
     * 后面布局的高度
     */
    private int mBottomViewHeight;

    public ScrollLayoutOld(Context context) {
        super(context);
    }

    public ScrollLayoutOld(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollLayoutOld(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 需要支持margin，直接使用系统的MarginLayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 负责设置子控件的测量模式和大小，根据所有子控件设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLog("onMeasure()");
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

//        mLog(sizeWidth + "," + sizeHeight);

        int width = 0;
        int height = 0;

        int childCount = getChildCount();
        if (childCount == 1) {
            try {
                throw new Exception("missing bottom layout, please add bottom layout in first level!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (childCount > 2) {
            try {
                throw new Exception("ScrollLayout can only include two child view!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        View bottomView = getChildAt(0);
        View contentView = getChildAt(1);

        // 测量bottomView的宽和高
        measureChild(bottomView, widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams lp = (MarginLayoutParams) bottomView.getLayoutParams();
        int bottomWidth = bottomView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        int bottomHeight = bottomView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
        mBottomViewHeight = bottomHeight;

        // 测量contentView的宽和高
        measureChild(contentView, widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams params = (MarginLayoutParams) contentView.getLayoutParams();
        int contentWidth = contentView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
        int contentHeight = contentView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

        width = contentWidth;
        height = contentHeight;

        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width,
                (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLog("onLayout()");
        int width = getWidth();
        int height = getHeight();

        int childCount = getChildCount();
        if (childCount == 1) {
            try {
                throw new Exception("missing bottom layout, please add bottom layout in first level!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (childCount > 2) {
            try {
                throw new Exception("ScrollLayout can only include two child view!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        View bottomView = getChildAt(0);
        View contentView = getChildAt(1);


        MarginLayoutParams params = (MarginLayoutParams) contentView.getLayoutParams();

        //计算bottomView的left,top,right,bottom
        int leftB = params.leftMargin;
        int topB = params.topMargin;
        int rightB = params.rightMargin + bottomView.getMeasuredWidth();
        int bottomB = params.bottomMargin + bottomView.getMeasuredHeight();

        bottomView.layout(leftB, topB, rightB, bottomB);

        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();

        //计算childView的left,top,right,bottom
        int left = lp.leftMargin;
        int top = lp.topMargin;
        int right = lp.rightMargin + contentView.getMeasuredWidth();
        int bottom = lp.bottomMargin + contentView.getMeasuredHeight();

        contentView.layout(left, mContentTopY + top, right, mContentTopY + bottom);
    }

    /**
     * 内容View的绘制top位置，通过改变该值，来控制内容View的高度，进而显示底部的 view
     */
    private int mContentTopY = 0;
    /**
     * 记录最后触摸的Y值
     */
    private float mLastY = 0;
    /**
     * 记录最后触摸的Y值
     */
    private float mPullDownY = 0;
    /**
     * 过滤多点触摸
     */
    private int mEvents = 0;

    //    private int  radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()* (pullDownY + Math.abs(pullUpY))));
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float mRadio = 2;

    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                mEvents = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                ViewGroup view = (ViewGroup) getChildAt(1);
                int childCount = view.getChildCount();
                if (childCount == 2) {
                    ViewGroup scrollView = (ViewGroup) view.getChildAt(1);
                    ViewGroup linearLayout = (ViewGroup) scrollView.getChildAt(0);
                    if (linearLayout.getChildCount() > 0) {
                        if (linearLayout.getChildAt(0).getGlobalVisibleRect(new Rect())) {

                            mPullDownY = mPullDownY + (ev.getY() - mLastY) / mRadio;

                            mRadio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (mPullDownY)));

                            mContentTopY = (int) mPullDownY;

                            if (mContentTopY < 0) {
                                mContentTopY = 0;
                            }

                            if (mContentTopY > mBottomViewHeight) {
                                mContentTopY = mBottomViewHeight;
                            }

                            mLastY = ev.getY();
                            requestLayout();
                        } else {
                            if (mContentTopY > 0) {
                                mPullDownY = mPullDownY + (ev.getY() - mLastY) / mRadio;

                                mRadio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (mPullDownY)));

                                if (mPullDownY < 0) {
                                    mPullDownY = 0;
                                }

                                if (mPullDownY > mBottomViewHeight) {
                                    mPullDownY = mBottomViewHeight;
                                }

                                mContentTopY = (int) mPullDownY;

                                requestLayout();
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void mLog(Object object) {
        Log.i(TAG, object + "");
    }
}