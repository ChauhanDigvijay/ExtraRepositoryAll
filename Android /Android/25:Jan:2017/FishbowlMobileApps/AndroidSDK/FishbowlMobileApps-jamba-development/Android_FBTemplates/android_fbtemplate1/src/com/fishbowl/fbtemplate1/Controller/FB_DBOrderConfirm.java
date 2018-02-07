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
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 **
 * Created by Digvijay Chauhan on 16/12/15.
 */
public class FB_DBOrderConfirm
{
	private static FB_DBOrderConfirm dborder;
	private static Context appContext;
	// Database fields
	private SQLiteDatabase database;
	private FishbowlDbHelper dbHelper;

	private String[] allColumns = 
		{ 
				FishbowlDbHelper.COLUMN_ID, 
				FishbowlDbHelper.COLUMN_FB_ORDERID,
				FishbowlDbHelper.COLUMN_FB_USERID, 	
				FishbowlDbHelper.COLUMN_FB_ITEM_ID, 
				FishbowlDbHelper.COLUMN_FB_ITEM_NAME,
				FishbowlDbHelper.COLUMN_FB_ITEM_PRICE, 
				FishbowlDbHelper.COLUMN_FB_ITEM_QUANTITY, 
				FishbowlDbHelper.COLUMN_FB_ITEM_TOTALPRICE,
				FishbowlDbHelper.COLUMN_FB_ITEM_DESCRIPTION,
				FishbowlDbHelper.COLUMN_FB_ITEM_IMAGEURL,
				FishbowlDbHelper.COLUMN_FB_ITEM_GRADIENT,

		};

	private final List<String> allColumnsList = Arrays.asList(allColumns);

	public static Context getAppContext() 
	{
		return appContext;
	}

	public static void setAppContext(Context appContext)
	{
		FB_DBOrderConfirm.appContext = appContext;
	}

	private FB_DBOrderConfirm() 
	{
		dbHelper = FishbowlDbHelper.getInstance(appContext);
		this.open();
	}

