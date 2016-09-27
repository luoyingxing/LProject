package com.luo.project.ViewGroup;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * LuoView
 * <p/>
 * Created by luoyingxing on 16/9/26.
 */
public class LuoView extends ViewGroup {
    private static final String TAG = "LuoView";

    public LuoView(Context context) {
        super(context);
    }

    public LuoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LuoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:

                mLog_d("CANCEL");

                break;
            case MotionEvent.ACTION_DOWN:

                mLog_d("down");

                break;
            case MotionEvent.ACTION_MOVE:

                mLog_d("move");

                break;
        }


        return super.dispatchTouchEvent(ev);
    }


    private void mLog_d(String msg) {
        Log.d(TAG, msg);
    }
}