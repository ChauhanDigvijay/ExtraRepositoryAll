package com.olo.jambajuice.Activites.NonGeneric.OrderHistory;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Adapters.OrderHistoryViewPagerAdapter;
import com.olo.jambajuice.BusinessLogic.Interfaces.FavoriteOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.RecentOrderCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.FavoriteOrder;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ihsanulhaq on 28/05/15.
 */
public class OrderHistoryActivity extends BaseActivity implements View.OnClickListener, RecentOrderCallback, FavoriteOrderCallback,ViewPager.OnPageChangeListener {
    static OrderHistoryActivity myactivity;
    String from;
    RelativeLayout layout;
    int count = 0;
    private CustomViewPager pager;
    private int position;
    private OrderHistoryViewPagerAdapter adapter;
    private Button btnFav;
    private Button btnAll;
    private List<RecentOrder> recentOrder;
    private List<RecentOrder> newOrder;
    private List<FavoriteOrder> favOrder;
    private int out;

    public static OrderHistoryActivity getInstance() {
        return myactivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myactivity = this;
        setContentView(R.layout.activity_order_history);
        setUpToolBar(true);
        isSlideDown = true;
        setTitle("Order History");
        setBackButton(false, false);
        initComponents();
        setClickListeners();
        fetchData();
    }

//    getting recent orders & favorite orders datas

    private void fetchData() {
        enableLoading(true);
        System.gc();
        if (UserService.recentOrder == null || UserService.recentOrder.size() == 0) {
            UserService.getRecentOrder(this);
            out = 500;
        } else {
            onOrderCallback(UserService.recentOrder, null);
            out = 200;
        }
        if (UserService.favOrder == null || UserService.favOrder.size() == 0) {
            UserService.getFavoriteOrder(this);
        } else {
            onFavoriteCallback(UserService.favOrder, null);
        }
    }

    //    enable or disable loading
    private void enableLoading(boolean b) {
        if (b) {
            btnFav.setEnabled(false);
            btnAll.setEnabled(false);
            layout.setVisibility(View.VISIBLE);
        } else {
            btnFav.setEnabled(true);
            btnAll.setEnabled(true);
            layout.setVisibility(View.GONE);
            DataManager.getInstance().setFromFavorite(false);
        }
    }

    private void setClickListeners() {
        btnAll.setOnClickListener(this);
        btnFav.setOnClickListener(this);
        btnAll.setSelected(true);
    }

    //    setting adapter for recentorder and favoriteorder lists
    private void setAdapter() {
        adapter = new OrderHistoryViewPagerAdapter(getSupportFragmentManager(), recentOrder, favOrder);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(1);
        if (DataManager.getInstance().getFromFavorite()) {
            btnAll.setSelected(false);
            btnFav.setSelected(true);
            pager.setCurrentItem(1);
        } else {
            pager.setCurrentItem(0);
        }
        enableLoading(false);
    }

    private void initComponents() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            from = bundle.getString("from");
        }
        pager = (CustomViewPager) findViewById(R.id.viewpager);
        pager.addOnPageChangeListener(this);
        btnAll = (Button) findViewById(R.id.hist_button);
        btnFav = (Button) findViewById(R.id.fav_button);
        layout = (RelativeLayout) findViewById(R.id.historyprogress);
        if (layout != null) layout.setClickable(false);
        newOrder = new ArrayList<RecentOrder>();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hist_button:
                showAllFragment();
                break;
            case R.id.fav_button:
                showFavsFragment();
                break;
        }
    }

    private void showAllFragment() {
        if (!btnAll.isSelected()) {
            btnAll.setSelected(true);
            btnFav.setSelected(false);
            pager.setCurrentItem(0);
        }
    }

    private void showFavsFragment() {
        if (!btnFav.isSelected()) {
            btnAll.setSelected(false);
            btnFav.setSelected(true);
            pager.setCurrentItem(1);
        }
    }

    //    callback for recent orders
    @Override
    public void onOrderCallback(List<RecentOrder> status, Exception exception) {
        if (exception == null) {
            recentOrder = status;
        } else {
            Utils.showErrorAlert(OrderHistoryActivity.this, exception);
        }
    }
//    callback for favorite orders

    @Override
    public void onFavoriteCallback(List<FavoriteOrder> status, Exception e) {
        Handler handler = new Handler();
        if (e == null) {
            favOrder = status;
            setAdapter();
        } else {
            Utils.showErrorAlert(OrderHistoryActivity.this, e);
            enableLoading(false);
        }
    }

//    @Override
//    public void onFavoriteCallback(List<FavoriteOrder> status, Exception e) {
//        Handler handler = new Handler();
//        if (e == null) {
//            favOrder = status;
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if(DataManager.getInstance().getNewRecentOrder() == null
//                            || DataManager.getInstance().getNewRecentOrder().size() == 0) {
//                        getFavoriteProducts(favOrder);
//                    }else{
//                        setAdapter();
//                    }
//                }
//            }, out);
//        } else {
//            Utils.showErrorAlert(OrderHistoryActivity.this, e);
//            enableLoading(false);
//        }
//    }

    // getting favorite order products title and saving it in new Model
    private void getFavoriteProducts(final List<FavoriteOrder> favOrder) {
        if (count < favOrder.size()) {
            UserService.getFavoriteOrderDetail(favOrder.get(count).getId(), new RecentOrderCallback() {
                @Override
                public void onOrderCallback(List<RecentOrder> status, Exception e) {
                    if (e == null) {
                        newOrder.add(status.get(0));
                        count++;
                        getFavoriteProducts(favOrder);
                    } else {
                        Utils.showErrorAlert(OrderHistoryActivity.this, e);
                        enableLoading(false);
                    }
                }
            });
        } else {
            count = 0;
            DataManager.getInstance().setNewRecentOrder(newOrder);
            setAdapter();
        }
    }

    // onresume of activity from other activity
    @Override
    protected void onResume() {
        if (DataManager.getInstance().getFromFavorite() != null) {
            if (DataManager.getInstance().getFromFavorite()) {
                enableLoading(true);
                out = 100;
                UserService.getFavoriteOrder(this);
            }
        }
        if(DataManager.getInstance().isOrderCancelFlag()) {
            fetchRecentOrder();
        }
        super.onResume();
    }

    private void fetchRecentOrder() {
        enableLoading(true);
        if (UserService.isUserAuthenticated()) {
            UserService.getRecentOrder(new RecentOrderCallback() {
                @Override
                public void onOrderCallback(List<RecentOrder> status, Exception exception) {
                    enableLoading(false);
                    DataManager.getInstance().setOrderCancelFlag(false);
                    if (exception == null) {
                        recentOrder = status;
                        setAdapter();
                    } else {
                        Utils.showErrorAlert(OrderHistoryActivity.this, exception);
                    }
                }
            });
        }
    }

    // ondestroy when activity deleted
    @Override
    protected void onDestroy() {
        DataManager.getInstance().setFromFavorite(false);
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0){
            showAllFragment();
        }else {
            showFavsFragment();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
