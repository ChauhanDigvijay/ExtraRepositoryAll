package com.fishbowl.fbtemplate1.widget;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class GifWebView extends WebView {

	public GifWebView(Context context, String path) {
		super(context);        

		loadUrl(path);
		setBackgroundColor(Color.TRANSPARENT);
		
	}
}