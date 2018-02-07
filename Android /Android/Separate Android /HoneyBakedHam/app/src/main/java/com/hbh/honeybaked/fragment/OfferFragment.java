package com.hbh.honeybaked.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Services.FBUserOfferService;
import com.fishbowl.basicmodule.Services.FBUserOfferService.FBOfferCallback;
import com.fishbowl.basicmodule.Services.FBUserOfferService.FBRewardCallback;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.adapter.OfferAdapter;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.constants.PreferenceConstants;
import com.hbh.honeybaked.fbsupportingfiles.FBAnalyticsManager;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.module.OfferModule;
import com.hbh.honeybaked.supportingfiles.Utility;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class OfferFragment extends BaseFragment {
    int list_flag = 0;
    TextView no_offer_tv;
    ArrayList<OfferModule> offer_List = new ArrayList();
    ListView offer_lv;
    TextView offers_count_tv;
    LinearLayout offers_rl;
    ArrayList<OfferModule> reward_List = new ArrayList();
    TextView rewards_count_tv;
    LinearLayout rewards_rl;
    OfferAdapter simpleAdapter = null;

    class C17611 implements OnItemClickListener {
        C17611() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (OfferFragment.this.list_flag == 0) {
                FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(((OfferModule) OfferFragment.this.reward_List.get(position)).getCampaignId()), ((OfferModule) OfferFragment.this.reward_List.get(position)).getCampaignTitle(), FBEventSettings.OPEN_APP_REWARD);
                if (!Utility.isEmptyString(((OfferModule) OfferFragment.this.reward_List.get(position)).getCouponURL())) {
                    OfferFragment.this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SHOPONLINE, new MenuModel("MY COUPON", "", ((OfferModule) OfferFragment.this.reward_List.get(position)).getCouponURL()));
                    return;
                } else if (Utility.isEmptyString(((OfferModule) OfferFragment.this.reward_List.get(position)).getPromotionCode())) {
                    Toast.makeText(OfferFragment.this.getContext(), "No Coupon Available.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    OfferFragment.this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SHOW_PROMOCODE, new MenuModel(((OfferModule) OfferFragment.this.reward_List.get(position)).getCampaignTitle(), ((OfferModule) OfferFragment.this.reward_List.get(position)).getCampaignDescription(), ((OfferModule) OfferFragment.this.reward_List.get(position)).getPromotionCode()));
                    return;
                }
            }
            FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(((OfferModule) OfferFragment.this.offer_List.get(position)).getCampaignId()), ((OfferModule) OfferFragment.this.offer_List.get(position)).getCampaignTitle(), FBEventSettings.OPEN_APP_OFFER);
            if (!Utility.isEmptyString(((OfferModule) OfferFragment.this.offer_List.get(position)).getCouponURL())) {
                OfferFragment.this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SHOPONLINE, new MenuModel("MY COUPON", "", ((OfferModule) OfferFragment.this.offer_List.get(position)).getCouponURL()));
            } else if (Utility.isEmptyString(((OfferModule) OfferFragment.this.offer_List.get(position)).getPromotionCode())) {
                Toast.makeText(OfferFragment.this.getContext(), "No Coupon Available.", Toast.LENGTH_LONG).show();
            } else {
                OfferFragment.this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SHOW_PROMOCODE, new MenuModel(((OfferModule) OfferFragment.this.offer_List.get(position)).getCampaignTitle(), ((OfferModule) OfferFragment.this.offer_List.get(position)).getCampaignDescription(), ((OfferModule) OfferFragment.this.offer_List.get(position)).getPromotionCode()));
            }
        }
    }

    class C17622 implements FBRewardCallback {
        C17622() {
        }

        public void OnFBRewardCallback(JSONObject rewards_response, Exception error) {
            if (rewards_response != null) {
                if (rewards_response.has("successFlag")) {
                    if (rewards_response.optBoolean("successFlag")) {
                        OfferFragment.this.hb_dbHelper.deleteTable("hbha_reward_table");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
                        JSONArray reward_arr = rewards_response.optJSONArray("inAppOfferList");
                        for (int rew = 0; rew < reward_arr.length(); rew++) {
                            JSONObject reward_obj = reward_arr.optJSONObject(rew);
                            int campaignId = reward_obj.optInt("campaignId");
                            String campaignTitle = reward_obj.optString("campaignTitle");
                            String campaignDescription = reward_obj.optString("campaignDescription");
                            String validityEndDateTime = reward_obj.optString("validityEndDateTime");
                            int promotionID = reward_obj.optInt("promotionID");
                            int channelTypeID = reward_obj.optInt("channelTypeID");
                            boolean isPMOffer = reward_obj.optBoolean("channelTypeID");
                            int mailingId = reward_obj.optInt("mailingId");
                            int templateId = reward_obj.optInt("templateId");
                            int channelId = reward_obj.optInt("channelId");
                            String campaignType = reward_obj.optString("campaignType");
                            String couponURL = reward_obj.optString("couponURL");
                            String htmlBody = reward_obj.optString("htmlBody");
                            String promotionCode = reward_obj.optString("promotionCode");
                            int isPMOffer_val = isPMOffer ? 1 : 0;
                            String load_url = "";
                            if (!Utility.isEmptyString(couponURL)) {
                                load_url = couponURL;
                            } else if (!Utility.isEmptyString(htmlBody)) {
                                load_url = AppConstants.HTML_TAG.replace("add_body", htmlBody);
                            }
                            String sDate = "";
                            if (!Utility.isEmptyString(validityEndDateTime)) {
                                try {
                                    Date date;
                                    String[] date_time_arr = validityEndDateTime.split("\\s+");
                                    if (date_time_arr[0].contains("/")) {
                                        date = simpleDateFormat.parse(date_time_arr[0]);
                                    } else {
                                        date = simpleDateFormat.parse(date_time_arr[0]);
                                    }
                                    sDate = simpleDateFormat.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } catch (java.text.ParseException e2) {
                                    e2.printStackTrace();
                                } catch (Throwable th) {
                                    OfferFragment.this.dbRewards();
                                    OfferFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                                }
                            }
                            OfferFragment.this.hb_dbHelper.insertRewardsDetails(campaignId, campaignTitle, campaignDescription, sDate, promotionID, channelTypeID, isPMOffer_val, mailingId, templateId, channelId, campaignType, load_url, promotionCode);
                        }
                    }
                }
                OfferFragment.this.dbRewards();
                OfferFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                return;
            }
            OfferFragment.this.dbRewards();
            OfferFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
            Utility.tryHandleTokenExpiry(OfferFragment.this.getActivity(), error);
        }
    }

    class C17633 implements FBOfferCallback {
        C17633() {
        }

        @RequiresApi(api = 19)
        public void OnFBOfferCallback(JSONObject offer_response, Exception error) {
            if (offer_response != null) {
                if (offer_response.has("successFlag")) {
                    if (offer_response.optBoolean("successFlag")) {
                        OfferFragment.this.hb_dbHelper.deleteTable("hbha_offer_table");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
                        JSONArray offer_arr = offer_response.optJSONArray("inAppOfferList");
                        for (int off = 0; off < offer_arr.length(); off++) {
                            JSONObject offer_obj = offer_arr.optJSONObject(off);
                            int campaignId = offer_obj.optInt("campaignId");
                            String campaignTitle = offer_obj.optString("campaignTitle");
                            String campaignDescription = offer_obj.optString("campaignDescription");
                            String validityEndDateTime = offer_obj.optString("validityEndDateTime");
                            int promotionID = offer_obj.optInt("promotionID");
                            int channelTypeID = offer_obj.optInt("channelTypeID");
                            boolean isPMOffer = offer_obj.optBoolean("channelTypeID");
                            int mailingId = offer_obj.optInt("mailingId");
                            int templateId = offer_obj.optInt("templateId");
                            int channelId = offer_obj.optInt("channelId");
                            String campaignType = offer_obj.optString("campaignType");
                            String couponURL = offer_obj.optString("couponURL");
                            String htmlBody = offer_obj.optString("htmlBody");
                            String promotionCode = offer_obj.optString("promotionCode");
                            String load_url = "";
                            int isPMOffer_val = isPMOffer ? 1 : 0;
                            if (!Utility.isEmptyString(couponURL)) {
                                load_url = couponURL;
                            } else if (!Utility.isEmptyString(htmlBody)) {
                                load_url = AppConstants.HTML_TAG.replace("add_body", htmlBody);
                            }
                            String sDate = "";
                            if (!Utility.isEmptyString(validityEndDateTime)) {
                                try {
                                    Date date;
                                    String[] date_time_arr = validityEndDateTime.split("\\s+");
                                    if (date_time_arr[0].contains("/")) {
                                        date = simpleDateFormat.parse(date_time_arr[0]);
                                    } else {
                                        date = simpleDateFormat.parse(date_time_arr[0]);
                                    }
                                    sDate = simpleDateFormat.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } catch (java.text.ParseException e2) {
                                    e2.printStackTrace();
                                } catch (Throwable th) {
                                    OfferFragment.this.dbOffer(true);
                                    OfferFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                                }
                            }
                            OfferFragment.this.hb_dbHelper.insertOfferDetails(campaignId, campaignTitle, campaignDescription, sDate, promotionID, channelTypeID, isPMOffer_val, mailingId, templateId, channelId, campaignType, load_url, promotionCode);
                        }
                    }
                }
                OfferFragment.this.dbOffer(true);
                OfferFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                return;
            }
            OfferFragment.this.dbOffer(true);
            OfferFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
            Utility.tryHandleTokenExpiry(OfferFragment.this.getActivity(), error);
        }
    }

