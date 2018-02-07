package com.BasicApp.Activites.Generic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BasicApp.BusinessLogic.Models.MenuDrawerItem;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.basicmodule.sdk.R;
import com.BasicApp.BusinessLogic.Models.OrderItem;
import com.Preferences.FBPreferences;

import java.util.List;

/**
 * Created by schaudhary_ic on 03-Oct-16.
 */

public class BottomToolbarActivity extends LinearLayout implements View.OnClickListener {
    private TextView storename, storeno, storelocationn,distance;
    FBStoresItem location;
    LinearLayout car_layout;
    OrderItem order = null;
    ImageButton call;
    Integer storeId;
    List<MenuDrawerItem> listss;
    String strnum, strloc, strname;
    public static Boolean historyflag = false;
    String storelocation;

    Float storedistance;
    public BottomToolbarActivity(Context context) {
        super(context);
    }

    public BottomToolbarActivity(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomToolbarActivity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initBottomToolbar() {
        inflateBottomToolbar();
    }

    private void inflateBottomToolbar() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.bottom_toolbar_activity, this);
        /*logo = (ImageView) findViewById(R.id.logo);
        label = (TextView) findViewById(R.id.label);
        loginButton = (Button) findViewById(R.id.login);*/
        storename = (TextView) v.findViewById(R.id.storename);
        storeno = (TextView) v.findViewById(R.id.storeno);
        storelocationn = (TextView) v.findViewById(R.id.storelocation);

        distance = (TextView) v.findViewById(R.id.distance);
        call = (ImageButton) v.findViewById(R.id.call);

        call.setOnClickListener(this);


car_layout = (LinearLayout) v.findViewById(R.id.car_layout);
        car_layout.setOnClickListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        if (FBPreferences.sharedInstance(getContext()).getStoreName() != null) { /*String strname = preferences.getString("Storename", null);
       //  strnum = preferences.getString("Storenum", "");
        String strloc = preferences.getString("Storeloc", "");
    */
            strnum = FBPreferences.sharedInstance(getContext()).getStorePhoneNum();
            strloc = FBPreferences.sharedInstance(getContext()).getStoreAddress();
            strname = FBPreferences.sharedInstance(getContext()).getStoreName();
            storedistance = FBPreferences.sharedInstance(getContext()).getStoreDistance();
            storename.setText(strname);
            storeno.setText(strnum);
            storelocationn.setText(strloc);
            distance.setText(String.valueOf(storedistance)+"mi"
            );
        } else {
            storename.setText("SunWest Village");
            storeno.setText("209.368.2037");
            storelocationn.setText("2624 W. Kettelman Ln");
            distance.setText(String.valueOf("5.0 mi"));

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        inflateBottomToolbar();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.call) {
            Intent myIntent = new Intent(Intent.ACTION_DIAL);
            //    myIntent.setPackage("com.android.phone");
            myIntent.setData(Uri.parse("tel:" + strnum));
            try {
                getContext().startActivity(myIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId() ==R.id.car_layout){
            Intent i = new Intent(getContext(),DirectionMapActivity.class);
            getContext().startActivity(i);
    }
}}
