package com.olo.jambajuice.Activites.NonGeneric.Menu.BrowseMenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.MenuLanding.MenuActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductSearch.ProductSearchActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards.MyRewardsAndOfferActivity;
import com.olo.jambajuice.Activites.NonGeneric.Settings.PushNotificationActivity;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductSearch.ProductSearchActivity;
import com.olo.jambajuice.Adapters.ProductFamiliesAdapter;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductCategoryServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductFamilyServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.BusinessLogic.Models.ProductFamily;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Services.ProductService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.wearehathway.apps.olo.Models.OloCategory;

import java.util.ArrayList;
import java.util.List;

public class ProductFamiliesActivity extends BaseActivity implements ProductCategoryServiceCallback, ProductFamilyServiceCallback, ProductServiceCallback, View.OnClickListener {

    public String e = "";
    Bundle extras;
    private ListView categoriesListview;
    private ProductFamiliesAdapter adapter;
    private List<ProductCategory> productCategoryList;
    private List<ProductFamily> productFamilyList;
    private int productServiceQueue = 0;
    private String offerId;
    private String custId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_families);
        //setUpToolBar(true, true);
        //setTitle("Full Menu");

        Intent i = getIntent();
        extras = i.getExtras();
        {
            if (extras != null) {

                this.e = extras.getString("Title");
                this.offerId = extras.getString("offerId");
                this.custId = extras.getString("custId");


            }
        }

        if (StringUtilities.isValidString(e)) {
            if (StringUtilities.isValidString(custId)) {
                if (FBSdk.sharedInstance(this).getFBSdkData() != null
                        && FBSdk.sharedInstance(this).getFBSdkData().getCurrCustomer() != null) {
                    if (FBSdk.sharedInstance(this).getFBSdkData().getCurrCustomer().getCustomerID() == Long.parseLong(custId)) {
                        Bundle extras = new Bundle();
                        extras.putString("Title", e);
                        extras.putString("offerId", offerId);
                        extras.putString("custId", custId);
                        TransitionManager.slideUp(ProductFamiliesActivity.this, PushNotificationActivity.class, extras);
                        if (HomeActivity.homeActivity != null) {
                            finish();
                        }
                    }
                }
            }
        }

        initUIComponents();
        setAdapter();

        setData();
