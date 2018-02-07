package com.hbh.honeybaked.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.facebook.appevents.AppEventsConstants;
import com.fishbowl.basicmodule.Services.FBStoreService;
import com.fishbowl.basicmodule.Services.FBStoreService.FBAllSearchStorejsonCallback;
import com.google.android.gms.actions.SearchIntents;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.adapter.StoreAdapter;
import com.hbh.honeybaked.adapter.StoreHoursAdapter;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.supportingfiles.Utility;
import com.hbh.honeybaked.tracker.GPSTracker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreLocatorFragment extends BaseFragment implements LocationListener {
    RelativeLayout call;
    String city;
    RelativeLayout get_drections;
    GoogleMap googleMap = null;
    GPSTracker gps;
    boolean isSearchEnabled;
    RelativeLayout mapLayout;
    ScrollView my_store_sv;
    RelativeLayout no_shop_tv;
    TextView no_shop_tv1;
    ImageView order_now_img_vw;
    EditText search_et;
    ImageView search_icon;
    RelativeLayout search_ll;
    LinearLayout searchview_layout;
    Boolean setAutoSearch = Boolean.valueOf(false);
    TextView shop_address_tv;
    TextView shop_phno_tv;
    StoreAdapter storeAdapter;
    StoreHoursAdapter storeHoursAdapter = null;
    LinearLayout store_Lay;
    LinearLayout store_ex_hours_lay;
    TextView store_ex_hrs_head_tv;
    TextView store_ex_hrs_tv;
    LinearLayout store_function_bt_lay;
    LinearLayout store_hours_lay;
    ListView store_hrs_lv;
    RelativeLayout store_img_lay;
    ImageView store_img_vw;
    ArrayList<HashMap<String, String>> store_list = new ArrayList();
    ListView store_list_vw;
    String[] time_arr;

    class C17951 implements OnEditorActionListener {
        C17951() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId != 3) {
                return false;
            }
            StoreLocatorFragment.this.searchStore();
            return true;
        }
    }

    class C17962 implements OnGlobalLayoutListener {
        C17962() {
        }

        public void onGlobalLayout() {
            LayoutParams params = StoreLocatorFragment.this.store_img_lay.getLayoutParams();
            params.height = StoreLocatorFragment.this.store_function_bt_lay.getHeight();
            StoreLocatorFragment.this.store_img_lay.setLayoutParams(params);
            LayoutParams params1 = StoreLocatorFragment.this.store_img_vw.getLayoutParams();
            params1.width = StoreLocatorFragment.this.store_function_bt_lay.getWidth();
            params1.height = StoreLocatorFragment.this.store_function_bt_lay.getHeight();
            StoreLocatorFragment.this.store_img_vw.setLayoutParams(params1);
        }
    }

    class C17973 implements OnDismissListener {
        C17973() {
        }

        @TargetApi(23)
        public void onDismiss(DialogInterface dialogInterface) {
            StoreLocatorFragment.this.requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 2);
        }
    }

    class C17984 implements OnDismissListener {
        C17984() {
        }

        @TargetApi(23)
        public void onDismiss(DialogInterface dialogInterface) {
            StoreLocatorFragment.this.requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 1);
        }
    }

    class C17995 implements FBAllSearchStorejsonCallback {
        C17995() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void OnAllSearchStorejsonCallback(JSONObject r13, Exception r14) {
            /*
            r12 = this;
            if (r13 == 0) goto L_0x023d;
        L_0x0002:
            r8 = "successFlag";
            r8 = r13.optBoolean(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = 1;
            if (r8 != r9) goto L_0x0204;
        L_0x000b:
            r8 = "storeList";
            r8 = r13.has(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            if (r8 == 0) goto L_0x01f6;
        L_0x0013:
            r8 = "storeList";
            r5 = r13.optJSONArray(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r5.length();	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            if (r8 <= 0) goto L_0x01c7;
        L_0x001f:
            r0 = 0;
        L_0x0020:
            r8 = r5.length();	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            if (r0 >= r8) goto L_0x01f6;
        L_0x0026:
            r7 = r5.getJSONObject(r0);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6 = new java.util.HashMap;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.<init>();	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "storeId";
            r9 = "storeId";
            r9 = r7.optInt(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = java.lang.String.valueOf(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "storeName";
            r9 = "storeName";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "description";
            r9 = "description";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "address";
            r9 = "address";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "city";
            r9 = "city";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "zipcode";
            r9 = "zipCode";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "state";
            r9 = "state";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "country";
            r9 = "country";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "mobile";
            r9 = "mobile";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "email";
            r9 = "email";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "phone";
            r9 = "phone";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = com.hbh.honeybaked.supportingfiles.Utility.convertToUsFormat(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "number";
            r9 = "number";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "distance";
            r9 = "distance";
            r9 = r7.optInt(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = java.lang.String.valueOf(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "refernceUrl";
            r9 = "refernceUrl";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "latitude";
            r8 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r10 = 0;
            r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
            if (r8 != 0) goto L_0x0151;
        L_0x00e1:
            r8 = "lat";
            r9 = "0";
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
        L_0x00e8:
            r8 = "longitude";
            r8 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r10 = 0;
            r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
            if (r8 != 0) goto L_0x0183;
        L_0x00f4:
            r8 = "lang";
            r9 = "0";
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
        L_0x00fb:
            r8 = "address";
            r8 = r7.optString(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = "";
            r4 = com.hbh.honeybaked.supportingfiles.Utility.getStoreValues(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "mailingState";
            r2 = r7.optString(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "mailingZip";
            r3 = r7.optString(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r1 = "";
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r2);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            if (r8 != 0) goto L_0x01b5;
        L_0x011b:
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r3);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            if (r8 != 0) goto L_0x01b5;
        L_0x0121:
            r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8.<init>();	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.append(r2);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = " ";
            r8 = r8.append(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.append(r3);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r1 = r8.toString();	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
        L_0x0138:
            r4 = com.hbh.honeybaked.supportingfiles.Utility.getStoreValues(r1, r4);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = "store_address_string";
            r6.put(r8, r4);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.store_list;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8.add(r6);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8.setValues();	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r0 = r0 + 1;
            goto L_0x0020;
        L_0x0151:
            r9 = "lat";
            r8 = "latitude";
            r10 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            if (r8 != 0) goto L_0x0180;
        L_0x0163:
            r8 = "latitude";
            r10 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
        L_0x016d:
            r6.put(r9, r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            goto L_0x00e8;
        L_0x0172:
            r8 = move-exception;
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);
            r8.performDialogAction(r9, r10);
        L_0x017f:
            return;
        L_0x0180:
            r8 = "";
            goto L_0x016d;
        L_0x0183:
            r9 = "lang";
            r8 = "longitude";
            r10 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            if (r8 != 0) goto L_0x01b2;
        L_0x0195:
            r8 = "longitude";
            r10 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
        L_0x019f:
            r6.put(r9, r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            goto L_0x00fb;
        L_0x01a4:
            r8 = move-exception;
            r9 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;
            r10 = "hide_progress_dialog";
            r11 = 0;
            r11 = java.lang.Boolean.valueOf(r11);
            r9.performDialogAction(r10, r11);
            throw r8;
        L_0x01b2:
            r8 = "";
            goto L_0x019f;
        L_0x01b5:
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r2);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            if (r8 != 0) goto L_0x01be;
        L_0x01bb:
            r1 = r2;
            goto L_0x0138;
        L_0x01be:
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r3);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            if (r8 != 0) goto L_0x0138;
        L_0x01c4:
            r1 = r3;
            goto L_0x0138;
        L_0x01c7:
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8.performDialogAction(r9, r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.my_store_sv;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = 8;
            r8.setVisibility(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.store_list_vw;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = 8;
            r8.setVisibility(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.no_shop_tv;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = 0;
            r8.setVisibility(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.no_shop_tv1;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = "No Stores Found.";
            r8.setText(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
        L_0x01f6:
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);
            r8.performDialogAction(r9, r10);
            goto L_0x017f;
        L_0x0204:
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8.performDialogAction(r9, r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.getActivity();	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            com.hbh.honeybaked.supportingfiles.Utility.hideSoftKeyboard(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.my_store_sv;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = 8;
            r8.setVisibility(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.store_list_vw;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = 8;
            r8.setVisibility(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.no_shop_tv;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = 0;
            r8.setVisibility(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r8 = r8.no_shop_tv1;	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            r9 = "No Stores Found.";
            r8.setText(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a4 }
            goto L_0x01f6;
        L_0x023d:
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);
            r8.performDialogAction(r9, r10);
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;
            r8 = r8.getActivity();
            com.hbh.honeybaked.supportingfiles.Utility.hideSoftKeyboard(r8);
            r8 = com.hbh.honeybaked.fragment.StoreLocatorFragment.this;
            r8 = r8.getActivity();
            com.hbh.honeybaked.supportingfiles.Utility.tryHandleTokenExpiry(r8, r14);
            goto L_0x017f;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.hbh.honeybaked.fragment.StoreLocatorFragment.5.OnAllSearchStorejsonCallback(org.json.JSONObject, java.lang.Exception):void");
        }
    }

    class C18006 implements OnDismissListener {
        C18006() {
        }

        public void onDismiss(DialogInterface dialog) {
        }
    }

    class C18017 implements OnDismissListener {
        C18017() {
        }

        public void onDismiss(DialogInterface dialog) {
        }
    }

    public static StoreLocatorFragment newInstances(ArrayList<HashMap<String, String>> store_list, String store_searchvalue, boolean isSearchEnabled) {
        StoreLocatorFragment storeLocatorFragment = new StoreLocatorFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("store_list", store_list);
        bundle.putSerializable("store_searchvalue", store_searchvalue);
        bundle.putBoolean("isSearchEnabled", isSearchEnabled);
        storeLocatorFragment.setArguments(bundle);
        return storeLocatorFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int i;
        View v = inflater.inflate(R.layout.fragment_storelocator, container, false);
        this.store_list_vw = (ListView) v.findViewById(R.id.store_list_vw);
        this.shop_address_tv = (TextView) v.findViewById(R.id.shop_address_tv);
        this.my_store_sv = (ScrollView) v.findViewById(R.id.my_store_sv);
        this.shop_phno_tv = (TextView) v.findViewById(R.id.shop_phno_tv);
        this.no_shop_tv = (RelativeLayout) v.findViewById(R.id.no_shop_tv);
        this.no_shop_tv1 = (TextView) v.findViewById(R.id.no_shop_tv1);
        this.call = (RelativeLayout) v.findViewById(R.id.call_phone);
        this.mapLayout = (RelativeLayout) v.findViewById(R.id.mapLayout);
        this.get_drections = (RelativeLayout) v.findViewById(R.id.get_drections);
        this.search_et = (EditText) v.findViewById(R.id.search_et);
        this.search_ll = (RelativeLayout) v.findViewById(R.id.search_ll);
        this.searchview_layout = (LinearLayout) v.findViewById(R.id.searchview_layout);
        this.store_ex_hours_lay = (LinearLayout) v.findViewById(R.id.store_ex_hours_lay);
        this.store_function_bt_lay = (LinearLayout) v.findViewById(R.id.store_function_bt_lay);
        this.store_img_lay = (RelativeLayout) v.findViewById(R.id.store_img_lay);
        this.store_ex_hrs_head_tv = (TextView) v.findViewById(R.id.store_ex_hrs_head_tv);
        this.store_ex_hrs_tv = (TextView) v.findViewById(R.id.store_ex_hrs_tv);
        this.store_Lay = (LinearLayout) v.findViewById(R.id.store_lay);
        this.store_hours_lay = (LinearLayout) v.findViewById(R.id.store_hours_lay);
        this.order_now_img_vw = (ImageView) v.findViewById(R.id.order_now_img_vw);
        this.search_icon = (ImageView) v.findViewById(R.id.search_icon);
        this.store_img_vw = (ImageView) v.findViewById(R.id.store_img_vw);
        this.store_hrs_lv = (ListView) v.findViewById(R.id.store_hrs_lv);
        Utility.hideSoftKeyboard(getActivity());
        if (getArguments() != null) {
            this.store_list = (ArrayList) getArguments().getSerializable("store_list");
            this.isSearchEnabled = getArguments().getBoolean("isSearchEnabled");
            this.search_et.setText((CharSequence) getArguments().getSerializable("store_searchvalue"));
        }
        if (this.store_list == null) {
            this.store_list = new ArrayList();
        }
        LinearLayout linearLayout = this.searchview_layout;
        if (this.isSearchEnabled) {
            i = 0;
        } else {
            i = 8;
        }
        linearLayout.setVisibility(View.GONE);
        this.call.setOnClickListener(this);
        this.get_drections.setOnClickListener(this);
        this.search_ll.setOnClickListener(this);
        this.order_now_img_vw.setOnClickListener(this);
        this.search_icon.setOnClickListener(this);
        this.search_et.setOnEditorActionListener(new C17951());
        this.store_function_bt_lay.getViewTreeObserver().addOnGlobalLayoutListener(new C17962());
        if (this.googleMap == null) {
            try {
                this.googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.store_map)).getMap();
                CameraPosition googlePlex = CameraPosition.builder().target(new LatLng(37.09024d, -95.712891d)).zoom(3.0f).bearing(0.0f).tilt(45.0f).build();
                if (this.googleMap != null) {
                    this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setDefaultStore();
        if (this.store_list.size() > 0) {
            setValues();
        }
        if (this.hbha_pref_helper.getStringValue("store").trim().length() == 0 && this.store_list.size() == 0) {
            setAutoSearch(true);
        }
        return v;
    }

    private void setAutoSearch(boolean value) {
        this.setAutoSearch = Boolean.valueOf(value);
        if (VERSION.SDK_INT < 23) {
            goToLocation(Boolean.valueOf(value));
        } else if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            goToLocation(Boolean.valueOf(value));
        } else if (shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION") && shouldShowRequestPermissionRationale("android.permission.ACCESS_COARSE_LOCATION")) {
            Builder builder = new Builder(getActivity());
            builder.setTitle("This app needs location access");
            builder.setMessage("Go to Settings and enable the location permission");
            //builder.setPositiveButton(17039370, null);
            builder.setOnDismissListener(new C17973());
            builder.show();
        } else {
            requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 2);
        }
    }

    private void setDefaultStore() {
        if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue("store"))) {
            this.no_shop_tv.setVisibility(View.GONE);
            this.no_shop_tv1.setText("Enter in the field above an address, Zip code, city name or store name");
            this.my_store_sv.setVisibility(View.GONE);
            this.store_list_vw.setVisibility(View.GONE);
            return;
        }
        this.my_store_sv.setVisibility(View.GONE);
        this.no_shop_tv.setVisibility(View.GONE);
        this.store_list_vw.setVisibility(View.GONE);
        Utility.loadImagesToView(getActivity(), this.hbha_pref_helper.getStringValue("store_image"), this.store_img_vw, R.drawable.no_image_available);
        String[] add_ = this.hbha_pref_helper.getStringValue("store").split("######", -1);
        this.shop_address_tv.setText(add_[0]);
        this.shop_phno_tv.setVisibility(View.GONE);
        if (Utility.isEmptyString(add_[1].trim())) {
            this.shop_phno_tv.setText("No Number");
        } else {
            this.shop_phno_tv.setText(add_[1]);
        }
        if (add_[2] == null || add_[2].length() <= 0 || add_[2].equalsIgnoreCase("")) {
            this.store_hours_lay.setVisibility(View.GONE);
        } else {
            this.time_arr = add_[2].trim().split("\\$\\$\\$\\$\\$", -1);
            if (Utility.isEmpty(this.time_arr)) {
                this.store_hours_lay.setVisibility(View.GONE);
            } else {
                this.storeHoursAdapter = new StoreHoursAdapter(getActivity(), this.time_arr, this);
                this.store_hrs_lv.setAdapter(this.storeHoursAdapter);
            }
        }
        this.city = add_[3].toString();
        if (!(Utility.isEmptyString(this.hbha_pref_helper.getStringValue("lat")) || Utility.isEmptyString(this.hbha_pref_helper.getStringValue("lang")) || this.hbha_pref_helper.getStringValue("lat").equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO) || this.hbha_pref_helper.getStringValue("lang").equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO) || this.googleMap == null)) {
            this.googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(this.hbha_pref_helper.getStringValue("lat")).doubleValue(), Double.valueOf(this.hbha_pref_helper.getStringValue("lang")).doubleValue()))).showInfoWindow();
            this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(Double.valueOf(this.hbha_pref_helper.getStringValue("lat")).doubleValue(), Double.valueOf(this.hbha_pref_helper.getStringValue("lang")).doubleValue())).zoom(12.0f).build()));
        }
        if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue("store_ex_hrs_head"))) {
            this.store_ex_hrs_head_tv.setVisibility(View.GONE);
        } else {
            this.store_ex_hrs_head_tv.setVisibility(View.GONE);
            this.store_ex_hrs_head_tv.setText(this.hbha_pref_helper.getStringValue("store_ex_hrs_head"));
        }
        if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue("store_ex_hrs"))) {
            this.store_ex_hrs_tv.setVisibility(View.GONE);
            return;
        }
        this.store_ex_hrs_tv.setVisibility(View.GONE);
        this.store_ex_hrs_tv.setText(this.hbha_pref_helper.getStringValue("store_ex_hrs"));
    }

    private void setHeight() {
        if (this.hbha_pref_helper.getIntValue("list_height") != 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, (this.hbha_pref_helper.getIntValue("list_height") * 0) + (this.hbha_pref_helper.getIntValue("list_height") * this.store_list.size()));
            if (this.store_list_vw != null) {
                this.store_list_vw.setLayoutParams(layoutParams);
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_icon:
                searchStore();
                return;
            case R.id.get_drections:
                setAutoSearch(false);
                return;
            case R.id.call_phone:
                if (!this.shop_phno_tv.getText().toString().equalsIgnoreCase("No Number")) {
                    if (VERSION.SDK_INT < 23) {
                        goToCall();
                        return;
                    } else if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.CALL_PHONE") == 0) {
                        goToCall();
                        return;
                    } else if (shouldShowRequestPermissionRationale("android.permission.CALL_PHONE")) {
                        Builder builder = new Builder(getActivity());
                        builder.setTitle("This app needs call access");
                        builder.setMessage("Go to Settings and enable the phone permission");
                      //  builder.setPositiveButton(17039370, null);
                        builder.setOnDismissListener(new C17984());
                        builder.show();
                        return;
                    } else {
                        requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 1);
                        return;
                    }
                }
                return;
            case R.id.order_now_img_vw:
                this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SHOPONLINE, new MenuModel("Reserve For Pick Up", "", ""));
                return;
            default:
                return;
        }
    }

    private void searchStore() {
        if (Utility.isEmptyString(this.search_et.getText().toString().trim())) {
            Utility.showToast(getActivity(), "Please enter address or zipcode");
            return;
        }
        if (this.googleMap != null) {
            this.googleMap.clear();
        }
        if (this.cd.isConnectingToInternet()) {
            getSearchAllStoreNew1(this.search_et.getText().toString());
        } else {
            Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
        }
    }

    public void getSearchAllStoreNew1(String searchStore) {
        performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        JSONObject store_data = new JSONObject();
        try {
            store_data.put(SearchIntents.EXTRA_QUERY, searchStore);
            store_data.put("radius", "1000");
            store_data.put("count", "100");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.store_list.clear();
        FBStoreService.sharedInstance().getSearchAllStore1(store_data, searchStore, new C17995());
    }

    private void goToLocation(Boolean value) {
        this.gps = new GPSTracker(getActivity());
        if (this.gps.canGetLocation()) {
            double latitude = this.gps.getLatitude();
            double longitude = this.gps.getLongitude();
            if (value.booleanValue()) {
                try {
                    List<Address> addresses = new Geocoder(getActivity(), Locale.getDefault()).getFromLocation(latitude, longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        this.search_et.setText(((Address) addresses.get(0)).getLocality());
                        if (this.googleMap != null) {
                            this.googleMap.clear();
                        }
                        if (this.cd.isConnectingToInternet()) {
                            getSearchAllStoreNew1(((Address) addresses.get(0)).getPostalCode());
                            return;
                        } else {
                            Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
                            return;
                        }
                    }
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + Double.valueOf(this.hbha_pref_helper.getStringValue("lat")) + "," + Double.valueOf(this.hbha_pref_helper.getStringValue("lang")))));
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

    public void performAdapterAction(String tagName, Object data) {
        if (tagName.equals(AppConstants.SET_HEIGHT_LIST)) {
            setHeight();
        } else if (tagName.equals(AppConstants.STORE_SELECT)) {
            if (data != null) {
                this.fragmentActivityListener.performFragmentActivityAction(AppConstants.STORE_MAIN_SAVE, new Object[]{this.store_list, this.search_et.getText()});
                this.fragmentActivityListener.performFragmentActivityAction(AppConstants.STORE_DETAILS_PAGE, (HashMap) data);
            }
        } else if (!tagName.equals("set_store_hours_listview_height")) {
            super.performAdapterAction(tagName, data);
        } else if (data != null) {
            setListViewParams(((Integer) data).intValue());
        }
    }

    private void setListViewParams(int size) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.store_hrs_lv.getLayoutParams();
        if (Utility.isEmpty(this.time_arr)) {
            params.height = 0;
        } else {
            params.height = this.time_arr.length * size;
        }
        this.store_hrs_lv.setLayoutParams(params);
    }

    private void setValues() {
        if (this.googleMap != null) {
            this.googleMap.clear();
        }
        if (this.store_list.size() > 0) {
            if (this.googleMap != null) {
                int gm = 0;
                while (gm < this.store_list.size()) {
                    if (!(((String) ((HashMap) this.store_list.get(gm)).get("lat")).equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO) || ((String) ((HashMap) this.store_list.get(gm)).get("lang")).equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO))) {
                        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf((String) ((HashMap) this.store_list.get(gm)).get("lat")).doubleValue(), Double.valueOf((String) ((HashMap) this.store_list.get(gm)).get("lang")).doubleValue())));
                    }
                    gm++;
                }
                if (this.store_list.size() > 0) {
                    showMap(Double.valueOf((String) ((HashMap) this.store_list.get(0)).get("lat")).doubleValue(), Double.valueOf((String) ((HashMap) this.store_list.get(0)).get("lang")).doubleValue());
                } else {
                    showMap(37.09024d, -95.712891d);
                }
            }
            this.my_store_sv.setVisibility(View.GONE);
            this.store_list_vw.setVisibility(View.GONE);
            this.no_shop_tv.setVisibility(View.GONE);
            if (getActivity() != null) {
                this.storeAdapter = new StoreAdapter(getActivity(), this.store_list, false, this);
                this.store_list_vw.setAdapter(this.storeAdapter);
                return;
            }
            return;
        }
        this.my_store_sv.setVisibility(View.GONE);
        this.store_list_vw.setVisibility(View.GONE);
        this.no_shop_tv.setVisibility(View.GONE);
        this.no_shop_tv1.setText("No Stores Found.");
    }

    public void onLocationChanged(Location location) {
    }

    public void onProviderDisabled(String arg0) {
    }

    public void onProviderEnabled(String arg0) {
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    public void showMap(double v1, double v2) {
        this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(new LatLng(v1, v2)).zoom(5.0f).bearing(0.0f).tilt(45.0f).build()));
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
               // builder.setPositiveButton(17039370, null);
                builder.setOnDismissListener(new C18006());
                builder.show();
                return;
            case 2:
                if (grantResults[0] == 0) {
                    goToLocation(this.setAutoSearch);
                    return;
                }
                builder = new Builder(getActivity());
                builder.setTitle("Functionality limited");
                builder.setMessage("Since location access has not been granted, this app will not be able to get location");
                //builder.setPositiveButton(17039370, null);
                builder.setOnDismissListener(new C18017());
                builder.show();
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
                    goToLocation(this.setAutoSearch);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
