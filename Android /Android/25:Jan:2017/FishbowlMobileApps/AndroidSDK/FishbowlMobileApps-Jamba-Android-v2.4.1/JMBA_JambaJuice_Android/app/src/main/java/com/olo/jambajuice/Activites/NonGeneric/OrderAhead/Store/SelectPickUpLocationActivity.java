package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Store;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketFlagViewManager;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorActivity;
import com.olo.jambajuice.BusinessLogic.Interfaces.StartOrderCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.BusinessLogic.Services.StoreService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;


/**
 * Created by Nauman on 21/06/15.
 */
public class SelectPickUpLocationActivity extends BaseActivity implements View.OnClickListener {
    Button pickUpAtPreferredStore;
    Button selectAnotherStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pickup_location);
        isBackButtonEnabled = true;
        isAnimated = false;
        setUpClickListeners();
        udpateNameAndAddress();
        LocalBroadcastManager.getInstance(JambaApplication.getAppContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_REMOVE_STORE_LOCATOR_ACTIVITY));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pickUpAtPreferredStore:
                preferredStoreViewSelected();
                break;
            case R.id.selectAnotherStore:
                selectAnotherStoreSelected();
                break;

        }
    }

    private void showDeleteBasketConfirmation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Starting a new order will empty the basket and cancel your current order. Continue?");
        alertDialogBuilder.setTitle("Start New Order");
        alertDialogBuilder.setPositiveButton("Start New Order", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                DataManager.getInstance().resetBasket();
                BasketFlagViewManager.getInstance().removeBasketFlag();
                selectNewOrder();
            }
        });
        alertDialogBuilder.setNegativeButton("No", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void preferredStoreViewSelected() {
        if (DataManager.getInstance().isBasketPresent()) {
            showDeleteBasketConfirmation();
        } else {
            selectNewOrder();
        }
    }

    private void selectNewOrder() {
        enableScreen(false);
        StoreService.startNewOrderFromPreferredStore(this, new StartOrderCallback() {
            @Override
            public void onStartOrderCallback(Exception exception) {
                enableScreen(true);
                if (exception == null) {
                    onBackPressed();
                } else {
                    Utils.showErrorAlert(SelectPickUpLocationActivity.this, exception);
                }
            }
        });
    }

    private void selectAnotherStoreSelected() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            TransitionManager.slideUp(this, StoreLocatorActivity.class, bundle);
        }
    }

    private void setUpClickListeners() {
        pickUpAtPreferredStore = (Button) findViewById(R.id.pickUpAtPreferredStore);
        selectAnotherStore = (Button) findViewById(R.id.selectAnotherStore);
        pickUpAtPreferredStore.setOnClickListener(this);
        selectAnotherStore.setOnClickListener(this);

    }

    private void udpateNameAndAddress() {
        final TextView storeName = (TextView) findViewById(R.id.storeName);
        final TextView storeAddress = (TextView) findViewById(R.id.storeAddress);
        User user = UserService.getUser();
        storeName.setText(user.getStoreName());
        storeAddress.setText(user.getStoreAddress());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In case user has changed preferred store and this activity is in stack then update name and address on resume
        udpateNameAndAddress();
    }

    @Override
    public void enableScreen(boolean isEnabled) {
        pickUpAtPreferredStore.setEnabled(isEnabled);
        selectAnotherStore.setEnabled(isEnabled);
        super.enableScreen(isEnabled);
    }

    @Override
    protected void handleBroadCastReceiver(Intent intent) {
        finish();
    }

    @Override
    protected void handleAuthTokenFailure() {
        finish(); // Remove this UI as it is only required for logged in user.
    }
}
