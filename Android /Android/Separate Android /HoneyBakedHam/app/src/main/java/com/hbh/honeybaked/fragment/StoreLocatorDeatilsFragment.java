package com.hbh.honeybaked.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Services.FBStoreService.FBStoreDetailCallback;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBUserService.FBFavouriteStoreUpdateCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.adapter.StoreHoursAdapter;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.constants.PreferenceConstants;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.supportingfiles.Utility;
import com.hbh.honeybaked.tracker.GPSTracker;
import java.util.HashMap;
import org.json.JSONObject;

public class StoreLocatorDeatilsFragment extends BaseFragment implements OnClickListener {
    String address;
    RelativeLayout call;
    String city;
    String ex_header;
    String ex_hours_des;
    int geoFenceCorrFactor;
    RelativeLayout get_drections;
    GoogleMap googleMap = null;
    GPSTracker gps;
    double lati = 0.0d;
    double longi = 0.0d;
    ImageView make_my_store_image;
    TextView make_my_store_tv;
    RelativeLayout makemystore;
    RelativeLayout mapLayout;
    ImageView order_now_img_vw;
    String phone;
    String sFullAddress = "";
    TextView shop_address_tv;
    TextView shop_phno_tv;
    String state;
    Bundle storeDetails;
    StoreHoursAdapter storeExHoursAdapter = null;
    StoreHoursAdapter storeHoursAdapter = null;
    int storeID;
    String storeName;
    String storeNumber;
    ListView store_det_ex_hrs_lv;
    LinearLayout store_det_function_bt_lay;
    LinearLayout store_det_hours_lay;
    ListView store_det_hrs_lv;
    RelativeLayout store_det_img_lay;
    ImageView store_det_img_vw;
    LinearLayout store_ex_hours_det_lay;
    TextView store_ex_hrs_det_tv;
    TextView store_ex_hrs_head_det_tv;
    String store_ex_time;
    String store_id;
    String store_image;
    LinearLayout store_loc_Lay;
    String store_time;
    int tenantId;
    String[] time_arr;
    String zip;

    class C17881 implements OnGlobalLayoutListener {
        C17881() {
        }

        public void onGlobalLayout() {
            LayoutParams params = StoreLocatorDeatilsFragment.this.store_det_function_bt_lay.getLayoutParams();
            params.height = StoreLocatorDeatilsFragment.this.store_det_function_bt_lay.getHeight();
            StoreLocatorDeatilsFragment.this.store_det_img_lay.setLayoutParams(params);
            LayoutParams params1 = StoreLocatorDeatilsFragment.this.store_det_img_vw.getLayoutParams();
            params1.width = StoreLocatorDeatilsFragment.this.store_det_function_bt_lay.getWidth();
            params1.height = StoreLocatorDeatilsFragment.this.store_det_function_bt_lay.getHeight();
            StoreLocatorDeatilsFragment.this.store_det_img_vw.setLayoutParams(params1);
        }
    }

