package com.fishbowl.fbtemplate1.activity;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.clp.sdk.CLPSdk;
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.OrderConfirmDrawerAdapter;
import com.fishbowl.fbtemplate1.Controller.FB_DBUser;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.CoreActivity.MainActivity;
import com.fishbowl.fbtemplate1.Model.OrderConfirmDrawItem;
import com.fishbowl.fbtemplate1.Model.UserItem;
import com.fishbowl.fbtemplate1.fragment.DetailMenu_Fragment;
import com.fishbowl.fbtemplate1.fragment.ImageMenu_Fragment.ListItemSelectedListener;
import com.fishbowl.fbtemplate1.widget.MyMessageDialog;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.hockeyapp.android.CrashManager;

@SuppressLint("NewApi") public class MenuActivity extends ActionBarActivity implements ListItemSelectedListener,AdapterView.OnItemClickListener, SearchView.OnQueryTextListener{

	public static ListItemSelectedListener itemListerner;


	ImageView v;
	public CLPSdk clpsdkObj;
	GoogleCloudMessaging gcm;
	private int appVersion;
	FishbowlTemplate1App fishTApp;
	TextView offertv,historytv,carttv;
	ImageView offeriv,historyiv,cartiv;

	ActionBar mActionbar;
	private DrawerLayout drawerLayout;	

	DetailMenu_Fragment detailsFragment;

