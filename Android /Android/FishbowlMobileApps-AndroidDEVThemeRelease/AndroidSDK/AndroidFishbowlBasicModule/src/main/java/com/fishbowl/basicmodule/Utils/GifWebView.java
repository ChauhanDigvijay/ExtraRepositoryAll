package com.fishbowl.basicmodule.Utils;
import android.content.Context;
import android.graphics.Color;
import android.webkit.WebView;
/**
 * Created by digvijay(dj)
 */
public class GifWebView extends WebView {

	public GifWebView(Context context, String path) {
		super(context);        

		loadUrl(path);
		setBackgroundColor(Color.TRANSPARENT);
		
	}
}