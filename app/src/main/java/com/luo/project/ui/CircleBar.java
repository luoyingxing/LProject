package com.luo.project.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CircleBar extends View {
    private static final String TAG = CircleBar.class.getName();
    private RectF mColorWheelRectangle = new RectF(); //圆圈的矩形范围
    private Paint mDefaultWheelPaint;  //绘制底部灰色圆圈的画笔
    private Paint mColorWheelPaint; //绘制蓝色扇形的画笔
    private Paint textPaint; //中间文字的画笔
    private float mColorWheelRadius;// 圆圈普通状态下的半径
    private float circleStrokeWidth; //圆圈的线条粗细

    private float pressExtraStrokeWidth; //按下状态下增加的圆圈线条增加的粗细

    private String mText;//中间文字内容
    private int mCount; //为了达到数字增加效果而添加的变量，他和mText其实代表一个意思
    private float mSweepAnglePer;  //为了达到蓝色扇形增加效果而添加的变量，他和mSweepAngle其实代表一个意思

    private float mSweepAngle; //扇形弧度
    private int mTextSize; //文字颜色
    private BarAnimation anim; //动画类

    public CircleBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        circleStrokeWidth = 10;
        pressExtraStrokeWidth = 2;
        mTextSize = 40;

        mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorWheelPaint.setColor(0xFF29a6f6);
        mColorWheelPaint.setStyle(Paint.Style.STROKE);
        mColorWheelPaint.setStrokeWidth(circleStrokeWidth);

        mDefaultWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultWheelPaint.setColor(0xFFeeefef);
        mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
        mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(0xFF333333);
        textPaint.setStyle(Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Align.LEFT);
        textPaint.setTextSize(mTextSize);

        mText = "0";
        mSweepAngle = 0;

        anim = new BarAnimation();
        anim.setDuration(2000);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mColorWheelRectangle, -90, 360, false, mDefaultWheelPaint);
        canvas.drawArc(mColorWheelRectangle, -90, mSweepAnglePer, false, mColorWheelPaint);
        Rect bounds = new Rect();
        String textstr = mCount + "";
        textPaint.getTextBounds(textstr, 0, textstr.length(), bounds);
        canvas.drawText(
                textstr + "",
                (mColorWheelRectangle.centerX())
                        - (textPaint.measureText(textstr) / 2),
                mColorWheelRectangle.centerY() + bounds.height() / 2,
                textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        mColorWheelRadius = min - circleStrokeWidth - pressExtraStrokeWidth;
        mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth, circleStrokeWidth + pressExtraStrokeWidth,
                mColorWheelRadius, mColorWheelRadius);
    }


    @Override
    public void setPressed(boolean pressed) {

        Log.i(TAG, "call setPressed ");
        if (pressed) {
            mColorWheelPaint.setColor(0xFF165da6);
            textPaint.setColor(0xFF070707);
            mColorWheelPaint.setStrokeWidth(circleStrokeWidth + pressExtraStrokeWidth);
            mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth + pressExtraStrokeWidth);
            textPaint.setTextSize(mTextSize - pressExtraStrokeWidth);
        } else {
            mColorWheelPaint.setColor(0xFF29a6f6);
            textPaint.setColor(0xFF333333);
            mColorWheelPaint.setStrokeWidth(circleStrokeWidth);
            mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);
            textPaint.setTextSize(mTextSize);
        }
        super.setPressed(pressed);
        this.invalidate();
    }

    public void startCustomAnimation() {
        this.startAnimation(anim);
    }

    public void setText(String text) {
        mText = text;
        this.startAnimation(anim);
    }

    public void setSweepAngle(float sweepAngle) {
        mSweepAngle = sweepAngle;

    }


    public class BarAnimation extends Animation {
        /**
         * Initializes expand collapse animation, has two types, collapse (1) and expand (0).
         *
         * @param view The view to animate
         * @param type The type of animation: 0 will expand from gone and 0 size to visible and layout size defined in xml.
         *             1 will collapse view and set to gone
         */
        public BarAnimation() {
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                mSweepAnglePer = interpolatedTime * mSweepAngle;
                mCount = (int) (interpolatedTime * Float.parseFloat(mText));
            } else {
                mSweepAnglePer = mSweepAngle;
                mCount = Integer.parseInt(mText);
            }
            postInvalidate();
        }
    }
}