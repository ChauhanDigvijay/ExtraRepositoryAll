package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Confirmation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp.SignUpJambaInsiderActivity;
import com.olo.jambajuice.Activites.NonGeneric.Home.HomeActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.UpsellActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderHistory.OrderDetailActivity;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.OrderStatus;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.wearehathway.apps.olo.Utils.Constants.Server_Time_Format;

public class OrderConfirmationActivity extends BaseActivity implements View.OnClickListener {
    Store selectedStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        if (DataManager.getInstance().getCurrentBasket() == null) {
            //Incase the data is not available and activity is recreating.
            finish();
            return;
        }
        if(UpsellActivity.upsellActivity != null){
            UpsellActivity.upsellActivity.finish();
        }
        isBackButtonEnabled = true;
        isShowBasketIcon = false;
        selectedStore = DataManager.getInstance().getCurrentSelectedStore();
        setUpTextViews();
        setUpClickListeners();
        DataManager.getInstance().resetBasket();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.store_layout:
                getDirectionTapped();
                break;
            case R.id.phone_layout:
                callBtnTapped();
                break;
            case R.id.close_button:
                startHomeActivity();
                onBackPressed();
                break;
            case R.id.jambainsider_layout:
                startSignUp();
                break;
            case R.id.orderStatus:
                showOrderDetail();
                break;
        }
    }

    private void showOrderDetail() {
        Bundle bundle = new Bundle();
        bundle.putString("type", "recent");
        bundle.putSerializable(Constants.B_ORDER_DETAIL, (UserService.recentOrder != null && UserService.recentOrder.size() > 0) ? UserService.recentOrder.get(0) : null);
        TransitionManager.transitFrom(OrderConfirmationActivity.this, OrderDetailActivity.class, bundle);
        finish();
    }

    private void startHomeActivity() {

        TransitionManager.transitFrom(this, HomeActivity.class);
    }


    private void startSignUp() {
        DataManager.getInstance().resetDataManager();
        this.finish();
        TransitionManager.transitFrom(this, SignUpJambaInsiderActivity.class);
    }

    private void setUpTextViews() {
        OrderStatus orderStatus = DataManager.getInstance().getOrderStatus();
        TextView tv_time_info = (TextView) findViewById(R.id.tv_time_info);
        TextView tv_store_name = (TextView) findViewById(R.id.tv_store_name);
        TextView tv_store_address = (TextView) findViewById(R.id.tv_store_address);
        TextView tv_store_phone = (TextView) findViewById(R.id.tv_phone);
        TextView total_amount = (TextView) findViewById(R.id.total_amount);
        String totalAmountText = "TOTAL: $";
        total_amount.setText(totalAmountText + "" + String.format("%.02f", orderStatus.getTotal()));

        if (DataManager.getInstance().getCurrentBasket().getOrderType() == BasketActivity.OrderType.DELIVERY) {
            RelativeLayout storeLayout = (RelativeLayout) findViewById(R.id.store_layout);
            RelativeLayout phoneLayout = (RelativeLayout) findViewById(R.id.phone_layout);
            RelativeLayout orderStatusLayout = (RelativeLayout) findViewById(R.id.orderStatus);
            storeLayout.setVisibility(View.GONE);
            phoneLayout.setVisibility(View.VISIBLE);
        }

        try {
            SimpleDateFormat serverFormat = new SimpleDateFormat(Server_Time_Format);
            Date readyTime = serverFormat.parse(orderStatus.getReadytime());

            //String day = Utils.isTomorrow(orderStatus.getReadytime()) ? "tomorrow" : "today";

            String day = "";

            if (Utils.isTomorrow(orderStatus.getReadytime())) {
                day = "tomorrow";
            } else if (Utils.isToday(orderStatus.getReadytime())) {
                day = "today";
            } else {
                day = "on " + new SimpleDateFormat("EEEE, MMMM d, yyyy").format(readyTime);
            }

            String time = Utils.getTimeDisplayFormat(this).format(readyTime);
            String estTime = DataManager.getInstance().getCurrentBasket().getLeadtimeestimateminutes();
            String conjunctionText1 = "deliver";
            String conjunctionText2 = "";
            String mainText = "Your order was placed at " + time + " " + day + ". Your order's estimated delivery time is " + estTime + " mins.";


            if (DataManager.getInstance().getCurrentBasket().getOrderType() == BasketActivity.OrderType.PICKUP) {
                conjunctionText1 = "ready";
                conjunctionText2 = "Head straight to the pick up counter in store and Skip the Line®!";
                mainText = "Your order will be " + conjunctionText1 + " at " + time + " " + day + ". " + conjunctionText2;
            }


            //Old Format: Order ready at 12:45 PM, tomorrow (Friday, 6/26/2015)
            //New  Format: Order ready at 12:45 PM, tomorrow
            //String dayOfTheWeek = new SimpleDateFormat("EEEE").format(readyTime);
            //String date = new SimpleDateFormat("dd/MM/yyyy").format(readyTime);


            tv_time_info.setText(mainText);
            if (DataManager.getInstance().isDebug) {
                tv_store_name.setText(Utils.setDemoStoreName(selectedStore).getName().replace("Jamba Juice ", ""));
            } else {
                tv_store_name.setText(selectedStore.getName().replace("Jamba Juice ", ""));
            }
            tv_store_address.setText(selectedStore.getCompleteAddress());
            tv_store_phone.setText(selectedStore.getTelephone());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setUpClickListeners() {
        RelativeLayout getDir = (RelativeLayout) findViewById(R.id.store_layout);
        RelativeLayout callBtn = (RelativeLayout) findViewById(R.id.phone_layout);
        RelativeLayout orderStatus = (RelativeLayout) findViewById(R.id.orderStatus);
        Button closeBtn = (Button) findViewById(R.id.close_button);
        getDir.setOnClickListener(this);
        callBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        orderStatus.setOnClickListener(this);
        if (!UserService.isUserAuthenticated()) {
            LinearLayout linearlayout = (LinearLayout) findViewById(R.id.jambainsider_layout);
            linearlayout.setVisibility(View.VISIBLE);
            linearlayout.setOnClickListener(this);
            orderStatus.setVisibility(View.GONE);
        }
    }

    private void getDirectionTapped() {
        Utils.getDirection(this, selectedStore);
    }

    private void callBtnTapped() {
        Utils.showDialerConfirmation(this, selectedStore);
    }
}
