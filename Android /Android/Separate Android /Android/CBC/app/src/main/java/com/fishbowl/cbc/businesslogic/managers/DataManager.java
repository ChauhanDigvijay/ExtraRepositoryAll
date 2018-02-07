package com.fishbowl.cbc.businesslogic.managers;

import com.fishbowl.apps.olo.Models.OloBasket;
import com.fishbowl.apps.olo.Models.OloCategory;
import com.fishbowl.apps.olo.Models.OloOrderInfo;
import com.fishbowl.cbc.businesslogic.models.Basket;
import com.fishbowl.cbc.businesslogic.models.BillingAccount;
import com.fishbowl.cbc.businesslogic.models.OrderStatus;
import com.fishbowl.cbc.businesslogic.models.Product;
import com.fishbowl.cbc.businesslogic.models.ProductCategory;
import com.fishbowl.cbc.businesslogic.models.ProductFamily;
import com.fishbowl.cbc.businesslogic.models.ProductFamilyModel;
import com.fishbowl.cbc.businesslogic.models.Store;
import com.fishbowl.cbc.businesslogic.models.StoreMenuProduct;
import com.fishbowl.cbc.businesslogic.models.StoreMenuProductModifier;
import com.fishbowl.cbc.businesslogic.models.StoreTiming;
import com.fishbowl.cbc.businesslogic.services.ProductService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by VT027 on 4/24/2017.
 */

public class DataManager {

    public static boolean isDebug = true;

    static DataManager sInstance;

    private Basket currentBasket;
    private Store currentSelectedStore;
    private StoreTiming selectedStoreTiming;
    private OloOrderInfo orderInfo;
    private OrderStatus orderStatus;

    private OloCategory[] oloStoreCategories;

    private List<ProductFamily> allProductFamily;
    private ArrayList<BillingAccount> billingAccount;
    private ArrayList<ProductCategory> allCategories;
    private List<ProductFamilyModel> mProductCategoryList;
    private List<ProductFamily> selectedStoreProductFamily;
    private ArrayList<ProductCategory> selectedStoreCategories;
    private List<Product> selectedStoreFeaturedProducts;
    private List<ProductFamily> tempSelectedStoreProductFamily;
    private ArrayList<ProductCategory> tempSelectedStoreCategories;
    private List<Product> tempSelectedStoreFeaturedProducts;

