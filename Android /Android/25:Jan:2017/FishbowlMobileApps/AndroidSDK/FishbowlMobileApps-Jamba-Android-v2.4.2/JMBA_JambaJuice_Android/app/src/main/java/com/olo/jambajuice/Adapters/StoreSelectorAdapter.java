package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.R;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 14/05/15.
 */
public class StoreSelectorAdapter extends ArrayAdapter<Store> {
    ArrayList<Store> data = null;
    LayoutInflater inflater;

    public StoreSelectorAdapter(Context context, ArrayList<Store> data) {
        super(context, android.R.layout.simple_list_item_1);
        this.data = data;
        inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            row = inflater.inflate(R.layout.row_store_selector, parent, false);
            holder = new ViewHolder();
            holder.storeName = (TextView) row.findViewById(R.id.storeName);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.storeName.setText(data.get(position).getName());
        return row;
    }

    static class ViewHolder {
        TextView storeName;
    }
}
