//package com.hbh.honeybaked.fragment;
//
//import android.os.AsyncTask;
//import android.os.Build.VERSION;
//import android.os.Bundle;
//import android.text.Html;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ExpandableListView;
//import android.widget.ExpandableListView.OnGroupExpandListener;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import bolts.MeasurementEvent;
//import com.Preferences.FBPreferences;
//import com.facebook.internal.NativeProtocol;
//import com.fishbowl.basicmodule.Analytics.FBEventSettings;
//import com.fishbowl.basicmodule.Services.FBUserService;
//import com.fishbowl.basicmodule.Services.FBUserService.FBMenuDrawerCallback;
//import com.fishbowl.basicmodule.Services.FBUserService.FBMenuSubCategoryCallback;
//import com.fishbowl.basicmodule.Utils.FBConstant;
//import com.fishbowl.basicmodule.Utils.FBUtility;
//import com.google.firebase.analytics.FirebaseAnalytics.Param;
//import com.hbh.honeybaked.R;
//import com.hbh.honeybaked.adapter.GridMenuAdapter;
//import com.hbh.honeybaked.base.BaseFragment;
//import com.hbh.honeybaked.constants.AppConstants;
//import com.hbh.honeybaked.constants.Constants;
//import com.hbh.honeybaked.constants.PreferenceConstants;
//import com.hbh.honeybaked.fbsupportingfiles.FBAnalyticsManager;
//import com.hbh.honeybaked.module.GridModule;
//import com.hbh.honeybaked.supportingfiles.JsonParser;
//import com.hbh.honeybaked.supportingfiles.Utility;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class GridRecipesFragment extends BaseFragment {
//    int childCount;
//    LinearLayout grid_Lay;
//    HashMap<String, List<GridModule>> grid_child_data;
//    List<String> grid_header_data;
//    GridMenuAdapter grid_list_adapter;
//    ExpandableListView grid_recipe_exp_lv;
//    int groupPosition = 0;
//    TextView no_data_tv;
//    int pc_id = 0;
//    int pf_id = 0;
//    int width;
//
//    class C17321 implements OnGroupExpandListener {
//        int previousItem;
//
//        C17321() {
//        }
//
//        public void onGroupExpand(int groupPosition) {
//            if (groupPosition != this.previousItem) {
//                GridRecipesFragment.this.grid_recipe_exp_lv.collapseGroup(this.previousItem);
//                this.previousItem = groupPosition;
//            }
//        }
//    }
//
//    class C17342 implements FBMenuSubCategoryCallback {
//        C17342() {
//        }
//
//        public void onMenuSubCategoryCallback(JSONObject response, Exception error) {
//            GridRecipesFragment.this.grid_header_data.clear();
//            GridRecipesFragment.this.grid_child_data.clear();
//            GridRecipesFragment.this.childCount = 0;
//            if (response != null) {
//                try {
//                    if (!response.has("successFlag")) {
//                        GridRecipesFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                        GridRecipesFragment.this.grid_recipe_exp_lv.setVisibility(8);
//                        GridRecipesFragment.this.no_data_tv.setText("No Menus Available");
//                        GridRecipesFragment.this.no_data_tv.setVisibility(0);
//                        return;
//                    } else if (response.optBoolean("successFlag")) {
//                        JSONArray sc_arr = response.getJSONArray("productSubCategoryList");
//                        for (int pca = 0; pca < sc_arr.length(); pca++) {
//                            JSONObject sc_obj = sc_arr.getJSONObject(pca);
//                            int pro_sc_id = sc_obj.optInt("productSubCategoryId");
//                            GridRecipesFragment.this.grid_header_data.add(String.valueOf(pro_sc_id) + "######" + sc_obj.optString("productSubCategoryName"));
//                        }
//                        if (GridRecipesFragment.this.grid_header_data.size() > 0) {
//                            for (int i = 0; i < GridRecipesFragment.this.grid_header_data.size(); i++) {
//                                String[] sHeader = ((String) GridRecipesFragment.this.grid_header_data.get(i)).split("######");
//                                if (GridRecipesFragment.this.cd.isConnectingToInternet()) {
//                                    String pro_sc_id2 = sHeader[0];
//                                    String str = sHeader[1];
//                                    final int finalI = i;
//                                    FBUserService.sharedInstance().getDrawerProductList(new JSONObject(), GridRecipesFragment.this.hbha_pref_helper.getStringValue("store_id"), String.valueOf(GridRecipesFragment.this.pc_id), pro_sc_id2, new FBMenuDrawerCallback() {
//                                        public void onMenuDrawerCallback(JSONObject response, Exception error) {
//                                            GridRecipesFragment gridRecipesFragment = GridRecipesFragment.this;
//                                            gridRecipesFragment.childCount++;
//                                            if (response != null) {
//                                                try {
//                                                    if (response.has("successFlag")) {
//                                                        if (response.optBoolean("successFlag")) {
//                                                            JSONArray product_list_arr = response.getJSONArray("productList");
//                                                            List<GridModule> menu_drw = new ArrayList();
//                                                            for (int p_name = 0; p_name < product_list_arr.length(); p_name++) {
//                                                                JSONObject product_list_arrJSONObject = product_list_arr.getJSONObject(p_name);
//                                                                menu_drw.add(new GridModule(product_list_arrJSONObject.optInt("id"), product_list_arrJSONObject.optString("productName"), Html.fromHtml(product_list_arrJSONObject.optString("productImageUrl")).toString(), product_list_arrJSONObject.optDouble("productBasePrice"), Html.fromHtml(product_list_arrJSONObject.optString("productLongDescription")).toString(), Html.fromHtml(product_list_arrJSONObject.optString("productShortDescription")).toString()));
//                                                            }
//                                                            GridRecipesFragment.this.grid_child_data.put(GridRecipesFragment.this.grid_header_data.get(finalI), menu_drw);
//                                                        }
//                                                    }
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                            GridRecipesFragment.this.setAdaptertoExpandableListView(GridRecipesFragment.this.groupPosition);
//                                        }
//                                    });
//                                } else {
//                                    Utility.showToast(GridRecipesFragment.this.getActivity(), AppConstants.NO_CONNECTION_TEXT);
//                                }
//                            }
//                            return;
//                        }
//                        GridRecipesFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                        return;
//                    } else {
//                        GridRecipesFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                        GridRecipesFragment.this.grid_recipe_exp_lv.setVisibility(8);
//                        GridRecipesFragment.this.no_data_tv.setText("No Menus Available");
//                        GridRecipesFragment.this.no_data_tv.setVisibility(0);
//                        return;
//                    }
//                } catch (Exception e) {
//                    return;
//                }
//            }
//            GridRecipesFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//            GridRecipesFragment.this.grid_recipe_exp_lv.setVisibility(8);
//            GridRecipesFragment.this.no_data_tv.setText("No Menus Available");
//            GridRecipesFragment.this.no_data_tv.setVisibility(0);
//        }
//    }
//
//    private class getMobileAppEventAsyncTask extends AsyncTask<String, Void, String> {
//        int f18a = 0;
//
//        public getMobileAppEventAsyncTask(int a) {
//            this.f18a = a;
//        }
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//            GridRecipesFragment.this.performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
//        }
//
//        protected String doInBackground(String... params) {
//            JSONObject mobile_event_object = new JSONObject();
//            JSONArray mobile_event_arr = new JSONArray();
//            try {
//                JSONObject mobile_event_prod_obj = new JSONObject();
//                mobile_event_prod_obj.put(Param.ITEM_ID, String.valueOf(this.f18a));
//                mobile_event_prod_obj.put(Param.ITEM_NAME, "ProductCount=1 Total=4.87");
//                mobile_event_prod_obj.put(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY, FBEventSettings.PRODUCT_CLICK);
//                mobile_event_prod_obj.put(NativeProtocol.WEB_DIALOG_ACTION, "AppEvent");
//                mobile_event_prod_obj.put("memberid", GridRecipesFragment.this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_CUSTOMER_ID));
//                mobile_event_prod_obj.put("lat", "28.6154469");
//                mobile_event_prod_obj.put("lon", "77.3906964");
//                mobile_event_prod_obj.put("device_type", "ANDROID");
//                mobile_event_prod_obj.put("tenantid", FBConstant.client_tenantid);
//                mobile_event_prod_obj.put("device_os_ver", "5.0");
//                mobile_event_arr.put(0, mobile_event_prod_obj);
//                mobile_event_object.put("mobileAppEvent", mobile_event_arr);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return new JsonParser().getMobileEventsFromUrl(Constants.sdkPointingUrl(10) + "event/submitallappevents", FBUtility.getAndroidDeviceID(GridRecipesFragment.this.getActivity()), FBPreferences.sharedInstance(GridRecipesFragment.this.getActivity()).getAccessTokenforapp(), mobile_event_object);
//        }
//
//        protected void onPostExecute(String s) {
//            if (Utility.isEmptyString(s)) {
//                GridRecipesFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                return;
//            }
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                if (jsonObject == null) {
//                    GridRecipesFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                } else if (jsonObject.optBoolean("successFlag")) {
//                    GridRecipesFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                } else {
//                    GridRecipesFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                    if (jsonObject.has("message")) {
//                        Utility.showToast(GridRecipesFragment.this.getActivity(), jsonObject.optString("message"));
//                    } else {
//                        Utility.showToast(GridRecipesFragment.this.getActivity(), "Error in Login, Please try again later!");
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                GridRecipesFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//            }
//        }
//    }
//
//    public static GridRecipesFragment newInstances(int pf_id, int pc_id, ArrayList<String> grid_header_data, HashMap<String, List<GridModule>> grid_child_data, int groupPosition) {
//        GridRecipesFragment gridRecipesFragment = new GridRecipesFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("pf_id", pf_id);
//        bundle.putInt("pc_id", pc_id);
//        bundle.putSerializable("grid_header_data", grid_header_data);
//        bundle.putSerializable("grid_child_data", grid_child_data);
//        bundle.putInt("groupPosition", groupPosition);
//        gridRecipesFragment.setArguments(bundle);
//        return gridRecipesFragment;
//    }
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_grid_recipe, container, false);
//        this.grid_Lay = (LinearLayout) v.findViewById(R.id.grid_rec_lay);
//        this.no_data_tv = (TextView) v.findViewById(R.id.no_menu_tv);
//        if (getArguments() != null) {
//            this.pf_id = getArguments().getInt("pf_id");
//            this.pc_id = getArguments().getInt("pc_id");
//            this.grid_header_data = (List) getArguments().getSerializable("grid_header_data");
//            this.grid_child_data = (HashMap) getArguments().getSerializable("grid_child_data");
//            this.groupPosition = getArguments().getInt("groupPosition");
//        }
//        this.grid_recipe_exp_lv = (ExpandableListView) v.findViewById(R.id.grid_recipe_exp_lv);
//        this.grid_recipe_exp_lv.setGroupIndicator(null);
//        if (this.grid_header_data == null) {
//            this.grid_header_data = new ArrayList();
//            this.grid_child_data = new HashMap();
//        }
//        if (this.grid_header_data.size() > 0) {
//            this.childCount = this.grid_header_data.size();
//            setAdaptertoExpandableListView(this.groupPosition);
//        } else if (this.cd.isConnectingToInternet()) {
//            getMenuSubCategory();
//        } else {
//            Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
//            this.grid_recipe_exp_lv.setVisibility(8);
//            this.no_data_tv.setText("Please try again later!");
//            this.no_data_tv.setVisibility(0);
//        }
//        DisplayMetrics diaplayMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(diaplayMetrics);
//        this.width = diaplayMetrics.widthPixels;
//        if (VERSION.SDK_INT < 18) {
//            this.grid_recipe_exp_lv.setIndicatorBounds(this.width - GetDipsFromPixel(70), this.width - GetDipsFromPixel(20));
//        } else {
//            this.grid_recipe_exp_lv.setIndicatorBoundsRelative(this.width - GetDipsFromPixel(70), this.width - GetDipsFromPixel(20));
//        }
//        this.grid_recipe_exp_lv.setOnGroupExpandListener(new C17321());
//        return v;
//    }
//
//    private int GetDipsFromPixel(int pixels) {
//        return (int) ((((float) pixels) * getResources().getDisplayMetrics().density) + 0.5f);
//    }
//
//    public void performAdapterAction(String tagName, Object data) {
//        if (tagName.equals(AppConstants.GRID_CLICK)) {
//            GridModule gridModule = (GridModule) ((Object[]) data)[1];
//            FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(gridModule.getProductId()), gridModule.getsProductName(), FBEventSettings.PRODUCT_CLICK);
//            if (data != null) {
//                this.fragmentActivityListener.performFragmentActivityAction(AppConstants.MENUPAGE_DETAILS, new Object[]{data, Integer.valueOf(this.pf_id), Integer.valueOf(this.pc_id)});
//                return;
//            }
//            return;
//        }
//        super.performAdapterAction(tagName, data);
//    }
//
//    public void onClick(View v) {
//    }
//
//    public void getMenuSubCategory() {
//        performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
//        FBUserService.sharedInstance().getMenuSubCategory(new JSONObject(), this.hbha_pref_helper.getStringValue("store_id"), String.valueOf(this.pc_id), new C17342());
//    }
//
//    private void setAdaptertoExpandableListView(int position) {
//        if (this.childCount == this.grid_header_data.size()) {
//            performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//            if (getActivity() != null) {
//                this.grid_list_adapter = new GridMenuAdapter(getActivity(), this, this.grid_header_data, this.grid_child_data);
//                this.grid_recipe_exp_lv.setAdapter(this.grid_list_adapter);
//            }
//            this.grid_recipe_exp_lv.expandGroup(position);
//        }
//    }
//}
