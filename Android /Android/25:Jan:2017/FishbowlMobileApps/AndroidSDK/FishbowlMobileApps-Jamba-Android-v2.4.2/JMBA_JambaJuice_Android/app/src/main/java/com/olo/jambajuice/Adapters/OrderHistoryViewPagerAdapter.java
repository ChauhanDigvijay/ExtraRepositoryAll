package com.olo.jambajuice.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.olo.jambajuice.BusinessLogic.Models.FavoriteOrder;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.Fragments.OrderFavoriteFragment;
import com.olo.jambajuice.Fragments.OrderHistoryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ihsanulhaq on 18/06/15.
 */
public class OrderHistoryViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<RecentOrder> recentOrder;
    private List<FavoriteOrder> favOrder;

    public OrderHistoryViewPagerAdapter(FragmentManager manager, List<RecentOrder> recentOrder, List<FavoriteOrder> favoriteOrder) {
        super(manager);
        this.recentOrder = recentOrder;
        this.favOrder = favoriteOrder;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (recentOrder == null) {
                    recentOrder = new ArrayList<RecentOrder>();
                }
                return OrderHistoryFragment.newInstance(recentOrder);
            case 1:
                if (favOrder == null) {
                    favOrder = new ArrayList<FavoriteOrder>();
                }
                return OrderFavoriteFragment.getInstance(favOrder);
            default:
                if (recentOrder == null) {
                    recentOrder = new ArrayList<RecentOrder>();
                }
                return OrderHistoryFragment.newInstance(recentOrder);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
