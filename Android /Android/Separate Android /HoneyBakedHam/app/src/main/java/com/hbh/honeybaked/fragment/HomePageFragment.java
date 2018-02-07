package com.hbh.honeybaked.fragment;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fishbowl.basicmodule.Services.FBUserOfferService;
import com.fishbowl.basicmodule.Services.FBUserOfferService.FBOfferCallback;
import com.fishbowl.basicmodule.Services.FBUserOfferService.FBRewardCallback;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.ApiConstants;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.constants.PreferenceConstants;
import com.hbh.honeybaked.listener.AdapterListener;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.supportingfiles.CircularTextView;
import com.hbh.honeybaked.supportingfiles.Utility;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class HomePageFragment extends BaseFragment {
    protected int height;
    ImageView home_qr_img_vw;
    TextView home_qr_tv;
    LinearLayout homelist_linearLayout;
    private int itemHeight;
    ArrayList<MenuModel> menu_list_ = new ArrayList();
    int offer_cur_count = 0;
    Timer swipeTimer;
    String text_ = "Show your MY HONEYBAKED HAM Loyalty ID to the register associate at every visit to earn points for your HoneyBaked Ham purchases!";
    protected int width;

    class C17351 implements FBRewardCallback {
        C17351() {
        }

        public void OnFBRewardCallback(JSONObject rewards_response, Exception error)  {
            if (rewards_response != null) {
                if (rewards_response.has("successFlag")) {
                    if (rewards_response.optBoolean("successFlag")) {
                        HomePageFragment.this.hb_dbHelper.deleteTable("hbha_reward_table");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
                        JSONArray reward_arr = rewards_response.optJSONArray("inAppOfferList");
                        if (reward_arr != null) {
                            for (int rew = 0; rew < reward_arr.length(); rew++) {
                                JSONObject reward_obj = null;
                                try {
                                    reward_obj = reward_arr.getJSONObject(rew);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                                        try {
                                            e.printStackTrace();
                                        } catch (Throwable th) {
                                            HomePageFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                                        }
                                    } catch (java.text.ParseException e3) {
                                        e3.printStackTrace();
                                    }
                                }
                                HomePageFragment.this.hb_dbHelper.insertRewardsDetails(campaignId, campaignTitle, campaignDescription, sDate, promotionID, channelTypeID, isPMOffer_val, mailingId, templateId, channelId, campaignType, load_url, promotionCode);
                            }
                        }
                    }
                }
                HomePageFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
            } else {
                HomePageFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                Utility.tryHandleTokenExpiry(HomePageFragment.this.getActivity(), error);
            }
            if (HomePageFragment.this.cd.isConnectingToInternet()) {
                HomePageFragment.this.getOffer();
            } else {
                Utility.showToast(HomePageFragment.this.getActivity(), AppConstants.NO_CONNECTION_TEXT);
            }
        }
    }

    class C17362 implements FBOfferCallback {
        C17362() {
        }

        @RequiresApi(api = 19)
        public void OnFBOfferCallback(JSONObject offer_response, Exception error) {
            Cursor rewards_cursor;
            Cursor offer_cursor;
            if (offer_response != null) {
                if (offer_response.has("successFlag")) {
                    if (offer_response.optBoolean("successFlag")) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
                        HomePageFragment.this.hb_dbHelper.deleteTable("hbha_offer_table");
                        JSONArray offer_arr = null;
                        try {
                            offer_arr = offer_response.getJSONArray("inAppOfferList");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (offer_arr != null) {
                            for (int off = 0; off < offer_arr.length(); off++) {
                                JSONObject offer_obj = null;
                                try {
                                    offer_obj = offer_arr.getJSONObject(off);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                                JSONArray storeRestriction = offer_obj.optJSONArray("storeRestriction");
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
                                        try {
                                            e.printStackTrace();
                                        } catch (Throwable th) {
                                            rewards_cursor = HomePageFragment.this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_reward_table", null);
                                            offer_cursor = HomePageFragment.this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_offer_table", null);
                                            HomePageFragment.this.offer_cur_count = rewards_cursor.getCount() + offer_cursor.getCount();
                                            offer_cursor.close();
                                            rewards_cursor.close();
                                            HomePageFragment.this.setAdpater();
                                            HomePageFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                                        }
                                    } catch (java.text.ParseException e3) {
                                        e3.printStackTrace();
                                    }
                                }
                                HomePageFragment.this.hb_dbHelper.insertOfferDetails(campaignId, campaignTitle, campaignDescription, sDate, promotionID, channelTypeID, isPMOffer_val, mailingId, templateId, channelId, campaignType, load_url, promotionCode);
                            }
                        }
                    }
                }
                rewards_cursor = HomePageFragment.this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_reward_table", null);
                offer_cursor = HomePageFragment.this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_offer_table", null);
                HomePageFragment.this.offer_cur_count = rewards_cursor.getCount() + offer_cursor.getCount();
                offer_cursor.close();
                rewards_cursor.close();
                HomePageFragment.this.setAdpater();
                HomePageFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
                return;
            }
            HomePageFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
        }
    }

    class C17373 implements OnTouchListener {
        C17373() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            ImageView img = (ImageView) v.findViewWithTag("menu_icon_img_vw");
            ImageView img1 = (ImageView) v.findViewWithTag("right_arrow");
            CircularTextView circularTextView = (CircularTextView) v.findViewWithTag("circularview");
            TextView menu_title_tv = (TextView) v.findViewWithTag("menu_title_tv");
            if (event.getAction() == 0) {
                circularTextView.setSelection(true);
                menu_title_tv.setTextColor(-1);
                HomePageFragment.this.setColor(-1, img);
                HomePageFragment.this.setColor(-1, img1);
            } else if (event.getAction() == 3 || event.getAction() == 1) {
                circularTextView.setSelection(false);
                menu_title_tv.setTextColor(HomePageFragment.this.getActivity().getResources().getColor(R.color.ham_burg_new));
                HomePageFragment.this.setDefault(img);
                HomePageFragment.this.setDefault(img1);
            }
            return false;
        }
    }

    class C17395 implements ImageListener {
        C17395() {
        }

        public void onResponse(ImageContainer imageContainer, boolean b) {
            Bitmap rBitamp = imageContainer.getBitmap();
            if (rBitamp != null) {
                HomePageFragment.this.home_qr_img_vw.setImageBitmap(rBitamp);
                HomePageFragment.this.home_qr_img_vw.setVisibility(View.GONE);
                HomePageFragment.this.home_qr_img_vw.invalidate();
                if (rBitamp.isRecycled()) {
                    rBitamp.recycle();
                }
            }
        }

        public void onErrorResponse(VolleyError volleyError) {
            HomePageFragment.this.home_qr_img_vw.setVisibility(View.GONE);
        }
    }

