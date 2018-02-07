package com.fishbowl.basicmodule.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.preference.PreferenceManager;

import com.fishbowl.basicmodule.R;

import java.util.Set;

/**
 * Created by digvijay(dj)
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


	public String getUserMemberforAppId()
	{
		String key = "user_id_forapp";
		return mSharedPreferences.getString(key, NULL_STRING);
	}

	public void setUserMemberforAppId(String userId)
	{
		String key = "user_id_forapp";
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, userId);
		editor.commit();
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


	public String getTenantId()
	{
		String key = mContext.getString(R.string.prefs_key_tenantid);
		return mSharedPreferences.getString(key, NULL_STRING);
	}

	public void setTenantId(String tenantId)
	{
		String key = mContext.getString(R.string.prefs_key_tenantid);
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, tenantId);
		editor.commit();
	}




	public long getGuestMemberId()
	{
		String key = mContext.getString(R.string.prefs_key_guest_member_id);
		return mSharedPreferences.getLong(key, NULL_LONG);
	}

	public void setGuestMemberId(long userId)
	{
		String key = mContext.getString(R.string.prefs_key_guest_member_id);
		Editor editor = mSharedPreferences.edit();
		editor.putLong(key, userId);
		editor.commit();
	}

	public long getUserMemberId()
	{
		String key = mContext.getString(R.string.prefs_key_user_member_id);
		return mSharedPreferences.getLong(key, NULL_LONG);
	}

	public void setUserMemberId(long userId)
	{
		String key = "user_id";
		Editor editor = mSharedPreferences.edit();
		editor.putLong(key, userId);
		editor.commit();
	}






	public void setDeviceId(String deviceId)
	{
		String key = "location";
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, deviceId);
		editor.commit();
	}

	public String getLocation() {
		String key = mContext.getString(R.string.prefs_key_device_id);
		return mSharedPreferences.getString(key, NULL_STRING);
	}

	public void setLocation(Location location)
	{
		String key = "location";
		Editor editor = mSharedPreferences.edit();
		editor.putStringSet(key, (Set<String>) location);
		editor.commit();
	}

	public String getDeviceId() {
		String key = mContext.getString(R.string.prefs_key_device_id);
		return mSharedPreferences.getString(key, NULL_STRING);
	}


	public void setPushToken(String pushtoken)
	{
		String key = mContext.getString(R.string.prefs_key_push_token);
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, pushtoken);
		editor.commit();
	}

	public String getPushToken() {
		String key = mContext.getString(R.string.prefs_key_push_token);
		return mSharedPreferences.getString(key, NULL_STRING);
	}


	public String getPassword()
	{
		String key = mContext.getString(R.string.prefs_key_password);
		return mSharedPreferences.getString(key, NULL_STRING);
	}


	public void setPassword(String password)
	{
		String key = mContext.getString(R.string.prefs_key_password);
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, password);
		editor.commit();
	}


	public String getVersion()
	{
		String key = mContext.getString(R.string.prefs_key_version);
		return mSharedPreferences.getString(key, NULL_STRING);
	}


	public void setVersion(String version)
	{
		String key = mContext.getString(R.string.prefs_key_version);
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, version);
		editor.commit();
	}


	public boolean getGuestRegister()
	{
		String key = mContext.getString(R.string.prefs_key_guestregister);

		if(key==null)
			return  false;

		return mSharedPreferences.getBoolean(key, false);
	}


	public void setGuestRegister(Boolean register)
	{
		String key = mContext.getString(R.string.prefs_key_guestregister);
		Editor editor = mSharedPreferences.edit();
		editor.putBoolean(key, register);
		editor.commit();
	}


	public String getAccessToken()
	{
		String key = "access_token";
		return mSharedPreferences.getString(key, NULL_STRING);
	}

	public void setAccessToken(String access_token)
	{
		String key = "access_token";
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, access_token);
		editor.commit();
	}


	public String getAccessTokenforapp()
	{
		String key = "access_tokenforapp";
		return mSharedPreferences.getString(key, NULL_STRING);
	}

	public void setAccessTokenforapp(String access_token)
	{
		String key = "access_tokenforapp";
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, access_token);
		editor.commit();
	}


	public String getExternalCustomerIdforapp()
	{
		String key = "ExternalCustomerId";
		return mSharedPreferences.getString(key, NULL_STRING);
	}

	public void setExternalCustomerIdforapp(String access_token)
	{
		String key = "ExternalCustomerId";
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, access_token);
		editor.commit();
	}

	public String getSpendGoAuthTokenforapp()
	{
		String key = "SpendGoAuthToken";
		return mSharedPreferences.getString(key, NULL_STRING);
	}

	public void setSpendGoAuthTokenforapp(String access_token)
	{
		String key = "SpendGoAuthToken";
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, access_token);
		editor.commit();
	}



	public String getClassSignatureforapp()
	{
		String key = "ClassSignature";
		return mSharedPreferences.getString(key, NULL_STRING);
	}

	public void setClassSignatureforapp(String access_token)
	{
		String key = "ClassSignature";
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, access_token);
		editor.commit();
	}


	public boolean IsSignin()
	{
		String key = mContext.getString(R.string.prefs_key_signin);

		if(key==null)
			return  false;

		return mSharedPreferences.getBoolean(key, false);
	}


	public void setSignin(Boolean Signin)
	{
		String key = mContext.getString(R.string.prefs_key_signin);
		Editor editor = mSharedPreferences.edit();
		editor.putBoolean(key, Signin);
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

	public long getLatestBuildCode()
	{
		String key = mContext.getString(R.string.prefs_key_latest_build_code);
		return mSharedPreferences.getLong(key, NULL_LONG);
	}

	public void setLatestBuildCode(long buildCode)
	{
		String key = mContext.getString(R.string.prefs_key_latest_build_code);
		Editor editor = mSharedPreferences.edit();
		editor.putLong(key, buildCode);
		editor.commit();
	}

	public boolean isUserDismissedAppUpdate()
	{
		String key = mContext.getString(R.string.prefs_key_user_dismiss_option);

		if(key == null) {
			return false;
		}

		return mSharedPreferences.getBoolean(key, false);
	}


	public void setUserDismissedAppUpdate(Boolean dismissOption)
	{
		String key = mContext.getString(R.string.prefs_key_user_dismiss_option);
		Editor editor = mSharedPreferences.edit();
		editor.putBoolean(key, dismissOption);
		editor.commit();
	}



}
