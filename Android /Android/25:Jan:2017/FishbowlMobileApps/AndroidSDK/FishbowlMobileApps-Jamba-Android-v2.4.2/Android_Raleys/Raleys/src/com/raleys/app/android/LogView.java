package com.raleys.app.android;


import java.util.ArrayList;
import java.util.Arrays;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.raleys.libandroid.SizedImageButton;
import com.raleys.libandroid.Utils;

public class LogView extends Dialog implements OnTouchListener
{
	private RaleysApplication _app;
	private int _screenWidth;
	private int _screenHeight;
	private int _dialogTop;
	private int _dialogLeft;
	private int _dialogWidth;
	private int _dialogHeight;
	private RelativeLayout _mainLayout;
	private ListView _listView;
	
	
	@SuppressWarnings("deprecation")
	public LogView(Context context, float width, float height)
	{
		super(context, R.style.ModalDialog);

		try
		{
			_app  = (RaleysApplication)context.getApplicationContext();
			setCancelable(false); // disables the back button while dialog is active
			setCanceledOnTouchOutside(false);
	    	DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics(); // metrics calculated for portrait mode
	    	Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    	
	    	if(display.getHeight() > display.getWidth()) // portrait mode
	    	{
	    		_screenWidth = displayMetrics.widthPixels;
	    		_screenHeight = displayMetrics.heightPixels;;
	    	}
	    	else // landscape mode
	    	{
	    		_screenWidth = displayMetrics.heightPixels;
	    		_screenHeight = displayMetrics.widthPixels;
	    	}
	    	
	        _dialogWidth = (int)(_screenWidth * width);
	        _dialogHeight = (int)(_screenHeight * height);
	        _dialogLeft = (_screenWidth - _dialogWidth) / 2;
	        _dialogTop = (_screenHeight - _dialogHeight) / 2;
	        
	    	_mainLayout = new RelativeLayout(context);	        
	    	_mainLayout.setOnTouchListener(this);
	    	_mainLayout.setBackgroundColor(Color.argb(127, 0, 0, 0));
	    	
	        int cancelButtonSize = (int)(_dialogHeight * .05);

	        // dialog layout
	        RelativeLayout dialogLayout = new RelativeLayout(context);	        
	        dialogLayout.setBackgroundColor(Color.WHITE);
	        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        layoutParams.topMargin = _dialogTop;
	        layoutParams.leftMargin = _dialogLeft;
	        layoutParams.height = _dialogHeight;
	        layoutParams.width = _dialogWidth;
	        _mainLayout.addView(dialogLayout, layoutParams);
	        
			String log = Utils.readLog();
			String[] lines = log.split(System.getProperty("line.separator"));

	        // list
	        _listView = new ListView(context);
	        _listView.setBackgroundColor(Color.TRANSPARENT);
	        //_listView.setDividerHeight(0);
	        ArrayList<String> logList = new ArrayList<String>(Arrays.asList(lines));	        
	        LogListAdapter adapter = new LogListAdapter(context, logList, _dialogWidth, (int)(_dialogHeight * .05));
	        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, lines);
	        _listView.setAdapter(adapter);
	        layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        layoutParams.topMargin = 0;
	        layoutParams.leftMargin = 0;
	        layoutParams.height = _dialogHeight;
	        layoutParams.width = _dialogWidth;
	        dialogLayout.addView(_listView, layoutParams);  	
		
	        // close button
			Bitmap closeButtonBitmap = _app.getAppBitmap("log_view_close_button", R.drawable.button_close, cancelButtonSize, cancelButtonSize);
            SizedImageButton closeButton = new SizedImageButton(context, closeButtonBitmap);
            closeButton.setOnClickListener(new View.OnClickListener() { @Override
			public void onClick(View v) { dismiss(); } }); 
            layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 0;
            layoutParams.leftMargin = _dialogWidth - cancelButtonSize;
            dialogLayout.addView(closeButton, layoutParams);
		}
		catch(Exception ex) { ex.printStackTrace(); }
    	
    	setContentView(_mainLayout);
        getWindow().setLayout(_screenWidth, _screenHeight);
	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// handles all touches that aren't handled by other UI components
		return false;
	}

}
