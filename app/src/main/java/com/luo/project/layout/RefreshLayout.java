package com.luo.project.layout;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luo.project.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ReleaseLayout
 * <p/>
 * Created by luoyingxing on 16/10/25.
 */
public class RefreshLayout extends ViewGroup {
    /**
     * TAG for PullRefreshLayout
     */
    public static final String TAG = RefreshLayout.class.getSimpleName();
    /**
     * 初始状态
     */
    public static final int INIT = 0;
    /**
     * 释放刷新
     */
    public static final int RELEASE_TO_REFRESH = 1;
    /**
     * 正在刷新
     */
    public static final int REFRESHING = 2;
    /**
     * 释放加载
     */
    public static final int RELEASE_TO_LOAD = 3;
    /**
     * 正在加载
     */
    public static final int LOADING = 4;
    /**
     * 操作完毕
     */
    public static final int DONE = 5;
    /**
     * 当前状态
     */
    private int mState = INIT;

    /**
     * 刷新回调接口
     */
    private OnRefreshListener mListener;
    /**
     * 刷新成功
     */
    public static final int SUCCEED = 0;
    /**
     * 刷新失败
     */
    public static final int FAIL = 1;

    /**
     * 按下Y坐标，上一个事件点Y坐标
     */
    private float mDownY, mLastY;
    /**
     * 下拉的距离。注意：pullDownY和pullUpY必有一个为0
     */
    public float mPullDownY = 0;
    /**
     * 上拉的距离
     */
    private float mPullUpY = 0;
    /**
     * 释放刷新的距离
     */
    private float mRefreshDist = 200;
    /**
     * 释放加载的距离
     */
    private float mLoadMoreDist = 200;

    /**
     * 定时器
     */
    private MyTimer mTimer;
    /**
     * 回滚速度
     */
    public float MOVE_SPEED = 8;
    /**
     * 第一次执行布局
     */
    private boolean mIsLayout = false;
    /**
     * 在刷新过程中滑动操作
     */
    private boolean mIsTouch = false;
    /**
     * 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
     */
    private float mRadio = 2;
    /**
     * 下拉箭头的转180°动画
     */
    private RotateAnimation mRotateAnimation;
    /**
     * 均匀旋转动画
     */
    private RotateAnimation mRefreshingAnimation;
    /**
     * 下拉头
     */
    private View mRefreshView;
    /**
     * 下拉的箭头
     */
    private View mPullDownView;
    /**
     * 正在刷新的图标
     */
    private View mRefreshingView;
    /**
     * 刷新结果图标
     */
    private View mRefreshStateImageView;
    /**
     * 刷新结果：成功或失败
     */
    private TextView mRefreshStateTextView;

    /**
     * 上拉头
     */
    private View mLoadMoreView;
    /**
     * 上拉的箭头
     */
    private View mPullUpView;
    /**
     * 正在加载的图标
     */
    private View mLoadingView;
    /**
     * 加载结果图标
     */
    private View mLoadStateImageView;
    /**
     * 加载结果：成功或失败
     */
    private TextView mLoadStateTextView;

    /**
     * 实现了PullAble接口的View
     */
    private View mPullAbleView;
    /**
     * 过滤多点触碰
     */
    private int mEvents;

    /**
     * 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
     */
    private boolean mCanPullDown = true;
    private boolean mCanPullUp = true;

    private Context mContext;