//    private class getMemberProfileAsyncTask extends AsyncTask<String, Void, String> {
//        private getMemberProfileAsyncTask() {
//        }
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//            HomePageFragment.this.performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
//        }
//
//        protected String doInBackground(String... params) {
//            return new JSONParser().getStringFromUrl(Constants.sdkPointingUrl(10) + "/member/getMemberProfileForSDK", FBUtility.getAndroidDeviceID(HomePageFragment.this.getActivity()), FBPreferences.sharedInstance(HomePageFragment.this.getActivity()).getAccessTokenforapp());
//        }
//
//        protected void onPostExecute(String s) {
//            if (Utility.isEmptyString(s)) {
//                HomePageFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                return;
//            }
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                if (jsonObject == null) {
//                    HomePageFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                } else if (jsonObject.optBoolean("successFlag")) {
//                    try {
//                        FBUserService.sharedInstance().member.initWithJson(jsonObject, HomePageFragment.this.getActivity());
//                        FBPreferences.sharedInstance(HomePageFragment.this.getActivity()).setUserMemberforAppId(Utility.getStringValue(jsonObject, "customerID"));
//                        String formattedDate1 = "";
//                        if (!Utility.isEmptyString(Utility.getStringValue(jsonObject, "previousLoginTime"))) {
//                            String[] last_visit_arr = Utility.getStringValue(jsonObject, "previousLoginTime").split("\\s+");
//                            try {
//                                formattedDate1 = new SimpleDateFormat("MM/dd/yy").format(new SimpleDateFormat("yyyy-MM-dd").parse(last_visit_arr[0]));
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            } catch (java.text.ParseException e2) {
//                                e2.printStackTrace();
//                            }
//                        }
//                        HomePageFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LASTLOGINTIME, formattedDate1);
//                    } catch (Exception e3) {
//                        e3.printStackTrace();
//                    } finally {
//                        HomePageFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                    }
//                } else {
//                    HomePageFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//                    if (jsonObject.has("message")) {
//                        Utility.showToast(HomePageFragment.this.getActivity(), jsonObject.optString("message"));
//                    } else {
//                        Utility.showToast(HomePageFragment.this.getActivity(), "Error in Login, Please try again later!");
//                    }
//                }
//            } catch (Exception e32) {
//                e32.printStackTrace();
//                HomePageFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//            }
//        }
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        this.homelist_linearLayout = (LinearLayout) v.findViewById(R.id.homelist_linearLayout);
        this.home_qr_tv = (TextView) v.findViewById(R.id.home_qr_tv);
        this.home_qr_img_vw = (ImageView) v.findViewById(R.id.home_qr_img_vw);
        this.hb_dbHelper.openDb();
        this.menu_list_.clear();
        int[] heightWidthArray = Utility.getWindowHeightWidth(getActivity());
        this.width = heightWidthArray[0];
        this.height = heightWidthArray[1];
        SpannableStringBuilder sb = new SpannableStringBuilder(this.text_);
        sb.setSpan(new StyleSpan(1), 10, 39, 18);
        this.home_qr_tv.setText(sb);
        setQrImage();
        //new getMemberProfileAsyncTask().execute(new String[0]);
        if (this.cd.isConnectingToInternet()) {
            getRewards();
        } else {
            Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
        }
        Cursor cursor = this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_menu_table", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                this.menu_list_.add(new MenuModel(cursor.getString(1), cursor.getString(3), cursor.getString(4)));
            }
        }
        cursor.close();
        Cursor rewards_cursor1 = this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_reward_table", null);
        Cursor offer_cursor1 = this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_offer_table", null);
        this.offer_cur_count = rewards_cursor1.getCount() + offer_cursor1.getCount();
        offer_cursor1.close();
        rewards_cursor1.close();
        setAdpater();
        return v;
    }

    public void getRewards() {
        setProgressDialog(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        FBUserOfferService.sharedInstance().getUserFBReward(new JSONObject(), MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, new C17351());
    }

    public void getOffer() {
        setProgressDialog(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        FBUserOfferService.sharedInstance().getUserFBOffer(new JSONObject(), this.hbha_pref_helper.getStringValue("customerID"), new C17362());
    }

    private void setAdpater() {
        this.homelist_linearLayout.removeAllViews();
        final AdapterListener adapterListener = (AdapterListener) getActivity();
        for (int position = 0; position < this.menu_list_.size(); position++) {
            View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.home_page_menu_custom_lay, this.homelist_linearLayout, false);
            View view_divider = convertView.findViewById(R.id.view_divider);
            LinearLayout menu_ll = (LinearLayout) convertView.findViewById(R.id.menu_ll);
            LinearLayout main_layout = (LinearLayout) convertView.findViewById(R.id.main_layout);
            TextView menu_store_tv = (TextView) convertView.findViewById(R.id.menu_store_tv);
            ImageView menu_icon_img_vw = (ImageView) convertView.findViewById(R.id.menu_icon_img_vw);
            ImageView right_arrow = (ImageView) convertView.findViewById(R.id.right_arrow);
            TextView menu_title_tv = (TextView) convertView.findViewById(R.id.menu_title_tv);
            CircularTextView circularTextView = (CircularTextView) convertView.findViewById(R.id.circularTextView);
            if (this.menu_list_.size() - 1 == position) {
                view_divider.setVisibility(View.VISIBLE);
            }
            if (position == 1) {
                circularTextView.setVisibility(View.VISIBLE);
                circularTextView.setText("" + this.offer_cur_count);
            } else {
                circularTextView.setVisibility(View.VISIBLE);
            }
            if (position == View.VISIBLE) {
                menu_store_tv.setVisibility(View.VISIBLE);
                String add = this.hbha_pref_helper.getStringValue("store");
                if (add.length() > 0) {
                    String[] add_ = add.split("######");
                    if (Utility.isEmptyString(add_[1].trim())) {
                        menu_store_tv.setText(add_[0].trim());
                    } else {
                        menu_store_tv.setText(add_[0].trim() + "\n" + add_[1].trim());
                    }
                } else {
                    menu_store_tv.setText("Please select a Store");
                }
            } else {
                menu_store_tv.setVisibility(View.VISIBLE);
            }
            menu_title_tv.setText(((MenuModel) this.menu_list_.get(position)).getMenu_title());
            menu_icon_img_vw.setTag("menu_icon_img_vw");
            right_arrow.setTag("right_arrow");
            circularTextView.setTag("circularview");
            menu_title_tv.setTag("menu_title_tv");
            if (((MenuModel) this.menu_list_.get(position)).getMenu_title().equalsIgnoreCase("My Store")) {
                menu_icon_img_vw.setImageResource(R.drawable.location_icon);
            } else if (((MenuModel) this.menu_list_.get(position)).getMenu_title().equalsIgnoreCase(AppConstants.MYOFFER_PAGE)) {
                menu_icon_img_vw.setImageResource(R.drawable.dollar_icon);
            } else if (((MenuModel) this.menu_list_.get(position)).getMenu_title().equalsIgnoreCase("My Loyalty")) {
                menu_icon_img_vw.setImageResource(R.drawable.loyalty_icon);
            } else if (((MenuModel) this.menu_list_.get(position)).getMenu_title().equalsIgnoreCase("Menus")) {
                menu_icon_img_vw.setImageResource(R.drawable.menu_icon);
            } else if (((MenuModel) this.menu_list_.get(position)).getMenu_title().equalsIgnoreCase("Reserve For Pick Up")) {
                menu_icon_img_vw.setImageResource(R.drawable.bag_icon);
            } else if (((MenuModel) this.menu_list_.get(position)).getMenu_title().equalsIgnoreCase("Shop Online")) {
                menu_icon_img_vw.setImageResource(R.drawable.shop_icon);
            } else if (((MenuModel) this.menu_list_.get(position)).getMenu_title().equalsIgnoreCase("Recipes")) {
                menu_icon_img_vw.setImageResource(R.drawable.recipe_icon);
            } else if (((MenuModel) this.menu_list_.get(position)).getMenu_title().equalsIgnoreCase("Contact us")) {
               // menu_icon_img_vw.setImageResource(R.drawable.customer_service_icon);
            } else if (((MenuModel) this.menu_list_.get(position)).getMenu_title().equalsIgnoreCase("Logout")) {
                menu_icon_img_vw.setImageResource(R.drawable.home_icon);
            }
            menu_ll.setOnTouchListener(new C17373());
            final LinearLayout finalMenu_ll = menu_ll;
            final int finalPosition = position;
            menu_ll.setOnClickListener(new OnClickListener() {
                @TargetApi(21)
                public void onClick(View v) {
                    v.setPressed(true);
                    finalMenu_ll.performLongClick();
                    if (((MenuModel) HomePageFragment.this.menu_list_.get(finalPosition)).getMenu_title().equalsIgnoreCase("Recipes")) {
                        adapterListener.performAdapterAction(AppConstants.RECIPE_MAIN_PAGE, HomePageFragment.this.menu_list_.get(finalPosition));
                    } else if (((MenuModel) HomePageFragment.this.menu_list_.get(finalPosition)).getMenu_title().equalsIgnoreCase("My Store")) {
                        adapterListener.performAdapterAction(AppConstants.STORE_MAIN_PAGE, HomePageFragment.this.menu_list_.get(finalPosition));
                    } else if (((MenuModel) HomePageFragment.this.menu_list_.get(finalPosition)).getMenu_title().equalsIgnoreCase("Menus")) {
                        adapterListener.performAdapterAction(AppConstants.MENUPAGE, HomePageFragment.this.menu_list_.get(finalPosition));
                    } else if (((MenuModel) HomePageFragment.this.menu_list_.get(finalPosition)).getMenu_title().equalsIgnoreCase("My Loyalty")) {
                        adapterListener.performAdapterAction(AppConstants.LOYALTY_PAGE, HomePageFragment.this.menu_list_.get(finalPosition));
                    } else if (((MenuModel) HomePageFragment.this.menu_list_.get(finalPosition)).getMenu_title().equalsIgnoreCase(AppConstants.MYOFFER_PAGE)) {
                        adapterListener.performAdapterAction(AppConstants.MYOFFER_PAGE, HomePageFragment.this.menu_list_.get(finalPosition));
                    } else if (((MenuModel) HomePageFragment.this.menu_list_.get(finalPosition)).getMenu_title().equalsIgnoreCase("Reserve for Pick up") || ((MenuModel) HomePageFragment.this.menu_list_.get(finalPosition)).getMenu_title().equalsIgnoreCase("Shop online")) {
                        adapterListener.performAdapterAction(AppConstants.SHOPONLINE, HomePageFragment.this.menu_list_.get(finalPosition));
                    } else if (((MenuModel) HomePageFragment.this.menu_list_.get(finalPosition)).getMenu_title().equalsIgnoreCase(AppConstants.CONTACT_US_PAGE)) {
                        adapterListener.performAdapterAction(AppConstants.CONTACT_US_PAGE, HomePageFragment.this.menu_list_.get(finalPosition));
                    } else if (((MenuModel) HomePageFragment.this.menu_list_.get(finalPosition)).getMenu_title().equalsIgnoreCase("Logout")) {
                        adapterListener.performAdapterAction(AppConstants.LOGOUT_TEXT, Boolean.valueOf(true));
                    }
                }
            });
            this.homelist_linearLayout.addView(convertView);
        }
    }

    public void onClick(View v) {
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.swipeTimer != null) {
            this.swipeTimer.cancel();
        }
        this.swipeTimer = null;
    }

    public void performAdapterAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }

    private void setDefault(ImageView img) {
        if (img.getDrawable() != null) {
            img.getDrawable().clearColorFilter();
            img.invalidate();
        }
    }

    private void setColor(int color, ImageView img) {
        if (img.getDrawable() != null) {
            img.getDrawable().setColorFilter(color, Mode.SRC_ATOP);
            img.invalidate();
        }
    }

    private void setListViewParams(ListView home_menu_list, int size) {
        LayoutParams params = home_menu_list.getLayoutParams();
        if (this.menu_list_.size() == 0) {
            params.height = 0;
        } else {
            params.height = ((((home_menu_list.getDividerHeight() + home_menu_list.getPaddingTop()) + home_menu_list.getPaddingBottom()) + this.hbha_pref_helper.getIntValue("home_text_height")) + size) * this.menu_list_.size();
        }
        home_menu_list.setLayoutParams(params);
        home_menu_list.invalidateViews();
        home_menu_list.requestLayout();
    }

    private void setQrImage() {
        if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_LOYALTY_NO))) {
            this.home_qr_img_vw.setVisibility(View.VISIBLE);
            return;
        }
        try {
            LayoutParams layoutParams = this.home_qr_img_vw.getLayoutParams();
            layoutParams.width = (int) (((double) this.width) * 0.35d);
            layoutParams.height = (int) (((double) this.width) * 0.35d);
            this.home_qr_img_vw.setLayoutParams(layoutParams);
            Utility.getImageLoader(getActivity()).get(String.format(ApiConstants.QRCODE_URL, new Object[]{this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_LOYALTY_NO), Integer.valueOf(400)}), new C17395());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
