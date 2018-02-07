package com.olo.jambajuice.Activites.NonGeneric.Menu.ProductCategoryDetail;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductSearch.ProductSearchActivity;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductSearch.ProductSearchActivity;
import com.olo.jambajuice.Adapters.ProductCategoryDetailViewPagerAdapter;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.CustomViewPager;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;

import java.util.ArrayList;
import java.util.List;

import static com.olo.jambajuice.Utils.Constants.B_PRODUCT_CATEGORY;
import static com.olo.jambajuice.Utils.Constants.B_PRODUCT_DETAIL_POSITION;

public class ProductCategoryDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    CustomViewPager pager;
    int position;
    int savePosition;
    private ArrayList<ProductCategory> products;

    public static void show(Activity activity, List<ProductCategory> products, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(B_PRODUCT_CATEGORY, new ArrayList<ProductCategory>(products));
        bundle.putInt(B_PRODUCT_DETAIL_POSITION, position);
        TransitionManager.slideUp(activity, ProductCategoryDetailActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_detail);
        setUpToolBar(true);
        isSlideDown = true;
        setUpIntentData();
        if (products != null) {
            ProductCategoryDetailViewPagerAdapter adapter = new ProductCategoryDetailViewPagerAdapter(getSupportFragmentManager(), products);
            pager = (CustomViewPager) findViewById(R.id.viewpager);
            pager.setAdapter(adapter);
            pager.setOffscreenPageLimit(0);// Set number of pages that should be retained to either side of current page.
            pager.setCurrentItem(position);
            pager.setOnPageChangeListener(this);
        }
        //Header click event
        LinearLayout menuStoreChangeHeaderInnerLeft = (LinearLayout) findViewById(R.id.menuStoreChangeHeaderInnerLeft);
        menuStoreChangeHeaderInnerLeft.setOnClickListener(this);

        ImageView imgHeaderProductSearch = (ImageView) findViewById(R.id.imgHeaderProductSearch);
        imgHeaderProductSearch.setOnClickListener(this);

        RelativeLayout headerLogoContainer = (RelativeLayout) findViewById(R.id.headerLogoContainer);
        headerLogoContainer.setOnClickListener(this);

        DataManager manager = DataManager.getInstance();

        Store selectedStore = manager.getCurrentSelectedStore();
        if (selectedStore == null || selectedStore.getName() == null) {
            Utils.showErrorAlert(this, new Exception("Not able to find a store for you. Please try again!"));
            this.finish();
            return;
        }
        com.olo.jambajuice.Views.Generic.SemiBoldTextView headerStoreTitle = (SemiBoldTextView) findViewById(R.id.headerStoreTitle);
        com.olo.jambajuice.Views.Generic.SemiBoldTextView headerStoreLocation = (SemiBoldTextView) findViewById(R.id.headerStoreLocation);
        if(DataManager.getInstance().isDebug){
            headerStoreTitle.setText(Utils.setDemoStoreName(selectedStore).getName().replace("Jamba Juice ", ""));
        }else{
            headerStoreTitle.setText(selectedStore.getName().replace("Jamba Juice ", ""));
        }
//        String location=selectedStore.getCity().concat(" , ").concat(selectedStore.getZip());
        String location = selectedStore.getCompleteAddress();
        headerStoreLocation.setText(location);
    }

    private void setUpIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            products = (ArrayList<ProductCategory>) bundle.getSerializable(B_PRODUCT_CATEGORY);
            position = bundle.getInt(B_PRODUCT_DETAIL_POSITION);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        trackUXEvent("gesture", "ProductCategoryDetailActivity::Swipe");
        if (state >= 0 && state < products.size()) {
            if (savePosition > state) {
                trackUXEvent("swipe_next_category", products.get(state).getName());
            } else {
                trackUXEvent("swipe_prev_category", products.get(state).getName());
            }
        }
        savePosition = state;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuStoreChangeHeaderInnerLeft:
                TransitionManager.slideUp(ProductCategoryDetailActivity.this, StoreLocatorActivity.class);
                break;
            case R.id.imgHeaderProductSearch:
                TransitionManager.transitFrom(ProductCategoryDetailActivity.this, ProductSearchActivity.class);
                break;
            case R.id.headerLogoContainer:
                goToDashBoard();
                break;
        }
    }

    private void goToDashBoard() {
        if (UserService.isUserAuthenticated()) {
            TransitionManager.transitFrom(this, HomeActivity.class, true);
        }
    }

}
