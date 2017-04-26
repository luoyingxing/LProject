package com.luo.project.event;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * TestLayout
 * <p>
 * Created by luoyingxing on 2017/4/26.
 */

public class TestLayout extends LinearLayout {
    public TestLayout(Context context) {
        super(context);
    }

    public TestLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("TestLayout", "onInterceptTouchEvent()" + ev.getActionMasked());
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("TestLayout", "onTouchEvent()" + event.getActionMasked());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("TestLayout", "dispatchTouchEvent()" + ev.getActionMasked());
        return super.dispatchTouchEvent(ev);
    }
}