    class C17892 implements FBStoreDetailCallback {
        C17892() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void OnFBStoreDetailCallback(JSONObject r27, Exception r28) {
            /*
            r26 = this;
            if (r27 == 0) goto L_0x075f;
        L_0x0002:
            r18 = "successFlag";
            r0 = r27;
            r1 = r18;
            r18 = r0.optBoolean(r1);	 Catch:{ Exception -> 0x0218 }
            r19 = 1;
            r0 = r18;
            r1 = r19;
            if (r0 != r1) goto L_0x03a2;
        L_0x0014:
            r7 = new java.text.SimpleDateFormat;	 Catch:{ Exception -> 0x0218 }
            r18 = "HH:mm:ss";
            r0 = r18;
            r7.<init>(r0);	 Catch:{ Exception -> 0x0218 }
            r14 = new java.text.SimpleDateFormat;	 Catch:{ Exception -> 0x0218 }
            r18 = "h:mm aa";
            r0 = r18;
            r14.<init>(r0);	 Catch:{ Exception -> 0x0218 }
            r18 = "mobileStores";
            r0 = r27;
            r1 = r18;
            r10 = r0.optJSONObject(r1);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "tenantId";
            r0 = r19;
            r19 = r10.optInt(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.tenantId = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "storeName";
            r0 = r19;
            r19 = r10.optString(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.storeName = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "latitude";
            r0 = r19;
            r20 = r10.optDouble(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r20;
            r2 = r18;
            r2.lati = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "longitude";
            r0 = r19;
            r20 = r10.optDouble(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r20;
            r2 = r18;
            r2.longi = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "storeImage";
            r0 = r19;
            r19 = r10.optString(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.store_image = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "storeID";
            r0 = r19;
            r19 = r10.optInt(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.storeID = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "storeNumber";
            r0 = r19;
            r19 = r10.optString(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.storeNumber = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "address";
            r0 = r19;
            r19 = r10.optString(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.address = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "city";
            r0 = r19;
            r19 = r10.optString(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.city = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "state";
            r0 = r19;
            r19 = r10.optString(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.state = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "zip";
            r0 = r19;
            r19 = r10.optString(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.zip = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "phone";
            r0 = r19;
            r19 = r10.optString(r0);	 Catch:{ Exception -> 0x0218 }
            r19 = com.hbh.honeybaked.supportingfiles.Utility.convertToUsFormat(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.phone = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "geoFenceCorrFactor";
            r0 = r19;
            r19 = r10.optInt(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.geoFenceCorrFactor = r0;	 Catch:{ Exception -> 0x0218 }
            r18 = "storeHoursHoliday";
            r0 = r18;
            r18 = r10.has(r0);	 Catch:{ Exception -> 0x0218 }
            if (r18 == 0) goto L_0x02f5;
        L_0x0142:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_ex_hours_det_lay;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 0;
            r18.setVisibility(r19);	 Catch:{ Exception -> 0x0218 }
            r18 = "storeHoursHoliday";
            r0 = r18;
            r16 = r10.optJSONObject(r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "header";
            r0 = r16;
            r1 = r19;
            r19 = r0.getString(r1);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.ex_header = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "holidayHoursDescription";
            r0 = r16;
            r1 = r19;
            r19 = r0.getString(r1);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.ex_hours_des = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.ex_header;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r18);	 Catch:{ Exception -> 0x0218 }
            if (r18 == 0) goto L_0x01eb;
        L_0x0199:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_ex_hrs_head_det_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 8;
            r18.setVisibility(r19);	 Catch:{ Exception -> 0x0218 }
        L_0x01aa:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.ex_hours_des;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "<br>";
            r9 = r18.split(r19);	 Catch:{ Exception -> 0x0218 }
            r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0218 }
            r12.<init>();	 Catch:{ Exception -> 0x0218 }
            r17 = 0;
        L_0x01c3:
            r0 = r9.length;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r17;
            r1 = r18;
            if (r0 >= r1) goto L_0x024d;
        L_0x01cc:
            r0 = r9.length;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = r18 + -1;
            r0 = r17;
            r1 = r18;
            if (r0 >= r1) goto L_0x022e;
        L_0x01d7:
            r18 = r9[r17];	 Catch:{ Exception -> 0x0218 }
            r18 = r18.trim();	 Catch:{ Exception -> 0x0218 }
            r0 = r18;
            r18 = r12.append(r0);	 Catch:{ Exception -> 0x0218 }
            r19 = "\n";
            r18.append(r19);	 Catch:{ Exception -> 0x0218 }
        L_0x01e8:
            r17 = r17 + 1;
            goto L_0x01c3;
        L_0x01eb:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_ex_hrs_head_det_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 0;
            r18.setVisibility(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_ex_hrs_head_det_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.ex_header;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r18.setText(r19);	 Catch:{ Exception -> 0x0218 }
            goto L_0x01aa;
        L_0x0218:
            r8 = move-exception;
            r8.printStackTrace();	 Catch:{ all -> 0x023a }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;
            r18 = r0;
            r19 = "hide_progress_dialog";
            r20 = 0;
            r20 = java.lang.Boolean.valueOf(r20);
            r18.setProgressDialog(r19, r20);
        L_0x022d:
            return;
        L_0x022e:
            r18 = r9[r17];	 Catch:{ Exception -> 0x0218 }
            r18 = r18.trim();	 Catch:{ Exception -> 0x0218 }
            r0 = r18;
            r12.append(r0);	 Catch:{ Exception -> 0x0218 }
            goto L_0x01e8;
        L_0x023a:
            r18 = move-exception;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;
            r19 = r0;
            r20 = "hide_progress_dialog";
            r21 = 0;
            r21 = java.lang.Boolean.valueOf(r21);
            r19.setProgressDialog(r20, r21);
            throw r18;
        L_0x024d:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = r12.toString();	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.store_ex_time = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_ex_time;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r18);	 Catch:{ Exception -> 0x0218 }
            if (r18 == 0) goto L_0x02c8;
        L_0x026f:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_ex_hrs_det_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 8;
            r18.setVisibility(r19);	 Catch:{ Exception -> 0x0218 }
        L_0x0280:
            r18 = "storeHoursByGroup";
            r0 = r18;
            r15 = r10.optJSONArray(r0);	 Catch:{ Exception -> 0x0218 }
            if (r15 == 0) goto L_0x067e;
        L_0x028a:
            r18 = r15.length();	 Catch:{ Exception -> 0x0218 }
            if (r18 <= 0) goto L_0x067e;
        L_0x0290:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = r15.length();	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r0 = new java.lang.String[r0];	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r1 = r18;
            r1.time_arr = r0;	 Catch:{ Exception -> 0x0218 }
            r13 = 0;
        L_0x02a7:
            r18 = r15.length();	 Catch:{ Exception -> 0x0218 }
            r0 = r18;
            if (r13 >= r0) goto L_0x0308;
        L_0x02af:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.time_arr;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = r15.optString(r13);	 Catch:{ Exception -> 0x0218 }
            r19 = r19.toString();	 Catch:{ Exception -> 0x0218 }
            r18[r13] = r19;	 Catch:{ Exception -> 0x0218 }
            r13 = r13 + 1;
            goto L_0x02a7;
        L_0x02c8:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_ex_hrs_det_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 0;
            r18.setVisibility(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_ex_hrs_det_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.store_ex_time;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r18.setText(r19);	 Catch:{ Exception -> 0x0218 }
            goto L_0x0280;
        L_0x02f5:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_ex_hours_det_lay;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 8;
            r18.setVisibility(r19);	 Catch:{ Exception -> 0x0218 }
            goto L_0x0280;
        L_0x0308:
            r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0218 }
            r12.<init>();	 Catch:{ Exception -> 0x0218 }
            r17 = 0;
        L_0x030f:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.time_arr;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.length;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r17;
            r1 = r18;
            if (r0 >= r1) goto L_0x036f;
        L_0x0326:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.time_arr;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.length;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = r18 + -1;
            r0 = r17;
            r1 = r18;
            if (r0 >= r1) goto L_0x035b;
        L_0x033f:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.time_arr;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = r18[r17];	 Catch:{ Exception -> 0x0218 }
            r0 = r18;
            r18 = r12.append(r0);	 Catch:{ Exception -> 0x0218 }
            r19 = "$$$$$";
            r18.append(r19);	 Catch:{ Exception -> 0x0218 }
        L_0x0358:
            r17 = r17 + 1;
            goto L_0x030f;
        L_0x035b:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.time_arr;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = r18[r17];	 Catch:{ Exception -> 0x0218 }
            r0 = r18;
            r12.append(r0);	 Catch:{ Exception -> 0x0218 }
            goto L_0x0358;
        L_0x036f:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = r12.toString();	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.store_time = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.time_arr;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = com.hbh.honeybaked.supportingfiles.Utility.isEmpty(r18);	 Catch:{ Exception -> 0x0218 }
            if (r18 == 0) goto L_0x0623;
        L_0x0391:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_det_hours_lay;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 8;
            r18.setVisibility(r19);	 Catch:{ Exception -> 0x0218 }
        L_0x03a2:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.city;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r20 = "";
            r19 = com.hbh.honeybaked.supportingfiles.Utility.getStoreValues(r19, r20);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.sFullAddress = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.address;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r20 = r0;
            r0 = r20;
            r0 = r0.sFullAddress;	 Catch:{ Exception -> 0x0218 }
            r20 = r0;
            r19 = com.hbh.honeybaked.supportingfiles.Utility.getStoreValues(r19, r20);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.sFullAddress = r0;	 Catch:{ Exception -> 0x0218 }
            r11 = "";
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.state;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r18);	 Catch:{ Exception -> 0x0218 }
            if (r18 != 0) goto L_0x069f;
        L_0x03fc:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.zip;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r18);	 Catch:{ Exception -> 0x0218 }
            if (r18 != 0) goto L_0x069f;
        L_0x040e:
            r18 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0218 }
            r18.<init>();	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.state;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r18 = r18.append(r19);	 Catch:{ Exception -> 0x0218 }
            r19 = " ";
            r18 = r18.append(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.zip;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r18 = r18.append(r19);	 Catch:{ Exception -> 0x0218 }
            r11 = r18.toString();	 Catch:{ Exception -> 0x0218 }
        L_0x043d:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.sFullAddress;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r19 = com.hbh.honeybaked.supportingfiles.Utility.getStoreValues(r11, r0);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.sFullAddress = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.shop_address_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.sFullAddress;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r18.setText(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = r18.getActivity();	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.store_image;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r20 = r0;
            r0 = r20;
            r0 = r0.store_det_img_vw;	 Catch:{ Exception -> 0x0218 }
            r20 = r0;
            r21 = 2130837771; // 0x7f02010b float:1.7280505E38 double:1.0527737395E-314;
            com.hbh.honeybaked.supportingfiles.Utility.loadImagesToView(r18, r19, r20, r21);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = r18.hbha_pref_helper;	 Catch:{ Exception -> 0x0218 }
            r19 = "store_id";
            r18 = r18.getStringValue(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.store_id;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r18 = r18.equals(r19);	 Catch:{ Exception -> 0x0218 }
            if (r18 == 0) goto L_0x06db;
        L_0x04c0:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.make_my_store_image;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 0;
            r18.setVisibility(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.make_my_store_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "My Store";
            r18.setText(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.makemystore;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r19 = r19.getResources();	 Catch:{ Exception -> 0x0218 }
            r20 = 2131493001; // 0x7f0c0089 float:1.860947E38 double:1.053097466E-314;
            r19 = r19.getColor(r20);	 Catch:{ Exception -> 0x0218 }
            r18.setBackgroundColor(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.make_my_store_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r19 = r19.getResources();	 Catch:{ Exception -> 0x0218 }
            r20 = 2131493151; // 0x7f0c011f float:1.8609774E38 double:1.0530975403E-314;
            r19 = r19.getColor(r20);	 Catch:{ Exception -> 0x0218 }
            r18.setTextColor(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.makemystore;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 0;
            r18.setEnabled(r19);	 Catch:{ Exception -> 0x0218 }
        L_0x0533:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.phone;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = r18.trim();	 Catch:{ Exception -> 0x0218 }
            r18 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r18);	 Catch:{ Exception -> 0x0218 }
            if (r18 == 0) goto L_0x0742;
        L_0x0549:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.shop_phno_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "No Number";
            r18.setText(r19);	 Catch:{ Exception -> 0x0218 }
        L_0x055a:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.lati;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r20 = 0;
            r18 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1));
            if (r18 == 0) goto L_0x0610;
        L_0x056c:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.longi;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r20 = 0;
            r18 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1));
            if (r18 == 0) goto L_0x0610;
        L_0x057e:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.googleMap;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = new com.google.android.gms.maps.model.MarkerOptions;	 Catch:{ Exception -> 0x0218 }
            r19.<init>();	 Catch:{ Exception -> 0x0218 }
            r20 = new com.google.android.gms.maps.model.LatLng;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r21 = r0;
            r0 = r21;
            r0 = r0.lati;	 Catch:{ Exception -> 0x0218 }
            r22 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r21 = r0;
            r0 = r21;
            r0 = r0.longi;	 Catch:{ Exception -> 0x0218 }
            r24 = r0;
            r0 = r20;
            r1 = r22;
            r3 = r24;
            r0.<init>(r1, r3);	 Catch:{ Exception -> 0x0218 }
            r19 = r19.position(r20);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r20 = r0;
            r0 = r20;
            r0 = r0.storeName;	 Catch:{ Exception -> 0x0218 }
            r20 = r0;
            r19 = r19.title(r20);	 Catch:{ Exception -> 0x0218 }
            r18 = r18.addMarker(r19);	 Catch:{ Exception -> 0x0218 }
            r18.showInfoWindow();	 Catch:{ Exception -> 0x0218 }
            r18 = new com.google.android.gms.maps.model.CameraPosition$Builder;	 Catch:{ Exception -> 0x0218 }
            r18.<init>();	 Catch:{ Exception -> 0x0218 }
            r19 = new com.google.android.gms.maps.model.LatLng;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r20 = r0;
            r0 = r20;
            r0 = r0.lati;	 Catch:{ Exception -> 0x0218 }
            r20 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r22 = r0;
            r0 = r22;
            r0 = r0.longi;	 Catch:{ Exception -> 0x0218 }
            r22 = r0;
            r19.<init>(r20, r22);	 Catch:{ Exception -> 0x0218 }
            r18 = r18.target(r19);	 Catch:{ Exception -> 0x0218 }
            r19 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
            r18 = r18.zoom(r19);	 Catch:{ Exception -> 0x0218 }
            r6 = r18.build();	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.googleMap;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition(r6);	 Catch:{ Exception -> 0x0218 }
            r18.moveCamera(r19);	 Catch:{ Exception -> 0x0218 }
        L_0x0610:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;
            r18 = r0;
            r19 = "hide_progress_dialog";
            r20 = 0;
            r20 = java.lang.Boolean.valueOf(r20);
            r18.setProgressDialog(r19, r20);
            goto L_0x022d;
        L_0x0623:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = new com.hbh.honeybaked.adapter.StoreHoursAdapter;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r20 = r0;
            r20 = r20.getActivity();	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r21 = r0;
            r0 = r21;
            r0 = r0.time_arr;	 Catch:{ Exception -> 0x0218 }
            r21 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r22 = r0;
            r19.<init>(r20, r21, r22);	 Catch:{ Exception -> 0x0218 }
            r0 = r19;
            r1 = r18;
            r1.storeHoursAdapter = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_det_hrs_lv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.storeHoursAdapter;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r18.setAdapter(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_det_hrs_lv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 0;
            com.hbh.honeybaked.supportingfiles.Utility.setListViewHeightBasedOnChildren(r18, r19);	 Catch:{ Exception -> 0x0218 }
            goto L_0x03a2;
        L_0x067e:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "";
            r0 = r19;
            r1 = r18;
            r1.store_time = r0;	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.store_det_hours_lay;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 8;
            r18.setVisibility(r19);	 Catch:{ Exception -> 0x0218 }
            goto L_0x03a2;
        L_0x069f:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.state;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r18);	 Catch:{ Exception -> 0x0218 }
            if (r18 != 0) goto L_0x06bd;
        L_0x06b1:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r11 = r0.state;	 Catch:{ Exception -> 0x0218 }
            goto L_0x043d;
        L_0x06bd:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.zip;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r18 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r18);	 Catch:{ Exception -> 0x0218 }
            if (r18 != 0) goto L_0x043d;
        L_0x06cf:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r11 = r0.zip;	 Catch:{ Exception -> 0x0218 }
            goto L_0x043d;
        L_0x06db:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.make_my_store_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = "Make This My Store";
            r18.setText(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.make_my_store_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r19 = r19.getResources();	 Catch:{ Exception -> 0x0218 }
            r20 = 2131493001; // 0x7f0c0089 float:1.860947E38 double:1.053097466E-314;
            r19 = r19.getColor(r20);	 Catch:{ Exception -> 0x0218 }
            r18.setTextColor(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.make_my_store_image;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 4;
            r18.setVisibility(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.makemystore;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 2130837812; // 0x7f020134 float:1.7280589E38 double:1.05277376E-314;
            r18.setBackgroundResource(r19);	 Catch:{ Exception -> 0x0218 }
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.makemystore;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r19 = 1;
            r18.setEnabled(r19);	 Catch:{ Exception -> 0x0218 }
            goto L_0x0533;
        L_0x0742:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r18;
            r0 = r0.shop_phno_tv;	 Catch:{ Exception -> 0x0218 }
            r18 = r0;
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r0 = r19;
            r0 = r0.phone;	 Catch:{ Exception -> 0x0218 }
            r19 = r0;
            r18.setText(r19);	 Catch:{ Exception -> 0x0218 }
            goto L_0x055a;
        L_0x075f:
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;
            r18 = r0;
            r18 = r18.getActivity();
            com.hbh.honeybaked.supportingfiles.Utility.hideSoftKeyboard(r18);
            r0 = r26;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;
            r18 = r0;
            r18 = r18.getActivity();
            r0 = r18;
            r1 = r28;
            com.hbh.honeybaked.supportingfiles.Utility.tryHandleTokenExpiry(r0, r1);
            goto L_0x022d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.2.OnFBStoreDetailCallback(org.json.JSONObject, java.lang.Exception):void");
        }
    }

    class C17903 implements OnDismissListener {
        C17903() {
        }

        @TargetApi(23)
        public void onDismiss(DialogInterface dialogInterface) {
            StoreLocatorDeatilsFragment.this.requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 2);
        }
    }

    class C17914 implements OnDismissListener {
        C17914() {
        }

        @TargetApi(23)
        public void onDismiss(DialogInterface dialogInterface) {
            StoreLocatorDeatilsFragment.this.requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 1);
        }
    }

    class C17925 implements OnDismissListener {
        C17925() {
        }

        public void onDismiss(DialogInterface dialog) {
        }
    }

    class C17936 implements OnDismissListener {
        C17936() {
        }

        public void onDismiss(DialogInterface dialog) {
        }
    }

    class C17947 implements FBFavouriteStoreUpdateCallback {
        C17947() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onFBFavouriteStoreUpdateCallback(JSONObject r6, Exception r7) {
            /*
            r5 = this;
            r4 = 0;
            if (r6 == 0) goto L_0x0142;
        L_0x0003:
            r0 = "successFlag";
            r0 = r6.has(r0);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            if (r0 == 0) goto L_0x0122;
        L_0x000b:
            r0 = "successFlag";
            r0 = r6.optBoolean(r0);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = 1;
            if (r0 != r1) goto L_0x0102;
        L_0x0014:
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.getActivity();	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = "Store Updated Successfully";
            r2 = 0;
            android.widget.Toast.makeText(r0, r1, r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.hbha_pref_helper;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = "store";
            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2.<init>();	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r3 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r3 = r3.sFullAddress;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r3 = " ###### ";
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r3 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r3 = r3.phone;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r3 = "######";
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r3 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r3 = r3.store_time;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r3 = "######";
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r3 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r3 = r3.city;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.toString();	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0.saveStringValue(r1, r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.hbha_pref_helper;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = "store_id";
            r2 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.store_id;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0.saveStringValue(r1, r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.hbha_pref_helper;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = "store_code";
            r2 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.storeNumber;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0.saveStringValue(r1, r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.hbha_pref_helper;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = "city";
            r2 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.city;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0.saveStringValue(r1, r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.hbha_pref_helper;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = "lat";
            r2 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.lati;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = java.lang.String.valueOf(r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0.saveStringValue(r1, r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.hbha_pref_helper;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = "lang";
            r2 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.longi;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = java.lang.String.valueOf(r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0.saveStringValue(r1, r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.hbha_pref_helper;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = "store_ex_hrs_head";
            r2 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.ex_header;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0.saveStringValue(r1, r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.hbha_pref_helper;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = "store_ex_hrs";
            r2 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.store_ex_time;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0.saveStringValue(r1, r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.hbha_pref_helper;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = "store_image";
            r2 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r2 = r2.store_image;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0.saveStringValue(r1, r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.adapterListener;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r1 = "HomePage";
            r2 = 0;
            r2 = java.lang.Boolean.valueOf(r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0.performAdapterAction(r1, r2);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
        L_0x00f6:
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;
            r1 = "hide_progress_dialog";
            r2 = java.lang.Boolean.valueOf(r4);
            r0.performDialogAction(r1, r2);
        L_0x0101:
            return;
        L_0x0102:
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.getActivity();	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            com.hbh.honeybaked.supportingfiles.Utility.hideSoftKeyboard(r0);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.getActivity();	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            com.hbh.honeybaked.supportingfiles.Utility.tryHandleTokenExpiry(r0, r7);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            goto L_0x00f6;
        L_0x0115:
            r0 = move-exception;
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;
            r1 = "hide_progress_dialog";
            r2 = java.lang.Boolean.valueOf(r4);
            r0.performDialogAction(r1, r2);
            goto L_0x0101;
        L_0x0122:
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.getActivity();	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            com.hbh.honeybaked.supportingfiles.Utility.hideSoftKeyboard(r0);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            r0 = r0.getActivity();	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            com.hbh.honeybaked.supportingfiles.Utility.tryHandleTokenExpiry(r0, r7);	 Catch:{ Exception -> 0x0115, all -> 0x0135 }
            goto L_0x00f6;
        L_0x0135:
            r0 = move-exception;
            r1 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;
            r2 = "hide_progress_dialog";
            r3 = java.lang.Boolean.valueOf(r4);
            r1.performDialogAction(r2, r3);
            throw r0;
        L_0x0142:
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;
            r1 = "hide_progress_dialog";
            r2 = java.lang.Boolean.valueOf(r4);
            r0.performDialogAction(r1, r2);
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;
            r0 = r0.getActivity();
            com.hbh.honeybaked.supportingfiles.Utility.hideSoftKeyboard(r0);
            r0 = com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.this;
            r0 = r0.getActivity();
            com.hbh.honeybaked.supportingfiles.Utility.tryHandleTokenExpiry(r0, r7);
            goto L_0x0101;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment.7.onFBFavouriteStoreUpdateCallback(org.json.JSONObject, java.lang.Exception):void");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_storelocator_details, container, false);
        this.storeDetails = getArguments();
        this.store_loc_Lay = (LinearLayout) v.findViewById(R.id.stor_loc_lay);
        this.store_det_function_bt_lay = (LinearLayout) v.findViewById(R.id.store_det_function_bt_lay);
        this.store_det_hours_lay = (LinearLayout) v.findViewById(R.id.store_det_hours_lay);
        this.store_ex_hours_det_lay = (LinearLayout) v.findViewById(R.id.store_ex_hours_det_lay);
        this.call = (RelativeLayout) v.findViewById(R.id.call_phone);
        this.makemystore = (RelativeLayout) v.findViewById(R.id.my_store);
        this.get_drections = (RelativeLayout) v.findViewById(R.id.get_drections);
        this.store_det_img_lay = (RelativeLayout) v.findViewById(R.id.store_det_img_lay);
        this.shop_address_tv = (TextView) v.findViewById(R.id.shop_address_tv);
        this.shop_phno_tv = (TextView) v.findViewById(R.id.shop_phno_tv);
        this.store_ex_hrs_det_tv = (TextView) v.findViewById(R.id.store_ex_hrs_det_tv);
        this.store_ex_hrs_head_det_tv = (TextView) v.findViewById(R.id.store_ex_hrs_head_det_tv);
        this.make_my_store_tv = (TextView) v.findViewById(R.id.make_my_store_tv);
        this.make_my_store_image = (ImageView) v.findViewById(R.id.make_my_store_image);
        this.store_det_img_vw = (ImageView) v.findViewById(R.id.store_det_img_vw);
        this.mapLayout = (RelativeLayout) v.findViewById(R.id.mapLayout);
        this.order_now_img_vw = (ImageView) v.findViewById(R.id.order_now_img_vw);
        this.store_det_hrs_lv = (ListView) v.findViewById(R.id.store_det_hrs_lv);
        if (this.storeDetails != null) {
            this.store_id = (String) ((HashMap) this.storeDetails.getSerializable("bundle")).get("storeId");
        }
        if (this.cd.isConnectingToInternet()) {
            getStoreDetails(this.store_id);
        } else {
            Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
        }
        this.call.setOnClickListener(this);
        this.makemystore.setOnClickListener(this);
        this.get_drections.setOnClickListener(this);
        this.order_now_img_vw.setOnClickListener(this);
        if (this.googleMap == null) {
            try {
                this.googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.store_map_details)).getMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.store_det_function_bt_lay.getViewTreeObserver().addOnGlobalLayoutListener(new C17881());
        return v;
    }

    public void getStoreDetails(String storeid) {
        performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        FBStoreService.sharedInstance().getStoreDetails(storeid, new C17892());
    }

    public void performAdapterAction(String tagName, Object data) {
        if (!tagName.equals("set_store_hours_listview_height")) {
            super.performAdapterAction(tagName, data);
        } else if (data == null) {
        }
    }

    private void setListViewParams(int size) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.store_det_hrs_lv.getLayoutParams();
        if (Utility.isEmpty(this.time_arr)) {
            params.height = 0;
        } else {
            params.height = this.time_arr.length * size;
        }
        this.store_det_hrs_lv.setLayoutParams(params);
        this.store_det_hrs_lv.invalidate();
    }

    public void onClick(View v) {
        Builder builder;
        switch (v.getId()) {
            case R.id.get_drections:
                if (VERSION.SDK_INT < 23) {
                    goToLocation();
                    return;
                } else if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                    goToLocation();
                    return;
                } else if (shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION") && shouldShowRequestPermissionRationale("android.permission.ACCESS_COARSE_LOCATION")) {
                    builder = new Builder(getActivity());
                    builder.setTitle("This app needs location access");
                    builder.setMessage("Go to Settings and enable the location permission");
                  //  builder.setPositiveButton(17039370, null);
                    builder.setOnDismissListener(new C17903());
                    builder.show();
                    return;
                } else {
                    requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 2);
                    return;
                }
            case R.id.call_phone:
                if (!this.shop_phno_tv.getText().toString().equalsIgnoreCase("No Number")) {
                    if (VERSION.SDK_INT < 23) {
                        goToCall();
                        return;
                    } else if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.CALL_PHONE") == 0) {
                        goToCall();
                        return;
                    } else if (shouldShowRequestPermissionRationale("android.permission.CALL_PHONE")) {
                        builder = new Builder(getActivity());
                        builder.setTitle("This app needs call access");
                        builder.setMessage("Go to Settings and enable the phone permission");
                     //   builder.setPositiveButton(17039370, null);
                        builder.setOnDismissListener(new C17914());
                        builder.show();
                        return;
                    } else {
                        requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 1);
                        return;
                    }
                }
                return;
            case R.id.my_store:
                if (this.cd.isConnectingToInternet()) {
                    favouriteStoreUpdate();
                    return;
                } else {
                    Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
                    return;
                }
            case R.id.order_now_img_vw:
                this.hbha_pref_helper.saveStringValue("store_code1", this.storeNumber);
                this.hbha_pref_helper.saveStringValue("city1", this.city);
                this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SHOPONLINE, new MenuModel("Reserve For Pick Up", "", ""));
                return;
            default:
                return;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.CALL_PHONE") == 0) {
                    goToCall();
                    return;
                }
                return;
            case 2:
                if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                    goToLocation();
                    return;
                }
                return;
            default:
                return;
        }
    }

    @TargetApi(17)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Builder builder;
        switch (requestCode) {
            case 1:
                if (grantResults[0] == 0) {
                    goToCall();
                    return;
                }
                builder = new Builder(getActivity());
                builder.setTitle("Functionality limited");
                builder.setMessage("Since call access has not been granted, this app will not be able to connect call");
              //  builder.setPositiveButton(17039370, null);
                builder.setOnDismissListener(new C17925());
                builder.show();
                return;
            case 2:
                if (grantResults[0] == 0) {
                    goToLocation();
                    return;
                }
                builder = new Builder(getActivity());
                builder.setTitle("Functionality limited");
                builder.setMessage("Since location access has not been granted, this app will not be able to get location");
              //  builder.setPositiveButton(17039370, null);
                builder.setOnDismissListener(new C17936());
                builder.show();
                return;
            default:
                return;
        }
    }

    private void goToLocation() {
        this.gps = new GPSTracker(getActivity());
        if (this.gps.canGetLocation()) {
            double latitude = this.gps.getLatitude();
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://maps.google.com/maps?saddr=" + latitude + "," + this.gps.getLongitude() + "&daddr=" + this.lati + "," + this.longi)));
            return;
        }
        this.gps.showSettingsAlert();
    }

    private void goToCall() {
        if (((TelephonyManager) getActivity().getSystemService("phone")).getSimState() == 1) {
            Utility.showToast(getActivity(), "No Sim Available");
        } else if (this.shop_phno_tv.getText().toString().trim().equalsIgnoreCase("No Number")) {
            Utility.showToast(getActivity(), "No Number to Call");
        } else {
            try {
                Intent in = new Intent("android.intent.action.DIAL");
                in.setData(Uri.parse("tel:" + this.shop_phno_tv.getText().toString().trim()));
                startActivity(in);
            } catch (ActivityNotFoundException e) {
                Utility.showToast(getActivity(), "No Sim Available");
            }
        }
    }

    public void favouriteStoreUpdate() {
        JSONObject object = new JSONObject();
        try {
            object.put("memberid", this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_CUSTOMER_ID));
            object.put("storeCode", this.storeNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        FBUserService.sharedInstance().favourteStoreUpdate(object, new C17947());
    }
}
