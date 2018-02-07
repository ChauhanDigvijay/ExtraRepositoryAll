package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.RewardsAndPromotions.Offer;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Adapters.MoreStoreListAdapter;
import com.olo.jambajuice.Adapters.MyRewardOfferAdapter;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreDetailCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.OfferAvailableStore;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Services.StoreService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.olo.Interfaces.OloRestaurantServiceCallback;
import com.wearehathway.apps.olo.Models.OloRestaurant;
import com.wearehathway.apps.olo.Services.OloRestaurantService;

import java.util.ArrayList;
import java.util.List;

import static com.olo.jambajuice.R.id.rewardsList;

public class MoreInfoActivity extends BaseActivity {

    private ListView availableStoreListView;
    private RelativeLayout searchView;
    private EditText searchAvStore;
    private ArrayList<OfferAvailableStore> storeResList = new ArrayList<>();
    //private ArrayAdapter<String> arrayAdapter;
    private MoreStoreListAdapter arrayAdapter;
    private OloRestaurant[] allStores;
    private List<Store> availableStoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        setToolbar();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            storeResList = (ArrayList<OfferAvailableStore>) extras.getSerializable("storeList");
        }

        availableStoreListView = (ListView) findViewById(R.id.availableStoreListView);
        searchView = (RelativeLayout) findViewById(R.id.searchView);
        searchAvStore = (EditText) findViewById(R.id.searchAvStore);
        searchAvStore.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (searchAvStore.getText().equals("") || searchAvStore.getText() == null) {
                    invalidText();
                } else {
                    searchStore(searchAvStore.getText().toString());
                }
                return false;
            }
        });

        searchAvStore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchStore(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0){
                    setAdapter(storeResList, allStores);
                }
            }
        });
        if (storeResList.size() > 10) {
            searchView.setVisibility(View.VISIBLE);
        } else {
            searchView.setVisibility(View.GONE);
        }

        downloadAllStores();
    }

    private void downloadAllStores() {
        if(DataManager.getInstance().getAllStores() != null && DataManager.getInstance().getAllStores().length > 0){
            setAdapter(storeResList, DataManager.getInstance().getAllStores());
        }else {
            enableScreen(false);
            OloRestaurantService.getAllRestaurants(new OloRestaurantServiceCallback() {
                @Override
                public void onRestaurantServiceCallback(OloRestaurant[] restaurants, Exception exception) {
                    enableScreen(true);
                    if (exception == null) {
                        if (restaurants != null) {
                            allStores = new OloRestaurant[restaurants.length];
                            allStores = restaurants;
                            DataManager.getInstance().setAllStores(allStores);
                        }
                    }

                    setAdapter(storeResList, allStores);
                }
            });
        }
    }

    private void setAdapter(ArrayList<OfferAvailableStore> storeResList,OloRestaurant[] allStores) {
        availableStoresList = new ArrayList<>();
        if (allStores != null && allStores.length > 0) {
            for (int i = 0; i < storeResList.size(); i++) {
                for (int j = 0; j < allStores.length; j++) {
                    if (Integer.parseInt(storeResList.get(i).getStoreCode()) == Integer.parseInt(allStores[j].getStoreCode())) {

                        String storeName = null;
                        if(DataManager.getInstance().isDebug){
                            storeName = Utils.setOloDemoStoreName(allStores[j]).getName().replace("Jamba Juice ","");
                        }else{
                            storeName = allStores[j].getName().replace("Jamba Juice ","");
                        }
                        String storeCity = allStores[j].getCity();
                        String storeState = allStores[j].getState();

                        availableStoresList.add(new Store(storeName, storeCity, storeState));
                    }
                }
            }
        } else {
            for (int i = 0; i < storeResList.size(); i++) {
                String storeName = storeResList.get(i).getStoreName();
                availableStoresList.add(new Store(storeName,"",""));
            }
        }
        setAdapter();
//        arrayAdapter = new ArrayAdapter<String>(
//
//                this,
//                R.layout.avilable_stores_textview,
//                availableStoresList);
//
//        availableStoreListView.setAdapter(arrayAdapter);
    }

    private void setAdapter() {
        arrayAdapter = new MoreStoreListAdapter(this, R.layout.avilable_stores_textview, availableStoresList);
        availableStoreListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    private void searchStore(String keyword) {
        ArrayList<OfferAvailableStore> searchedList = new ArrayList<>();
        for (OfferAvailableStore offerAvailableStore : storeResList) {
            String storeName = offerAvailableStore.getStoreName();
            if(storeName.toLowerCase().contains(keyword)) {
                searchedList.add(offerAvailableStore);
            }
        }
        setAdapter(searchedList,allStores);
    }

    private void setToolbar() {
        setUpToolBar(true, true);
        setTitle("More Stores");
        setBackButton(false, false);
    }

    private void invalidText() {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Invalid Text");
        alertDialogBuilder.setMessage("Please! Enter a valid name");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (searchAvStore != null) {
                    searchAvStore.requestFocus();
                }
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
