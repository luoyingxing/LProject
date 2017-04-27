package com.luo.project.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class PullAbleWebView extends WebView implements PullAble
{

	public PullAbleWebView(Context context)
	{
		super(context);
	}

	public PullAbleWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullAbleWebView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (getScrollY() >= getContentHeight() * getScale()
				- getMeasuredHeight())
			return true;
		else
			return false;
	}
}
