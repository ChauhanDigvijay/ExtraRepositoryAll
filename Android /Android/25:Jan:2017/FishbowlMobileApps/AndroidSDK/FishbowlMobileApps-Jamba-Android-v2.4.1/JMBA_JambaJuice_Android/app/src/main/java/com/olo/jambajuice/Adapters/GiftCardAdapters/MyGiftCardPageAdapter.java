package com.olo.jambajuice.Adapters.GiftCardAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.Fragments.GiftCardFragments.MyGiftCardFragment;

/**
 * Created by vt02 on 5/23/16.
 */
public class MyGiftCardPageAdapter extends FragmentStatePagerAdapter {
    private FragmentManager fm;

    public MyGiftCardPageAdapter(FragmentManager fm){
        super(fm);
        if(this.fm!=null && this.fm.getFragments()!=null){
            this.fm.getFragments().clear();
        }
        this.fm= fm;
    }

    @Override
    public Fragment getItem(int position) {
        if(GiftCardDataManager.getInstance().getInCommCards()==null){
            return null;
        }
        return MyGiftCardFragment.newInstance(GiftCardDataManager.getInstance().getInCommCards().get(position),position);
    }

    @Override
    public int getCount() {

        if (GiftCardDataManager.getInstance().isDoNotifyDataSetChangedOnce()) {
            GiftCardDataManager.getInstance().setDoNotifyDataSetChangedOnce(false);
            notifyDataSetChanged();
        }

        if(GiftCardDataManager.getInstance().getInCommCards()==null){
            return 0;
        }
        return GiftCardDataManager.getInstance().getInCommCards().size();
    }

    @Override
    public int getItemPosition(Object object)
    {
        return PagerAdapter.POSITION_NONE;
    }

}
