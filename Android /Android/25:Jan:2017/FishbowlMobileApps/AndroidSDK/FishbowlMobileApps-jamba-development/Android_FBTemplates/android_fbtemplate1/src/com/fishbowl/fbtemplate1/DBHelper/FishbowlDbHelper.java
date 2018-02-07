//dj init
package com.fishbowl.fbtemplate1.DBHelper;


/**
 **
 * Created by Digvijay Chauhan on 14/12/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FishbowlDbHelper extends SQLiteOpenHelper 
{


	private static final String DATABASE_NAME = "FishbowlTemplate1.db";
	public static final String FB_USERTABLE = "user";
	public static final String FB_ORDERCONFIRM = "orderconfirm";
	public static final String FB_ORDER = "ordertable";
	public static final String FB_STORELOCATION = "storelocation";
	public static final String FB_OFFER = "offer";
	public static final String FB_USERADDRESS = "useraddress";


	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FB_FIRSTNAME = "firstname";
	public static final String COLUMN_FB_LASTNAME = "lastname";
	public static final String COLUMN_FB_MOBILE_NO = "mobile";
	public static final String COLUMN_FB_FULLNAME = "fullname";
	public static final String COLUMN_FB_EMAIL = "email";
	public static final String COLUMN_FB_COUNTRY = "country";
	public static final String COLUMN_FB_USERID = "userID"; 
	public static final String COLUMN_FB_USERPASSWORD = "password"; 
	public static final String COLUMN_FB_STATUS = "status";
	public static final String COLUMN_FB_DEVICETYPE = "devicetype";
	public static final String COLUMN_FB_DEVICENAME = "devicename";
	public static final String COLUMN_FB_USERISCONFIRMED = "isConfirmed";
	public static final String COLUMN_FB_USERPUSHTOKEN = "pushtoken";



	public static final String COLUMN_FB_USERADDRESS_TYPE = "useraddressType";
	public static final String COLUMN_FB_USERADDRESS_STREET = "userStreet";
	public static final String COLUMN_FB_USERADDRESS_TOWNCITY= "usertownCity"; 
	public static final String COLUMN_FB_USERADDRESS_STATEREGION = "userstateRegion"; 
	public static final String COLUMN_FB_USERADDRESS_POSTZIP = "userPostzip";
	public static final String COLUMN_FB_USERADDRESS_COUNTRY = "userCountry";
	public static final String COLUMN_FB_USERADDRESS_PREFRERRED = "userPreferred";
	public static final String COLUMN_FB_USERADDRESS_GEOLOCATION = "userGeoloc";
	public static final String COLUMN_FB_USERADDRESS_LOCATIONID = "userLocationID";



	public static final String COLUMN_FB_ITEM_ID = "Itemid";  
	public static final String COLUMN_FB_ITEM_NAME = "Itemname";
	public static final String COLUMN_FB_ITEM_PRICE = "Itemprice";
	public static final String COLUMN_FB_ITEM_QUANTITY = "Itemquantity";	
	public static final String COLUMN_FB_ITEM_TOTALPRICE = "Itemtotalprice";
	public static final String COLUMN_FB_ITEM_DESCRIPTION = "Itemdescription";
	public static final String COLUMN_FB_ITEM_IMAGEURL = "Itemimageurl";
	public static final String COLUMN_FB_ITEM_GRADIENT = "Itemgradient";
	

	public static final String COLUMN_FB_ORDERID = "OrderId";  
	public static final String COLUMN_FB_ORDERSTATUS = "Orderstatus";
	public static final String COLUMN_FB_ORDERMESSAGE = "Ordertotalmessage";
	public static final String COLUMN_FB_ORDERTOTALPRICE = "Ordertotalprice";	
	public static final String COLUMN_FB_ORDERDATE= "OrderDate";
	public static final String COLUMN_FB_ORDERTIME= "OrderTime";
	public static final String COLUMN_FB_ORDERTYPE= "OrderType";


	public static final String COLUMN_FB_STOREID = "StoreId";  
	public static final String COLUMN_FB_STORENAME = "StoreName";
	public static final String COLUMN_FB_STOREADDRESS = "StoreAddress";
	public static final String COLUMN_FB_STORECITY = "StoreCity";		
	public static final String COLUMN_FB_STORESTATE = "StoreState";  
	public static final String COLUMN_FB_STOREZIPCODE = "StoreZipcode";
	public static final String COLUMN_FB_STOREPHONENO = "StorePhoneno";


	public static final String COLUMN_FB_OFFERID = "OfferId";  
	public static final String COLUMN_FB_OFFERINAME = "OfferName";
	public static final String COLUMN_FB_OFFERIURL = "OrderUrl";
	public static final String COLUMN_FB_OFFERIITEM = "OrderItem";	
	public static final String COLUMN_FB_OFFERIOTHER= "OrderOther";


	private static final int DATABASE_VERSION = 1;


	// Database creation sql statement
	private static final String CREATE_FB_USERTABLE = "create table "
			+ FB_USERTABLE + "(" + COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_FB_FULLNAME  + " text , " + 	
			COLUMN_FB_FIRSTNAME  + " text , " + 
			COLUMN_FB_LASTNAME  + " text , " + 
			COLUMN_FB_USERPASSWORD  + " text , " + 
			COLUMN_FB_MOBILE_NO  + " text not null, " + 
			COLUMN_FB_COUNTRY  + " text , " + 
			COLUMN_FB_EMAIL  + " text , " + 
			COLUMN_FB_USERPUSHTOKEN + " text ," +
			COLUMN_FB_USERISCONFIRMED + " integer,"+
			COLUMN_FB_USERID  + " text);";



	// Database creation sql statement
	private static final String CREATE_FB_ORDERCONFIRM = "create table "
			+ FB_ORDERCONFIRM + "(" + COLUMN_ID + " integer primary key autoincrement, " + 		
			COLUMN_FB_ORDERID  + " integer , " + 	
			COLUMN_FB_USERID  + " text, " + 
			COLUMN_FB_ITEM_ID  + " integer , " + 
			COLUMN_FB_ITEM_NAME  + " text , " + 
			COLUMN_FB_ITEM_DESCRIPTION  + " text , " + 
			COLUMN_FB_ITEM_IMAGEURL  + " text , " + 
			COLUMN_FB_ITEM_GRADIENT  + " text , " + 
			COLUMN_FB_ITEM_PRICE  + " double , " + 
			COLUMN_FB_ITEM_QUANTITY  + " integer , " + 			
			COLUMN_FB_ITEM_TOTALPRICE  + " double);";




	private static final String CREATE_FB_USERADDRESS = "create table "
			+ FB_USERADDRESS + "(" + COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_FB_USERID  + " text , " + 	
			COLUMN_FB_USERADDRESS_TYPE  + " text , " + 
			COLUMN_FB_USERADDRESS_STREET  + " text , " + 
			COLUMN_FB_USERADDRESS_TOWNCITY  + " text, " + 
			COLUMN_FB_USERADDRESS_STATEREGION  + " text , " + 
			COLUMN_FB_USERADDRESS_POSTZIP  + " text , " + 	
			COLUMN_FB_USERADDRESS_COUNTRY  + " text, " + 
			COLUMN_FB_USERADDRESS_PREFRERRED  + " integer , " + 
			COLUMN_FB_USERADDRESS_GEOLOCATION  + " double , " + 	
			COLUMN_FB_USERADDRESS_LOCATIONID  + " text);";

	/*
	// Database creation sql statement
	private static final String CREATE_FB_ORDER = "create table "
			+ FB_ORDER + "(" + COLUMN_ID + " integer primary key autoincrement, " + 						
			COLUMN_FB_ORDERID  + " integer , " + 
			COLUMN_FB_ORDERSTATUS  + " text , " + 
			COLUMN_FB_ORDERMESSAGE  + " text , " + 
			COLUMN_FB_ORDERTOTALPRICE  + " double);";*/



	private static final String CREATE_FB_ORDER = "create table "
			+ FB_ORDER + "(" + COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_FB_ORDERID  + " integer , " + 
			COLUMN_FB_STOREID  + " text , " + 
			COLUMN_FB_ORDERSTATUS  + " text , " + 
			COLUMN_FB_ORDERMESSAGE  + " text , " + 
			COLUMN_FB_ORDERTYPE  + " text , " + 
			COLUMN_FB_ORDERDATE  + " long , " + 
			COLUMN_FB_ORDERTIME  + " long , " + 
			COLUMN_FB_ORDERTOTALPRICE  + " double);";




	private static final String CREATE_FB_STORETABLE = "create table "
			+ FB_STORELOCATION + "(" + COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_FB_STOREID  + " text , " + 	
			COLUMN_FB_STORENAME  + " text , " + 
			COLUMN_FB_STOREADDRESS  + " text , " + 
			COLUMN_FB_STORECITY  + " text, " + 
			COLUMN_FB_STORESTATE  + " text , " + 
			COLUMN_FB_STOREZIPCODE  + " text , " + 			
			COLUMN_FB_STOREPHONENO  + " text);";






	private static final String CREATE_FB_OFFERTABLE = "create table "
			+ FB_OFFER + "(" + COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_FB_OFFERID  + " text , " + 	
			COLUMN_FB_OFFERINAME  + " text , " + 
			COLUMN_FB_OFFERIURL  + " text , " + 
			COLUMN_FB_OFFERIITEM  + " text, " + 
			COLUMN_FB_OFFERIOTHER  + " text);";



	private FishbowlDbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	private static FishbowlDbHelper dbHelper;
	public static FishbowlDbHelper getInstance(Context context)
	{
		if(dbHelper == null)
		{
			dbHelper = new FishbowlDbHelper(context);
		}
		return dbHelper;
	}


	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(CREATE_FB_USERTABLE);	
		database.execSQL(CREATE_FB_ORDERCONFIRM);	
		database.execSQL(CREATE_FB_ORDER);
		database.execSQL(CREATE_FB_STORETABLE);
		database.execSQL(CREATE_FB_OFFERTABLE);
		database.execSQL(CREATE_FB_USERADDRESS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) 
	{
		// TODO Auto-generated method stub

	}

}
