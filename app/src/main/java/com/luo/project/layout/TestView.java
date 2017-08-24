package com.luo.project.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * TestView
 * <p>
 * Created by luoyingxing on 2017/8/24.
 */

public class TestView extends View {

    private void mLog(Object object) {
        Log.v("TestView", "" + object.toString());
    }

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        mLog("dispatchTouchEvent()");
        return super.dispatchTouchEvent(event);
    }

}