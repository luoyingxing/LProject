package com.luo.project.move;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.luo.project.R;
import com.luo.project.move.entity.Way;
import com.luo.project.move.view.MonsterView;
import com.luo.project.move.view.TowerView;
import com.luo.project.move.view.WayView;

import java.util.ArrayList;
import java.util.List;


/**
 * <p/>
 * Created by luoyingxing on 2018/7/14.
 */
public class ViewMoveActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ViewMoveActivity";
    private FrameLayout rootLayout;
    private Button button;
    private ProgressBar progressBar;
    private WayView wayView;

    private List<Way> wayList;

    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_move_layout);


        findView();

        initParams();

        initWay();

        initTower();
    }

    private void findView() {
        rootLayout = findViewById(R.id.root_view);
        button = findViewById(R.id.btn_start);
        wayView = findViewById(R.id.view_way);
        progressBar = findViewById(R.id.progress);

        button.setOnClickListener(this);
    }

    private void initParams() {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        Log.d(TAG, "screenWidth:" + screenWidth + "  screenHeight:" + screenHeight);
    }

    private void initWay() {
        wayList = new ArrayList<>();
        wayList.add(new Way(0, 200));
        wayList.add(new Way(400, 200));
        wayList.add(new Way(400, 400));
        wayList.add(new Way(1000, 400));
        wayList.add(new Way(1000, screenHeight));

    }

    private int towerX = 500;
    private int towerY = 400;
    private int scope = 400;

    private int towerATK = 60;
    private int monsterHP = 2000;

    private View fireView;

    private void initTower() {
        TowerView tower = new TowerView(this);
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(80, 100);

        tower.setTranslationX(towerX);
        tower.setTranslationY(towerY);
        rootLayout.addView(tower, params);


        fireView = new View(this);
        ViewGroup.LayoutParams param = new FrameLayout.LayoutParams(10, 10);
        fireView.setBackgroundResource(R.mipmap.fire_one);
        fireView.setTranslationX(towerX);
        fireView.setTranslationY(towerY);
        rootLayout.addView(fireView, param);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                mLog("start");
                start();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        wayView.drawWay(wayList);
    }


    private void start() {
        monsterHP = 2000;
        progressBar.setMax(monsterHP);
        progressBar.setProgress(monsterHP);

        MonsterView wolf = new MonsterView(this);
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(80, 80);

        wolf.setTranslationX(0);
        wolf.setTranslationY(200);
        rootLayout.addView(wolf, params);

        propertyValuesHolder(wolf);
    }

    public void propertyValuesHolder(final View view) {
        this.view = view;
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", 0, 400 - 40, 400 - 40, 1000 - 40, 1000 - 40);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", 200 - 40, 200 - 40, 400 - 40, 400 - 40, screenHeight);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY);
        animator.setDuration(1000 * 20);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override

            public void onAnimationUpdate(ValueAnimator animation) {
                Float X = (Float) animation.getAnimatedValue("translationX");
                Float Y = (Float) animation.getAnimatedValue("translationY");


                double distance = Math.sqrt(Math.pow(Math.abs(X - towerX), 2) + Math.pow(Math.abs(Y - towerY), 2));


                if (distance <= scope) {
                    if (!alreadyFire) {
                        Log.w("onAnimationUpdate", distance + "----进入----攻击范围了");
                        startFire();
                    }
                } else {
                    if (alreadyFire) {
                        Log.d("onAnimationUpdate", distance + "----脱离----攻击范围了");
                        stopFire();
                    }
                }
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        animator.start();
    }

    private boolean alreadyFire;

    private void startFire() {
        alreadyFire = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alreadyFire && monsterHP > 0) {
                    try {
                        Thread.sleep(500);

                        handler.sendEmptyMessage(1);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private View view;

    private void stopFire() {
        alreadyFire = false;

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    fire();
                    break;
            }

            super.handleMessage(msg);
        }
    };

    private void fire() {
        if (monsterHP <= 0) {
            return;
        }

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", towerX, view.getX() + 20);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", towerY, view.getY() + 20);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fireView, pvhX, pvhY);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                fireView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fireView.setVisibility(View.INVISIBLE);
                fireView.setTranslationX(towerX);
                fireView.setTranslationY(towerY);

                monsterHP -= towerATK;
                progressBar.setProgress(monsterHP);


                if (monsterHP <= 0) {
                    rootLayout.removeView(view);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(130).start();
    }

    private void mLog(Object obj) {
        Log.i("ViewMoveActivity", "" + obj);
    }

}