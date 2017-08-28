package com.luo.project.refresh;

import android.content.Context;
import android.util.AttributeSet;


/**
 * BaseFooter
 * <p>
 * Created by luoyingxing on 2017/8/28.
 */

public class BaseFooter extends RefreshLayout implements Footer {

    public BaseFooter(Context context) {
        super(context);
    }

    public BaseFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPullingUp(float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onPullReleasing(float percent, int offset, int footerHeight, int extendHeight) {

    }
}