    /**
     * 执行自动回滚的handler
     */
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // 回弹速度随下拉距离 moveDeltaY 增大而增大
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (mPullDownY + Math.abs(mPullUpY))));
            if (!mIsTouch) {
                // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                if (mState == REFRESHING && mPullDownY <= mRefreshDist) {
                    mPullDownY = mRefreshDist;
                    mTimer.cancel();
                } else if (mState == LOADING && -mPullUpY <= mPullUpY) {
                    mPullUpY = -mPullUpY;
                    mTimer.cancel();
                }
            }

            if (mPullDownY > 0) {
                mPullDownY -= MOVE_SPEED;
            } else if (mPullUpY < 0) {
                mPullUpY += MOVE_SPEED;
            }

            if (mPullDownY < 0) {
                // 已完成回弹
                mPullDownY = 0;
                mPullDownView.clearAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (mState != REFRESHING && mState != LOADING) {
                    changeState(INIT);
                }
                mTimer.cancel();
                requestLayout();
            }

            if (mPullUpY > 0) {
                // 已完成回弹
                mPullUpY = 0;
                mPullUpView.clearAnimation();
                // 隐藏上拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (mState != REFRESHING && mState != LOADING) {
                    changeState(INIT);
                }
                mTimer.cancel();
                requestLayout();
            }

            requestLayout();
            // 没有拖拉或者回弹完成
            if (mPullDownY + Math.abs(mPullUpY) == 0) {
                mTimer.cancel();
            }
        }
    };

    public void setOnRefreshListener(OnRefreshListener mListener) {
        this.mListener = mListener;
    }

    public RefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mTimer = new MyTimer(mHandler);

        //TODO 后期可以利用代码直接写
