//dj initial
package com.fishbowl.fbtemplate1.Controller;

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
import com.fishbowl.fbtemplate1.Model.OfferItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 **
 * Created by Digvijay Chauhan on 7/1/16.
 */
public class FB_DBOffer
{
	private static FB_DBOffer dborder;
	private static Context appContext;
	// Database fields
	private SQLiteDatabase database;
	private FishbowlDbHelper dbHelper;
	private String[] allColumns = 
		{ 
				FishbowlDbHelper.COLUMN_ID, 
				FishbowlDbHelper.COLUMN_FB_OFFERID,
				FishbowlDbHelper.COLUMN_FB_OFFERINAME, 	
				FishbowlDbHelper.COLUMN_FB_OFFERIURL, 
				FishbowlDbHelper.COLUMN_FB_OFFERIITEM,
				FishbowlDbHelper.COLUMN_FB_OFFERIOTHER, 
		};

	private final List<String> allColumnsList = Arrays.asList(allColumns);

	public static Context getAppContext() 
	{
		return appContext;
	}

	public static void setAppContext(Context appContext)
	{
		FB_DBOffer.appContext = appContext;
	}

	private FB_DBOffer() 
	{
		dbHelper = FishbowlDbHelper.getInstance(appContext);
		this.open();
	}

	public static FB_DBOffer getInstance() 
	{
		if(appContext == null)
		{
			return null;
		}
		if (dborder == null)
		{
			dborder = new FB_DBOffer();
		}
		return dborder;
	}

	public void open() throws SQLException 
	{
		database = dbHelper.getWritableDatabase();
	}

	public void close()
	{
		dbHelper.close();
	}

	public OfferItem createOrderConfirm(OfferItem item)
	{
		return createOrderConfirm(item, true);
	}

	public OfferItem createOrderConfirm(OfferItem item, boolean syncOrderConfirmOnline) 
	{
		ContentValues values = new ContentValues();
	
		
	//	values.put(FishbowlDbHelper.COLUMN_FB_ITEM_ID, item.getId());
		
		values.put(FishbowlDbHelper.COLUMN_FB_OFFERID, item.getOfferId());
		
		values.put(FishbowlDbHelper.COLUMN_FB_OFFERINAME, item.getOfferIName());
		
		values.put(FishbowlDbHelper.COLUMN_FB_OFFERIURL, item.getOfferIUrl());
		
		values.put(FishbowlDbHelper.COLUMN_FB_OFFERIITEM, item.getOfferIItem());
		
		values.put(FishbowlDbHelper.COLUMN_FB_OFFERIOTHER, item.getOfferIOther());
		
	
		// values.put(UserDBHelper.COLUMN_ID, item.getId());
		if (item.getId() > 0)
		{
			database.update(FishbowlDbHelper.FB_OFFER, values,
					FishbowlDbHelper.COLUMN_ID + "=" + item.getId(), null);
		} 
		else 
		{
			long insertId = database.insert(FishbowlDbHelper.FB_OFFER, null,
					values);
			item.setId(insertId);
		}

		if(syncOrderConfirmOnline)
		{

		//	createUpdateOrderConfirm(item);
		}

		return item;
	}
	public void deleteOrderConfirm(OfferItem item) 
	{
		long id = item.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(FishbowlDbHelper.FB_OFFER, FishbowlDbHelper.COLUMN_ID + " = "
				+ id, null);
	}

	public List<OfferItem> getAllOrderConfirm() 
	{
		List<OfferItem> users = new ArrayList<OfferItem>();

		Cursor cursor = database.query(FishbowlDbHelper.FB_OFFER, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			OfferItem item = cursorToOrderConfirm(cursor);
			users.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}


	public List<OfferItem> getAllItem() 
	{
		List<OfferItem> users = new ArrayList<OfferItem>();

		Cursor cursor = database.query(FishbowlDbHelper.FB_OFFER,allColumns, null, null ,null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			OfferItem item = cursorToOrderConfirm(cursor);
			users.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}
	
	
	public List<OfferItem> getAllItemWithOrderno(int i) 
	{
		List<OfferItem> users = new ArrayList<OfferItem>();
	 String orderno	=String.valueOf(i);

		Cursor cursor = database.query(FishbowlDbHelper.FB_OFFER,allColumns, FishbowlDbHelper.COLUMN_FB_ORDERID + "=?",
				new String[] {orderno} ,null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			OfferItem item = cursorToOrderConfirm(cursor);
			users.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	
	}
	
	
	public OfferItem cursorToOrderConfirm(Cursor cursor) 
	{
		OfferItem item = new OfferItem();
		item.setId(cursor.getLong(allColumnsList.indexOf(FishbowlDbHelper.COLUMN_ID)));
		item.setOfferId(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_OFFERID)));
		item.setOfferIName(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_OFFERINAME)));
		item.setOfferIUrl(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_OFFERIURL)));
		item.setOfferIItem(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_OFFERIITEM)));
		item.setOfferIOther(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_OFFERIOTHER)));
		

		return item;
	}

	public OfferItem createUpdateOrderConfirm(OfferItem item)
	{
		try
		{
			JSONObject userJSON = new JSONObject();
			userJSON.put("OfferItem-"+FishbowlDbHelper.COLUMN_FB_OFFERID, item.getOfferId());
			userJSON.put("OfferItem-"+FishbowlDbHelper.COLUMN_FB_OFFERINAME, item.getOfferIName());
			userJSON.put("OfferItem-"+FishbowlDbHelper.COLUMN_FB_OFFERIURL, item.getOfferIUrl());
			userJSON.put("OfferItem-"+FishbowlDbHelper.COLUMN_FB_OFFERIITEM, item.getOfferIItem());
			userJSON.put("OfferItem-"+FishbowlDbHelper.COLUMN_FB_OFFERIOTHER, item.getOfferIOther());
					
			userJSON.put("OfferItem-id", 0);

			JSONObject requestObj = new JSONObject();
			requestObj.put("data", userJSON);
			RequestQueue queue = Volley.newRequestQueue(appContext);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.POST,"", requestObj,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{
							try
							{

							} 
							catch(Exception ex)
							{

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
		return item;
	}
}
