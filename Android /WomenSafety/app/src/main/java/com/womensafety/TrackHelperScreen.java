package com.womensafety;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.womensafety.constant.Constant;
import com.womensafety.object.HelperDO;

import java.util.ArrayList;
import java.util.Iterator;

public class TrackHelperScreen extends BaseActivity implements OnMapReadyCallback, OnMapLongClickListener {
    int count = 0;
    private ArrayList<HelperDO> arrayList;
    private LinearLayout llMap;
    private ListView lvHelperList;
    private GoogleMap mMap;
    private MapFragment mMapFragment;
    private TextView tvTotal;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void initialize() {
        this.llMap = (LinearLayout) this.inflater.inflate(R.layout.track_helper, null);
        this.llBody.addView(this.llMap, -1, -1);
        if (getIntent().getExtras() != null) {
            this.arrayList = (ArrayList) getIntent().getExtras().get("arrayList");
        }
        final FrameLayout topFrameLayout = (FrameLayout) this.llMap.findViewById(R.id.root_container);
        this.lvHelperList = (ListView) this.llMap.findViewById(R.id.lvHelperList);
        this.tvTotal = (TextView) this.llMap.findViewById(R.id.tvTotal);
        if (this.arrayList != null && this.arrayList.size() > 0) {
            this.tvTotal.setText(this.arrayList.size() + " " + getString(R.string.on_the_way));
        }
        topFrameLayout.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                insets = topFrameLayout.onApplyWindowInsets(insets);
                LayoutParams params = (LayoutParams) topFrameLayout.getLayoutParams();
                params.setMargins(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
                topFrameLayout.setLayoutParams(params);
                return insets;
            }
        });
        this.mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        this.mMapFragment.getMapAsync(this);
        this.lvHelperList.setAdapter(new HelperAdapter());
    }

    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            this.mMap = googleMap;
            this.mMap.setOnMapLongClickListener(this);
            this.mMap.setMyLocationEnabled(true);
            doSomthing();
            return;
        }
        Toast.makeText(this, "Unable to load map.", 0).show();
    }

    public void onMapLongClick(LatLng latLng) {
    }

    private void doSomthing() {
        this.count = 0;
        this.mMap.clear();
        LatLng latLngM = new LatLng(Constant.latitude, Constant.longitude);
        Iterator it = this.arrayList.iterator();
        while (it.hasNext()) {
            HelperDO helperDO = (HelperDO) it.next();
            addMarker(this.mMap, new LatLng(helperDO.Latitude, helperDO.Longitude), helperDO);
        }
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngM, 14.0f));
    }

    private void addMarker(GoogleMap Map, LatLng latLng, HelperDO object) {
        Map.addMarker(new MarkerOptions().position(latLng).title(object.helperName).snippet((this.count + 1) + object.estimatedTime).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        this.count++;
    }

    class HelperAdapter extends BaseAdapter {
        HelperAdapter() {
        }

        public int getCount() {
            if (TrackHelperScreen.this.arrayList == null || TrackHelperScreen.this.arrayList.size() <= 0) {
                return 0;
            }
            return TrackHelperScreen.this.arrayList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            HelperDO helperDO = (HelperDO) TrackHelperScreen.this.arrayList.get(position);
            if (convertView == null) {
                convertView = TrackHelperScreen.this.inflater.inflate(R.layout.helper_cell, null);
            }
            TextView tvDist = (TextView) convertView.findViewById(R.id.tvDist);
            ((TextView) convertView.findViewById(R.id.tvName)).setText(helperDO.helperName);
            tvDist.setText((position + 1) + helperDO.estimatedTime);
            return convertView;
        }
    }
}
