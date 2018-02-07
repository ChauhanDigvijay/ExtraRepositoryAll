package com.fishbowl.fbtemplate1.CoreActivity;
import java.util.List;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
import com.fishbowl.fbtemplate1.R;
import com.fishbowl.fbtemplate1.Controller.FB_DBUser;
import com.fishbowl.fbtemplate1.Model.UserItem;
import com.fishbowl.fbtemplate1.activity.MenuActivity;
import com.fishbowl.fbtemplate1.activity.SignUpActivity;
import com.fishbowl.fbtemplate1.activity.TestSignIn;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity
{

	private final int SPLASH_DISPLAY_LENGTH = 5000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run() 
			{
					List<UserItem> userlist=	FB_DBUser.getInstance().getAllUsers();
				if(userlist!=null&&userlist.size()>0)	
				{
					for(UserItem user:userlist)
					{
						if(user.isConfirmed())
						{

							Intent mainIntent = new Intent(MainActivity.this,MenuActivity.class);
							MainActivity.this.startActivity(mainIntent);
							MainActivity.this.finish();
						}
						else
						{

							Intent mainIntent = new Intent(MainActivity.this,TestSignIn.class);
							MainActivity.this.startActivity(mainIntent);
							MainActivity.this.finish();
						}
					}
				}
				else
				{
					Intent mainIntent = new Intent(MainActivity.this,TestSignIn.class);
					MainActivity.this.startActivity(mainIntent);
					MainActivity.this.finish();
				}
			}
		}, SPLASH_DISPLAY_LENGTH);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}
}
