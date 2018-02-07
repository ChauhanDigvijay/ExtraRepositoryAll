package com.fishbowl.fbtemplate1.activity;
/**
 **
 * Created by Digvijay Chauhan on 14/12/15.
 */
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.HistoryAdapter;
import com.fishbowl.fbtemplate1.Adapter.OrderConfirmDrawerAdapter;
import com.fishbowl.fbtemplate1.Controller.FB_DBOrder;
import com.fishbowl.fbtemplate1.Controller.FB_DBOrderConfirm;
import com.fishbowl.fbtemplate1.CoreActivity.MainActivity;
import com.fishbowl.fbtemplate1.Model.LocationItem;
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;
import com.fishbowl.fbtemplate1.Model.OrderConfirmDrawItem;
import com.fishbowl.fbtemplate1.Model.OrderItem;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import net.hockeyapp.android.CrashManager;


@SuppressLint("NewApi") public class MenuHistoryActivity extends ActionBarActivity  
{
	List<OrderItem> orderlist;
	private DrawerLayout drawerLayout;
	HistoryAdapter adapter;
	ListView listView;
	ActionBar mActionbar;
	public static List<LocationItem> dataList;
	public static List<OrderConfirmDrawItem> mdataList;
	List<MenuDrawerItem> lists;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);		
		final List<MenuDrawerItem> lists =FB_DBOrderConfirm.getInstance().getAllItem();
		setContentView(R.layout.fragment_listhistory);

		mActionbar = getSupportActionBar();
		orderlist=	FB_DBOrder.getInstance().getAllOrder();
		adapter = new HistoryAdapter(MenuHistoryActivity.this,R.layout.list_history,orderlist);
		ListView	listView = (ListView)MenuHistoryActivity.this.findViewById(R.id.history_list);
		listView.setAdapter(adapter);

		mdataList = new ArrayList<OrderConfirmDrawItem>();
		mdataList.add(new OrderConfirmDrawItem(true,"Sign in or Sign up","Enjoy the benefit of Zpizza.Save Favourite places an express order, and track your rewardal",R.drawable.ic_launcher)); // adding a spinner to the list
		mdataList.add(new OrderConfirmDrawItem("HOME", R.drawable.menu_home));
		mdataList.add(new OrderConfirmDrawItem("PIZZA MENU", R.drawable.menu_pizza_menu));
		mdataList.add(new OrderConfirmDrawItem("STORE LOCATOR", R.drawable.menu_storelocator));
		mdataList.add(new OrderConfirmDrawItem("MY ORDER", R.drawable.menu_myorder));
		mdataList.add(new OrderConfirmDrawItem("ORDER HISTORY", R.drawable.menu_orderhistory));
		mdataList.add(new OrderConfirmDrawItem("MY OFFERS", R.drawable.menu_myoffers));
		mdataList.add(new OrderConfirmDrawItem("MY FAVORITES", R.drawable.menu_myfavorites));
		mdataList.add(new OrderConfirmDrawItem("Privacy Policy | Term of use",0,0)); 

		OrderConfirmDrawerAdapter adapter1 = new OrderConfirmDrawerAdapter(this, R.layout.list_sign,mdataList);
		ListView lv1=(ListView)this.findViewById(R.id.left_drawer);
		lv1.setAdapter(adapter1);
		drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		setActionBar();

		lv1.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id)
			{
				navigateTo1(position);
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
			{

				navigateTo(position);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@SuppressLint("NewApi")
	protected void setActionBar() 
	{
		mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		setActionBarTitle();
		mActionbar.setHomeButtonEnabled(true);
		mActionbar.setDisplayHomeAsUpEnabled(true);
	}

	public void setActionBarTitle()
	{
		mActionbar.setTitle("ORDER HISTORY");
	}

	public static final String TAG = MainActivity.class.getSimpleName();
	public void navigateTo1(int position) {
		switch(position) {
		case 1:

			Intent homeintent = new Intent(this, SignUpActivity.class);		
			startActivity(homeintent);
			break;

		case 2:
			Intent menuintent = new Intent(this, MenuActivity.class);		
			startActivity(menuintent);
			break;

		case 3:

			Intent storeintent = new Intent(this, StoreLocationActivity.class);		
			startActivity(storeintent);
			break;

		case 4:
			Intent menuhistoryintent = new Intent(this, MenuHistoryActivity.class);		
			startActivity(menuhistoryintent);
			break;


		case 5:Intent menuhistoryintent1 = new Intent(this, MenuHistoryActivity.class);		
		startActivity(menuhistoryintent1);
		break;	

		case 7:	Intent storeintent1 = new Intent(this, StoreLocationActivity.class);		
		startActivity(storeintent1);
		break;		 

		case 6:			 		

		}
	}


	@Override
	protected void onResume() 
	{
		checkForCrashes();
		super.onResume();
	}

	public  void createdummyUpdateOrder(final List<MenuDrawerItem> lists)
	{
		try
		{
			JSONObject userJSON = new JSONObject();
			userJSON.put("orderNumber", "-1");			
			userJSON.put("customerId", "123");
			userJSON.put("customerName","yes");					
			userJSON.put("companyID", "8");


			RequestQueue queue = Volley.newRequestQueue(this);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.POST,"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/createorder", userJSON,
					new Response.Listener<JSONObject>() 
					{
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
										Double currPrice=0.00;
										lt.setOrderId(Integer.valueOf(message.split("-")[1]));
										for(MenuDrawerItem eachcurrentAttachment:lists)
										{

											{
												currPrice=currPrice+eachcurrentAttachment.getExt();
												eachcurrentAttachment.setOrderId(Integer.valueOf(message.split("-")[1]));
												FB_DBOrderConfirm.getInstance().createOrderConfirm(eachcurrentAttachment);	
											}

										}
										lt.setOrderPrice(currPrice);	

										SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
										String currentDateandTime = sdf.format(new Date());		
										lt.setOrdereDateNTime(currentDateandTime);	
									}									
									FB_DBOrder.getInstance().createOrder(lt);
									orderlist=	FB_DBOrder.getInstance().getAllOrder();
									adapter = new HistoryAdapter(MenuHistoryActivity.this,R.layout.list_history,orderlist);
									ListView	listView = (ListView)MenuHistoryActivity.this.findViewById(R.id.history_list);
									listView.setAdapter(adapter);


									listView.setOnItemClickListener(new OnItemClickListener() 
									{
										@Override
										public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
										{

											navigateTo(position);
										}
									});



								}

								//System.out.println("updatefield response: " );
							} 

							catch(Exception ex)
							{

							}
						}
					}, new Response.ErrorListener() 
					{
						@Override
						public void onErrorResponse(VolleyError error) 
						{
							if(error != null){
								System.out.println("updatefield response: " + error.toString());
							}
						}
					});
			queue.add(jsObjRequest);
		} catch (Exception ex){

		}

	}


	public void navigateTo(int position) 
	{
		Intent intent = new Intent(MenuHistoryActivity.this,OrderConfirmActivity.class);
		Bundle extras = new Bundle();

		if( orderlist!=null)	
		{

			OrderItem order= orderlist.get(position);
			final List<MenuDrawerItem> lists =FB_DBOrderConfirm.getInstance().getAllItemWithOrderno(order.getOrderId());
			extras.putSerializable("draweritem1", (Serializable)lists);
			extras.putSerializable("order", order);
			extras.putSerializable("historyflag", true);
		}

		intent.putExtras(extras);
		startActivityForResult(intent, 2);

	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{

		switch (item.getItemId())
		{
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() 
	{
		if (getFragmentManager().getBackStackEntryCount() == 0) 
		{
			this.finish();
		} 

		else
		{
			getFragmentManager().popBackStack();
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		setActionBar();

	}

	private void checkForCrashes()
	{
		CrashManager.register(this, "9665c28bb83c477dbf1081723fab9379");
	}

}
