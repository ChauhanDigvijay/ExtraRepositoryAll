package com.raleys.libandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 *  This class implements a custom TextView that is sized based on the provided width and height.  This class will use the maximum
 *  font size that can be rendered in the view.  The text itself is also configurable, the text foreground/backgrounds colors, and
 *  font are also explicitly defined.
 */
@SuppressLint("ViewConstructor")
public class SizedTextView extends TextView
{
	private int         _width;
	private int         _height;
	private int         _backgroundColor;
	private int         _textColor;
	private int         _typefaceSizeOriginal;
	private int         _typefaceSizeTemporary = 0;
	private String      _text;
	private Typeface    _typeface;
	private Rect        _textBounds;
	private Paint       _paint;
	private Paint.Align _align;
	private Bitmap      _viewBitmap = null;
	
	public int Width() { return _width; }
	public int Height() { return _height; }
	

	/**
	 *  Class constructor.
	 *  @param Context - the context of the calling activity.
	 *  @param width - the specific pixel width of the button
	 *  @param height - the specific pixel height of the button
	 *  @param backgroundColor - the text background color
	 *  @param textColor - the text foreground color
	 *  @param typeface - the text font
	 *  @param text - the text to be displayed
	 */
	public SizedTextView(Context context, int width, int height, int backgroundColor, int textColor, Typeface typeface, String text)
	{
		this(context, width, height, backgroundColor, textColor, typeface, 0, text, Align.CENTER);
	}


	/**
	 *  Class constructor.
	 *  @param Context - the context of the calling activity.
	 *  @param width - the specific pixel width of the button
	 *  @param height - the specific pixel height of the button
	 *  @param backgroundColor - the text background color
	 *  @param textColor - the text foreground color
	 *  @param typeface - the text font
	 *  @param text - the text to be displayed
	 *  @param align - the text alignment
	 */
	public SizedTextView(Context context, int width, int height, int backgroundColor, int textColor, Typeface typeface, String text, Paint.Align align)
	{
		this(context, width, height, backgroundColor, textColor, typeface, 0, text, align);
	}


	/**
	 *  Class constructor.
	 *  @param Context - the context of the calling activity.
	 *  @param width - the specific pixel width of the button
	 *  @param height - the specific pixel height of the button
	 *  @param backgroundColor - the text background color
	 *  @param textColor - the text foreground color
	 *  @param typeface - the text font
	 *  @param typefaceSize - the typeface size
	 *  @param text - the text to be displayed
	 */
	public SizedTextView(Context context, int width, int height, int backgroundColor, int textColor, Typeface typeface, int typefaceSize, String text)
	{
		this(context, width, height, backgroundColor, textColor, typeface, typefaceSize, text, Align.CENTER);
	}


	/**
	 *  Class constructor.
	 *  @param Context - the context of the calling activity.
	 *  @param width - the specific pixel width of the button
	 *  @param height - the specific pixel height of the button
	 *  @param backgroundColor - the text background color
	 *  @param textColor - the text foreground color
	 *  @param typeface - the text font
	 *  @param typefaceSize - the typeface size
	 *  @param text - the text to be displayed
	 *  @param align - the text alignment
	 */
	public SizedTextView(Context context, int width, int height, int backgroundColor, int textColor, Typeface typeface, int typefaceSize, String text, Paint.Align align)
	{
		super(context);
		_paint = new Paint();
    	_textBounds = new Rect();
		_width = width;
		_height = height;
		_backgroundColor = backgroundColor;
		_textColor = textColor;
		_typeface = typeface;
		_typefaceSizeOriginal = typefaceSize;
		_text = text != null ? text : "";
		_align = align;

		if(_typefaceSizeOriginal == 0)
		{
			_typefaceSizeOriginal = Utils.getFontSize(_width, _height, _typeface, _text);
		}
		else // check to see if the new text fits, if not downsize it
		{
	    	int size = Utils.getFontSize(_width, _height, _typeface, _text);

	    	if(size < _typefaceSizeOriginal)
				_typefaceSizeTemporary = size;
		}
	}


	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        this.setMeasuredDimension(_width, _height);
    }

	
    public void changeText(String text)
    {
		_typefaceSizeTemporary = 0;
		
    	// first check to see if new text fits, if not downsize it
    	int size = Utils.getFontSize(_width, _height, _typeface, text);

    	if(size < _typefaceSizeOriginal)
			_typefaceSizeTemporary = size;

		_text = text;		
    	invalidate();
    }
    
    
    @Override
    protected void onDraw(Canvas canvas)
    {
    	super.onDraw(canvas);

    	if(_viewBitmap == null)
    	{
    		int currentTypefaceSize = 0;

    		// draw background
    		_paint.setStyle(Paint.Style.FILL);
    		_paint.setColor(_backgroundColor);
    		canvas.drawPaint(_paint);
    	
    		// draw text
    		_paint.setColor(_textColor);
	    	_paint.setAntiAlias(true); // smooths out edges
			_paint.setTypeface(_typeface);
		
			if(_typefaceSizeTemporary > 0) // this is set if the text doesn't fit into the original size
				currentTypefaceSize = _typefaceSizeTemporary;
			else
				currentTypefaceSize = _typefaceSizeOriginal;
			
			_paint.setTextAlign(_align);
			_paint.setTextSize(currentTypefaceSize);
	    	_paint.getTextBounds(_text, 0, _text.length(), _textBounds);
			Paint.FontMetrics fontMetrics = _paint.getFontMetrics();
			float fontMetricsHeight = ((fontMetrics.top * -1) + fontMetrics.bottom);
			float unusedVerticalSpace = _height - fontMetricsHeight;
			float yOrigin = _height - (_height + fontMetrics.top) + (unusedVerticalSpace / 2);
			
			_text = Utils.autoTruncate(_paint, _width, _text);
			
			if(_align == Align.CENTER)
				canvas.drawText(_text, ((_width) / 2), yOrigin, _paint);
			else if(_align == Align.LEFT)
				canvas.drawText(_text, 0, yOrigin, _paint);
			else if(_align == Align.RIGHT)
				canvas.drawText(_text, _width, yOrigin, _paint);
    	}
    	else
    	{
    		canvas.drawBitmap(_viewBitmap, 0, 0, _paint);
    	}
    }
}
