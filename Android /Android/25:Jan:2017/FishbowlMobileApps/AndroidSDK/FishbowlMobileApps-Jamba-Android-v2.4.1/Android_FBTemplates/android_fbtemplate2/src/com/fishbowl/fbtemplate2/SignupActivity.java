package com.fishbowl.fbtemplate2;


/**
 * Created by Digvijay Chauhan on 7/12/15.
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;


@SuppressLint("NewApi") public class SignupActivity extends ActionBarActivity implements
OnItemClickListener{
	Handler hand = new Handler();
	private DrawerLayout drawerLayout;	
	public static List<Menu_Item> dataList;
	private final int SPLASH_DISPLAY_LENGTH = 5000;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	ListView lv;
	public static final String[] titles = new String[] { "My Order",
			"Store Location", "Pizza Menu", "My Offer" };



	public static final Integer[] images = { R.drawable.menu_myorder,
			R.drawable.menu_storelocator, R.drawable.menu_shareables, R.drawable.menu_myoffers };

	ListView listView;
	List<DescriptionItem> rowItems;
	Button signup_btn;
	Button sign_btn;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

			getSupportActionBar().hide();
		sign_btn = (Button)findViewById(R.id.sign_btn);
		sign_btn.setTextColor(getResources().getColor(R.color.White));
		sign_btn.setBackgroundColor(getResources().getColor(R.color.background_top));

		signup_btn = (Button)findViewById(R.id.signup_btn);


		dataList = new ArrayList<Menu_Item>();
		dataList.add(new Menu_Item(true,"Sign in or Sign up","Enjoy the benefit of Zpizza.Save Favourite places an express order, and track your rewardal",R.drawable.ic_launcher)); // adding a spinner to the list

		//	dataList.add(new Menu_Item(true,"We're giving you Free Slice",null,0));

		dataList.add(new Menu_Item("Home", R.drawable.menu_home));
		dataList.add(new Menu_Item("Pizza Menu", R.drawable.menu_pizza_menu));
		dataList.add(new Menu_Item("Store Locator", R.drawable.menu_storelocator));
		dataList.add(new Menu_Item("My Order", R.drawable.menu_myorder));
		dataList.add(new Menu_Item("Order History", R.drawable.menu_orderhistory));
		dataList.add(new Menu_Item("My Offers", R.drawable.menu_myoffers));
		dataList.add(new Menu_Item("My Favourites", R.drawable.menu_myfavorites));
		dataList.add(new Menu_Item("Zpizza Rewards", R.drawable.menu_zpizzarewards));
		dataList.add(new Menu_Item("See Whats Treanding", R.drawable.menu_trending));
		dataList.add(new Menu_Item("Privacy Policy | Term of use",0,0)); 

		MenuAdapter adapter = new MenuAdapter(this, R.layout.list_menu,
				dataList);
		lv=(ListView)this.findViewById(R.id.left_drawer);

		lv.setAdapter(adapter);


		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		lv.setOnItemClickListener(new DrawerItemClickListener());
		/*	drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);*/


		actionBarDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				drawerLayout,         /* DrawerLayout object */
				R.drawable.sidebutton,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.drawer_close  /* "close drawer" description */
				);

		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		drawerLayout.setDrawerShadow(R.drawable.sidebutton, GravityCompat.START);

		rowItems = new ArrayList<DescriptionItem>();
		for (int i = 0; i < titles.length; i++) {
			DescriptionItem item = new DescriptionItem(images[i], titles[i]);
			rowItems.add(item);
		}

		listView = (ListView) findViewById(R.id.mobile_list);
		MenuListAdapter adapter1 = new MenuListAdapter(this,
				R.layout.list_item, rowItems);
		listView.setAdapter(adapter1);
		listView.setOnItemClickListener(this);




	}


/*	
	  @Override
	  public void onBackPressed() {
	      if (getFragmentManager().getBackStackEntryCount() == 0) {
	          this.finish();
	      } else {
	          getFragmentManager().popBackStack();
	      }
	  }*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {


		if(drawerLayout.isDrawerOpen(lv)){
			drawerLayout.closeDrawer(lv);
		}else {
			drawerLayout.openDrawer(lv);
		}    
		/*if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		 */
		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			navigateTo(position);
		}
	}


	public static final String TAG = MainActivity.class.getSimpleName();
	public void navigateTo(int position) {
		switch(position) {
		case 0:

			Intent newn=new Intent(this,SignupActivity.class);
			startActivity(newn);

		case 2:



			FragmentTransaction fta = getSupportFragmentManager().beginTransaction();
			//fta.hide(this);				
			fta.replace(R.id.main_container,new Description_Fragment() );			
			fta.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fta.addToBackStack(null);
			fta.commit();




			break;
		case 3:

		case 4:			 


		case 5:			 




			break;


		case 6:			 

		}
		drawerLayout.closeDrawer(lv);

	}


	public void signin(View v) {
		sign_btn.setTextColor(getResources().getColor(R.color.White));
		signup_btn.setTextColor(getResources().getColor(R.color.background_top));
		sign_btn.setBackgroundColor(getResources().getColor(R.color.Red));
		signup_btn.setBackgroundColor(getResources().getColor(R.color.invisible));
	}

	public void signup(View v) {
		sign_btn.setTextColor(getResources().getColor(R.color.Red));
		signup_btn.setTextColor(getResources().getColor(R.color.White));
		signup_btn.setBackgroundColor(getResources().getColor(R.color.background_top));
		sign_btn.setBackgroundColor(getResources().getColor(R.color.invisible));
	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	protected void onResume() {
		checkForCrashes();
		super.onResume();
	}
	/*
	@Override
	public boolean onOptionsItemSelected(Menu_Item item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/

	
	
	public void facebook(View v) {/*
		// does something very interesting

		Intent mainIntent = new Intent(this,MenuActivity.class);
		beforeCurrencyCall(0);
	
	//	if(jsonreturn!=null)
		Bundle b = new Bundle();
		b.putBoolean("facebookflag", true);
		mainIntent.putExtras(b);
	//	this.startActivity(mainIntent);

	*/}


	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch(position) {
		case 0:

			Intent newn=new Intent(this,Description_Fragment.class);
			startActivity(newn);

		case 2:



			FragmentTransaction fta = getSupportFragmentManager().beginTransaction();
			//fta.hide(this);				
			fta.replace(R.id.main_container,new Description_Fragment() );			
			fta.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fta.addToBackStack(null);
			fta.commit();




			break;
		case 3:

		case 4:			 


		case 5:			 




			break;


		case 6:			 

		}

	}
	
	private void checkForCrashes() {
		                                
		   CrashManager.register(this, "f59b804472cc487d97077cb81d203ed5");
		 }
	
	
	private void checkForUpdates() {
	    // Remove this for store / production builds!
	    UpdateManager.register(this, "f59b804472cc487d97077cb81d203ed5");
	  }

}
