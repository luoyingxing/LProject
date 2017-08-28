package com.luo.project.refresh;

import android.content.Context;
import android.util.AttributeSet;


/**
 * BaseHeader
 * <p>
 * Created by luoyingxing on 2017/8/28.
 */

public class BaseHeader extends RefreshLayout implements Header {

    public BaseHeader(Context context) {
        super(context);
    }

    public BaseHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {

    }
}