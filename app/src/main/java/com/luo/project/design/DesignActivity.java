package com.luo.project.design;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.luo.project.R;

public class DesignActivity extends AppCompatActivity {
    private AppBarLayout appBarLayout;
    private Toolbar mToolbar;
    private TextView titleTV;
    private TextView contentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);

        appBarLayout = (AppBarLayout) findViewById(R.id.base_top_al);
        titleTV = (TextView) findViewById(R.id.toolbar_title);
        contentTV = (TextView) findViewById(R.id.content);

        mToolbar = (Toolbar) findViewById(R.id.base_tool_bar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("DesignActivity");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.i("DesignActivity", "verticalOffset = " + verticalOffset);
                Log.i("DesignActivity", "appBarLayout.getTotalScrollRange() = " + appBarLayout.getTotalScrollRange());

                int offset = Math.abs(verticalOffset);
                int total = appBarLayout.getTotalScrollRange();
                int alphaIn = offset;
                int alphaOut = (200 - offset) < 0 ? 0 : 200 - offset;
                int maskColorIn = Color.argb(alphaIn, Color.red(0), Color.green(0), Color.blue(0));
                int maskColorInDouble = Color.argb(alphaIn * 2, Color.red(142), Color.green(200), Color.blue(141));
                int maskColorOut = Color.argb(alphaOut * 2, Color.red(162), Color.green(11), Color.blue(74));
                if (offset <= total / 2) {
//                    tl_expand.setVisibility(View.VISIBLE);
//                    tl_collapse.setVisibility(View.GONE);
//                    titleTV.setTextColor(maskColorOut);
                } else {
//                    tl_expand.setVisibility(View.GONE);
//                    tl_collapse.setVisibility(View.VISIBLE);
//                    titleTV.setTextColor(maskColorOut);
                }
//                mToolbar.setBackgroundColor(maskColorIn);

                Log.i("DesignActivity", "offset/total = " + (float) offset / (float) total);

                int color = Color.argb(100, Color.red(offset), Color.green(offset/2), Color.blue(offset*2));
                contentTV.setBackgroundColor(color);
                titleTV.setAlpha((float) offset / (float) total);
            }
        });
    }
}