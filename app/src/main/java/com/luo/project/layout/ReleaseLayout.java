package com.luo.project.layout;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ReleaseLayout
 * <p/>
 * Created by luoyingxing on 16/10/25.
 */
public class ReleaseLayout extends ViewGroup {
    /**
     * 下拉的最高度
     */
    private float mMaxHeight = 200;
    /**
     * 下拉的速率
     */
    private float mSpeed = 8;
    /**
     * 下拉的高度
     */
    private float mPullDownY;
    /**
     * 时刻的Y坐标
     */
    private float mDownY;
    /**
     * 下拉前的Y坐标
     */
    private float mLastY;


    public ReleaseLayout(Context context) {
        super(context);
        init();
    }

    public ReleaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReleaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        };
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int chileCount = getChildCount();

        mLog("chileCount = " + chileCount);

//        if (chileCount == 1) {


        View view = getChildAt(0);

        mLog("getWidth() = " + getWidth());
        mLog("mPullDownY = " + mPullDownY);
        mLog("view.getHeight() = " + view.getHeight());

        view.layout(0, (int) mPullDownY, getMeasuredWidth(), (int) mPullDownY + view.getMeasuredHeight());


//        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mLastY = mDownY;
                mLog("dispatchTouchEvent - mLastY = " + mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                mDownY = ev.getY();

                mPullDownY = mDownY - mLastY;

                mPullDownY = mPullDownY - mPullDownY / mSpeed;


                mLog("dispatchTouchEvent - mPullDownY = " + mPullDownY);

                if (mPullDownY < 0) {
                    mPullDownY = 0;
                }

                if (mPullDownY > 0) {
                    requestLayout();
                }

                break;
            case MotionEvent.ACTION_UP:


                int temp = (int) (mPullDownY % 5) + 1;

//                for (int i = 0; i < temp; i++) {
//                    mPullDownY --;
//                    requestLayout();
//                }

//                while (mPullDownY > 0) {
//                    mPullDownY--;
//                    requestLayout();
//                }

                hide();

                mLog("dispatchTouchEvent - ACTION_UP = " + mPullDownY);
                break;
        }


        super.dispatchTouchEvent(ev);
        return true;
    }


    private void hide() {
        mTimer = new Timer();


        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        };

        mTimer.schedule(mTimerTask, 0, 10);
    }

    private int temp = 1;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mPullDownY > 0) {
                temp++;
                mPullDownY = mPullDownY - temp;
                requestLayout();
            } else {
                mTimer.cancel();
                mTimerTask.cancel();
            }


        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public static int getDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED: //相当于match_parent
                result = size;
                break;
            case MeasureSpec.AT_MOST: //相当于wrap_content
            case MeasureSpec.EXACTLY: //相当于xx dp
                result = specSize;
                break;
        }
        return result;
    }

    private Timer mTimer;
    private TimerTask mTimerTask;


    /**
     * mLog("ACTION_UP");
     *
     * @param msg message
     */
    private void mLog(Object msg) {
        Log.e(ReleaseLayout.class.getSimpleName(), "" + msg);
    }

}