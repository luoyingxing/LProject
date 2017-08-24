package com.luo.project.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * TestLayout
 * <p>
 * Created by luoyingxing on 2017/8/24.
 */

public class TestLayout extends LinearLayout {

    private void mLog(Object object) {
        Log.d("TestLayout", "" + object.toString());
    }

    public TestLayout(Context context) {
        super(context);
    }

    public TestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mLog("onTouchEvent()");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        mLog("dispatchKeyEvent()");
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mLog("dispatchTouchEvent()");
        return super.dispatchTouchEvent(ev);
    }
}
