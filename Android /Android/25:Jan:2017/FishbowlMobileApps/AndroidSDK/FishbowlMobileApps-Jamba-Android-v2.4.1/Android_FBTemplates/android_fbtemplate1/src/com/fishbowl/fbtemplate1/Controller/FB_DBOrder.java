//dj initial
package com.fishbowl.fbtemplate1.Controller;

/**
 **
 * Created by Digvijay Chauhan on 17/12/15.
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
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;
import com.fishbowl.fbtemplate1.Model.OrderItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FB_DBOrder
{
	private static FB_DBOrder dborder;
	private static Context appContext;
	// Database fields
	private SQLiteDatabase database;
	private FishbowlDbHelper dbHelper;

	private String[] allColumns = 
		{ 
				FishbowlDbHelper.COLUMN_ID, 
				FishbowlDbHelper.COLUMN_FB_ORDERID, 	
				FishbowlDbHelper.COLUMN_FB_ORDERSTATUS, 
				FishbowlDbHelper.COLUMN_FB_ORDERMESSAGE,
				FishbowlDbHelper.COLUMN_FB_ORDERTOTALPRICE, 
				FishbowlDbHelper.COLUMN_FB_ORDERDATE, 
				FishbowlDbHelper.COLUMN_FB_STOREID, 
				FishbowlDbHelper.COLUMN_FB_ORDERTIME, 
				FishbowlDbHelper.COLUMN_FB_ORDERTYPE, 

		};

	private final List<String> allColumnsList = Arrays.asList(allColumns);

	public static Context getAppContext() 
	{
		return appContext;
	}

	public static void setAppContext(Context appContext)
	{
		FB_DBOrder.appContext = appContext;
	}

	private FB_DBOrder() 
	{
		dbHelper = FishbowlDbHelper.getInstance(appContext);
		this.open();
	}

	public static FB_DBOrder getInstance() 
	{
		if(appContext == null)
		{
			return null;
		}
		if (dborder == null)
		{
			dborder = new FB_DBOrder();
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

	public OrderItem createOrder(OrderItem order)
	{
		return createOrder(order, true);
	}

	public OrderItem createOrder(OrderItem order, boolean syncOrderConfirmOnline) 
	{
		ContentValues values = new ContentValues();

		values.put(FishbowlDbHelper.COLUMN_FB_ORDERID, order.getOrderId());		
		values.put(FishbowlDbHelper.COLUMN_FB_ORDERMESSAGE, order.getOrderMessage());					
		values.put(FishbowlDbHelper.COLUMN_FB_ORDERSTATUS, order.getOrderStatus());		
		values.put(FishbowlDbHelper.COLUMN_FB_ORDERTOTALPRICE, order.getOrderPrice());
		values.put(FishbowlDbHelper.COLUMN_FB_ORDERDATE, order.getOrdereDateNTime());
		values.put(FishbowlDbHelper.COLUMN_FB_STOREID, order.getStoreID());
		values.put(FishbowlDbHelper.COLUMN_FB_ORDERTIME, order.getOrderTime());
		values.put(FishbowlDbHelper.COLUMN_FB_ORDERTYPE, order.getOrderType());
		// values.put(UserDBHelper.COLUMN_ID, order.getId());
		if (order.getId() > 0)
		{
			database.update(FishbowlDbHelper.FB_ORDER, values,
					FishbowlDbHelper.COLUMN_ID + "=" + order.getId(), null);
		} 
		else 
		{
			long insertId = database.insert(FishbowlDbHelper.FB_ORDER, null,
					values);
			order.setId(insertId);
		}

		if(syncOrderConfirmOnline)
		{

			//	createUpdateOrder(order);
		}

		return order;
	}
	public void deleteOrder(OrderItem order) 
	{
		long id = order.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(FishbowlDbHelper.FB_ORDER, FishbowlDbHelper.COLUMN_ID + " = "
				+ id, null);
	}

	public List<OrderItem> getAllOrder() 
	{
		List<OrderItem> users = new ArrayList<OrderItem>();

		Cursor cursor = database.query(FishbowlDbHelper.FB_ORDER, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			OrderItem order = cursorToOrder(cursor);
			users.add(order);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}

	public OrderItem cursorToOrder(Cursor cursor) 
	{
		OrderItem order = new OrderItem();

		order.setId(cursor.getLong(allColumnsList.indexOf(FishbowlDbHelper.COLUMN_ID)));
		order.setOrderId(cursor.getInt(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ORDERID)));
		order.setOrderMessage(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ORDERMESSAGE)));
		order.setOrderPrice(cursor.getDouble(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ORDERTOTALPRICE)));		
		order.setOrderStatus(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ORDERSTATUS)));
		order.setOrderType(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ORDERTYPE)));
		order.setOrdereDateNTime(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ORDERDATE)));
		order.setOrderTime(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_ORDERTIME)));
		order.setStoreID(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_STOREID)));


		return order;
	}

	public OrderItem createUpdateOrder(OrderItem order)
	{
		try
		{
			JSONObject userJSON = new JSONObject();
			userJSON.put("OrderItem-"+FishbowlDbHelper.COLUMN_FB_ORDERID, order.getOrderId());			
			userJSON.put("OrderItem-"+FishbowlDbHelper.COLUMN_FB_ORDERMESSAGE, order.getOrderMessage());
			userJSON.put("OrderItem-"+FishbowlDbHelper.COLUMN_FB_ORDERTOTALPRICE, order.getOrderPrice());
			userJSON.put("OrderItem-"+FishbowlDbHelper.COLUMN_FB_ORDERSTATUS, order.getOrderStatus());			
			userJSON.put("OrderItem-id", 0);

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
		return order;
	}

	public  List<MenuDrawerItem> createdummyUpdateOrder(final List<MenuDrawerItem> lists)
	{
		try
		{
			JSONObject userJSON = new JSONObject();
			userJSON.put("orderNumber", "-1");			
			userJSON.put("customerId", "123");
			userJSON.put("customerName","yes");					
			userJSON.put("companyID", "8");


			RequestQueue queue = Volley.newRequestQueue(appContext);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.POST,"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/createorder", userJSON,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{
							try
							{

								if(response != null)
								{

									OrderItem lt=new OrderItem();

									String message= response.getString("message");

									lt.setOrderMessage((response.getString("message")));
									lt.setOrderStatus(response.getString("successFlag"));



									if(message.indexOf("-") >=0)
									{
										lt.setOrderId(Integer.valueOf(message.split("-")[1]));
									}

									if(lists!=null)
									{
										if(lists.size()>0){
											MenuDrawerItem item=	lists.get(lists.size());
											if(item!=null)
											{
												lt.setOrderPrice(item.getExt());
											}


											lt.setItemlist(lists);
										}
									}
									FB_DBOrder.getInstance().createOrder(lt);

									//System.out.println("updatefield response: " );
								} 
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
		return lists;
	}
}
