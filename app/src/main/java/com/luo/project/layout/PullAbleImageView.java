package com.luo.project.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class PullAbleImageView extends ImageView implements PullAble
{

	public PullAbleImageView(Context context)
	{
		super(context);
	}

	public PullAbleImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullAbleImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		return true;
	}

	@Override
	public boolean canPullUp()
	{
		return true;
	}

}
