package com.raleys.libandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.view.MotionEvent;
import android.widget.Button;

/**
 *  This class implements a custom button with a bitmapped background and text on top of the bitmap.  The button is sized based on the
 *  size of the supplied parameters for width and height.  The text is also configurable, it's height can be specified as a percentage
 *  of the button height, and the text font and color are also explicitly defined.  In addition, highlighting is provided by default
 *  when the button is pressed.  These defaults can be overriden for custom behavior. 
 */
@SuppressLint("ViewConstructor")
public class SizedImageTextButton extends Button
{
	private int _width;
	private int _height;
	private float _textHeight;
	private int _textColor;
	private Typeface _typeface;
	private int _typefaceSize;
	private String _text;
	private Bitmap _viewBitmap = null;
	private Bitmap _background;
	private Bitmap _selectedBackground;
	private Bitmap _currentBackground;
	private Paint _paint;
	private Rect _textBounds;
	private Rect _backgroundRect;

    public int Width() { return _width; }
	public int Height() { return _height; }
	
	
	/**
	 *  Class constructor for instances where the typeface size is calculated and alpha dimming is used.
	 *  @param Context - the context of the calling activity.
	 *  @param background - the bitmap to be used for the background.
	 *  @param textHeight - the height of the text expressed as a fraction of the maximum text size that can be displayed
	 *  @param textColor - the text foreground color
	 *  @param typeface - the text font
	 *  @param text - the text to be displayed
	 *  @param animationDuration - the button highlight period in milliseconds(default is 100)
	 *  @param buttonTouchAlpha - the alpha value of the button when highlighted(default is .5f)
	 */
	public SizedImageTextButton(Context context, Bitmap background, float textHeight, int textColor, Typeface typeface, String text)
	{
		super(context);
		
		try
		{
			_paint = new Paint();
			_width = background.getWidth();
			_height = background.getHeight();
			_textHeight = textHeight;
			_textColor = textColor;
			_typeface = typeface;
			_typefaceSize = 0;
			_text = text;
			_currentBackground = _background = background;
			_selectedBackground = null;
	        _backgroundRect = new Rect(0, 0, _width, _height);
	    	setBackgroundColor(Color.TRANSPARENT);
			_textBounds = new Rect();
		}
		catch(Exception ex) { ex.printStackTrace(); }
	}


	/**
	 *  Class constructor for instances where the typeface size is known and alpha dimming is used.
	 *  @param Context - the context of the calling activity.
	 *  @param background - the bitmap to be used for the background.
	 *  @param textColor - the text foreground color
	 *  @param typeface - the text font
	 *  @param typefaceSize - the height of the text expressed as a fraction of the maximum text size that can be displayed
	 *  @param text - the text to be displayed
	 *  @param animationDuration - the button highlight period in milliseconds(default is 100)
	 *  @param buttonTouchAlpha - the alpha value of the button when highlighted(default is .5f)
	 */
	public SizedImageTextButton(Context context, Bitmap background, int textColor, Typeface typeface, int typefaceSize, String text)
	{
		super(context);
		
		try
		{
			_paint = new Paint();
			_width = background.getWidth();
			_height = background.getHeight();
			_textHeight = 0;
			_textColor = textColor;
			_typeface = typeface;
			_typefaceSize = typefaceSize;
			_text = text;
			_currentBackground = _background = background;
			_selectedBackground = null;
	        _backgroundRect = new Rect(0, 0, _width, _height);
	    	setBackgroundColor(Color.TRANSPARENT);
			_textBounds = new Rect();
		}
		catch(Exception ex) { ex.printStackTrace(); }
	}


	/**
	 *  Class constructor for instances where the typeface size is calculated and a selected background image is used.
	 *  @param Context - the context of the calling activity.
	 *  @param background - the bitmap to be used for the background.
	 *  @param selectedBackground - the bitmap to be used for the background when the button is pressed.
	 *  @param textHeight - the height of the text expressed as a fraction of the maximum text size that can be displayed
	 *  @param textColor - the text foreground color
	 *  @param typeface - the text font
	 *  @param text - the text to be displayed
	 */
	public SizedImageTextButton(Context context, Bitmap background, Bitmap selectedBackground, float textHeight, int textColor, Typeface typeface, String text)
	{
		super(context);
		
		try
		{
			_paint = new Paint();
			_width = background.getWidth();
			_height = background.getHeight();
			_textHeight = textHeight;
			_textColor = textColor;
			_typeface = typeface;
			_typefaceSize = 0;
			_text = text;
			_currentBackground = _background = background;
			_selectedBackground = selectedBackground;
	        _backgroundRect = new Rect(0, 0, _width, _height);
	    	setBackgroundColor(Color.TRANSPARENT);
			_textBounds = new Rect();
		}
		catch(Exception ex) { ex.printStackTrace(); }
	}


