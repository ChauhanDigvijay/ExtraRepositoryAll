package com.fishbowl.fbtemplate1.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.MenuListAdapter;
import com.fishbowl.fbtemplate1.Adapter.OrderConfirmDrawerAdapter;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.CoreActivity.MainActivity;
import com.fishbowl.fbtemplate1.Model.DescriptionItem;
import com.fishbowl.fbtemplate1.Model.OrderConfirmDrawItem;
import com.fishbowl.fbtemplate1.util.StringUtilities;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.hockeyapp.android.CrashManager;


@SuppressLint("NewApi") public class TestSignIn extends Activity implements
OnItemClickListener
{
	
	private DrawerLayout drawerLayout;
	public static List<OrderConfirmDrawItem> mdataList;
	TextView skip;
	Button signup_btn;
	Button sign_btn;
	ActionBar mActionbar;
	List<DescriptionItem> rowItems;

	public static final String[] titles = new String[] { "Pizza Menu" ,
			"Store Location","My Order"};



	public static final Integer[] images = { R.drawable.menub,
			R.drawable.locaterb, R.drawable.orderb};

	ListView listView;

	// Your Facebook APP ID
	private static String APP_ID = "1661409320799739"; // Replace with your App ID

	// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	FishbowlTemplate1App fishTApp;
	// Buttons
	Button btnFbLogin;
	Button btnFbGetProfile;
	Button btnPostToWall;
	Button btnShowAccessTokens;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_signup);


		fishTApp=FishbowlTemplate1App.getInstance();
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);


		sign_btn = (Button)findViewById(R.id.sign_btn);
	//	sign_btn.setTextColor(getResources().getColor(R.color.White));
		//sign_btn.setBackgroundColor(getResources().getColor(R.color.background_top));
		signup_btn = (Button)findViewById(R.id.signup_btn);
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
	
		
		OrderConfirmDrawerAdapter adapter2 = new OrderConfirmDrawerAdapter(this, R.layout.list_sign,mdataList);

		ListView lv1=(ListView)this.findViewById(R.id.left_drawer);
		lv1.setAdapter(adapter2);
		drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);

		//setActionBar();

		lv1.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
			{
				navigateTo(position);
			}
		});
		
		//	mActionbar = getSupportActionBar();
		//	setActionBar();
	}


	
	
	public static final String TAG = MainActivity.class.getSimpleName();
	public void navigateTo(int position) {
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

	@SuppressLint("NewApi")
	protected void setActionBar() 
	{



		mActionbar.setDisplayShowHomeEnabled(true);
		mActionbar.setDisplayShowTitleEnabled(true);
		mActionbar.setHomeButtonEnabled(true);
		mActionbar.setDisplayHomeAsUpEnabled(true);
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



	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		getMenuInflater().inflate(R.menu.menu_registration, menu);
		MenuItem itemmo = menu.findItem(R.id.menu_edit1);

		MenuItemCompat.setActionView(itemmo, R.layout.customregistration);

		RelativeLayout offer = (RelativeLayout) MenuItemCompat.getActionView(itemmo);
		skip = (TextView) offer.findViewById(R.id.title_textreg);

		skip.setOnClickListener(first_radio_listener);

		return super.onCreateOptionsMenu(menu);



	}*/
	OnClickListener first_radio_listener = new OnClickListener (){
		public void onClick(View v) {

			//Your Implementaions...
			switch (v.getId()) 
			{


			case R.id.title_textreg:
				Intent inten = new Intent(TestSignIn.this, MenuActivity.class);		
				startActivity(inten);;
				break;


		

			default:
				break;
			}
		}
	};

	public void guest(View v) 
	{
		// does something very interesting

		Intent mainIntent = new Intent(this,MenuActivity.class);
		this.startActivity(mainIntent);

	}

	/*public void signin(View v) 
	{
		// does something very interesting



	}*/

	public void facebooklogin(View v) 
	{

		loginToFacebook();

	}

	public void register(View v) 
	{


	}


	public void loginToFacebook() {
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
					new String[] { "email", "publish_stream" },
					new DialogListener() {

				@Override
				public void onCancel() {
					// Function to handle cancel event
				}

				@Override
				public void onComplete(Bundle values) {
					// Function to handle complete event
					// Edit Preferences and update facebook acess_token
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token",
							facebook.getAccessToken());
					editor.putLong("access_expires",
							facebook.getAccessExpires());
					editor.commit();
				}

				@Override
				public void onError(DialogError error) {
					// Function to handle error

				}

				@Override
				public void onFacebookError(FacebookError fberror) {
					// Function to handle Facebook errors

				}

			});
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}


	/**
	 * Get Profile information by making request to Facebook Graph API
	 * */
	public void getProfileInformation()
	{
		mAsyncRunner.request("me", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					// Facebook Profile JSON data
					JSONObject profile = new JSONObject(json);

					// getting name of the user
					final String name = profile.getString("name");

					// getting email of the user
					final String email = profile.getString("email");

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email, Toast.LENGTH_LONG).show();
						}

					});


				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}





	/**
	 * Function to post to facebook wall
	 * */
	public void postToWall() {
		// post on user's wall.
		facebook.dialog(this, "feed", new DialogListener() {

			@Override
			public void onFacebookError(FacebookError e) {
			}

			@Override
			public void onError(DialogError e) {
			}

			@Override
			public void onComplete(Bundle values) {
			}

			@Override
			public void onCancel() {
			}
		});

	}

	/**
	 * Function to show Access Tokens
	 * */
	public void showAccessTokens() 
	{
		String access_token = facebook.getAccessToken();

		Toast.makeText(getApplicationContext(),
				"Access Token: " + access_token, Toast.LENGTH_LONG).show();
	}

	/**
	 * Function to Logout user from Facebook
	 * */
	public void logoutFromFacebook() {
		mAsyncRunner.logout(this, new RequestListener() 
		{
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Logout from Facebook", response);
				if (Boolean.parseBoolean(response) == true) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// make Login button visible
							btnFbLogin.setVisibility(View.VISIBLE);

							// making all remaining buttons invisible
							btnFbGetProfile.setVisibility(View.INVISIBLE);
							btnPostToWall.setVisibility(View.INVISIBLE);
							btnShowAccessTokens.setVisibility(View.INVISIBLE);
						}

					});

				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}




	public void signin(View v) {
		sign_btn.setTextColor(getResources().getColor(R.color.White));
		signup_btn.setTextColor(getResources().getColor(R.color.background_top));
		sign_btn.setBackgroundColor(getResources().getColor(R.color.fbred));
		signup_btn.setBackgroundColor(getResources().getColor(R.color.invisible));

		Intent mainIntent = new Intent(this,SignInActivity.class);
		this.startActivity(mainIntent);
	}

	public void signup(View v) {
		sign_btn.setTextColor(getResources().getColor(R.color.fbred));
		signup_btn.setTextColor(getResources().getColor(R.color.White));
		signup_btn.setBackgroundColor(getResources().getColor(R.color.background_top));
		sign_btn.setBackgroundColor(getResources().getColor(R.color.invisible));

		Intent mainIntent = new Intent(this,RegistrationActivity.class);
		this.startActivity(mainIntent);

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
		return super.onOptionsItemSelected(item);
	}


	private void checkForCrashes() 
	{
		CrashManager.register(this, "9665c28bb83c477dbf1081723fab9379");
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

		switch(position) {
		case 0:


			Intent newnsxx=new Intent(this, MenuActivity.class);
			startActivity(newnsxx);

			break;


		case 1:

			Intent newns=new Intent(this,StoreLocationActivity.class);
	
			Bundle extras = new Bundle();
			extras.putBoolean("signin",true);
			newns.putExtras(extras);
			startActivity(newns);

			break;
		case 2:
			if(fishTApp!=null)
			{
				if(fishTApp.getCountcart()>0)
				{
					Intent menucart = new Intent(this, OrderConfirmActivity.class);		
					startActivity(menucart);
				}
				else
				{
					Intent newn=new Intent(this,MenuActivity.class);
					
					startActivity(newn);
				}
			}
			else
			{
				Intent newn=new Intent(this,MenuActivity.class);
				startActivity(newn);
			}
			break;

		case 4:			 


		case 5:			 




			break;


		case 6:			 

		}



	}

}
