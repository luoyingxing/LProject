package com.luo.project.vector;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageView.getDrawable();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (animatedVectorDrawable.isRunning()) {
                animatedVectorDrawable.stop();
            } else {
                animatedVectorDrawable.start();
            }
        }
    }
}