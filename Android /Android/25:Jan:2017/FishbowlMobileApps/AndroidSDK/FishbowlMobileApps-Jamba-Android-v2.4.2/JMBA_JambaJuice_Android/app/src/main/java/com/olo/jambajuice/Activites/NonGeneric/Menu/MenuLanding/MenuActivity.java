package com.olo.jambajuice.Activites.NonGeneric.Menu.MenuLanding;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.BrowseMenu.ProductFamiliesActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductSearch.ProductSearchActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Rewards.MyRewardsAndOfferActivity;
import com.olo.jambajuice.Activites.NonGeneric.Settings.PushNotificationActivity;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductSearch.ProductSearchActivity;
import com.olo.jambajuice.Adapters.FeaturedProductAdapter;
import com.olo.jambajuice.Adapters.RecentOrderAdapter;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductAdsServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductAd;
import com.olo.jambajuice.BusinessLogic.Models.ProductAdDetail;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProduct;
import com.olo.jambajuice.BusinessLogic.Services.ProductService;
import com.olo.jambajuice.BusinessLogic.Services.RecentOrdersService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.CustomViewPager;
import com.olo.jambajuice.Views.Generic.FixedSpeedScroller;
import com.olo.jambajuice.Views.Generic.NexaRustSansBlackTextView;
import com.olo.jambajuice.Views.Generic.OnSwipeTouchListener;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.olo.jambajuice.Views.Generic.SpacesItemDecoration;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation")
public class MenuActivity extends BaseActivity implements View.OnClickListener, ProductServiceCallback, ViewPager.OnPageChangeListener {
    private static final boolean isOffer = false;
    public String e = "";
    public JambaApplication _app;
    public RecyclerView recyclerView;
    public RecyclerView recentOrderRecyclerView;
    ScrollView scrollView;
    List<Product> featuredProducts = new ArrayList<>();
    List<Product> originalFeaturedProducts = new ArrayList<>();
    List<Product> recentProd;
    RecentOrderAdapter adapter;
    FeaturedProductAdapter featuredProductAdapter;
    FeaturedProductAdapter recentOrderedProductAdapter;
    Bundle extras;
    Timer timer;
    int page = 0, posOffsetPixels = 0;
    ArrayList<ProductAdDetail> currentStoreProductAds;
    int adRotationInterval;
    private CustomViewPager pager;
    private CirclePageIndicator circlePageIndicator;
    private String offerId;
    private String custId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        recentProd = new ArrayList<>();
        setUpToolBar(true, true);
        enableBrowseMenuButton(false);
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
                        TransitionManager.slideUp(MenuActivity.this, PushNotificationActivity.class, extras);
                        if (HomeActivity.homeActivity != null) {
                            finish();
                        }
                    }
                }
            }
        }

        setUpView();
