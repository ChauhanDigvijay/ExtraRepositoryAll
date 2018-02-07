//dj initial
package com.fishbowl.fbtemplate1.Controller;
/**
 **
 * Created by Digvijay Chauhan on 14/12/15.
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
import com.clp.model.CLPCustomer;
import com.clp.sdk.CLPSdk;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.DBHelper.FishbowlDbHelper;
import com.fishbowl.fbtemplate1.Model.UserItem;
import com.fishbowl.fbtemplate1.activity.MenuActivity;
import com.fishbowl.fbtemplate1.util.StringUtilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



public class FB_DBUser {

	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";

	private static FB_DBUser dbuser;
	private static Context appContext;
	// Database fields
	private SQLiteDatabase database;
	private FishbowlDbHelper dbHelper;
	private String[] allColumns = { FishbowlDbHelper.COLUMN_ID, FishbowlDbHelper.COLUMN_FB_FIRSTNAME,
			FishbowlDbHelper.COLUMN_FB_LASTNAME, FishbowlDbHelper.COLUMN_FB_MOBILE_NO,
			FishbowlDbHelper.COLUMN_FB_FULLNAME, FishbowlDbHelper.COLUMN_FB_COUNTRY, FishbowlDbHelper.COLUMN_FB_USERPASSWORD,
			FishbowlDbHelper.COLUMN_FB_EMAIL,FishbowlDbHelper.COLUMN_FB_USERPUSHTOKEN,
			FishbowlDbHelper.COLUMN_FB_USERID, FishbowlDbHelper.COLUMN_FB_USERISCONFIRMED, 
	};
	private final List<String> allColumnsList = Arrays.asList(allColumns);

	public static Context getAppContext() {
		return appContext;
	}

	public static void setAppContext(Context appContext) {
		FB_DBUser.appContext = appContext;
	}

	private FB_DBUser() {
		dbHelper = FishbowlDbHelper.getInstance(appContext);
		this.open();
	}

	public static FB_DBUser getInstance() {
		if(appContext == null){
			return null;
		}
		if (dbuser == null) {
			dbuser = new FB_DBUser();
		}
		return dbuser;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public UserItem createUpdateUser(UserItem user) {
		return createUpdateUser(user, true);
	}

	public UserItem createUpdateUser(UserItem user, boolean syncUserOnline) {
		ContentValues values = new ContentValues();
		values.put(FishbowlDbHelper.COLUMN_FB_FIRSTNAME, user.getFirstname());
		values.put(FishbowlDbHelper.COLUMN_FB_LASTNAME, user.getLastname());
		values.put(FishbowlDbHelper.COLUMN_FB_MOBILE_NO, user.getMobile());
		values.put(FishbowlDbHelper.COLUMN_FB_FULLNAME, user.getFullname());
		values.put(FishbowlDbHelper.COLUMN_FB_COUNTRY, user.getCountry());
		values.put(FishbowlDbHelper.COLUMN_FB_EMAIL, user.getEmail());
		values.put(FishbowlDbHelper.COLUMN_FB_USERPUSHTOKEN, user.getPushToken());
		values.put(FishbowlDbHelper.COLUMN_FB_USERID, user.getUserID());
		values.put(FishbowlDbHelper.COLUMN_FB_USERPASSWORD, user.getPassword());
		values.put(FishbowlDbHelper.COLUMN_FB_USERISCONFIRMED, user.isConfirmed() ? 1 : 0);

		// values.put(UserDBHelper.COLUMN_ID, user.getId());
		if (user.getId() > 0) {
			database.update(FishbowlDbHelper.FB_USERTABLE, values,
					FishbowlDbHelper.COLUMN_ID + "=" + user.getId(), null);
		} else {
			long insertId = database.insert(FishbowlDbHelper.FB_USERTABLE, null,
					values);
			user.setId(insertId);
		}
		if(syncUserOnline){
			// sync user to online DB
			//createUpdateUserOnline(user);
		}
		return user;
	}
	public void deleteUser(UserItem user) {
		long id = user.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(FishbowlDbHelper.FB_USERTABLE, FishbowlDbHelper.COLUMN_ID + " = "
				+ id, null);
	}

	public List<UserItem> getAllUsers() {
		List<UserItem> users = new ArrayList<UserItem>();

		Cursor cursor = database.query(FishbowlDbHelper.FB_USERTABLE, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserItem user = cursorToUser(cursor);
			users.add(user);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return users;
	}

	public UserItem cursorToUser(Cursor cursor) {
		UserItem user = new UserItem();
		user.setId(cursor.getLong(allColumnsList.indexOf(FishbowlDbHelper.COLUMN_ID)));
		user.setFirstname(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_FIRSTNAME)));
		user.setLastname(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_LASTNAME)));
		user.setMobile(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_MOBILE_NO)));
		user.setFullname(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_FULLNAME)));
		user.setCountry(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_COUNTRY)));
		user.setEmail(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_EMAIL)));	
		user.setPushToken(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERPUSHTOKEN)));
		user.setUserID(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERID)));
		user.setPassword(cursor.getString(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERPASSWORD)));		
		user.setConfirmed(cursor.getInt(allColumnsList
				.indexOf(FishbowlDbHelper.COLUMN_FB_USERISCONFIRMED)) == 1 ? true : false);
		return user;
	}


	// // Get GCM preferences
	public SharedPreferences getGCMPreferences(Context context,
			String simpleName) {

		return context.getSharedPreferences(simpleName, Context.MODE_PRIVATE);

	}


	//dj note
	// Get Application Version
	public int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	// Get Registration / Token ID
	private String getRegistrationId(Context context, String simpleName)

	{
		FishbowlTemplate1App fishTApp=FishbowlTemplate1App.getInstance();

		MenuActivity mainac=fishTApp.getMainActivity();

		final SharedPreferences prefs = getGCMPreferences(context, simpleName);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		/*Log.i("RegisterID", registrationId);

		if (StringUtilities.isValidString(registrationId)) {
			Log.i("Error: getRegistrationId", "Registration not found.");

			return "";
		}*/
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(mainac.getApplicationContext());
		/*if (registeredVersion != currentVersion) {
			Log.i("Error: getRegistration", "App version changed.");
			return "";
		}*/
		return registrationId;

	}


	public void CreateCustomerWithCompany(UserItem user)
	{
		FishbowlTemplate1App fishTApp=FishbowlTemplate1App.getInstance();

		MenuActivity mainac=fishTApp.getMainActivity();


		String url = "http://zpizza.clpcloud.com/clpapi/";
		CLPSdk.SERVER_URL = url;

		CLPCustomer customer = new CLPCustomer();
		String regid=this.getRegistrationId(mainac,mainac.getLocalClassName());
		customer.companyId = Integer.parseInt("8");
		customer.firstName = user.getFullname();
		if(!StringUtilities.isValidString(user.getFirstname())){
			customer.firstName = user.getFullname();
		}else{
			customer.firstName = user.getFirstname();
		}
		if(!StringUtilities.isValidString(user.getLastname())){
			customer.lastName =  user.getFullname();
		}else{
			customer.lastName = user.getLastname();
		}
		customer.emailID = user.getEmail();
		customer.loginID = user.getEmail();
		customer.loginPassword = "password";
		customer.loyalityNo = user.getMobile();
		customer.loyalityLevel = "5";
		customer.homePhone = "185524";
		customer.cellPhone = user.getMobile();
		customer.additionalPhone = "5558123";
		customer.addressLine1 = "RS Lane";
		customer.addressLine2 = "Swan Circle";
		customer.addressCity = "Texas";
		customer.addressState = "NY";
		customer.addressZip = "1154214";
		customer.addressCountry = "States";
		customer.customerTenantID = "44";
		customer.customerStatus = "Active";
		customer.lastActivtiy = "Purch";
		customer.lastActivityTime = "2015-1-1 11:11:11";
		customer.lastLoginTime = "2015-1-1 11:11:11";
		customer.statusCode = 1;
		customer.registeredDate = "2015-1-1 11:11:11";
		customer.registeredIP = "192.168.5.5";
		customer.invitationDate = "2015-1-1 11:11:11";
		customer.customerGender = "Male";
		customer.dateOfBirth = "2015-1-1 11:11:11";
		customer.customerAge = "25";
		customer.homeStore = "54";
		customer.favoriteDepartment = "Beverages";
		customer.pushOpted = "Y";
		customer.smsOpted = "Y";
		customer.emailOpted = "Y";
		customer.phoneOpted = "Y";
		customer.adOpted = "Y";
		customer.loyalityRewards = "Y";
		customer.createdBy = "FISHBOWL";
		customer.createdDate = "2015-1-1 11:11:11";
		customer.modifiedBy = "FISHBOWL";
		customer.modifedDate = "2015-1-1 11:11:11";
		//	customer.customerDeviceID = user.ge;
		customer.deviceID = regid;
		customer.pushToken = regid;
		customer.deviceType = "Phone";
		customer.deviceOsVersion = "4588";
		customer.deviceVendor = "VODA";
		customer.modifiedDate = "2015-1-1l11:11:11";
		customer.enabledFlag = "Y";
		try {
			mainac.clpsdkObj.saveCustomer(customer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public UserItem createUpdateUserOnline(UserItem user){
		try{
			JSONObject userJSON = new JSONObject();
			userJSON.put("UserItem-"+FishbowlDbHelper.COLUMN_FB_USERID, user.getUserID());
			userJSON.put("UserItem-"+FishbowlDbHelper.COLUMN_FB_FULLNAME, user.getUserID());
			userJSON.put("UserItem-"+FishbowlDbHelper.COLUMN_FB_FIRSTNAME, user.getFirstname());
			userJSON.put("UserItem-"+FishbowlDbHelper.COLUMN_FB_LASTNAME, user.getLastname());
			userJSON.put("UserItem-"+FishbowlDbHelper.COLUMN_FB_MOBILE_NO, user.getMobile());
			userJSON.put("UserItem-"+FishbowlDbHelper.COLUMN_FB_EMAIL, user.getEmail());
			userJSON.put("UserItem-"+FishbowlDbHelper.COLUMN_FB_COUNTRY, user.getCountry());			
			userJSON.put("UserItem-id", 0);

			JSONObject requestObj = new JSONObject();
			requestObj.put("data", userJSON);
			RequestQueue queue = Volley.newRequestQueue(appContext);
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.POST,"", requestObj,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try{

							} catch(Exception ex){

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
		return user;
	}
}
