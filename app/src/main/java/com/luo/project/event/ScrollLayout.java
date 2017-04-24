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

public class ScrollLayout extends ViewGroup {
    private static final String TAG = "ScrollLayout";

    /**
     * 后面布局的高度
     */
    private int mBottomViewHeight;

    public ScrollLayout(Context context) {
        super(context);
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 需要支持margin，直接使用系统的MarginLayoutParams
     */
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 负责设置子控件的测量模式和大小，根据所有子控件设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
     * 过滤多点触摸
     */
    private int mEvents = 0;

    /**
     * 下拉的距离
     */
    public float mPullDownY = 0;
    /**
     * 上拉的距离
     */
    private float mPullUpY = 0;
    /**
     * 可下拉
     */
    private boolean canPullUp = false;
    /**
     * 是否展开了底层布局
     */
    private boolean mSpread = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化，根据下拉距离改变比例
    private float mRadio = 2;

    private void log() {
        Rect rect = new Rect();
        Rect rect1 = new Rect();
        getChildAt(1).getGlobalVisibleRect(rect);
        getChildAt(1).getLocalVisibleRect(rect1);
        mLog("Global: " + rect.top + " Local: " + rect1.top);
    }

    private boolean canPullDown() {
        Rect glob = new Rect();
        getChildAt(1).getGlobalVisibleRect(glob);

        Rect local = new Rect();
        getChildAt(1).getLocalVisibleRect(local);

        return getChildCount() != 1 && local.top == 0;
    }

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
                if (mEvents == 0) {
                    if (mPullDownY > 0 || canPullDown()) {
                        mPullDownY = mPullDownY + (ev.getY() - mLastY) / mRadio;
                        mContentTopY = (int) mPullDownY;
                        if (mPullDownY < 0) {
                            mPullDownY = 0;
                            canPullUp = true;
                        }
                        if (mPullDownY > getMeasuredHeight()) {
                            mPullDownY = getMeasuredHeight();
                        }

                    } else if (mPullUpY < 0 && canPullUp) {
                        mPullUpY = mPullUpY + (ev.getY() - mLastY) / mRadio;
                        mContentTopY = (int) mPullUpY;
                        if (mPullUpY > 0) {
                            mPullUpY = 0;
                            canPullUp = false;
                        }
                        if (mPullUpY < -getMeasuredHeight()) {
                            mPullUpY = -getMeasuredHeight();
                        }
                    }
                } else {
                    mEvents = 0;
                }

                mLastY = ev.getY();
                mRadio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (mPullDownY + Math.abs(mPullUpY))));
                if (mPullDownY > 0 || mPullUpY < 0) {
                    requestLayout();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mSpread) {
                    if (mContentTopY < mBottomViewHeight - 50) {
                        mPullDownY = 0;
                        mContentTopY = 0;
                        mPullUpY = 0;
                        canPullUp = false;
                        mSpread = false;
                    } else {
                        mPullDownY = mBottomViewHeight;
                        mContentTopY = mBottomViewHeight;
                        canPullUp = true;
                        mSpread = true;
                    }
                } else {
                    if (mContentTopY > 50) {
                        mPullDownY = mBottomViewHeight;
                        mContentTopY = mBottomViewHeight;
                        canPullUp = true;
                        mSpread = true;
                    } else {
                        mPullDownY = 0;
                        mContentTopY = 0;
                        mPullUpY = 0;
                        canPullUp = false;
                        mSpread = false;
                    }
                }

                requestLayout();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void mLog(Object object) {
        Log.i(TAG, object + "");
    }

//    @Override
//    public boolean onInterceptHoverEvent(MotionEvent ev) {
//        mLog("Hover");
//        if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
//            if (mEvents == 0 && mPullUpY < 0 && canPullUp) {
//                mPullUpY = mPullUpY + (ev.getY() - mLastY) / mRadio;
//                mContentTopY = (int) mPullUpY;
//                if (mPullUpY > 0) {
//                    mPullUpY = 0;
//                    canPullUp = false;
//                }
//                if (mPullUpY < -getMeasuredHeight()) {
//                    mPullUpY = -getMeasuredHeight();
//                }
//                return true;
//            }
//        }
//        return super.onInterceptHoverEvent(ev);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        mLog("Touch");
//        if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
//            if (mEvents == 0 && mPullUpY < 0 && canPullUp) {
//                mPullUpY = mPullUpY + (ev.getY() - mLastY) / mRadio;
//                mContentTopY = (int) mPullUpY;
//                if (mPullUpY > 0) {
//                    mPullUpY = 0;
//                    canPullUp = false;
//                }
//                if (mPullUpY < -getMeasuredHeight()) {
//                    mPullUpY = -getMeasuredHeight();
//                }
//                return true;
//            }
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
}