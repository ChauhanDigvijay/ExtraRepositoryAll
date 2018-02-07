package com.BasicApp.BusinessLogic.Services;

import android.app.Activity;

import com.fishbowl.basicmodule.Interfaces.FBRestaurantServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBRestaurantServiceDetailCallback;
import com.fishbowl.basicmodule.Interfaces.FBSessionServiceCallback;
import com.fishbowl.basicmodule.Models.FBRestaurantListDetailItem;
import com.fishbowl.basicmodule.Models.FBRestaurantListItem;
import com.fishbowl.basicmodule.Models.FBSessionItem;
import com.fishbowl.basicmodule.Services.FBRestaurantService;


/**
 * Created by digvijay(dj)
 */
public class FBARestaurantService {

    public static FBARestaurantService instance=null;
    public static Activity mContext;
    public static FBARestaurantService sharedInstance(Activity context){

        if(instance==null){
            instance=new FBARestaurantService(context);
        }

        return  instance;
    }
    public FBARestaurantService(Activity context)
    {
        if(context == null);
        mContext = context;
    }

    public void getAllStores() {

        FBRestaurantService.getAllRestaurants(new FBRestaurantServiceCallback() {
            @Override
            public void onRestaurantServiceCallback(FBRestaurantListItem response, Exception error) {
                if (response != null)
                {


                } else {

                }
            }
        });
    }


    public void getStoresSearch(String query,String query1,String query2) {

        FBRestaurantService.getAllRestaurantsNear(query, query1, query2, new FBRestaurantServiceCallback() {
            @Override
            public void onRestaurantServiceCallback(FBRestaurantListItem response, Exception error) {
                if (response != null) {

                } else {

                }
            }
        });
    }

    public void getStoresDetail(String storeid) {

        FBRestaurantService.getRestaurantById(String.valueOf(storeid), new FBRestaurantServiceDetailCallback() {
            @Override
            public void onRestaurantServiceDetailCallback(FBRestaurantListDetailItem response, Exception error) {
                if (response != null) {



                } else {

                }
            }
        });
    }

    public void favouriteStoreUpdate(String storeid) {

        FBRestaurantService.favourteStoreUpdate(String.valueOf(storeid), new FBSessionServiceCallback() {
            @Override
            public void onSessionServiceCallback(FBSessionItem response, Exception error) {
                if (response != null)

                {
                   ;


                } else {

                }
            }
        });
    }

}
