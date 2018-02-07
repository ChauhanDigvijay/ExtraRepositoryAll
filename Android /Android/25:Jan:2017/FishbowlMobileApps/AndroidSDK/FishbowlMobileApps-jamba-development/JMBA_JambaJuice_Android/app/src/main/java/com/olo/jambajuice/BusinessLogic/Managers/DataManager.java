package com.olo.jambajuice.BusinessLogic.Managers;

import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketFlagViewManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.BillingAccount;
import com.olo.jambajuice.BusinessLogic.Models.DeliveryAddress;
import com.olo.jambajuice.BusinessLogic.Models.OrderStatus;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductAd;
import com.olo.jambajuice.BusinessLogic.Models.ProductAdDetail;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.BusinessLogic.Models.ProductFamily;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProduct;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProductModifier;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProductModifierOption;
import com.olo.jambajuice.BusinessLogic.Models.StoreTiming;
import com.olo.jambajuice.BusinessLogic.Models.UpSell;
import com.olo.jambajuice.BusinessLogic.Models.UpsellConfig;
import com.olo.jambajuice.BusinessLogic.Services.ProductService;
import com.wearehathway.apps.olo.Models.OloBasket;
import com.wearehathway.apps.olo.Models.OloCategory;
import com.wearehathway.apps.olo.Models.OloOrderInfo;
import com.wearehathway.apps.olo.Models.OloRestaurant;
import com.wearehathway.apps.olo.Models.OloUpsellGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nauman Afzaal on 14/05/15.
 */
public class DataManager {
    // Development Flags
    public static boolean isDebug = true;
    public static boolean showDemoStore = true;
    public static boolean showDemoLabStore = true;

    static DataManager instance;
    public int groupHeight = 0;
    public int childHeight = 0;
    public ArrayList<Integer> modifiersId;
    private Basket currentBasket;
    private Store currentSelectedStore;
    private Store tempCurrentSelectedStore;
    private StoreTiming selectedStoreTiming;
    private HashMap<Integer, StoreMenuProduct> storeMenu;
    private HashMap<Integer, StoreMenuProduct> tempStoreMenu;
    private OloOrderInfo orderInfo;
    private OrderStatus orderStatus;
    private ArrayList<BillingAccount> billingAccount;
    private ArrayList<DeliveryAddress> deliveryAddresses;
    private OloCategory[] oloStoreCategories;
    private List<ProductFamily> allProductFamily;
    private ArrayList<ProductCategory> allCategories;
    private List<ProductFamily> selectedStoreProductFamily;
    private ArrayList<ProductCategory> selectedStoreCategories;
    private List<Product> selectedStoreFeaturedProducts;
    private List<ProductFamily> tempSelectedStoreProductFamily;
    private ArrayList<ProductCategory> tempSelectedStoreCategories;
    private List<Product> tempSelectedStoreFeaturedProducts;
    private ArrayList<ProductAd> tempSelectedStoreProductAd;
    private ArrayList<ProductAd> selectedStoreProductAd;
    private ArrayList<ProductAdDetail> selectedStoreProductAdDetail;
    private List<StoreMenuProductModifier> storeMenuProductModifiers;
    private List<Product> recentOrderList;
    private HashMap<Integer, List<StoreMenuProductModifier>> storeMenuProductModifiersList;
    private HashMap<Integer, List<StoreMenuProductModifierOption>> mainParentOption;
    private HashMap<Integer, ArrayList<StoreMenuProductModifierOption>> substituteListDataHeader;
    private HashMap<Integer, HashMap<StoreMenuProductModifierOption, ArrayList<StoreMenuProductModifierOption>>> substituteListDataChild;
    private HashMap<Integer, ArrayList<StoreMenuProductModifierOption>> RemoveListDataChild;
    private Boolean isAlreadyShownUpdateScreen = false;
    private OloRestaurant[] allStores;
    private List<RecentOrder> newRecentOrder;
    private Boolean fromFavorite = false;
    private boolean currentBasketSupportGiftCard=false;
    private String inCommToken;
    private boolean orderCancelFlag;
    private String lastCheckedDateAndtime;
    private int loginCount;
    private ArrayList<OloUpsellGroup> oloUpsellGroups;
    private ArrayList<UpsellConfig> upsellConfigs;
    private ArrayList<UpSell> upSells;

