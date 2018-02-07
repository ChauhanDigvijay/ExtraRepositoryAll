package com.olo.jambajuice.Adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.olo.jambajuice.BusinessLogic.Models.UpSell;
import com.olo.jambajuice.Fragments.RecentOrderFragment;
import com.olo.jambajuice.Fragments.UpSellFragment;

import java.util.List;

/**
 * Created by vt021 on 21/10/17.
 */

public class UpSellImageAdapter extends FragmentStatePagerAdapter {
    List<UpSell> upSells;

    public UpSellImageAdapter(FragmentManager manager, List<UpSell> upSells) {
        super(manager);
        this.upSells = upSells;
    }

    @Override
    public Fragment getItem(int position) {
        return UpSellFragment.getInstance(upSells, position);
    }

    @Override
    public int getCount() {
        return upSells.size();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
