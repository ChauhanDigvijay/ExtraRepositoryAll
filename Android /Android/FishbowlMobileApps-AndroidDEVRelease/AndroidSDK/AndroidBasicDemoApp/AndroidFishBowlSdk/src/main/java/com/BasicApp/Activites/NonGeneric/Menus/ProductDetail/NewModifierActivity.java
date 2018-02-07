package com.BasicApp.Activites.NonGeneric.Menus.ProductDetail;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.MenuActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.MenuDrawerItem;
import com.BasicApp.BusinessLogic.Models.MenuProductDetail;
import com.BasicApp.BusinessLogic.Models.NewMenuDrawer;
import com.BasicApp.BusinessLogic.Models.OrderConfirmDrawItem;
import com.BasicApp.BusinessLogic.Models.OrderItem;
import com.BasicApp.BusinessLogic.Models.OrderProductList;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.BasicApp.Utils.StringUtilities;
import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.activity.ModifierActivity;
import com.BasicApp.Activites.NonGeneric.Order.OrderConfirmActivity;
import com.Preferences.FBPreferences;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by digvijay(dj)
 */

public class NewModifierActivity extends ActionBarActivity implements View.OnClickListener{
    public static Boolean checkflag = false;
    public static List<MenuDrawerItem> dataList1;
    public static List<OrderConfirmDrawItem> dataList;
    public static int count = 1;
    public ModifierActivity.MyAdapter mAdapter;
    public ArrayList<MenuProductDetail> mcatList = new ArrayList<MenuProductDetail>();
    ProgressBarHandler p;
    ImageView cartiv;
    TextView addcart, cart_textview;
    ListView lv;
    ActionBar mActionbar;
    List<MenuDrawerItem> listss;
    NewMenuDrawer drawitem = null;
    OrderItem order = null;
    String storelocation;

    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter2;
    ArrayList<String> list1 = new ArrayList<String>();
    ArrayAdapter<String> adapter3;
    int proposition, catposition;
    String name, desc, imageUrl;
    int productID, categoryID, subCategoryID;
    View.OnClickListener first_radio_listener = new View.OnClickListener() {
        public void onClick(View v) {

            //Your Implementaions...
            int i = v.getId();
            if (i == R.id.title_textf) {
                Intent intent = new Intent(NewModifierActivity.this, OrderConfirmActivity.class);
                Bundle extras = new Bundle();
                if (checkflag) {
                    OrderProductList.sharedInstance().addIteme(drawitem);
                    OrderProductList.sharedInstance().currentAdded = drawitem;
                }
                intent.putExtras(extras);
                startActivityForResult(intent, 2);
            } else if (i == R.id.imageButton) {

                Intent menucart = new Intent(NewModifierActivity.this, OrderConfirmActivity.class);
                startActivity(menucart);

            } else if (i == R.id.imageView1) {
                finish();

            } else {
            }
        }
    };
    private ImageLoader mImageLoader;
    private NetworkImageView background, netimage;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {


            productID = extras.getInt("productID");
            categoryID = extras.getInt("categoryID");
            subCategoryID = extras.getInt("subCategoryID");
            drawitem = (NewMenuDrawer) extras.getSerializable("draweritem1");

        }
        setContentView(R.layout.activity_modifier_new);
        p = new ProgressBarHandler(this);
        getProductDetail();
        netimage = (NetworkImageView) findViewById(R.id.netimage);

        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();

        final String url = drawitem.getProductImageUrl();
        mImageLoader.get(url, ImageLoader.getImageListener(netimage, R.drawable.menu_ham, R.drawable.menu_ham));
        netimage.setImageUrl(url, mImageLoader);

        BottomToolbarActivity b = (BottomToolbarActivity) findViewById(R.id.bottom_toolbar);
        b.initBottomToolbar();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        list.add("16");
        list.add("17");
        list.add("18");
        list.add("19");
        list.add("20");

        list1.add("LARGE");
        list1.add("MEDIUM");
        list1.add("SMALL");
        background = (NetworkImageView) findViewById(R.id.img_Back);

        if (extras != null) {
            checkflag = extras.getBoolean("checkback", false);
            drawitem = (NewMenuDrawer) extras.getSerializable("draweritem1");


        }

