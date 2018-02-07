package com.hbh.honeybaked.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.helper.PreferenceHelper;
import com.hbh.honeybaked.listener.AdapterListener;
import com.hbh.honeybaked.supportingfiles.Utility;

public class CustomerServiceAdapter extends BaseAdapter {
    AdapterListener adapter_listener = null;
    Context context = null;
    String[] cs_list_name;
    PreferenceHelper hbha_pref_helper;
    ViewHolder holder = null;
    LayoutInflater l_Inflater = null;
    StoreHoursAdapter storeHoursAdapter;
    String[] time_arr;

    class C17081 implements OnClickListener {
        C17081() {
        }

        public void onClick(View v) {
            CustomerServiceAdapter.this.adapter_listener.performAdapterAction(AppConstants.CONTACT_US_CHANGE_STORE, Integer.valueOf(0));
        }
    }

    class C17092 implements OnClickListener {
        C17092() {
        }

        public void onClick(View v) {
            CustomerServiceAdapter.this.adapter_listener.performAdapterAction(AppConstants.CONTACTUS_CALL, CustomerServiceAdapter.this.holder.ph_no.getText().toString());
        }
    }

    private static class ViewHolder {
        TextView change_store;
        ListView con_store_hrs_lv;
        TextView cstore_ex_hrs_head_tv;
        TextView cstore_ex_hrs_tv;
        TextView cus_ser_tv;
        LinearLayout custom_lay;
        LinearLayout hour_lay;
        LinearLayout list_part1;
        LinearLayout list_part2;
        TextView list_title;
        TextView ph_no;
        TextView place_nm;
        TextView shop_nm;

        private ViewHolder() {
        }
    }

    public CustomerServiceAdapter(Context context, String[] cs_list_name, AdapterListener adapter_listener) {
        this.context = context;
        this.cs_list_name = cs_list_name;
        this.l_Inflater = LayoutInflater.from(context);
        this.hbha_pref_helper = new PreferenceHelper(context);
        this.adapter_listener = adapter_listener;
    }