//        Animation animation = new RotateAnimation(context);

        mRotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotate_animation);
        mRefreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.refresh_animation);

        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        mRotateAnimation.setInterpolator(lir);
        mRefreshingAnimation.setInterpolator(lir);
    }

    private void hide() {
        mTimer.schedule(5);
    }

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     *
     * @param result RefreshLayout.SUCCEED代表成功，RefreshLayout.FAIL代表失败
     */
    public void refreshFinish(int result) {
        mRefreshingView.clearAnimation();
        mRefreshingView.setVisibility(View.GONE);
        switch (result) {
            case SUCCEED:
                // 刷新成功
                mRefreshStateImageView.setVisibility(View.VISIBLE);
                mRefreshStateTextView.setText("刷新成功！");
                mRefreshStateImageView.setBackgroundResource(R.mipmap.refresh_succeed);
                break;
            case FAIL:
            default:
                // 刷新失败
                mRefreshStateImageView.setVisibility(View.VISIBLE);
                mRefreshStateTextView.setText("刷新失败！");
                mRefreshStateImageView.setBackgroundResource(R.mipmap.refresh_failed);
                break;
        }

        if (mPullDownY > 0) {
            // 刷新结果停留0.5秒
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    changeState(DONE);
                    hide();
                }
            }.sendEmptyMessageDelayed(0, 500);
        } else {
            changeState(DONE);
            hide();
        }
    }

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     *
     * @param result RefreshLayout.SUCCEED代表成功，RefreshLayout.FAIL代表失败
     */
    public void loadmoreFinish(int result) {
        mLoadingView.clearAnimation();
        mLoadingView.setVisibility(View.GONE);
        switch (result) {
            case SUCCEED:
                // 加载成功
                mLoadStateImageView.setVisibility(View.VISIBLE);
                mLoadStateTextView.setText("加载成功！");
                mLoadStateImageView.setBackgroundResource(R.mipmap.load_succeed);
                break;
            case FAIL:
            default:
                // 加载失败
                mLoadStateImageView.setVisibility(View.VISIBLE);
                mLoadStateTextView.setText("加载失败！");
                mLoadStateImageView.setBackgroundResource(R.mipmap.load_failed);
                break;
        }

        if (mPullUpY < 0) {
            // 刷新结果停留0.5秒
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    changeState(DONE);
                    hide();
                }
            }.sendEmptyMessageDelayed(0, 500);
        } else {
            changeState(DONE);
            hide();
        }
    }

    private void changeState(int state) {
        mState = state;
        switch (mState) {
            case INIT:
                // 下拉布局初始状态
                mRefreshStateImageView.setVisibility(View.GONE);
                mRefreshStateTextView.setText("下拉刷新");
                mPullDownView.clearAnimation();
                mPullDownView.setVisibility(View.VISIBLE);

                // 上拉布局初始状态
                mLoadStateImageView.setVisibility(View.GONE);
                mLoadStateTextView.setText("上拉加载更多");
                mPullUpView.clearAnimation();
                mPullUpView.setVisibility(View.VISIBLE);
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                mRefreshStateTextView.setText("释放刷新");
                mPullDownView.startAnimation(mRotateAnimation);
                break;
            case REFRESHING:
                // 正在刷新状态
                mPullDownView.clearAnimation();
                mRefreshingView.setVisibility(View.VISIBLE);
                mPullDownView.setVisibility(View.INVISIBLE);
                mRefreshingView.startAnimation(mRefreshingAnimation);
                mRefreshStateTextView.setText("正在刷新");
                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                mLoadStateTextView.setText("释放加载更多");
                mPullUpView.startAnimation(mRotateAnimation);
                break;
            case LOADING:
                // 正在加载状态
                mPullUpView.clearAnimation();
                mLoadingView.setVisibility(View.VISIBLE);
                mPullUpView.setVisibility(View.INVISIBLE);
                mLoadingView.startAnimation(mRefreshingAnimation);
                mLoadStateTextView.setText("加载中...");
                break;
            case DONE:
                // refresh done or loadMore done , do nothing
                break;
        }
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        mCanPullDown = true;
        mCanPullUp = true;
    }


    /**
     * 由父控件决定是否分发事件，防止事件冲突
     *
     * @param ev MotionEvent
     * @return boolean
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mLastY = mDownY;
                mTimer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEvents == 0) {
                    if (mPullDownY > 0 || (((PullAble) mPullAbleView).canPullDown() && mCanPullDown && mState != LOADING)) {
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        mPullDownY = mPullDownY + (ev.getY() - mLastY) / mRadio;
                        if (mPullDownY < 0) {
                            mPullDownY = 0;
                            mCanPullDown = false;
                            mCanPullUp = true;
                        }

                        if (mPullDownY > getMeasuredHeight()) {
                            mPullDownY = getMeasuredHeight();
                        }

                        if (mState == REFRESHING) {
                            // 正在刷新的时候触摸移动
                            mIsTouch = true;
                        }

                    } else if (mPullUpY < 0 || (((PullAble) mPullAbleView).canPullUp() && mCanPullUp && mState != REFRESHING)) {
                        // 可以上拉，正在刷新时不能上拉
                        mPullUpY = mPullUpY + (ev.getY() - mLastY) / mRadio;
                        if (mPullUpY > 0) {
                            mPullUpY = 0;
                            mCanPullDown = true;
                            mCanPullUp = false;
                        }

                        if (mPullUpY < -getMeasuredHeight()) {
                            mPullUpY = -getMeasuredHeight();
                        }

                        if (mState == LOADING) {
                            // 正在加载的时候触摸移动
                            mIsTouch = true;
                        }

                    } else {
                        releasePull();
                    }
                } else {
                    mEvents = 0;
                }

                mLastY = ev.getY();
                // 根据下拉距离改变比例
                mRadio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (mPullDownY + Math.abs(mPullUpY))));
                if (mPullDownY > 0 || mPullUpY < 0) {
                    requestLayout();
                }

                if (mPullDownY > 0) {
                    if (mPullDownY <= mRefreshDist && (mState == RELEASE_TO_REFRESH || mState == DONE)) {
                        // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                        changeState(INIT);
                    }

                    if (mPullDownY >= mRefreshDist && mState == INIT) {
                        // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                        changeState(RELEASE_TO_REFRESH);
                    }

                } else if (mPullUpY < 0) {
                    // 下面是判断上拉加载的，同上，注意pullUpY是负值
                    if (-mPullUpY <= mLoadMoreDist && (mState == RELEASE_TO_LOAD || mState == DONE)) {
                        changeState(INIT);
                    }
                    // 上拉操作
                    if (-mPullUpY >= mLoadMoreDist && mState == INIT) {
                        changeState(RELEASE_TO_LOAD);
                    }
                }

                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用
                // (pullDownY + Math.abs(pullUpY))就可以不对当前状态作区分了
                if ((mPullDownY + Math.abs(mPullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mPullDownY > mRefreshDist || -mPullUpY > mLoadMoreDist) {
                    // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                    mIsTouch = false;
                }

                if (mState == RELEASE_TO_REFRESH) {
                    changeState(REFRESHING);
                    // 刷新操作
                    if (mListener != null) {
                        mListener.onRefresh(this);
                    }
                } else if (mState == RELEASE_TO_LOAD) {
                    changeState(LOADING);
                    // 加载操作
                    if (mListener != null) {
                        mListener.onLoadMore(this);
                    }
                }
                hide();
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    /**
     * 自动模拟手指滑动的task
     */
    private class AutoRefreshAndLoadTask extends AsyncTask<Integer, Float, String> {

        @Override
        protected String doInBackground(Integer... params) {
            while (mPullDownY < 4 / 3 * mRefreshDist) {
                mPullDownY += MOVE_SPEED;
                publishProgress(mPullDownY);
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            changeState(REFRESHING);
            // 刷新操作
            if (mListener != null) {
                mListener.onRefresh(RefreshLayout.this);
            }
            hide();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            if (mPullDownY > mRefreshDist) {
                changeState(RELEASE_TO_REFRESH);
            }
            requestLayout();
        }
    }


    /**
     * 自动刷新
     */
    public void autoRefresh() {
        AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
        task.execute(20);
    }

    /**
     * 自动加载
     */
    public void autoLoad() {
        mPullUpY = -mLoadMoreDist;
        requestLayout();
        changeState(LOADING);
        // 加载操作
        if (mListener != null) {
            mListener.onLoadMore(RefreshLayout.this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!mIsLayout) {
            // 这里是第一次进来的时候做一些初始化
            mRefreshView = getChildAt(0);
            mPullAbleView = getChildAt(1);
            mLoadMoreView = getChildAt(2);
            mIsLayout = true;
            initView();

            mRefreshDist = ((ViewGroup) mRefreshView).getChildAt(0).getMeasuredHeight();
            mLoadMoreDist = ((ViewGroup) mLoadMoreView).getChildAt(0).getMeasuredHeight();
        }
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        mRefreshView.layout(0, (int) (mPullDownY + mPullUpY) - mRefreshView.getMeasuredHeight(),
                mRefreshView.getMeasuredWidth(), (int) (mPullDownY + mPullUpY));
        mPullAbleView.layout(0, (int) (mPullDownY + mPullUpY),
                mPullAbleView.getMeasuredWidth(), (int) (mPullDownY + mPullUpY) + mPullAbleView.getMeasuredHeight());
        mLoadMoreView.layout(0, (int) (mPullDownY + mPullUpY) + mPullAbleView.getMeasuredHeight(),
                mLoadMoreView.getMeasuredWidth(),
                (int) (mPullDownY + mPullUpY) + mPullAbleView.getMeasuredHeight() + mLoadMoreView.getMeasuredHeight());
    }

    private void initView() {
        // 初始化下拉布局
        mPullDownView = mRefreshView.findViewById(R.id.pull_icon);
        mRefreshStateTextView = (TextView) mRefreshView.findViewById(R.id.state_tv);
        mRefreshingView = mRefreshView.findViewById(R.id.refreshing_icon);
        mRefreshStateImageView = mRefreshView.findViewById(R.id.state_iv);
        // 初始化上拉布局
        mPullUpView = mLoadMoreView.findViewById(R.id.pullup_icon);
        mLoadStateTextView = (TextView) mLoadMoreView.findViewById(R.id.loadstate_tv);
        mLoadingView = mLoadMoreView.findViewById(R.id.loading_icon);
        mLoadStateImageView = mLoadMoreView.findViewById(R.id.loadstate_iv);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }


    /**
     * 刷新加载回调接口
     *
     * @author chenjing
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh(RefreshLayout pullToRefreshLayout);

        /**
         * 加载操作
         */
        void onLoadMore(RefreshLayout pullToRefreshLayout);
    }

    /**
     * mLog("ACTION_UP");
     *
     * @param msg message
     */
    private void mLog(Object msg) {
        Log.i(TAG, "" + msg);
    }

}