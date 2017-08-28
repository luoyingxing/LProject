package com.luo.project.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * RefreshLayout
 * <p>
 * Created by luoyingxing on 2017/8/28.
 */

public class RefreshLayout extends ViewGroup {
    private static final String TAG = "RefreshLayout";
    private int mHeaderHeight = 200;
    private int mFooterHeight = 200;
    private View mHeaderView;
    private View mContentView;
    private View mFooterView;

    public RefreshLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

    }

    @Override
    protected void onFinishInflate() {
        Log.d(TAG, "onFinishInflate()");
        super.onFinishInflate();
        int count = getChildCount();

        if (1 == count) {
            View view = getChildAt(0);
            //强制默认为内容布局
            if (null == mContentView) {
                mContentView = view;
            }
        } else if (3 == count) {
            View view0 = getChildAt(0);
            View view1 = getChildAt(1);
            View view2 = getChildAt(2);

            if (null == mHeaderView && view0 instanceof Header) {
                mHeaderView = view0;
            }

            if (null == mContentView) {
                mContentView = view1;
            }

            if (null == mFooterView && view2 instanceof Footer) {
                mFooterView = view2;
            }

        } else if (2 == count) {
            //遍历child view来适配
            for (int i = 0; i < count; i++) {
                if (null == mHeaderView && getChildAt(i) instanceof Header) {
                    mHeaderView = getChildAt(i);
                } else if (null == mFooterView && getChildAt(i) instanceof Footer) {
                    mFooterView = getChildAt(i);
                } else if (null == mContentView) {
                    mContentView = getChildAt(i);
                }
            }
        } else {
            throw new RuntimeException("RefreshLayout can only include three child view, and must include at most one child view");
        }

    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow()");
        super.onAttachedToWindow();
    }

    /**
     * need backup the margin，use system's MarginLayoutParams
     */
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure()");
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // measure mHeaderView's height and width
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) mHeaderView.getLayoutParams();
            int headerWidth = mHeaderView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int headerHeight = mHeaderView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            mHeaderHeight = headerHeight;
        }

        // measure mFooterView's height and width
        if (mFooterView != null) {
            measureChild(mFooterView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) mFooterView.getLayoutParams();
            int footerWidth = mFooterView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int footerHeight = mFooterView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            mFooterHeight = footerHeight;
        }

        // measure contentView's height and width
        measureChild(mContentView, widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams params = (MarginLayoutParams) mContentView.getLayoutParams();
        int contentWidth = mContentView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
        int contentHeight = mContentView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : contentWidth, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : contentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout()");

        if (mHeaderView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            int left = lp.leftMargin;
            int top = lp.topMargin;
            int right = lp.rightMargin + mHeaderView.getMeasuredWidth();
            int bottom = lp.bottomMargin + mHeaderView.getMeasuredHeight();

            mHeaderView.layout(left, top, right, bottom);
        }

        if (mFooterView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
            int left = lp.leftMargin;
            int top = lp.topMargin;
            int right = lp.rightMargin + mFooterView.getMeasuredWidth();
            int bottom = lp.bottomMargin + mFooterView.getMeasuredHeight();

            mFooterView.layout(left, getMeasuredHeight() - top, right, getMeasuredHeight());
        }

        MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
        int left = lp.leftMargin;
        int top = lp.topMargin;
        int right = lp.rightMargin + mContentView.getMeasuredWidth();
        int bottom = lp.bottomMargin + mContentView.getMeasuredHeight();

        mContentView.layout(left, top + mContentTopY, right, bottom + mContentTopY);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.d(TAG, "dispatchDraw()");
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow()");
        super.onDetachedFromWindow();
    }

    private int mContentTopY;
    private float mPullDownY;
    private float mPullUpY;
    private float mLastY;
    private float mRadio = 4;
    private int mEvents;
    private boolean mCanPullUp;

    /**
     * @return true is can pull down , otherwise.
     */
    private boolean canPullDown() {
        if (null != mContentView) {
            Rect local = new Rect();
            mContentView.getLocalVisibleRect(local);
            return 0 == local.top;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                mEvents = 0;
                return true;
//                break;
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
                            mCanPullUp = true;
                        }
                        if (mPullDownY > getMeasuredHeight()) {
                            mPullDownY = getMeasuredHeight();
                        }
                    } else if (mPullUpY < 0) {
                        mPullUpY = mPullUpY + (ev.getY() - mLastY) / mRadio;
                        mContentTopY = (int) mPullUpY;
                        if (mPullUpY > 0) {
                            mPullUpY = 0;
                            mCanPullUp = false;
                        }
                        if (mPullUpY < -getMeasuredHeight()) {
                            mPullUpY = -getMeasuredHeight();
                        }
                    }
                } else {
                    mEvents = 0;
                }

                mLastY = ev.getY();
                mRadio = (float) (4 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (mPullDownY + Math.abs(mPullUpY))));
                if (mPullDownY > 0 || mPullUpY < 0) {
                    requestLayout();
                }

                return true;

//                break;
            case MotionEvent.ACTION_UP:

                if (mHeaderHeight < mContentTopY) {
                    mContentTopY = mHeaderHeight;
                }

                requestLayout();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    public void onRefreshComplete() {
        mContentTopY = 0;
        requestLayout();
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return ev.getActionMasked() == MotionEvent.ACTION_MOVE && mContentTopY > 0 || super.onInterceptTouchEvent(ev);
//    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.d(TAG, "requestDisallowInterceptTouchEvent()");
//        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }
}