    public int getCount() {
        return this.cs_list_name.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            this.holder = new ViewHolder();
            convertView = this.l_Inflater.inflate(R.layout.custom_service_layout, null);
            this.holder.list_part1 = (LinearLayout) convertView.findViewById(R.id.list_part1);
            this.holder.list_part2 = (LinearLayout) convertView.findViewById(R.id.list_part2);
            this.holder.custom_lay = (LinearLayout) convertView.findViewById(R.id.custom_lay);
            this.holder.hour_lay = (LinearLayout) convertView.findViewById(R.id.hour_lay);
            this.holder.list_title = (TextView) convertView.findViewById(R.id.list_title);
            this.holder.shop_nm = (TextView) convertView.findViewById(R.id.shop_nm);
            this.holder.place_nm = (TextView) convertView.findViewById(R.id.place_nm);
            this.holder.ph_no = (TextView) convertView.findViewById(R.id.ph_no);
            this.holder.change_store = (TextView) convertView.findViewById(R.id.change_store);
            this.holder.cus_ser_tv = (TextView) convertView.findViewById(R.id.cus_ser_tv);
            this.holder.con_store_hrs_lv = (ListView) convertView.findViewById(R.id.con_store_hrs_lv);
            this.holder.cstore_ex_hrs_head_tv = (TextView) convertView.findViewById(R.id.cstore_ex_hrs_head_tv);
            this.holder.cstore_ex_hrs_tv = (TextView) convertView.findViewById(R.id.cstore_ex_hrs_tv);
            convertView.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            this.holder.list_part1.setVisibility(View.VISIBLE);
            this.holder.list_part2.setVisibility(View.VISIBLE);
            SpannableString content = new SpannableString("Change Store>");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            this.holder.change_store.setText(content);
            String add = this.hbha_pref_helper.getStringValue("store");
            if (add.length() > 0) {
                this.holder.list_title.setText(this.cs_list_name[position]);
                String[] add_ = add.split("######", -1);
                if (!Utility.isEmptyString(add_[0])) {
                    String[] nameSplit = add_[0].split(",");
                    this.holder.shop_nm.setText(nameSplit[0].trim());
                    this.holder.place_nm.setText(add_[0].substring(nameSplit[0].length() + 1, add_[0].length()));
                }
                if (Utility.isEmptyString(add_[1].trim())) {
                    this.holder.ph_no.setVisibility(View.VISIBLE);
                } else {
                    this.holder.ph_no.setText(add_[1].trim());
                }
                if (Utility.isEmptyString(add_[2]) || Utility.isEmptyString(add_[2].replaceAll("\\$", "").trim()) || add_[2] == null || add_[2].length() <= 0 || add_[2].equalsIgnoreCase("")) {
                    this.holder.hour_lay.setVisibility(View.VISIBLE);
                } else {
                    this.holder.hour_lay.setVisibility(View.VISIBLE);
                    this.time_arr = add_[2].trim().split("\\$\\$\\$\\$\\$", -1);
                    this.storeHoursAdapter = new StoreHoursAdapter(this.context, this.time_arr, this.adapter_listener);
                    this.holder.con_store_hrs_lv.setAdapter(this.storeHoursAdapter);
                    if (this.hbha_pref_helper.getIntValue("store_hours_listview_height") != 0) {
                        setListViewParams(this.holder.con_store_hrs_lv, this.hbha_pref_helper.getIntValue("store_hours_listview_height"));
                    }
                }
                if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue("store_ex_hrs_head"))) {
                    this.holder.cstore_ex_hrs_head_tv.setVisibility(View.VISIBLE);
                } else {
                    this.holder.cstore_ex_hrs_head_tv.setVisibility(View.VISIBLE);
                    this.holder.cstore_ex_hrs_head_tv.setText(this.hbha_pref_helper.getStringValue("store_ex_hrs_head"));
                }
                if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue("store_ex_hrs"))) {
                    this.holder.cstore_ex_hrs_tv.setVisibility(View.VISIBLE);
                } else {
                    this.holder.cstore_ex_hrs_tv.setVisibility(View.VISIBLE);
                    this.holder.cstore_ex_hrs_tv.setText(this.hbha_pref_helper.getStringValue("store_ex_hrs"));
                }
            } else {
                this.holder.shop_nm.setText("Local Store Not selected");
                this.holder.place_nm.setVisibility(View.VISIBLE);
                this.holder.ph_no.setVisibility(View.VISIBLE);
                this.holder.hour_lay.setVisibility(View.VISIBLE);
                this.holder.change_store.setVisibility(View.VISIBLE);
                this.holder.list_title.setVisibility(View.VISIBLE);
            }
        } else {
            this.holder.list_part1.setVisibility(View.VISIBLE);
            this.holder.list_part2.setVisibility(View.VISIBLE);
            this.holder.cus_ser_tv.setText(this.cs_list_name[position]);
        }
        this.holder.change_store.setOnClickListener(new C17081());
        this.holder.ph_no.setOnClickListener(new C17092());
        return convertView;
    }

    private void goToCall() {
        if (((TelephonyManager) this.context.getSystemService("phone")).getSimState() != 1) {
            try {
                Intent in = new Intent("android.intent.action.DIAL");
                in.setData(Uri.parse("tel:" + this.holder.ph_no.getText().toString()));
                this.context.startActivity(in);
                return;
            } catch (ActivityNotFoundException e) {
                Utility.showToast((Activity) this.context, "No Sim Available");
                return;
            }
        }
        Utility.showToast((Activity) this.context, "No Sim Available");
    }

    private void setListViewParams(ListView store_det_hrs_lv, int size) {
        LayoutParams params = (LayoutParams) store_det_hrs_lv.getLayoutParams();
        if (Utility.isEmpty(this.time_arr)) {
            params.height = 0;
        } else {
            params.height = this.time_arr.length * size;
        }
        store_det_hrs_lv.setLayoutParams(params);
        store_det_hrs_lv.invalidate();
    }

    public void UpdateStoreTimeList() {
        if (this.storeHoursAdapter != null) {
            this.storeHoursAdapter.notifyDataSetChanged();
        }
    }
}