    private HashMap<Integer, StoreMenuProduct> storeMenu;
    private HashMap<Integer, List<StoreMenuProductModifier>> storeMenuProductModifiersList;

    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }


    public OloCategory[] getOloStoreCategories() {
        return oloStoreCategories;
    }

    public void setOloStoreCategories(OloCategory[] oloStoreCategories) {
        this.oloStoreCategories = oloStoreCategories;
    }

    public OloOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OloOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ArrayList<BillingAccount> getBillingAccount() {
        return billingAccount;
    }

    public void setBillingAccount(ArrayList<BillingAccount> billingAccount) {
        this.billingAccount = billingAccount;
    }

    public Basket getCurrentBasket() {
        return currentBasket;
    }

    public void setCurrentBasket(Basket currentBasket) {
        this.currentBasket = currentBasket;
    }

    public Store getCurrentSelectedStore() {
        return currentSelectedStore;
    }

    public void setCurrentSelectedStore(Store currentSelectedStore) {
        this.currentSelectedStore = currentSelectedStore;
    }

    public List<ProductFamily> getAllProductFamily() {
        return allProductFamily;
    }

    public void setAllProductFamily(List<ProductFamily> allProductFamily) {
        this.allProductFamily = allProductFamily;
    }

    public ArrayList<ProductCategory> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(ArrayList<ProductCategory> allCategories) {
        this.allCategories = allCategories;
    }

    public List<ProductFamily> getTempSelectedStoreProductFamily() {
        return tempSelectedStoreProductFamily;
    }

    public void setTempSelectedStoreProductFamily(List<ProductFamily> tempSelectedStoreProductFamily) {
        this.tempSelectedStoreProductFamily = tempSelectedStoreProductFamily;
    }

    public ArrayList<ProductCategory> getTempSelectedStoreCategories() {
        return tempSelectedStoreCategories;
    }

    public void setTempSelectedStoreCategories(ArrayList<ProductCategory> tempSelectedStoreCategories) {
        this.tempSelectedStoreCategories = tempSelectedStoreCategories;
    }


    public List<Product> getTempSelectedStoreFeaturedProducts() {
        return tempSelectedStoreFeaturedProducts;
    }

    public void setTempSelectedStoreFeaturedProducts(List<Product> tempSelectedStoreFeaturedProducts) {
        this.tempSelectedStoreFeaturedProducts = tempSelectedStoreFeaturedProducts;
    }


    public List<ProductFamily> getSelectedStoreProductFamily() {
        return selectedStoreProductFamily;
    }

    public void setSelectedStoreProductFamily(List<ProductFamily> selectedStoreProductFamily) {
        this.selectedStoreProductFamily = selectedStoreProductFamily;
    }

    public ArrayList<ProductCategory> getSelectedStoreCategories() {
        return selectedStoreCategories;
    }

    public void setSelectedStoreCategories(ArrayList<ProductCategory> selectedStoreCategories) {
        this.selectedStoreCategories = selectedStoreCategories;
    }

    public List<Product> getSelectedStoreFeaturedProducts() {
        return selectedStoreFeaturedProducts;
    }

    public void setSelectedStoreFeaturedProducts(List<Product> selectedStoreFeaturedProducts) {
        this.selectedStoreFeaturedProducts = selectedStoreFeaturedProducts;
    }

    public HashMap<Integer, StoreMenuProduct> getStoreMenu() {
        return storeMenu;
    }

    public void setStoreMenu(HashMap<Integer, StoreMenuProduct> storeMenu) {
        this.storeMenu = storeMenu;
    }

    public StoreTiming getSelectedStoreTiming() {
        return selectedStoreTiming;
    }

    public void setSelectedStoreTiming(StoreTiming selectedStoreTiming) {
        this.selectedStoreTiming = selectedStoreTiming;
    }

    public void updateBasket(OloBasket oloBasket) {
        if (this.currentBasket != null) {
            this.currentBasket.updateBasket(oloBasket);
        }
    }

    public void switchTempValuesToMainValues() {
        this.setSelectedStoreProductFamily(this.getTempSelectedStoreProductFamily());
        this.setSelectedStoreCategories(this.getTempSelectedStoreCategories());
        this.setSelectedStoreFeaturedProducts(this.getTempSelectedStoreFeaturedProducts());
//        this.setCurrentSelectedStore(this.getTempCurrentSelectedStore());
//        this.setStoreMenu(this.getTempStoreMenu());
    }

    public Product getParseProductWithProductId(int oloProductId) {
        Product product = null;
        if (storeMenu != null) {
            StoreMenuProduct storeMenuProduct = getStoreMenuProductWithOloProductId(oloProductId);
            if (storeMenuProduct != null) {
                product = ProductService.getProductWithProductId(storeMenuProduct.getChainProductId());
            }
        }
        return product;
    }

    private StoreMenuProduct getStoreMenuProductWithOloProductId(int productId) {
        StoreMenuProduct storeMenuProduct = null;
        if (storeMenu != null) {
            for (Integer key : storeMenu.keySet()) {
                storeMenuProduct = storeMenu.get(key);
                if (storeMenuProduct.getId() == productId) {
                    break;
                }
            }
        }
        return storeMenuProduct;
    }

    public List<StoreMenuProductModifier> getStoreMenuProductModifiers(int productId) {
        if ((storeMenuProductModifiersList != null) && storeMenuProductModifiersList.containsKey(productId)) {
            return storeMenuProductModifiersList.get(productId);
        } else {
            return new ArrayList<StoreMenuProductModifier>();
        }
    }

    public void setStoreMenuProductModifiers(int productId, List<StoreMenuProductModifier> storeMenuProductModifiers) {
        if (this.storeMenuProductModifiersList == null) {
            this.storeMenuProductModifiersList = new HashMap<>();
        }
        List<StoreMenuProductModifier> tempStoreMenuProductModifiers = new ArrayList<>(storeMenuProductModifiers);
        for (StoreMenuProductModifier modifier : storeMenuProductModifiers) {
            if (modifier.isASimpleSizeModifier()) {
                tempStoreMenuProductModifiers.remove(modifier);
            }
        }
        this.storeMenuProductModifiersList.put(productId, tempStoreMenuProductModifiers);
    }

    public List<ProductFamilyModel> getmProductCategoryList() {
        return mProductCategoryList;
    }

    public void setmProductCategoryList(List<ProductFamilyModel> mProductCategoryList) {
        this.mProductCategoryList = mProductCategoryList;
    }

    // Set product modifier against stored product.
    public void setProductModifier(int chainProductId, List<StoreMenuProductModifier> modifiers) {
        StoreMenuProduct product = getProductInStoreWithChainProductId(chainProductId);
        if (product != null && modifiers != null) {
            product.populateStoreMenuProductModifiers(modifiers);
        }
    }

    // Get product model on seleted store by passing Parse product Id(This is Equivalent to chain product Id on Olo).
    public StoreMenuProduct getProductInStoreWithChainProductId(int productId) {
        StoreMenuProduct product = null;
        if (storeMenu != null) {
            product = storeMenu.get(productId);
        }
        return product;
    }
}
