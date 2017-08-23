package com.luo.project.vector;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.luo.project.R;

/**
 * VectorActivity
 * <p>
 * Created by luoyingxing on 2017/8/18.
 */

public class VectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector);

        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        final ImageView oneIV = (ImageView) findViewById(R.id.image_one);
        ImageView twoIV = (ImageView) findViewById(R.id.image_two);

        final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageView.getDrawable();
        final AnimatedVectorDrawable twoIVDrawable = (AnimatedVectorDrawable) twoIV.getDrawable();

        oneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = oneIV.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    animatedVectorDrawable.start();
                }
            }
        });

        twoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    twoIVDrawable.start();
                }
            }
        });
    }
}