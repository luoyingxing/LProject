package com.luo.project.event;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * ScrollLayout, must include two child View and only two child View.
 * bottomGroupView,the distance of pull down over default{mDistance} can show this ViewGroup.
 * contentGroupView, include any view
 * <p>
 * Created by luoyingxing on 2017/4/5.
 */

public class ScrollLayout extends ViewGroup {
    private static final String TAG = "ScrollLayout";
    /**
     * the height of ContentView
     */
    private int mContentTopY = 0;
    /**
     * Record the last touch the Y value
     */
    private float mLastY = 0;
    /**
     * Filtering multi-touch
     */
    private int mEvents = 0;
    /**
     * the distance of pull down
     */
    public float mPullDownY = 0;
    /**
     * the distance of pull up
     */
    private float mPullUpY = 0;
    /**
     * can pull up
     */
    private boolean mCanPullUp = false;
    /**
     * the bottomView whether is spread
     */
    private boolean mSpread = false;
    /**
     * Under the fingers sliding distance and sliding distance than the first,
     * In the middle will change with tangent function,
     * According to the distance change ratio
     */
    private float mRadio = 2;
    /**
     * the default distance for pull to show or hide bottomView
     */
    private int mDistance = 200;
    /**
     * the height of bottom's view
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
     * need backup the margin，use system's MarginLayoutParams
     */
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        if (getChildCount() != 2) {
            try {
                throw new Exception("ScrollLayout must include two child View and only two child View!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        View bottomView = getChildAt(0);
        View contentView = getChildAt(1);

        // measure bottomView's height and width
        measureChild(bottomView, widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams lp = (MarginLayoutParams) bottomView.getLayoutParams();
        int bottomWidth = bottomView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        int bottomHeight = bottomView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
        mBottomViewHeight = bottomHeight;

        // measure contentView's height and width
        measureChild(contentView, widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams params = (MarginLayoutParams) contentView.getLayoutParams();
        int contentWidth = contentView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
        int contentHeight = contentView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : contentWidth,
                (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : contentHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() != 2) {
            try {
                throw new Exception("ScrollLayout must include two child View and only two child View!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        View bottomView = getChildAt(0);
        View contentView = getChildAt(1);

        MarginLayoutParams params = (MarginLayoutParams) contentView.getLayoutParams();

        int leftB = params.leftMargin;
        int topB = params.topMargin;
        int rightB = params.rightMargin + bottomView.getMeasuredWidth();
        int bottomB = params.bottomMargin + bottomView.getMeasuredHeight();

        bottomView.layout(leftB, topB, rightB, bottomB);

        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();

        int left = lp.leftMargin;
        int top = lp.topMargin;
        int right = lp.rightMargin + contentView.getMeasuredWidth();
        int bottom = lp.bottomMargin + contentView.getMeasuredHeight();

        contentView.layout(left, mContentTopY + top, right, mContentTopY + bottom);
    }

    /**
     * @return true is can pull down , otherwise.
     */
    private boolean canPullDown() {
        if (getChildCount() != 2) {
            try {
                throw new Exception("ScrollLayout must include two child View and only two child View!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Rect local = new Rect();
        getChildAt(1).getLocalVisibleRect(local);
        return getChildCount() != 1 && local.top == 0;
    }

    /**
     * control the ScrollVLayout show or hide Bottom View.
     *
     * @param ev MotionEvent
     * @return true is consumed the event, otherwise
     */
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
                            mCanPullUp = true;
                        }
                        if (mPullDownY > getMeasuredHeight()) {
                            mPullDownY = getMeasuredHeight();
                        }
                    } else if (mPullUpY < 0 && mCanPullUp) {
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
                mRadio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (mPullDownY + Math.abs(mPullUpY))));
                if (mPullDownY > 0 || mPullUpY < 0) {
                    requestLayout();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mSpread) {
                    if (mContentTopY < mBottomViewHeight - mDistance) {
                        mPullDownY = 0;
                        mContentTopY = 0;
                        mPullUpY = 0;
                        mCanPullUp = false;
                        mSpread = false;
                    } else {
                        mPullDownY = mBottomViewHeight;
                        mContentTopY = mBottomViewHeight;
                        mCanPullUp = true;
                        mSpread = true;
                    }
                } else {
                    if (mContentTopY > mDistance) {
                        mPullDownY = mBottomViewHeight;
                        mContentTopY = mBottomViewHeight;
                        mCanPullUp = true;
                        mSpread = true;
                    } else {
                        mPullDownY = 0;
                        mContentTopY = 0;
                        mPullUpY = 0;
                        mCanPullUp = false;
                        mSpread = false;
                    }
                }

                requestLayout();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * when the bottom view is spread, stop nested scroll,return true to implement
     *
     * @param ev MotionEvent
     * @return true is consumed the event, otherwise
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return ev.getActionMasked() == MotionEvent.ACTION_MOVE && mContentTopY > 0 || super.onInterceptTouchEvent(ev);
    }
}