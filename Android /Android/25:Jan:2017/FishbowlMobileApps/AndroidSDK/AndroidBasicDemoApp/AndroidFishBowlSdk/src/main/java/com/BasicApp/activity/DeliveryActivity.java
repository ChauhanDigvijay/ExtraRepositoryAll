package com.BasicApp.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.BasicApp.Activites.NonGeneric.Order.OrderConfirmActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.NewMenuDrawer;
import com.BasicApp.BusinessLogic.Models.OrderConfirmDrawItem;
import com.BasicApp.BusinessLogic.Models.OrderItem;
import com.BasicApp.BusinessLogic.Models.OrderProductList;
import com.BasicApp.Utils.FBUtils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * *
 * Created by Digvijay Chauhan on 25/12/15.
 */


@SuppressLint("NewApi")
public class DeliveryActivity extends ActionBarActivity implements View.OnClickListener{
    public static Boolean checkback = false;
    public static Boolean historyflag = false;
    public static List<OrderConfirmDrawItem> dataList;
    Button sign_delivery, sign_takeout;
    TextView conti;
    TextView tv1;
    TextView mEdit;
    ActionBar mActionbar;
    List<NewMenuDrawer> listss;
    OrderItem order = null;
    String storelocation;
    FBStoresItem location;

    private ImageLoader mImageLoader;
    private NetworkImageView background;
    private DrawerLayout drawerLayout;
    private RadioGroup radioGroup;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            location = (FBStoresItem) extras.getSerializable("location");
            order = (OrderItem) extras.getSerializable("order");
            listss = (List<NewMenuDrawer>) i.getSerializableExtra("draweritem1");
            historyflag = extras.getBoolean("historyflag", false);
            storelocation = extras.getString("storelocation");


        } else {

            OrderItem order1 = new OrderItem();
            final ArrayList<NewMenuDrawer> lists = OrderProductList.sharedInstance().orderProductList;

            order = order1;
            listss = lists;
            historyflag = false;
            storelocation = location.getAddress();
        }

        setContentView(R.layout.activity_delivery);
        background = (NetworkImageView) findViewById(R.id.img_Back);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.title_textf).setOnClickListener(first_radio_listener);

        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeliveryActivity.this.finish();
            }
        });
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(this);
        mActionbar = getSupportActionBar();
        mActionbar.hide();
        mActionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FAE3AF")));

        sign_delivery = (Button) findViewById(R.id.sign_delivery);
        sign_delivery.setTextColor(getResources().getColor(R.color.White));
        sign_delivery.setBackgroundColor(getResources().getColor(R.color.RedOrgin));

        sign_takeout = (Button) findViewById(R.id.sign_takeout);
        sign_takeout.setTextColor(getResources().getColor(R.color.RedOrgin));
        sign_takeout.setBackgroundColor(getResources().getColor(R.color.White));

        TextView T1 = (TextView) findViewById(R.id.text_location);
        TextView T2 = (TextView) findViewById(R.id.text_locationname);
        TextView T3 = (TextView) findViewById(R.id.changelocation_txt);
        mEdit = (TextView) findViewById(R.id.editText1);
        tv1 = (TextView) findViewById(R.id.editText2);


        SimpleDateFormat df = new SimpleDateFormat(" HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        order.setOrdereDateNTime(date);
        tv1.setText(date);


        SimpleDateFormat sdf = new SimpleDateFormat("dd//MM/yyyy");
        mEdit.setText(sdf.format(new Date()));
        order.setOrdereDateNTime(sdf.format(new Date()));
        T3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
//                Intent intent = new Intent(DeliveryActivity.this, StoreListActivity.class);
//                startActivity(intent);

            }
        });

        if (StringUtilities.isValidString(order.getStoreID())) {

            T1.setText(location.getAddress());
            T2.setText(location.getStoreName());

        }
        FBUtils.setUpNavigationDrawer(DeliveryActivity.this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


    }

    @Override
    public void onStart() {
        super.onStart();
        setActionBar();
        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();

            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));
            // background.setImageUrl(url, mImageLoader);

        }

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    protected void setActionBar() {
        mActionbar.setDisplayShowHomeEnabled(false);
        mActionbar.setDisplayShowTitleEnabled(false);
        mActionbar.setHomeButtonEnabled(false);
        mActionbar.setDisplayHomeAsUpEnabled(false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_time, menu);
        MenuItem itemmo = menu.findItem(R.id.menu_edit1);
        MenuItemCompat.setActionView(itemmo, R.layout.customtimemenu);
        RelativeLayout offer = (RelativeLayout) MenuItemCompat.getActionView(itemmo);
        ImageView backclick = (ImageView) offer.findViewById(R.id.imageView1);
        backclick.setOnClickListener(first_radio_listener);
        conti = (TextView) offer.findViewById(R.id.title_textf);
        conti.setOnClickListener(first_radio_listener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {

            this.finish();
        } else {

            getFragmentManager().popBackStack();
        }
    }

    public void contiue(View v) {
        {
            Intent intent = new Intent(this, OrderConfirmActivity.class);
            Bundle extras = new Bundle();
            {
                extras.putSerializable("draweritem1", (Serializable) listss);
                extras.putSerializable("order", order);
                extras.putSerializable("historyflag", true);
                extras.putSerializable("storelocation", storelocation);
            }
            intent.putExtras(extras);
            startActivityForResult(intent, 2);
        }
    }

    public void signin(View v) {
        FBAnalyticsManager.sharedInstance().track_ItemWith("55", "ASAP", FBEventSettings.PICK_UP_TIME);
        String signdeliver = sign_delivery.getText().toString();
        order.setOrderType(signdeliver);
        sign_delivery.setTextColor(getResources().getColor(R.color.White));
        sign_takeout.setTextColor(getResources().getColor(R.color.RedOrgin));
        sign_delivery.setBackgroundColor(getResources().getColor(R.color.RedOrgin));
        sign_takeout.setBackgroundColor(getResources().getColor(R.color.White));
    }

    public void signup(View v) {
        FBAnalyticsManager.sharedInstance().track_ItemWith("55", "ASAP", FBEventSettings.PICK_UP_TIME);
        String signtake = sign_takeout.getText().toString();
        order.setOrderType(signtake);
        sign_delivery.setTextColor(getResources().getColor(R.color.RedOrgin));
        sign_takeout.setTextColor(getResources().getColor(R.color.White));
        sign_delivery.setBackgroundColor(getResources().getColor(R.color.White));
        sign_takeout.setBackgroundColor(getResources().getColor(R.color.RedOrgin));
    }

    public void selectDate(View view) {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    public void populateSetDate(int year, int month, int day) {

        order.setOrdereDateNTime(day + "/" + month + "/" + year);
        mEdit.setText(day + "/" + month + "/" + year);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }




    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }
    }

    public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


            order.setOrderTime("" + view.getCurrentHour() + " : " + view.getCurrentMinute());
            tv1.setText(+view.getCurrentHour() + " : " + view.getCurrentMinute());


        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_navigator) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else
                drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    View.OnClickListener first_radio_listener = new View.OnClickListener() {
        public void onClick(View v) {


            int i = v.getId();
            if (i == R.id.imageView1) {/*Intent inta = new Intent(TestActivity.this, MenuActivity.class);
				startActivity(inta);;*/
                finish();

            } else if (i == R.id.title_textf) {
                Intent intent = new Intent(DeliveryActivity.this, OrderConfirmActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("draweritem1", (Serializable) listss);
                extras.putSerializable("order", order);
                extras.putSerializable("historyflag", true);
                extras.putSerializable("storelocation", storelocation);
                extras.putSerializable("location", location);


                intent.putExtras(extras);
                startActivityForResult(intent, 2);

            } else {
            }
        }
    };
}
