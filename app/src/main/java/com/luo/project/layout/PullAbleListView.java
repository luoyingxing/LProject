package com.luo.project.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author hcc on 2016.2.18
 */
public class PullAbleListView extends ListView implements PullAble {

    private boolean canPullDown = true;
    private boolean canPullUp = true;

    public PullAbleListView(Context context) {
        super(context);
    }

    public PullAbleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullAbleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isCanPullDown() {
        return canPullDown;
    }

    public void setCanPullDown(boolean canPullDown) {
        this.canPullDown = canPullDown;
    }

    public boolean isCanPullUp() {
        return canPullUp;
    }

    public void setCanPullUp(boolean canPullUp) {
        this.canPullUp = canPullUp;
    }

    @Override
    public boolean canPullDown() {

        if (!canPullDown) {
            return false;
        }

        if (getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0) {
            // 滑到ListView的顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {

        if (!canPullUp){
            return false;
        }

        if (getCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1)) {
            // 滑到底部了
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(
                    getLastVisiblePosition()
                            - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                return true;
        }
        return false;
    }
}
