package com.hbh.honeybaked.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.helper.PreferenceHelper;
import com.hbh.honeybaked.listener.AdapterListener;
import com.hbh.honeybaked.module.MenuFrgmentModule;
import java.util.ArrayList;

public class MenusFragmentAdapter extends BaseAdapter {
    AdapterListener adapter_listener = null;
    Context context = null;
    PreferenceHelper hbha_pref_helper;
    ViewHolder holder = null;
    ArrayList<MenuFrgmentModule> menu_list_data = null;

    private static class ViewHolder {
        ImageView menu_img_vw;
        TextView menu_title_tv;

        private ViewHolder() {
        }
    }

    public MenusFragmentAdapter(FragmentActivity context, AdapterListener adapterListener, ArrayList<MenuFrgmentModule> menu_list_data) {
        this.context = context;
        this.menu_list_data = menu_list_data;
        this.hbha_pref_helper = new PreferenceHelper(context);
        this.adapter_listener = adapterListener;
    }

    public int getCount() {
        return this.menu_list_data.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.menu_custom_layout, null);
            this.holder = new ViewHolder();
            this.holder.menu_img_vw = (ImageView) convertView.findViewById(R.id.menu_img_vw);
            this.holder.menu_title_tv = (TextView) convertView.findViewById(R.id.menu_title_tv);
            convertView.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) convertView.getTag();
        }
        this.holder.menu_title_tv.setText(((MenuFrgmentModule) this.menu_list_data.get(position)).getPc_nm());
        this.holder.menu_img_vw.setImageResource(((MenuFrgmentModule) this.menu_list_data.get(position)).getPc_img_url());
        return convertView;
    }
}
