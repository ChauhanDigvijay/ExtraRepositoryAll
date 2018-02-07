package com.raleys.app.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.SmartTextView;


public class AccountDropdownAdapter extends BaseAdapter
{
	private RaleysApplication _app;
	private Context           _context;
	private int               _width;
	private int               _height;
    private LayoutInflater    _inflater;
    private Typeface          _font;
    private Bitmap            _background;
    private int               _textColor;
    public String[]           _dropdownList;

    
	public AccountDropdownAdapter(Context context, String[] dropdownList, int width, int height, Bitmap background, int textColor)
    {
		_app  = (RaleysApplication)context.getApplicationContext();
        _context = context;
    	_width = width;
    	_height = height;
    	_dropdownList = dropdownList;
    	_inflater = LayoutInflater.from(context);
    	_font = _app.getNormalFont();
    	_background = background;
    	_textColor = textColor;
    }

    @Override
    public int getCount() { return _dropdownList.length; }

    @Override
    public Object getItem(int position) { return _dropdownList[position]; }

    @Override
    public long getItemId(int id) { return id; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
    	final String listItemText = _dropdownList[position];    	
    	DropdownCell cell;
    	RelativeLayout.LayoutParams layoutParams;
		
		try
		{
			if(convertView == null)
			{
				convertView = _inflater.inflate(R.layout.convert_view_layout, null);

				cell = new DropdownCell();
				cell._layout = (RelativeLayout) convertView.findViewById(R.id.holder_layout);
				layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.height = _height;
				layoutParams.width = _width;
				cell._layout.setLayoutParams(layoutParams);
				
				cell._background = new SizedImageView(_context, _width, _height);
				cell._background.setImageBitmap(_background);
				layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = 0;
				layoutParams.width = _width;
				layoutParams.topMargin = 0;
				layoutParams.height = _height;
				cell._layout.addView(cell._background, layoutParams);

				int textViewHeight = (int)(_height * .40);
				cell._textView = new SmartTextView(_context, (int)(_width * .8), textViewHeight, 1, Color.TRANSPARENT, _textColor, _font, Align.LEFT);
				layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = (int)(_width * .05);
				layoutParams.width = (int)(_width * .9);
				layoutParams.topMargin = ((_height - textViewHeight) / 2) + (int)(_height * .02); // slight offset for background shadow
				layoutParams.height = textViewHeight;
		    	cell._layout.addView(cell._textView, layoutParams);
				
				convertView.setTag(cell);
			}
			else
			{
				cell = (DropdownCell) convertView.getTag();
			}

			cell._textView.writeText(listItemText);
        }
		catch(Exception ex) { ex.printStackTrace(); }
		
		return convertView;
    }
    
    
    static class DropdownCell
	{
		RelativeLayout _layout;
		SizedImageView _background;
		SmartTextView  _textView;
	}
}
