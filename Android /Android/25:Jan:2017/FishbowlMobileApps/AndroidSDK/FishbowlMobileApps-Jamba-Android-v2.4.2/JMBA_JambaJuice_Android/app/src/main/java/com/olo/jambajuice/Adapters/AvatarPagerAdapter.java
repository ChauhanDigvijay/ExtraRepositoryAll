package com.olo.jambajuice.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.olo.jambajuice.Fragments.AvatarFragment;

import static com.olo.jambajuice.Utils.Constants.AVATAR_ICONS;

/**
 * Created by Nauman Afzaal on 13/05/15.
 */
public class AvatarPagerAdapter extends FragmentStatePagerAdapter {
    public AvatarPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return AvatarFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return AVATAR_ICONS.length;
    }
}
