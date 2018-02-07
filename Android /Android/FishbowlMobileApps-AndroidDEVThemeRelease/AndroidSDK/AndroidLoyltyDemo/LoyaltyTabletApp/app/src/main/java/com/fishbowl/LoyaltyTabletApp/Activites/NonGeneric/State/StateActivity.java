package com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.State;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.States;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by schaudhary_ic on 24-Oct-16.
 */

public class StateActivity extends Activity implements SearchView.OnQueryTextListener {
    public List<States> allStateListfromServer = new ArrayList<States>();
    public ListView list;
    StateListPopulationAdapter adapter;
    ProgressBarHandler progressBarHandler;
    Timer t = new Timer();
    Integer idState;
    String codeState;
    String nameState;
    SearchView searchview;
    EditText et;
    String CountryCode;
    RelativeLayout mtoolbar;
    TextView toolbar_title;
    private ImageLoader mImageLoader;
    private NetworkImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {

            CountryCode = extras.getString("CountryCode");
        }
        progressBarHandler = new ProgressBarHandler(this);

        setContentView(R.layout.activity_states_view);

        searchview = (SearchView) findViewById(R.id.searchView);
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
        background = (NetworkImageView) findViewById(R.id.img_Back);

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



        mtoolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        toolbar_title = (TextView) mtoolbar.findViewById(R.id.title_text);
        ;
        mtoolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StateActivity.this.finish();
            }
        });

        list = (ListView) findViewById(R.id.states_list_data);
        if (allStateListfromServer != null) {
            adapter = new StateListPopulationAdapter(this, allStateListfromServer);
            list.setAdapter(adapter);

        }
        list.setTextFilterEnabled(true);
        setupSearchView();
        list.setAdapter(adapter);

        adapter.notifyDataSetChanged();

//        TimerTask task = new TimerTask() {
//
//            @Override
//            public void run() {
//
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        adapter.notifyDataSetChanged();
//
//                    }
//                });
//            }
//        };
//
//        t.scheduleAtFixedRate(task, 0, 1000);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                States selectedState = allStateListfromServer.get(position);
                idState = selectedState.getStateID();
                codeState = selectedState.getStateCode();
                nameState = selectedState.getStateName();
                System.out.println(nameState);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("State", nameState);
                returnIntent.putExtra("StateCode", codeState);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getState();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }

    private void setupSearchView() {
        searchview.setIconifiedByDefault(false);
        searchview.setOnQueryTextListener(this);
        searchview.setSubmitButtonEnabled(true);
        searchview.setQueryHint("Search Here");


        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }


    public void getState() {
        progressBarHandler.show();
        JSONObject object = new JSONObject();
        FB_LY_UserService.sharedInstance().getState(object, new FB_LY_UserService.FBStateCallback() {
            @Override
            public void onStateCallback(JSONObject response, Exception error) {

                try {
                    if (error == null && response != null) {
                        if (!response.has("stateList"))
                            return;

                        JSONArray getArrayStates = response.getJSONArray("stateList");
                        if (getArrayStates != null) {

                            for (int i = 0; i < getArrayStates.length(); i++) {
                                JSONObject myStoresObj = getArrayStates.getJSONObject(i);
                                States statesListObj = new States(myStoresObj);
                                allStateListfromServer.add(statesListObj);


                            }
                              adapter.notifyDataSetChanged();

                        }
                        progressBarHandler.dismiss();

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

    public class StateListPopulationAdapter extends BaseAdapter implements Filterable {

        public List<States> statesList;
        public Context context;
        public List<States> orig;

        public StateListPopulationAdapter(Context context, List<States> statesList) {
            super();
            this.context = context;
            this.statesList = statesList;
        }

        @Override
        public int getCount() {
            return statesList.size();
        }

        @Override
        public Object getItem(int position) {
            return statesList.get(position);
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
            States fetchedList = allStateListfromServer.get(position);
            if (allStateListfromServer != null)
                name.setText(fetchedList.getStateName());
            progressBarHandler.dismiss();
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults oReturn = new FilterResults();
                    final List<States> results = new ArrayList<States>();
                    if (orig == null)
                        orig = statesList;
                    if (constraint != null) {
                        if (orig != null && orig.size() > 0) {
                            for (final States g : orig) {
                                if (g.getStateName().toLowerCase().contains(constraint.toString().toLowerCase()))
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
                    statesList = (ArrayList<States>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

    }

}
