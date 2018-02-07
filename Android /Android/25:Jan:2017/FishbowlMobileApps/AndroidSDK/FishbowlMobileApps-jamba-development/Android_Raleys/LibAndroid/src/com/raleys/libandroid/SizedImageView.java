package com.raleys.libandroid;


import android.content.Context;
import android.widget.ImageView;

public class SizedImageView extends ImageView
{
	private int _width;
	private int _height;
	
	public SizedImageView(Context context, int width, int height)
	{
		super(context);
		_width = width;
		_height = height;
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        this.setMeasuredDimension(_width, _height);
    }
}
