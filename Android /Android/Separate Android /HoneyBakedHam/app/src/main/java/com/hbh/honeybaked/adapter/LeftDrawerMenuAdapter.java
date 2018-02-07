package com.hbh.honeybaked.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbh.honeybaked.R;
import com.hbh.honeybaked.listener.AdapterListener;
import com.hbh.honeybaked.module.MenuModel;

import java.util.List;

public class LeftDrawerMenuAdapter extends BaseAdapter {
    AdapterListener adapter_listener = null;
    Context context;
    DrawerItemHolder drawerHolder = null;
    List<MenuModel> drawerItemList;
    int layoutResID;

    private static class DrawerItemHolder {
        TextView ItemName;
        LinearLayout drawer_custom_ll;
        ImageView icon;

        private DrawerItemHolder() {
        }
    }

    public LeftDrawerMenuAdapter(Context context, int layoutResourceID, List<MenuModel> listItems) {
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;
        this.adapter_listener = (AdapterListener) context;
    }

    public int getCount() {
        return this.drawerItemList.size();
    }

    public Object getItem(int position) {
        return this.drawerItemList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
            this.drawerHolder = new DrawerItemHolder();
            view = inflater.inflate(this.layoutResID, parent, false);
           // this.drawerHolder.drawer_custom_ll = (LinearLayout) view.findViewById(R.id.drawer_custom_ll);
            this.drawerHolder.ItemName = (TextView) view.findViewById(R.id.drawer_menu_title_tv);
            view.setTag(this.drawerHolder);
        } else {
            this.drawerHolder = (DrawerItemHolder) view.getTag();
        }
        final MenuModel dItem = (MenuModel) getItem(position);
        this.drawerHolder.ItemName.setText(dItem.getMenu_title());
//        this.drawerHolder.drawer_custom_ll.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                v.setPressed(true);
//                LeftDrawerMenuAdapter.this.drawerHolder.drawer_custom_ll.performLongClick();
//                if (dItem.getMenu_title().equalsIgnoreCase("Home")) {
//                    LeftDrawerMenuAdapter.this.adapter_listener.performAdapterAction(AppConstants.HOME_PAGE, Boolean.valueOf(true));
//                } else if (dItem.getMenu_title().equalsIgnoreCase("Recipes")) {
//                    LeftDrawerMenuAdapter.this.adapter_listener.performAdapterAction(AppConstants.RECIPE_MAIN_PAGE, LeftDrawerMenuAdapter.this.drawerItemList.get(position));
//                } else if (dItem.getMenu_title().equalsIgnoreCase("My Store")) {
//                    LeftDrawerMenuAdapter.this.adapter_listener.performAdapterAction(AppConstants.STORE_MAIN_PAGE, LeftDrawerMenuAdapter.this.drawerItemList.get(position));
//                } else if (dItem.getMenu_title().equalsIgnoreCase("Menus")) {
//                    LeftDrawerMenuAdapter.this.adapter_listener.performAdapterAction(AppConstants.MENUPAGE, LeftDrawerMenuAdapter.this.drawerItemList.get(position));
//                } else if (dItem.getMenu_title().equalsIgnoreCase("My Loyalty")) {
//                    LeftDrawerMenuAdapter.this.adapter_listener.performAdapterAction(AppConstants.LOYALTY_PAGE, LeftDrawerMenuAdapter.this.drawerItemList.get(position));
//                } else if (dItem.getMenu_title().equalsIgnoreCase(AppConstants.MYOFFER_PAGE)) {
//                    LeftDrawerMenuAdapter.this.adapter_listener.performAdapterAction(AppConstants.MYOFFER_PAGE, LeftDrawerMenuAdapter.this.drawerItemList.get(position));
//                } else if (dItem.getMenu_title().equalsIgnoreCase("Reserve for Pick up") || dItem.getMenu_title().equalsIgnoreCase("Shop online")) {
//                    LeftDrawerMenuAdapter.this.adapter_listener.performAdapterAction(AppConstants.SHOPONLINE, LeftDrawerMenuAdapter.this.drawerItemList.get(position));
//                } else if (dItem.getMenu_title().equalsIgnoreCase(AppConstants.CONTACT_US_PAGE)) {
//                    LeftDrawerMenuAdapter.this.adapter_listener.performAdapterAction(AppConstants.CONTACT_US_PAGE, LeftDrawerMenuAdapter.this.drawerItemList.get(position));
//                } else if (dItem.getMenu_title().equalsIgnoreCase("Logout")) {
//                    LeftDrawerMenuAdapter.this.adapter_listener.performAdapterAction(AppConstants.LOGOUT_TEXT, Boolean.valueOf(true));
//                }
//            }
//        });
        return view;
    }
}
