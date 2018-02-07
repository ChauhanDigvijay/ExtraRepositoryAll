package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.MenuLanding.MenuActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.PaymentInfo.AddCardActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.PaymentInfo.SelectExistingCardActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.PaymentInfo.UserInfoActivity;
import com.olo.jambajuice.Adapters.ProductSearchAdapter;
import com.olo.jambajuice.Adapters.RecentOrderAdapter;
import com.olo.jambajuice.Adapters.UpSellAdapter;
import com.olo.jambajuice.Adapters.UpSellImageAdapter;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.BillingAccountsCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.UpSellServiceCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.UpsellConfigServiceCallBack;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.BillingAccount;
import com.olo.jambajuice.BusinessLogic.Models.ProductAdDetail;
import com.olo.jambajuice.BusinessLogic.Models.UpSell;
import com.olo.jambajuice.BusinessLogic.Models.UpsellConfig;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.ProductService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.CustomViewPager;
import com.olo.jambajuice.Views.Generic.FixedSpeedScroller;
import com.olo.jambajuice.Views.Generic.OnSwipeTouchListener;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;
import com.viewpagerindicator.CirclePageIndicator;
import com.wearehathway.apps.olo.Models.OloUpsellItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class UpsellActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    RecyclerView upSellRecyclerView;
    UpSellAdapter upSellAdapter;
    ArrayList<OloUpsellItems> oloUpsellItemList;
    SemiBoldButton addToBasket, noThanks;
    Context context;
    CustomViewPager upSellAdsPager;
    Timer timer;
    int adRotationInterval;
    int page = 0;
    ArrayList<UpSell> currentUpSells;
    UpSellImageAdapter adapter;
    int upSellSelectedCount = 0;
    public static UpsellActivity upsellActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upsell);

        context = this;
        upsellActivity = this;

        oloUpsellItemList = new ArrayList<>();

        addToBasket = (SemiBoldButton) findViewById(R.id.addToBasket);
        noThanks = (SemiBoldButton) findViewById(R.id.noThanks);
        addToBasket.setOnClickListener(this);
        noThanks.setOnClickListener(this);

        initToolbar();
        if (DataManager.getInstance().getOloUpsellGroups().get(0).getUpsellitems() != null
                && DataManager.getInstance().getOloUpsellGroups().get(0).getUpsellitems().size() > 0) {
            for (int i = 0; i < DataManager.getInstance().getOloUpsellGroups().get(0).getUpsellitems().size(); i++) {
                OloUpsellItems oloUpsellItems = new OloUpsellItems(DataManager.getInstance().getOloUpsellGroups().get(0).getUpsellitems().get(i));
                oloUpsellItemList.add(oloUpsellItems);
            }
            setAdapter();
        }

        setImages();

    }

    private void setImages() {
        loadUpsellConfig();
    }

    private void loadUpsellConfig() {
        enableScreen(false);
        ProductService.loadUpSellConfig(this, new UpsellConfigServiceCallBack() {
            @Override
            public void onUpSellConfigServiceCallBack(ArrayList<UpsellConfig> upSellConfigs, Exception exception) {
                enableScreen(true);
                DataManager.getInstance().setUpsellConfigs(upSellConfigs);
                ArrayList<UpsellConfig> upSellConfigses = new ArrayList<UpsellConfig>(DataManager.getInstance().getUpsellConfigs());
                if (upSellConfigses != null && upSellConfigses.size() > 0) {
                    adRotationInterval = upSellConfigs.get(0).getRotationInterval();
                    Log.d("AdLocationInterval", "Received AdLocationInterval value is " + adRotationInterval);
                    if (adRotationInterval <= 0) {
                        adRotationInterval = 10;
                        Log.d("AdLocationInterval", "Default AdLocationInterval applied as 10");
                    }
                }
                loadUpSellDetails();
            }
        });
    }

    private void loadUpSellDetails() {
        enableScreen(false);
        ProductService.loadUpSellDetails(this, new UpSellServiceCallBack() {
            @Override
            public void onUpSellServiceCallBack(ArrayList<UpSell> upSells, Exception exception) {
                enableScreen(true);
                DataManager.getInstance().setUpSells(upSells);
                setUpViewPager();
            }
        });
    }

    //Advertisement View Pager
    private void setUpViewPager() {
        upSellAdsPager = (CustomViewPager) findViewById(R.id.upsellAdsPager);
        // Setting custom scroll speed for Viewpager
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(upSellAdsPager.getContext(), new FastOutLinearInInterpolator());
            // scroller.setFixedDuration(5000);
            mScroller.set(upSellAdsPager, scroller);
        } catch (java.lang.NoSuchFieldException e) {

        } catch (java.lang.IllegalAccessException e) {

        }

        upSellAdsPager.setVisibility(View.VISIBLE);
        upSellAdsPager.setPagingEnabled(true);
        upSellAdsPager.addOnPageChangeListener(this);


        if (DataManager.getInstance().getUpSells() != null && DataManager.getInstance().getUpSells().size() > 0) {
            setViewPagerAdAdapter();
        } else {
            upSellAdsPager.setVisibility(View.GONE);
        }

    }

    //Advertisement pager adapter
    private void setViewPagerAdAdapter() {
        ArrayList<UpSell> upSells = DataManager.getInstance().getUpSells();
        if (upSells != null) {
            currentUpSells = new ArrayList<UpSell>(upSells);
            if (adapter == null && currentUpSells != null) {
                adapter = new UpSellImageAdapter(getSupportFragmentManager(), currentUpSells);
                upSellAdsPager.setAdapter(adapter);
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
        //Setting time to change ad automatically
        pageSwitcher(adRotationInterval);
    }

    private void initToolbar() {
        setUpToolBar(true,true);
        setTitle("May We Suggest?");
        setBackButton(false, false);
        isShowBasketIcon = false;
        toolbar.setBackgroundColor(getResources().getColor(R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    private void setAdapter() {
        upSellRecyclerView = (RecyclerView) findViewById(R.id.upSellProductsListView);
        upSellRecyclerView.setNestedScrollingEnabled(false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        upSellRecyclerView.setLayoutManager(linearLayoutManager);
        upSellRecyclerView.setHasFixedSize(true);

        if (upSellAdapter == null) {
            upSellAdapter = new UpSellAdapter(oloUpsellItemList);
            upSellRecyclerView.setAdapter(upSellAdapter);
        }
        if (upSellAdapter != null) {
            upSellAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addToBasket) {
            addUpSellProducts();
        }

        if (v.getId() == R.id.noThanks) {
            moveToCreditCardScreen();
        }
    }


    private void addUpSellProducts() {

        upSellSelectedCount = 0;
        enableScreen(false);
        JSONObject upSellObject = createUpSellJsonObject();
        //   Toast.makeText(this,"Service not yet connected",Toast.LENGTH_SHORT).show();
        if(upSellSelectedCount > 0) {
            BasketService.addUpSell(upSellObject, new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception e) {
                    enableScreen(true);
                    if (e == null) {
                        onBackPressed();
                    } else {
                        Utils.alert(context, "Error", Utils.getErrorDescription(e));
                    }
                }
            });
        }else{
            enableScreen(true);
            Utils.alert(context,"Error","Please select atleast one");
        }
    }

    private JSONObject createUpSellJsonObject() {

        JSONObject jsonObject;
        JSONObject mainJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < oloUpsellItemList.size(); i++) {
                if (oloUpsellItemList.get(i).isSelected()) {
                    upSellSelectedCount++;
                    jsonObject = new JSONObject();
                    jsonObject.put("id", oloUpsellItemList.get(i).getId());
                    jsonObject.put("quantity", oloUpsellItemList.get(i).getSelectedQuantity());
                    jsonArray.put(jsonObject);
                }
            }
            mainJsonObject.put("items", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mainJsonObject;
    }


    private void moveToCreditCardScreen() {
        if (UserService.isUserAuthenticated()) {
            enableScreen(false);
            BasketService.getBillingAccountsForCurrentBasket(UpsellActivity.this, new BillingAccountsCallback() {
                @Override
                public void onBillingAccountsCallback(ArrayList<BillingAccount> billingAccounts, Exception error) {
                    enableScreen(true);
                    Basket basket = DataManager.getInstance().getCurrentBasket();
                    if (billingAccounts != null && billingAccounts.size() > 0) {
                        TransitionManager.transitFrom(UpsellActivity.this, SelectExistingCardActivity.class);
                    } else {
                        TransitionManager.transitFrom(UpsellActivity.this, AddCardActivity.class);
                    }
                }
            });
        } else {
            enableScreen(true);
            TransitionManager.transitFrom(UpsellActivity.this, UserInfoActivity.class);
        }
    }


    public void pageSwitcher(int seconds) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        System.out.println("onPageScrolled called = " + position + " " + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("onPageSelected called = " + position);
        page = position;
        pageSwitcher(adRotationInterval);
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
                    if (upSellAdsPager != null) {
                        upSellAdsPager.setCurrentItem(page++, true);
                    }

                    if (page == currentUpSells.size()) {
                        page = 0;
                    }
                }
            });

        }
    }
}
