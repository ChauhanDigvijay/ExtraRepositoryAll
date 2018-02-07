package com.fishbowl.basicmodule.Services;

import com.fishbowl.basicmodule.Interfaces.FBJsonServiceCallback;
import com.fishbowl.basicmodule.Interfaces.FBMenuCategoryCallback;
import com.fishbowl.basicmodule.Interfaces.FBMenuDrawerCallback;
import com.fishbowl.basicmodule.Interfaces.FBMenuProductCallback;
import com.fishbowl.basicmodule.Interfaces.FBMenuSubCategoryCallback;
import com.fishbowl.basicmodule.Models.FBMenuCategoryDetailItem;
import com.fishbowl.basicmodule.Models.FBMenuCategoryItem;
import com.fishbowl.basicmodule.Models.FBMenuDrawerDetailItem;
import com.fishbowl.basicmodule.Models.FBMenuDrawerItem;
import com.fishbowl.basicmodule.Models.FBMenuProductDetailItem;
import com.fishbowl.basicmodule.Models.FBMenuProductItem;
import com.fishbowl.basicmodule.Models.FBMenuSubCategoryDetailItem;
import com.fishbowl.basicmodule.Models.FBMenuSubCategoryItem;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.fishbowl.basicmodule.Services.FBSessionService.fbSdk;

/**
 * Created by digvijay(dj)
 */
public class FBMenuService
{


    public static void getMenuCategory(String storeId, final FBMenuCategoryCallback callback)
    {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onMenuCategoryCallback(null, FBUtility.getNetworkError());
        } else {


            String path = FBConstant.FBMenuCategory + "?storeId=" + storeId;

            HashMap<String, Object> parameters = new HashMap<>();


            FBMainService.getInstance().get(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {
                    FBMenuCategoryItem menu = null;
                    if (response != null) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            Boolean successFlag = response.getBoolean("successFlag");
                            JSONArray jsonCategories = response.getJSONArray("productCategoryList");
                            FBMenuCategoryDetailItem[] categories = gson.fromJson(jsonCategories.toString(), FBMenuCategoryDetailItem[].class);
                            menu = new FBMenuCategoryItem(message,successFlag, categories);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            error = new Exception("Error occurred while parsing data.");
                        }
                    }
                    if (callback != null) {
                        callback.onMenuCategoryCallback(menu, error);
                    }
                }
            });
        }

    }


    public static void getMenuSubCategory(String storeId, String categoryId, final FBMenuSubCategoryCallback callback)
    {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onMenuSubCategoryCallback(null, FBUtility.getNetworkError());
        } else {


            String path = FBConstant.FBMenuSubCategory+"?storeId="+storeId+"&categoryId="+categoryId;

            HashMap<String, Object> parameters = new HashMap<>();


            FBMainService.getInstance().get(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {
                    FBMenuSubCategoryItem submenu = null;
                    if (response != null) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            Boolean successFlag = response.getBoolean("successFlag");
                            JSONArray jsonSubCategories = response.getJSONArray("productSubCategoryList");
                            FBMenuSubCategoryDetailItem[] categories = gson.fromJson(jsonSubCategories.toString(), FBMenuSubCategoryDetailItem[].class);
                            submenu = new FBMenuSubCategoryItem(message,successFlag, categories);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            error = new Exception("Error occurred while parsing data.");
                        }
                    }
                    if (callback != null) {
                        callback.onMenuSubCategoryCallback(submenu, error);
                    }
                }
            });
        }

    }



    public static void getDrawerProductList(String storeId, final String categoryId, String subCategoryId, final FBMenuDrawerCallback callback)
    {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onMenuDrawerCallback(null, FBUtility.getNetworkError());
        } else {


            String path = FBConstant.FBMenuDrawer + "?storeId=" + storeId + "&categoryId=" + categoryId + "&subCategoryId=" + subCategoryId;

            HashMap<String, Object> parameters = new HashMap<>();


            FBMainService.getInstance().get(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {
                    FBMenuDrawerItem category = null;
                    if (response != null) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            Boolean successFlag = response.getBoolean("successFlag");
                            JSONArray jsonSubCategories = response.getJSONArray("productList");
                            FBMenuDrawerDetailItem[] categories = gson.fromJson(jsonSubCategories.toString(), FBMenuDrawerDetailItem[].class);
                            category = new FBMenuDrawerItem(message,successFlag, categories);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            error = new Exception("Error occurred while parsing data.");
                        }
                    }
                    if (callback != null) {
                        callback.onMenuDrawerCallback(category, error);
                    }
                }
            });
        }

    }


    public static void getMenuProduct(String storeId, String categoryId, String subCategoryId, String productId, final FBMenuProductCallback callback)
    {
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            callback.onMenuProductCallback(null, FBUtility.getNetworkError());
        } else {


            String path = FBConstant.FBMenuDrawer + "?storeId=" + storeId + "&categoryId=" + categoryId + "&subCategoryId=" + subCategoryId;

            HashMap<String, Object> parameters = new HashMap<>();


            FBMainService.getInstance().get(path, parameters, new FBJsonServiceCallback() {
                @Override
                public void onServiceCallback(JSONObject response, Exception error) {
                    FBMenuProductItem category = null;
                    if (response != null) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            Boolean successFlag = response.getBoolean("successFlag");
                            JSONArray jsonSubCategories = response.getJSONArray("productDetails");
                            FBMenuProductDetailItem[] categories = gson.fromJson(jsonSubCategories.toString(), FBMenuProductDetailItem[].class);
                            category = new FBMenuProductItem(message,successFlag, categories);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            error = new Exception("Error occurred while parsing data.");
                        }
                    }
                    if (callback != null) {
                        callback.onMenuProductCallback(category, error);
                    }
                }
            });
        }

    }



}
