package com.BasicApp.Activites.NonGeneric.Order;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.NewMenuActivity;
import com.BasicApp.Activites.NonGeneric.Payment.PaymentActivity;
import com.BasicApp.Activites.NonGeneric.Store.StoreListActivity;
import com.BasicApp.Adapters.ListOrderConfirmAdapter;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.MenuDrawerItem;
import com.BasicApp.BusinessLogic.Models.NewMenuDrawer;
import com.BasicApp.BusinessLogic.Models.OrderConfirmDrawItem;
import com.BasicApp.BusinessLogic.Models.OrderItem;
import com.BasicApp.BusinessLogic.Models.OrderProductList;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.activity.DeliveryActivity;
import com.Preferences.FBPreferences;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.MyMessageDialog;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;



/**
 * Created by digvijay(dj)
 */

@SuppressLint("NewApi")
public class OrderConfirmActivity extends ActionBarActivity implements View.OnClickListener {
    public static Boolean checkback = false;
    public static Boolean historyflag = false;
    public static List<OrderConfirmDrawItem> dataList;
    TextView addmore, continuetv;
    ListView lv;
    ActionBar mActionbar;
    ListOrderConfirmAdapter adapter;
    List<NewMenuDrawer> listss;
    NewMenuDrawer drawitem = null;
    OrderItem order = null;
    String storelocation;
    FBStoresItem location;
    private Toolbar toolbar;
    private ImageLoader mImageLoader;
    private NetworkImageView background;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            drawitem = OrderProductList.sharedInstance().currentAdded;
            order = (OrderItem) extras.getSerializable("order");
            listss = OrderProductList.sharedInstance().orderProductList;
            location = (FBStoresItem) extras.getSerializable("location");

