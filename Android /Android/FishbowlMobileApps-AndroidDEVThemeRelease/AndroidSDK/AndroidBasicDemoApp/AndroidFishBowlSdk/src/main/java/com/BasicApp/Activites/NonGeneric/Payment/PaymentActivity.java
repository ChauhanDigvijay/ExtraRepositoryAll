package com.BasicApp.Activites.NonGeneric.Payment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.BasicApp.Activites.NonGeneric.Home.DashboardActivity;
import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.MenuActivity;
import com.BasicApp.BusinessLogic.Models.MenuDrawerItem;
import com.BasicApp.BusinessLogic.Models.OrderConfirmDrawItem;
import com.BasicApp.BusinessLogic.Models.OrderItem;
import com.BasicApp.BusinessLogic.Models.OrderProductList;
import com.BasicApp.BusinessLogic.Models.UserItem;
import com.BasicApp.Utils.FBUtils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;


public class PaymentActivity extends ActionBarActivity implements View.OnClickListener {
    public static List<OrderConfirmDrawItem> dataList;
    public static Boolean historyflag = false;
    TextView payment;
    ActionBar mActionbar;
    List<MenuDrawerItem> listss;
    MenuDrawerItem drawitem = null;
    OrderItem order = null;
    String storelocation;
    EditText new_type, new_address, new_city, new_state, new_zipcode;
    EditText rname, remail, rpass, rphone;
    CheckBox chkrem;
    RadioButton button1, button2;
    EditText button;
    Boolean checknewaddress = false;
    UserItem user;
    OnClickListener first_radio_listener = new OnClickListener() {
        public void onClick(View v) {
            //Your Implementaions...
            int i = v.getId();
            if (i == R.id.radio_address) {
                checknewaddress = false;
                button2.setChecked(false);

            } else if (i == R.id.radio_new_address) {
                checknewaddress = true;
                button1.setChecked(false);

            } else if (i == R.id.chkremember) {
                if (((CheckBox) v).isChecked()) {
                    user.setConfirmed(true);
                }

            } else if (i == R.id.imageView1) {//Intent inta = new Intent(PaymentActivity.this, MenuActivity.class);
                //startActivity(inta);;
                finish();

            } else {
            }
        }
    };
    OnClickListener first_radio_listener2 = new OnClickListener() {
        public void onClick(View v) {


            int i = v.getId();
            if (i == R.id.imageView1) {
                Intent inta = new Intent(PaymentActivity.this, MenuActivity.class);
                startActivity(inta);

            } else if (i == R.id.title_textf) {
                signup1();
                ;
			/*


			case R.id.title_textf:
				Intent intent = new Intent(PaymentActivity.this,OrderConfirmActivity.class);
				Bundle extras = new Bundle();
				{
					extras.putSerializable("draweritem1", (Serializable)listss);
					extras.putSerializable("order", order);
					extras.putSerializable("historyflag", true);
					extras.putSerializable("storelocation", storelocation);
				}
				intent.putExtras(extras);
				startActivityForResult(intent, 2);
				break;

			default:
				break;
			*/
            }
        }
    };
    private Toolbar toolbar;
    private ImageLoader mImageLoader;
    private NetworkImageView background;
    private TextView textview_user_select_card_exp;


