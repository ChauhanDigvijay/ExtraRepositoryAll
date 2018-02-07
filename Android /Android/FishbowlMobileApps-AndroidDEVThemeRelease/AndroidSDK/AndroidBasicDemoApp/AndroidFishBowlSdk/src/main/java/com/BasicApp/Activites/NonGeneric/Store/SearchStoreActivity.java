package com.BasicApp.Activites.NonGeneric.Store;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by schaudhary_ic on 10-Aug-16.
 */
public class SearchStoreActivity extends FragmentActivity implements SearchView.OnQueryTextListener   {

    private SearchView serchView;
    private ListView mListView;

    public FBStoreService all;
    private List<FBStoresItem> storeList;
    private StoreAdapter storeAdapter;

    private Toolbar toolbar;
    private ImageLoader mImageLoader;
    private NetworkImageView background;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_store);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        serchView=(SearchView)findViewById(R.id.searchView);

        mListView=(ListView) findViewById(R.id.listView1);
        all= FBStoreService.sharedInstance();
        storeList=all.allStoreFromServer;

        background = (NetworkImageView) findViewById(R.id.img_Back);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchStoreActivity.this.finish();
            }
        });




        storeAdapter=new StoreAdapter(SearchStoreActivity.this, storeList);
        mListView.setAdapter(storeAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                FBStoresItem str=storeAdapter.storeResultList.get(i);
                serchView.setQuery((CharSequence) storeAdapter.storeResultList.get(i).getStoreName(),false);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("CLPSTORE",str);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });


        mListView.setTextFilterEnabled(false);
        setupSearchView();
        serchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // serchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        serchView.setIconifiedByDefault(false);


    }


    private void setupSearchView()
    {
        serchView.setIconifiedByDefault(false);
        serchView.setOnQueryTextListener(this);
        serchView.setSubmitButtonEnabled(true);
        serchView.setQueryHint("Search Here");

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
    public boolean onQueryTextChange(String newText)
    {

        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText);
        }
        return true;
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


        public class StoreHolder
        {
            TextView name;
        }

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
                                        .contains(constraint.toString()))
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
            return  this.storeResultList.size();
        }

        @Override
        public Object getItem(int position) {
            return  this.storeResultList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StoreHolder holder;
            if(convertView==null)
            {
                convertView= LayoutInflater.from(context).inflate(R.layout.store_row, parent, false);
                holder=new StoreHolder();
                holder.name=(TextView) convertView.findViewById(R.id.storename);
                convertView.setTag(holder);
            }
            else
            {
                holder=(StoreHolder) convertView.getTag();
            }

            holder.name.setText(this.storeResultList.get(position).getStoreName());

            return convertView;

        }

    }
    @Override
    public void onStart()
    {
        super.onStart();
        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();

            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
//            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));
//            background.setImageUrl(url, mImageLoader);

        }


    }
}
