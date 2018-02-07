package com.raleys.app.android;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.raleys.libandroid.SizedImageTextButton;
import com.raleys.libandroid.SizedTextView;
import com.raleys.libandroid.TextDialog;

public class ListCreateView extends ListModifyBaseView
{
	private RaleysApplication _app;
	private Context _context;
	private Object _handler;
	private String _callback;
	private Typeface _normalFont;
	private Typeface _boldFont;
	private EditText _listNameEditText;

	public ListCreateView(Context context, float width, float height, float top, String callback)
	{
		super(context, width, height, top);
		
		try
		{
			_context = context;
            _handler = context;
			_callback = callback;
	        _app = (RaleysApplication)context.getApplicationContext();
	        _normalFont = _app.getNormalFont();
	        _boldFont = _app.getBoldFont();

	        // title text
	        SizedTextView titleText = new SizedTextView(context, (int)(_dialogWidth * .96), (int)(_dialogHeight * .12), Color.TRANSPARENT, Color.argb(255, 204, 0, 0), _normalFont, "Create a New List");
	        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        layoutParams.leftMargin = _dialogLeft + (int)(_dialogWidth * .02);
	        layoutParams.topMargin = _dialogTop + (int)(_dialogHeight * .22);
	        _mainLayout.addView(titleText, layoutParams);
	        
            // first name text field
	        int textFieldHeight = (int)(_dialogHeight * .12);
	        _listNameEditText = new EditText(context);
	        _listNameEditText.setBackgroundColor(Color.WHITE);
	        _listNameEditText.setTextColor(Color.BLACK);
	        _listNameEditText.setTypeface(_normalFont);
	        _listNameEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)(textFieldHeight * .5));
	        _listNameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
	        _listNameEditText.setHint("List Name");
            layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    		layoutParams.topMargin = _dialogTop + (int)(_dialogHeight * .57);
            layoutParams.leftMargin = _dialogLeft + (int)(_dialogWidth * .1);
            layoutParams.width = (int)(_dialogWidth * .8);
            layoutParams.height = textFieldHeight;
            _mainLayout.addView(_listNameEditText, layoutParams);

	        // create button
	        Bitmap createBitmap = _app.getAppBitmap("list_create_button", R.drawable.map_forward_button, (int)(_dialogWidth  * .4), (int)(_dialogHeight  * .12));
	        SizedImageTextButton createButton = new SizedImageTextButton(context, createBitmap, .7f, Color.WHITE, _normalFont, "Create");
	    	createButton.setOnClickListener(new View.OnClickListener() { @Override
			public void onClick(View v) { createButtonPressed(); } }); 
	        layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        layoutParams.leftMargin = _dialogLeft + ((_dialogWidth - createBitmap.getWidth()) / 2);
	        layoutParams.topMargin = _dialogTop + (int)(_dialogHeight * .80);
	        _mainLayout.addView(createButton, layoutParams);
		}
		catch(Exception ex) { ex.printStackTrace(); }
	}
	
	
	public void createButtonPressed()
	{
		String listName = _listNameEditText.getText().toString();		
		
		if(listName == null || listName.equalsIgnoreCase(""))
		{
			TextDialog errorDialog = new TextDialog(_context, _app._dialogBackground, _boldFont, _normalFont, "Input Error", "List Name field can't be blank");
			errorDialog.show();			
        	return;
		}
		
		try { _handler.getClass().getMethod(_callback, String.class).invoke(_handler, listName); }
		catch(Exception ex) { ex.printStackTrace(); }
	}
}
