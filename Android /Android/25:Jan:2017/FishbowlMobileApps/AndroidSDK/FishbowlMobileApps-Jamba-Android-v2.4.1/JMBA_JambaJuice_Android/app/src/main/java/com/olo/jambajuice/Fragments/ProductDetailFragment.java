package com.olo.jambajuice.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.koushikdutta.ion.Ion;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail.CustomExpandableListView;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail.ProductCustomiseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail.ProductDetailViewPagerActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail.ProductOption;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketFlagViewManager;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Store.SelectPickUpLocationActivity;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductDetailCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.BasketChoice;
import com.olo.jambajuice.BusinessLogic.Models.BasketProduct;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProduct;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProductModifier;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProductModifierOption;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.StoreService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Interfaces.OloBasketServiceCallback;
import com.wearehathway.apps.olo.Models.OloBasket;
import com.wearehathway.apps.olo.Utils.Logger;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.olo.jambajuice.Utils.Constants.B_BASKET_PRODUCT;
import static com.olo.jambajuice.Utils.Constants.B_PRODUCT;
import static com.olo.jambajuice.Utils.Constants.REQUEST_CODE_ASK_PERMISSIONS;
import static com.olo.jambajuice.Utils.Constants.TOTAL_BASKET_PRODUCTS;

public class ProductDetailFragment extends Fragment implements View.OnClickListener {


    private final int PROD_TYPE_1 = 0;
    private final int PROD_TYPE_2 = 1;
    private final int ADD_QTY = 1;
    private final int SUB_QTY = -1;
    public JambaApplication _app;
    LayoutInflater inflater;
    DataManager manager = DataManager.getInstance();
    SwitchCompat sizesSwitch;
    TextView sizeDescription;
    ProductOption specialInstuction;
    boolean isDataUpdated = false;
    private int currentQuantity = 1;
    // private TextView addedTime;
    // private TextView ingredient2;
    private View view;
    private RelativeLayout chooseStoreContainer;
    private TextView productName;
    private TextView ingredient1;
    private TextView quantity;
    private TextView totalAmount;
    private Button addToBasket;
    private Button continueShoppingBtn;
    private ImageView productImage;
    private ImageView increaseQuantity;
    private ImageView decreaseQuantity;
    private CustomExpandableListView customExpandableListView;
    private Product product;
    private BasketProduct basketProduct;
    private List<WeightsViewHolder> priceAndQuantityViewHolder = new ArrayList<>();
    private List<BoostsViewHolder> boostsViewHolders = new ArrayList<>();
    private List<ProductOption> listDataHeader;
    private HashMap<Integer, List<ProductOption>> listDataChild;
    private List<StoreMenuProductModifierOption> mainParentOption;
    private ArrayList<StoreMenuProductModifierOption> substituteListDataHeader;
    private HashMap<StoreMenuProductModifierOption, ArrayList<StoreMenuProductModifierOption>> substituteListDataChild;
    private ArrayList<StoreMenuProductModifierOption> RemoveListDataChild;

