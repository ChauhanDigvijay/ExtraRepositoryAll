package com.fishbowl.fbtemplate1.fragment;
/**
 **
 * Created by Digvijay Chauhan on 14/12/15.
 */
import com.fishbowl.fbtemplate1.R;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


@SuppressLint("NewApi") public class Order_Fragment  extends Fragment implements OnClickListener
{
	Activity thisActivity;
	LayoutInflater inflater;
	private ViewGroup parentContainer = null;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{

		View rootView = inflater.inflate(R.layout.activity_order, container, false);
		this.inflater = inflater;
		rootView.setTag("USER_TAGS_TAB");
		parentContainer = container;
		thisActivity = getActivity();
		Button b = (Button) rootView.findViewById(R.id.instore_btn);
		Button b1 = (Button) rootView.findViewById(R.id.drivethru_btn);
		b.setOnClickListener(this);
		b1.setOnClickListener(this);

		setHasOptionsMenu(true);
		return rootView;
	}	





	public void message(View v) 
	{

		Intent mainIntent = new Intent(getActivity(),ZpizzaCard_Fragment.class);
		this.startActivity(mainIntent);

	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{

		int id = item.getItemId();		
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		ZpizzaCard_Fragment chatDetailFragment = new ZpizzaCard_Fragment();	
		FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
		fragTransaction.hide(this);
		fragTransaction.replace(R.id.relativemain,chatDetailFragment );
		fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTransaction.addToBackStack(null);
		fragTransaction.commit();


	}
}
