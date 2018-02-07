package com.olo.jambajuice.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductAd;
import com.olo.jambajuice.BusinessLogic.Models.ProductAdDetail;
import com.olo.jambajuice.Fragments.RecentOrderFragment;

import java.util.List;

/**
 * Created by Nauman Afzaal on 13/05/15.
 */
public class RecentOrderAdapter extends FragmentStatePagerAdapter {

    List<ProductAdDetail> recentOrders;

    public RecentOrderAdapter(FragmentManager manager, List<ProductAdDetail> recentOrders) {
        super(manager);
        this.recentOrders = recentOrders;
    }

    @Override
    public Fragment getItem(int position) {
        return RecentOrderFragment.getInstance(recentOrders, position);
    }

    @Override
    public int getCount() {
        return recentOrders.size();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
