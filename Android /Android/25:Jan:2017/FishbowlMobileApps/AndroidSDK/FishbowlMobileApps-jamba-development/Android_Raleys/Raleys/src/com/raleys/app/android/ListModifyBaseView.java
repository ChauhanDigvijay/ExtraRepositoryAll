package com.raleys.app.android;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.raleys.libandroid.SizedImageButton;
import com.raleys.libandroid.SizedImageView;

public class ListModifyBaseView extends Dialog implements OnTouchListener
{
	private RaleysApplication _app;
	private int _screenWidth;
	private int _screenHeight;
	protected int _dialogLeft;
	protected int _dialogTop;
	protected int _dialogWidth;
	protected int _dialogHeight;
	protected RelativeLayout _mainLayout;
	
	
	@SuppressWarnings("deprecation")
	public ListModifyBaseView(Context context, float width, float height, float top)
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
	        _dialogTop = (int)(_screenHeight * top);
	        _dialogLeft = (_screenWidth - _dialogWidth) / 2;
	        
	    	_mainLayout = new RelativeLayout(context);
	    	_mainLayout.setOnTouchListener(this);
	    	_mainLayout.setBackgroundColor(Color.argb(127, 0, 0, 0));
	    	
	        int cancelButtonSize = (int)(_dialogHeight * .1);

	        // dialog layout
	        SizedImageView dialog = new SizedImageView(context, _dialogWidth, _dialogHeight);
	        dialog.setBackgroundDrawable(new BitmapDrawable(_app.getAppBitmap("list_modify_background", R.drawable.list_dialog_background, _dialogWidth, _dialogHeight)));
	        
	        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        layoutParams.topMargin = _dialogTop;
	        layoutParams.leftMargin = _dialogLeft;
	        layoutParams.width = _dialogWidth;
	        layoutParams.height = _dialogHeight;
	        _mainLayout.addView(dialog, layoutParams);
	        
	        // dialog icon
	        int iconWidth = (int)(_dialogWidth * .25);
	        int iconHeight = (int)(_dialogHeight * .35);
	        SizedImageView dialogIcon = new SizedImageView(context, iconWidth, iconHeight);
	        dialogIcon.setBackgroundDrawable(new BitmapDrawable(_app.getAppBitmap("list_modify_icon", R.drawable.list_dialog_icon, iconWidth, iconHeight)));
	        
	        layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        layoutParams.topMargin = _dialogTop + (int)(_dialogHeight * .1);
	        layoutParams.leftMargin = _dialogLeft + ((_dialogWidth - iconWidth) / 2);
	        _mainLayout.addView(dialogIcon, layoutParams);
	        
	        // close button
			Bitmap closeButtonBitmap = _app.getAppBitmap("list_modify_close_button", R.drawable.button_close, cancelButtonSize, cancelButtonSize);
            SizedImageButton closeButton = new SizedImageButton(context, closeButtonBitmap);
            closeButton.setOnClickListener(new View.OnClickListener() { @Override
			public void onClick(View v) { dismiss(); } }); 
            layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = _dialogTop;
            layoutParams.leftMargin = _dialogLeft + _dialogWidth - closeButtonBitmap.getWidth();
            _mainLayout.addView(closeButton, layoutParams);
	    	
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