//        ProductService.loadFeaturedProducts(this);

        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_UPDATE_RECENT_ORDER));
        _app = (JambaApplication) getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setData();
        if (currentStoreProductAds != null && currentStoreProductAds.size() > 0) {
            if (DataManager.getInstance().getSelectedStoreProductAd() != null && DataManager.getInstance().getSelectedStoreProductAd().size() > 0) {
                adRotationInterval = DataManager.getInstance().getSelectedStoreProductAd().get(0).getRotationInterval();
                Log.d("AdLocationInterval", "Received AdLocationInterval value is " + adRotationInterval);
                if (adRotationInterval <= 0) {
                    adRotationInterval = 10;
                    Log.d("AdLocationInterval", "Default AdLocationInterval applied as 10");
                }
                //Setting time to change ad automatically
                pageSwitcher(adRotationInterval);
            }
        }
        // loadImages();

    }


    private void setData() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        List<Product> products = new ArrayList<>();
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
        String location = selectedStore.getCompleteAddress();

        headerStoreLocation.setText(location);

        if (products != null) {
            enableBrowseMenuButton(true);
            setRecentOrderViewAndAdapter();
            setFeatureProductsViewAdapter();
        }

    }


    //Feature products view adapter
    private void setFeatureProductsViewAdapter() {
        featuredProducts = DataManager.getInstance().getSelectedStoreFeaturedProducts();
        if (featuredProductAdapter == null && featuredProducts != null) {
            featuredProductAdapter = new FeaturedProductAdapter(this, featuredProducts, false);
            recyclerView.setAdapter(featuredProductAdapter);

            if (featuredProducts.size() > 0) {
                setRecyclerViewHeight(featuredProducts.size());
            }

        }

        NexaRustSansBlackTextView noFPText = (NexaRustSansBlackTextView) findViewById(R.id.noFPText);
        if (featuredProducts == null || featuredProducts.size() == 0) {
            noFPText.setVisibility(View.VISIBLE);
        } else {
            noFPText.setVisibility(View.GONE);
        }
        if (featuredProductAdapter != null) {
            featuredProductAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continueBtn:
                TransitionManager.slideUp(MenuActivity.this, ProductFamiliesActivity.class);
                break;
            case R.id.menuStoreChangeHeaderInnerLeft: {

                TransitionManager.slideUp(MenuActivity.this, StoreLocatorActivity.class);
                break;
            }
            case R.id.imgHeaderProductSearch:
                TransitionManager.transitFrom(MenuActivity.this, ProductSearchActivity.class);
                break;

            case R.id.imgHeaderMapIcon1:
                closeAnimation();
                break;
            case R.id.headerLogoContainer:
                goToDashBoard();
                break;

            case R.id.continueShoppingBtn:
                if (HomeActivity.homeActivity != null) {
                    onBackPressed();
                } else {
                    TransitionManager.transitFrom(MenuActivity.this, HomeActivity.class);
                    finish();
                }
                break;

            case R.id.viewOfferBtn:
                TransitionManager.slideUp(MenuActivity.this, MyRewardsAndOfferActivity.class);
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

    private void loadImages() {

        View menuStoreChangeHeaderInnerLeft1 = findViewById(R.id.menuStoreChangeHeader1);


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
        RelativeLayout horLineLayout = (RelativeLayout) findViewById(R.id.horLineLayout);

        if (StringUtilities.isValidString(offerId)) {
            newOfferText.setText("New Offer");
            imgHeaderMapIcon1.setVisibility(View.VISIBLE);
            infoLayout.setVisibility(View.VISIBLE);
            viewOfferBtn.setVisibility(View.VISIBLE);
            dismissBtn.setVisibility(View.GONE);
            horLineLayout.setVisibility(View.VISIBLE);
        } else {
            newOfferText.setText("Jamba Juice");
            imgHeaderMapIcon1.setVisibility(View.GONE);
            infoLayout.setVisibility(View.GONE);
            viewOfferBtn.setVisibility(View.GONE);
            dismissBtn.setVisibility(View.VISIBLE);
            horLineLayout.setVisibility(View.GONE);
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

    private void navigateToNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View menuStoreChangeHeaderInnerLeft1 = findViewById(R.id.menuStoreChangeHeader1);
                menuStoreChangeHeaderInnerLeft1.setVisibility(View.VISIBLE);
                com.olo.jambajuice.Views.Generic.SemiBoldTextView headerOfferTitle = (SemiBoldTextView) findViewById(R.id.headerOfferTitle);
                // com.olo.jambajuice.Views.Generic.SemiBoldTextView headerOfferdescription = (SemiBoldTextView) findViewById(R.id.headerOfferDescription);

                if (e.contains("Promo Code")) {
                    headerOfferTitle.setText(e.split("Promo Code")[0]);
                }


            }
        }, 100000);
    }

    @Override
    public void onProductServiceCallback(List<Product> products, ProductCategory selectedCategory, Exception exception) {

    }

    // Private Methods
    private void setUpView() {
        if (recyclerView == null) {
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recyclerView.setHasFixedSize(true);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new SpacesItemDecoration(false));
        }

        if (recentOrderRecyclerView == null) {
            recentOrderRecyclerView = (RecyclerView) findViewById(R.id.recentOrderRecyclerView);
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recentOrderRecyclerView.setHasFixedSize(true);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            recentOrderRecyclerView.setLayoutManager(layoutManager);
            recentOrderRecyclerView.addItemDecoration(new SpacesItemDecoration(false));
        }
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.fullScroll(ScrollView.FOCUS_UP);

        Button continueShoppingBtn = (Button) findViewById(R.id.continueShoppingBtn);
        continueShoppingBtn.setOnClickListener(this);

        Button continueButton = (Button) findViewById(R.id.continueBtn);
        continueButton.setOnClickListener(this);

        //Header click event
        LinearLayout menuStoreChangeHeaderInnerLeft = (LinearLayout) findViewById(R.id.menuStoreChangeHeaderInnerLeft);
        SemiBoldButton viewofferButton = (SemiBoldButton) findViewById(R.id.viewOfferBtn);
        viewofferButton.setOnClickListener(this);
        SemiBoldButton dismissButton = (SemiBoldButton) findViewById(R.id.dismissBtn);
        dismissButton.setOnClickListener(this);
        //LinearLayout menuStoreChangeHeaderInnerLeft1 = (LinearLayout) findViewById(R.id.menuStoreChangeHeaderInnerLeft1);
        menuStoreChangeHeaderInnerLeft.setOnClickListener(this);
        // menuStoreChangeHeaderInnerLeft1.setOnClickListener(this);

        ImageView imgHeaderProductSearch = (ImageView) findViewById(R.id.imgHeaderProductSearch);
        ImageView imgHeaderMapIcon1 = (ImageView) findViewById(R.id.imgHeaderMapIcon1);
        imgHeaderProductSearch.setOnClickListener(this);
        imgHeaderMapIcon1.setOnClickListener(this);

        RelativeLayout headerLogoContainer = (RelativeLayout) findViewById(R.id.headerLogoContainer);
        headerLogoContainer.setOnClickListener(this);
        setUpViewPager();
    }

    //Advertisement View Pager
    private void setUpViewPager() {
        pager = (CustomViewPager) findViewById(R.id.viewpager);
        // Setting custom scroll speed for Viewpager
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(pager.getContext(), new FastOutLinearInInterpolator());
            // scroller.setFixedDuration(5000);
            mScroller.set(pager, scroller);
        } catch (java.lang.NoSuchFieldException e) {

        } catch (java.lang.IllegalAccessException e) {

        }
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        pager.setOnTouchListener(new OnSwipeTouchListener(this, scrollView, pager));
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);
        circlePageIndicator.setSnap(true);
        pager.setVisibility(View.VISIBLE);
        pager.setPagingEnabled(true);
        pager.addOnPageChangeListener(this);

        circlePageIndicator.setVisibility(View.VISIBLE);

        if (DataManager.getInstance().getSelectedStoreProductAdDetail() != null && DataManager.getInstance().getSelectedStoreProductAdDetail().size() > 0) {
            setViewPagerAdAdapter();
        } else {
            pager.setVisibility(View.GONE);
        }

    }

    //Advertisement pager adapter
    private void setViewPagerAdAdapter() {
        ArrayList<ProductAdDetail> productAds = DataManager.getInstance().getSelectedStoreProductAdDetail();
        if (productAds != null) {
            currentStoreProductAds = new ArrayList<ProductAdDetail>();
            if (productAds != null) {
                for (int i = 0; i < productAds.size(); i++) {
                    {
                        if (productAds.get(i).getStatus()) {
                            currentStoreProductAds.add(productAds.get(i));
                        }
                    }
                }
            }
            if (adapter == null && currentStoreProductAds != null) {
                adapter = new RecentOrderAdapter(getSupportFragmentManager(), currentStoreProductAds);
                pager.setAdapter(adapter);
                circlePageIndicator.setViewPager(pager);
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            if (currentStoreProductAds.size() == 1) {
                circlePageIndicator.setVisibility(View.GONE);
            } else {
                circlePageIndicator.setVisibility(View.GONE);
            }
        }
    }


    //Recent Order view adapter
    private void setRecentOrderViewAndAdapter() {

        ProgressBar roProgressBar = (ProgressBar) findViewById(R.id.recentOrderProgressBar);
        roProgressBar.setVisibility(View.GONE);

        recentProd = DataManager.getInstance().getRecentOrderList();

        if (recentOrderedProductAdapter == null && recentProd != null) {
            recentOrderedProductAdapter = new FeaturedProductAdapter(this, recentProd, true);
            recentOrderRecyclerView.setAdapter(recentOrderedProductAdapter);
        }

        RelativeLayout recentOrderText = (RelativeLayout) findViewById(R.id.recentOrderText);
        NexaRustSansBlackTextView noROText = (NexaRustSansBlackTextView) findViewById(R.id.noROText);
        if (recentProd == null || recentProd.size() == 0) {
            recentOrderText.setVisibility(View.GONE);
            noROText.setVisibility(View.GONE);
            roProgressBar.setVisibility(View.GONE);
            recentOrderRecyclerView.setVisibility(View.GONE);
        }
        if (recentOrderedProductAdapter != null) {
            recentOrderedProductAdapter.notifyDataSetChanged();
        }


    }

//    private void setViewPagerAdapter() {
//        int count = Math.min(2, originalFeaturedProducts.size());
//        featuredProducts.clear();
//        featuredProducts.addAll(originalFeaturedProducts.subList(count, originalFeaturedProducts.size()));
//        List<Product> userProds = RecentOrdersService.getProductsFromRecentOrders();
//
//        if (userProds == null) {
//            userProds = new ArrayList<>();
//        }
//        List<Product> tempProductList = new ArrayList<>();//temp copy for validation
//        tempProductList.addAll(userProds);
//
//        //Remove recent products do not in current store menu
//        for (Product product : tempProductList) {
//            StoreMenuProduct storeMenuProduct = product.getStoreMenuProduct();
//            if (storeMenuProduct == null) {
//                userProds.remove(product);
//            }
//        }
//
//        if (recentProd.size() == 0 && userProds.size() == 0) {
//            recentProd.clear();
//            recentProd.addAll(originalFeaturedProducts.subList(0, count));
//        } else if (userProds.size() > 0) {
//            recentProd.clear();
//            recentProd.addAll(userProds);
//            featuredProducts.clear();
//            featuredProducts.addAll(originalFeaturedProducts);
//        }
//
//        if (adapter == null) {
//            adapter = new RecentOrderAdapter(getSupportFragmentManager(), recentProd);
//            pager.setAdapter(adapter);
//            circlePageIndicator.setViewPager(pager);
//        }
//        adapter.notifyDataSetChanged();
//        if (recentProd.size() == 1) {
//            circlePageIndicator.setVisibility(View.GONE);
//        } else {
//            circlePageIndicator.setVisibility(View.VISIBLE);
//        }
//    }

    private void setRecyclerViewHeight(int size) {
        Display display = getWindowManager().getDefaultDisplay();
        Point windowSize = new Point();
        display.getSize(windowSize);
        int width = windowSize.x;
        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.productThumbAspectRatio, outValue, true);
        float productThumbAspectRatio = outValue.getFloat();
        //featured product height
        int height = (int) ((width / 2) / productThumbAspectRatio);
        int recyclerViewHeight = (int) (Math.ceil(size / 2.0f) * height);
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = recyclerViewHeight;
        recyclerView.setLayoutParams(params);
        recyclerView.requestLayout();

        makeScrollViewToScrollAbove();
    }

    private void makeScrollViewToScrollAbove() {
        final RelativeLayout featuredProductText = (RelativeLayout) findViewById(R.id.featuredProductText);
        final RelativeLayout recentOrderText = (RelativeLayout) findViewById(R.id.recentOrderText);
        Utils.addOnGlobalLayoutListener(featuredProductText, new Runnable() {
            @Override
            public void run() {
                int height = findViewById(R.id.rootView).getHeight();
                RelativeLayout.LayoutParams recyclerViewLayoutParams = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
                int totalHeight = (int) recyclerViewLayoutParams.height;
                int requiredHeight = (int) (height - (pager.getHeight() + featuredProductText.getHeight() + getResources().getDimension(R.dimen.basket_icon_top_margin) + findViewById(R.id.continueBtn).getHeight()));
                if (totalHeight < requiredHeight) {
                    int diff = (int) requiredHeight - totalHeight;
                    recyclerViewLayoutParams.height = totalHeight + diff;
                    recyclerView.setLayoutParams(recyclerViewLayoutParams);
                    recyclerView.requestLayout();
                }
            }
        });

    }

    private void setROViewHeight(int recentOrderSize) {
        Display display = getWindowManager().getDefaultDisplay();
        Point windowSize = new Point();
        display.getSize(windowSize);
        int width = windowSize.x;
        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.productThumbAspectRatio, outValue, true);
        float productThumbAspectRatio = outValue.getFloat();

        //recent order height
        int recentOrderHeight = (int) ((width / 2) / productThumbAspectRatio);
        int recentOrderRecyclerViewHeight = (int) (Math.ceil(recentOrderSize / 2.0f) * recentOrderHeight);
        ViewGroup.LayoutParams recentOrderParams = recentOrderRecyclerView.getLayoutParams();
        recentOrderParams.height = recentOrderRecyclerViewHeight;
        recentOrderRecyclerView.setLayoutParams(recentOrderParams);
        recentOrderRecyclerView.requestLayout();

        makeROScrollViewToScrollAbove();
    }

    private void makeROScrollViewToScrollAbove() {
        final RelativeLayout recentOrderText = (RelativeLayout) findViewById(R.id.recentOrderText);
        Utils.addOnGlobalLayoutListener(recentOrderText, new Runnable() {
            @Override
            public void run() {
                int height = findViewById(R.id.rootView).getHeight();

                RelativeLayout.LayoutParams recentOrderRecyclerViewLayoutParams = (RelativeLayout.LayoutParams) recentOrderRecyclerView.getLayoutParams();

                int roTotalHeight = (int) recentOrderRecyclerViewLayoutParams.height;
                int roRequiredHeight = (int) (height - (pager.getHeight() + recentOrderText.getHeight() + getResources().getDimension(R.dimen.basket_icon_top_margin) + findViewById(R.id.continueBtn).getHeight()));
                if (roTotalHeight < roRequiredHeight) {
                    int diff = (int) roRequiredHeight - roTotalHeight;
                    recentOrderRecyclerViewLayoutParams.height = roTotalHeight + diff;
                    recentOrderRecyclerView.setLayoutParams(recentOrderRecyclerViewLayoutParams);
                    recentOrderRecyclerView.requestLayout();
                }
            }
        });

    }


    private void enableBrowseMenuButton(boolean isEnabled) {
        findViewById(R.id.continueBtn).setEnabled(isEnabled);
    }

    @Override
    protected void handleBroadCastReceiver(Intent intent) {
        setViewPagerAdAdapter();
    }


    @Override
    protected void onResume() {
        if (recyclerView != null) {
            recentProd = new ArrayList<>();
            adapter = null;
            setUpView();
            setData();
        }
        super.onResume();
    }

    public void pageSwitcher(final int seconds) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer(); // At this line a new Thread will be created
                timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
                // in
                // milliseconds
            }
        });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        System.out.println("onPageScrolled called = " + position + " " + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(final int position) {
        System.out.println("onPageSelected called = " + position);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                page = position;
                pageSwitcher(adRotationInterval);
            }
        });

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        System.out.println("onPageScrollStateChanged called = " + state);
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {
                    pager.setCurrentItem(page++, true);

                    if (page == currentStoreProductAds.size()) {
                        page = 0;
                    }
                }
            });

        }
    }
}