            historyflag = extras.getBoolean("historyflag", false);
            storelocation = extras.getString("storelocation");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirm);
        background = (NetworkImageView) findViewById(R.id.img_Back);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.title_textb).setOnClickListener(first_radio_listener);
        toolbar.findViewById(R.id.title_textf).setOnClickListener(first_radio_listener);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderConfirmActivity.this.finish();
            }
        });
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(this);
        mActionbar = getSupportActionBar();
        mActionbar.hide();
        mActionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FAE3AF")));
        lv = (ListView) this.findViewById(R.id.mobile_listdf);
        adapter = new ListOrderConfirmAdapter(this, drawitem, listss, checkback, historyflag, order, storelocation, location);
        adapter.notifyDataSetChanged();
        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        setActionBar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FBUtils.setUpNavigationDrawer(OrderConfirmActivity.this);


    }

    @SuppressLint("NewApi")
    protected void setActionBar() {
        mActionbar.setDisplayShowHomeEnabled(false);
        mActionbar.setDisplayShowTitleEnabled(false);
        mActionbar.setHomeButtonEnabled(false);
        mActionbar.setDisplayHomeAsUpEnabled(false);
    }



    @Override
    public void onStart() {
        super.onStart();
        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));

        }
        lv.setAdapter(adapter);
        lv.invalidateViews();
        lv.refreshDrawableState();
        adapter.notifyDataSetChanged();
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
        BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
        b.initBottomToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_order, menu);
        MenuItem itemmo = menu.findItem(R.id.menu_edit1);
        MenuItemCompat.setActionView(itemmo, R.layout.custombar);
        RelativeLayout offer = (RelativeLayout) MenuItemCompat.getActionView(itemmo);
        ImageView backclick = (ImageView) offer.findViewById(R.id.imageView1);
        backclick.setOnClickListener(first_radio_listener);
        addmore = (TextView) offer.findViewById(R.id.title_textb);
        continuetv = (TextView) offer.findViewById(R.id.title_textf);
        addmore.setOnClickListener(first_radio_listener);
        continuetv.setOnClickListener(first_radio_listener);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else if (i == R.id.menu_edit1) {
            List<MenuDrawerItem> lists = (List<MenuDrawerItem>) lv.getTag();
            if (order != null && StringUtilities.isValidString(order.getOrdereDateNTime()))

            {
                Intent intensw = new Intent(this, PaymentActivity.class);
                startActivity(intensw);
            } else if (lists.size() > 0 && lists != null) {
                //   ExpenseAttachmentAPICall();
                Intent intensw = new Intent(this, StoreListActivity.class);
                startActivity(intensw);
            } else {
                new MyMessageDialog(OrderConfirmActivity.this, "ITEM", "PLease select item before continue").show(getSupportFragmentManager(), "MyDialog");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            checkback = true;
            this.finish();

        } else {
            checkback = true;
            getFragmentManager().popBackStack();

        }
    }


    OnClickListener first_radio_listener = new OnClickListener() {
        public void onClick(View v) {

            int i = v.getId();
            if (i == R.id.title_textb) {
                checkback = false;
                Intent inten = new Intent(OrderConfirmActivity.this, NewMenuActivity.class);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(inten);
                ;
            } else if (i == R.id.title_textf) {
                List<NewMenuDrawer> lists = OrderProductList.sharedInstance().orderProductList;
                if (lists != null) {

                    if (order != null && StringUtilities.isValidString(order.getOrdereDateNTime()) && lists.size() > 0) {
                        Confirm();
                        FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(55), "", FBEventSettings.SUBMIT_ORDER);
                    } else if (FBPreferences.sharedInstance(OrderConfirmActivity.this).IsDashboardin() && OrderProductList.sharedInstance().getCurrentLoaction() != null)

                    {
                        delivery();

                    } else if (lists.size() > 0 && lists != null) {
//                        //   ExpenseAttachmentAPICall();
                        Intent intensw = new Intent(OrderConfirmActivity.this, StoreListActivity.class);
                        startActivity(intensw);
                    } else {
                        new MyMessageDialog(OrderConfirmActivity.this, "ITEM", "PLease select item before continue").show(getSupportFragmentManager(), "MyDialog");
                    }

                } else {
                    new MyMessageDialog(OrderConfirmActivity.this, "ITEM", "No Item Found for order, Select order from menu").show(getSupportFragmentManager(), "MyDialog");

                }


            } else if (i == R.id.imageView1) {
                finish();

            } else {
            }
        }
    };


    public void backAndAdd(View v) {
        Intent intent = new Intent(this, NewMenuActivity.class);
        Bundle extras = new Bundle();
        checkback = false;
        intent.putExtras(extras);
        setResult(2, intent);
        startActivityForResult(intent, 2);


    }

    public void Confirm() {
        JSONObject userJSON = new JSONObject();
        try {
            userJSON.put("orderNumber", "-1");
            userJSON.put("customerId", "123");
            userJSON.put("customerName", "yes");
            userJSON.put("companyID", "8");
            {
                Intent intent = new Intent(this, PaymentActivity.class);
                FBPreferences.sharedInstance(OrderConfirmActivity.this).setDashboardin(false);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void order(View v) {

        List<MenuDrawerItem> lists = (List<MenuDrawerItem>) lv.getTag();
        if (lists.size() > 0 && lists != null) {
            Intent intensw = new Intent(this, StoreListActivity.class);
            startActivity(intensw);
        } else {
            new MyMessageDialog(OrderConfirmActivity.this, "ITEM", "Please select item before continue").show(getSupportFragmentManager(), "MyDialog");
        }
    }

    public void cancelorder(View v) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Cancel Order");
        alertDialog.setMessage("Do you want to cancle Order");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Bundle extras = new Bundle();
                FBAnalyticsManager.sharedInstance().track_ItemWith("51", "", FBEventSettings.BASKET_DELETE);
                Intent inten = new Intent(OrderConfirmActivity.this, NewMenuActivity.class);
                inten.putExtras(extras);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                OrderProductList.sharedInstance().orderProductList.clear();
                startActivity(inten);
                OrderConfirmActivity.this.finish();
            }
        });


        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();


    }


    public void delivery() {
        Intent intent = new Intent(this, DeliveryActivity.class);
        startActivity(intent);
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

}
