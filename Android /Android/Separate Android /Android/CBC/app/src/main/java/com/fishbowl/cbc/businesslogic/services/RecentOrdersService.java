package com.fishbowl.cbc.businesslogic.services;

import com.fishbowl.apps.olo.Utils.Constants;
import com.fishbowl.cbc.businesslogic.managers.DataManager;
import com.fishbowl.cbc.businesslogic.models.BasketProduct;
import com.fishbowl.cbc.businesslogic.models.Product;
import com.fishbowl.cbc.businesslogic.models.RecentOrderProduct;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by VT027 on 5/20/2017.
 */

public class RecentOrdersService {
    private static final String RecentOrderProductEntityName = "RecentOrderProduct";
    private static final int MAX_RECENT_ORDER = 5;

    public static List<Product> getProductsFromRecentOrders() {
        List<RecentOrderProduct> list = getRecentData();
        List<Product> products = new ArrayList<>();
        if (list != null) {
            for (RecentOrderProduct recent : list) {
                try {
                    Product product = ProductService.getProductWithProductId(Integer.parseInt(recent.getId()));
                    product.setTimePlaced(recent.getTimePlaced());
                    products.add(product);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return products;
    }

    public static void cacheRecentProducts() {
        List<BasketProduct> basketProducts = DataManager.getInstance().getCurrentBasket().getProducts();
        List<RecentOrderProduct> recentOrderProducts = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.Server_Time_Format);
        Date time = Calendar.getInstance().getTime();
        String orderedTime = dateFormat.format(time);

        for (BasketProduct basketProduct : basketProducts) {
            Product product = DataManager.getInstance().getParseProductWithProductId(basketProduct.getProductId());
            if (product != null) {
                String id = product.getProductId() + "";
                recentOrderProducts.add(new RecentOrderProduct(id, orderedTime));
            }
        }
        persistRecentData(recentOrderProducts);
    }

    public static void clearAllData() {
        try {
            ParseObject.unpinAll(RecentOrderProductEntityName);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void persistRecentData(List<RecentOrderProduct> list) {
        HashMap<String, RecentOrderProduct> newRecentProductIds = new HashMap<>();
        HashMap<String, RecentOrderProduct> oldRecentProductIds = convertListToMap(getRecentData());

        for (RecentOrderProduct newProd : list) {
            newRecentProductIds.put(newProd.getId(), newProd);
            if (newRecentProductIds.size() >= MAX_RECENT_ORDER) {
                break;
            }
        }
        if (newRecentProductIds.size() < MAX_RECENT_ORDER) {
            for (RecentOrderProduct existingProd : oldRecentProductIds.values()) {
                if (!newRecentProductIds.containsKey(existingProd.getId())) {
                    newRecentProductIds.put(existingProd.getId(), existingProd);
                    if (newRecentProductIds.size() >= MAX_RECENT_ORDER) {
                        break;
                    }
                }
            }
        }

        try {
            ParseObject.unpinAll(RecentOrderProductEntityName);
            ParseObject.pinAll(RecentOrderProductEntityName, convertMapToList(newRecentProductIds));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, RecentOrderProduct> convertListToMap(List<RecentOrderProduct> list) {
        HashMap<String, RecentOrderProduct> recentProductIds = new HashMap<>();
        for (RecentOrderProduct product : list) {
            recentProductIds.put(product.getId(), product);
        }
        return recentProductIds;
    }

    private static List<RecentOrderProduct> convertMapToList(HashMap<String, RecentOrderProduct> recentProductIds) {
        List<RecentOrderProduct> list = new ArrayList<>();
        for (RecentOrderProduct product : recentProductIds.values()) {
            list.add(product);
        }
        return list;
    }

    private static List<RecentOrderProduct> getRecentData() {
        List<RecentOrderProduct> list = null;
        ParseQuery<RecentOrderProduct> query = ParseQuery.getQuery(RecentOrderProduct.class);
        query.fromLocalDatastore();
        query.orderByDescending("TimePlaced");
        try {
            list = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }
}