    //  public void signup(View v)
//    {
//        if(checknewaddress)
//        {
//            FishbowlTemplate1App fishTApp=FishbowlTemplate1App.getInstance();
//            UserItem useritem=	fishTApp.getLoggedInUser();
//            String newtype =new_type.getText().toString();
//            String newaddress =new_address.getText().toString();
//            String newcity=new_city.getText().toString();
//            String newstate=new_state.getText().toString();
//            String newzipcode=new_zipcode.getText().toString();
//
//            if((!StringUtilities.isValidString(newaddress))||(!StringUtilities.isValidString(newzipcode)))
//            {
//
//                if(!StringUtilities.isValidString(newtype))
//                {
//                    new_type.setError("Type is required!");
//                }
//                if(!StringUtilities.isValidEmail(newaddress))
//                {
//                    new_address.setError("Address is required!");
//                }
//                if(!StringUtilities.isValidPhoneNumber(newcity))
//                {
//                    new_city.setError("City is required!");
//                }
//                if(!StringUtilities.isValidString(newstate))
//                {
//                    new_state.setError("State is required!");
//                }
//                if(!StringUtilities.isValidString(newzipcode))
//                {
//                    new_zipcode.setError("Zipcode is required!");
//                }
//            }
//            else
//            {
//                UserAddressItem useraddress=new UserAddressItem();
//
//                //digvijay Chauhan modify it to get First name
//                if(StringUtilities.isValidString(newaddress)){
//
//                    useraddress.setUserID(useritem.getUserID());;
//                    useraddress.setUseraddressType(newtype);
//                    useraddress.setUserStreet(newaddress);
//                    useraddress.setUsertownCity(newcity);
//                    useraddress.setUserstateRegion(newstate);
//                    useraddress.setUserpostZip(newzipcode);
//                    FB_DBUserAddress.getInstance().createUpdateLocation(useraddress);
//
//                    Confirm_old();
//                }
//            }
//        }
//        else
//        {
//            if(fishTApp.getLoggedInUser()!=null)
//            {
//                Confirm_old();
//            }else{
//                signup1();
//            }
//
//        }
//
//    }
    OnDateSetListener ondate = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            textview_user_select_card_exp.setText(getDisplayDate(monthOfYear,
                    dayOfMonth));
        }
    };
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        background = (NetworkImageView) findViewById(R.id.img_Back);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.title_textf).setOnClickListener(first_radio_listener2);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();

              /*  Intent inten = new Intent(Payment_Activity.this, Dashboard_Activity.class);
                inten.putExtras(extras);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // OrderProductList.sharedInstance().orderProductList.clear();
                startActivity(inten);;*/
                PaymentActivity.this.finish();
            }
        });
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(this);
        mActionbar = getSupportActionBar();
        mActionbar.hide();
        mActionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FAE3AF")));
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            drawitem = (MenuDrawerItem) extras.getSerializable("draweritem");
            order = (OrderItem) extras.getSerializable("order");
            listss = (List<MenuDrawerItem>) i.getSerializableExtra("draweritem1");
            historyflag = extras.getBoolean("historyflag", false);
            storelocation = extras.getString("storelocation");

        }

        setActionBar();
        Spinner spinner_user_pay_method = (Spinner) findViewById(R.id.spinner_user_pay_method);
        //  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_payment_method,R.layout.spinnertext);
        //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LinearLayout lineardelivery = (LinearLayout) findViewById(R.id.linear_delivery);


        TextView text_user_info = (TextView) findViewById(R.id.text_user_info_desc_heading);
        TextView user_info = (TextView) findViewById(R.id.text_signin_user_info);
        TextView user_sign_out_info = (TextView) findViewById(R.id.text_signin_user_sign_out);

        TextView info_pay_req = (TextView) findViewById(R.id.text_user_info_pay_req);
//


        LinearLayout newlineardelivery = (LinearLayout) findViewById(R.id.linear_deliveryn);

        rname = ((EditText) findViewById(R.id.edit_user_info_name));
        remail = ((EditText) findViewById(R.id.edit_user_info_email));
        rpass = ((EditText) findViewById(R.id.edit_user_info_password));
        rphone = ((EditText) findViewById(R.id.editbox_user_info_phone_new));
        chkrem = (CheckBox) findViewById(R.id.chkremember);
        new_type = (EditText) findViewById(R.id.edit_user_type);
        new_address = (EditText) findViewById(R.id.edit_user_address);
        new_city = (EditText) findViewById(R.id.edit_user_city);
        new_state = (EditText) findViewById(R.id.edit_user_state);
        new_zipcode = (EditText) findViewById(R.id.edit_user_zipcode);
        button = (EditText) findViewById(R.id.editbox_user_info_phone_new);
        button1 = (RadioButton) findViewById(R.id.radio_address);
        button2 = (RadioButton) findViewById(R.id.radio_new_address);
        button1.setOnClickListener(first_radio_listener);
        button2.setOnClickListener(first_radio_listener);
        chkrem.setOnClickListener(first_radio_listener);

        //spinner_user_pay_method.setAdapter(adapter);

        // Card Expiry Date
        textview_user_select_card_exp = (TextView) findViewById(R.id.textview_user_select_card_exp);

        if (order != null) {
            if (StringUtilities.isValidString(order.getOrderType())) {

                Double currPrice = 0.00;
                DecimalFormat df = new DecimalFormat("0.00##");

                if (order.getOrderType().equalsIgnoreCase("DELIVERY")) {
                    lineardelivery.setVisibility(View.VISIBLE);
                } else {
                    lineardelivery.setVisibility(View.GONE);
                }

//                for(MenuDrawerItem eachcurrentAttachment:order.getItemlist())
//                {
//                    {
//                        currPrice=currPrice+eachcurrentAttachment.getExt();
//                        info_pay_req.setText("$" +df.format(currPrice)+" "+"deposit required");
//                    }
//
//                }
            }
        }


