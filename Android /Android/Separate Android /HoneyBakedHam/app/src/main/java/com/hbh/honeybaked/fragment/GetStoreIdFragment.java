package com.hbh.honeybaked.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.ServerProtocol;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
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
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.supportingfiles.Utility;
import com.hbh.honeybaked.tracker.GPSTracker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class GetStoreIdFragment extends BaseFragment {
    GoogleMap googleMap = null;
    GPSTracker gps;
    private boolean mShowDialog = false;
    StoreAdapter storeAdapter = null;
    ArrayList<HashMap<String, String>> store_list1 = new ArrayList();
    ListView storeid_list_vw;
    RelativeLayout storeid_no_shop_rl;
    TextView storeid_no_shop_tv;
    EditText storeid_search_et;
    RelativeLayout storeid_search_ll;

    class C17281 implements OnDismissListener {
        C17281() {
        }

        @TargetApi(23)
        public void onDismiss(DialogInterface dialogInterface) {
            GetStoreIdFragment.this.requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 2);
        }
    }

    class C17303 implements FBAllSearchStorejsonCallback {
        C17303() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void OnAllSearchStorejsonCallback(JSONObject r13, Exception r14) {
            /*
            r12 = this;
            if (r13 == 0) goto L_0x0206;
        L_0x0002:
            r8 = "successFlag";
            r8 = r13.optBoolean(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r9 = 1;
            if (r8 != r9) goto L_0x01f4;
        L_0x000b:
            r8 = "storeList";
            r8 = r13.has(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            if (r8 == 0) goto L_0x01e2;
        L_0x0013:
            r8 = "storeList";
            r5 = r13.optJSONArray(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = r5.length();	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            if (r8 <= 0) goto L_0x01d1;
        L_0x001f:
            r0 = 0;
        L_0x0020:
            r8 = r5.length();	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            if (r0 >= r8) goto L_0x01e2;
        L_0x0026:
            r7 = r5.getJSONObject(r0);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6 = new java.util.HashMap;	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.<init>();	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "storeID";
            r9 = "storeId";
            r9 = r7.optInt(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r9 = java.lang.String.valueOf(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "storeName";
            r9 = "storeName";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "description";
            r9 = "description";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "address";
            r9 = "address";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "city";
            r9 = "city";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "zipcode";
            r9 = "zipCode";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "state";
            r9 = "state";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "country";
            r9 = "country";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "mobile";
            r9 = "mobile";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "email";
            r9 = "email";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "phone";
            r9 = "phone";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r9 = com.hbh.honeybaked.supportingfiles.Utility.convertToUsFormat(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "storeNumber";
            r9 = "number";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "distance";
            r9 = "distance";
            r9 = r7.optInt(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r9 = java.lang.String.valueOf(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "refernceUrl";
            r9 = "refernceUrl";
            r9 = r7.optString(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "latitude";
            r8 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r10 = 0;
            r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
            if (r8 != 0) goto L_0x0151;
        L_0x00e1:
            r8 = "lat";
            r9 = "0";
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
        L_0x00e8:
            r8 = "longitude";
            r8 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r10 = 0;
            r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
            if (r8 != 0) goto L_0x0188;
        L_0x00f4:
            r8 = "lang";
            r9 = "0";
            r6.put(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
        L_0x00fb:
            r8 = "address";
            r8 = r7.optString(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r9 = "";
            r4 = com.hbh.honeybaked.supportingfiles.Utility.getStoreValues(r8, r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "mailingState";
            r2 = r7.optString(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "mailingZip";
            r3 = r7.optString(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r1 = "";
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r2);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            if (r8 != 0) goto L_0x01bf;
        L_0x011b:
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r3);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            if (r8 != 0) goto L_0x01bf;
        L_0x0121:
            r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8.<init>();	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = r8.append(r2);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r9 = " ";
            r8 = r8.append(r9);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = r8.append(r3);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r1 = r8.toString();	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
        L_0x0138:
            r4 = com.hbh.honeybaked.supportingfiles.Utility.getStoreValues(r1, r4);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = "store_address_string";
            r6.put(r8, r4);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = r8.store_list1;	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8.add(r6);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8.setValues();	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r0 = r0 + 1;
            goto L_0x0020;
        L_0x0151:
            r9 = "lat";
            r8 = "latitude";
            r10 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            if (r8 != 0) goto L_0x0185;
        L_0x0163:
            r8 = "latitude";
            r10 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
        L_0x016d:
            r6.put(r9, r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            goto L_0x00e8;
        L_0x0172:
            r8 = move-exception;
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);
            r8.performDialogAction(r9, r10);
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;
            r8.setValues();
        L_0x0184:
            return;
        L_0x0185:
            r8 = "";
            goto L_0x016d;
        L_0x0188:
            r9 = "lang";
            r8 = "longitude";
            r10 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            if (r8 != 0) goto L_0x01bc;
        L_0x019a:
            r8 = "longitude";
            r10 = r7.optDouble(r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
        L_0x01a4:
            r6.put(r9, r8);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            goto L_0x00fb;
        L_0x01a9:
            r8 = move-exception;
            r9 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;
            r10 = "hide_progress_dialog";
            r11 = 0;
            r11 = java.lang.Boolean.valueOf(r11);
            r9.performDialogAction(r10, r11);
            r9 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;
            r9.setValues();
            throw r8;
        L_0x01bc:
            r8 = "";
            goto L_0x01a4;
        L_0x01bf:
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r2);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            if (r8 != 0) goto L_0x01c8;
        L_0x01c5:
            r1 = r2;
            goto L_0x0138;
        L_0x01c8:
            r8 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r3);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            if (r8 != 0) goto L_0x0138;
        L_0x01ce:
            r1 = r3;
            goto L_0x0138;
        L_0x01d1:
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8.performDialogAction(r9, r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8.setValues();	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
        L_0x01e2:
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);
            r8.performDialogAction(r9, r10);
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;
            r8.setValues();
            goto L_0x0184;
        L_0x01f4:
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8.performDialogAction(r9, r10);	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            r8.setValues();	 Catch:{ Exception -> 0x0172, all -> 0x01a9 }
            goto L_0x01e2;
        L_0x0206:
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);
            r8.performDialogAction(r9, r10);
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;
            r8 = r8.getActivity();
            com.hbh.honeybaked.supportingfiles.Utility.hideSoftKeyboard(r8);
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;
            r8 = r8.getActivity();
            com.hbh.honeybaked.supportingfiles.Utility.tryHandleTokenExpiry(r8, r14);
            r8 = com.hbh.honeybaked.fragment.GetStoreIdFragment.this;
            r8.setValues();
            goto L_0x0184;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.hbh.honeybaked.fragment.GetStoreIdFragment.3.OnAllSearchStorejsonCallback(org.json.JSONObject, java.lang.Exception):void");
        }
    }

    class C17314 implements OnDismissListener {
        C17314() {
        }

        public void onDismiss(DialogInterface dialog) {
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_getstoreid, container, false);
        this.storeid_list_vw = (ListView) v.findViewById(R.id.storeid_list_vw);
        this.storeid_search_et = (EditText) v.findViewById(R.id.storeid_search_et);
        this.storeid_search_ll = (RelativeLayout) v.findViewById(R.id.storeid_search_ll);
        this.storeid_no_shop_rl = (RelativeLayout) v.findViewById(R.id.storeid_no_shop_rl);
        this.storeid_no_shop_tv = (TextView) v.findViewById(R.id.storeid_no_shop_tv);
        this.storeid_search_ll.setOnClickListener(this);
        this.storeid_list_vw.setVisibility(8);
        this.storeid_no_shop_rl.setVisibility(0);
        this.storeid_no_shop_tv.setText("Enter in the field above an address, Zip code, city name or store name");
        if (VERSION.SDK_INT < 23) {
            getLocation();
        } else if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") == 0) {
            getLocation();
        } else if (shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION")) {
            Builder builder = new Builder(getActivity());
            builder.setTitle("This app needs location access");
            builder.setMessage("Go to Settings and enable the location permission");
            builder.setPositiveButton(17039370, null);
            builder.setOnDismissListener(new C17281());
            builder.show();
        } else {
            requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 2);
        }
        if (this.googleMap == null) {
            try {
                //this.googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.storeid_map)).getMapAsync();
                CameraPosition googlePlex = CameraPosition.builder().target(new LatLng(37.09024d, -95.712891d)).zoom(3.0f).bearing(0.0f).tilt(45.0f).build();
                if (this.googleMap != null) {
                    this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return v;
    }

    public void getLocation() {
        this.gps = new GPSTracker(getActivity());
        if (this.gps.canGetLocation()) {
            double latitude;
            double longitude;
            while (true) {
                latitude = this.gps.getLatitude();
                longitude = this.gps.getLongitude();
                if (latitude != 0.0d && longitude != 0.0d) {
                    break;
                }
            }
            try {
                List<Address> addresses = new Geocoder(getActivity(), Locale.getDefault()).getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    this.storeid_search_et.setText(((Address) addresses.get(0)).getLocality());
                    if (this.googleMap != null) {
                        this.googleMap.clear();
                    }
                    if (this.cd.isConnectingToInternet()) {
                        final String postalCode = ((Address) addresses.get(0)).getPostalCode();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                GetStoreIdFragment.this.getSearchAllStores(postalCode);
                            }
                        });
                        return;
                    }
                    Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
                    return;
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        this.gps.showSettingsAlert();
    }

    public void getSearchAllStores(String search_val) {
        performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        JSONObject store_data = new JSONObject();
        try {
            store_data.put(SearchIntents.EXTRA_QUERY, search_val);
            store_data.put("radius", "1000");
            store_data.put("count", "100");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.store_list1.clear();
        FBStoreService.sharedInstance().getSearchAllStore1(store_data, search_val, new C17303());
    }

    public void performAdapterAction(String tagName, Object data) {
        if (!tagName.equals(AppConstants.STORE_SELECT)) {
            super.performAdapterAction(tagName, data);
        } else if (data != null) {
            HashMap<String, String> store_map = (HashMap) data;
            this.hbha_pref_helper.saveStringValue("reg_store_id", (String) store_map.get("storeID"));
            this.hbha_pref_helper.saveStringValue("reg_store_nm", (String) store_map.get("storeName"));
            this.hbha_pref_helper.saveStringValue("reg_store_code", (String) store_map.get("storeNumber"));
            this.hbha_pref_helper.saveStringValue("reg_store_add", (Utility.isEmptyString((String) store_map.get("store_address_string")) ? "" : ((String) store_map.get("store_address_string")).trim() + ", ") + (Utility.isEmptyString((String) store_map.get("city")) ? "" : ((String) store_map.get("city")).trim() + ", ") + (Utility.isEmptyString((String) store_map.get(ServerProtocol.DIALOG_PARAM_STATE)) ? "" : ((String) store_map.get(ServerProtocol.DIALOG_PARAM_STATE)).trim() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR) + (Utility.isEmptyString((String) store_map.get("zipcode")) ? "" : ((String) store_map.get("zipcode")).trim()));
            this.hbha_pref_helper.saveStringValue("reg_store_ph", ((String) store_map.get("phone")).trim());
            this.hbha_pref_helper.saveStringValue("reg_store_city", (String) store_map.get("city"));
            this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SIGNUP_PAGE, Boolean.valueOf(false));
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.storeid_search_ll:
                if (Utility.isEmptyString(this.storeid_search_et.getText().toString().trim())) {
                    Utility.showToast(getActivity(), "Please enter address or zipcode");
                    return;
                }
                if (this.googleMap != null) {
                    this.googleMap.clear();
                }
                if (this.cd.isConnectingToInternet()) {
                    getSearchAllStores(this.storeid_search_et.getText().toString());
                    return;
                } else {
                    Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
                    return;
                }
            default:
                return;
        }
    }

    private void setValues() {
        if (this.googleMap != null) {
            this.googleMap.clear();
        }
        if (this.store_list1.size() > 0) {
            if (this.googleMap != null) {
                int gm = 0;
                while (gm < this.store_list1.size()) {
                    if (!(((String) ((HashMap) this.store_list1.get(gm)).get("lat")).equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO) || ((String) ((HashMap) this.store_list1.get(gm)).get("lang")).equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO))) {
                        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf((String) ((HashMap) this.store_list1.get(gm)).get("lat")).doubleValue(), Double.valueOf((String) ((HashMap) this.store_list1.get(gm)).get("lang")).doubleValue())));
                    }
                    gm++;
                }
                if (this.store_list1.size() > 0) {
                    showMap(Double.valueOf((String) ((HashMap) this.store_list1.get(0)).get("lat")).doubleValue(), Double.valueOf((String) ((HashMap) this.store_list1.get(0)).get("lang")).doubleValue());
                } else {
                    showMap(37.09024d, -95.712891d);
                }
            }
            this.storeid_list_vw.setVisibility(0);
            this.storeid_no_shop_rl.setVisibility(8);
            if (getActivity() != null) {
                this.storeAdapter = new StoreAdapter(getActivity(), this.store_list1, true, this);
                this.storeid_list_vw.setAdapter(this.storeAdapter);
                return;
            }
            return;
        }
        this.storeid_list_vw.setVisibility(8);
        this.storeid_no_shop_rl.setVisibility(0);
        this.storeid_no_shop_tv.setText("No Stores Found");
    }

    public void showMap(double v1, double v2) {
        this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(new LatLng(v1, v2)).zoom(5.0f).bearing(0.0f).tilt(45.0f).build()));
    }

    @TargetApi(17)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults[0] == 0) {
                    this.mShowDialog = true;
                    return;
                }
                Builder builder = new Builder(getActivity());
                builder.setTitle("Functionality limited");
                builder.setMessage("Since location access has not been granted, this app will not be able to get location");
                builder.setPositiveButton(17039370, null);
                builder.setOnDismissListener(new C17314());
                builder.show();
                return;
            default:
                return;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                    this.mShowDialog = true;
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mShowDialog) {
            this.mShowDialog = false;
            getLocation();
        }
    }
}
