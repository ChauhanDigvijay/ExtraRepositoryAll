package com.BasicApp.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.BasicApp.Activites.Generic.BottomToolbarActivity;
import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.MenuActivity;
import com.BasicApp.Activites.NonGeneric.Order.OrderConfirmActivity;
import com.BasicApp.BusinessLogic.Models.Category;
import com.BasicApp.BusinessLogic.Models.MenuDrawerItem;
import com.BasicApp.BusinessLogic.Models.OrderConfirmDrawItem;
import com.BasicApp.BusinessLogic.Models.OrderItem;
import com.BasicApp.BusinessLogic.Models.Product;
import com.BasicApp.BusinessLogic.Models.ProductList;
import com.BasicApp.Utils.FBUtils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import java.util.ArrayList;
import java.util.List;

import static com.BasicApp.Activites.NonGeneric.Menus.ProductDetail.NewModifierActivity.count;


//import static com.fishbowl.basicmodule.Services.FBTokenService.count;

/**
 * *
 * Created by Digvijay Chauhan on 25/12/15.
 */


@SuppressLint("NewApi") public class ModifierActivity extends ActionBarActivity {
    private ImageLoader mImageLoader;
    private NetworkImageView background;
    ImageView cartiv;
    public static Boolean checkflag = false;
    TextView addcart, cart_textview;
    EditText semail;
    EditText spass;
    EditText mEdit;
    ViewGroup currentView = null;
    public static List<MenuDrawerItem> dataList1;
    ListView lv;
    ActionBar mActionbar;

