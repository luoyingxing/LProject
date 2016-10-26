package com.luo.project.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewLayout
 * <p/>
 * Created by luoyingxing on 16/10/23.
 */
public class ViewLayout extends ViewGroup {
    private static final String TAG = ViewLayout.class.getSimpleName();

    private Context mContext;

    // 过滤多点触碰
    private int mEvents;

    // 刷新成功
    public static final int SUCCEED = 0;
    // 刷新失败
    public static final int FAIL = 1;


    // 下拉头
    private View refreshView;
    // 下拉的箭头
    private View pullableView;
    // 正在刷新的图标
    private View loadmoreView;

    private boolean isLayout = false;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;

    public ViewLayout(Context context) {
        super(context);
        init(context);
    }

    public ViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int mViewGroupWidth = getMeasuredWidth();  //当前ViewGroup的总宽度
        int mViewGroupHeight = getMeasuredHeight(); //当前ViewGroup的总高度

        int mPainterPosX = left; //当前绘图光标横坐标位置
        int mPainterPosY = top;  //当前绘图光标纵坐标位置


        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);

            if (!isLayout) {
                // 这里是第一次进来的时候做一些初始化
                refreshView = getChildAt(0);
                pullableView = getChildAt(1);
                loadmoreView = getChildAt(2);
                isLayout = true;
            }
            // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
            refreshView.layout(0,
                    (int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
                    refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
            pullableView.layout(0, (int) (pullDownY + pullUpY),
                    pullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
                            + pullableView.getMeasuredHeight());
            loadmoreView.layout(0,
                    (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight(),
                    loadmoreView.getMeasuredWidth(),
                    (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight()
                            + loadmoreView.getMeasuredHeight());

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
//        mLog("width=" + width + ",height=" + height);
//        setMeasuredDimension(width, height);

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

    /**
     * 下拉的距离
     */
    private float pullDownY;
    /**
     * 上拉的距离
     */
    private float pullUpY;

    private float downY;
    private float upY;

    private float lastY;

    private float radio = 8;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                lastY = downY;
                mLog("downY = " + downY);
                mEvents = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
//                upY = ev.getY();
//                mLog("upY = " + upY);
//
                pullDownY = (int) (upY - downY);

                mLog(pullDownY);

                break;
            case MotionEvent.ACTION_MOVE:
                pullDownY = pullDownY + (ev.getY() - lastY) / radio;


                if (pullDownY > 0) {
                    // 可以下拉，正在加载时不能下拉
                    // 对实际滑动距离做缩小，造成用力拉的感觉
                    pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                    if (pullDownY < 0) {
                        pullDownY = 0;
                    }
                    if (pullDownY > getMeasuredHeight())
                        pullDownY = getMeasuredHeight();
                } else if (pullUpY < 0) {
                    // 可以上拉，正在刷新时不能上拉
                    pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                    if (pullUpY > 0) {
                        pullUpY = 0;
                    }
                    if (pullUpY < -getMeasuredHeight())
                        pullUpY = -getMeasuredHeight();
                }

                mLog("pullDownY = " + pullDownY);

                lastY = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
                        * (pullDownY + Math.abs(pullUpY))));
                if (pullDownY > 0 || pullUpY < 0)
                    requestLayout();


                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                // Math.abs(pullUpY))就可以不对当前状态作区分了
                if ((pullDownY + Math.abs(pullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }


                break;

            case MotionEvent.ACTION_UP:
                if (pullDownY > 200 || -pullUpY > 200) {
                    // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏

                    isTouch = false;
                }


                break;

        }


        super.dispatchTouchEvent(ev);
        return true;
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewLayout.LayoutParams(getContext(), attrs);
    }


    /**
     * mLog("ACTION_UP");
     *
     * @param msg message
     */
    private void mLog(Object msg) {
        Log.e(TAG, "" + msg);
    }
}
