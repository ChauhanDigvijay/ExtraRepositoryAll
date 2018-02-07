package com.fishbowl.fbtemplate1.activity;
import java.util.ArrayList;
import java.util.List;

import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.LocationAdapter;
import com.fishbowl.fbtemplate1.Adapter.MATabsPagerAdapter;
import com.fishbowl.fbtemplate1.Adapter.OrderConfirmDrawerAdapter;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.CoreActivity.MainActivity;
import com.fishbowl.fbtemplate1.Model.LocationItem;
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;
import com.fishbowl.fbtemplate1.Model.OrderConfirmDrawItem;
import com.fishbowl.fbtemplate1.Model.OrderItem;
import com.fishbowl.fbtemplate1.widget.SlidingTabLayout;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import net.hockeyapp.android.CrashManager;


@SuppressLint("NewApi") public class StoreLocationActivity extends ActionBarActivity 
{
	public static List<OrderConfirmDrawItem> mdataList;
	public ViewPager mPager;
	ActionBar mActionbar;
	private DrawerLayout drawerLayout;
	String url="http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/store/";
	ListView listView ;
	private SearchView mSearchView;
	List<LocationItem> lists;
	public static List<LocationItem> dataList;
	public static Boolean signiin=false;	
	LocationAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_location);
		signiin=false;
		Intent i=getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) 
		{

			signiin=extras.getBoolean("signin", false);


		}


		FishbowlTemplate1App	fishTApp=FishbowlTemplate1App.getInstance();
		fishTApp.setStoreActivity(this);
		
		mActionbar = getSupportActionBar();
		mdataList = new ArrayList<OrderConfirmDrawItem>();
		mdataList.add(new OrderConfirmDrawItem(true,"Sign in or Sign up","Enjoy the benefit of Zpizza.Save Favourite places an express order, and track your rewardal",R.drawable.ic_launcher)); // adding a spinner to the list
		mdataList.add(new OrderConfirmDrawItem("HOME", R.drawable.menu_home));
		mdataList.add(new OrderConfirmDrawItem("PIZZA MENU", R.drawable.menu_pizza_menu));
		mdataList.add(new OrderConfirmDrawItem("STORE LOCATOR", R.drawable.menu_storelocator));
		mdataList.add(new OrderConfirmDrawItem("MY ORDER", R.drawable.menu_myorder));
		mdataList.add(new OrderConfirmDrawItem("ORDER HISTORY", R.drawable.menu_orderhistory));
		mdataList.add(new OrderConfirmDrawItem("MY OFFERS", R.drawable.menu_myoffers));
		mdataList.add(new OrderConfirmDrawItem("MY FAVORITES", R.drawable.menu_myfavorites));
		mdataList.add(new OrderConfirmDrawItem("Privacy Policy | Term of use",0,0)); // adding a header to the list
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
				navigateTo(position);
			}
		});

		mPager = (ViewPager) findViewById(R.id.viewpager);
		mPager.setAdapter(new MATabsPagerAdapter(getSupportFragmentManager()));
		SlidingTabLayout    mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(mPager);

		mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) 
			{
				return 0;
			}
			@Override
			public int getDividerColor(int position) 
			{	
				return getResources().getColor(R.color.White);
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		getMenuInflater().inflate(R.menu.menu_location, menu);
		MenuItem itemmo = menu.findItem(R.id.menu_edit1);

		MenuItemCompat.setActionView(itemmo, R.layout.customstore);

		RelativeLayout offer = (RelativeLayout) MenuItemCompat.getActionView(itemmo);

		ImageView backclick=(ImageView) offer.findViewById(R.id.imageView1);

		backclick.setOnClickListener(first_radio_listener);

		//	skip = (TextView) offer.findViewById(R.id.title_textreg);

		//	skip.setOnClickListener(first_radio_listener);

		return super.onCreateOptionsMenu(menu);



	}
	OnClickListener first_radio_listener = new OnClickListener (){
		public void onClick(View v) {

			//Your Implementaions...
			switch (v.getId()) 
			{


			case R.id.title_textreg:
				Intent inten = new Intent(StoreLocationActivity.this, MenuActivity.class);		
				startActivity(inten);;
				break;

			case R.id.imageView1:
				Intent inta = new Intent(StoreLocationActivity.this, TestSignIn.class);		
				startActivity(inta);;
				break;



			default:
				break;
			}
		}
	};
	public void setActionBarTitle()
	{

		//mActionbar.setTitle("NEW USER REGISTRATION");
	}


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

		case 7:	Intent storeintent1 = new Intent(this, StoreLocationActivity.class);		
		startActivity(storeintent1);
		break;		 

		case 6:			 		

		}
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
	protected void onStart() 
	{
		setActionBar();
		super.onResume();
	}

	@Override
	protected void onResume() 
	{
		checkForCrashes();
		super.onResume();
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

	private void checkForCrashes() 
	{
		CrashManager.register(this, "9665c28bb83c477dbf1081723fab9379");
	}


}
