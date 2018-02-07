package com.fishbowl.fbtemplate1.activity;

import java.util.List;

/**
 **
 * Created by Digvijay Chauhan on 25/12/15.
 */
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Controller.FB_DBUser;
import com.fishbowl.fbtemplate1.Model.MenuDrawerItem;
import com.fishbowl.fbtemplate1.Model.UserItem;
import com.fishbowl.fbtemplate1.util.StringUtilities;
import com.fishbowl.fbtemplate1.widget.MyMessageDialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.hockeyapp.android.CrashManager;


@SuppressLint("NewApi") public class SignInActivity extends ActionBarActivity {
	EditText semail;
	EditText spass;
	ActionBar mActionbar;
	TextView skip;
	private final int SPLASH_DISPLAY_LENGTH = 5000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);		
		semail	= ((EditText)findViewById(R.id.email_etxt));
		spass	= ((EditText)findViewById(R.id.pass_etxt));
		mActionbar = getSupportActionBar();
		setActionBar();
	}

	public void create(View v) 
	{
		Intent mainIntent = new Intent(this,RegistrationActivity.class);
		this.startActivity(mainIntent);

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

	public void setActionBarTitle()
	{
		/*	int dj = Integer.parseInt(expenseClaimItem.getTimeTask().getExtStatus().getStatusID().toString());*/



	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		getMenuInflater().inflate(R.menu.menu_registration, menu);
		MenuItem itemmo = menu.findItem(R.id.menu_edit1);

		MenuItemCompat.setActionView(itemmo, R.layout.customregistration);

		RelativeLayout offer = (RelativeLayout) MenuItemCompat.getActionView(itemmo);
		
		ImageView backclick=(ImageView) offer.findViewById(R.id.imageView1);

		backclick.setOnClickListener(first_radio_listener);
		skip = (TextView) offer.findViewById(R.id.title_textreg);
		
		skip.setOnClickListener(first_radio_listener);


		return super.onCreateOptionsMenu(menu);



	}
	OnClickListener first_radio_listener = new OnClickListener (){
		public void onClick(View v) {

			//Your Implementaions...
			switch (v.getId()) 
			{


			case R.id.title_textreg:
				Intent inten = new Intent(SignInActivity.this, MenuActivity.class);		
				startActivity(inten);;
				break;

			case R.id.imageView1:
				Intent inta = new Intent(SignInActivity.this, TestSignIn.class);		
				startActivity(inta);;
				break;  


			default:
				break;
			}
		}
	};


	public void login(View v) 
	{
		String email =semail.getText().toString();

		String pass=spass.getText().toString();

		if((!StringUtilities.isValidEmail(email)))
		{
			if(!StringUtilities.isValidEmail(email))
			{
				semail.setError("Email is required!");			
			}

			if(!StringUtilities.isValidString(pass))
			{
				spass.setError("Password is required!");		
			}
		}
		else
		{

			List<UserItem> userlist=	FB_DBUser.getInstance().getAllUsers();
			for(UserItem user:userlist)
			{
				if(user.getEmail().equals(email)&&user.getPassword().equals(pass))
				{
					Intent mainIntent = new Intent(this,SignUpActivity.class);
					this.startActivity(mainIntent);
				}
				else
				{
					Toast.makeText(SignInActivity.this,"Invalid Users",Toast.LENGTH_SHORT).show();
				}
			}
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

		return super.onOptionsItemSelected(item);
	}


	private void checkForCrashes() {
		CrashManager.register(this, "9665c28bb83c477dbf1081723fab9379");
	}

}
