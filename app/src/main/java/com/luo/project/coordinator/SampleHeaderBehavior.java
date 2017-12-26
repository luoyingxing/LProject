package com.luo.project.coordinator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * SampleHeaderBehavior
 * <p>
 * Created by Admin on 2017/12/26.
 */

public class SampleHeaderBehavior extends CoordinatorLayout.Behavior<TextView> {

    // 界面整体向上滑动，达到列表可滑动的临界点
    private boolean upReach;
    // 列表向上滑动后，再向下滑动，达到界面整体可滑动的临界点
    private boolean downReach;
    // 列表上一个全部可见的item位置
    private int lastPosition = -1;

    public SampleHeaderBehavior() {
    }

    public SampleHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, TextView child, MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downReach = false;
                upReach = false;
                break;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, TextView child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, TextView child, View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, TextView child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        if (target instanceof RecyclerView) {
            RecyclerView list = (RecyclerView) target;
            // 列表第一个全部可见Item的位置
            int pos = ((LinearLayoutManager) list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (pos == 0 && pos < lastPosition) {
                downReach = true;
            }
            // 整体可以滑动，否则RecyclerView消费滑动事件
            if (canScroll(child, dy) && pos == 0) {
                float finalY = child.getTranslationY() - dy;
                if (finalY < -child.getHeight()) {
                    finalY = -child.getHeight();
                    upReach = true;
                } else if (finalY > 0) {
                    finalY = 0;
                }
                child.setTranslationY(finalY);
                // 让CoordinatorLayout消费滑动事件
                consumed[1] = dy;
            }
            lastPosition = pos;
        }
    }

    private boolean canScroll(View child, float scrollY) {
        if (scrollY > 0 && child.getTranslationY() == -child.getHeight() && !upReach) {
            return false;
        }

        if (downReach) {
            return false;
        }
        return true;
    }
}
