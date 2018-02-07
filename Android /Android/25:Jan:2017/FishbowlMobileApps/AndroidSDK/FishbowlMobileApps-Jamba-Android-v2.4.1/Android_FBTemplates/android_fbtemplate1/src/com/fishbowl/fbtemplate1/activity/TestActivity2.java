package com.fishbowl.fbtemplate1.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
/**
 **
 * Created by Digvijay Chauhan on 25/12/15.
 */
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.List_OrderConfirmAdapter;
import com.fishbowl.fbtemplate1.CoreActivity.FishbowlTemplate1App;
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;
import com.fishbowl.fbtemplate1.Model.OrderConfirmDrawItem;
import com.fishbowl.fbtemplate1.Model.OrderItem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import net.hockeyapp.android.CrashManager;


@SuppressLint("NewApi") public class TestActivity2 extends ActionBarActivity
{


	ImageView cartiv;
	public static Boolean checkflag=false;	
	TextView addcart,cart_textview;
	EditText semail;
	EditText spass;
	EditText mEdit;
	public static List<MenuDrawerItem> dataList1;
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
	private RadioGroup radioGroup;
	ArrayList<String> list = new ArrayList<String>();
	ArrayAdapter<String> adapter2;
	ArrayList<String> list1 = new ArrayList<String>();
	ArrayAdapter<String> adapter3;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("8");
		list.add("9");
		list.add("10");	
		list1.add("LARGE");
		list1.add("MEDIUM");
		list1.add("SMALL");

		Intent i=getIntent();
		Bundle extras = i.getExtras();
		if (extras != null)
		{
			drawitem = (MenuDrawerItem) extras.getSerializable("draweritem");
		//	dataList1 = (List<MenuDrawerItem>) i.getSerializableExtra("draweritem1");
			checkflag=extras.getBoolean("checkback", false);
			
			
			
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test2);		

		if(checkflag)
		{
			dataList1=null;	
		}
		

		FishbowlTemplate1App fishTApp=FishbowlTemplate1App.getInstance();


