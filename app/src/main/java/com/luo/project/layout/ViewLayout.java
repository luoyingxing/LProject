package com.luo.project.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * ViewLayout
 * <p/>
 * Created by luoyingxing on 16/10/23.
 */
public class ViewLayout extends ViewGroup {
    public ViewLayout(Context context) {
        super(context);
    }

    public ViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
