package com.hbh.honeybaked.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.internal.ServerProtocol;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.listener.AdapterListener;
import com.hbh.honeybaked.supportingfiles.Utility;
import java.util.ArrayList;
import java.util.HashMap;

public class StoreAdapter extends BaseAdapter {
    private AdapterListener adapterListener;
    private Context context;
    private LayoutInflater inflater = null;
    private boolean isSelectStore;
    ArrayList<HashMap<String, String>> storeList = new ArrayList();

    class C17181 implements OnClickListener {
        C17181() {
        }

        public void onClick(View v) {
            v.setPressed(true);
            StoreAdapter.this.go(((Integer) v.getTag()).intValue());
        }
    }

    class C17192 implements OnClickListener {
        C17192() {
        }

        public void onClick(View v) {
            v.setPressed(true);
            Utility.hideSoftKeyboard((Activity) StoreAdapter.this.context);
            StoreAdapter.this.go(((Integer) v.getTag()).intValue());
        }
    }

    public class ViewHolder {
        LinearLayout layout;
        TextView store_address_text;
        TextView store_name_text;
        TextView store_phone_text;
        TextView store_select;
    }

    public StoreAdapter(Context context, ArrayList<HashMap<String, String>> storeList, boolean isSelectStore, AdapterListener adapterListener) {
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.context = context;
        this.storeList = storeList;
        this.isSelectStore = isSelectStore;
        this.adapterListener = adapterListener;
    }

    public int getCount() {
        return this.storeList.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View vi = convertView;
        if (convertView == null) {
            holder = new ViewHolder();
            vi = this.inflater.inflate(R.layout.store_chlid_item, null);
            holder.layout = (LinearLayout) vi.findViewById(R.id.recipes_layer);
            holder.store_name_text = (TextView) vi.findViewById(R.id.store_name_text);
            holder.store_address_text = (TextView) vi.findViewById(R.id.store_address_text);
            holder.store_phone_text = (TextView) vi.findViewById(R.id.store_phone_text);
            holder.store_select = (TextView) vi.findViewById(R.id.store_select);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        HashMap<String, String> map = null;
        if (this.storeList.size() > position) {
            map = (HashMap) this.storeList.get(position);
        }
        if (map != null) {
            holder.store_name_text.setText(((String) map.get("city")).trim());
            holder.store_address_text.setText((Utility.isEmptyString((String) map.get("store_address_string")) ? "" : ((String) map.get("store_address_string")).trim() + ", ") + (Utility.isEmptyString((String) map.get("city")) ? "" : ((String) map.get("city")).trim() + ", ") + (Utility.isEmptyString((String) map.get(ServerProtocol.DIALOG_PARAM_STATE)) ? "" : ((String) map.get(ServerProtocol.DIALOG_PARAM_STATE)).trim() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR) + (Utility.isEmptyString((String) map.get("zipcode")) ? "" : ((String) map.get("zipcode")).trim()));
            holder.store_phone_text.setText((CharSequence) map.get("phone"));
        }
        holder.store_select.setText(this.isSelectStore ? "SELECT" : "Details");
        holder.store_select.setTag(Integer.valueOf(position));
        holder.layout.setTag(Integer.valueOf(position));
        holder.store_select.setOnClickListener(new C17181());
        holder.layout.setOnClickListener(new C17192());
        return vi;
    }

    private void go(int position) {
        this.adapterListener.performAdapterAction(AppConstants.STORE_SELECT, this.storeList.get(position));
    }
}
