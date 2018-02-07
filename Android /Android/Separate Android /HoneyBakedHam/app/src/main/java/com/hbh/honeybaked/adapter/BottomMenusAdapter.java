package com.hbh.honeybaked.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hbh.honeybaked.R;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.listener.AdapterListener;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.supportingfiles.Utility;

import java.util.ArrayList;

public class BottomMenusAdapter extends BaseAdapter {
    AdapterListener adapter_listener = null;
    int[] alImage;
    boolean bol_obj;
    private Context context;
    ViewHolder holder = null;
    private LayoutInflater mInflater;
    ArrayList<MenuModel> plotsImages;
    int[] screenSizes;

    private class ViewHolder {
        public ImageView imgThumbnail;

        private ViewHolder() {
        }
    }

    public BottomMenusAdapter(Context context, int[] alImage, boolean bol_obj) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.bol_obj = bol_obj;
        this.adapter_listener = (AdapterListener) context;
        this.alImage = alImage;
        this.screenSizes = Utility.getWindowHeightWidth((Activity) context);
    }

    public BottomMenusAdapter(Context context, ArrayList<MenuModel> plotsImages, boolean bol_obj) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.bol_obj = bol_obj;
        this.adapter_listener = (AdapterListener) context;
        this.plotsImages = plotsImages;
        this.screenSizes = Utility.getWindowHeightWidth((Activity) context);
    }

    public int getCount() {
        if (this.bol_obj) {
            return this.alImage.length;
        }
        return this.plotsImages.size();
    }

    public Object getItem(int position) {
        if (this.bol_obj) {
            return Integer.valueOf(this.alImage[position]);
        }
        return this.plotsImages.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            this.holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.bottom_menus_adapter_layout, parent, false);
           // this.holder.imgThumbnail = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            if (!this.bol_obj) {
                LayoutParams layoutParams = convertView.getLayoutParams();
                layoutParams.width = this.screenSizes[0] / this.plotsImages.size();
                convertView.setLayoutParams(layoutParams);
            }
            convertView.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) convertView.getTag();
        }
        if (this.bol_obj) {
            this.holder.imgThumbnail.setImageResource(this.alImage[position]);
        } else if (((MenuModel) this.plotsImages.get(position)).getMenu_title().equalsIgnoreCase("My Store")) {
            this.holder.imgThumbnail.setImageResource(R.drawable.location_icon);
        } else if (((MenuModel) this.plotsImages.get(position)).getMenu_title().equalsIgnoreCase(AppConstants.MYOFFER_PAGE)) {
            this.holder.imgThumbnail.setImageResource(R.drawable.dollar_icon);
        } else if (((MenuModel) this.plotsImages.get(position)).getMenu_title().equalsIgnoreCase("My Loyalty")) {
            this.holder.imgThumbnail.setImageResource(R.drawable.loyalty_icon);
        } else if (((MenuModel) this.plotsImages.get(position)).getMenu_title().equalsIgnoreCase("Menus")) {
            this.holder.imgThumbnail.setImageResource(R.drawable.menu_icon);
        } else if (((MenuModel) this.plotsImages.get(position)).getMenu_title().equalsIgnoreCase("Reserve For Pick Up")) {
            this.holder.imgThumbnail.setImageResource(R.drawable.bag_icon);
        } else if (((MenuModel) this.plotsImages.get(position)).getMenu_title().equalsIgnoreCase("Shop Online")) {
            this.holder.imgThumbnail.setImageResource(R.drawable.shop_icon);
        } else if (((MenuModel) this.plotsImages.get(position)).getMenu_title().equalsIgnoreCase("Recipes")) {
            this.holder.imgThumbnail.setImageResource(R.drawable.recipe_icon);
        } else if (((MenuModel) this.plotsImages.get(position)).getMenu_title().equalsIgnoreCase("Contact us")) {
           // this.holder.imgThumbnail.setImageResource(R.drawable.customer_service_icon);
        } else if (((MenuModel) this.plotsImages.get(position)).getMenu_title().equalsIgnoreCase("Home")) {
            this.holder.imgThumbnail.setImageResource(R.drawable.home_icon);
        }
