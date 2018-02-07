package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.OrderType;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Adapters.SavedAddressesAdapter;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.SavedDispatchAddressesCallBack;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.DeliveryAddress;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SavedDeliveryAddressesActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout NoAddressLayout;
    private SavedAddressesAdapter savedAddressesAdapter;
    private TextView addNew;
    private ListView savedAddressListView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_delivery_addresses);

        context = this;
        NoAddressLayout = (RelativeLayout) findViewById(R.id.NoAddressLayout);
        addNew = (TextView) findViewById(R.id.addNew);
        savedAddressListView = (ListView) findViewById(R.id.savedAddressListView);
        addNew.setOnClickListener(this);
        setToolBar();
        if (DataManager.getInstance().getDeliveryAddresses() != null && DataManager.getInstance().getDeliveryAddresses().size() > 0) {
            setUi();
        } else {
            getSavedAddressesList();
        }

    }

    private void setToolBar() {
        isShowBasketIcon = false;
        setUpToolBar(true);
        setTitle("Saved Addresses", getResources().getColor(android.R.color.white));
        setBackButton(true, true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
    }

    private void getSavedAddressesList() {
        enableScreen(false);
        BasketService.getSavedDeliveryAddresses(SavedDeliveryAddressesActivity.this, new SavedDispatchAddressesCallBack() {
            @Override
            public void onSavedDispatchAddresses(ArrayList<DeliveryAddress> deliveryAddresses, Exception error) {
                enableScreen(true);
                if (error == null) {
                    DataManager.getInstance().setDeliveryAddresses(deliveryAddresses);
                    setUi();
                } else {
                    Utils.showErrorAlert(SavedDeliveryAddressesActivity.this, error);
                }
            }
        });
    }

    private void setUi() {
        List<DeliveryAddress> deliveryAddresses = DataManager.getInstance().getDeliveryAddresses();
        if (deliveryAddresses == null || deliveryAddresses.size() == 0) {
            NoAddressLayout.setVisibility(View.VISIBLE);
            savedAddressListView.setVisibility(View.GONE);
        } else {
            NoAddressLayout.setVisibility(View.GONE);
            savedAddressListView.setVisibility(View.VISIBLE);
            savedAddressesAdapter = new SavedAddressesAdapter(this, R.layout.row_saved_addresses_item, deliveryAddresses);
            savedAddressListView.setAdapter(savedAddressesAdapter);
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addNew) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("IsAddNew", true);
            TransitionManager.slideUp(this, AddDeliveryAddressActivity.class, bundle);
        }
    }
}