    private DataManager() {
        substituteListDataHeader = new HashMap<>();
        substituteListDataChild = new HashMap<>();
        RemoveListDataChild = new HashMap<>();
        mainParentOption = new HashMap<>();
        modifiersId = new ArrayList<>();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public OloRestaurant[] getAllStores() {
        return allStores;
    }

    public void setAllStores(OloRestaurant[] allStores) {
        this.allStores = allStores;
    }

    public void resetDataManager() {
        currentBasket = null;
        currentSelectedStore = null;
        selectedStoreTiming = null;
        storeMenu = null;
        orderInfo = null;
        orderStatus = null;
        billingAccount = null;
        currentBasketSupportGiftCard=false;
        inCommToken = null;
        BasketFlagViewManager.getInstance().removeBasketFlag();
    }

    public void resetBasket() {
        currentBasket = null;
        orderInfo = null;
        orderStatus = null;
        billingAccount = null;
        currentBasketSupportGiftCard=false;
        BasketFlagViewManager.getInstance().removeBasketFlag();
        clearModifiersId();
    }

    // Basket Related Data Methods
    public Basket getCurrentBasket() {
        return currentBasket;
    }

    public void setCurrentBasket(Basket currentBasket) {
        this.currentBasket = currentBasket;
    }

    public boolean isBasketPresent() {
        return currentBasket != null && currentBasket.totalProductsCount() > 0;
    }

    public void updateBasket(OloBasket oloBasket) {
        if (this.currentBasket != null) {
            this.currentBasket.updateBasket(oloBasket);
        }
    }

    public Store getCurrentSelectedStore() {
        return currentSelectedStore;
    }

    public void setCurrentSelectedStore(Store currentSelectedStore) {
        this.currentSelectedStore = currentSelectedStore;
        this.currentSelectedStore.setStoreTiming(selectedStoreTiming);
    }

    public HashMap<Integer, StoreMenuProduct> getStoreMenu() {
        return storeMenu;
    }

    // Key = Chain Product Id
    // Value = Product
    public void setStoreMenu(HashMap<Integer, StoreMenuProduct> storeMenu) {
        this.storeMenu = storeMenu;
    }

    public Store getTempCurrentSelectedStore() {
        return tempCurrentSelectedStore;
    }

    public void setTempCurrentSelectedStore(Store tempCurrentSelectedStore) {
        this.tempCurrentSelectedStore = tempCurrentSelectedStore;
    }

    public HashMap<Integer, StoreMenuProduct> getTempStoreMenu() {
        return tempStoreMenu;
    }

    public void setTempStoreMenu(HashMap<Integer, StoreMenuProduct> tempStoreMenu) {
        this.tempStoreMenu = tempStoreMenu;
    }

    // Check if product is available on store before selecting store.
    public boolean isProductAvailableOnStore(HashMap<Integer, StoreMenuProduct> menu, int productId) {
        return menu != null && menu.get(productId) != null;
    }

    // Get product model on seleted store by passing Parse product Id(This is Equivalent to chain product Id on Olo).
    public StoreMenuProduct getProductInStoreWithChainProductId(int productId) {
        StoreMenuProduct product = null;
        if (storeMenu != null) {
            product = storeMenu.get(productId);
        }
        return product;
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

    // Set product modifier against stored product.
    public void setProductModifier(int chainProductId, List<StoreMenuProductModifier> modifiers) {
        StoreMenuProduct product = getProductInStoreWithChainProductId(chainProductId);
        if (product != null && modifiers != null) {
            product.populateStoreMenuProductModifiers(modifiers);
        }
    }

    public StoreTiming getSelectedStoreTiming() {
        return selectedStoreTiming;
    }

    public void setSelectedStoreTiming(StoreTiming selectedStoreTiming) {
        this.selectedStoreTiming = selectedStoreTiming;
    }

    public OloOrderInfo getOrderInfo() {
        if (orderInfo == null) {
            orderInfo = new OloOrderInfo();
            if (currentBasket != null) {
                orderInfo.setBasketId(currentBasket.getId());
            }
        }
        return orderInfo;
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

    public OloCategory[] getOloStoreCategories() {
        return oloStoreCategories;
    }

    public void setOloStoreCategories(OloCategory[] oloStoreCategories) {
        this.oloStoreCategories = oloStoreCategories;
    }

    public List<Product> getSelectedStoreFeaturedProducts() {
        return selectedStoreFeaturedProducts;
    }

    public void setSelectedStoreFeaturedProducts(List<Product> selectedStoreFeaturedProducts) {
        this.selectedStoreFeaturedProducts = selectedStoreFeaturedProducts;
    }

    public List<StoreMenuProductModifier> getStoreMenuProductModifiers(int productId) {
        if ((storeMenuProductModifiersList !=null) && storeMenuProductModifiersList.containsKey(productId)) {
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

    public ArrayList<StoreMenuProductModifierOption> getRemoveListDataChild(int productId) {
        if (RemoveListDataChild.get(productId) == null) {
            return new ArrayList<>();
        } else {
            return RemoveListDataChild.get(productId);
        }
    }

    public HashMap<StoreMenuProductModifierOption, ArrayList<StoreMenuProductModifierOption>> getSubstituteListDataChild(int productId) {
        if (substituteListDataChild.get(productId) == null) {
            return new HashMap<>();
        } else {
            return substituteListDataChild.get(productId);
        }
    }

    public ArrayList<StoreMenuProductModifierOption> getSubstituteListDataHeader(int productId) {
        if (substituteListDataHeader.get(productId) == null) {
            return new ArrayList<>();
        } else {
            return substituteListDataHeader.get(productId);
        }
    }

    public List<StoreMenuProductModifierOption> getMainParentOption(int productId) {
        if (mainParentOption.get(productId) == null) {
            return new ArrayList<>();
        } else {
            return mainParentOption.get(productId);
        }
    }

    public void setRemoveListDataChild(Integer productId, ArrayList<StoreMenuProductModifierOption> removeListDataChild) {
        if (RemoveListDataChild == null) {
            this.RemoveListDataChild.put(productId, new ArrayList<StoreMenuProductModifierOption>());
        } else {
            this.RemoveListDataChild.put(productId, removeListDataChild);

        }
    }

    public void setSubstituteListDataChild(Integer productId, HashMap<StoreMenuProductModifierOption, ArrayList<StoreMenuProductModifierOption>> substituteListDataChild) {
        if (substituteListDataChild == null) {
            this.substituteListDataChild.put(productId, new HashMap<StoreMenuProductModifierOption, ArrayList<StoreMenuProductModifierOption>>());
        } else {
            this.substituteListDataChild.put(productId, substituteListDataChild);

        }
    }

    public void setSubstituteListDataHeader(int productId, ArrayList<StoreMenuProductModifierOption> substituteListDataHeader) {
        if (substituteListDataHeader == null) {
            this.substituteListDataHeader.put(productId, new ArrayList<StoreMenuProductModifierOption>());
        } else {
            this.substituteListDataHeader.put(productId, substituteListDataHeader);
        }
    }

    public void setMainParentOption(int productId, List<StoreMenuProductModifierOption> mainParentOption) {
        if (mainParentOption == null) {
            this.mainParentOption.put(productId, new ArrayList<StoreMenuProductModifierOption>());
        } else {
            this.mainParentOption.put(productId, mainParentOption);
        }
    }

    public ArrayList<Integer> getModifiersId() {
        return modifiersId;
    }

    public void setModifiersId(Integer id) {
        if (id > 0) {
            if (modifiersId != null) {
                if (!modifiersId.contains(id)) {
                    modifiersId.add(id);
                }
            } else {
                modifiersId = new ArrayList<>();
                modifiersId.add(id);
            }
        }
    }

    public void clearModifiersId() {
        modifiersId = new ArrayList<>();
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

    public ArrayList<ProductAd> getTempSelectedStoreProductAd() {
        return tempSelectedStoreProductAd;
    }

    public void setTempSelectedStoreProductAd(ArrayList<ProductAd> tempSelectedStoreProductAd) {
        this.tempSelectedStoreProductAd = tempSelectedStoreProductAd;
    }

    public ArrayList<ProductAd> getSelectedStoreProductAd() {
        return selectedStoreProductAd;
    }

    public void setSelectedStoreProductAd(ArrayList<ProductAd> selectedStoreProductAd) {
        this.selectedStoreProductAd = selectedStoreProductAd;
    }

    public ArrayList<ProductAdDetail> getSelectedStoreProductAdDetail() {
        return selectedStoreProductAdDetail;
    }

    public void setSelectedStoreProductAdDetail(ArrayList<ProductAdDetail> selectedStoreProductAdDetail) {
        this.selectedStoreProductAdDetail = selectedStoreProductAdDetail;
    }

    public List<Product> getRecentOrderList() {
        return recentOrderList;
    }

    public void setRecentOrderList(List<Product> recentOrderList) {
        this.recentOrderList = recentOrderList;
    }

    public void switchTempValuesToMainValues() {
        this.setSelectedStoreProductFamily(this.getTempSelectedStoreProductFamily());
        this.setSelectedStoreCategories(this.getTempSelectedStoreCategories());
        this.setSelectedStoreFeaturedProducts(this.getTempSelectedStoreFeaturedProducts());
//        this.setCurrentSelectedStore(this.getTempCurrentSelectedStore());
//        this.setStoreMenu(this.getTempStoreMenu());
    }

    public Boolean getAlreadyShownUpdateScreen() {
        return isAlreadyShownUpdateScreen;
    }

    public void setAlreadyShownUpdateScreen(Boolean alreadyShownUpdateScreen) {
        isAlreadyShownUpdateScreen = alreadyShownUpdateScreen;
    }

    public ArrayList<DeliveryAddress> getDeliveryAddresses() {
        return deliveryAddresses;
    }

    public void setDeliveryAddresses(ArrayList<DeliveryAddress> deliveryAddresses) {
        this.deliveryAddresses = deliveryAddresses;
    }

    public List<RecentOrder> getNewRecentOrder() {
        return newRecentOrder;
    }

    public void setNewRecentOrder(List<RecentOrder> newRecentOrder) {
        this.newRecentOrder = newRecentOrder;
    }

    public Boolean getFromFavorite() {
        return fromFavorite;
    }

    public void setFromFavorite(Boolean fromFavorite) {
        this.fromFavorite = fromFavorite;
    }

    public boolean isCurrentBasketSupportGiftCard() {
        return currentBasketSupportGiftCard;
    }

    public void setCurrentBasketSupportGiftCard(boolean currentBasketSupportGiftCard) {
        this.currentBasketSupportGiftCard = currentBasketSupportGiftCard;
    }

    public String getInCommToken() {
        return inCommToken;
    }

    public void setInCommToken(String inCommToken) {
        this.inCommToken = inCommToken;
    }

    public boolean isOrderCancelFlag() {
        return orderCancelFlag;
    }

    public void setOrderCancelFlag(boolean orderCancelFlag) {
        this.orderCancelFlag = orderCancelFlag;
    }

    public String getLastCheckedDateAndtime() {
        return lastCheckedDateAndtime;
    }

    public void setLastCheckedDateAndtime(String lastCheckedDateAndtime) {
        this.lastCheckedDateAndtime = lastCheckedDateAndtime;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public ArrayList<OloUpsellGroup> getOloUpsellGroups() {
        return oloUpsellGroups;
    }

    public void setOloUpsellGroups(ArrayList<OloUpsellGroup> oloUpsellGroups) {
        this.oloUpsellGroups = oloUpsellGroups;
    }

    public ArrayList<UpsellConfig> getUpsellConfigs() {
        return upsellConfigs;
    }

    public void setUpsellConfigs(ArrayList<UpsellConfig> upsellConfigs) {
        this.upsellConfigs = upsellConfigs;
    }

    public ArrayList<UpSell> getUpSells() {
        return upSells;
    }

    public void setUpSells(ArrayList<UpSell> upSells) {
        this.upSells = upSells;
    }
}
