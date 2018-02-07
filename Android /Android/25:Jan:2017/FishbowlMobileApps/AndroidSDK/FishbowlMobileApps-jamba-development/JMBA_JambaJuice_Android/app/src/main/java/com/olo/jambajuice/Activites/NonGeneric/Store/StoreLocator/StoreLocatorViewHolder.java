package com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;

import org.json.JSONObject;

/**
 * Created by Nauman Afzaal on 27/05/15.
 */
public class StoreLocatorViewHolder {
    TextView storeName;
    TextView storeAddress;
    TextView storeCity;
    TextView storeMiles;
    ImageView orderahead,delivery;

    public StoreLocatorViewHolder(View view) {
        storeName = (TextView) view.findViewById(R.id.storeName);
        storeAddress = (TextView) view.findViewById(R.id.storeAddress);
        storeCity = (TextView) view.findViewById(R.id.storeCity);
        storeMiles = (TextView) view.findViewById(R.id.storeMiles);
        orderahead=(ImageView)view.findViewById(R.id.orderAheadImage);
        delivery=(ImageView)view.findViewById(R.id.deliveryImage);
    }

    public void invalidate(Store store) {
        String name = null;
        if(DataManager.getInstance().isDebug){
            name = Utils.setDemoStoreName(store).getName().replace("Jamba Juice ", "");
        }else{
            name = store.getName().replace("Jamba Juice ", "");
        }

        storeName.setText(name);
        storeAddress.setText(store.getStreetAddress());
        storeCity.setText(store.getCity());
        storeMiles.setText(String.valueOf(Math.round(store.getDistanceToUser() * 10D) / 10D));
        if (store.isSupportsOrderAhead()) {
            orderahead.setVisibility(View.VISIBLE);
        } else {
            orderahead.setVisibility(View.GONE);
        }
        if(store.isSupportsDeliveryOption()){
            delivery.setVisibility(View.VISIBLE);
        }else{
            delivery.setVisibility(View.GONE);
        }
    }
}