//    private class getMobileAppEventAsyncTask extends AsyncTask<String, Void, String> {
//        int f19a = 0;
//
//        public getMobileAppEventAsyncTask(int a) {
//            this.f19a = a;
//        }
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//            OfferFragment.this.performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
//        }
//
//        protected String doInBackground(String... params) {
//            JSONObject mobile_event_object = new JSONObject();
//            JSONArray mobile_event_arr = new JSONArray();
//            try {
//                JSONObject mobile_event_prod_obj = new JSONObject();
//                mobile_event_prod_obj.put(Param.ITEM_ID, this.f19a);
//                mobile_event_prod_obj.put(Param.ITEM_NAME, "ProductCount=1 Total=4.87");
//                mobile_event_prod_obj.put(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY, FBEventSettings.OPEN_APP_OFFER);
//                mobile_event_prod_obj.put(NativeProtocol.WEB_DIALOG_ACTION, "AppEvent");
//                mobile_event_prod_obj.put("memberid", OfferFragment.this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_CUSTOMER_ID));
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
//            return new JsonParser().getMobileEventsFromUrl(Constants.sdkPointingUrl(10) + "event/submitallappevents", FBUtility.getAndroidDeviceID(OfferFragment.this.getActivity()), FBPreferences.sharedInstance(OfferFragment.this.getActivity()).getAccessTokenforapp(), mobile_event_object);
//        }
//
//        protected void onPostExecute(String s) {
//            if (Utility.isEmptyString(s)) {
//                OfferFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                return;
//            }
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                if (jsonObject == null) {
//                    OfferFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                } else if (jsonObject.optBoolean("successFlag")) {
//                    OfferFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                } else {
//                    OfferFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                    if (jsonObject.has("message")) {
//                        Utility.showToast(OfferFragment.this.getActivity(), jsonObject.optString("message"));
//                    } else {
//                        Utility.showToast(OfferFragment.this.getActivity(), "Error in Login, Please try again later!");
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                OfferFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//            }
//        }
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_offer, container, false);
        this.list_flag = this.hbha_pref_helper.getIntValue(PreferenceConstants.PREFERENCE_REWARDS_OFFERS_PAGE, 0);
        this.hb_dbHelper.openDb();
        initView(v);
        setViewListeners();
        setButtonBackground();
        dbOffer(false);
        if (this.cd.isConnectingToInternet()) {
            getRewards();
        } else {
            dbRewards();
            Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
        }
        this.offer_lv.setOnItemClickListener(new C17611());
        return v;
    }

    private void initView(View v) {
        this.offer_lv = (ListView) v.findViewById(R.id.offer_lv);
        this.offers_count_tv = (TextView) v.findViewById(R.id.offers_count_tv);
        this.rewards_count_tv = (TextView) v.findViewById(R.id.rewards_count_tv);
        this.no_offer_tv = (TextView) v.findViewById(R.id.no_offer_tv);
        this.rewards_rl = (LinearLayout) v.findViewById(R.id.rewards_rl);
        this.offers_rl = (LinearLayout) v.findViewById(R.id.offers_rl);
    }

    private void setViewListeners() {
        this.rewards_rl.setOnClickListener(this);
        this.offers_rl.setOnClickListener(this);
    }

    public void dbRewards() {
        Cursor reward_cursor = this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_reward_table", null);
        this.reward_List.clear();
        if (reward_cursor.getCount() > 0) {
            while (reward_cursor.moveToNext()) {
                this.reward_List.add(new OfferModule(reward_cursor.getInt(0), reward_cursor.getString(1), reward_cursor.getString(2), reward_cursor.getString(3), reward_cursor.getInt(4), reward_cursor.getInt(5), reward_cursor.getInt(6), reward_cursor.getInt(7), reward_cursor.getInt(8), reward_cursor.getInt(9), reward_cursor.getString(10), reward_cursor.getString(11), reward_cursor.getString(12), Utility.getDateDifferent(reward_cursor.getString(3))));
            }
        }
        reward_cursor.close();
        displayList();
    }

    public void dbOffer(boolean isSetAdapter) {
        Cursor offer_cursor = this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_offer_table", null);
        this.offer_List.clear();
        if (offer_cursor.getCount() > 0) {
            while (offer_cursor.moveToNext()) {
                this.offer_List.add(new OfferModule(offer_cursor.getInt(0), offer_cursor.getString(1), offer_cursor.getString(2), offer_cursor.getString(3), offer_cursor.getInt(4), offer_cursor.getInt(5), offer_cursor.getInt(6), offer_cursor.getInt(7), offer_cursor.getInt(8), offer_cursor.getInt(9), offer_cursor.getString(10), offer_cursor.getString(11), offer_cursor.getString(12), Utility.getDateDifferent(offer_cursor.getString(3))));
            }
        }
        offer_cursor.close();
        if (isSetAdapter) {
            displayList();
        }
    }

    public void displayList() {
        if ((this.reward_List.size() == 0 && this.list_flag == 0) || (this.offer_List.size() == 0 && this.list_flag == 1)) {
            showNoResult();
        } else {
            setListAdapter();
        }
        this.rewards_count_tv.setText("" + this.reward_List.size());
        this.offers_count_tv.setText("" + this.offer_List.size());
    }

    private void setListAdapter() {
        this.no_offer_tv.setVisibility(View.GONE);
        this.offer_lv.setVisibility(View.GONE);
        this.offer_lv.invalidate();
        if (this.list_flag == 0) {
            this.simpleAdapter = new OfferAdapter(getActivity(), this.reward_List);
        } else {
            this.simpleAdapter = new OfferAdapter(getActivity(), this.offer_List);
        }
        this.offer_lv.setAdapter(this.simpleAdapter);
    }

    private void showNoResult() {
        this.offer_lv.setVisibility(View.GONE);
        this.no_offer_tv.setVisibility(View.GONE);
        if (this.list_flag == 0) {
            this.no_offer_tv.setText("No Rewards Found.");
        } else {
            this.no_offer_tv.setText("No Offers Found.");
        }
    }

    public void performAdapterAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rewards_rl:
                this.list_flag = 0;
                this.hbha_pref_helper.saveIntValue(PreferenceConstants.PREFERENCE_REWARDS_OFFERS_PAGE, this.list_flag);
                setButtonBackground();
                if (this.cd.isConnectingToInternet()) {
                    getRewards();
                    return;
                }
                dbRewards();
                Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
                return;
            case R.id.offers_rl:
                this.list_flag = 1;
                this.hbha_pref_helper.saveIntValue(PreferenceConstants.PREFERENCE_REWARDS_OFFERS_PAGE, this.list_flag);
                setButtonBackground();
                if (this.cd.isConnectingToInternet()) {
                    getOffer();
                    return;
                }
                dbOffer(true);
                Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
                return;
            default:
                return;
        }
    }

    private void setButtonBackground() {
        if (this.list_flag == 0) {
            this.rewards_rl.setBackgroundColor(Color.parseColor("#AD2F14"));
            this.offers_rl.setBackgroundColor(Color.parseColor("#89201a"));
            return;
        }
        this.rewards_rl.setBackgroundColor(Color.parseColor("#89201a"));
        this.offers_rl.setBackgroundColor(Color.parseColor("#AD2F14"));
    }

    public void getRewards() {
        setProgressDialog(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(true));
        FBUserOfferService.sharedInstance().getUserFBReward(new JSONObject(), MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, new C17622());
    }

    public void getOffer() {
        setProgressDialog(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(true));
        FBUserOfferService.sharedInstance().getUserFBOffer(new JSONObject(), this.hbha_pref_helper.getStringValue("customerID"), new C17633());
    }
}
