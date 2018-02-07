package com.fishbowl.fbtemplate1.CoreActivity;
/**
 **
 * Created by Digvijay Chauhan on 14/12/15.
 */
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Controller.FB_DBLocation;
import com.fishbowl.fbtemplate1.Controller.FB_DBOffer;
import com.fishbowl.fbtemplate1.Controller.FB_DBOrder;
import com.fishbowl.fbtemplate1.Controller.FB_DBOrderConfirm;
import com.fishbowl.fbtemplate1.Controller.FB_DBUser;
import com.fishbowl.fbtemplate1.Controller.FB_DBUserAddress;
import com.fishbowl.fbtemplate1.Model.UserItem;
import com.fishbowl.fbtemplate1.activity.MenuActivity;
import com.fishbowl.fbtemplate1.activity.StoreLocationActivity;
import com.fishbowl.fbtemplate1.helper.LruBitMapCache;
import com.fishbowl.fbtemplate1.widget.Customfont;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;

public class FishbowlTemplate1App extends Application
{
	public static final String TAG = FishbowlTemplate1App.class.getSimpleName();	
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	LruBitMapCache mLruBitmapCache;
	private static FishbowlTemplate1App mInstance;
	private static MenuActivity mainActivity;

	private static StoreLocationActivity storeActivity;




	public static UserItem loggedInUser;


	public String regid = null;

	public static int countorderhistory;	
	public static int countoffer;	

	public static int countcart;

	@Override
	public void onCreate() 
	{
		super.onCreate();
		mInstance = this;  //dj volley private static
		FB_DBUser.setAppContext(this);
		FB_DBOrderConfirm.setAppContext(this);
		FB_DBOrder.setAppContext(this);
		FB_DBLocation.setAppContext(this);
		FB_DBOffer.setAppContext(this);
		FB_DBUserAddress.setAppContext(this);	


		//	Customfont.setDefaultFont(this, "DEFAULT", "CircleD_Font_by_CrazyForMusic.ttf");
		Customfont.setDefaultFont(this, "MONOSPACE", "FuturBoo.ttf");
		//	Customfont.setDefaultFont(this, "SERIF", "MyFontAsset3.ttf");
		//	Customfont.setDefaultFont(this, "SANS_SERIF", "MyFontAsset4.ttf");
	}

	//volley to get instance 
	public static synchronized FishbowlTemplate1App getInstance() {
		return mInstance;
	}


	//volley to getRequestQueue 
	public RequestQueue getRequestQueue()
	{
		if (mRequestQueue == null) 
		{
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	//volley to getImageLoader
	public ImageLoader getImageLoader() 
	{
		getRequestQueue();
		if (mImageLoader == null) 
		{
			getLruBitmapCache();
			mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
		}

		return this.mImageLoader;
	}

	public LruBitMapCache getLruBitmapCache() 
	{
		if (mLruBitmapCache == null)
			mLruBitmapCache = new LruBitMapCache();
		return this.mLruBitmapCache;
	}

	public static int getCountorderhistory() 
	{
		if(countorderhistory==0)
		{	
			FishbowlTemplate1App.countorderhistory = FB_DBOrderConfirm.getInstance().getAllItemnull().size();

		}
		return countorderhistory;
	}

	public static void setCountorderhistory(int countorderhistory)
	{

		/*	if(countorderhistory==0)
		{	
			FishbowlTemplate1App.countorderhistory = FB_DBOrderConfirm.getInstance().getAllItem().size();
		}
		else
		{*/
		FishbowlTemplate1App.countorderhistory = countorderhistory;
		/*}*/
	}


	public static int getCountoffer() {

		if(countoffer==0)
		{	
			FishbowlTemplate1App.countoffer = FB_DBOffer.getInstance().getAllItem().size();

		}
		return countoffer;
	}

	public static void setCountoffer(int countoffer) {
		FishbowlTemplate1App.countoffer = countoffer;
	}

	// volley to addToRequestQueue
	public <T> void addToRequestQueue(Request<T> req, String tag) 
	{
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}
	// volley to addToRequestQueue
	public <T> void addToRequestQueue(Request<T> req) 
	{
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	//volley to cancelPendingRequests
	public void cancelPendingRequests(Object tag) 
	{
		if (mRequestQueue != null) 
		{
			mRequestQueue.cancelAll(tag);
		}
	}




	public static MenuActivity getMainActivity()
	{
		return mainActivity;
	}
	//instantiate mainActivity refernce from MainActivity
	public static void setMainActivity(MenuActivity mainActivity)
	{
		FishbowlTemplate1App.mainActivity = mainActivity;
	}

	public static UserItem getLoggedInUser() 
	{
		return loggedInUser;
	}

	public static void setLoggedInUser(UserItem loggedInUser)
	{
		FishbowlTemplate1App.loggedInUser = loggedInUser;
	}


	public Uri getNotificationURI(String soundfileNameFn) 
	{

		Uri notificationuri = null;
		if (soundfileNameFn == null || soundfileNameFn == "" || soundfileNameFn.contains("nosound")) 
		{

			notificationuri = Uri.parse("android.resource://"+ getPackageName() + "/" + R.raw.bell);
			return null;// nosound
		}

		return notificationuri;
	}


	public static StoreLocationActivity getStoreActivity() {
		return storeActivity;
	}

	public static void setStoreActivity(StoreLocationActivity storeActivity) {
		FishbowlTemplate1App.storeActivity = storeActivity;
	}


	//
	// Get Application Version
	public int getAppVersion(Context context)
	{
		try 
		{
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} 
		catch (NameNotFoundException e) 
		{
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	public static int getCountcart() {
		return countcart;
	}

	public static void setCountcart(int countcart) {
		FishbowlTemplate1App.countcart = countcart;
	}

}