    public static ProductDetailFragment getInstance(Product product, BasketProduct basketProduct) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(B_PRODUCT, product);
        if (basketProduct != null) {
            bundle.putSerializable(B_BASKET_PRODUCT, basketProduct);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ProductDetailFragment getInstance(Product product) {
        return getInstance(product, null);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            resetQuantityIfRequired();
            //hideExpandableListIfRequired();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        getIntentData();
        this.inflater = inflater;
        ImageView productImageGradient = (ImageView) view.findViewById(R.id.productImageGradient);
        BitmapUtils.loadBitmapResourceWithViewSize(productImageGradient, R.drawable.product_detail_gradient, true);
        initComponents();
        setClickListeners();
        setValues();
        setUpBasketButtonState();
        fetchDataIfRequired();
        _app = JambaApplication.getAppContext();
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        return view;
    }

    private void fetchDataIfRequired() {
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        if (manager.getCurrentSelectedStore() != null && storeMenuProduct != null && !storeMenuProduct.isHasPopulatedModifiers()) {
            isDataUpdated = false;
            clearScreenDummyDataAndShowProgress();
            //Fetch Product Modifiers
            StoreService.fetchProductModifiers(getActivity(), storeMenuProduct, new ProductDetailCallback() {
                @Override
                public void onProductDetailCallback(List<StoreMenuProductModifier> storeMenuProductModifiers, Exception exception) {
                    try {
                        if (getActivity() != null) {
                            StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
                            if ((exception == null && storeMenuProduct != null && storeMenuProduct.getProductSizeModifier() != null) || (storeMenuProduct != null && storeMenuProduct.isHasPopulatedModifiers())) {
                                if (storeMenuProduct.getProductModifiers() != null && storeMenuProduct.getProductModifiers().size() > 0) {
                                    //Normal Modifer in Products
                                    DataManager.getInstance().setStoreMenuProductModifiers(product.getProductId(), new ArrayList<StoreMenuProductModifier>(storeMenuProduct.getProductModifiers()));
                                } else if (storeMenuProduct.getProductSizeModifier() != null && storeMenuProduct.getProductSizeModifier().getOptions() != null && storeMenuProduct.getProductSizeModifier().getOptions().size() > 0) {
                                    //Second level Modifier products
                                    boolean isOptionSelected = false;
                                    for (StoreMenuProductModifierOption option : storeMenuProduct.getProductSizeModifier().getOptions()) {
                                        if (option.isSelected() && option.getModifiers() != null) {
                                            isOptionSelected = true;
                                            DataManager.getInstance().setStoreMenuProductModifiers(product.getProductId(), option.getModifiers());
                                            break;
                                        }
                                    }
                                    if (isOptionSelected == false && storeMenuProduct.getProductSizeModifier().getOptions().get(0).getModifiers() != null) {
                                        DataManager.getInstance().setStoreMenuProductModifiers(product.getProductId(), storeMenuProduct.getProductSizeModifier().getOptions().get(0).getModifiers());
                                    }
                                }
                                updateData();
                            } else {
                                showFetchError();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (storeMenuProduct != null && storeMenuProduct.isHasPopulatedModifiers() && !isDataUpdated) {
            if (storeMenuProduct != null && storeMenuProduct.getProductModifiers() != null) {
                DataManager.getInstance().setStoreMenuProductModifiers(product.getProductId(), new ArrayList<StoreMenuProductModifier>(storeMenuProduct.getProductModifiers()));
            } else {
                DataManager.getInstance().setStoreMenuProductModifiers(product.getProductId(), new ArrayList<StoreMenuProductModifier>());
            }
            clearScreenDummyDataAndShowProgress();
            updateData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            fetchDataIfRequired();
            if (isDataUpdated) {
                updatePrice();
            }
            setUpBasketButtonState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  hideExpandableListIfRequired();
    }

//    private void hideExpandableListIfRequired() {
//        if(DataManager.getInstance().getCurrentBasket() == null && view != null)
//        {
//            view.findViewById(R.id.detailView).setVisibility(View.GONE);
//        }
//        else if(DataManager.getInstance().getCurrentBasket() != null && view != null)
//        {
//            view.findViewById(R.id.detailView).setVisibility(View.VISIBLE);
//        }
//    }

    private void resetQuantityIfRequired() {
        if (!isValidToAddMoreProducts()) {
            currentQuantity = 1;
            if (quantity != null) {
                quantity.setText(currentQuantity + "");
                updatePrice();
            }
        }
    }

    private void setValues() {
        quantity.setText(currentQuantity + "");
        productName.setText(product.getName());
        String formatedIngredients = product.getIngredients();
        if (formatedIngredients.equals("")) {
            ingredient1.setVisibility(View.GONE);
        } else {
            ingredient1.setText(formatedIngredients);
        }
        View productPriceQuantityView1 = view.findViewById(R.id.size1);
        View productPriceQuantityView2 = view.findViewById(R.id.size2);
        View productPriceQuantityView3 = view.findViewById(R.id.size3);
        WeightsViewHolder weightsViewHolder1 = new WeightsViewHolder(productPriceQuantityView1);
        WeightsViewHolder weightsViewHolder2 = new WeightsViewHolder(productPriceQuantityView2);
        WeightsViewHolder weightsViewHolder3 = new WeightsViewHolder(productPriceQuantityView3);
        weightsViewHolder1.setView(0, 5, "16 oz");
        weightsViewHolder2.setView(0, 8, "22 oz");
        weightsViewHolder3.setView(0, 10, "28 oz");

        Ion.with(productImage).placeholder(R.drawable.product_placeholder).load(product.getImageUrl());
    }

    private void setUpBasketButtonState() {
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        //RelativeLayout transparentView = (RelativeLayout) view.findViewById(R.id.transparentView);
        if (manager.getCurrentSelectedStore() == null) {
            //If there is no store select change text
            addToBasket.setText("Choose a Store");
            continueShoppingBtn.setVisibility(View.GONE);
            totalAmount.setVisibility(View.GONE);
            chooseStoreContainer.setVisibility(View.VISIBLE);
            //transparentView.setClickable(true);
        } else if (DataManager.getInstance().getCurrentSelectedStore() != null && storeMenuProduct == null) {
            //Show error label: Incase we have store menu but the product is not available on that store
            TextView notavaliable = (TextView) view.findViewById(R.id.chooseStore);
            notavaliable.setText("This product is not available at\nthe selected store");
            notavaliable.setVisibility(View.VISIBLE);
            addToBasket.setText("Choose different Store");
            continueShoppingBtn.setVisibility(View.GONE);
            totalAmount.setVisibility(View.GONE);
            //transparentView.setClickable(false);
        } else {
            addToBasket.setText("Add To Basket");
            continueShoppingBtn.setVisibility(View.VISIBLE);
            totalAmount.setVisibility(View.VISIBLE);
            chooseStoreContainer.setVisibility(View.GONE);
            //transparentView.setClickable(false);
        }
        moveScrollToInitialPosition();
    }

    private void initComponents() {
        productImage = (ImageView) view.findViewById(R.id.productImage);
        //        addedTime = (TextView) view.findViewById(R.id.tv_added_time);
        productName = (TextView) view.findViewById(R.id.tv_product_name);
        ingredient1 = (TextView) view.findViewById(R.id.tv_ingredient_1);
        //        ingredient2 = (TextView) view.findViewById(R.id.tv_ingredient_2);
        quantity = (TextView) view.findViewById(R.id.tv_quantity);
        addToBasket = (Button) view.findViewById(R.id.tv_add_to_basket);
        continueShoppingBtn = (Button) view.findViewById(R.id.continueShoppingBtn);
        totalAmount = (TextView) view.findViewById(R.id.tv_total_amount);
        chooseStoreContainer = (RelativeLayout) view.findViewById(R.id.chooseStoreContainer);
        increaseQuantity = (ImageView) view.findViewById(R.id.btn_increase_qty);
        decreaseQuantity = (ImageView) view.findViewById(R.id.btn_decrease_qty);

        View selectionType = view.findViewById(R.id.selectionType);
        sizeDescription = (TextView) view.findViewById(R.id.description);
        sizesSwitch = (SwitchCompat) selectionType.findViewById(R.id.sizeSwitch);
        sizesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toogleProductTypeOrBoosts();
                updatePrice();
            }
        });
    }

    private void toogleProductTypeOrBoosts() {
        StoreMenuProductModifierOption option = getStoreMenuProductModifierOption();
        if (option != null) {
            setPriceAndSize();
            setModifiersList();
        }
    }

    private void setModifiersList() {

        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();

        if (storeMenuProduct.hasSmoothieTypesOrSecondLevelModifiers()) {
            List<StoreMenuProductModifier> modifierList = storeMenuProduct.getProductSecondLevelModifier(getProductType());
            if (modifierList != null && modifierList.size() > 1) {
                //If modifier list has other than size modifiers
                DataManager.getInstance().setStoreMenuProductModifiers(product.getProductId(), new ArrayList<StoreMenuProductModifier>(modifierList));
                parseModifiers();//overwrite the UI with new modifiers
            }
        }


    }

    private StoreMenuProductModifierOption getStoreMenuProductModifierOption() {
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        StoreMenuProductModifierOption option = null;
        if (storeMenuProduct != null && storeMenuProduct.hasSmoothieTypesOrSecondLevelModifiers()) {
            option = storeMenuProduct.getStoreMenuProductOptionAt(getProductType());
        }
        return option;
    }

    private int getProductType() {
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        if (storeMenuProduct.hasSecondLevelModifiers()) {
            int i = 0;
            StoreMenuProductModifier storeMenuProductModifier = storeMenuProduct.getProductSizeModifier();
            for (StoreMenuProductModifierOption option : storeMenuProductModifier.getOptions()) {
                for (BoostsViewHolder boostsViewHolder : boostsViewHolders) {
                    if (boostsViewHolder.isSelected && boostsViewHolder.optionId == option.getId()) {
                        return i;
                    }
                }
                i++;
            }
            return 0;
        } else {
            // Classic or Make it light
            return !sizesSwitch.isChecked() ? PROD_TYPE_1 : PROD_TYPE_2;
        }
    }

    // Methods to be called after data has been successfully or with error fetched
    private void updateData() {
        setSwitchState();
        addModifierWithSecondLevelSize();
        setPriceAndSize();
        setBoostsAdapter();
        showQuantityAndProductType();
        toogleProductTypeOrBoosts();
        updateDataWithBasket();
        updatePrice();
        moveScrollToInitialPosition();
        isDataUpdated = true;
    }

    // Fill UI with Basket Product When updating product
    private void updateDataWithBasket() {
        if (isUpdatingProduct()) {
            setCurrentQuantity();
            setProductSize();
            setSpecialInstruction();
        }
    }

    private void setCurrentQuantity() {
        currentQuantity = basketProduct.getQuantity() > 0 ? basketProduct.getQuantity() : 1;
        quantity.setText(currentQuantity + "");
    }

    private void setProductSize() {
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        if (storeMenuProduct.isSizeManadatory()) {
            for (BasketChoice choice : basketProduct.getChoices()) {
                boolean isFound = false;
                for (WeightsViewHolder viewHolder : priceAndQuantityViewHolder) {
                    if (viewHolder.sizeId == choice.getOptionid()) {
                        viewHolder.setSelected(true);
                        isFound = true;
                    } else {
                        viewHolder.setSelected(false);
                    }
                }
                if (isFound) {
                    break;
                }
            }
        }
    }

    private void setSpecialInstruction() {
        specialInstuction.setName(basketProduct.getSpecialinstructions());
    }

    private void setSwitchState() {
        if (isUpdatingProduct()) {
            StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
            if (storeMenuProduct.isSmoothieTypesAvailable()) {
                StoreMenuProductModifier storeMenuProductModifier = storeMenuProduct.getProductSizeModifier();
                boolean isFound = false;
                for (BasketChoice choice : basketProduct.getChoices()) {
                    int i = 0;
                    for (StoreMenuProductModifierOption storeMenuProductModifierOption : storeMenuProductModifier.getOptions()) {
                        if (choice.getOptionid() == storeMenuProductModifierOption.getId()) {
                            isFound = true;
                            break;
                        }
                        ++i;
                    }
                    if (isFound) {
                        //Check if product is of other type else switch should be unchecked.
                        if (i == PROD_TYPE_2) {
                            sizesSwitch.setChecked(true);
                        }
                        break;
                    }
                }
            }
        }
    }

    private void parseModifiers() {
        try {
            StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
            if (DataManager.getInstance().getStoreMenuProductModifiers(product.getProductId()) != null && storeMenuProduct != null) {
                List<StoreMenuProductModifier> storeMenuProductModifiers = DataManager.getInstance().getStoreMenuProductModifiers(product.getProductId());
                listDataHeader = new ArrayList<>();
                listDataChild = new HashMap<>();
                int i = 0;
                //First level (Add boost, Add yummy extra,..)
                for (StoreMenuProductModifier modifier : storeMenuProductModifiers) {

                    if (modifier.isCustomizeModifer()) {
                        LinearLayout customize_container = (LinearLayout) view.findViewById(R.id.customize_container);
                        customize_container.setOnClickListener(this);
                        customize_container.setVisibility(View.VISIBLE);
                        continue;
                    }
                    ProductOption headerOption = new ProductOption(modifier.getId(), modifier.getDescription(), 0, false);
                    headerOption.setMinSelect(modifier.getMinSelects());
                    headerOption.setMaxSelect(modifier.getMaxSelects());
                    headerOption.setMinChoiceQuantity(modifier.getMinchoicequantity());
                    headerOption.setMaxChoiceQuantity(modifier.getMaxchoicequantity());
                    headerOption.setMaxAggregateQuantity(modifier.getMaxaggregatequantity());
                    headerOption.setSupportschoicequantities(modifier.isSupportschoicequantities());
                    ArrayList<StoreMenuProductModifierOption> storeMenuProductModifierOptions = modifier.getOptions();

                    if (storeMenuProductModifierOptions != null) {
                        List<ProductOption> productOptions = new ArrayList<>();
                        int j = 0;
                        //Second Level (Choose boost, add juices , add fruits and vegies...)
                        for (StoreMenuProductModifierOption option : storeMenuProductModifierOptions) {
                            int id = option.getId();
                            int maxChoice1 = 0, minChoice = 0, maxAgregate = 0, maxChoiceQty = 0, minChoiceQty = 0;
                            boolean supportschoicequantities = false;
                            boolean isMandatory = false;
                            List<ProductOption> secLevProductOptions = null;
                            if (option.getModifiers() != null && option.getModifiers().size() > 0) {
                                isMandatory = true;
                            }

                            if (option.getModifiers() != null) {
                                List<StoreMenuProductModifier> secLevModifiers = option.getModifiers();
                                secLevProductOptions = new ArrayList<>();
                                for (StoreMenuProductModifier secModifier : secLevModifiers) {
                                    List<StoreMenuProductModifierOption> secLevOptions = secModifier.getOptions();
                                    if (secLevOptions != null) {

                                        //Third level
                                        for (StoreMenuProductModifierOption secLevOption : secLevOptions) {
                                            int secLevId = secLevOption.getId();
                                            boolean secLevIsMandatory = false;
                                            if (secLevOption.getModifiers() != null && secLevOption.getModifiers().size() > 0) {
                                                isMandatory = true;
                                            }

                                            ProductOption grandChildOption = new ProductOption(secLevId, secLevOption.getName(), secLevOption.getCost(), isOptionSelected(secLevId), isMandatory);
                                            maxAgregate = secModifier.getMaxaggregatequantity();
                                            maxChoiceQty = secModifier.getMaxchoicequantity();
                                            minChoiceQty = secModifier.getMinchoicequantity();
                                            maxChoice1 = secModifier.getMaxSelects();
                                            minChoice = secModifier.getMinSelects();
                                            supportschoicequantities = secModifier.isSupportschoicequantities();
                                            secLevProductOptions.add(grandChildOption);
                                        }
                                    }

                                }

                            }

                            ProductOption childOption = null;
                            childOption = new ProductOption(id, option.getName(), option.getCost(), isOptionSelected(id), isMandatory);
                            childOption.setMaxAggregateQuantity(maxAgregate);
                            childOption.setMaxChoiceQuantity(maxChoiceQty);
                            childOption.setMinChoiceQuantity(minChoiceQty);
                            childOption.setMaxSelect(maxChoice1);
                            childOption.setMinSelect(minChoice);
                            childOption.setSupportschoicequantities(supportschoicequantities);
                            childOption.setNestedOptionId(modifier.getOptionIdForUnnestedOption()); // For items with unnecessary nested option
                            childOption.setSubOptions(secLevProductOptions);
                            productOptions.add(childOption);

                            listDataChild.put(i, productOptions);
                            ++j;
                        }
                        listDataHeader.add(i, headerOption);
                        ++i;
                    }
                }

                listDataHeader.add(i, new ProductOption(0, "Add Special Instructions", 0, false));
                List<ProductOption> specialInstructionOptions = new ArrayList<>();
                specialInstuction = new ProductOption(0, "", 0, false);
                specialInstructionOptions.add(specialInstuction);
                listDataChild.put(i, specialInstructionOptions);
                LinearLayout detailView = (LinearLayout) view.findViewById(R.id.detailView);
                this.customExpandableListView = new CustomExpandableListView(getActivity(), this, detailView, listDataHeader, listDataChild);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isOptionSelected(int id) {
        if (isUpdatingProduct()) {
            for (BasketChoice choice : basketProduct.getChoices()) {
                if (choice.getOptionid() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addModifierWithSecondLevelSize() {
        LinearLayout proteinBoostContainer = (LinearLayout) view.findViewById(R.id.proteinBoostContainer);
        proteinBoostContainer.removeAllViews();
        proteinBoostContainer.setVisibility(View.VISIBLE);
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        StoreMenuProductModifier modifier = storeMenuProduct.getProductSizeModifier();
        if (modifier != null && modifier.hasSizeModifierOn2ndLevelButNotMakeItLightChoice()) {
            View header = inflater.inflate(R.layout.row_boost_header, proteinBoostContainer, false);
            TextView headerText = (TextView) header.findViewById(R.id.description);
            headerText.setText(modifier.getDescription());
            header.findViewById(R.id.img_selected).setVisibility(View.GONE);
            proteinBoostContainer.addView(header, 0);
            List<StoreMenuProductModifierOption> options = modifier.getOptions();
            boostsViewHolders.clear();
            if (options != null) {
                int size = options.size();
                for (int i = 0; i < size; i++) {
                    StoreMenuProductModifierOption storeMenuProductModifierOption = options.get(i);
                    View rowBoost = inflater.inflate(R.layout.row_boost, proteinBoostContainer, false);
                    int viewId = storeMenuProductModifierOption.getId();
                    rowBoost.setId(viewId);
                    BoostsViewHolder boostsViewHolder = new BoostsViewHolder(rowBoost);
                    boostsViewHolder.setSelected(false);
                    boostsViewHolder.setOnClickListener(this);
                    boostsViewHolder.setView(viewId, storeMenuProductModifierOption.getCost(), storeMenuProductModifierOption.getName());
                    boostsViewHolders.add(boostsViewHolder);
                    proteinBoostContainer.addView(rowBoost);
                }
                if (size > 0) {
                    boostsViewHolders.get(0).setSelected(true);
                }
            }
        } else {
            proteinBoostContainer.setVisibility(View.GONE);
        }

    }

    private void setPriceAndSize() {
        if (getActivity() == null) {
            return;
        }
        LinearLayout productPriceQuantityContainer = (LinearLayout) view.findViewById(R.id.productPriceQuantityContainer);
        productPriceQuantityContainer.removeAllViews();
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        StoreMenuProductModifier modifier = storeMenuProduct.getProductSizeModifier();
        if (storeMenuProduct.hasSmoothieTypesOrSecondLevelModifiers()) {
            modifier = storeMenuProduct.getProductSizeModifier(getProductType());
        }
        if (modifier == null || modifier.getOptions() == null || modifier.getOptions().size() == 0) {
            double cost = getProductCostIfSizeNotAvaliable();
            //            productPriceQuantityContainer.setVisibility(View.GONE);
            priceAndQuantityViewHolder.clear();
            View productPriceQuantityView = inflater.inflate(R.layout.layout_product_price_quantity, productPriceQuantityContainer, false);
            productPriceQuantityView.findViewById(R.id.margin).setVisibility(View.GONE);
            WeightsViewHolder weightsViewHolder = new WeightsViewHolder(productPriceQuantityView);
            String result = String.format("$%.2f", cost);
            weightsViewHolder.setView(0, 0, result);
            priceAndQuantityViewHolder.add(weightsViewHolder);
            productPriceQuantityContainer.addView(productPriceQuantityView);
            //weightsViewHolder.setSelected(true);
        } else {
            int selectedPriceOption = getSelectedPriceIndex(); //Previously Selected Price Size If Any
            int totalSize = modifier.getOptions().size();
            priceAndQuantityViewHolder.clear();
            for (int i = 0; i < totalSize; i++) {
                StoreMenuProductModifierOption storeMenuProductModifierOption = modifier.getOptions().get(i);
                View productPriceQuantityView = inflater.inflate(R.layout.layout_product_price_quantity, productPriceQuantityContainer, false);
                productPriceQuantityView.setId(i);
                if (i + 1 == totalSize) {
                    productPriceQuantityView.findViewById(R.id.margin).setVisibility(View.GONE);
                }
                WeightsViewHolder weightsViewHolder = new WeightsViewHolder(productPriceQuantityView);
                weightsViewHolder.setView(storeMenuProductModifierOption.getId(), storeMenuProductModifierOption.getCost(), storeMenuProductModifierOption.getName());
                weightsViewHolder.setOnClickListener(this);
                priceAndQuantityViewHolder.add(weightsViewHolder);
                productPriceQuantityContainer.addView(productPriceQuantityView);
            }
            //Initially select first size
//            priceAndQuantityViewHolder.get(selectedPriceOption).setSelected(true);
            if (totalSize > 0) {
                priceAndQuantityViewHolder.get(0).setSelected(true);
            }

        }
    }

    private int getSelectedPriceIndex() {
        int index = 0;
        for (WeightsViewHolder weightsViewHolder : priceAndQuantityViewHolder) {
            if (weightsViewHolder.isSelected) {
                return index;
            }
            index++;
        }
        return index;
    }

    private void showFetchError() {
        View progressBarView = view.findViewById(R.id.progressBarView);
        progressBarView.setVisibility(View.GONE);

        TextView notavaliable = (TextView) view.findViewById(R.id.notavaliable);
        notavaliable.setText("Unable to get product detail.");
        notavaliable.setVisibility(View.VISIBLE);
    }

    private void setBoostsAdapter() {
        parseModifiers();

        //        expListView.setAdapter(listAdapter);
        //        setTotalHeightOfListView();
    }

    private void clearDummyData() {
        //Clear Price and Quantity Container
        LinearLayout productPriceQuantityContainer = (LinearLayout) view.findViewById(R.id.productPriceQuantityContainer);
        productPriceQuantityContainer.removeAllViews();
        priceAndQuantityViewHolder.clear();
        //Hide Quantity Layout
        RelativeLayout quantity_layout = (RelativeLayout) view.findViewById(R.id.quantity_layout);
        quantity_layout.setVisibility(View.GONE);

        //Hide Customize type
        LinearLayout customize_container = (LinearLayout) view.findViewById(R.id.customize_container);
        customize_container.setVisibility(View.GONE);

        //Hide SelectionType
        View selectionType = view.findViewById(R.id.selectionType);
        selectionType.setVisibility(View.GONE);


    }

    private void clearScreenDummyDataAndShowProgress() {
        View progressBarView = view.findViewById(R.id.progressBarView);
        progressBarView.setVisibility(View.VISIBLE);

        TextView notavaliable = (TextView) view.findViewById(R.id.notavaliable);
        notavaliable.setVisibility(View.GONE);

        clearDummyData();
    }

    private void showQuantityAndProductType() {
        View progressBarView = view.findViewById(R.id.progressBarView);
        progressBarView.setVisibility(View.GONE);
        RelativeLayout quantity_layout = (RelativeLayout) view.findViewById(R.id.quantity_layout);
        quantity_layout.setVisibility(View.VISIBLE);
        View selectionType = view.findViewById(R.id.selectionType);
        if (product.getStoreMenuProduct().isSmoothieTypesAvailable()) {
            selectionType.setVisibility(View.VISIBLE);
        } else {
            selectionType.setVisibility(View.GONE);
        }
    }

    //Click Listeners
    private void setClickListeners() {
        addToBasket.setOnClickListener(this);
        increaseQuantity.setOnClickListener(this);
        decreaseQuantity.setOnClickListener(this);
        continueShoppingBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!handleSizeAndPriceClick(v) && !handleBoostClick(v)) {
            switch (v.getId()) {
                case R.id.btn_increase_qty:
                    setQuantity(ADD_QTY);
                    break;
                case R.id.btn_decrease_qty:
                    setQuantity(SUB_QTY);
                    break;
                case R.id.tv_add_to_basket:
                    if (manager.getCurrentBasket() != null) {
                        if (StringUtilities.isValidString(manager.getCurrentBasket().getPromotionCode())
                                || manager.getCurrentBasket().getAppliedRewards().size() > 0) {
                            confirmationAlert(Constants.ADD_PRODUCT_OR_REMOVE_PRODUCT_ALERT_MESSAGE);
                        } else {
                            addToBasket();
                        }
                    } else {
                        addToBasket();
                    }
                    break;
                case R.id.continueShoppingBtn: {
                    BaseActivity baseActivity = (BaseActivity) getActivity();
                    baseActivity.isSlideDown = true;
                    baseActivity.onBackPressed();
                }
                break;
                case R.id.customize_container: {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.B_MODIFIER_PRODUCT_ID, product.getProductId());
                    TransitionManager.slideUp(this.getActivity(), ProductCustomiseActivity.class, bundle);//Open substitution screen
                }
                break;
                default:
                    Logger.i("");
                    break;
            }
        }
    }

    private void confirmationAlert(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setCancelable(false);
        dialog.setTitle("Alert");
        dialog.setMessage(message);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                removeOfferOrRewardAndAddProductToBasket();
            }
        });
        dialog.setNegativeButton("No ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void removeOfferOrRewardAndAddProductToBasket() {
        enableScreen(false);
        if (StringUtilities.isValidString(DataManager.getInstance().getCurrentBasket().getPromotionCode())) {
            BasketService.removeCoupon(new OloBasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(OloBasket basket, Exception error) {
                    enableScreen(true);
                    if (error == null) {
                        DataManager.getInstance().getCurrentBasket().setPromotionCode("");
                        DataManager.getInstance().getCurrentBasket().setPromoId(0);
                        enableScreen(false);
                        addToBasket();
                    } else {
                        Utils.showErrorAlert(getContext(), error);
                    }
                }
            });
        }

        if (DataManager.getInstance().getCurrentBasket().getAppliedRewards().size() > 0) {
            BasketService.removeRewards(new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception e) {
                    enableScreen(true);
                    if (e == null) {
                        enableScreen(false);
                        addToBasket();
                    } else {
                        Utils.showErrorAlert(getContext(), e);
                    }
                }
            });
        }
    }

    private boolean handleBoostClick(View view) {
        int id = view.getId();
        boolean isHandled = false;
        for (BoostsViewHolder boostsViewHolder : boostsViewHolders) {
            if (id == boostsViewHolder.optionId) {
                isHandled = true;
            }
        }
        if (isHandled) {
            for (BoostsViewHolder boostsViewHolder : boostsViewHolders) {
                boostsViewHolder.setSelected(false);
                if (boostsViewHolder.optionId == id) {
                    boostsViewHolder.setSelected(true);
                }
            }
            toogleProductTypeOrBoosts();// Update price Ids
            updatePrice();// update price
        }
        return isHandled;
    }

    private boolean handleSizeAndPriceClick(View view) {
        int id = view.getId();
        if (id >= 0 && id <= 4) {
            for (WeightsViewHolder viewHolder : priceAndQuantityViewHolder) {
                viewHolder.setSelected(false);
            }
            if (id < priceAndQuantityViewHolder.size()) {
                WeightsViewHolder selectedViewHolder = priceAndQuantityViewHolder.get(id);
                selectedViewHolder.setSelected(true);
            }
            updatePrice();
            return true;
        }
        return false;
    }

    private void addToBasketSub() {
        if (addToBasket != null) {
            //AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.UX.value, "button_press", addToBasket.getText().toString(), 0, "ProductDetailFragment");
        }

        JSONObject data = new JSONObject();
        if (product != null) {
            //Track Product is added
            String equal = "";
            try {
                equal = URLDecoder.decode("=", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            if (product.getStoreMenuProduct() != null) {

                int oloProductId = product.getStoreMenuProduct().getId();
                JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(oloProductId)
                        , "PRODUCT_NAME " + equal + " " + product.getName() + ";TOTAL_QUANTITY " + equal + " " + currentQuantity + ";TOTAL_COST " + equal + " " + totalAmount.getText().toString().replace("$", "")
                        , FBEventSettings.ADD_PRODUCT);
            }


        }

        DataManager manager = DataManager.getInstance();
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        if (manager.getCurrentSelectedStore() == null || (manager.getCurrentSelectedStore() != null && storeMenuProduct == null)) {
            chooseAStore();
        } else if (!manager.getCurrentSelectedStore().isSupportsOrderAhead()) {
            showStoreSelectorAlert("Store you have selected does not support orderahead. Please select a different store.");
        } else if (isUpdatingProduct()) {
            verifyAndUpdateProduct();
        } else if (manager.getCurrentBasket() == null) {

            createBasketAndAddProductToBasket();
        } else {
            verifyAndAddProductToBasket();
        }
    }

    //Create Basket and Add Product Methods
    private void addToBasket() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getActivity())) {
                openDrawLayoutSettings(getActivity());
            } else {
                addToBasketSub();
            }
        } else {
            addToBasketSub();
        }

    }

    private void openDrawLayoutSettings(final Activity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("We need your permission to display Basket icon on top of this App. Please grant permission!");
        alertDialogBuilder.setTitle("Basket Permission");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.getPackageName()));
                startActivityForResult(intent, Constants.REQUEST_CODE_ASK_PERMISSIONS);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
        //    }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (Settings.canDrawOverlays(getActivity())) {
                addToBasket();
            }
        }
    }

    private void createBasketAndAddProductToBasket() {
        if (verifyProductDetails()) {
            enableScreen(false);
            BasketService.createBasket(getActivity(), manager.getCurrentSelectedStore().getRestaurantId(), new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception e) {
                    if (e != null) {
                        enableScreen(true);
                        Utils.showErrorAlert(getActivity(), e);
                    } else {
                        BasketFlagViewManager.getInstance().showBasketFlag(getActivity());
                        verifyAndAddProductToBasket();
                    }
                }
            });
        }
    }

    private void verifyAndAddProductToBasket() {
        if (verifyProductDetails()) {
            enableScreen(false);
            //Fill product with values
            final BasketProduct basketProduct = getBasketProduct();
            if (basketProduct.getChoices().size() == 0 && product.getStoreMenuProduct() != null) {
                if (product.getStoreMenuProduct().getProductModifiers() != null
                        && product.getStoreMenuProduct().getProductModifiers().size() > 0
                        && product.getStoreMenuProduct().getProductModifiers().get(0).isMandatory()) {
                    enableScreen(true);
                    //Utils.showErrorAlert(getActivity(), "This item requires customization. Please select a customization to continue.");
                    Utils.showErrorAlert(getActivity(), "Please make a selection for " + "'" + product.getStoreMenuProduct().getProductModifiers().get(0).getDescription() + "'");
                    return;
                }
            }
            BasketService.addProductToBasket(getActivity(), manager.getCurrentBasket().getId(), basketProduct, new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception e) {
                    enableScreen(true);
                    if (e != null) {
                        Utils.showErrorAlert(getActivity(), e);
                    } else {
//                        JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(basketProduct.getProductId())
//                                ,"ProductName = "+basketProduct.getName()+" ProductCount = "+basketProduct.getQuantity()+" Total Cost = "+basketProduct.getTotalcost()
//                                , FBEventSettings.ADD_PRODUCT);

                        //Product has been successfully added to basket now update the flag count.
                        BasketFlagViewManager.getInstance().showBasketFlag(getActivity());
                        BasketFlagViewManager.getInstance().updateCount();
                        resetQuantityIfRequired();
                        //clear all local modifiers
                        DataManager.getInstance().setMainParentOption(product.getProductId(), new ArrayList<StoreMenuProductModifierOption>());
                        DataManager.getInstance().setSubstituteListDataHeader(product.getProductId(), new ArrayList<StoreMenuProductModifierOption>());
                        DataManager.getInstance().setSubstituteListDataChild(product.getProductId(), new HashMap<StoreMenuProductModifierOption, ArrayList<StoreMenuProductModifierOption>>());
                        DataManager.getInstance().setMainParentOption(product.getProductId(), new ArrayList<StoreMenuProductModifierOption>());
                        closeActivity();
                    }
                }
            });
        }
    }

    private void closeActivity() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            activity.onBackPressed();
        }
    }

    private void verifyAndUpdateProduct() {
        if (verifyProductDetails()) {
            enableScreen(false);
            //Fill product with values
            BasketProduct basketProduct = getBasketProduct();
            BasketService.updateProduct(getActivity(), manager.getCurrentBasket().getId(), basketProduct, new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception e) {
                    enableScreen(true);
                    if (e != null) {
                        Utils.showErrorAlert(getActivity(), e);
                    } else {
                        //Product has been successfully added to basket now update the flag count.
                        BasketFlagViewManager.getInstance().updateCount();
                        Utils.showAlert(getActivity(), product.getName() + " has been successfully updated.");
                    }
                }
            });
        }
    }

    private BasketProduct getBasketProduct() {
        //Fill product with values
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        BasketProduct basketProd = new BasketProduct();
        basketProd.setProductId(storeMenuProduct.getId());
        basketProd.setQuantity(currentQuantity);
        basketProd.setChoices(getSelectedOptionsIdAsString());

        if (isUpdatingProduct()) {
            basketProd.setId(this.basketProduct.getId());
        }
        return basketProd;
    }

    private boolean verifyProductDetails() {
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        if (storeMenuProduct != null && !storeMenuProduct.isHasPopulatedModifiers()) {
            showTryAgainError();
            return false;
        } else if (storeMenuProduct == null || (storeMenuProduct.getProductSizeModifier() == null && storeMenuProduct.isSizeManadatory())) {
            Utils.showAlert(getActivity(), "Unable to get product detail");
            return false;
        } else if (storeMenuProduct.isSizeManadatory() && !isSizeSelected()) {
            Utils.showAlert(getActivity(), "Please select a valid size.");
            return false;
        } else if (!isValidToAddMoreProducts()) {
            showMaxItemsReachedError();
            return false;
        } else if (!checkMaxAndMinSelects()) {
            return false;
        }
        return true;
    }

    private boolean checkMaxAndMinSelects() {
        if (listDataHeader != null) {
            for (int i = 0; i < listDataHeader.size(); i++) {
                ProductOption option = listDataHeader.get(i);
                int maxAllowed = option.getMaxSelect();
                int minAllowed = option.getMinSelect();
                int selected = 0;
                List<ProductOption> childOptions = listDataChild.get(i);
                for (ProductOption child : childOptions) {
                    if (child.getSubOptions() != null && child.getSubOptions().size() > 0) {
                        maxAllowed = child.getMaxSelect();
                        minAllowed = child.getMinSelect();
                        selected = 0;
                        for (ProductOption grandChildOption : child.getSubOptions()) {
                            if (grandChildOption.isSelected()) {
                                ++selected;
                            }
                            if (maxAllowed > 0 && selected > maxAllowed) {
                                Utils.showAlert(getActivity(), "Choose up to " + maxAllowed + " options for '" + option.getName() + "'");
                                return false;
                            } else if (minAllowed > 0 && selected < minAllowed) {
                                Utils.showAlert(getActivity(), "Choose at least " + minAllowed + " options for '" + option.getName() + "'");
                                return false;
                            }
                        }
                    } else {
                        if (child.isSelected()) {
                            ++selected;
                        }
                        if (maxAllowed > 0 && selected > maxAllowed) {
                            Utils.showAlert(getActivity(), "Choose up to " + maxAllowed + " options for '" + option.getName() + "'");
                            return false;
                        } else if (minAllowed > 0 && selected < minAllowed) {
                            Utils.showAlert(getActivity(), "Choose at least " + minAllowed + " options for '" + option.getName() + "'");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean checkMaxChoiceQuantity(int id, int childPos) {
        if (listDataHeader != null) {
            ProductOption option = listDataHeader.get(id);
            int maxAllowed = option.getMaxAggregateQuantity();
            int selected = 0;
            List<ProductOption> childOptions = listDataChild.get(id);

            ProductOption child = childOptions.get(childPos);
            if (child.getSubOptions() != null && child.getSubOptions().size() > 0) {
                maxAllowed = child.getMaxAggregateQuantity();
                for (ProductOption grandChildOption : child.getSubOptions()) {
                    if (grandChildOption.isSelected()) {
                        selected += grandChildOption.getQuantity();
                    }
                    if (maxAllowed > 0 && selected > maxAllowed) {
                        Utils.showAlert(getActivity(), "Choose Ingredients up to " + maxAllowed + " quantity for '" + option.getName() + "'");

                        return false;
                    }
                }
            } else {
                //Two level
                for (ProductOption childOption : childOptions) {
                    if (childOption.isSelected()) {
                        selected += childOption.getQuantity();
                    }
                    if (maxAllowed > 0 && selected > maxAllowed) {
                        Utils.showAlert(getActivity(), "Choose Ingredients up to " + maxAllowed + " quantity for '" + option.getName() + "'");

                        return false;
                    }
                }
            }
            return true;
        }
        return true;
    }

    public boolean isMoreQuantitySelectionAllowed(int id, int childPos) {
        if (listDataHeader != null) {
            ProductOption option = listDataHeader.get(id);
            int maxAllowed = option.getMaxAggregateQuantity();
            int selected = 0;
            List<ProductOption> childOptions = listDataChild.get(id);

            ProductOption child = childOptions.get(childPos);
            if (child.getSubOptions() != null && child.getSubOptions().size() > 0) {
                maxAllowed = child.getMaxAggregateQuantity();
                for (ProductOption grandChildOption : child.getSubOptions()) {
                    if (grandChildOption.isSelected()) {
                        selected += grandChildOption.getQuantity();
                    }
                    if (maxAllowed > 0 && selected == maxAllowed) {
                        Utils.showAlert(getActivity(), "Choose Ingrediants up to " + maxAllowed + " quantity for '" + option.getName() + "'");
                        return false;
                    }
                }
            } else {
                //Two level
                for (ProductOption childOption : childOptions) {
                    if (childOption.isSelected()) {
                        selected += childOption.getQuantity();
                    }
                    if (maxAllowed > 0 && selected == maxAllowed) {
                        Utils.showAlert(getActivity(), "Choose Ingrediants up to " + maxAllowed + " quantity for '" + option.getName() + "'");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public int getSpinnerLength(int id, int childPos) {
        if (listDataHeader != null) {
            ProductOption option = listDataHeader.get(id);
            int maxAllowed = option.getMaxChoiceQuantity();
            List<ProductOption> childOptions = listDataChild.get(id);
            ProductOption child = childOptions.get(childPos);
            if (child.getSubOptions() != null && child.getSubOptions().size() > 0) {
                maxAllowed = child.getMaxChoiceQuantity();
                if (maxAllowed > 0) {
                    return maxAllowed;
                } else {
                    return 1;
                }
            } else {
                if (maxAllowed > 0) {
                    return maxAllowed;
                } else {
                    return 1;
                }
            }
        }
        return 1;
    }

    public boolean isSpinnerAvailable(int id, int childPos) {
        if (listDataHeader != null) {
            ProductOption option = listDataHeader.get(id);
            boolean isAvailable = option.isSupportschoicequantities();
            List<ProductOption> childOptions = listDataChild.get(id);
            ProductOption child = childOptions.get(childPos);
            if (child.getSubOptions() != null && child.getSubOptions().size() > 0) {
                isAvailable = child.isSupportschoicequantities();
                return isAvailable;
            } else {
                return isAvailable;
            }
        }
        return false;
    }

//
//    public boolean isMoreOptionSelectionAllowed(int id){
//        if (listDataHeader != null) {
//            ProductOption option = listDataHeader.get(id);
//            int maxAllowed = option.getMaxSelect();
//            int selected = 0;
//            List<ProductOption> childOptions = listDataChild.get(id);
//            for (ProductOption child : childOptions) {
//                if (child.isSelected()) {
//                    ++selected;
//                }
//                if (maxAllowed > 0 && selected >= maxAllowed) {
//                    Utils.showAlert(getActivity(), "Choose up to " + maxAllowed + " options for '" + option.getName() + "'");
//                    return false;
//                }
//            }
//            return true;
//        }
//        return false;
//    }


    public boolean isMoreOptionSelectionAllowed(int id, ProductOption clickedChildParent) {
        if (listDataHeader != null) {
            ProductOption option = listDataHeader.get(id);
            int maxAllowed = option.getMaxSelect();
            int selected = 0;
            List<ProductOption> childOptions = listDataChild.get(id);

            for (ProductOption child : childOptions) {
                if (child.getSubOptions() != null && child.getSubOptions().size() > 0) {
                    maxAllowed = child.getMaxSelect();
                    for (ProductOption grandChildOption : child.getSubOptions()) {
                        if (grandChildOption.isSelected()) {
                            ++selected;
                        }
                        if (maxAllowed > 0 && selected >= maxAllowed) {
//                            if(clickedChildParent.getName().equals(child.getName())) {
                            Utils.showAlert(getActivity(), "Choose up to " + maxAllowed + " options for '" + option.getName() + "'");
                            return false;
//                            }
                        }
                    }
                } else {
                    //Two level
                    if (child.isSelected()) {
                        ++selected;
                    }
                    if (maxAllowed > 0 && selected >= maxAllowed) {
                        Utils.showAlert(getActivity(), "Choose up to " + maxAllowed + " options for '" + option.getName() + "'");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean isValidToAddMoreProducts() {
        Basket basket = DataManager.getInstance().getCurrentBasket();
        int count = 0;
        if (basket != null) {
            count = basket.totalProductsCount();
            if ((count + currentQuantity) <= TOTAL_BASKET_PRODUCTS) {
                return true;
            } else {
                return false;
            }
        }

        if(currentQuantity <= TOTAL_BASKET_PRODUCTS){
            return true;
        }else {
            return false;
        }
    }

    private void showTryAgainError() {
        String error = "Unable to get product detail. Do you want to retry?";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(error);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                fetchDataIfRequired();
            }
        });
        alertDialogBuilder.setNegativeButton("No", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private List<BasketChoice> getSelectedOptionsIdAsString() {
        int selectedSize = getSelectedSize();
        List<BasketChoice> options = new ArrayList<>();
        BasketChoice basketChoice;
        if (selectedSize > 0) {
            basketChoice = new BasketChoice();
            basketChoice.setId(selectedSize);
            basketChoice.setQuantity(1);
            options.add(basketChoice);
            int typeId = getCurrentSelectedTypeId();
            if (typeId > 0) {
                basketChoice = new BasketChoice();
                basketChoice.setId(typeId);
                basketChoice.setQuantity(1);
                options.add(basketChoice);
            }
        }

        if (listDataChild != null && listDataChild.size() > 0) {
            for (Integer key : listDataChild.keySet()) {
                List<ProductOption> productOption = listDataChild.get(key);
                for (ProductOption option : productOption) {
                    int id = option.getId();
                    if (option.isSelected()) {

                        if (id > 0) {
                            basketChoice = new BasketChoice();
                            basketChoice.setId(id);
                            basketChoice.setQuantity(option.getQuantity());
                            options.add(basketChoice);
                            if (option.getNestedOptionId() > 0 && !options.contains(option.getNestedOptionId() + ""))//Do not add this nested option id multiple time.
                            {
                                basketChoice = new BasketChoice();
                                basketChoice.setId(option.getNestedOptionId());
                                basketChoice.setQuantity(1);
                                options.add(basketChoice);
                            }
                        }
                    } else if (option.getSubOptions() != null) {
                        Boolean childIsSelected = false;
                        for (ProductOption subOption : option.getSubOptions()) {
                            if (subOption.isSelected()) {
                                int sid = subOption.getId();
                                if (sid > 0) {
                                    childIsSelected = true;
                                    basketChoice = new BasketChoice();
                                    basketChoice.setId(sid);
                                    basketChoice.setQuantity(subOption.getQuantity());
                                    options.add(basketChoice);
                                }
                            }
                        }
                        if (childIsSelected) {
                            basketChoice = new BasketChoice();
                            basketChoice.setId(id);
                            basketChoice.setQuantity(option.getQuantity());
                            options.add(basketChoice);
                        }
                    }
                }
            }
        }
        mainParentOption = DataManager.getInstance().getMainParentOption(product.getProductId());
        substituteListDataHeader = DataManager.getInstance().getSubstituteListDataHeader(product.getProductId());
        substituteListDataChild = DataManager.getInstance().getSubstituteListDataChild(product.getProductId());
        RemoveListDataChild = DataManager.getInstance().getRemoveListDataChild(product.getProductId());


//        if (mainParentOption != null && mainParentOption.size() > 0) {
//
//            options += "," + mainParentOption.get(0).getId();
//        }

        if (substituteListDataChild != null) {
            for (StoreMenuProductModifierOption headOption : substituteListDataChild.keySet()) {
                if (headOption.isSelected()) {
                    int id = headOption.getId();
                    basketChoice = new BasketChoice();
                    basketChoice.setId(id);
                    basketChoice.setQuantity(1);
                    options.add(basketChoice);
                    for (int i = 0; i < substituteListDataChild.get(headOption).size(); i++) {
                        if (substituteListDataChild.get(headOption).get(i).isSelected()) {
                            if (mainParentOption != null && mainParentOption.size() > 0 && !options.contains(String.valueOf(mainParentOption.get(0).getId()))) {
                                basketChoice = new BasketChoice();
                                basketChoice.setId(mainParentOption.get(0).getId());
                                basketChoice.setQuantity(1);
                                options.add(basketChoice);
                            }
                            basketChoice = new BasketChoice();
                            basketChoice.setId(substituteListDataChild.get(headOption).get(i).getId());
                            basketChoice.setQuantity(1);
                            options.add(basketChoice);
                        }

                    }
                }
            }
        }

        if (RemoveListDataChild != null) {
            for (int i = 0; i < RemoveListDataChild.size(); i++) {
                if (RemoveListDataChild.get(i).isSelected()) {
                    if (mainParentOption != null && mainParentOption.size() > 0 && !options.contains(String.valueOf(mainParentOption.get(0).getId()))) {
                        basketChoice = new BasketChoice();
                        basketChoice.setId(mainParentOption.get(0).getId());
                        basketChoice.setQuantity(1);
                        options.add(basketChoice);
                    }
                    basketChoice = new BasketChoice();
                    basketChoice.setId(RemoveListDataChild.get(i).getId());

                    basketChoice.setQuantity(1);
                    options.add(basketChoice);
                }
            }
        }

        return options;
    }

    private int getCurrentSelectedTypeId() {
        int typeId = 0;
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        if (storeMenuProduct.isSmoothieTypesAvailable()) {
            StoreMenuProductModifierOption option = null;
            if (!sizesSwitch.isChecked()) {
                option = storeMenuProduct.getStoreMenuProductOptionAt(PROD_TYPE_1);
            } else {
                option = storeMenuProduct.getStoreMenuProductOptionAt(PROD_TYPE_2);
            }
            if (option != null) {
                typeId = option.getId();
            }
        } else if (storeMenuProduct.hasSecondLevelModifiers()) {
            for (BoostsViewHolder boostsViewHolder : boostsViewHolders) {
                if (boostsViewHolder.isSelected) {
                    typeId = boostsViewHolder.optionId;
                }
            }
        }
        return typeId;
    }

    private String getSpecialInstructions() {

        {
            if (specialInstuction != null)
                return specialInstuction.getName();
        }

        return " ";
    }

    private int getSelectedSize() {
        int sizeSelected = 0;
        for (WeightsViewHolder weightsViewHolder : priceAndQuantityViewHolder) {
            if (weightsViewHolder.isSelected) {
                sizeSelected = weightsViewHolder.sizeId;
                break;
            }
        }
        return sizeSelected;
    }

    private boolean isSizeSelected() {
        return getSelectedSize() > 0;
    }

    //Choose A Store.
    private void chooseAStore() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.B_IS_CHOOOSE_STORE_FROM_PROD_DETAIL, true);
        bundle.putSerializable(B_PRODUCT, product);
        if (UserService.isUserAuthenticated() && UserService.getUser().isPreferredStoreSupportsOrderAHead()) {
            //If user preferred store support order ahead allow user to pickup from that store.
            TransitionManager.startActivity(getActivity(), SelectPickUpLocationActivity.class, bundle);
        } else {
            TransitionManager.slideUp(getActivity(), StoreLocatorActivity.class, bundle);
        }
    }

    private void showStoreSelectorAlert(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("Select A Store");
        alertDialogBuilder.setNegativeButton("Cancel", null);
        alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                chooseAStore();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //Calculate Product Total Price and Display
    private void setQuantity(int value) {
        switch (value) {
            case ADD_QTY:
                currentQuantity += value;
                if (!isValidToAddMoreProducts()) {
                    currentQuantity = currentQuantity - 1;
                    showMaxItemsReachedError();
                }
                break;
            case SUB_QTY:
                currentQuantity += value;
                if (currentQuantity <= 1) {
                    currentQuantity = 1;
                }
                break;
        }

        quantity.setText(currentQuantity + "");

        //JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(product.getProductId()), product.getName(), FBEventSettings.MODIFY_PRODUCT);

        updatePrice();
    }

    private void showMaxItemsReachedError() {
        Utils.showAlert(getActivity(), "Can not add more than " + TOTAL_BASKET_PRODUCTS + " items in your cart per order.");
    }

    public void updatePrice() {
        float singleProductPrice = getSizeCost() + getOptionsCost() + getProductCostIfSizeNotAvaliable() + getCustomizeOptionsCost();
        float total = currentQuantity * singleProductPrice;
        String result = Utils.formatPrice(total);
        totalAmount.setText(result);


    }

    private void moveScrollToInitialPosition() {
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
    }

    private float getSizeCost() {
        float sizeCost = 0;
        for (WeightsViewHolder weightsViewHolder : priceAndQuantityViewHolder) {
            if (weightsViewHolder.isSelected) {
                sizeCost = weightsViewHolder.cost;
                break;
            }
        }
        return sizeCost;
    }

    private float getProductCostIfSizeNotAvaliable() {
        StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
        if (storeMenuProduct != null && !storeMenuProduct.isSizeManadatory() && storeMenuProduct.isHasPopulatedModifiers()) {
            return storeMenuProduct.getProductCost();
        }
        return 0;
    }

    private float getOptionsCost() {

        float optionsCost = 0;
        if (listDataChild != null) {
            for (Integer key : listDataChild.keySet()) {
                List<ProductOption> productOptions = listDataChild.get(key);
                if (productOptions != null) {
                    for (ProductOption option : productOptions) {
                        if (option.isSelected()) {
                            optionsCost += option.getCost() * option.getQuantity();
                            if (listDataHeader != null && key < listDataHeader.size() && !listDataHeader.get(key).getName().contains("Add Boosts")) {
                                DataManager.getInstance().setModifiersId(option.getId());//maintains all modifers expcest Add boosts
                            }
                        } else {
                            List<ProductOption> subProductOptionList = option.getSubOptions();
                            if (subProductOptionList != null) {
                                for (ProductOption subProductOption : subProductOptionList) {
                                    if (subProductOption.isSelected()) {
                                        optionsCost += subProductOption.getCost() * subProductOption.getQuantity();
                                        if (listDataHeader != null && key < listDataHeader.size() && !listDataHeader.get(key).getName().contains("Add Boosts")) {
                                            DataManager.getInstance().setModifiersId(subProductOption.getId());//maintains all modifers expcest Add boosts
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return optionsCost;
    }

    private float getCustomizeOptionsCost() {
        substituteListDataHeader = DataManager.getInstance().getSubstituteListDataHeader(product.getProductId());
        substituteListDataChild = DataManager.getInstance().getSubstituteListDataChild(product.getProductId());
        RemoveListDataChild = DataManager.getInstance().getRemoveListDataChild(product.getProductId());
        float optionsCost = 0;

        if (substituteListDataChild != null) {
            for (StoreMenuProductModifierOption headOption : substituteListDataChild.keySet()) {
                if (headOption.isSelected()) {
                    for (int i = 0; i < substituteListDataChild.get(headOption).size(); i++) {
                        if (substituteListDataChild.get(headOption).get(i).isSelected()) {
                            optionsCost += substituteListDataChild.get(headOption).get(i).getCost();
                            DataManager.getInstance().setModifiersId(substituteListDataChild.get(headOption).get(i).getId());
                        }
                    }
                }
            }
        }

        if (RemoveListDataChild != null) {
            for (int i = 0; i < RemoveListDataChild.size(); i++) {
                if (RemoveListDataChild.get(i).isSelected()) {
                    optionsCost += RemoveListDataChild.get(i).getCost();
                    DataManager.getInstance().setModifiersId(RemoveListDataChild.get(i).getId());
                }
            }
        }
        return optionsCost;
    }

    private void getIntentData() {
        Bundle bundle = getArguments();
        product = (Product) bundle.getSerializable(B_PRODUCT);
        basketProduct = (BasketProduct) bundle.getSerializable(B_BASKET_PRODUCT);
    }

    private void enableScreen(boolean isEnabled) {
        ProductDetailViewPagerActivity activity = (ProductDetailViewPagerActivity) getActivity();
        if (activity != null) {
            activity.isBackButtonEnabled = isEnabled;
            activity.enablePager(isEnabled);
            RelativeLayout screenDisableView = (RelativeLayout) view.findViewById(R.id.screenDisableView);
            if (!isEnabled) {
                screenDisableView.setVisibility(View.VISIBLE);
            } else {
                screenDisableView.setVisibility(View.GONE);
            }
        }
    }

    private boolean isUpdatingProduct() {
        return basketProduct != null;
    }

    private static class WeightsViewHolder {
        int sizeId;
        float cost;
        boolean isSelected;
        private View view;
        private TextView price;
        private TextView weight;

        public WeightsViewHolder(View v) {
            view = v;
            price = (TextView) view.findViewById(R.id.tv_price);
            weight = (TextView) view.findViewById(R.id.tv_weight);
        }

        public void setView(int sizeId, float price, String weight) {
            this.sizeId = sizeId;
            this.cost = price;
            if (price == 0) {
                this.price.setVisibility(View.INVISIBLE);
            }
            String amountString = String.format("$%.2f", cost);
            this.price.setText(amountString);
            this.weight.setText(weight);
        }

        public View getView() {
            return view;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
            view.setSelected(isSelected);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            view.setOnClickListener(listener);
        }
    }

    private static class BoostsViewHolder {
        int optionId;
        float cost;
        ImageView img_selected;
        boolean isSelected;
        RelativeLayout modifierQuantityLayout;
        TextView tv_price_quantity;
        private View view;
        private TextView price;
        private TextView name;

        public BoostsViewHolder(View v) {
            view = v;
            price = (TextView) view.findViewById(R.id.tv_price);
            name = (TextView) view.findViewById(R.id.tv_boost_detail);
            img_selected = (ImageView) view.findViewById(R.id.img_selected);
            modifierQuantityLayout = (RelativeLayout) view.findViewById(R.id.modifierQuantityLayout);
            tv_price_quantity = (TextView) view.findViewById(R.id.tv_price_quantity);
            modifierQuantityLayout.setVisibility(View.GONE);
            tv_price_quantity.setVisibility(View.GONE);
        }

        public void setView(int optionId, float cost, String name) {
            this.optionId = optionId;
            this.cost = cost;
            this.name.setText(name);
            String amountString = String.format("$%.2f", cost);
            this.price.setText(amountString);
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
            if (isSelected) {
                img_selected.setVisibility(View.VISIBLE);
            } else {
                img_selected.setVisibility(View.GONE);
            }
        }

        public void setOnClickListener(View.OnClickListener listener) {
            view.setOnClickListener(listener);
        }

    }
}
