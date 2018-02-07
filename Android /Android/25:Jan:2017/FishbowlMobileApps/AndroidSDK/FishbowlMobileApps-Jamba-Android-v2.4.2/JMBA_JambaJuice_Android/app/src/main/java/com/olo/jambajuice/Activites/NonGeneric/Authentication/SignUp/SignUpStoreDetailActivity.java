package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;

public class SignUpStoreDetailActivity extends SignUpBaseActivity implements View.OnClickListener, OnMapReadyCallback {
    private GoogleMap googleMap;
    private Store selectedStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_store_detail);
        setUpToolBar(true);
        isShowBasketIcon = false;
        setTitle("Select Preferred Store");
        setBackButton(true,false);
        initilizeMap();
//        fillViewWithData();
//        if (selectedStore != null) {
//            addMarkerOnMap();
//            setUpOrderAhead();
//            initializeCircle(3);
//            findViewById(R.id.continueBtn).setOnClickListener(this);
//        }
    }

    private void setUpOrderAhead() {
        TextView orderAheadText = (TextView) findViewById(R.id.orderAheadText);
        ImageView orderAheadIcon = (ImageView) findViewById(R.id.orderAheadIcon);
        if (selectedStore.isSupportsOrderAhead()) {
            orderAheadText.setText("Order Ahead is available at this store");
            orderAheadText.setTextColor(getResources().getColor(R.color.dark_green));
            //orderAheadText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.order_ahead_icon, 0, 0);
        } else {
            orderAheadText.setText("Order Ahead is not available at this store");
            orderAheadText.setTextColor(getResources().getColor(R.color.toolbar_text));
            orderAheadIcon.setBackgroundResource(R.drawable.order_ahead_icon_gray);
            //orderAheadText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.order_ahead_icon_gray, 0, 0);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (googleMap != null) {
            googleMap.getUiSettings().setAllGesturesEnabled(false);
        }

        fillViewWithData();
        if (selectedStore != null) {
            addMarkerOnMap();
            setUpOrderAhead();
            initializeCircle(3);
            findViewById(R.id.continueBtn).setOnClickListener(this);
        }
    }

    private void initilizeMap() {
        //googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment!= null) {
            mapFragment.getMapAsync(this);
        }

    }

    private void fillViewWithData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedStore = (Store) bundle.getSerializable(Constants.B_STORE);
            if (selectedStore != null) {
                TextView storeName = (TextView) findViewById(R.id.storeName);
                TextView storeAddress = (TextView) findViewById(R.id.storeAddress);
                TextView storeMiles = (TextView) findViewById(R.id.storeMiles);
                storeName.setText(selectedStore.getName());
                storeAddress.setText(selectedStore.getCompleteAddress());
                storeMiles.setText(String.valueOf(selectedStore.getDistanceToUser())+" miles away");
                moveMapToLocation(selectedStore.getLatitude(), selectedStore.getLongitude());
            }
        }
    }

    private void moveMapToLocation(double lat, double lng) {
        if (googleMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(15.5f).build();
            CameraUpdate center = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.moveCamera(center);
        }
    }

    private void addMarkerOnMap() {
        if (googleMap != null && selectedStore != null) {
            MarkerOptions options = new MarkerOptions().position(new LatLng(selectedStore.getLatitude(), selectedStore.getLongitude()));
            googleMap.addMarker(options);
        }
    }

    @Override
    public void onClick(View view) {
        if (selectedStore != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
//                int storeId = selectedStore.getId(); // SpendGo Store Id.
//                bundle.putInt(Constants.B_PREFERRED_STORE_ID, storeId);
                String storeCode = selectedStore.getStoreCode(); // SpendGo Store Id.
                bundle.putString(Constants.B_PREFERRED_STORE_ID, storeCode);
                TransitionManager.transitFrom(this, SignUpNameAndEmailActivity.class, bundle);
            }
        }
    }
}