//        if(fishTApp.getLoggedInUser()!=null)
//        {
//
//            UserItem useritem=	fishTApp.getLoggedInUser();
//
//            rname.setText(useritem.getFullname());
//            remail.setText(useritem.getEmail());
//            rpass.setText(useritem.getPassword());
//            button.setText(useritem.getMobile());
//            user_info.setVisibility(View.GONE);
//            user_info.setVisibility(View.GONE);
//            text_user_info.setVisibility(View.GONE);
//            List<UserAddressItem> location= FB_DBUserAddress.getInstance().getAllLocation();
//
//            if(location!=null&&location.size()>0)
//            {
//
//                button1.setText(" "+location.get(0).getUserStreet()+" "+location.get(0).getUsertownCity()+" "+location.get(0).getUserstateRegion());
//
//            }
//        }
//
//        else
//        {
//            button.setVisibility(View.GONE);
//            button1.setVisibility(View.GONE);
//
//            user=new UserItem();
//        }
//
//        textview_user_select_card_exp.setText(getDisplayDate(getCurrentMonth(),getCurrentYear()));
//
        FBUtils.setUpNavigationDrawer(PaymentActivity.this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


    }

    private String getDisplayDate(int month, int year) {
        String displayDate = "";
        displayDate += String.valueOf(getCurrentMonth()).concat(" / ");
        displayDate += String.valueOf(getCurrentYear());
        return displayDate;
    }

    public void openCardExpSelection(View v) {
        openCustomDialog().show();
    }

    private int getCurrentYear() {
        Calendar calender = Calendar.getInstance();
        return calender.get(Calendar.YEAR);

    }

    private int getCurrentMonth() {
        Calendar calender = Calendar.getInstance();
        return calender.get(Calendar.MONTH);

    }

    public DatePickerDialog openCustomDialog() {
        Calendar calender = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, ondate, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), 1);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass()
                    .getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField
                            .get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField
                            .getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }

    public void openCardExpSelection1(View v) {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    @SuppressLint("NewApi")
    protected void setActionBar() {
        mActionbar.setDisplayShowHomeEnabled(false);
        mActionbar.setDisplayShowTitleEnabled(false);
        mActionbar.setHomeButtonEnabled(false);
        mActionbar.setDisplayHomeAsUpEnabled(false);
    }

    public void setActionBarTitle() {
        /*	int dj = Integer.parseInt(expenseClaimItem.getTimeTask().getExtStatus().getStatusID().toString());*/


        //	mActionbar.setTitle("PAYMENT");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_payment, menu);
        MenuItem itemmo = menu.findItem(R.id.menu_edit1);

        MenuItemCompat.setActionView(itemmo, R.layout.custompaymenu);
        RelativeLayout offer = (RelativeLayout) MenuItemCompat.getActionView(itemmo);
        ImageView backclick = (ImageView) offer.findViewById(R.id.imageView1);

        backclick.setOnClickListener(first_radio_listener);


        payment = (TextView) offer.findViewById(R.id.title_textf);

        payment.setOnClickListener(first_radio_listener2);

        //	cartiv.setOnClickListener(first_radio_listener);
        return super.onCreateOptionsMenu(menu);
    }


    public void signup1() {
        user = new UserItem();
        String name = rname.getText().toString();
        String email = remail.getText().toString();
        //String phone=rphone.getText().toString();
        //	String pass=rpass.getText().toString();


        if ((!StringUtilities.isValidEmail(email))) {

            if (!StringUtilities.isValidString(name)) {
                rname.setError("Name is required!");
            }
            if (!StringUtilities.isValidEmail(email)) {
                remail.setError("Email is required!");
            }
//			if(!StringUtilities.isValidPhoneNumber(phone))
//			{
//				rphone.setError("Phone is required!");
//			}
//			if(!StringUtilities.isValidString(pass))
//			{
//				rpass.setError("Password is required!");
            //	}
        } else {


            //digvijay Chauhan modify it to get First name
            if (StringUtilities.isValidString(name)) {

                user.setFullname(name);
                try {
                    if (name.indexOf(" ") > 0) {
                        user.setFirstname(name.split(" ")[0]);
                        if (name.split(" ").length > 1) {
                            user.setLastname(name.split(" ")[1]);
                        }

                    } else {
                        if (StringUtilities.isValidString(user.getFullname())) {
                            user.setFirstname(user.getFullname());
                            user.setLastname("");
                        }
                    }
                } catch (Exception e) {
                    user.setFullname(name);
                    e.printStackTrace();

                }


                user.setEmail(email);
                //user.setMobile(phone);
                //user.setPassword(pass);
                user.setConfirmed(true);
                //   FB_DBUser.getInstance().createUpdateUser(user);

                Confirm_old();

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();

            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));
            //   background.setImageUrl(url, mImageLoader);

        }
        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();

            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));
            //  background.setImageUrl(url, mImageLoader);

        }
    }

    public void Confirm_old()

    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Payment");

        // Setting Dialog Message
        alertDialog.setMessage("Press yes for payment");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.logo);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Bundle extras = new Bundle();
                Intent inten = new Intent(PaymentActivity.this, DashboardActivity.class);
                inten.putExtras(extras);
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                OrderProductList.sharedInstance().orderProductList.clear();
                startActivity(inten);
                PaymentActivity.this.finish();


                // Write your code here to invoke YES event
                //   Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
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

    private class DatePickerFragment extends DialogFragment {
        OnDateSetListener ondateSet;
        private int year, month, day;

        public DatePickerFragment() {
        }

        public void setCallBack(OnDateSetListener ondate) {
            ondateSet = ondate;
        }

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            year = args.getInt("year");
            month = args.getInt("month");
            day = args.getInt("day");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(), ondateSet, year, month,
                    day);
        }

    }


}