		mActionbar = getSupportActionBar();
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.clearCheck();

		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rb = (RadioButton) group.findViewById(checkedId);
				if(null!=rb && checkedId > -1)
				{

				}

			}
		});


		TextView T1=(TextView)findViewById(R.id.todayDate);
		TextView T2=(TextView)findViewById(R.id.expenseType1);
		NetworkImageView	nimage	=(NetworkImageView) findViewById(R.id.feedImage1);
		ImageLoader imageLoader = FishbowlTemplate1App.getInstance().getImageLoader();
		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		Button continu = (Button) findViewById(R.id.contnue_btn);
		adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
		spinner2.setAdapter(adapter2);
		adapter2.setDropDownViewResource(R.layout.spinner_item);
		adapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item, list1);	
		spinner.setAdapter(adapter3);	
		adapter3.setDropDownViewResource(R.layout.spinner_item);


		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
			{

				String selected_val=spinner.getSelectedItem().toString();

				drawitem.setGradient(selected_val);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parentView) 
			{

			}

		});


		spinner2.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
			{

				String selected_val=spinner2.getSelectedItem().toString();

				drawitem.setQuantity(Integer.valueOf(selected_val));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parentView) 
			{

			}

		});

		T1.setText(drawitem.getItemdesc());
		T2.setText(drawitem.getItemName());
		nimage.setImageUrl(drawitem.getImageurl(),imageLoader);

	}


	@SuppressLint("NewApi")
	protected void setActionBar() 
	{

		//	setActionBarTitle();
		mActionbar.setDisplayShowHomeEnabled(false);
		mActionbar.setDisplayShowTitleEnabled(false);
		mActionbar.setHomeButtonEnabled(false);
		mActionbar.setDisplayHomeAsUpEnabled(false);
		/*mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		setActionBarTitle();
		mActionbar.setHomeButtonEnabled(true);
		mActionbar.setDisplayHomeAsUpEnabled(true);




		//	mActionbar.setHomeButtonEnabled(false);
		mActionbar.setDisplayHomeAsUpEnabled(true);
		mActionbar.setDisplayShowHomeEnabled(true); 

		mActionbar.setDisplayShowTitleEnabled(false);

		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.customedit, null);

			ImageButton imageButton = (ImageButton) mCustomView.findViewById(R.id.imageButton);

		ImageButton imageButton = (ImageButton) mCustomView.findViewById(R.id.imageButton);




		addcart = (TextView) mCustomView.findViewById(R.id.title_textf);


		addcart.setOnClickListener(first_radio_listener);

		//continuetv.setOnClickListener(first_radio_listener);

		//	offeriv = (ImageView) mCustomView.findViewById(R.id.actionbar_offer_imageview);	


		mActionbar.setCustomView(mCustomView);
		mActionbar.setDisplayShowCustomEnabled(true);
		 */	}


	OnClickListener first_radio_listener = new OnClickListener (){
		public void onClick(View v) {

			//Your Implementaions...
			switch (v.getId()) 
			{


			/*	case R.id.title_textb:
				Intent inten = new Intent(TestActivity2.this, MenuActivity.class);		
				startActivity(inten);;
				break;*/

			case R.id.title_textf:

			{
				Intent intent = new Intent(TestActivity2.this,OrderConfirmActivity.class);
				Bundle extras = new Bundle();


			//	extras.putSerializable("draweritem1", (Serializable)dataList1);
			//	extras.putSerializable("draweritem", drawitem);

				
				
				 if(dataList1==null) 
				    {
				     dataList1 = new ArrayList<MenuDrawerItem>();
				     dataList1.add(drawitem);
				     extras.putSerializable("draweritem1", (Serializable)dataList1);
				     extras.putSerializable("draweritem", drawitem);
				    }

				    else

				    {
				     dataList1.add(drawitem);
				     extras.putSerializable("draweritem1", (Serializable)dataList1); 
				     extras.putSerializable("draweritem", drawitem);
				    }



				intent.putExtras(extras);
				startActivityForResult(intent, 2);
				break;
			}
			case R.id.imageButton:
				cartiv.setImageResource(R.drawable.cartr);
				Intent menucart = new Intent(TestActivity2.this, OrderConfirmActivity.class);		
				startActivity(menucart);
				break;

				
			case R.id.imageView1:
				Intent inta = new Intent(TestActivity2.this, MenuActivity.class);		
				startActivity(inta);;
				break;

			default:
				break;
			}
		}
	};

	public void setActionBarTitle()
	{

		//	mActionbar.setTitle("VIEW ITEM");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_edit, menu);
		MenuItem itemmo = menu.findItem(R.id.menu_edit1);

		MenuItemCompat.setActionView(itemmo, R.layout.customedit);
		RelativeLayout offer = (RelativeLayout) MenuItemCompat.getActionView(itemmo);

		ImageView backclick=(ImageView) offer.findViewById(R.id.imageView1);

		backclick.setOnClickListener(first_radio_listener);
		//	cart_textview = (TextView) offer.findViewById(R.id.actionbar_cart_textview);
		//	cartiv = (ImageView) offer.findViewById(R.id.imageButton);	
		//cart_textview.setText(String.valueOf(getCountcart()));

		addcart = (TextView) offer.findViewById(R.id.title_textf);

		addcart.setOnClickListener(first_radio_listener);

		//	cartiv.setOnClickListener(first_radio_listener);
		return super.onCreateOptionsMenu(menu);
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

		case R.id.menu_edit1:
		{
			Intent intent = new Intent(this,OrderConfirmActivity.class);
			Bundle extras = new Bundle();

			if(dataList1==null)	
			{
				dataList1 = new ArrayList<MenuDrawerItem>();
				dataList1.add(drawitem);
				extras.putSerializable("draweritem1", (Serializable)dataList1);
				extras.putSerializable("draweritem", drawitem);
			}

			else

			{
				dataList1.add(drawitem);
				extras.putSerializable("draweritem1", (Serializable)dataList1);	
				extras.putSerializable("draweritem", drawitem);
			}
			intent.putExtras(extras);
			startActivityForResult(intent, 2);
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}


	public void orderscreen(View v)
	{

		Intent intent = new Intent(this,OrderConfirmActivity.class);
		Bundle extras = new Bundle();

		if(dataList1==null)	
		{
			dataList1 = new ArrayList<MenuDrawerItem>();
			dataList1.add(drawitem);
			extras.putSerializable("draweritem1", (Serializable)dataList1);
			extras.putSerializable("draweritem", drawitem);
		}

		else

		{
			dataList1.add(drawitem);
			extras.putSerializable("draweritem1", (Serializable)dataList1);	
			extras.putSerializable("draweritem", drawitem);
		}
		intent.putExtras(extras);
		startActivityForResult(intent, 2);

	}

	@Override
	public void onStart() {
		super.onStart();
		setActionBar();

	}
	private void checkForCrashes() {
		CrashManager.register(this, "9665c28bb83c477dbf1081723fab9379");
	}




}
