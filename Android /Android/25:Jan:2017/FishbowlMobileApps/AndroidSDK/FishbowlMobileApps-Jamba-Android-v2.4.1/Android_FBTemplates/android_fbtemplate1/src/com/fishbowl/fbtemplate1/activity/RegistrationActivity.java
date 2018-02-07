package com.fishbowl.fbtemplate1.activity;
/**
 **
 * Created by Digvijay Chauhan on 25/12/15.
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Controller.FB_DBUser;
import com.fishbowl.fbtemplate1.Model.LocationItem;
import com.fishbowl.fbtemplate1.Model.UserItem;
import com.fishbowl.fbtemplate1.util.StringUtilities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import net.hockeyapp.android.CrashManager;


@SuppressLint("NewApi") public class RegistrationActivity extends ActionBarActivity 
{

	EditText rname;
	EditText remail;
	EditText rpass;
	EditText rphone;
	EditText	rlocation;
	Dialog diaSaveLocation;
	int currentSelectedDialogRow;
	//	EditText email;

	String sDiaCancel ="Cancel";

	String sDiaClear="Clear";

	String sDiaOk="OK";
	ArrayAdapter<String> dataAdapter ;
	CharSequence[] items;
	ArrayList<String> dataArrayList = new ArrayList<String>();
	String url="http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/store/";
	ActionBar mActionbar;
	private SearchView mSearchView;

	public static List<LocationItem> dataList;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		mActionbar = getSupportActionBar();
		rname	= ((EditText)findViewById(R.id.name_edtxt));
		remail	= ((EditText)findViewById(R.id.email_edtxt));
		rpass	= ((EditText)findViewById(R.id.pass_edtxt));
		rphone	= ((EditText)findViewById(R.id.phone_edtxt));
		setActionBar();
	}



	@Override
	public void onBackPressed() {



		if (getFragmentManager().getBackStackEntryCount() == 0) 
		{
			moveTaskToBack(true);
			this.finish();
		} 
		else
		{

			getFragmentManager().popBackStack();
		}
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

		getMenuInflater().inflate(R.menu.menu_registration1, menu);
		MenuItem itemmo = menu.findItem(R.id.menu_edit1);

		MenuItemCompat.setActionView(itemmo, R.layout.customregist);

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
				Intent inten = new Intent(RegistrationActivity.this, MenuActivity.class);		
				startActivity(inten);;
				break;

			case R.id.imageView1:
				Intent inta = new Intent(RegistrationActivity.this, TestSignIn.class);		
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

	public void signup(View v) 
	{

		String name =rname.getText().toString();
		String email =remail.getText().toString();
		String phone=rphone.getText().toString();
		String pass=rpass.getText().toString();


		if((!StringUtilities.isValidEmail(email))||(!StringUtilities.isValidPhoneNumber(phone)))
		{

			if(!StringUtilities.isValidString(name))
			{
				rname.setError("Name is required!");			
			}
			if(!StringUtilities.isValidEmail(email))
			{
				remail.setError("Email is required!");			
			}
			if(!StringUtilities.isValidPhoneNumber(phone))
			{
				rphone.setError("Phone is required!");			
			}
			if(!StringUtilities.isValidString(pass))
			{
				rpass.setError("Password is required!");		
			}
		}
		else
		{
			UserItem user=new UserItem();

			//digvijay Chauhan modify it to get First name
			if(StringUtilities.isValidString(name))
			{

				user.setFullname(name);
				try{
					if(name.indexOf(" ") > 0){
						user.setFirstname(name.split(" ")[0]);
						if(name.split(" ").length >1){
							user.setLastname(name.split(" ")[1]);
						}

					}
					else
					{
						if(StringUtilities.isValidString(user.getFullname())){
							user.setFirstname(user.getFullname());
							user.setLastname("");
						}	
					}
				}
				catch(Exception e)
				{
					user.setFullname(name);
					e.printStackTrace();

				}
				user.setEmail(email);
				user.setMobile(phone);
				user.setPassword(pass);
				user.setConfirmed(true);
				FB_DBUser.getInstance().createUpdateUser(user);

				Intent mainIntent = new Intent(this,MenuActivity.class);
				this.startActivity(mainIntent);

			}
		}
	}

	public void loadNewItemsCount(String url) 
	{
		try
		{

			RequestQueue queue = Volley.newRequestQueue(this);

			JSONObject userJSON = new JSONObject();

			//			userJSON.put("UserItem-mobile", ((OyeApp)appContext).getCurrentUser().getMobile());
			JSONObject requestObj = new JSONObject();
			requestObj.put("data", userJSON);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.GET,url, requestObj,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {

							System.out.println("updatefield response: " );

							try{

								if(response.has("categories")) {

									JSONArray	jsonArray	 = response.getJSONArray("categories");

									if(jsonArray != null && jsonArray.length() > 0)
									{
										dataList = new ArrayList<LocationItem>();


										for( int i=0; i<jsonArray.length(); i++)
										{
											JSONObject	wallObj = jsonArray.getJSONObject(i);
											if(wallObj != null)
											{
												LocationItem lt=new LocationItem();

												lt.setStoreID((wallObj.getString("storeID")));
												lt.setName(wallObj.getString("name"));
												lt.setAddress(wallObj.getString("address"));
												lt.setState((wallObj.getString("state")));
												lt.setCity(wallObj.getString("city"));
												lt.setZipcode(wallObj.getString("zipcode"));
												lt.setPhone(wallObj.getString("phone"));
												dataList.add(lt);
												///	FB_DBLocation.getInstance().createUpdateLocation(lt);
											}
											if(dataList!=null)
											{
												{
													for(LocationItem locationitem:dataList)
														dataArrayList.add(locationitem.getAddress()+locationitem.getState());
												}

												dataAdapter = new ArrayAdapter<String>(RegistrationActivity.this,android.R.layout.simple_spinner_item,getData());
												dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
												create_expenseclaim_dialog();
												//	new GeocoderTask().execute(dataList);
											}
										}

									}
									else
									{
										//listView.clearChoices();
									}



								}

							}

							catch(Exception ex){
								ex.printStackTrace();

							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							if(error != null){

								error.printStackTrace(System.out);

							}
						}
					});
			queue.add(jsObjRequest);
		} 
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

	}
	public ArrayList<String> getData() {
		return dataArrayList;
	}

	public void setData(ArrayList<String> dataArrayList) {
		this.dataArrayList = dataArrayList;
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
		switch (item.getItemId()) {
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

	@Override
	public void onStart() 
	{
		super.onStart();
		setActionBar();

	}

	private void create_expenseclaim_dialog() 
	{

		items = dataArrayList.toArray(new CharSequence[dataArrayList.size()]);
		if( diaSaveLocation != null && diaSaveLocation.isShowing() ) 
			return;
		diaSaveLocation = new AlertDialog.Builder(this).setTitle("Choose expenseClaimItem")
				.setSingleChoiceItems(items, currentSelectedDialogRow,clickDialog)
				.setPositiveButton(sDiaOk, clickSaveOk)
				.setNegativeButton(sDiaCancel, clickCancel).create();

		diaSaveLocation.show();

	}

	final DialogInterface.OnClickListener clickDialog = new DialogInterface.OnClickListener() 
	{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			currentSelectedDialogRow = which;
		}
	};

	final DialogInterface.OnClickListener clickSaveOk = new DialogInterface.OnClickListener() 
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{

			rlocation.setText(items[currentSelectedDialogRow]);
			dialog.dismiss();

		}

	};
	final DialogInterface.OnClickListener clickCancel = new DialogInterface.OnClickListener() 
	{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	};



}