	public static List<OrderConfirmDrawItem> dataList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		mActionbar = getSupportActionBar();

	
		fishTApp=FishbowlTemplate1App.getInstance();
		fishTApp.setMainActivity(this);
		detailsFragment = new DetailMenu_Fragment();
		Intent i=getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			DetailMenu_Fragment detailsFragment = new DetailMenu_Fragment();
			detailsFragment.onActivityResult(2, 2, i);

		}

		if(FB_DBUser.getInstance().getAllUsers().size()>0)
		{
			UserItem user	=FB_DBUser.getInstance().getAllUsers().get(0);
			fishTApp.setLoggedInUser(user);

		}

		if(fishTApp.getLoggedInUser()!=null&&(FB_DBUser.getInstance().getAllUsers().size()>0))
		{
			//sdk
			new Thread(){
				public void run()
				{ 

					String msg = "";
					try {
						if (gcm == null) 
						{
							gcm = GoogleCloudMessaging.getInstance(MenuActivity.this);
						}
						String regid = gcm.register("906308993060");		
						msg = "Device registered, registration ID=" + regid;
						final SharedPreferences prefs = MenuActivity.this.getSharedPreferences(MenuActivity.this.getLocalClassName(), Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putString("registration_id", regid);
						editor.putInt("appVersion", appVersion);
						editor.commit();

					} catch (Exception ex) 
					{
						msg = "Error :" + ex.getMessage();
					}

				}
			}.start();


			clpsdkObj = CLPSdk.sharedInstanceWithKey(this.getApplicationContext(),"password");
			clpsdkObj.startLocationService();

			try 
			{
				JSONObject data = new JSONObject();
				data.put("event_name", "Opened");
				data.put("event_time", clpsdkObj.formatedCurrentDate());
				clpsdkObj.updateAppEvent(data);
			} catch (JSONException e) 
			{

				e.printStackTrace();
			}
		}


		dataList = new ArrayList<OrderConfirmDrawItem>();

		if(FB_DBUser.getInstance().getAllUsers().size()>0)
		{

			dataList.add(new OrderConfirmDrawItem(true,"WELCOME",FB_DBUser.getInstance().getAllUsers().get(0).getFullname(),R.drawable.ic_launcher)); // adding a spinner to the list
		}
		else
		{
			dataList.add(new OrderConfirmDrawItem(true,"Sign in or Sign up","Enjoy the benefit of Zpizza.Save Favourite places an express order, and track your rewardal",R.drawable.ic_launcher)); // adding a spinner to the list
		}
		dataList.add(new OrderConfirmDrawItem("HOME", R.drawable.menu_home));
		dataList.add(new OrderConfirmDrawItem("PIZZA MENU", R.drawable.menu_pizza_menu));
		dataList.add(new OrderConfirmDrawItem("STORE LOCATOR", R.drawable.menu_storelocator));
		dataList.add(new OrderConfirmDrawItem("MY ORDER", R.drawable.menu_myorder));
		dataList.add(new OrderConfirmDrawItem("ORDER HISTORY", R.drawable.menu_orderhistory));
		dataList.add(new OrderConfirmDrawItem("MY OFFERS", R.drawable.menu_myoffers));
		dataList.add(new OrderConfirmDrawItem("MY FAVORITES", R.drawable.menu_myfavorites));
		dataList.add(new OrderConfirmDrawItem("Privacy Policy | Term of use",0,0)); // adding a header to the list
		OrderConfirmDrawerAdapter adapter = new OrderConfirmDrawerAdapter(this, R.layout.list_sign,
				dataList);

		ListView lv=(ListView)this.findViewById(R.id.left_drawer);

		lv.setAdapter(adapter);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		lv.setOnItemClickListener(new OnItemClickListener() 
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
		getMenuInflater().inflate(R.menu.mainmenu, menu);

		MenuItem itemmo = menu.findItem(R.id.menu_offer);


		//MenuItem searchItem = menu.findItem(R.id.contacts_menu_search_btn);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(itemmo);


		int searchImgId = android.support.v7.appcompat.R.id.search_button;
		//int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
		v = (ImageView) searchView.findViewById(searchImgId);
		/*  int width = 60;
	    int height = 60;
	    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
	    iv.setLayoutParams(parms);*/
		v.setImageResource(R.drawable.searchwhite);

		/*	//	    SearchManager searchManager = (SearchManager) thisActivity.getSystemService(Context.SEARCH_SERVICE);
		//        if (searchManager != null) {
		//            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
		//
		//            SearchableInfo info = searchManager.getSearchableInfo(thisActivity.getComponentName());
		//            for (SearchableInfo inf : searchables) {
		//                if (inf.getSuggestAuthority() != null
		//                        && inf.getSuggestAuthority().startsWith("applications")) {
		//                    info = inf;
		//                }
		//            }
		//            searchView.setSearchableInfo(info);
		//        }
		 */		if(searchView!=null){
			 searchView.setOnQueryTextListener(this);
			 
			 
			 
		 }


		 /*		MenuItem itemmoo = menu.findItem(R.id.contacts_menu_new_group);
		MenuItemCompat.setActionView(itemmoo, R.layout.custom_menu);



		RelativeLayout offer = (RelativeLayout) MenuItemCompat.getActionView(itemmoo);


		offertv = (TextView) offer.findViewById(R.id.actionbar_offer_textview);
		offeriv = (ImageView) offer.findViewById(R.id.actionbar_offer_imageview);	

		historytv = (TextView) offer.findViewById(R.id.actionbar_order_textview);
		historyiv = (ImageView) offer.findViewById(R.id.actionbar_order_imageview);	


		carttv = (TextView) offer.findViewById(R.id.actionbar_cart_textview);
		cartiv = (ImageView) offer.findViewById(R.id.actionbar_cart_imageview);	


		offeriv.setOnClickListener(first_radio_listener);
		historyiv.setOnClickListener(first_radio_listener);
		cartiv.setOnClickListener(first_radio_listener);


		String histcount=String.valueOf(fishTApp.getCountorderhistory());
		historytv.setText(histcount);

		offertv.setText(String.valueOf(fishTApp.getCountoffer()));


		carttv.setText(String.valueOf(getCountcart()));*/
		 return super.onCreateOptionsMenu(menu);
	}


	OnClickListener first_radio_listener = new OnClickListener (){
		public void onClick(View v) {

			//Your Implementaions...
			switch (v.getId()) 
			{


			case R.id.actionbar_offer_imageview:
				offeriv.setImageResource(R.drawable.giftr);
				Intent menuoffer = new Intent(MenuActivity.this, OfferActivity.class);		
				startActivity(menuoffer);
				break;

			case R.id.actionbar_order_imageview:
				historyiv.setImageResource(R.drawable.dater);
				Intent menuorder = new Intent(MenuActivity.this, MenuHistoryActivity.class);		
				startActivity(menuorder);
				break;      




			case R.id.actionbar_cart_imageview:
				cartiv.setImageResource(R.drawable.cartr);
				Intent menucart = new Intent(MenuActivity.this, OrderConfirmActivity.class);		
				startActivity(menucart);
				break;


			default:
				break;
			}
		}
	};


	public static final String TAG = MainActivity.class.getSimpleName();
	public void navigateTo(int position) {
		switch(position) {
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

		case 6:
			Intent menuoffer = new Intent(this, OfferActivity.class);		
			startActivity(menuoffer);
			break;

		case 7:	Intent storeintent1 = new Intent(this, StoreLocationActivity.class);		
		startActivity(storeintent1);
		break;		 


		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{

		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;


		case R.id.menu_offer:
			v.setImageResource(R.drawable.searchwhite);


		case R.id.contacts_menu_new_group:
			if(fishTApp.getCountcart()>0)
			{
				Intent menucart = new Intent(MenuActivity.this, OrderConfirmActivity.class);		
				startActivity(menucart);
			}

			else
			{
				new MyMessageDialog(MenuActivity.this,"ITEM","PLease select item..").show(getSupportFragmentManager(), "MyDialog");

			}
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("NewApi")
	protected void setActionBar()
	{

		setActionBarTitle();
	//	mActionbar.setHomeButtonEnabled(f);
	//	mActionbar.setDisplayHomeAsUpEnabled();
	}

	public void setActionBarTitle()
	{
		mActionbar.setTitle("MENU");
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

	@Override
	protected void onResume() 
	{
		checkForCrashes();
		super.onResume();
	}

	@Override
	public void listItemSelectedListener(int pos)
	{

		DetailMenu_Fragment detailsFragment = new DetailMenu_Fragment();
		detailsFragment.getItemValue(pos,this);

	}

	private void checkForCrashes() 
	{
		CrashManager.register(this, "9665c28bb83c477dbf1081723fab9379");
	}




	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub

		if(detailsFragment.adapter != null){
			Filter fl = detailsFragment.adapter .getFilter();
			if(fl != null){
				fl.filter(arg0);
			}
		}
		return false;
	}

	public static ListItemSelectedListener getItemListerner() {
		return itemListerner;
	}



	public static void setItemListerner(ListItemSelectedListener itemListerner) {
		MenuActivity.itemListerner = itemListerner;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

	}
}