	/**
	 *  Class constructor for instances where the typeface size is known and a selected background image is used.
	 *  @param Context - the context of the calling activity.
	 *  @param background - the bitmap to be used for the background.
	 *  @param selectedBackground - the bitmap to be used for the background when the button is pressed.
	 *  @param textColor - the text foreground color
	 *  @param typeface - the text font
	 *  @param typefaceSize - the height of the text expressed as a fraction of the maximum text size that can be displayed
	 *  @param text - the text to be displayed
	 */
	public SizedImageTextButton(Context context, Bitmap background, Bitmap selectedBackground, int textColor, Typeface typeface, int typefaceSize, String text)
	{
		super(context);
		
		try
		{
			_paint = new Paint();
			_width = background.getWidth();
			_height = background.getHeight();
			_textHeight = 0;
			_textColor = textColor;
			_typeface = typeface;
			_typefaceSize = typefaceSize;
			_text = text;
			_currentBackground = _background = background;
			_selectedBackground = selectedBackground;
	        _backgroundRect = new Rect(0, 0, _width, _height);
	    	setBackgroundColor(Color.TRANSPARENT);
			_textBounds = new Rect();
		}
		catch(Exception ex) { ex.printStackTrace(); }
	}


	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        this.setMeasuredDimension(_width, _height);
    }
    

    @Override
    protected void onDraw(Canvas canvas)
    {
    	super.onDraw(canvas);   	

		try
		{
	        // draw background
			canvas.drawBitmap(_currentBackground, null, _backgroundRect, _paint);
		
	    	if(_viewBitmap == null)
	    	{
	    		// draw background
	    		_paint.setStyle(Paint.Style.FILL);
	    		_paint.setColor(Color.TRANSPARENT);
	    		canvas.drawPaint(_paint);
	    	
	    		// draw text
	    		_paint.setColor(_textColor);
	    		_paint.setAntiAlias(true); // ???
				_paint.setTypeface(_typeface);
				_paint.setTextAlign(Align.CENTER);
				
				if(_typefaceSize > 0)
					_paint.setTextSize(_typefaceSize);
				else
					_paint.setTextSize((int)(Utils.getFontSize(_width, _height, _typeface, _text) * _textHeight));
				
				_paint.getTextBounds(_text, 0, _text.length(), _textBounds);
				Paint.FontMetrics fontMetrics = _paint.getFontMetrics();
				float fontMetricsHeight = ((fontMetrics.top * -1) + fontMetrics.bottom);
				float unusedVerticalSpace = _height - fontMetricsHeight;
				float yOrigin = _height - (_height + fontMetrics.top) + (unusedVerticalSpace / 2);
				canvas.drawText(_text, ((_width) / 2), yOrigin, _paint);
	    	}
	    	else
	    	{
	    		canvas.drawBitmap(_viewBitmap, 0, 0, _paint);
	    	}
		}
		catch(Exception ex) { ex.printStackTrace(); }
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	super.onTouchEvent(event);
    	int action = event.getAction();
    	
    	if(action == MotionEvent.ACTION_DOWN)
    	{
    		if(_selectedBackground != null)
    		{
    			_currentBackground = _selectedBackground;
    			invalidate();
    		}
    		else
    		{
    			setAlpha(.8f);
    		}
    	}
    	else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)
    	{
    		if(_selectedBackground != null)
    		{
    			_currentBackground = _background;
    			invalidate();
    		}
    		else
    		{
    			setAlpha(1.0f);
    		}
    	}
    	
    	return true;
    }
    
    
    public void setTextColor(int textColor)
    {
    	_textColor = textColor;
    	this.invalidate();
    }
    
    
    public void changeText(String text)
    {
    	_text = text;
    	this.invalidate();
    }
}
