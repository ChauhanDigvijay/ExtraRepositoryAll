package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Store;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Models.FBStoresItem;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by schaudhary_ic on 10-Aug-16.
 */
public class SearchStoreActivity extends Activity implements SearchView.OnQueryTextListener {

    public FB_LY_UserService all;
    ProgressBarHandler progressBarHandler;
    private SearchView serchView;
    private ListView mListView;
    private List<FBStoresItem> storeList;
    private StoreAdapter storeAdapter;
    private Toolbar toolbar;
    private ImageLoader mImageLoader;
    private NetworkImageView background;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_store);
        progressBarHandler = new ProgressBarHandler(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        serchView = (SearchView) findViewById(R.id.searchView);
        mListView = (ListView) findViewById(R.id.listView1);
        all = FB_LY_UserService.sharedInstance();
        storeList = all.allStoreFromServer;
        background = (NetworkImageView) findViewById(R.id.img_Back);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchStoreActivity.this.finish();
            }
        });


        if (storeList != null) {
            storeAdapter = new StoreAdapter(SearchStoreActivity.this, storeList);
            mListView.setAdapter(storeAdapter);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                FBStoresItem str = storeAdapter.storeResultList.get(i);
                serchView.setQuery((CharSequence) storeAdapter.storeResultList.get(i).getStoreName(), false);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("CLPSTORE", str);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mListView.setTextFilterEnabled(true);
        } else {
            mListView.setTextFilterEnabled(false);
        }


        setupSearchView();
        serchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // serchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        serchView.setIconifiedByDefault(false);


    }


    private void setupSearchView() {
        serchView.setIconifiedByDefault(false);
        serchView.setOnQueryTextListener(this);
        serchView.setSubmitButtonEnabled(true);
        serchView.setQueryHint("Search Here");

        serchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                storeAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText);
        }
        return true;
    }

    public void getStore() {
        progressBarHandler.show();
        JSONObject object = new JSONObject();
        FB_LY_UserService.sharedInstance().getAllStore(new FB_LY_UserService.FBAllStoreCallback() {


            @Override
            public void OnAllStoreCallback(JSONObject response, String error) {

                try {
                    if (error == null && response != null) {

                        all = FB_LY_UserService.sharedInstance();
                        storeList = all.allStoreFromServer;
                        progressBarHandler.dismiss();
                        storeAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }


        });
    }

    @Override
    public void onStart() {
        super.onStart();

        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
        //searchstore backgroundimage/color
        final String signUpBackgroundImageUrl = FBThemeMobileSettingsService.sharedInstance().registermapsetting.get("SignUpBackgroundImageUrl");
        //signup image/color
        if(StringUtilities.isValidString(signUpBackgroundImageUrl)) {
            final String signupbgurl = "http://" + signUpBackgroundImageUrl;
            mImageLoader.get(signupbgurl, ImageLoader.getImageListener(background, R.color.white, R.color.white));
            background.setImageUrl(signupbgurl, mImageLoader);
        }

        else if(FBThemeMobileSettingsService.sharedInstance().registermapsetting.get("SignUpBackgroundColor")!=null)
        {
            final String signUpBackgroundColor =  FBThemeMobileSettingsService.sharedInstance().registermapsetting.get("SignUpBackgroundColor");
            if(StringUtilities.isValidString(signUpBackgroundColor))
                background.setBackgroundColor(Color.parseColor(signUpBackgroundColor));
        }


        storeAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        storeAdapter.notifyDataSetChanged();

    }

    class StoreAdapter extends BaseAdapter implements Filterable {

        public Context context;
        public List<FBStoresItem> storeResultList;
        public List<FBStoresItem> orig;

        public StoreAdapter(Context context, List<FBStoresItem> storeList) {
            super();
            this.context = context;
            this.storeResultList = storeList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults oReturn = new FilterResults();
                    final List<FBStoresItem> results = new ArrayList<FBStoresItem>();
                    if (orig == null)
                        orig = storeResultList;
                    if (constraint != null) {
                        if (orig != null && orig.size() > 0) {
                            for (final FBStoresItem g : orig) {
                                if (g.getStoreName().toLowerCase()
                                        .contains(constraint.toString().toLowerCase()))
                                    results.add(g);
                            }
                        }
                        oReturn.values = results;
                    }
                    return oReturn;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    storeResultList = (ArrayList<FBStoresItem>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.storeResultList.size();
        }

        @Override
        public Object getItem(int position) {
            return this.storeResultList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StoreHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.store_row, parent, false);
                holder = new StoreHolder();
                holder.name = (TextView) convertView.findViewById(R.id.storename);
                convertView.setTag(holder);
            } else {
                holder = (StoreHolder) convertView.getTag();
            }

            holder.name.setText(this.storeResultList.get(position).getStoreName());

            return convertView;

        }

        public class StoreHolder {
            TextView name;
        }

    }
}
