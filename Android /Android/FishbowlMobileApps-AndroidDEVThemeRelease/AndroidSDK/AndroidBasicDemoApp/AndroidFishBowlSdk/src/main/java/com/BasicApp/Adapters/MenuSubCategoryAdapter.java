package com.BasicApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BasicApp.Fragments.MenuSubCategoryList;
import com.BasicApp.BusinessLogic.Models.MenuSubCategory;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;

import java.util.List;

/**
 * Created by schaudhary_ic on 28-Dec-16.
 */

public class MenuSubCategoryAdapter extends BaseAdapter {
    Context context;
    List<MenuSubCategory> menuSubCategoryList;
    public static int position = 0;
    NetworkImageView image;
    TextView text;


    private ImageLoader mImageLoader;

    public MenuSubCategoryAdapter(Context context, List<MenuSubCategory> menuSubCategoryList) {

        this.context = context;
        this.menuSubCategoryList = menuSubCategoryList;
    }

    @Override
    public int getCount() {
        return menuSubCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuSubCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        int category= MenuSubCategoryList.sharedInstance().mcatList.size();




        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_new_menusubcategory, parent, false);

        }
        try
        {   MenuSubCategory category1= menuSubCategoryList.get(position);
            this.position=position;
            text = (TextView) convertView.findViewById(R.id.text);
            image = (NetworkImageView) convertView.findViewById(R.id.image);
            text.setText(category1.getProductSubCategoryName());

        }
        catch (Exception e){}

        return convertView;
    }


}
