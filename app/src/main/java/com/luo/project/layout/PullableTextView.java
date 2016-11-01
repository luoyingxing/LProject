package com.luo.project.layout;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class PullAbleTextView extends TextView implements PullAble
{

	public PullAbleTextView(Context context)
	{
		super(context);
	}

	public PullAbleTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullAbleTextView(Context context, AttributeSet attrs, int defStyle)
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
