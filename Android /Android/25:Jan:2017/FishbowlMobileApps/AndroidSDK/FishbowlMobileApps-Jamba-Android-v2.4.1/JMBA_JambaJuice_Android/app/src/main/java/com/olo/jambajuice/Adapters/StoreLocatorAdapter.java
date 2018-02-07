package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorViewHolder;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;

import java.util.List;

/**
 * Created by Nauman Afzaal on 06/05/15.
 */
public class StoreLocatorAdapter extends ArrayAdapter<Store> {
    LayoutInflater inflater;

    public StoreLocatorAdapter(Context context, List<Store> stores) {
        super(context, R.layout.row_store_locator, stores);
        inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        if (super.getCount() > Constants.MaxStores) {
            return Constants.MaxStores;
        }
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Store store = getItem(position);
        View row = convertView;
        StoreLocatorViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.row_store_locator, parent, false);
            holder = new StoreLocatorViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (StoreLocatorViewHolder) row.getTag();
        }
        holder.invalidate(store);
        return row;
    }


}
