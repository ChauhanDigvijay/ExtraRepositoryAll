package com.fishbowl.cbc.businesslogic.services;

import android.app.Activity;

import com.fishbowl.apps.olo.Interfaces.OloMenuServiceCallback;
import com.fishbowl.apps.olo.Models.OloCategory;
import com.fishbowl.apps.olo.Models.OloMenu;
import com.fishbowl.apps.olo.Models.OloProduct;
import com.fishbowl.apps.olo.Services.OloMenuService;
import com.fishbowl.cbc.CbcApplication;
import com.fishbowl.cbc.businesslogic.interfaces.AllStoreMenuCallBack;
import com.fishbowl.cbc.businesslogic.interfaces.BasketServiceCallback;
import com.fishbowl.cbc.businesslogic.interfaces.ProductCategoryServiceCallback;
import com.fishbowl.cbc.businesslogic.interfaces.ProductFamilyServiceCallback;
import com.fishbowl.cbc.businesslogic.interfaces.ProductServiceCallback;
import com.fishbowl.cbc.businesslogic.interfaces.StoreCalendarCallback;
import com.fishbowl.cbc.businesslogic.interfaces.StoreMenuCallback;
import com.fishbowl.cbc.businesslogic.managers.DataManager;
import com.fishbowl.cbc.businesslogic.models.Basket;
import com.fishbowl.cbc.businesslogic.models.Product;
import com.fishbowl.cbc.businesslogic.models.ProductCategory;
import com.fishbowl.cbc.businesslogic.models.ProductFamily;
import com.fishbowl.cbc.businesslogic.models.Store;
import com.fishbowl.cbc.businesslogic.models.StoreMenuProduct;
import com.fishbowl.cbc.businesslogic.models.StoreTiming;
import com.fishbowl.cbc.utils.SharedPreferenceHandler;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.fishbowl.cbc.utils.SharedPreferenceHandler.LastProductUpdate;

/**
 * Created by VT027 on 5/22/2017.
 */

public class ProductService {
    public static int TwentyFourHourInMiliSeconds = 24 * 60 * 60 * 1000;
    private static String ProductEntityName = "Product";
    private static String ProductCategoryEntityName = "ProductCategory";
    private static String ProductFamilyEntityName = "ProductFamily";
    private static String ProductAd = "Ad";
    private static String ProductAdDetail = "AdDetail";

    public static void loadFeaturedProducts(ProductServiceCallback callback) {
        final WeakReference<ProductServiceCallback> callbackWeakReference = new WeakReference<ProductServiceCallback>(callback);
        ProductService.loadDataFromServerIfRequired(new LoadCallback() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductEntityName);
                query.whereEqualTo("featured", true);
                query.orderByAscending("order");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        Exception exception = null;
                        List<Product> products = null;
                        if (e != null || parseObjects == null) {
                            exception = new Exception("Unable to load featured products");
                        } else {
                            products = new ArrayList<Product>();
                            for (ParseObject object : parseObjects) {
                                Product product = new Product(object);
                                products.add(product);
                            }
                        }
                        ProductServiceCallback cb = callbackWeakReference.get();
                        if (cb != null && !((Activity) cb).isFinishing()) {
                            cb.onProductServiceCallback(products, null, exception);
                        }
                    }
                });
            }
        });

    }

    /**
     * TODO: uncomment when ads are available : loadadsconfig
     * @param activity
     * @param ad
     * @param callback
     */
//    public static void loadAdsConfig(final Activity activity, final ProductAdsServiceCallback callback) {
//        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
//        ProductService.loadDataFromServerIfRequired(new LoadCallback() {
//            @Override
//            public void run() {
//                ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductAd);
//                query.fromLocalDatastore();
//                query.findInBackground(new FindCallback<ParseObject>() {
//                    @Override
//                    public void done(List<ParseObject> parseObjects, ParseException e) {
//                        Exception exception = null;
//                        ArrayList<ProductAd> ads = null;
//                        if (e != null || parseObjects == null) {
//                            exception = new Exception("Unable to load ads");
//                        } else {
//                            ads = new ArrayList<ProductAd>();
//                            for (ParseObject obj : parseObjects) {
//                                ads.add(new ProductAd(obj));
//                            }
//                        }
//
//                        Activity cb = callbackWeakReference.get();
//                        if (cb != null && !((Activity) cb).isFinishing()) {
//                            callback.onProductAdsCallback(ads, exception);
//                        }
//                    }
//                });
//            }
//        });
//    }

    /**
     * TODO: uncomment when ads are available : loadalladdetails
     *
     * @param callback
     */
