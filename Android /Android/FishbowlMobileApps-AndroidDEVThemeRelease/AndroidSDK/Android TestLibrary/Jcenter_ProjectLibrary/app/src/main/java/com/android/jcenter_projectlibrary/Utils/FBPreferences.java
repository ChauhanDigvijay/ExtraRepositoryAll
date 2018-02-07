package com.android.jcenter_projectlibrary.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */
public class FBPreferences
{
	public static final int NULL_INT = -1;
	public static final long NULL_LONG = -1L;
	public static final double NULL_DOUBLE = -1.0;
	public static final String NULL_STRING = null;

	public static FBPreferences instance=null;

	private SharedPreferences mSharedPreferences;
	private Context mContext;

	public static FBPreferences sharedInstance(Context context){

		if(instance==null){
			instance=new FBPreferences(context);
		}

		return  instance;
	}

	public FBPreferences(Context context)
	{
		if(context == null) context = context;
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		mContext = context;
	}


	public void clearPreferences()
	{
		Editor editor = mSharedPreferences.edit();
		editor.clear();
		editor.commit();
	}






	public void setUserMemberId(long userId)
	{
		String key = "user_id";
		Editor editor = mSharedPreferences.edit();
		editor.putLong(key, userId);
		editor.commit();
	}




	public boolean IsDashboardin()
	{
		String key = "dashboardin";

		if(key==null)
			return  false;

		return mSharedPreferences.getBoolean(key, false);
	}


	public void setDashboardin(Boolean dashboard)
	{
		String key = "dashboardin";
		Editor editor = mSharedPreferences.edit();
		editor.putBoolean(key, dashboard);
		editor.commit();
	}



}
