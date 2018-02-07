package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorActivity;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;

public class SignUpFindStoresActivity extends SignUpBaseActivity implements View.OnClickListener {
    Button findAStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_find_stores);
        setUpToolBar(true);
        isShowBasketIcon = false;
        setTitle("Select Preferred Store");
        setBackButton(true,false);
        initializeCircle(1);
        findAStore = (Button) findViewById(R.id.findAStore);
        findAStore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.B_IS_CHOOOSE_STORE_FROM_SIGN_UP, true);
        TransitionManager.transitFrom(SignUpFindStoresActivity.this, StoreLocatorActivity.class, bundle);
    }
}
