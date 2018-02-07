package com.hbh.honeybaked.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fishbowl.basicmodule.Services.FBUserOfferService;
import com.fishbowl.basicmodule.Services.FBUserOfferService.FBPointBankOffer;
import com.fishbowl.basicmodule.Services.FBUserOfferService.FBRewardPointCallback;
import com.fishbowl.basicmodule.Services.FBUserOfferService.FBUseOfferCallback;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.adapter.RedeemsAdapter;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.constants.PreferenceConstants;
import com.hbh.honeybaked.dialogs.LoyaltyRewardsDialog;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.module.OfferModule;
import com.hbh.honeybaked.supportingfiles.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MyLoyaltyFragment extends BaseFragment {
    TextView Last_visit_dt;
    LinearLayout Last_vist_lay;
    Bitmap bitmap;
    TextView circularTextView;
    TextView content_tit_tv;
    LinearLayout earnedPointsLayout;
    int earned_points;
    private RedeemsAdapter mAdapter;
    private LayoutManager mLayoutManager;
    private RecyclerView mRewardsRecyclerView;
    TextView more_points_tv;
    ArrayList<OfferModule> offerModuleArrayList = new ArrayList();
    String point_next_rewards;
    ImageView qr_code_img_vw;
    RelativeLayout redeem_rl;
    String upperString = "";

    class C17551 implements FBRewardPointCallback {
        C17551() {
        }

        public void OnFBRewardPointCallback(JSONObject loyalty_response, Exception error) {
            if (loyalty_response != null) {
                try {
                    if (loyalty_response.optBoolean("successFlag")) {
                        MyLoyaltyFragment.this.point_next_rewards = String.valueOf(loyalty_response.optInt("pointsToNextReward"));
                        MyLoyaltyFragment.this.earned_points = loyalty_response.optInt("earnedPoints");
                        MyLoyaltyFragment.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_EARN_POINTS, "" + MyLoyaltyFragment.this.earned_points);
                    }
                    MyLoyaltyFragment.this.circularTextView.setText("" + MyLoyaltyFragment.this.earned_points);
                    MyLoyaltyFragment.this.more_points_tv.setText("You need " + MyLoyaltyFragment.this.point_next_rewards + " more points for the next offer.");
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    MyLoyaltyFragment.this.setReddemList();
                    MyLoyaltyFragment.this.getUserFBPointBankOffer();
                }
                return;
            }
            MyLoyaltyFragment.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
            Utility.tryHandleTokenExpiry(MyLoyaltyFragment.this.getActivity(), error);
        }
    }

    class C17562 implements FBPointBankOffer {
        C17562() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void OnFBPointBankOfferCallback(JSONObject r10, Exception r11) {
            /*
            r9 = this;
            r8 = 0;
            if (r10 == 0) goto L_0x00aa;
        L_0x0003:
            r5 = "inAppOfferList";
            r3 = r10.optJSONArray(r5);	 Catch:{ JSONException -> 0x0067 }
            r5 = r3.length();	 Catch:{ JSONException -> 0x0067 }
            if (r5 <= 0) goto L_0x0087;
        L_0x000f:
            r1 = 0;
        L_0x0010:
            r5 = r3.length();	 Catch:{ JSONException -> 0x0067 }
            if (r1 >= r5) goto L_0x0087;
        L_0x0016:
            r2 = r3.getJSONObject(r1);	 Catch:{ JSONException -> 0x0067 }
            r4 = new com.hbh.honeybaked.module.OfferModule;	 Catch:{ JSONException -> 0x0067 }
            r4.<init>();	 Catch:{ JSONException -> 0x0067 }
            r5 = "campaignId";
            r5 = r2.optInt(r5);	 Catch:{ JSONException -> 0x0067 }
            r4.setCampaignId(r5);	 Catch:{ JSONException -> 0x0067 }
            r5 = "promotionID";
            r5 = r2.optInt(r5);	 Catch:{ JSONException -> 0x0067 }
            r4.setPromotionID(r5);	 Catch:{ JSONException -> 0x0067 }
            r5 = "campaignTitle";
            r5 = r2.optString(r5);	 Catch:{ JSONException -> 0x0067 }
            r5 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r5);	 Catch:{ JSONException -> 0x0067 }
            if (r5 != 0) goto L_0x0061;
        L_0x003d:
            r5 = "campaignTitle";
            r5 = r2.optString(r5);	 Catch:{ JSONException -> 0x0067 }
            r6 = "Public name";
            r7 = "";
            r5 = r5.replaceAll(r6, r7);	 Catch:{ JSONException -> 0x0067 }
            r4.setCampaignTitle(r5);	 Catch:{ JSONException -> 0x0067 }
        L_0x004e:
            r5 = "loyaltyPoints";
            r5 = r2.optInt(r5);	 Catch:{ JSONException -> 0x0067 }
            r4.setPoint(r5);	 Catch:{ JSONException -> 0x0067 }
            r5 = com.hbh.honeybaked.fragment.MyLoyaltyFragment.this;	 Catch:{ JSONException -> 0x0067 }
            r5 = r5.offerModuleArrayList;	 Catch:{ JSONException -> 0x0067 }
            r5.add(r4);	 Catch:{ JSONException -> 0x0067 }
            r1 = r1 + 1;
            goto L_0x0010;
        L_0x0061:
            r5 = "";
            r4.setCampaignTitle(r5);	 Catch:{ JSONException -> 0x0067 }
            goto L_0x004e;
        L_0x0067:
            r0 = move-exception;
            r0.printStackTrace();	 Catch:{ all -> 0x0098 }
            r5 = com.hbh.honeybaked.fragment.MyLoyaltyFragment.this;
            r5.setCompaignTitle();
            r5 = com.hbh.honeybaked.fragment.MyLoyaltyFragment.this;
            r6 = "hide_progress_dialog";
            r7 = java.lang.Boolean.valueOf(r8);
            r5.setProgressDialog(r6, r7);
        L_0x007b:
            r5 = com.hbh.honeybaked.fragment.MyLoyaltyFragment.this;
            r6 = "hide_progress_dialog";
            r7 = java.lang.Boolean.valueOf(r8);
            r5.setProgressDialog(r6, r7);
            return;
        L_0x0087:
            r5 = com.hbh.honeybaked.fragment.MyLoyaltyFragment.this;
            r5.setCompaignTitle();
            r5 = com.hbh.honeybaked.fragment.MyLoyaltyFragment.this;
            r6 = "hide_progress_dialog";
            r7 = java.lang.Boolean.valueOf(r8);
            r5.setProgressDialog(r6, r7);
            goto L_0x007b;
        L_0x0098:
            r5 = move-exception;
            r6 = com.hbh.honeybaked.fragment.MyLoyaltyFragment.this;
            r6.setCompaignTitle();
            r6 = com.hbh.honeybaked.fragment.MyLoyaltyFragment.this;
            r7 = "hide_progress_dialog";
            r8 = java.lang.Boolean.valueOf(r8);
            r6.setProgressDialog(r7, r8);
            throw r5;
        L_0x00aa:
            r5 = com.hbh.honeybaked.fragment.MyLoyaltyFragment.this;
            r5 = r5.getActivity();
            com.hbh.honeybaked.supportingfiles.Utility.tryHandleTokenExpiry(r5, r11);
            goto L_0x007b;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.hbh.honeybaked.fragment.MyLoyaltyFragment.2.OnFBPointBankOfferCallback(org.json.JSONObject, java.lang.Exception):void");
        }
    }

    class C17573 implements FBUseOfferCallback {
        C17573() {
        }

        public void onUseOfferCallback(JSONObject response, Exception error) {
            if (response == null) {
                Utility.tryHandleTokenExpiry(MyLoyaltyFragment.this.getActivity(), error);
            } else if (response.has("successFlag")) {
                MyLoyaltyFragment.this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SHOPONLINE, new MenuModel("REDEEM OFFER", "", response.optString("message")));
            }
        }
    }

    class Task implements Runnable {
        Task() {
        }

        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_loyalty, container, false);
        initViews(v);
        setViewListeners();
        this.upperString = this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_FIRST_NAME);
        if (this.cd.isConnectingToInternet()) {
            getLoyalityPoints();
        } else {
            Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
        }
        return v;
    }

    private void initViews(View v) {
        this.more_points_tv = (TextView) v.findViewById(R.id.more_points_tv);
        this.circularTextView = (TextView) v.findViewById(R.id.circularTextView);
        this.qr_code_img_vw = (ImageView) v.findViewById(R.id.qr_code_img_vw);
        this.Last_visit_dt = (TextView) v.findViewById(R.id.last_visit_dt);
        this.earnedPointsLayout = (LinearLayout) v.findViewById(R.id.earnedPointsLayout);
        this.Last_vist_lay = (LinearLayout) v.findViewById(R.id.last_vist_lay);
        this.mRewardsRecyclerView = (RecyclerView) v.findViewById(R.id.rewards_recycler_view);
        this.mRewardsRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(getActivity(), 0, false);
        this.mRewardsRecyclerView.setLayoutManager(this.mLayoutManager);
        this.content_tit_tv = (TextView) v.findViewById(R.id.content_tit_tv);
        this.redeem_rl = (RelativeLayout) v.findViewById(R.id.redeem_rl);
        if (this.hbha_pref_helper.getBooleanValue(PreferenceConstants.PREFERENCE_SIGNUP_FIRSTTIME)) {
            this.Last_vist_lay.setVisibility(View.VISIBLE);
        } else if (this.hbha_pref_helper.getStringValue("last_reg_ph_no").equalsIgnoreCase(this.hbha_pref_helper.getStringValue("current_reg_ph_no"))) {
            this.Last_vist_lay.setVisibility(View.VISIBLE);
        } else if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_LASTLOGINTIME))) {
            this.Last_vist_lay.setVisibility(View.VISIBLE);
        } else {
            this.Last_visit_dt.setText(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_LASTLOGINTIME));
            this.Last_vist_lay.setVisibility(View.VISIBLE);
        }
    }

    private void setViewListeners() {
        this.qr_code_img_vw.setOnClickListener(this);
        this.redeem_rl.setOnClickListener(this);
    }

    public void getLoyalityPoints() {
        setProgressDialog(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        FBUserOfferService.sharedInstance().getUserFBRewardPoint(new JSONObject(), this.hbha_pref_helper.getStringValue("customerID"), new C17551());
    }

    public void getUserFBPointBankOffer() {
        FBUserOfferService.sharedInstance().getUserFBPointBankOffer(new JSONObject(), this.hbha_pref_helper.getStringValue("customerID"), new C17562());
    }

    private void setCompaignTitle() {
        OfferModule offerModule = getMaxPointOfferModule();
        if (Utility.isEmpty(offerModule) || Utility.isEmptyString(offerModule.getCampaignTitle())) {
            this.earnedPointsLayout.setVisibility(View.VISIBLE);
            return;
        }
        this.content_tit_tv.setText(Html.fromHtml("YOU'VE EARNED <b>" + offerModule.getCampaignTitle() + "</b>"));
        this.earnedPointsLayout.setVisibility(View.VISIBLE);
    }

    private OfferModule getMaxPointOfferModule() {
        OfferModule offerModule = null;
        if (this.offerModuleArrayList != null && this.offerModuleArrayList.size() > 0) {
            int maxPoint = 0;
            Iterator it = this.offerModuleArrayList.iterator();
            while (it.hasNext()) {
                OfferModule mOfferModule = (OfferModule) it.next();
                if (maxPoint <= mOfferModule.getPoint()) {
                    maxPoint = mOfferModule.getPoint();
                    offerModule = mOfferModule;
                }
            }
        }
        return offerModule;
    }

    public void useOffer(OfferModule offerModule) {
        JSONObject object = new JSONObject();
        String custom_id = this.hbha_pref_helper.getStringValue("customerID");
        String offer_id = String.valueOf(offerModule.getPromotionID());
        String cam_id = String.valueOf(offerModule.getPoint());
        String ten_id = "581";
        try {
            object.put("memberId", custom_id);
            object.put("offerId", offer_id);
            object.put("tenantId", ten_id);
            object.put("claimPoints", cam_id);
        } catch (Exception e) {
        }
        FBUserOfferService.sharedInstance().useOffer(object, new C17573());
    }

    public void showAlert() {
        LoyaltyRewardsDialog.newInstance(this.offerModuleArrayList, this, this.bitmap).show(getActivity().getSupportFragmentManager(), "LoyaltyRewardsDialog");
    }

    public int dpToPx(int dp) {
        return Math.round(((float) dp) * (getContext().getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qr_code_img_vw:
                if (this.cd.isConnectingToInternet()) {
                    performDialogAction(AppConstants.SHOW_DIALOG, Boolean.valueOf(true));
                    return;
                }
                return;
            case R.id.redeem_rl:
                if (this.cd.isConnectingToInternet()) {
                    showAlert();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void performDialogAction(String tagName, Object data) {
        if (tagName.equals(AppConstants.SHOW_DIALOG)) {
            showAlert();
        } else if (!tagName.equals(AppConstants.SHOPONLINE)) {
            super.performDialogAction(tagName, data);
        } else if (data != null) {
            useOffer((OfferModule) data);
        }
    }

    public void performAdapterAction(String tagName, Object data) {
        if (tagName.equals(AppConstants.SHOW_ALERT)) {
            if (this.cd.isConnectingToInternet()) {
                showAlert();
            }
        } else if (tagName.equals(AppConstants.MYOFFER_PAGE)) {
            this.fragmentActivityListener.performFragmentActivityAction(AppConstants.MYOFFER_PAGE, data);
        } else if (!tagName.equals(AppConstants.SHOPONLINE)) {
            super.performAdapterAction(tagName, data);
        } else if (data != null) {
            useOffer((OfferModule) data);
        }
    }

    private void setReddemList() {
        this.mAdapter = new RedeemsAdapter(getActivity(), new int[]{5, 10, 15, 20, 50, 100}, Integer.valueOf(this.earned_points));
        this.mRewardsRecyclerView.setAdapter(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }
}
