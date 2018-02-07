//dj initial
package com.fishbowl.fbtemplate1.Controller;

/**
 **
 * Created by Digvijay Chauhan on 14/12/15.
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
import com.fishbowl.fbtemplate1.Model.LocationItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



public class FB_DBLocation
{
	private static FB_DBLocation dbuser;
	private static Context appContext;
	// Database fields
	private SQLiteDatabase database;
	private FishbowlDbHelper dbHelper;
	private String[] allColumns = { FishbowlDbHelper.COLUMN_ID, FishbowlDbHelper.COLUMN_FB_STOREID,
			FishbowlDbHelper.COLUMN_FB_STORENAME, FishbowlDbHelper.COLUMN_FB_STOREADDRESS,
			FishbowlDbHelper.COLUMN_FB_STORECITY, FishbowlDbHelper.COLUMN_FB_STORESTATE,
			FishbowlDbHelper.COLUMN_FB_STOREZIPCODE,
			FishbowlDbHelper.COLUMN_FB_STOREPHONENO, 
		 };
	private final List<String> allColumnsList = Arrays.asList(allColumns);

	public static Context getAppContext() {
		return appContext;
	}

	public static void setAppContext(Context appContext) {
		FB_DBLocation.appContext = appContext;
	}

	private FB_DBLocation() {
		dbHelper = FishbowlDbHelper.getInstance(appContext);
		this.open();
	}

	public static FB_DBLocation getInstance() {
		if(appContext == null){
			return null;
		}
		if (dbuser == null) {
			dbuser = new FB_DBLocation();
		}
		return dbuser;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public LocationItem createUpdateLocation(LocationItem location) {
		return createUpdateLocation(location, true);
	}

	public LocationItem createUpdateLocation(LocationItem location, boolean syncUserOnline) {
		ContentValues values = new ContentValues();
		values.put(FishbowlDbHelper.COLUMN_FB_STOREID, location.getStoreID());
		values.put(FishbowlDbHelper.COLUMN_FB_STORENAME, location.getName());
		values.put(FishbowlDbHelper.COLUMN_FB_STOREADDRESS, location.getAddress());
		values.put(FishbowlDbHelper.COLUMN_FB_STORECITY, location.getCity());
		values.put(FishbowlDbHelper.COLUMN_FB_STORESTATE, location.getState());
		values.put(FishbowlDbHelper.COLUMN_FB_STOREZIPCODE, location.getZipcode());
		values.put(FishbowlDbHelper.COLUMN_FB_STOREPHONENO, location.getPhone());
	
		// values.put(UserDBHelper.COLUMN_ID, location.getId());
		if (location.getId() > 0) {
			database.update(FishbowlDbHelper.FB_STORELOCATION, values,
					FishbowlDbHelper.COLUMN_ID + "=" + location.getId(), null);
		} 
		else 
		{
			long insertId = database.insert(FishbowlDbHelper.FB_STORELOCATION, null,values);
			location.setId(insertId);
		}
		if(syncUserOnline)
		{
			// sync location to online DB
		//	createUpdateLocationOnline(location);
		}
		return location;
	}
	public void deleteLocation(LocationItem location) 
	{
		long id = location.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(FishbowlDbHelper.FB_STORELOCATION, FishbowlDbHelper.COLUMN_ID + " = "
				+ id, null);
	}

	public List<LocationItem> getAllLocation() 
	{
		List<LocationItem> users = new ArrayList<LocationItem>();

		Cursor cursor = database.query(FishbowlDbHelper.FB_STORELOCATION, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) 
		{
			LocationItem location = cursorToLocation(cursor);
			users.add(location);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return users;
	}

	public LocationItem getAllLocationWithStore1(String stringid) 
	{
		//LocationItem LocationItem = new LocationItem();

		Cursor cursor = database.query(FishbowlDbHelper.FB_STORELOCATION, allColumns,
				 FishbowlDbHelper.COLUMN_FB_STOREID + "=?", new String[] {stringid}, null, null, null);

		
			LocationItem location = cursorToLocation(cursor);
			
		
		// make sure to close the cursor
	
		return location;
	}
	public List<LocationItem> getAllLocationWithStore(String stringid) 
	{
		List<LocationItem> users = new ArrayList<LocationItem>();

		Cursor cursor = database.query(FishbowlDbHelper.FB_STORELOCATION, allColumns,
				FishbowlDbHelper.COLUMN_FB_STOREID + "=?", new String[] {stringid}, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) 
		{
			LocationItem location = cursorToLocation(cursor);
			users.add(location);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return users;
	}
	
	
	
	public LocationItem cursorToLocation(Cursor cursor) 
	{
		LocationItem location = new LocationItem();
		location.setId(cursor.getLong(allColumnsList.indexOf(FishbowlDbHelper.COLUMN_ID)));
		location.setStoreID(cursor.getString(allColumnsList.indexOf(FishbowlDbHelper.COLUMN_FB_STOREID)));
		location.setName(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_STORENAME)));
		location.setAddress(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_STOREADDRESS)));
		location.setCity(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_STORECITY)));
		location.setState(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_STORESTATE)));
		location.setZipcode(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_STOREZIPCODE)));
		location.setPhone(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_STOREPHONENO)));		
		
	
		return location;
	}

	public LocationItem createUpdateLocationOnline(LocationItem location)
	{
		try{
			JSONObject userJSON = new JSONObject();
			userJSON.put("LocationItem-"+FishbowlDbHelper.COLUMN_FB_STOREID, location.getStoreID());
			userJSON.put("LocationItem-"+FishbowlDbHelper.COLUMN_FB_STORENAME, location.getName());
			userJSON.put("LocationItem-"+FishbowlDbHelper.COLUMN_FB_STOREADDRESS, location.getAddress());
			userJSON.put("LocationItem-"+FishbowlDbHelper.COLUMN_FB_STORECITY, location.getCity());
			userJSON.put("LocationItem-"+FishbowlDbHelper.COLUMN_FB_STORESTATE, location.getState());
			userJSON.put("LocationItem-"+FishbowlDbHelper.COLUMN_FB_STOREZIPCODE, location.getZipcode());
			userJSON.put("LocationItem-"+FishbowlDbHelper.COLUMN_FB_STOREPHONENO, location.getPhone());			
			userJSON.put("LocationItem-id", 0);
			
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
