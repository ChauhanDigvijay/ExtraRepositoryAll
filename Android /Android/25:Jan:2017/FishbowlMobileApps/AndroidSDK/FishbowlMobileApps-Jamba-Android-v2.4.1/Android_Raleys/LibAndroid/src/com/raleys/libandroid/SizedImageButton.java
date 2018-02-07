package com.raleys.libandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.ImageButton;

@SuppressLint("ViewConstructor")
public class SizedImageButton extends ImageButton
{
	protected int _width = 0;
	protected int _height = 0;
	private Bitmap _background;
	private Bitmap _selectedBackground;


    public SizedImageButton(Context context, Bitmap background)
	{
		this(context, background, null);
	}
    
    
	public SizedImageButton(Context context, Bitmap background, Bitmap selectedBackground)
	{
		super(context);

		try
		{
			_width = background.getWidth();
			_height = background.getHeight();
			_background = background;
			_selectedBackground = selectedBackground;
	    	setImageBitmap(background);
	    	setBackgroundColor(Color.TRANSPARENT);
		}
		catch(Exception ex) { ex.printStackTrace(); }
	}

	
	public void changeBitmap(Bitmap bitmap)
	{
    	setImageBitmap(bitmap);
	}
	
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        this.setMeasuredDimension(_width, _height);
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	super.onTouchEvent(event);
    	int action = event.getAction();
    	
    	if(action == MotionEvent.ACTION_DOWN)
    	{
    		if(_selectedBackground != null)
    			setImageBitmap(_selectedBackground);
    		else
    			setAlpha(.8f);
    	}
    	else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)
    	{
    		if(_selectedBackground != null)
    			setImageBitmap(_background);
    		else
    			setAlpha(1.0f);
    	}
    	
    	return true;
    }
}
