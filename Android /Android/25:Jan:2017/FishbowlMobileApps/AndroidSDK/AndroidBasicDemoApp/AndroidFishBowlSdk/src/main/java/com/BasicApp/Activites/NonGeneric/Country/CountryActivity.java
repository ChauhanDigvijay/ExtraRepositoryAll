package com.BasicApp.Activites.NonGeneric.Country;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.Country;
import com.BasicApp.Utils.ProgressBarHandler;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBUserService;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by schaudhary_ic on 24-Oct-16.
 */

public class CountryActivity extends Activity implements SearchView.OnQueryTextListener {
    CountryListPopulationAdapter adapter;
    public List<Country> allCountryListfromServer = new ArrayList<Country>();
    public ListView list;
    ProgressBarHandler progressBarHandler;
    Timer t = new Timer();
    String countrycode;
    String nameState;
    SearchView searchview;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBarHandler =new ProgressBarHandler(this);

        setContentView(R.layout.activity_country);
        searchview = (SearchView) findViewById(R.id.searchView);


        list = (ListView) findViewById(R.id.states_list_data);
        if (allCountryListfromServer!=null){
            adapter  = new CountryListPopulationAdapter(this,allCountryListfromServer);
            list.setAdapter(adapter);

        }
        list.setTextFilterEnabled(true);
        setupSearchView();
        list.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();

                    }
                });
            }
        };

        t.scheduleAtFixedRate(task, 0, 1000);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Country selectedState =  allCountryListfromServer.get(position);
                countrycode = selectedState.getCountryCode();
                nameState = selectedState.getcountryName();
                System.out.println(nameState);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Country",nameState);
                returnIntent.putExtra("CountryCode",countrycode);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        getCountry();
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

    }
    private void setupSearchView() {
        searchview.setIconifiedByDefault(false);
        searchview.setOnQueryTextListener(this);
        searchview.setSubmitButtonEnabled(false);
        searchview.setQueryHint("Search Here");
    }


    public void getCountry() {
        progressBarHandler.show();
        JSONObject object = new JSONObject();
        // progressBarHandler.show();
        //   pd.show();
        FBUserService.sharedInstance().getCountry(object, new FBUserService.FBCountryCallback() {
            @Override
            public void onCountryCallback(JSONObject response, Exception error) {

                try {
                    if (error == null && response != null)
                    {
                        if (!response.has("countryList"))
                            return;

                        JSONArray getArrayStates = response.getJSONArray("countryList");
                        if (getArrayStates != null) {

                            for (int i = 0; i < getArrayStates.length(); i++) {
                                JSONObject myCountryObj = getArrayStates.getJSONObject(i);
                                Country countryListObj = new Country(myCountryObj);
                                allCountryListfromServer.add(countryListObj);


                            }
                        }
                        progressBarHandler.hide();
                    }
                } catch (Exception e) {
                }
            }


        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            list.clearTextFilter();
        } else {
            list.setFilterText(newText.toString());
        }
        return true;
    }

    public class CountryListPopulationAdapter extends BaseAdapter implements Filterable{

        public List<Country> countryList;
        public Context context;
        public List<Country> orig;

        public CountryListPopulationAdapter(Context context, List<Country> countryList) {
            super();
            this.context = context;
            this.countryList = countryList;
        }
        @Override
        public int getCount() {
            return countryList.size();
        }

        @Override
        public Object getItem(int position) {
            return countryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_state_row, parent, false);
            }
            progressBarHandler.show();
            TextView name = (TextView) convertView.findViewById(R.id.stateName);
            Country fetchedList =  allCountryListfromServer.get(position);
            if(allCountryListfromServer!=null)
                name.setText(fetchedList.getcountryName());
            progressBarHandler.hide();
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults oReturn = new FilterResults();
                    final List<Country> results = new ArrayList<Country>();
                    if (orig == null)
                        orig = countryList;
                    if (constraint != null) {
                        if (orig != null && orig.size() > 0) {
                            for (final Country g : orig)
                            {
                                if (g.getcountryName().toLowerCase().contains(constraint.toString()))
                                    results.add(g);
                            }
                        }
                        oReturn.values = results;
                    }
                    return oReturn;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    countryList = (ArrayList<Country>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public void notifyDataSetChanged()
        {
            super.notifyDataSetChanged();
        }

    }

}