//        registerCallbacks();
    }

    @Override
    protected void onResume() {
        if (categoriesListview != null) {
            productFamilyList = new ArrayList<>();
            adapter = null;
            initUIComponents();
            setAdapter();
            setData();
        }
        super.onResume();
    }

    private void startAnimationAfterDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, 500);
    }

    private void startAnimation() {

        View menuStoreChangeHeaderInnerLeft1 = findViewById(R.id.menuStoreChangeHeader1);
        TextView newOfferText = (TextView) findViewById(R.id.newOfferText);
        ImageButton imgHeaderMapIcon1 = (ImageButton) findViewById(R.id.imgHeaderMapIcon1);
        RelativeLayout infoLayout = (RelativeLayout) findViewById(R.id.infoLayout);
        SemiBoldButton viewOfferBtn = (SemiBoldButton) findViewById(R.id.viewOfferBtn);
        SemiBoldButton dismissBtn = (SemiBoldButton) findViewById(R.id.dismissBtn);

        if (StringUtilities.isValidString(offerId)) {
            newOfferText.setText("New Offer");
            imgHeaderMapIcon1.setVisibility(View.VISIBLE);
            infoLayout.setVisibility(View.VISIBLE);
            viewOfferBtn.setVisibility(View.VISIBLE);
            dismissBtn.setVisibility(View.GONE);
        } else {
            newOfferText.setText("Jamba Juice");
            imgHeaderMapIcon1.setVisibility(View.GONE);
            infoLayout.setVisibility(View.GONE);
            viewOfferBtn.setVisibility(View.GONE);
            dismissBtn.setVisibility(View.VISIBLE);
        }


        JambaApplication appContext = JambaApplication.getAppContext();
        Animation animdown = AnimationUtils.loadAnimation(appContext, R.anim.slide_up_activity);

        animdown.setFillAfter(true);

        animdown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                com.olo.jambajuice.Views.Generic.SemiBoldTextView headerOfferTitle = (SemiBoldTextView) findViewById(R.id.headerOfferTitle);
                headerOfferTitle.setText(e);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //navigateToNextScreen();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        menuStoreChangeHeaderInnerLeft1.setVisibility(View.VISIBLE);

        menuStoreChangeHeaderInnerLeft1.startAnimation(animdown);

    }


    private void navigateToNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View menuStoreChangeHeaderInnerLeft1 = findViewById(R.id.menuStoreChangeHeader1);
                menuStoreChangeHeaderInnerLeft1.setVisibility(View.VISIBLE);
                com.olo.jambajuice.Views.Generic.SemiBoldTextView headerOfferTitle = (SemiBoldTextView) findViewById(R.id.headerOfferTitle);
                //   com.olo.jambajuice.Views.Generic.SemiBoldTextView headerOfferdescription = (SemiBoldTextView) findViewById(R.id.headerOfferDescription);

                if (e.contains("Promo Code")) {
                    headerOfferTitle.setText(e.split("Promo Code")[0]);
                }

            }
        }, 100000);
    }

    private void closeAnimation() {

        View menuStoreChangeHeaderInnerLeft1 = findViewById(R.id.menuStoreChangeHeader1);
        JambaApplication appContext = JambaApplication.getAppContext();
        Animation animdown = AnimationUtils.loadAnimation(appContext, R.anim.slide_down_activity);

        animdown.setFillAfter(true);

        animdown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //navigateToNextScreen();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        menuStoreChangeHeaderInnerLeft1.setVisibility(View.INVISIBLE);
        this.e = "";
        menuStoreChangeHeaderInnerLeft1.startAnimation(animdown);


    }

    private void setData() {

        DataManager manager = DataManager.getInstance();

        Store selectedStore = manager.getCurrentSelectedStore();
        if (selectedStore == null || selectedStore.getName() == null) {
            Utils.showErrorAlert(this, new Exception("Not able to find a store for you. Please try again!"));
            this.finish();
            return;
        }
        com.olo.jambajuice.Views.Generic.SemiBoldTextView headerStoreTitle = (SemiBoldTextView) findViewById(R.id.headerStoreTitle);
        com.olo.jambajuice.Views.Generic.SemiBoldTextView headerStoreLocation = (SemiBoldTextView) findViewById(R.id.headerStoreLocation);
        if (DataManager.getInstance().isDebug) {
            headerStoreTitle.setText(Utils.setDemoStoreName(selectedStore).getName().replace("Jamba Juice ", ""));
        } else {
            headerStoreTitle.setText(selectedStore.getName().replace("Jamba Juice ", ""));
        }

//        String location=selectedStore.getCity().concat(" , ").concat(selectedStore.getZip());
        String location = selectedStore.getCompleteAddress();
        headerStoreLocation.setText(location);

        this.productFamilyList = manager.getSelectedStoreProductFamily();
        this.productCategoryList = manager.getSelectedStoreCategories();

        List<ProductFamily> mData = new ArrayList<ProductFamily>();

        if (productFamilyList != null && productCategoryList != null) {
            for (ProductFamily productFamily : productFamilyList) {
                mData.add(productFamily);
                boolean isSubCategoryExist = false;
                for (int i = 0; i < productCategoryList.size(); i++) {
                    ProductCategory productCategory = productCategoryList.get(i);
                    if (productFamily.getObjectID().equals(productCategory.getFamily())) {
                        mData.add(productCategory);
                        isSubCategoryExist = true;
                    }
                }
                if (!isSubCategoryExist) {
                    mData.remove(productFamily);
                }
            }

            adapter.addData(mData);
            adapter.setAllCategories(productCategoryList);
        }

        if (StringUtilities.isValidString(e)) {

            startAnimationAfterDelay();
        }
    }

    private void initUIComponents() {
        final FloatingActionButton scrollTopFab = (FloatingActionButton) findViewById(R.id.scrollTopFab);
        scrollTopFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoriesListview.smoothScrollToPosition(0);
            }
        });

        if (categoriesListview == null) {
            categoriesListview = (ListView) findViewById(R.id.listView_categories);
            categoriesListview.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    //adjustToolBar();
                    if(firstVisibleItem > 0)
                    {
                        scrollTopFab.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        scrollTopFab.setVisibility(View.GONE);
                    }
                }
            });
        }

        //Header click event
        LinearLayout menuStoreChangeHeaderInnerLeft = (LinearLayout) findViewById(R.id.menuStoreChangeHeaderInnerLeft);
        menuStoreChangeHeaderInnerLeft.setOnClickListener(this);

        SemiBoldButton viewofferBtn = (SemiBoldButton) findViewById(R.id.viewOfferBtn);
        viewofferBtn.setOnClickListener(this);

        SemiBoldButton dismissButton = (SemiBoldButton) findViewById(R.id.dismissBtn);
        dismissButton.setOnClickListener(this);

        RelativeLayout headerLogoContainer = (RelativeLayout) findViewById(R.id.headerLogoContainer);
        headerLogoContainer.setOnClickListener(this);

        ImageView imgHeaderProductSearch = (ImageView) findViewById(R.id.imgHeaderProductSearch);
        imgHeaderProductSearch.setOnClickListener(this);

        Button continueShoppingBtn = (Button) findViewById(R.id.continueShoppingBtn);
        continueShoppingBtn.setOnClickListener(this);

    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new ProductFamiliesAdapter(this);
            categoriesListview.setAdapter(adapter);
        }
    }

    private void registerCallbacks() {
        ProductService.loadAllProductFamilies(this);
    }

    @Override
    public void onProductFamilyCallback(List<ProductFamily> productFamily, Exception exception) {
        if (exception == null) {
            this.productFamilyList = productFamily;
            ProductService.loadAllProductCategories(this);
        }
    }


    @Override
    public void onProductCategoryCallback(List<ProductCategory> productCategories, Exception exception) {
        if (exception == null) {
//            DataManager manager= DataManager.getInstance();
//            OloCategory[]  storeCategories =manager.getStoreCategories();
//            List<ProductCategory> finalProductCategories=new ArrayList<>();
//            for(ProductCategory parseCategory:productCategories){
//                boolean isExist=false;
//                for(OloCategory oloCategory:storeCategories){
//                    if(oloCategory.getName().equalsIgnoreCase(parseCategory.getFamily())){
//                        finalProductCategories.add(parseCategory);
//                        break;
//                    }
//                }
//            }

            this.productCategoryList = new ArrayList<ProductCategory>();

            productServiceQueue = productCategories.size();
            for (ProductCategory parseCategory : productCategories) {
                DataManager manager = DataManager.getInstance();
                OloCategory[] oloCatList = manager.getOloStoreCategories();
                for (OloCategory cat : oloCatList) {
                    if (String.valueOf(cat.getId()).equals(parseCategory.getCategoryId())) {
                        this.productCategoryList.add(parseCategory);
                        break;
                    }
                }
                ProductService.loadProductsForCategory(this, parseCategory, this);

            }
        }
    }

    @Override
    public void onProductServiceCallback(List<Product> products, ProductCategory requestProductCategory, Exception exception) {

        if (productServiceQueue > 1) {
            if (products == null || products.size() == 0) {
                if (this.productCategoryList.contains(requestProductCategory)) {
                    this.productCategoryList.remove(requestProductCategory);
                }
            }
            productServiceQueue = productServiceQueue - 1;
        } else {
            List<ProductFamily> mData = new ArrayList<ProductFamily>();
            int count = 0;
            for (ProductFamily productFamily : productFamilyList) {
                mData.add(productFamily);
                for (int i = count; i < productCategoryList.size(); i++) {
                    ProductCategory productCategory = productCategoryList.get(i);
                    if (productFamily.getObjectID().equals(productCategory.getFamily())) {
                        mData.add(productCategory);
                    } else {
                        count = i;
                        break;
                    }
                }
            }
            adapter.addData(mData);
            adapter.setAllCategories(productCategoryList);
        }

    }

    // Show Hide Toolbar
    private void adjustToolBar() {
        if (categoriesListview.getFirstVisiblePosition() == 0) {
            hideToolbar();
        } else {
            showToolbar();
        }
    }

    private void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    private void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuStoreChangeHeaderInnerLeft:
                TransitionManager.slideUp(ProductFamiliesActivity.this, StoreLocatorActivity.class);
                break;
            case R.id.headerLogoContainer:
                goToDashBoard();
                break;
            case R.id.imgHeaderProductSearch:
                TransitionManager.transitFrom(ProductFamiliesActivity.this, ProductSearchActivity.class);
                break;
            case R.id.imgHeaderMapIcon1:
                closeAnimation();
                break;

            case R.id.continueShoppingBtn:
                onBackPressed();
                break;

            case R.id.viewOfferBtn:
                TransitionManager.slideUp(ProductFamiliesActivity.this, MyRewardsAndOfferActivity.class);
                break;

            case R.id.dismissBtn:
                closeAnimation();
                break;
        }
    }

    private void goToDashBoard() {
        if (UserService.isUserAuthenticated()) {
            TransitionManager.transitFrom(this, HomeActivity.class, true);
        }
    }
}