//        this.holder.imgThumbnail.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (BottomMenusAdapter.this.bol_obj) {
//                    switch (position) {
//                        case 0:
//                            BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.SHOPONLINE, new MenuModel("FACEBOOK", "", "https://www.facebook.com/HoneyBaked/"));
//                            return;
//                        case 1:
//                            BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.SHOPONLINE, new MenuModel("INSTAGRAM", "", "https://www.instagram.com/honeybaked_ham/"));
//                            return;
//                        case 2:
//                            BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.SHOPONLINE, new MenuModel("TWITTER", "", "https://twitter.com/honeybakedham"));
//                            return;
//                        case 3:
//                            BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.SHOPONLINE, new MenuModel("PINTEREST", "", "https://www.pinterest.com/honeybaked/"));
//                            return;
//                        default:
//                            return;
//                    }
//                }
//                String menu_title = ((MenuModel) BottomMenusAdapter.this.plotsImages.get(position)).getMenu_title();
//                boolean z = true;
//                switch (menu_title.hashCode()) {
//                    case -2099936115:
//                        if (menu_title.equals(AppConstants.MYOFFER_PAGE)) {
//                            z = true;
//                            break;
//                        }
//                        break;
//                    case -1547897723:
//                        if (menu_title.equals("Recipes")) {
//                            z = true;
//                            break;
//                        }
//                        break;
//                    case -1097410305:
//                        if (menu_title.equals("Reserve For Pick Up")) {
//                            z = true;
//                            break;
//                        }
//                        break;
//                    case 2255103:
//                        if (menu_title.equals("Home")) {
//                            z = false;
//                            break;
//                        }
//                        break;
//                    case 53442002:
//                        if (menu_title.equals("My Loyalty")) {
//                            z = true;
//                            break;
//                        }
//                        break;
//                    case 74229460:
//                        if (menu_title.equals("Menus")) {
//                            z = true;
//                            break;
//                        }
//                        break;
//                    case 1778562749:
//                        if (menu_title.equals("Shop Online")) {
//                            z = true;
//                            break;
//                        }
//                        break;
//                    case 2062523757:
//                        if (menu_title.equals("My Store")) {
//                            z = true;
//                            break;
//                        }
//                        break;
//                    case 2133280478:
//                        if (menu_title.equals(AppConstants.CONTACT_US_PAGE)) {
//                            z = true;
//                            break;
//                        }
//                        break;
//                }
//                switch (z) {
//                    case false:
//                        BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.HOME_PAGE, Boolean.valueOf(true));
//                        return;
//                    case true:
//                        BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.STORE_MAIN_PAGE, BottomMenusAdapter.this.plotsImages.get(position));
//                        return;
//                    case true:
//                        BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.MYOFFER_PAGE, BottomMenusAdapter.this.plotsImages.get(position));
//                        return;
//                    case true:
//                        BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.LOYALTY_PAGE, BottomMenusAdapter.this.plotsImages.get(position));
//                        return;
//                    case true:
//                        BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.MENUPAGE, BottomMenusAdapter.this.plotsImages.get(position));
//                        return;
//                    case true:
//                        BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.SHOPONLINE, BottomMenusAdapter.this.plotsImages.get(position));
//                        return;
//                    case true:
//                        BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.SHOPONLINE, BottomMenusAdapter.this.plotsImages.get(position));
//                        return;
//                    case true:
//                        BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.RECIPE_MAIN_PAGE, BottomMenusAdapter.this.plotsImages.get(position));
//                        return;
//                    case true:
//                        BottomMenusAdapter.this.adapter_listener.performAdapterAction(AppConstants.CONTACT_US_PAGE, BottomMenusAdapter.this.plotsImages.get(position));
//                        return;
//                    default:
//                        return;
//                }
//            }
//        });
        return convertView;
    }
}
