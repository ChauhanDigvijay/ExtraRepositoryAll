package com.fishbowl.LoyaltyTabletApp.Adapters;


import android.content.Context;
import android.widget.ArrayAdapter;

public class HintAdapter extends ArrayAdapter<String> {

    public HintAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub

    }

    @Override
    public int getCount() {

        // TODO Auto-generated method stub
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}