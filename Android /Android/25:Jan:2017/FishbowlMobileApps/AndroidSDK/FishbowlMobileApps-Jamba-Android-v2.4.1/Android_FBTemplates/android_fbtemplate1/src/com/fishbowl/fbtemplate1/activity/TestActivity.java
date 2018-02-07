package com.fishbowl.fbtemplate1.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 **
 * Created by Digvijay Chauhan on 25/12/15.
 */
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.List_OrderConfirmAdapter;
import com.fishbowl.fbtemplate1.Controller.FB_DBLocation;
import com.fishbowl.fbtemplate1.Model.LocationItem;
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;
import com.fishbowl.fbtemplate1.Model.OrderConfirmDrawItem;
import com.fishbowl.fbtemplate1.Model.OrderItem;
import com.fishbowl.fbtemplate1.util.StringUtilities;
import com.fishbowl.fbtemplate1.widget.MyMessageDialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import net.hockeyapp.android.CrashManager;


@SuppressLint("NewApi") public class TestActivity extends ActionBarActivity 
{
	Button sign_delivery,sign_takeout;

	TextView conti;

	TextView tv1;
	EditText semail;
	EditText spass;
	TextView mEdit;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		Intent i=getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) 
		{
			drawitem = (MenuDrawerItem) extras.getSerializable("draweritem");
			order = (OrderItem) extras.getSerializable("order");
			listss = (List<MenuDrawerItem>) i.getSerializableExtra("draweritem1");
			historyflag=extras.getBoolean("historyflag", false);
			storelocation=extras.getString("storelocation");



		}


		setContentView(R.layout.timetest);		
		mActionbar = getSupportActionBar();
		/*		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		RadioButton	radiobtn = (RadioButton) findViewById(R.id.radioButton2);




		//radioGroup.clearCheck();
		radiobtn.setChecked(true);
		order.setOrderType(radiobtn.getText().toString());
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				RadioButton rb = (RadioButton) group.findViewById(checkedId);
				if(null!=rb && checkedId > -1)
				{
					order.setOrderType(rb.getText().toString());
				}

			}
		});*/


		sign_delivery = (Button)findViewById(R.id.sign_delivery);
		sign_delivery.setTextColor(getResources().getColor(R.color.White));
		sign_delivery.setBackgroundColor(getResources().getColor(R.color.fbred));

		sign_takeout = (Button)findViewById(R.id.sign_takeout);
		sign_takeout.setTextColor(getResources().getColor(R.color.fbred));

		TextView T1=(TextView)findViewById(R.id.text_location);
		TextView T2=(TextView)findViewById(R.id.text_locationname);
		TextView T3=(TextView)findViewById(R.id.changelocation_txt);
		mEdit = (TextView)findViewById(R.id.editText1);
		tv1=(TextView)findViewById(R.id.editText2);




		SimpleDateFormat df = new SimpleDateFormat(" HH:mm");
		String date = df.format(Calendar.getInstance().getTime());
		order.setOrdereDateNTime(date);
		tv1.setText(date);


		SimpleDateFormat sdf = new SimpleDateFormat( "dd//MM/yyyy" ); 
		mEdit.setText( sdf.format( new Date() ));
		order.setOrdereDateNTime(sdf.format( new Date()));
		T3.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)

			{
				Intent intent = new Intent(TestActivity.this,StoreLocationActivity.class);
				startActivity(intent);

			}
		});

		if(StringUtilities.isValidString(order.getStoreID()))
		{
			List<LocationItem> location=FB_DBLocation.getInstance().getAllLocationWithStore(order.getStoreID());	 
			T1.setText(location.get(0).getAddress());
			T2.setText(location.get(0).getName());

		}

	}


	@SuppressLint("NewApi")
	protected void setActionBar()
	{
		mActionbar.setDisplayShowHomeEnabled(false);
		mActionbar.setDisplayShowTitleEnabled(false);
		mActionbar.setHomeButtonEnabled(false);
		mActionbar.setDisplayHomeAsUpEnabled(false);	}

	public void setActionBarTitle()
	{

		//	mActionbar.setTitle("ORDER");
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_time, menu);
		MenuItem itemmo = menu.findItem(R.id.menu_edit1);

		MenuItemCompat.setActionView(itemmo, R.layout.customtimemenu);
		RelativeLayout offer = (RelativeLayout) MenuItemCompat.getActionView(itemmo);

		ImageView backclick=(ImageView) offer.findViewById(R.id.imageView1);

		backclick.setOnClickListener(first_radio_listener);

		conti = (TextView) offer.findViewById(R.id.title_textf);

		conti.setOnClickListener(first_radio_listener);

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

	public void contiue(View v) 
	{

		/*if((StringUtilities.isValidString(order.getOrderTime())) && (StringUtilities.isValidString(order.getOrdereDateNTime()))&& (StringUtilities.isValidString(order.getOrderType())))*/
		{
			Intent intent = new Intent(this,OrderConfirmActivity.class);	
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
			customAlertMessage();
		}
		 */
	}


	public void signin(View v) {

		String signdeliver=	sign_delivery.getText().toString();
		order.setOrderType(signdeliver);
		sign_delivery.setTextColor(getResources().getColor(R.color.White));
		sign_takeout.setTextColor(getResources().getColor(R.color.fbred));
		sign_delivery.setBackgroundColor(getResources().getColor(R.color.fbred));
		sign_takeout.setBackgroundColor(getResources().getColor(R.color.fbbackgroung));
	}

	public void signup(View v) {
		String signtake=	sign_takeout.getText().toString();
		order.setOrderType(signtake);
		sign_delivery.setTextColor(getResources().getColor(R.color.fbred));
		sign_takeout.setTextColor(getResources().getColor(R.color.White));
		sign_delivery.setBackgroundColor(getResources().getColor(R.color.fbbackgroung));
		sign_takeout.setBackgroundColor(getResources().getColor(R.color.fbred));
	}


	public void selectDate(View view)
	{
		DialogFragment newFragment = new SelectDateFragment();
		newFragment.show(getFragmentManager(), "DatePicker");
	}

	public void populateSetDate(int year, int month, int day) 
	{

		order.setOrdereDateNTime(day+"/"+month+"/"+year);
		mEdit.setText(day+"/"+month+"/"+year);
	}
	public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) 
		{
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd)
		{
			populateSetDate(yy, mm+1, dd);
		}
	}

	public void showTimePickerDialog(View v)
	{
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}

	public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) 
		{

			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			return new TimePickerDialog(getActivity(), this, hour, minute,DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute)
		{


			order.setOrderTime(""+view.getCurrentHour()+" : "+view.getCurrentMinute());
			tv1.setText(+view.getCurrentHour()+" : "+view.getCurrentMinute());



		}
	}



	OnClickListener first_radio_listener = new OnClickListener (){
		public void onClick(View v) {


			switch (v.getId()) 
			{

			case R.id.imageView1:
				Intent inta = new Intent(TestActivity.this, MenuActivity.class);		
				startActivity(inta);;
				break;
				
			case R.id.title_textf:
				Intent intent = new Intent(TestActivity.this,OrderConfirmActivity.class);	
				Bundle extras = new Bundle();
				{
					extras.putSerializable("draweritem1", (Serializable)listss);
					extras.putSerializable("order", order);
					extras.putSerializable("historyflag", true);
					extras.putSerializable("storelocation", storelocation);
				}
				intent.putExtras(extras);
				startActivityForResult(intent, 2);
				break;

			default:
				break;
			}
		}
	};


	public void customAlertMessage()
	{


		if(!StringUtilities.isValidString(order.getOrderType()))
			new MyMessageDialog(TestActivity.this,"Type","Type is Required").show(getSupportFragmentManager(), "MyDialog");
		else if (!StringUtilities.isValidString(order.getOrdereDateNTime()))
			new MyMessageDialog(TestActivity.this,"Date","Date is Required").show(getSupportFragmentManager(), "MyDialog");
		else if (!StringUtilities.isValidString(order.getOrderTime()))
			new MyMessageDialog(TestActivity.this,"Time","Time is Required").show(getSupportFragmentManager(), "MyDialog");


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
