package com.raleys.app.android;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.raleys.libandroid.SmartTextView;

public class LogListAdapter  extends BaseAdapter
{
	private RaleysApplication _app;
	private Context _context;
	private int _width;
	private int _height;
    private ArrayList<String> _logList;
    private LayoutInflater _inflater;
    private Typeface _normalFont;

    
    public LogListAdapter(Context context, ArrayList<String> logList, int width, int height)
    {
		_app  = (RaleysApplication)context.getApplicationContext();
        _context = context;
    	_width = width;
    	_height = height;
    	_logList = logList;
    	_inflater = LayoutInflater.from(context);
    	_normalFont = _app.getNormalFont();
    }

    @Override
    public int getCount() { return _logList.size(); }

    @Override
    public Object getItem(int position) { return _logList.get(position); }

    @Override
    public long getItemId(int id) { return id; }


	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
    	final String logString = _logList.get(position);    	
    	LogCell cell;
    	RelativeLayout.LayoutParams layoutParams;
		
		try
		{
			if(convertView == null)
			{
				convertView = _inflater.inflate(R.layout.convert_view_layout, null);
				convertView.setBackgroundColor(Color.WHITE);			

				cell = new LogCell();
				cell._layout = (RelativeLayout) convertView.findViewById(R.id.holder_layout);
				layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.height = _height;
				layoutParams.width = _width;
				cell._layout.setLayoutParams(layoutParams);
		        
				cell._logTextView = new SmartTextView(_context, _width, _height, 3, Color.TRANSPARENT, Color.BLACK, _normalFont, Align.LEFT);
		    	layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    	layoutParams.leftMargin = 0;
		    	layoutParams.topMargin = 0;
		    	cell._layout.addView(cell._logTextView, layoutParams);

				
				convertView.setTag(cell);
			}
			else
			{
				cell = (LogCell) convertView.getTag();
			}

			cell._logTextView.writeText(logString);
        }
		catch(Exception ex) { ex.printStackTrace(); }
		
		return convertView;
	}
    
    
    static class LogCell
	{
		RelativeLayout _layout;
		SmartTextView _logTextView;
	}
}