	public static FB_DBOrderConfirm getInstance() 
	{
		if(appContext == null)
		{
			return null;
		}
		if (dborder == null)
		{
			dborder = new FB_DBOrderConfirm();
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

	public MenuDrawerItem createOrderConfirm(MenuDrawerItem item)
	{
		return createOrderConfirm(item, true);
	}

	public MenuDrawerItem createOrderConfirm(MenuDrawerItem item, boolean syncOrderConfirmOnline) 
	{
		ContentValues values = new ContentValues();


		values.put(FishbowlDbHelper.COLUMN_FB_ITEM_ID, item.getItemId());

		values.put(FishbowlDbHelper.COLUMN_FB_USERID, item.getUserID());

		values.put(FishbowlDbHelper.COLUMN_FB_ITEM_NAME, item.getItemName());

		values.put(FishbowlDbHelper.COLUMN_FB_ITEM_PRICE, item.getPrice());

		values.put(FishbowlDbHelper.COLUMN_FB_ITEM_QUANTITY, item.getQuantity());

		values.put(FishbowlDbHelper.COLUMN_FB_ITEM_TOTALPRICE, item.getExt());

		values.put(FishbowlDbHelper.COLUMN_FB_ITEM_DESCRIPTION, item.getItemdesc());	

		values.put(FishbowlDbHelper.COLUMN_FB_ORDERID, item.getOrderId());

		values.put(FishbowlDbHelper.COLUMN_FB_ITEM_IMAGEURL, item.getImageurl());	

		values.put(FishbowlDbHelper.COLUMN_FB_ITEM_GRADIENT, item.getGradient());

		// values.put(UserDBHelper.COLUMN_ID, item.getId());
		if (item.getId() > 0)
		{
			database.update(FishbowlDbHelper.FB_ORDERCONFIRM, values,
					FishbowlDbHelper.COLUMN_ID + "=" + item.getId(), null);
		} 
		else 
		{
			long insertId = database.insert(FishbowlDbHelper.FB_ORDERCONFIRM, null,
					values);
			item.setId(insertId);
		}

		if(syncOrderConfirmOnline)
		{

			//	createUpdateOrderConfirm(item);
		}

		return item;
	}
	public void deleteOrderConfirm(MenuDrawerItem item) 
	{
		long id = item.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(FishbowlDbHelper.FB_ORDERCONFIRM, FishbowlDbHelper.COLUMN_ID + " = "
				+ id, null);
	}

	public List<MenuDrawerItem> getAllOrderConfirm() 
	{
		List<MenuDrawerItem> users = new ArrayList<MenuDrawerItem>();

		Cursor cursor = database.query(FishbowlDbHelper.FB_ORDERCONFIRM, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			MenuDrawerItem item = cursorToOrderConfirm(cursor);
			users.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}

	public List<MenuDrawerItem> getAllItemnull() 
	{
		List<MenuDrawerItem> users = new ArrayList<MenuDrawerItem>();

		Cursor cursor = database.query(FishbowlDbHelper.FB_ORDERCONFIRM,allColumns,null,
				null ,null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			MenuDrawerItem item = cursorToOrderConfirm(cursor);
			users.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}


	public List<MenuDrawerItem> getAllItem() 
	{
		List<MenuDrawerItem> users = new ArrayList<MenuDrawerItem>();

		Cursor cursor = database.query(FishbowlDbHelper.FB_ORDERCONFIRM,allColumns, FishbowlDbHelper.COLUMN_FB_ORDERID + "=?",
				new String[] {"0"} ,null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			MenuDrawerItem item = cursorToOrderConfirm(cursor);
			users.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}


	public List<MenuDrawerItem> getAllItemWithOrderno(int i) 
	{
		List<MenuDrawerItem> users = new ArrayList<MenuDrawerItem>();
		String orderno	=String.valueOf(i);

		Cursor cursor = database.query(FishbowlDbHelper.FB_ORDERCONFIRM,allColumns, FishbowlDbHelper.COLUMN_FB_ORDERID + "=?",
				new String[] {orderno} ,null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			MenuDrawerItem item = cursorToOrderConfirm(cursor);
			users.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return users;

	}


	public MenuDrawerItem cursorToOrderConfirm(Cursor cursor) 
	{
		MenuDrawerItem item = new MenuDrawerItem();
		item.setId(cursor.getLong(allColumnsList.indexOf(FishbowlDbHelper.COLUMN_ID)));
		item.setItemId(cursor.getInt(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ITEM_ID)));
		item.setUserID(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERID)));
		item.setItemName(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ITEM_NAME)));

		item.setImageurl(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ITEM_IMAGEURL)));

		item.setGradient(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ITEM_GRADIENT)));


		item.setPrice(cursor.getDouble(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ITEM_PRICE)));
		item.setQuantity(cursor.getInt(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ITEM_QUANTITY)));
		item.setExt(cursor.getDouble(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ITEM_TOTALPRICE)));		
		item.setItemdesc(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ITEM_DESCRIPTION)));
		item.setOrderId(cursor.getInt(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ORDERID)));

		return item;
	}

	public MenuDrawerItem createUpdateOrderConfirm(MenuDrawerItem item)
	{
		try
		{
			JSONObject userJSON = new JSONObject();
			userJSON.put("MenuDrawerItem-"+FishbowlDbHelper.COLUMN_FB_ITEM_ID, item.getId());
			userJSON.put("MenuDrawerItem-"+FishbowlDbHelper.COLUMN_FB_USERID, item.getUserID());
			userJSON.put("MenuDrawerItem-"+FishbowlDbHelper.COLUMN_FB_ITEM_NAME, item.getItemName());
			userJSON.put("MenuDrawerItem-"+FishbowlDbHelper.COLUMN_FB_ITEM_PRICE, item.getPrice());
			userJSON.put("MenuDrawerItem-"+FishbowlDbHelper.COLUMN_FB_ITEM_QUANTITY, item.getQuantity());
			userJSON.put("MenuDrawerItem-"+FishbowlDbHelper.COLUMN_FB_ITEM_TOTALPRICE, item.getExt());
			userJSON.put("MenuDrawerItem-"+FishbowlDbHelper.COLUMN_FB_ITEM_DESCRIPTION, item.getItemdesc());			
			userJSON.put("MenuDrawerItem-id", 0);

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
