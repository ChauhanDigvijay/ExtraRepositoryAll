package com.BasicApp.ModelAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.FBRestaurantItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by digvijaychauhan on 28/09/16.
 */

public class StoreModelAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public List<FBRestaurantItem> storeResultList;
    public List<FBRestaurantItem> orig;

    FBRestaurantItem dItem;

    public StoreModelAdapter(Context context, List<FBRestaurantItem> storeList) {
        super();
        this.context = context;
        this.storeResultList = storeList;
    }




    public class StoreHolder {
        TextView storename, ItemName1, storelocation, storezipcode, storestate, storeno, storedistance;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<FBRestaurantItem> results = new ArrayList<FBRestaurantItem>();
                if (orig == null)
                    orig = storeResultList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final FBRestaurantItem g : orig) {
                            if (g.getStoreName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                storeResultList = (ArrayList<FBRestaurantItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return this.storeResultList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.storeResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        final StoreHolder drawerHolder;
        View view = convertView;


        double lat = 37.3462302;
        double lon = -121.9417057;
     //   FBStoresItem clps = FBStoreService.sharedInstance().allStoreFromServer.get(position);

        FBRestaurantItem clps = storeResultList.get(position);


        double lat1 = Double.valueOf(clps.getLatitude());
        double lon1 = Double.valueOf(clps.getLongitude());

        double earthRadius = 6371;
        double dLat = Math.toRadians(lat1 - lat);
        double dLng = Math.toRadians(lon1 - lon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat1)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);
        float miles = (float) (dist*.621371);
        //int b =(int)Math.round(dist);


        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new StoreHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.row_list_store, parent, false);
            drawerHolder.storename = (TextView) convertView.findViewById(R.id.storename);
            drawerHolder.storelocation = (TextView) convertView.findViewById(R.id.storelocation);
            drawerHolder.storedistance = (TextView) convertView.findViewById(R.id.storedistance);
            //    drawerHolder.storezipcode = (TextView) convertView.findViewById(R.id.storezipcode);
            //      drawerHolder.storestate = (TextView) convertView.findViewById(R.id.storestate);
            drawerHolder.storeno = (TextView) convertView.findViewById(R.id.storeno);

            convertView.setTag(drawerHolder);

        } else {
            drawerHolder = (StoreModelAdapter.StoreHolder) view.getTag();


        }
        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }


		/*if(position % 2 == 0)
		{
			view.setBackgroundColor(Color.rgb(238, 233, 233));
		}*/
        dItem = (FBRestaurantItem) this.storeResultList.get(position);
        DecimalFormat df = new DecimalFormat("0.00##");
        drawerHolder.storename.setText(dItem.getStoreName());
        drawerHolder.storedistance.setText ((String.format("%.2f",miles))+" "+"miles");

        drawerHolder.storelocation.setText(dItem.getAddress());
        // drawerHolder.storezipcode.setText(""+dItem.getZip());
        drawerHolder.storeno.setText("" + dItem.getPhone());
        //      drawerHolder.storestate.setText(dItem.getState()+" "+"("+dItem.getCity()+")");


        return convertView;
    }


}