//    public static void loadAllAdDetails(final Activity activity,final ProductAd ad,final ProductAdDetailsServiceCallback callback) {
//        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
//        ProductService.loadDataFromServerIfRequired(new LoadCallback() {
//            @Override
//            public void run() {
//                ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductAdDetail);
//                ParseObject object = ParseObject.createWithoutData(ProductAd, ad.getObjectId());
//                query.whereEqualTo("Ad",object);
//                query.whereEqualTo("status", true);
//                query.orderByAscending("order_no");
//                query.fromLocalDatastore();
//                query.findInBackground(new FindCallback<ParseObject>() {
//                    @Override
//                    public void done(List<ParseObject> parseObjects, ParseException e) {
//                        Exception exception = null;
//                        ArrayList<ProductAdDetail> ads = null;
//                        if (e != null || parseObjects == null) {
//                            exception = new Exception("Unable to load ad details");
//                        } else {
//                            ads = new ArrayList<ProductAdDetail>();
//                            for (ParseObject obj : parseObjects) {
//                                ads.add(new ProductAdDetail(obj));
//                            }
//                        }
//
//                        Activity cb = callbackWeakReference.get();
//                        if (cb != null && !((Activity) cb).isFinishing()) {
//                            callback.onProductAdDetailsCallback(ads, exception);
//                        }
//                    }
//                });
//            }
//        });
//    }
    public static void loadAllProductCategories(ProductCategoryServiceCallback callback) {
        final WeakReference<ProductCategoryServiceCallback> callbackWeakReference = new WeakReference<ProductCategoryServiceCallback>(callback);
        ProductService.loadDataFromServerIfRequired(new LoadCallback() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductCategoryEntityName);
                query.orderByAscending("order");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        Exception exception = null;
                        ArrayList<ProductCategory> categories = null;
                        if (e != null || parseObjects == null) {
                            exception = new Exception("Unable to load menu categories");
                        } else {
                            categories = new ArrayList<ProductCategory>();
                            for (ParseObject obj : parseObjects) {
                                categories.add(new ProductCategory(obj));
                            }
                        }
                        ProductCategoryServiceCallback cb = callbackWeakReference.get();
                        if (cb != null && !((Activity) cb).isFinishing()) {
                            cb.onProductCategoryCallback(categories, exception);
                        }
                    }
                });
            }
        });
    }

    public static void loadAllProductFamilies(ProductFamilyServiceCallback callback) {
        final WeakReference<ProductFamilyServiceCallback> callbackWeakReference = new WeakReference<ProductFamilyServiceCallback>(callback);
        ProductService.loadDataFromServerIfRequired(new LoadCallback() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductFamilyEntityName);
                query.orderByAscending("order");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        Exception exception = null;
                        List<ProductFamily> productFamily = null;
                        if (e != null || parseObjects == null) {
                            exception = new Exception("Unable to load product families");
                        } else {
                            productFamily = new ArrayList<ProductFamily>();
                            for (ParseObject obj : parseObjects) {
                                productFamily.add(new ProductFamily(obj));
                            }

                            ProductFamilyServiceCallback cb = callbackWeakReference.get();
                            if (cb != null && !((Activity) cb).isFinishing()) {
                                cb.onProductFamilyCallback(productFamily, exception);
                            }
                        }
                    }
                });
            }
        });
    }


    public static void loadProductsForCategory(Activity activity, final ProductCategory productCategory, final ProductServiceCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(activity);
        ProductService.loadDataFromServerIfRequired(new LoadCallback() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductEntityName);
                ParseObject object = ParseObject.createWithoutData(ProductCategoryEntityName, productCategory.getObjectID());
                query.whereEqualTo("category", object);
                query.orderByAscending("order");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        Exception exception = null;
                        List<Product> products = null;
                        if (e != null || parseObjects == null) {
                            exception = new Exception("Unable to load product families");
                        } else {
                            products = new ArrayList<Product>();
                            for (ParseObject obj : parseObjects) {
                                Product prod = new Product(obj);
                                if (prod.getStoreMenuProduct() != null) {
                                    products.add(new Product(obj));
                                }
                            }

                            Activity cb = callbackWeakReference.get();
                            if (cb != null && !((Activity) cb).isFinishing()) {
                                callback.onProductServiceCallback(products, productCategory, exception);
                            }
                        }
                    }
                });
            }
        });
    }

    public static void startNewOrderForStore(final Activity context, final Store store, final AllStoreMenuCallBack callback) {
        /**
         * TODO: uncomment when track event by name analytic is available, start new order
         */
        //JambaAnalyticsManager.sharedInstance().track_EventbyName(FBEventSettings.START_NEW_ORDER);
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(context);
        getStoreMenu(context, store.getRestaurantId(), new StoreMenuCallback() {
            @Override
            public void onStoreMenuCallback(final HashMap<Integer, StoreMenuProduct> storeProducts, Exception exception) {
                if (storeProducts != null) {
                    BasketService.createBasket(callbackWeakReference.get(), store.getRestaurantId(), new BasketServiceCallback() {
                        @Override
                        public void onBasketServiceCallback(Basket basket, Exception error) {
                            if (basket != null) {
                                //Store menu as we have successfully created basket.
//                                        DataManager manager = DataManager.getInstance();
//                                        manager.setCurrentSelectedStore(store);
//                                        manager.setStoreMenu(storeProducts);
//                                        //put current selected store in persistent file
//                                        Gson gson = new Gson();
//                                        String json = gson.toJson(store);
//                                        SharedPreferenceHandler.put(SharedPreferenceHandler.LastSelectedStore, json);
                                //AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.STORES.value, "start_new_order", store.getName());

                                ///
                                loadAllFamily(callbackWeakReference, store, storeProducts, callback);

                            } else if (error != null) {
                                notifyErrorCallback(error, callbackWeakReference, callback);
                            } else {
                                notifyErrorCallback(new Exception("Error in creating Basket"), callbackWeakReference, callback);
                            }


                        }
                    });
                } else if (exception != null) {
                    notifyErrorCallback(exception, callbackWeakReference, callback);
                } else {
                    notifyErrorCallback(new Exception("Error in getting store"), callbackWeakReference, callback);
                }
            }
        });
    }

    private static void notifyErrorCallback(Exception exception, WeakReference<Activity> callbackWeakReference, AllStoreMenuCallBack callback) {
        Activity cb = callbackWeakReference.get();
        if (cb != null && !cb.isFinishing()) {
            callback.onAllStoreMenuErrorCallback(exception);
        }
    }

    private static void notifySuccessCallback(Exception exception, WeakReference<Activity> callbackWeakReference, AllStoreMenuCallBack callback) {
        Activity cb = callbackWeakReference.get();
        if (cb != null && !cb.isFinishing()) {
            callback.onAllStoreMenuCallback(exception);
        }
    }


    public static void loadAllFamily(final WeakReference<Activity> callbackWeakReference, final Store store, final HashMap<Integer, StoreMenuProduct> storeProducts, final AllStoreMenuCallBack callback) {

        ProductService.loadDataFromServerIfRequired(new LoadCallback() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductFamilyEntityName);
                query.orderByAscending("order");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        Exception exception = null;
                        List<ProductFamily> productFamily = null;
                        if (e != null || parseObjects == null || parseObjects.size() == 0) {
                            exception = new Exception("There is a problem in connecting server. Please try again..!!");
                            notifyErrorCallback(exception, callbackWeakReference, callback);
                        } else {
                            productFamily = new ArrayList<ProductFamily>();
                            for (ParseObject obj : parseObjects) {
                                productFamily.add(new ProductFamily(obj));
                            }

                            DataManager manager = DataManager.getInstance();
                            manager.setCurrentSelectedStore(store);
                            manager.setStoreMenu(storeProducts);
                            //put current selected store in persistent file
                            Gson gson = new Gson();
                            String json = gson.toJson(store);
                            SharedPreferenceHandler.put(SharedPreferenceHandler.LastSelectedStore, json);
                            manager.setAllProductFamily(productFamily);
                            loadAllCategory(callbackWeakReference, callback);
                        }
                    }
                });
            }
        });
    }

    private static void loadAllCategory(final WeakReference<Activity> callbackWeakReference, final AllStoreMenuCallBack callback) {
        ProductService.loadDataFromServerIfRequired(new LoadCallback() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductCategoryEntityName);
                query.orderByAscending("order");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        Exception exception = null;
                        ArrayList<ProductCategory> categories = null;
                        if (e != null || parseObjects == null || parseObjects.size() == 0) {
                            exception = new Exception("There is a problem in connecting server. Please try again..!!");
                            notifyErrorCallback(exception, callbackWeakReference, callback);
                        } else {
                            categories = new ArrayList<ProductCategory>();
                            for (ParseObject obj : parseObjects) {
                                categories.add(new ProductCategory(obj));
                            }
                            DataManager manager = DataManager.getInstance();

                            manager.setAllCategories(categories);//set all categories of Parse
                            manager.setTempSelectedStoreCategories(categories);//set all categories of Parse as selected category initialy
                            loadAllFeaturedProducts(callbackWeakReference, callback);
                        }
                    }
                });
            }
        });
    }

    private static void loadAllFeaturedProducts(final WeakReference<Activity> callbackWeakReference, final AllStoreMenuCallBack callback) {

        ProductService.loadDataFromServerIfRequired(new LoadCallback() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductEntityName);
                query.whereEqualTo("featured", true);
                query.orderByAscending("order");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        Exception exception = null;
                        List<Product> products = null;
                        if (e != null || parseObjects == null || parseObjects.size() == 0) {
                            exception = new Exception("There is a problem in connecting server. Please try again..!!");
                            notifyErrorCallback(exception, callbackWeakReference, callback);
                        } else {
                            products = new ArrayList<Product>();
                            for (ParseObject object : parseObjects) {
                                Product product = new Product(object);
                                //Store only storemenu featured products
                                if (product.getStoreMenuProduct() != null)
                                    products.add(product);
                            }
                        }

                        DataManager manager = DataManager.getInstance();
                        manager.setTempSelectedStoreFeaturedProducts(products);// set filtered featured product family


                        ArrayList<ProductCategory> categories = new ArrayList<ProductCategory>();
                        categories = manager.getAllCategories();

                        Activity activity = callbackWeakReference.get();
                        if (activity != null) {
                            ((CbcApplication) activity.getApplication()).categoryProductDownloadQueue = categories.size();
                            for (ProductCategory category : categories) {
                                loadProductsForCategory(callbackWeakReference, category, callback);
                            }
                        } else {
                            exception = new Exception("There is a problem in connecting server. Please try again..!!");
                            notifyErrorCallback(exception, callbackWeakReference, callback);
                        }
                    }
                });
            }
        });

    }

    public static void loadProductsForCategory(final WeakReference<Activity> callbackWeakReference, final ProductCategory productCategory, final AllStoreMenuCallBack callback) {

        ProductService.loadDataFromServerIfRequired(new LoadCallback() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductEntityName);
                ParseObject object = ParseObject.createWithoutData(ProductCategoryEntityName, productCategory.getObjectID());
                query.whereEqualTo("category", object);
                query.orderByAscending("order");
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        Exception exception = null;
                        List<Product> products = null;
                        if (e != null || parseObjects == null || parseObjects.size() == 0) {
                            exception = new Exception("There is a problem in connecting server. Please try again..!!");
                        } else {
                            products = new ArrayList<Product>();
                            for (ParseObject obj : parseObjects) {
                                Product prod = new Product(obj);
                                if (prod.getStoreMenuProduct() != null) {
                                    products.add(new Product(obj));
                                }
                            }

                            DataManager manager = DataManager.getInstance();
                            Activity cb = callbackWeakReference.get();
                            if (cb != null && !cb.isFinishing()) {
                                //check download queue finished
                                if (((CbcApplication) cb.getApplication()).categoryProductDownloadQueue <= 1) {

                                    //Check and filter categories
                                    ArrayList<ProductCategory> filteredCategories = new ArrayList<ProductCategory>();
                                    filteredCategories = manager.getTempSelectedStoreCategories();
                                    if (products.size() == 0 && filteredCategories.contains(productCategory)) {
                                        filteredCategories.remove(productCategory);
                                        manager.setTempSelectedStoreCategories(filteredCategories);
                                    }
                                    //

                                    //Filter product family using filtered categories
                                    ArrayList<ProductFamily> selectedProductFamily = new ArrayList<ProductFamily>();
                                    for (ProductFamily productFamily : manager.getAllProductFamily()) {
                                        boolean isFamilyExist = false;
                                        for (int i = 0; i < filteredCategories.size(); i++) {
                                            ProductCategory productCategory = filteredCategories.get(i);
                                            if (productFamily.getObjectID().equals(productCategory.getFamily())) {
                                                isFamilyExist = true;
                                                break;
                                            }
                                        }
                                        if (isFamilyExist)
                                            selectedProductFamily.add(productFamily);
                                    }

                                    manager.setTempSelectedStoreProductFamily(selectedProductFamily);//set filtered store based product family
                                    DataManager.getInstance().switchTempValuesToMainValues();
                                    notifySuccessCallback(null, callbackWeakReference, callback);

                                } else {

                                    //Check and filter categories for the current queue
                                    ArrayList<ProductCategory> filteredCategories = new ArrayList<ProductCategory>();
                                    filteredCategories = manager.getTempSelectedStoreCategories();
                                    if (products.size() == 0 && filteredCategories.contains(productCategory)) {
                                        filteredCategories.remove(productCategory);
                                        manager.setTempSelectedStoreCategories(filteredCategories);
                                    }
                                    //
                                    //Update queue
                                    ((CbcApplication) cb.getApplication()).categoryProductDownloadQueue = ((CbcApplication) cb.getApplication()).categoryProductDownloadQueue - 1;
                                }
                            } else {
                                notifyErrorCallback(new Exception("There is a problem in connecting server. Please try again..!!"), callbackWeakReference, callback);
                            }

                        }
                    }
                });
            }
        });
    }

    public static void getStoreMenu(final Activity context, final int storeId, final StoreMenuCallback callback) {
        final WeakReference<Activity> callbackWeakReference = new WeakReference<Activity>(context);
        OloMenuService.getRestaurantMenu(storeId, new OloMenuServiceCallback() {
            @Override
            public void onRestaurantMenuCallback(OloMenu menu, Exception exception) {
                HashMap<Integer, StoreMenuProduct> storeProducts = null;
                if (exception == null) {
                    storeProducts = new HashMap<Integer, StoreMenuProduct>();
                    OloCategory[] categories = menu.getCategories();
                    if (categories != null) {
                        for (OloCategory category : categories) {
                            List<OloProduct> oloProducts = category.getProducts();
                            for (OloProduct product : oloProducts) {
                                //ChainProductId is globally unique for each product.
                                storeProducts.put(product.getChainproductid(), new StoreMenuProduct(product));
                            }
                        }
                    }
                    DataManager manager = DataManager.getInstance();
                    manager.setOloStoreCategories(categories);

                    final HashMap<Integer, StoreMenuProduct> finalStoreProducts = storeProducts;
                    final OloCategory[] finalStoreCategories = menu.getCategories();
                    StoreService.getStoreCalendarForWeek(callbackWeakReference.get(), storeId,
                            new StoreCalendarCallback() {
                                @Override
                                public void onStoreCalendarCallback(StoreTiming calendar, Exception ex) {
                                    if (calendar != null) {
                                        DataManager.getInstance().setSelectedStoreTiming(calendar);
                                    }
                                    notifyStoreMenuCallback(callbackWeakReference, finalStoreProducts, ex, callback);
                                }
                            });
                } else {
                    notifyStoreMenuCallback(callbackWeakReference, storeProducts, exception, callback);
                }
            }
        });
    }

    private static void notifyStoreMenuCallback(WeakReference<Activity> callbackWeakReference, HashMap<Integer, StoreMenuProduct> storeProducts, Exception exception, final StoreMenuCallback callback) {
        Activity cb = callbackWeakReference.get();
        if (cb != null && !cb.isFinishing()) {
            callback.onStoreMenuCallback(storeProducts, exception);
        }
    }

    public static Product getProductWithProductId(int productId) {
        Product product = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductEntityName);
        query.fromLocalDatastore();
        query.whereEqualTo("productId", productId);
        try {
            ParseObject parseObject = query.getFirst();
            product = new Product(parseObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return product;
    }

    public static ProductCategory getCategoryWithCategoryId(String categoryId) {
        ProductCategory productCategory = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductCategoryEntityName);
        query.fromLocalDatastore();
        query.whereEqualTo("objectId", categoryId);
        try {
            ParseObject parseObject = query.getFirst();
            productCategory = new ProductCategory(parseObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return productCategory;
    }

    public static Product getProductWithProductName(String name) {
        Product product = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductEntityName);
        query.fromLocalDatastore();
        query.whereEqualTo("name", name);
        try {
            ParseObject parseObject = query.getFirst();
            product = new Product(parseObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return product;
    }

    private static void loadDataFromServerIfRequired(LoadCallback callback) {
        long lastPullTime = SharedPreferenceHandler.getInstance().getLong(LastProductUpdate, -1);
        if (System.currentTimeMillis() - lastPullTime > TwentyFourHourInMiliSeconds || lastPullTime == -1) {
            loadDataFromServer(callback);
        } else {
            callback.run();
        }
    }

    /**
     * TODO: uncomment when ads are available , loadAdConfigfromserver & loadAdDetailsfromserver
     *
     * @param callback
     */
//    private static void loadAdConfigFromServer(final LoadCallback callback) {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductAd);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> parseObjects, ParseException e) {
//                try {
//                    if (parseObjects != null) {
//                        ParseObject.unpinAll(ProductAd);
//                        ParseObject.pinAll(ProductAd, parseObjects);
//                        long currentTimeMillis = System.currentTimeMillis();
//                        SharedPreferenceHandler.put(LastProductUpdate, currentTimeMillis);
//                    }
//                    //callback.run();
//                    loadAdDetailsFromServer(callback);
//                } catch (ParseException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private static void loadAdDetailsFromServer(final LoadCallback callback) {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductAdDetail);
//        query.whereEqualTo("status", true);
//        query.orderByAscending("order_no");
//        query.include("category");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> parseObjects, ParseException e) {
//                try {
//                    if (parseObjects != null) {
//                        ParseObject.unpinAll(ProductAdDetail);
//                        ParseObject.pinAll(ProductAdDetail, parseObjects);
//                        long currentTimeMillis = System.currentTimeMillis();
//                        SharedPreferenceHandler.put(LastProductUpdate, currentTimeMillis);
//                    }
//                    callback.run();
//                } catch (ParseException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
//    }
    private static void loadDataFromServer(final LoadCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ProductEntityName);
        query.whereEqualTo("published", true);
        query.include("category");
        query.include("family");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                try {
                    if (parseObjects != null) {
                        ParseObject.unpinAll(ProductEntityName);
                        ParseObject.pinAll(ProductEntityName, parseObjects);
                        long currentTimeMillis = System.currentTimeMillis();
                        SharedPreferenceHandler.put(LastProductUpdate, currentTimeMillis);
                    }
                    //callback.run();
                    /**
                     * TODO : calling loadAdConfigFromServer
                     */
                    //loadAdConfigFromServer(callback);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    abstract static class LoadCallback {
        public abstract void run();
    }

}
