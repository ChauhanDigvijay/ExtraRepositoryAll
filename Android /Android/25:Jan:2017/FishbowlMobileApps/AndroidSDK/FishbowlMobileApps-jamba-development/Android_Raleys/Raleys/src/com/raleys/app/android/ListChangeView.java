package com.raleys.app.android;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.raleys.app.android.models.ShoppingListName;
import com.raleys.libandroid.SizedImageView;
import com.raleys.libandroid.SizedTextView;

public class ListChangeView extends ListModifyBaseView
{
	private RaleysApplication _app;
	private String _callback;
	private ListView _listView;
	private ListNameAdapter _listNameAdapter;

	public ListChangeView(Context context, float width, float height, float top, ArrayList<ShoppingListName> listNames, String callback)
	{
		super(context, width, height, top);
		
        _callback = callback;
        _app = (RaleysApplication)context.getApplicationContext();

        SizedTextView titleText = new SizedTextView(context, (int)(_dialogWidth * .96), (int)(_dialogHeight * .12), Color.TRANSPARENT, Color.argb(255, 204, 0, 0), _app.getNormalFont(), "Select an Existing List");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = _dialogLeft + (int)(_dialogWidth * .02);
        layoutParams.topMargin = _dialogTop + (int)(_dialogHeight * .22);
        _mainLayout.addView(titleText, layoutParams);
        
        int tableCellHeight = (int)(_dialogHeight * .14);
        int tableWidth = (int)(_dialogWidth * .8);
        int maxTableHeight = (int)(_dialogHeight * .49);
        int tableHeight;

        if(listNames.size() * tableCellHeight < maxTableHeight)
            tableHeight = listNames.size() * tableCellHeight;
        else
            tableHeight = maxTableHeight;

        int listXOrigin = _dialogLeft + ((_dialogWidth - tableWidth) / 2);
        int listYOrigin = _dialogTop + (int)(_dialogHeight * .48) + ((maxTableHeight - tableHeight) / 2);
        
        // list
        _listView = new ListView(context);
        _listView.setBackgroundColor(Color.TRANSPARENT);
        _listView.setDividerHeight(0);
        layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = listYOrigin;
        layoutParams.leftMargin = listXOrigin;
        layoutParams.height = tableHeight;
        layoutParams.width = tableWidth;
        _mainLayout.addView(_listView, layoutParams);

		_listNameAdapter = new ListNameAdapter(context, listNames, tableWidth, tableCellHeight, _callback);
        _listView.setAdapter(_listNameAdapter);
        
        int borderHeight = (int)(_dialogHeight * .01);
        
	    SizedImageView topBorder = new SizedImageView(context, tableWidth, borderHeight);
	    topBorder.setImageBitmap(_app.getAppBitmap("listname_table_top_border", R.drawable.listname_table_top_border, tableWidth, borderHeight));
	    layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = listXOrigin;
		layoutParams.topMargin = listYOrigin;
		_mainLayout.addView(topBorder, layoutParams);
	    
	    SizedImageView bottomBorder = new SizedImageView(context, tableWidth, borderHeight);
	    bottomBorder.setImageBitmap(_app.getAppBitmap("listname_table_bottom_border", R.drawable.listname_table_bottom_border, tableWidth, borderHeight));
	    layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = listXOrigin;
		layoutParams.topMargin = listYOrigin + tableHeight - borderHeight;
		_mainLayout.addView(bottomBorder, layoutParams);
	}
}
