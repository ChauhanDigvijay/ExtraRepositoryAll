//dj initial
package com.fishbowl.fbtemplate1.Controller;

/**
 **
 * Created by Digvijay Chauhan on 9/1/16.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fishbowl.fbtemplate1.DBHelper.FishbowlDbHelper;
import com.fishbowl.fbtemplate1.Model.UserAddressItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



public class FB_DBUserAddress
{
	private static FB_DBUserAddress dbuser;
	private static Context appContext;
	// Database fields
	private SQLiteDatabase database;
	private FishbowlDbHelper dbHelper;
	private String[] allColumns = { FishbowlDbHelper.COLUMN_ID, FishbowlDbHelper.COLUMN_FB_USERID, FishbowlDbHelper.COLUMN_FB_USERADDRESS_TYPE,
			FishbowlDbHelper.COLUMN_FB_USERADDRESS_STREET, FishbowlDbHelper.COLUMN_FB_USERADDRESS_TOWNCITY,
			FishbowlDbHelper.COLUMN_FB_USERADDRESS_STATEREGION, FishbowlDbHelper.COLUMN_FB_USERADDRESS_POSTZIP,
			FishbowlDbHelper.COLUMN_FB_USERADDRESS_COUNTRY,
			FishbowlDbHelper.COLUMN_FB_USERADDRESS_PREFRERRED, 
			FishbowlDbHelper.COLUMN_FB_USERADDRESS_GEOLOCATION,
			FishbowlDbHelper.COLUMN_FB_USERADDRESS_LOCATIONID, 
		 };
	private final List<String> allColumnsList = Arrays.asList(allColumns);

	public static Context getAppContext() {
		return appContext;
	}

	public static void setAppContext(Context appContext) {
		FB_DBUserAddress.appContext = appContext;
	}

	private FB_DBUserAddress() {
		dbHelper = FishbowlDbHelper.getInstance(appContext);
		this.open();
	}

	public static FB_DBUserAddress getInstance() {
		if(appContext == null){
			return null;
		}
		if (dbuser == null) {
			dbuser = new FB_DBUserAddress();
		}
		return dbuser;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public UserAddressItem createUpdateLocation(UserAddressItem location) {
		return createUpdateLocation(location, true);
	}

	public UserAddressItem createUpdateLocation(UserAddressItem location, boolean syncUserOnline) {
		ContentValues values = new ContentValues();
		values.put(FishbowlDbHelper.COLUMN_FB_USERID, location.getUserID());
		values.put(FishbowlDbHelper.COLUMN_FB_USERADDRESS_TYPE, location.getUseraddressType());
		values.put(FishbowlDbHelper.COLUMN_FB_USERADDRESS_STREET, location.getUserStreet());
		values.put(FishbowlDbHelper.COLUMN_FB_USERADDRESS_TOWNCITY, location.getUsertownCity());
		values.put(FishbowlDbHelper.COLUMN_FB_USERADDRESS_STATEREGION, location.getUserstateRegion());
		values.put(FishbowlDbHelper.COLUMN_FB_USERADDRESS_POSTZIP, location.getUserpostZip());
		values.put(FishbowlDbHelper.COLUMN_FB_USERADDRESS_COUNTRY, location.getUserCountry());
		values.put(FishbowlDbHelper.COLUMN_FB_USERADDRESS_PREFRERRED, location.isUserPreferred() ? 1 : 0);
		values.put(FishbowlDbHelper.COLUMN_FB_USERADDRESS_GEOLOCATION, location.getUsergeoLoc());
		values.put(FishbowlDbHelper.COLUMN_FB_USERADDRESS_LOCATIONID, location.getUserlocationID());
		
		// values.put(UserDBHelper.COLUMN_ID, location.getId());
		if (location.getId() > 0) {
			database.update(FishbowlDbHelper.FB_USERADDRESS, values,
					FishbowlDbHelper.COLUMN_ID + "=" + location.getId(), null);
		} 
		else 
		{
			long insertId = database.insert(FishbowlDbHelper.FB_USERADDRESS, null,values);
			location.setId(insertId);
		}
		if(syncUserOnline)
		{
			// sync location to online DB
		//	createUpdateLocationOnline(location);
		}
		return location;
	}
	public void deleteLocation(UserAddressItem location) 
	{
		long id = location.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(FishbowlDbHelper.FB_USERADDRESS, FishbowlDbHelper.COLUMN_ID + " = "
				+ id, null);
	}

	public List<UserAddressItem> getAllLocation() 
	{
		List<UserAddressItem> users = new ArrayList<UserAddressItem>();

		Cursor cursor = database.query(FishbowlDbHelper.FB_USERADDRESS, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) 
		{
			UserAddressItem location = cursorToLocation(cursor);
			users.add(location);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return users;
	}

	public UserAddressItem getAllLocationWithStore1(String stringid) 
	{
		//UserAddressItem UserAddressItem = new UserAddressItem();

		Cursor cursor = database.query(FishbowlDbHelper.FB_USERADDRESS, allColumns,
				 FishbowlDbHelper.COLUMN_FB_STOREID + "=?", new String[] {stringid}, null, null, null);

		
			UserAddressItem location = cursorToLocation(cursor);
			
		
		// make sure to close the cursor
	
		return location;
	}
	public List<UserAddressItem> getAllLocationWithStore(String stringid) 
	{
		List<UserAddressItem> users = new ArrayList<UserAddressItem>();

		Cursor cursor = database.query(FishbowlDbHelper.FB_USERADDRESS, allColumns,
				FishbowlDbHelper.COLUMN_FB_STOREID + "=?", new String[] {stringid}, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) 
		{
			UserAddressItem location = cursorToLocation(cursor);
			users.add(location);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return users;
	}
	
	
	
	public UserAddressItem cursorToLocation(Cursor cursor) 
	{
		UserAddressItem location = new UserAddressItem();
		location.setId(cursor.getLong(allColumnsList.indexOf(FishbowlDbHelper.COLUMN_ID)));
		location.setUserID(cursor.getString(allColumnsList.indexOf(FishbowlDbHelper.COLUMN_FB_USERID)));
		location.setUseraddressType(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERADDRESS_TYPE)));
		location.setUserStreet(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERADDRESS_STREET)));
		location.setUsertownCity(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERADDRESS_TOWNCITY)));
		location.setUserstateRegion(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERADDRESS_STATEREGION)));
		location.setUserpostZip(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERADDRESS_POSTZIP)));
		location.setUserCountry(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERADDRESS_COUNTRY)));		
		
		location.setUserPreferred(cursor.getInt(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERADDRESS_PREFRERRED))== 1 ? true : false);
		location.setUsergeoLoc(cursor.getDouble(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERADDRESS_GEOLOCATION)));
		location.setUserlocationID(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERADDRESS_LOCATIONID)));		
		
	
		return location;
	}

	public UserAddressItem createUpdateLocationOnline(UserAddressItem location)
	{
		try{
			JSONObject userJSON = new JSONObject();
			userJSON.put("UserAddressItem-"+FishbowlDbHelper.COLUMN_FB_USERID, location.getUserID());
			userJSON.put("UserAddressItem-"+FishbowlDbHelper.COLUMN_FB_USERADDRESS_TYPE, location.getUseraddressType());
			userJSON.put("UserAddressItem-"+FishbowlDbHelper.COLUMN_FB_USERADDRESS_STREET, location.getUserStreet());
			userJSON.put("UserAddressItem-"+FishbowlDbHelper.COLUMN_FB_USERADDRESS_TOWNCITY, location.getUsertownCity());
			userJSON.put("UserAddressItem-"+FishbowlDbHelper.COLUMN_FB_USERADDRESS_STATEREGION, location.getUserstateRegion());
			userJSON.put("UserAddressItem-"+FishbowlDbHelper.COLUMN_FB_USERADDRESS_POSTZIP, location.getUserpostZip());
			userJSON.put("UserAddressItem-"+FishbowlDbHelper.COLUMN_FB_USERADDRESS_COUNTRY, location.getUserCountry());
			userJSON.put("UserAddressItem-"+FishbowlDbHelper.COLUMN_FB_USERADDRESS_PREFRERRED, location.isUserPreferred());
			userJSON.put("UserAddressItem-"+FishbowlDbHelper.COLUMN_FB_USERADDRESS_GEOLOCATION, location.getUsergeoLoc());
			userJSON.put("UserAddressItem-"+FishbowlDbHelper.COLUMN_FB_USERADDRESS_LOCATIONID, location.getUserlocationID());
		
			
			userJSON.put("UserAddressItem-id", 0);
			
			JSONObject requestObj = new JSONObject();
			requestObj.put("data", userJSON);
			RequestQueue queue = Volley.newRequestQueue(appContext);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.POST,"", requestObj,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try{
								
							} catch(Exception ex){
								
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							if(error != null){
								System.out.println("updatefield response: " + error.toString());
							}
						}
					});
			queue.add(jsObjRequest);
		} catch (Exception ex){
			
		}
		return location;
	}
}
