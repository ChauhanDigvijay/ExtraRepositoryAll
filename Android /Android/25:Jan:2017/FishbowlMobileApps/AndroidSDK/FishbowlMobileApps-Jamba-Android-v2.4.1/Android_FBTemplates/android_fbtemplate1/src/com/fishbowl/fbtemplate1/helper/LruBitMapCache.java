package com.fishbowl.fbtemplate1.helper;

/**
 **
 * Created by Digvijay Chauhan on 11/12/15.
 */
import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

//digvijay Chauhan This class is required to handle image cache.
public class LruBitMapCache extends LruCache<String, Bitmap> implements
ImageCache {

	//dj change it from 8 to 16

	//volley to getDefaultLruCacheSize
	public static int getDefaultLruCacheSize() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024*2);
		final int cacheSize = maxMemory / 16;

		return cacheSize;
	}

	//volley LruBitMapCache
	public LruBitMapCache() {
		this(getDefaultLruCacheSize());
	}

	//volley LruBitMapCache
	public LruBitMapCache(int sizeInKiloBytes) {
		super(sizeInKiloBytes);
	}

	//volley sizeOf
	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight() / 1024;
	}
	//volley getBitmap
	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}
	//volley putBitmap
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}


	public Bitmap getStaticBitmap(int drawable) {
		Bitmap bitMap = get(drawable+"");
		if(bitMap == null)
		{

		}
		return bitMap;
	}

	public void putStaticBitmap(int drawable, Bitmap bitmap) {
		put(drawable+"", bitmap);
	}
}