        mActionbar = getSupportActionBar();
        mActionbar.hide();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.title_textf).setOnClickListener(first_radio_listener);


        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                Intent inten = new Intent(NewModifierActivity.this, MenuActivity.class);
                inten.putExtras(extras);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(inten);
                NewModifierActivity.this.finish();
            }
        });
        TextView T1 = (TextView) findViewById(R.id.todayDate);
        TextView T2 = (TextView) findViewById(R.id.expenseType1);

        final TextView txtCount = (TextView) findViewById(R.id.txtCount);
        TextView buttonInc = (TextView) findViewById(R.id.buttonInc);
        TextView buttonDec = (TextView) findViewById(R.id.buttonDec);

        buttonInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;
                if (count < 20) {
                    txtCount.setText(String.valueOf(count));
                    String selected_val = String.valueOf(count);
                    drawitem.setQuantity(Integer.valueOf(selected_val));
                    if(StringUtilities.isValidString(drawitem.getProductName())) {
                        FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(productID), drawitem.getProductName(), FBEventSettings.ADD_BOOST);
                    }
                } else {
                    alertlimit();
                }
            }
        });

        buttonDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if (count > 0) {
                    txtCount.setText(String.valueOf(count));
                    String selected_val = String.valueOf(count);
                    drawitem.setQuantity(Long.valueOf(selected_val));
                    if(StringUtilities.isValidString(drawitem.getProductName())) {
                        FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(productID), drawitem.getProductName(), FBEventSettings.REMOVE_BOOST);
                    }
                }
            }
        });

        ImageLoader imageLoader = CustomVolleyRequestQueue.getInstance(this).getImageLoader();


        Button continu = (Button) findViewById(R.id.addorder);
        adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
        adapter2.setDropDownViewResource(R.layout.spinner_item);
        adapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item, list1);
        adapter3.setDropDownViewResource(R.layout.spinner_item);


        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewModifierActivity.this, OrderConfirmActivity.class);
                Bundle extras = new Bundle();
                OrderProductList.sharedInstance().orderProductList.add(drawitem);
                OrderProductList.sharedInstance().currentAdded = drawitem;
                intent.putExtras(extras);
                startActivityForResult(intent, 2);

            }
        });


        mActionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF000000")));
        mActionbar.setElevation(0);

        T1.setText(drawitem.getProductLongDescription());
        T2.setText(drawitem.getProductName());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FBUtils.setUpNavigationDrawer(NewModifierActivity.this);
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(this);

    }


    @Override
    public void onStart() {
        super.onStart();
        setActionBar();
        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        count = 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        MenuItem itemmo = menu.findItem(R.id.menu_edit1);
        MenuItemCompat.setActionView(itemmo, R.layout.customedit);
        RelativeLayout offer = (RelativeLayout) MenuItemCompat.getActionView(itemmo);
        ImageView backclick = (ImageView) offer.findViewById(R.id.imageView1);
        backclick.setOnClickListener(first_radio_listener);
        addcart = (TextView) offer.findViewById(R.id.title_textf);
        addcart.setOnClickListener(first_radio_listener);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else if (i == R.id.menu_edit1) {
            Intent intent = new Intent(this, OrderConfirmActivity.class);
            Bundle extras = new Bundle();
            OrderProductList.sharedInstance().orderProductList.add(drawitem);
            OrderProductList.sharedInstance().currentAdded = drawitem;
            intent.putExtras(extras);
            startActivityForResult(intent, 2);
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("NewApi")
    protected void setActionBar() {

        //	setActionBarTitle();
        mActionbar.setDisplayShowHomeEnabled(false);
        mActionbar.setDisplayShowTitleEnabled(false);
        mActionbar.setHomeButtonEnabled(false);
        mActionbar.setDisplayHomeAsUpEnabled(false);
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }


    public void orderscreen(View v) {

        Intent intent = new Intent(this, OrderConfirmActivity.class);
        Bundle extras = new Bundle();
        OrderProductList.sharedInstance().orderProductList.add(drawitem);
        OrderProductList.sharedInstance().currentAdded = drawitem;
        intent.putExtras(extras);
        startActivityForResult(intent, 2);
    }

    public void getProductDetail() {
        p.show();
        final JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getMenuProduct(object, FBPreferences.sharedInstance(NewModifierActivity.this).getStoreCode(), String.valueOf(categoryID), String.valueOf(subCategoryID), String.valueOf(productID), new FBUserService.FBMenuProductCallback() {

            public void onMenuProductCallback(JSONObject response, Exception error) {
                try {
                    if (response != null) {
                        initMenuDrawer(response);

                    } else {
                    }
                } catch (Exception e) {

                }
            }

        });
    }


    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void alertlimit() {

        AlertDialog alertDialog = new AlertDialog.Builder(NewModifierActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("You have cross maximum limit");
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.show();


    }


    public void initMenuDrawer(JSONObject json) {
        try {

            JSONObject jsonArray = json.getJSONObject("productDetails");
            MenuProductDetail getStoresObj = new MenuProductDetail();
            getStoresObj.initProductDetail(jsonArray);
            mcatList.add(getStoresObj);
            p.hide();
            TextView expenseType2 = (TextView) findViewById(R.id.expenseType2);
            drawitem.setPrice((double) mcatList.get(0).getProductPrice());
            expenseType2.setText("$" + " " + String.valueOf(mcatList.get(0).getProductPrice()));
            expenseType2.setVisibility(View.VISIBLE);

        } catch (Exception e) {
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

}