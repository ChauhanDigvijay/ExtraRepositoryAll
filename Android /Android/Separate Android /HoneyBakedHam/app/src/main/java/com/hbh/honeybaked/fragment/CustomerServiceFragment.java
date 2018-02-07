package com.hbh.honeybaked.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.adapter.CustomerServiceAdapter;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.supportingfiles.Utility;

public class CustomerServiceFragment extends BaseFragment {
    int EMAIL_RESPONSE_CODE = 101;
    boolean adapter = false;
    String[] cs_item_link = new String[]{"", "http://www.honeybaked.com", "http://www.nutritionix.com/honeybaked-ham/portal", "https://www.contacthoneybaked.com/?CommentType=2", "https://www.contacthoneybaked.com/?CommentType=1", "http://locator.honeybaked.com/"};
    String[] cs_list_name = new String[]{"Local Store Information ", "View Our Website", "Nutrition Site", "Store Visit Feedback", "Shipped Orders Feedback", "Store Locator"};
    LinearLayout cus_ser_layout;
    TextView cus_ser_tv;
    CustomerServiceAdapter menu_adapter = null;
    ListView menu_list;
    String ph_no = "";
    TextView version_tv;

    class C17251 implements OnItemClickListener {
        C17251() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (position == 1 || position == 2 || position == 3 || position == 4) {
                CustomerServiceFragment.this.fragmentActivityListener.performFragmentActivityAction(AppConstants.SHOPONLINE, new MenuModel(CustomerServiceFragment.this.cs_list_name[position], "", CustomerServiceFragment.this.cs_item_link[position]));
            } else if (position != 5) {
            } else {
                if (Utility.isEmptyString(CustomerServiceFragment.this.hbha_pref_helper.getStringValue("store"))) {
                    Utility.showToast(CustomerServiceFragment.this.getActivity(), "Please select your local store");
                    return;
                }
                CustomerServiceFragment.this.fragmentActivityListener.performFragmentActivityAction(AppConstants.STORE_MAIN_PAGE1, new MenuModel(CustomerServiceFragment.this.cs_list_name[position], "", CustomerServiceFragment.this.cs_item_link[position]));
            }
        }
    }

    class C17262 implements OnDismissListener {
        C17262() {
        }

        @TargetApi(23)
        public void onDismiss(DialogInterface dialogInterface) {
            CustomerServiceFragment.this.requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 1);
        }
    }

    class C17273 implements OnDismissListener {
        C17273() {
        }

        public void onDismiss(DialogInterface dialog) {
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_service, container, false);
        this.menu_list = (ListView) v.findViewById(R.id.menu_list);
        this.version_tv = (TextView) v.findViewById(R.id.version_tv);
        this.cus_ser_tv = (TextView) v.findViewById(R.id.cus_ser_tv);
        this.cus_ser_layout = (LinearLayout) v.findViewById(R.id.cus_ser_layout);
        this.cus_ser_layout.setOnClickListener(this);
        SpannableString content = new SpannableString("appsupport@hbham.com");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        this.cus_ser_tv.setText(content);
        this.menu_adapter = new CustomerServiceAdapter(getActivity(), this.cs_list_name, this);
        this.menu_list.setAdapter(this.menu_adapter);
        this.menu_list.setOnItemClickListener(new C17251());
        try {
            PackageInfo pinfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            this.version_tv.setText("Version : " + pinfo.versionName + " (" + String.valueOf(pinfo.versionCode) + ")");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return v;
    }

    public void performAdapterAction(String tagName, Object data) {
        if (tagName.equals(AppConstants.CONTACT_US_CHANGE_STORE)) {
            if (((Integer) data).intValue() != 0) {
                return;
            }
            if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue("store"))) {
                Utility.showToast(getActivity(), "Please select your local store");
                return;
            }
            MenuModel menumodel = null;
            this.hb_dbHelper.openDb();
            Cursor cursor = this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_menu_table where sub_title='My Store'", null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    menumodel = new MenuModel(cursor.getString(1), cursor.getString(3), cursor.getString(4));
                }
            }
            cursor.close();
            if (menumodel != null) {
                this.fragmentActivityListener.performFragmentActivityAction(AppConstants.STORE_MAIN_PAGE, menumodel);
            }
        } else if (tagName.equals(AppConstants.CONTACTUS_CALL)) {
            if (data != null) {
                this.ph_no = (String) data;
                callFunction();
            }
        } else if (!tagName.equals("set_store_hours_listview_height")) {
            super.performAdapterAction(tagName, data);
        } else if (data != null && this.menu_adapter != null) {
            this.menu_adapter.UpdateStoreTimeList();
            if (!this.adapter) {
                this.menu_list.invalidateViews();
                this.adapter = true;
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cus_ser_layout:
                if (this.cd.isConnectingToInternet()) {
                    sendMail(this.cus_ser_tv.getText().toString());
                    return;
                } else {
                    Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
                    return;
                }
            default:
                return;
        }
    }

    public void sendMail(String email_id) {
        startActivityForResult(Intent.createChooser(new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", email_id, null)), "Send email..."), this.EMAIL_RESPONSE_CODE);
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (requestCode != this.EMAIL_RESPONSE_CODE) {
        }
    }

    private void callFunction() {
        if (VERSION.SDK_INT < 23) {
            goToCall();
        } else if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.CALL_PHONE") == 0) {
            goToCall();
        } else if (shouldShowRequestPermissionRationale("android.permission.CALL_PHONE")) {
            Builder builder = new Builder(getActivity());
            builder.setTitle("This app needs call access");
            builder.setMessage("Go to Settings and enable the phone permission");
           // builder.setPositiveButton(17039370, null);
            builder.setOnDismissListener(new C17262());
            builder.show();
        } else {
            requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 1);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == 0) {
                    goToCall();
                    return;
                }
                Builder builder = new Builder(getActivity());
                builder.setTitle("Functionality limited");
                builder.setMessage("Since call access has not been granted, this app will not be able to connect call");
                //builder.setPositiveButton(17039370, null);
                builder.setOnDismissListener(new C17273());
                builder.show();
                return;
            default:
                return;
        }
    }

    private void goToCall() {
        if (((TelephonyManager) getActivity().getSystemService("phone")).getSimState() != 1) {
            try {
                Intent in = new Intent("android.intent.action.DIAL");
                in.setData(Uri.parse("tel:" + this.ph_no));
                startActivity(in);
                return;
            } catch (ActivityNotFoundException e) {
                Utility.showToast(getActivity(), "No Sim Available");
                return;
            }
        }
        Utility.showToast(getActivity(), "No Sim Available");
    }
}
