package com.raleys.libandroid;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.widget.TextView;


@SuppressLint({ "ViewConstructor", "DrawAllocation" })
public class CenteredTextView extends TextView
{
	private int _width;
	private int _height;
	private int _lines;
	private int _backgroundColor;
	private int _textColor;
	private Typeface _font;
	private int _fontSize;
	private String _text;
	private Paint _paint;
	private Paint.Align _align;
	
	public int Width() { return _width; }
	public int Height() { return _height; }


	/**
	 *  Class constructor.
	 *  @param Context - the context of the calling activity.
	 *  @param width - the specific pixel width of the button
	 *  @param height - the specific pixel height of the button
	 *  @param lines - the number of lines to render in the view
	 *  @param backgroundColor - the text background color
	 *  @param textColor - the text foreground color
	 *  @param typeface - the text font
	 *  @param text - the text to be displayed
	 */
	public CenteredTextView(Context context, int width, int height, int lines, int backgroundColor, int textColor, Typeface font, String text)
	{
		this(context, width, height, lines, backgroundColor, textColor, font, text, Align.CENTER);
	}
	
	
	/**
	 *  Class constructor.
	 *  @param Context - the context of the calling activity.
	 *  @param width - the specific pixel width of the button
	 *  @param height - the specific pixel height of the button
	 *  @param lines - the number of lines to render in the view
	 *  @param backgroundColor - the text background color
	 *  @param textColor - the text foreground color
	 *  @param typeface - the text font
	 *  @param text - the text to be displayed
	 *  @param align - the text justification, Align.CENTER is the default
	 */
	public CenteredTextView(Context context, int width, int height, int lines, int backgroundColor, int textColor, Typeface font, String text, Paint.Align align)
	{
		super(context);
		_width = width;
		_height = height;
		_lines = lines;
		_backgroundColor = backgroundColor;
		_textColor = textColor;
		_font = font;
		_text = text;
		_align = align;
		_paint = new Paint();
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

		int lineHeight = _height / _lines;
		_fontSize = Utils.getFontSizeByHeight(lineHeight, _font);
		// draw background
		_paint.setStyle(Paint.Style.FILL);
		_paint.setColor(_backgroundColor);
		canvas.drawPaint(_paint);
	
		// draw text
		_paint.setColor(_textColor);
    	_paint.setAntiAlias(true); // smooths out edges
		_paint.setTypeface(_font);
		_paint.setTextSize(_fontSize);
		_paint.setTextAlign(_align);
		ArrayList<String> textArray = Utils.wrapText(_text, (int)(_width * .96), _paint); // allow a small pad on each side
		int arrayCount = textArray.size();
		int lastLineIndex = _lines - 1;
		
		if(arrayCount > _lines)
		{
			String lastLine = textArray.get(_lines - 1);
			String adjustedLastLine = lastLine.substring(0, lastLine.length() - 1) + "..."; // truncate the last allowable line

			for(int i = lastLineIndex; i < arrayCount; i++) // remove the last allowable line and beyond
				textArray.remove(lastLineIndex);
			
			textArray.add(adjustedLastLine); // put the adjusted lastLine back				
		}

		int actualLines = textArray.size() < _lines  ? textArray.size() : _lines;
		int yPadding = actualLines < _lines ? ((_lines - actualLines) * lineHeight) / 2 : 0;
		String text = null;
		int yOrigin = 0;
		
		for(int i = 0; i < actualLines; i++)
		{
			Rect lineRect = new Rect(0, (lineHeight * i) + yPadding, _width, (lineHeight * i) + lineHeight);
			text = textArray.get(i);
			yOrigin = Utils.getTextTopAlignedYOrigin(_paint, lineRect);
			
			if(_align == Align.CENTER)
				canvas.drawText(text, ((_width) / 2), yOrigin, _paint);
			else if(_align == Align.LEFT)
				canvas.drawText(text, 0, yOrigin, _paint);
			else if(_align == Align.RIGHT)
				canvas.drawText(text, _width, yOrigin, _paint);
		}
    }
}
