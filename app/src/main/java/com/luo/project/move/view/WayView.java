package com.luo.project.move.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.luo.project.move.entity.Way;

import java.util.List;

/**
 * <p/>
 * Created by luoyingxing on 2018/7/14.
 */
public class WayView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private boolean mEnableDraw = false;

    private List<Way> wayList;


    public WayView(Context context) {
        super(context);
    }

    public WayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mEnableDraw = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mEnableDraw = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    public void drawWay(List<Way> list) {
        this.wayList = list;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (wayList.size() > 1) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(1);
            //连接的外边缘以圆弧的方式相交
            paint.setStrokeJoin(Paint.Join.ROUND);
            //线条结束处绘制一个半圆
//            paint.setStrokeCap(Paint.Cap.ROUND);

            //float startX, float startY, float stopX, float stopY,  Paint paint

            int x = wayList.get(0).getX();
            int y = wayList.get(0).getY();

            for (int i = 1; i < wayList.size(); i++) {
                Way way = wayList.get(i);

                canvas.drawLine(x, y, way.getX(), way.getY(), paint);

                x = way.getX();
                y = way.getY();
            }
        }


    }


    private void mLog(Object obj) {
        Log.w("WayView", "" + obj);
    }

}
