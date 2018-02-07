package com.fishbowl.fbtemplate1.Adapter;

import com.fishbowl.fbtemplate1.fragment.LocationFavourite_Fragment;
import com.fishbowl.fbtemplate1.fragment.LocationList_Fragment;
import com.fishbowl.fbtemplate1.fragment.LocationRecent_Fragment;
import com.fishbowl.fbtemplate1.fragment.LocationMap_Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MATabsPagerAdapter extends FragmentPagerAdapter 
{
    String tabs[]={"LIST","MAP","FAVOURITE","RECENT"};

    public MATabsPagerAdapter(FragmentManager fm) 
    {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabs[position];
    }
 
    @Override
    public Fragment getItem(int index) 
    {
 
    	
        switch (index) {
        case 0:
            return new LocationList_Fragment();
        case 1:
			return new LocationMap_Fragment();
		case 2:
			return new LocationFavourite_Fragment();            
		case 3:
			return new LocationRecent_Fragment();
		
        }
 
        return null;
    }
 
    @Override
    public int getCount() 
    {
        // get item count - equal to number of tabs
        return 4;
    }
 
}