package com.luo.project.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

/**
 * RefreshLayout
 * <p>
 * Created by luoyingxing on 2017/8/28.
 */

public class RefreshLayout extends ViewGroup {
    private static final String TAG = "RefreshLayout";
    private int mWindowWidth;
    private int mWindowHeight;
    private int mHeaderHeight = 200;
    private int mFooterHeight = 200;
    private View mHeaderView;
    private View mContentView;
    private View mFooterView;

    private Status mStatus = Status.INIT;

    private enum Status {
        INIT,
        RELEASE_TO_REFRESH,
        REFRESHING,
        RELEASE_TO_LOAD,
        LOADING,
        DONE
    }

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
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mWindowWidth = metrics.widthPixels;
        mWindowHeight = metrics.heightPixels;
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
            mHeaderView.layout(0,
                    (int) (mPullDownY + mPullUpY) - mHeaderView.getMeasuredHeight(),
                    mHeaderView.getMeasuredWidth(),
                    (int) (mPullDownY + mPullUpY));
        }

        mContentView.layout(0,
                (int) (mPullDownY + mPullUpY),
                mContentView.getMeasuredWidth(),
                (int) (mPullDownY + mPullUpY) + mContentView.getMeasuredHeight());


        if (mFooterView != null) {
            mFooterView.layout(0,
                    (int) (mPullDownY + mPullUpY) + mContentView.getMeasuredHeight(),
                    mFooterView.getMeasuredWidth(),
                    (int) (mPullDownY + mPullUpY) + mContentView.getMeasuredHeight() + mFooterView.getMeasuredHeight());
        }

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

    /**
     * @return true is can pull down , otherwise.
     */
    private boolean canPullDown() {
        if (null != mContentView) {

            if (mContentView instanceof AbsListView) {
                int firstVisiblePosition = ((AbsListView) mContentView).getFirstVisiblePosition();
                int lastVisiblePosition = ((AbsListView) mContentView).getLastVisiblePosition();

                if (firstVisiblePosition == 0) {
                    View topChildView = ((AbsListView) mContentView).getChildAt(0);
                    return topChildView.getTop() == 0;
                }
            }

//            Rect local = new Rect();
//            mContentView.getLocalVisibleRect(local);
//            Log.w(TAG, "Down : " + local.left + "," + local.top + "," + local.right + "," + local.bottom);
//            return 0 == local.top;
        }
        return false;
    }

    private boolean canPullUp() {
        if (null != mContentView) {
            if (mContentView instanceof AbsListView) {
                int firstVisiblePosition = ((AbsListView) mContentView).getFirstVisiblePosition();
                int lastVisiblePosition = ((AbsListView) mContentView).getLastVisiblePosition();

                if (lastVisiblePosition == (((AbsListView) mContentView).getCount() - 1)) {
                    View bottomChildView = ((AbsListView) mContentView).getChildAt(lastVisiblePosition - firstVisiblePosition);
                    return mContentView.getHeight() >= bottomChildView.getBottom();
                }
            }

//            Rect local = new Rect();
//            mContentView.getGlobalVisibleRect(local);

//            Log.i(TAG, "Up : " + local.left + "," + local.top + "," + local.right + "," + local.bottom);

//            return local.bottom == mContentView.getMeasuredHeight();
        }
        return false;
    }

    private float mPullDownY;
    private float mPullUpY;
    private float mDownY;
    private float mLastY;
    private float mRadio = 4;
    private int mEvents;
    private boolean mCanPullDown;
    private boolean mCanPullUp;
    private boolean mIsTouch;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "DownY " + mPullDownY);
        Log.e(TAG, "UpY " + mPullUpY);

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mLastY = mDownY;
//                timer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
//                Rect local = new Rect();
//                mContentView.getLocalVisibleRect(local);
//                Rect local1 = new Rect();
//                mContentView.getGlobalVisibleRect(local1);
//                Log.e(TAG, "ContentView.getMeasuredHeight() -- " + mContentView.getMeasuredHeight());
//                Log.e(TAG, "ContentView.getY() -- " + mContentView.getY());
//                Log.e(TAG, "ContentView.getHeight() -- " + mContentView.getHeight());
//                Log.i(TAG, "local   " + local.left + "," + local.top + "," + local.right + "," + local.bottom);
//                Log.i(TAG, "Global   " + local1.left + "," + local1.top + "," + local1.right + "," + local1.bottom);

                if (mEvents == 0) {
                    if (mPullDownY > 0 || canPullDown() && mCanPullDown && mStatus != Status.LOADING) {
                        // 可以下拉，正在加载时不能下拉

                        mPullDownY = mPullDownY + (ev.getY() - mLastY) / mRadio;
                        if (mPullDownY < 0) {
                            mPullDownY = 0;
                            mCanPullDown = false;
                            mCanPullUp = true;
                        }

                        if (mPullDownY > getMeasuredHeight()) {
                            mPullDownY = getMeasuredHeight();
                        }

                        if (mStatus == Status.REFRESHING) {
                            // 正在刷新的时候触摸移动
                            mIsTouch = true;
                        }

                    } else if (mPullUpY < 0 || canPullUp() && mCanPullUp && mStatus != Status.REFRESHING) {
                        // 可以上拉，正在刷新时不能上拉
                        mPullUpY = mPullUpY + (ev.getY() - mLastY) / mRadio;
                        if (mPullUpY > 0) {
                            mPullUpY = 0;
                            mCanPullDown = true;
                            mCanPullUp = false;
                        }
                        if (mPullUpY < -getMeasuredHeight()) {
                            mPullUpY = -getMeasuredHeight();
                        }

                        if (mStatus == Status.LOADING) {
                            // 正在加载的时候触摸移动
                            mIsTouch = true;
                        }
                    } else
                        releasePull();
                } else {
                    mEvents = 0;
                }

                mLastY = ev.getY();
                // 根据下拉距离改变比例
                mRadio = (float) (4 + 4 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (mPullDownY + Math.abs(mPullUpY))));
                if (mPullDownY > 0 || mPullUpY < 0) {
                    requestLayout();
                }
                if (mPullDownY > 0) {
                    if (mPullDownY <= mHeaderHeight && (mStatus == Status.RELEASE_TO_REFRESH || mStatus == Status.DONE)) {
                        // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                        changeState(Status.INIT);
                    }
                    if (mPullDownY >= mHeaderHeight && mStatus == Status.INIT) {
                        // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                        changeState(Status.RELEASE_TO_REFRESH);
                    }
                } else if (mPullUpY < 0) {
                    // 下面是判断上拉加载的，同上，注意mPullUpY是负值
                    if (-mPullUpY <= mFooterHeight && (mStatus == Status.RELEASE_TO_LOAD || mStatus == Status.DONE)) {
                        changeState(Status.INIT);
                    }
                    // 上拉操作
                    if (-mPullUpY >= mFooterHeight && mStatus == Status.INIT) {
                        changeState(Status.RELEASE_TO_LOAD);
                    }

                }
                // 因为刷新和加载操作不能同时进行，所以mPullDownY和mPullUpY不会同时不为0，因此这里用(mPullDownY +
                // Math.abs(mPullUpY))就可以不对当前状态作区分了
                if ((mPullDownY + Math.abs(mPullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "changeState() -- ACTION_UP");
                if (mPullDownY > mHeaderHeight || -mPullUpY > mFooterHeight) {
                    // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                    mIsTouch = false;
                }

                Log.e(TAG, "changeState() -- mStatus " + mStatus);

                if (mStatus == Status.RELEASE_TO_REFRESH) {
                    changeState(Status.REFRESHING);
                    mPullDownY = mHeaderHeight;
                    // 刷新操作
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh(this);
                    }
                } else if (mStatus == Status.RELEASE_TO_LOAD) {
                    changeState(Status.LOADING);
                    mPullUpY = -mFooterHeight;
                    // 加载操作
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onLoadMore(this);
                    }
                } else {
                    mPullUpY = 0;
                    mPullDownY = 0;
                }

                requestLayout();
            default:
                break;
        }

        super.dispatchTouchEvent(ev);
        return true;
    }

    private void changeState(Status status) {
        mStatus = status;
        Log.d(TAG, "changeState() -- " + status);
    }

    public void onRefreshComplete() {
        mPullUpY = 0;
        mPullDownY = 0;
        requestLayout();
        mStatus = Status.INIT;
    }

    public void onLoadMoreComplete() {
        mPullUpY = 0;
        mPullDownY = 0;
        requestLayout();
        mStatus = Status.INIT;
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        mCanPullDown = true;
        mCanPullUp = true;
    }

    private OnRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    /**
     * 刷新加载回调接口
     *
     * @author chenjing
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh(RefreshLayout pullToRefreshLayout);

        /**
         * 加载操作
         */
        void onLoadMore(RefreshLayout pullToRefreshLayout);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return ev.getActionMasked() == MotionEvent.ACTION_DOWN && mStatus == Status.LOADING  || super.onInterceptTouchEvent(ev);
//    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.d(TAG, "requestDisallowInterceptTouchEvent()");
//        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }
}