    public static Boolean checkback = false;
    public static Boolean historyflag = false;
    private DrawerLayout drawerLayout;
    public static List<OrderConfirmDrawItem> dataList;
    List<MenuDrawerItem> listss;
    MenuDrawerItem drawitem = null;
    OrderItem order = null;
    String storelocation;
    private RadioGroup radioGroup;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter2;
    ArrayList<String> list1 = new ArrayList<String>();
    ArrayAdapter<String> adapter3;
    private Toolbar toolbar;
    int proposition, catposition;
    public MyAdapter mAdapter;
    public ViewPager mPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_bistro);
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
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        proposition = extras.getInt("ProductPosition");
        catposition = extras.getInt("CategoryPosition");

        if (extras != null) {
           // drawitem = OrderProductList.sharedInstance().currentAdded;
            //	dataList1 = (List<MenuDrawerItem>) i.getSerializableExtra("draweritem1");
            checkflag = extras.getBoolean("checkback", false);


        }


        mActionbar = getSupportActionBar();
        mActionbar.hide();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.title_textf).setOnClickListener(first_radio_listener);


        toolbar.findViewById(R.id.backbutton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                Intent inten = new Intent(ModifierActivity.this, MenuActivity.class);
                inten.putExtras(extras);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //OrderProductList.sharedInstance().orderProductList.clear();
                startActivity(inten);
                ModifierActivity.this.finish();
            }
        });
        //	TextView title_textf= (TextView) findViewById(R.id.title_textf);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                }

            }
        });


        TextView T1 = (TextView) findViewById(R.id.todayDate);
        TextView T2 = (TextView) findViewById(R.id.expenseType1);

        final TextView txtCount =(TextView) findViewById(R.id.txtCount);
        TextView buttonInc= (TextView ) findViewById(R.id.buttonInc);
        TextView buttonDec= (TextView ) findViewById(R.id.buttonDec);

        buttonInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;
                if(count<20) {
                    txtCount.setText(String.valueOf(count));
                    String selected_val = String.valueOf(count);

                    drawitem.setQuantity(Integer.valueOf(selected_val));
                }
                else
                {
                    alertlimit();
                }
            }
        });

        buttonDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if(count>0) {
                    txtCount.setText(String.valueOf(count));
                    String selected_val = String.valueOf(count);

                    drawitem.setQuantity(Integer.valueOf(selected_val));
                }

            }
        });
    //   TextView T3 = (TextView) findViewById(R.id.expenseType2);

     //   TextView categoryName = (TextView) findViewById(R.id.categoryName);

     //   categoryName.setText(ProductList.sharedInstance().categories.get(catposition).category);
      //  NetworkImageView nimage = (NetworkImageView) findViewById(R.id.menu_img);

        mAdapter = new MyAdapter(this);
        mPager = (ViewPager) findViewById(R.id.menu_img);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(catposition);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
             //   reloadadapter(position);
            }


            public void onPageSelected(int position) {
                // Check if this is the page you want.

            }
        });

        ImageLoader imageLoader = CustomVolleyRequestQueue.getInstance(this).getImageLoader();


        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        Button continu = (Button) findViewById(R.id.addorder);
        adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
        spinner2.setAdapter(adapter2);
        adapter2.setDropDownViewResource(R.layout.spinner_item);
        adapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item, list1);
        spinner.setAdapter(adapter3);
        adapter3.setDropDownViewResource(R.layout.spinner_item);

        spinner.setSelection(getIndex(spinner, String.valueOf(drawitem.gradient)));
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selected_val = spinner.getSelectedItem().toString();

                drawitem.setGradient(selected_val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifierActivity.this, OrderConfirmActivity.class);
                Bundle extras = new Bundle();
            //    OrderProductList.sharedInstance().orderProductList.add(drawitem);
              //  OrderProductList.sharedInstance().currentAdded = drawitem;
                intent.putExtras(extras);
                startActivityForResult(intent, 2);

            }
        });

        spinner2.setSelection(getIndex(spinner2, String.valueOf(drawitem.quantity)));
        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selected_val = spinner2.getSelectedItem().toString();

                drawitem.setQuantity(Integer.valueOf(selected_val));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        mActionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF000000")));
        mActionbar.setElevation(0);

        T1.setText(drawitem.getItemdesc());
        T2.setText(drawitem.getItemName());
     //   T3.setText(drawitem.getItemId());

   //     imageLoader.get(drawitem.getImageurl(), ImageLoader.getImageListener(nimage, R.drawable.headerone, android.R.drawable.ic_dialog_alert));

     //
        //
        //
        // nimage.setImageUrl(drawitem.getImageurl(), imageLoader);

        FBUtils.setUpNavigationDrawer(ModifierActivity.this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


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

    @SuppressLint("NewApi")
    protected void setActionBar() {

        //	setActionBarTitle();
        mActionbar.setDisplayShowHomeEnabled(false);
        mActionbar.setDisplayShowTitleEnabled(false);
        mActionbar.setHomeButtonEnabled(false);
        mActionbar.setDisplayHomeAsUpEnabled(false);
    }


    OnClickListener first_radio_listener = new OnClickListener() {
        public void onClick(View v) {

            //Your Implementaions...
            int i = v.getId();
            if (i == R.id.title_textf) {
                Intent intent = new Intent(ModifierActivity.this, OrderConfirmActivity.class);
                Bundle extras = new Bundle();
                if (checkflag) {
                //    OrderProductList.sharedInstance().addIteme(drawitem);
                  //  OrderProductList.sharedInstance().currentAdded = drawitem;
                }
                intent.putExtras(extras);
                startActivityForResult(intent, 2);
            } else if (i == R.id.imageButton) {

                Intent menucart = new Intent(ModifierActivity.this, OrderConfirmActivity.class);
                startActivity(menucart);

            } else if (i == R.id.imageView1) {
                finish();

            } else {
            }
        }
    };

    public void setActionBarTitle() {


    }

    public  void alertlimit() {


        AlertDialog alertDialog = new AlertDialog.Builder(ModifierActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Alert");

        // Setting Dialog Message
        alertDialog.setMessage("You have cross maximum limit");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_launcher);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        // Showing Alert Message
        alertDialog.show();







                  /*  Intent i=new Intent(UserProfile_Activity.this,SignInActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                            startActivity(i);*/

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
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }


    @Override
    protected void onResume() {

        super.onResume();
        count=0;
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
       //     OrderProductList.sharedInstance().orderProductList.add(drawitem);
       //     OrderProductList.sharedInstance().currentAdded = drawitem;
            intent.putExtras(extras);
            startActivityForResult(intent, 2);
        }
        return super.onOptionsItemSelected(item);
    }


    public void orderscreen(View v) {

        Intent intent = new Intent(this, OrderConfirmActivity.class);
        Bundle extras = new Bundle();


     //   OrderProductList.sharedInstance().orderProductList.add(drawitem);
     //   OrderProductList.sharedInstance().currentAdded = drawitem;

        intent.putExtras(extras);
        startActivityForResult(intent, 2);

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

    public class MyAdapter extends PagerAdapter {
        private ImageLoader mImageLoader;

        LayoutInflater inflater;
        private int postion;
        Activity ctx;
        public MyAdapter(Activity ctx){
            this.ctx=ctx;
            inflater=LayoutInflater.from(ctx);
            mImageLoader = CustomVolleyRequestQueue.getInstance(ctx)
                    .getImageLoader();
            //instantiate your views list
        }
        public  void release(){


        }

        @Override
        public int getCount() {

            if(ProductList.sharedInstance().categories!=null){
                if(ProductList.sharedInstance().categories.size()!=0)
                {
                    return ProductList.sharedInstance().categories.get(catposition).products.size();
                }}

            return 0;
        }

        public Object instantiateItem(ViewGroup container, int position) {


            Category item=null;
            //   Log.e("MyViewPagerAdapter","instantiateItem for "+position);

            this.postion=catposition;

            item=ProductList.sharedInstance().categories.get(catposition);

            //  Log.e("MyViewPagerAdapter","instantiateItem need to create the View");

            currentView= (ViewGroup) inflater.inflate(R.layout.image_layout,container,false);
            //  Log.i("MyViewPagerAdapter", "instantiateItem() [position: " + position + "]" + " childCount:" + container.getChildCount());
            setView(currentView,item,catposition);


            ((ViewPager) container).addView(currentView);

            //        reloadadapter(position);

            return currentView;
        }

        public void setView(ViewGroup currentView, Category item, final int position)
        {


            NetworkImageView imageView = (NetworkImageView) currentView.findViewById(R.id.imageView_viewPager);
            TextView categoryName=(TextView)currentView.findViewById(R.id.categoryName);

            if(StringUtilities.isValidString(item.imageurl)) {


                imageView.setImageUrl(item.imageurl,mImageLoader);

            }
            else
            {
                imageView.setBackgroundResource(R.drawable.happyhour);
            }
            categoryName.setText(item.category);
            // reloadadapter(item,position);
            //   reloadadapter(position);

        }



        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
            Log.i("MyViewPagerAdapter", "destroyItem() [position: " + position + "]" + " childCount:" + container.getChildCount());
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view==((View)object);
        }



}

    public void reloadadapter(final int position)


    {
        TextView T1 = (TextView) findViewById(R.id.todayDate);
        TextView T2 = (TextView) findViewById(R.id.expenseType1);
        catposition = position;

        final Product category= ProductList.sharedInstance().categories.get(catposition).products.get(position);


        T1.setText(category.idesc);
        T2.setText(category.iname);



    }
}