package com.fishbowl.fbtemplate1.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.List_OrderConfirmAdapter;
import com.fishbowl.fbtemplate1.Adapter.OrderConfirmDrawerAdapter;
import com.fishbowl.fbtemplate1.Controller.FB_DBOrder;
import com.fishbowl.fbtemplate1.Controller.FB_DBOrderConfirm;
import com.fishbowl.fbtemplate1.CoreActivity.MainActivity;
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;
import com.fishbowl.fbtemplate1.Model.OrderConfirmDrawItem;
import com.fishbowl.fbtemplate1.Model.OrderItem;
import com.fishbowl.fbtemplate1.util.StringUtilities;
import com.fishbowl.fbtemplate1.widget.MyMessageDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.hockeyapp.android.CrashManager;


@SuppressLint("NewApi") public class OrderConfirmActivity extends ActionBarActivity 
{


	TextView addmore,continuetv;
	ListView lv;
	ActionBar mActionbar;
	List_OrderConfirmAdapter adapter ;	
	public static Boolean checkback=false;	
	public static Boolean historyflag=false;	
	private DrawerLayout drawerLayout;	
	public static List<OrderConfirmDrawItem> dataList;
	List<MenuDrawerItem> listss ;
	MenuDrawerItem drawitem = null;
	OrderItem order=null;
	String storelocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{

		Intent i=getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			drawitem = (MenuDrawerItem) extras.getSerializable("draweritem");
			order = (OrderItem) extras.getSerializable("order");
			listss = (List<MenuDrawerItem>) i.getSerializableExtra("draweritem1");
			historyflag=extras.getBoolean("historyflag", false);
			storelocation=extras.getString("storelocation");

		}

		super.onCreate(savedInstanceState);


		setContentView(R.layout.test22);

		mActionbar = getSupportActionBar();

		lv=(ListView)this.findViewById(R.id.mobile_listdf);


		adapter = new List_OrderConfirmAdapter(this,drawitem,listss,checkback,historyflag,order,storelocation);

		adapter.notifyDataSetChanged();
		dataList = new ArrayList<OrderConfirmDrawItem>();
		dataList.add(new OrderConfirmDrawItem(true,"Sign in or Sign up","Enjoy the benefit of Zpizza.Save Favourite places an express order, and track your rewardal",R.drawable.ic_launcher)); // adding a spinner to the list
		dataList.add(new OrderConfirmDrawItem("HOME", R.drawable.menu_home));
		dataList.add(new OrderConfirmDrawItem("PIZZA MENU", R.drawable.menu_pizza_menu));
		dataList.add(new OrderConfirmDrawItem("STORE LOCATOR", R.drawable.menu_storelocator));
		dataList.add(new OrderConfirmDrawItem("MY ORDER", R.drawable.menu_myorder));
		dataList.add(new OrderConfirmDrawItem("ORDER HISTORY", R.drawable.menu_orderhistory));
		dataList.add(new OrderConfirmDrawItem("MY OFFERS", R.drawable.menu_myoffers));
		dataList.add(new OrderConfirmDrawItem("MY FAVORITES", R.drawable.menu_myfavorites));
		dataList.add(new OrderConfirmDrawItem("Privacy Policy | Term of use",0,0));
		OrderConfirmDrawerAdapter adapter1 = new OrderConfirmDrawerAdapter(this, R.layout.list_sign,dataList);

		ListView lv1=(ListView)this.findViewById(R.id.left_drawer);
		lv1.setAdapter(adapter1);
		drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		//setListViewHeightBasedOnChildren(lv);
		setActionBar();

