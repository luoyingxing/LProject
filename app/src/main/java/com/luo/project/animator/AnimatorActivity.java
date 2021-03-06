package com.luo.project.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.luo.project.R;

public class AnimatorActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageView imageViewOne;
    private Animation animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);


        imageView = (ImageView) findViewById(R.id.image_view);
        imageViewOne = (ImageView) findViewById(R.id.img_one);

        animator = AnimationUtils.loadAnimation(this, R.anim.anim_loading_rotating);
        LinearInterpolator lin = new LinearInterpolator();
        animator.setInterpolator(lin);
//        imageView.startAnimation(animator);


        imageViewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                rotateAnimRun(v);
                propertyValuesHolder(v);
            }
        });

//        verticalRun(imageView);

        animationSet(imageViewOne);
    }

    /**
     * 透明度：alpha
     * 旋转度数: rotation、rotationX、rotationY
     * 平移：translationX、translationY
     * 缩放：scaleX、scaleY
     *
     * @param view view
     */
    public void rotateAnimRun(final View view) {
        ObjectAnimator anim = ObjectAnimator
                .ofFloat(view, "rotationX", 1.0F, 0.0F)
                .setDuration(2000);
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                view.setAlpha(cVal);
                view.setScaleX(cVal);
                view.setScaleY(cVal);
            }
        });
    }

    public void propertyValuesHolder(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f, 0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0, 1f, 0);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0, 1f, 0);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(2000).start();
    }

    public void verticalRun(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 600);
        animator.setTarget(view);
        animator.setDuration(5000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationY((Float) animation.getAnimatedValue());
                view.setTranslationX((Float) animation.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) {
                    parent.removeView(view);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * after(Animator anim) 将传入的动画在原有动画执行之前执行：
     */
    private void animationSet(View v) {
        // 将after中传入的缩放动画在原有动画之前执行
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(v, "rotation", 0.0f, 360f);
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 0.0f, 2.0f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotateAnim).with(scaleAnim);
        animSet.setDuration(2000);
        animSet.start();
    }

}