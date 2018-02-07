package com.raleys.libandroid;

import android.content.Context;
import android.widget.ScrollView;

public class SmartScrollView extends ScrollView
{
	private int _currentYOffset;
	
	public SmartScrollView(Context context)
	{
		super(context);
		_currentYOffset = 0;
	}
	
	@Override
	protected void onScrollChanged(int left, int top, int oldLeft, int oldTop)
	{
		super.onScrollChanged(left, top, oldLeft, oldTop);
		_currentYOffset = top;
	}
	

	public int getYOffset() { return _currentYOffset; }
}