		lv1.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id)
			{
				navigateTo(position);
			}
		});


	}
	@SuppressLint("NewApi")
	protected void setActionBar() 
	{

		mActionbar.setDisplayShowHomeEnabled(false);
		mActionbar.setDisplayShowTitleEnabled(false);
		mActionbar.setHomeButtonEnabled(false);
		mActionbar.setDisplayHomeAsUpEnabled(false);
		/*mActionbar.setHomeButtonEnabled(true);
		mActionbar.setDisplayHomeAsUpEnabled(true);*/


	/*	mActionbar.setDisplayShowHomeEnabled(false);
		mActionbar.setDisplayShowTitleEnabled(false);
		mActionbar.setHomeButtonEnabled(false);
		mActionbar.setDisplayHomeAsUpEnabled(false);*/
		/*	mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		//setActionBarTitle();
		mActionbar.setHomeButtonEnabled(true);
		mActionbar.setDisplayHomeAsUpEnabled(true);

		mActionbar.setHomeButtonEnabled(false);
		mActionbar.setDisplayHomeAsUpEnabled(false);
		mActionbar.setDisplayShowHomeEnabled(false);

		mActionbar.setDisplayShowTitleEnabled(false);

		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.custombar, null);

			ImageButton imageButton = (ImageButton) mCustomView.findViewById(R.id.imageButton);

		ImageButton imageButton = (ImageButton) mCustomView.findViewById(R.id.imageButton);


		addmore = (TextView) mCustomView.findViewById(R.id.title_textb);

		continuetv = (TextView) mCustomView.findViewById(R.id.title_textf);


		addmore.setOnClickListener(first_radio_listener);
		continuetv.setOnClickListener(first_radio_listener);

		//	offeriv = (ImageView) mCustomView.findViewById(R.id.actionbar_offer_imageview);	


		mActionbar.setCustomView(mCustomView);
		mActionbar.setDisplayShowCustomEnabled(true);*/
	}

	public void setActionBarTitle()
	{
		/*	int dj = Integer.parseInt(expenseClaimItem.getTimeTask().getExtStatus().getStatusID().toString());*/


	//	mActionbar.setTitle("ADD MORE");
	}




	public static final String TAG = MainActivity.class.getSimpleName();
	public void navigateTo(int position) 
	{
		switch(position) 
		{
		case 1:
			Intent homeintent = new Intent(this, TestSignIn.class);		
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

	public static void setListViewHeightBasedOnChildren(ListView listView) 
	{
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}


	public void back(View v)
	{

		finish();

	}




	public void backAndAdd(View v) 
	{

		Intent intent=new Intent(this,MenuActivity.class);  
		Bundle extras = new Bundle();
		checkback=false;
		intent.putExtras(extras);
		setResult(2,intent);  
		startActivityForResult(intent, 2);


	}


	public void Confirm()
	{
		JSONObject userJSON = new JSONObject();
		try {
			userJSON.put("orderNumber", "-1");
			userJSON.put("customerId", "123");
			userJSON.put("customerName","yes");					
			userJSON.put("companyID", "8");
			/*	if((StringUtilities.isValidString(order.getOrderTime())) && (StringUtilities.isValidString(order.getOrdereDateNTime()))&& (StringUtilities.isValidString(order.getOrderType())))*/
			{
				Intent intent = new Intent(this,PaymentActivity.class);	
				Bundle extras = new Bundle();

				{
					extras.putSerializable("draweritem1", (Serializable)listss);
					extras.putSerializable("order", order);
					extras.putSerializable("historyflag", true);
					extras.putSerializable("storelocation", storelocation);
				}
				intent.putExtras(extras);
				startActivityForResult(intent, 2);
			}
			/*else
			{

			}*/


		} catch (JSONException e) {
			e.printStackTrace();
		}			

	}

	public void Confirm_old(View v)
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
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{
							try
							{

								if(response != null)
								{

									String message= response.getString("message");

									order.setOrderMessage((response.getString("message")));
									order.setOrderStatus(response.getString("successFlag"));

									if(message.indexOf("-") >=0)
									{
										Double currPrice=0.00;
										order.setOrderId(Integer.valueOf(message.split("-")[1]));

										for(MenuDrawerItem eachcurrentAttachment:order.getItemlist())
										{
											{
												currPrice=currPrice+eachcurrentAttachment.getExt();
												eachcurrentAttachment.setOrderId(Integer.valueOf(message.split("-")[1]));
												FB_DBOrderConfirm.getInstance().createOrderConfirm(eachcurrentAttachment);	
											}

										}
										order.setOrderPrice(currPrice);	

										SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
										String currentDateandTime = sdf.format(new Date());		
										order.setOrdereDateNTime(currentDateandTime);	
									}								
									FB_DBOrder.getInstance().createOrder(order);
									Intent intens = new Intent(OrderConfirmActivity.this, MenuHistoryActivity.class);		
									startActivity(intens);
								}


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


	public void ExpenseAttachmentAPICall()	
	{

		List<MenuDrawerItem> lists =(List<MenuDrawerItem>) lv.getTag();
		for(MenuDrawerItem eachcurrentAttachment:lists)
		{

			{			
				FB_DBOrderConfirm.getInstance().createOrderConfirm(eachcurrentAttachment);	
			}

		}
	}

	public void order(View v)
	{

		List<MenuDrawerItem> lists =(List<MenuDrawerItem>) lv.getTag();
		if(lists.size()>0 && lists!=null)
		{
			ExpenseAttachmentAPICall();
			Intent intensw = new Intent(this, StoreLocationActivity.class);		
			startActivity(intensw);
		}
		else
		{
			new MyMessageDialog(OrderConfirmActivity.this,"ITEM","PLease select item before continue").show(getSupportFragmentManager(), "MyDialog");	
		}
	}


	
	public void cancelorder(View v)
	{
		Bundle extras = new Bundle();
		this.checkback=true;
		Intent inten = new Intent(OrderConfirmActivity.this, MenuActivity.class);	
		inten.putExtras(extras);
		startActivity(inten);;
		
	}
	
	
	@Override
	public void onBackPressed() 
	{
		if (getFragmentManager().getBackStackEntryCount() == 0)
		{
			checkback=true;
			this.finish();

		} 
		else
		{
			checkback=true;
			getFragmentManager().popBackStack();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		getMenuInflater().inflate(R.menu.menu_order, menu);
		MenuItem itemmo = menu.findItem(R.id.menu_edit1);

		MenuItemCompat.setActionView(itemmo, R.layout.custombar);

		RelativeLayout offer = (RelativeLayout) MenuItemCompat.getActionView(itemmo);
		ImageView backclick=(ImageView) offer.findViewById(R.id.imageView1);

		backclick.setOnClickListener(first_radio_listener);
		addmore = (TextView) offer.findViewById(R.id.title_textb);
		continuetv = (TextView) offer.findViewById(R.id.title_textf);
		addmore.setOnClickListener(first_radio_listener);
		continuetv.setOnClickListener(first_radio_listener);

		return super.onCreateOptionsMenu(menu);


	/*	 View mCustomView = mInflater.inflate(R.layout.custombar, null);
		    getSupportActionBar().setCustomView(mCustomView);
		    getSupportActionBar().setDisplayShowCustomEnabled(true);
		    Toolbar parent =(Toolbar) mCustomView.getParent();//first get parent toolbar of current action bar 
		    parent.setContentInsetsAbsolute(0,0);*/
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;


		case R.id.menu_edit1:
		{
			List<MenuDrawerItem> lists =(List<MenuDrawerItem>) lv.getTag();
			if(order!=null && StringUtilities.isValidString(order.getOrdereDateNTime()))
			
			{
				Intent intensw = new Intent(this, PaymentActivity.class);		
				startActivity(intensw);
			}
			else if(lists.size()>0 && lists!=null)
			{
				ExpenseAttachmentAPICall();
				Intent intensw = new Intent(this, StoreLocationActivity.class);		
				startActivity(intensw);	
			}
			else
			{
				new MyMessageDialog(OrderConfirmActivity.this,"ITEM","PLease select item before continue").show(getSupportFragmentManager(), "MyDialog");	
			}
			break;
		}



		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStart() 
	{
		super.onStart();
		lv.setAdapter(adapter);
		lv.invalidateViews();
		lv.refreshDrawableState();
		adapter.notifyDataSetChanged();
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

			}
		});


	}
	public void customAlertDialog()
	{
		try {

			final Dialog dialog = new Dialog(this,R.style.CustomDialogTheme);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_dialog_new);
			dialog.setCancelable(false);
			Button cancel = (Button)dialog.findViewById(R.id.textCancel);
			Button never = (Button)dialog.findViewById(R.id.textNever);
			Button ok = (Button)dialog.findViewById(R.id.textOk);
			TextView desc = (TextView) dialog.findViewById(R.id.txt_dia);

			desc.setText("Do You want to exit!!");

			ok.setOnClickListener(new View.OnClickListener() 
			{

				@Override
				public void onClick(View v) 
				{
					dialog.dismiss();
					OrderConfirmActivity.this.finish();

				}
			});

			never.setVisibility(View.GONE);

			cancel.setOnClickListener(new View.OnClickListener() 
			{

				@Override
				public void onClick(View v)
				{
					dialog.dismiss();

				}
			});

			dialog.setCancelable(true);
			dialog.show();
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	OnClickListener first_radio_listener = new OnClickListener (){
		public void onClick(View v) {

			//Your Implementaions...
			switch (v.getId()) 
			{


			case R.id.title_textb:
			{
				checkback=false;
				Intent inten = new Intent(OrderConfirmActivity.this, MenuActivity.class);					
				startActivity(inten);;
				break;
			}
			case R.id.title_textf:

			
			{
				List<MenuDrawerItem> lists =(List<MenuDrawerItem>) lv.getTag();
				if(order!=null && StringUtilities.isValidString(order.getOrdereDateNTime()))
				
				{
					Confirm();
				}
				else if(lists.size()>0 && lists!=null)
				{
					ExpenseAttachmentAPICall();
					Intent intensw = new Intent(OrderConfirmActivity.this, StoreLocationActivity.class);		
					startActivity(intensw);	
				}
				else
				{
					new MyMessageDialog(OrderConfirmActivity.this,"ITEM","PLease select item before continue").show(getSupportFragmentManager(), "MyDialog");	
				}
			
			}

				break;      
			case R.id.imageView1:
				Intent inta = new Intent(OrderConfirmActivity.this, MenuActivity.class);		
				startActivity(inta);;
				break;


			default:
				break;
			}
		}
	};




	@Override
	protected void onResume() 
	{
		checkForCrashes();
		adapter.notifyDataSetChanged();
		super.onResume();
	}

	private void checkForCrashes() 
	{
		CrashManager.register(this, "9665c28bb83c477dbf1081723fab9379");
	}
}
