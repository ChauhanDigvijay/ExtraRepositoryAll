package com.fishbowl.fbtemplate1.activity;

import java.util.List;

/**
 **
 * Created by Digvijay Chauhan on 7/1/16.
 */
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Adapter.OfferAdapter;
import com.fishbowl.fbtemplate1.Controller.FB_DBOffer;
import com.fishbowl.fbtemplate1.Model.OfferItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import net.hockeyapp.android.CrashManager;


@SuppressLint("NewApi") public class OfferActivity extends ActionBarActivity 
{
	ListView lv;
	ActionBar mActionbar;
	OfferAdapter adapter ;	
	public static List<OfferItem> dataList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offer);		
		
		mActionbar = getSupportActionBar();
		dataList=FB_DBOffer.getInstance().getAllItem();
		lv=(ListView)this.findViewById(R.id.image_list);
		adapter = new OfferAdapter(this, R.layout.list_offer,dataList);	
		lv.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		setActionBar();
		//	Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(this));
	}

	

	@SuppressLint("NewApi")
	protected void setActionBar() {
		mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		setActionBarTitle();
		mActionbar.setHomeButtonEnabled(true);
		mActionbar.setDisplayHomeAsUpEnabled(true);
	}

	public void setActionBarTitle()
	{
		/*	int dj = Integer.parseInt(expenseClaimItem.getTimeTask().getExtStatus().getStatusID().toString());*/


		mActionbar.setTitle("OFFERS");
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
			}
		return super.onOptionsItemSelected(item);
	}

	/*public void customAlertMessage()
	{


		if(!StringUtilities.isValidString(expenseClaimItem.getExpenseData().getType()))
			new MyMessageDialog(SignInActivity.this,"Type","Type is Required").show(getSupportFragmentManager(), "MyDialog");
		else if (!StringUtilities.isValidDoubleToString(Double.toString(expenseClaimItem.getExpenseData().getAmt().getAmount())))
			new MyMessageDialog(SignInActivity.this,"Amount","Amount is Required").show(getSupportFragmentManager(), "MyDialog");
		else
			new MyMessageDialog(SignInActivity.this,"Type and Amount ","Type and Amount is Required").show(getSupportFragmentManager(), "MyDialog");

	}

	public void customAlertDialog()
	{
		try {

			final Dialog dialog = new Dialog(SignInActivity.this,R.style.CustomDialogTheme);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_dialog_new);
			dialog.setCancelable(false);
			Button cancel = (Button)dialog.findViewById(R.id.textCancel);
			Button never = (Button)dialog.findViewById(R.id.textNever);
			Button ok = (Button)dialog.findViewById(R.id.textOk);
			//TextView titleText = (TextView) dialog.findViewById(R.id.title_txt);
			TextView desc = (TextView) dialog.findViewById(R.id.txt_dia);
			if(str==null || str=="")
				desc.setText("Type is required");
			else if(expenseClaimItem.getExpenseData().getAmt().getAmount()<0||expenseClaimItem.getExpenseData().getAmt().getAmount()==0.0)
				desc.setText("Amount is required");
			else if(expenseClaimItem.getRemark()==null||expenseClaimItem.getRemark().get(0).getText().toString()==null)
				desc.setText("Description is required");
			else
				desc.setText("Classification are mandatory in Expense !!");

			ok.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					SignInActivity.this.finish();

				}
			});

			never.setVisibility(View.GONE);

			cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});

			dialog.setCancelable(true);
			dialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/


	private void checkForCrashes() 
	{
		CrashManager.register(this, "9665c28bb83c477dbf1081723fab9379");
	}

}
