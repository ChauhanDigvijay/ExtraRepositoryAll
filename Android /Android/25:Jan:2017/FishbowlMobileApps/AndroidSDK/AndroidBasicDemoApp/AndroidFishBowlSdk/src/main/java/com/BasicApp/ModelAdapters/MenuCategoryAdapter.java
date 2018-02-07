package com.BasicApp.ModelAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BasicApp.Fragments.MenuCategoryList;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.FBMenuCategoryDetailItem;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;

import java.util.List;

/**
 * Created by schaudhary_ic on 26-Dec-16.
 */

public class MenuCategoryAdapter extends BaseAdapter{
    Context context;
    List<FBMenuCategoryDetailItem> menuCategoryList;
    public static int position = 0;
    NetworkImageView image;
    TextView text;


    private ImageLoader mImageLoader;

    public MenuCategoryAdapter(Context context, List<FBMenuCategoryDetailItem> menuCategoryList) {

        this.context = context;
        this.menuCategoryList = menuCategoryList;
    }




    @Override
    public int getCount() {
        return menuCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        int category= MenuCategoryList.sharedInstance().mcatList.size();




        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_new_menu_category, parent, false);

        }
try
{   FBMenuCategoryDetailItem category1= menuCategoryList.get(position);
            this.position=position;
    text = (TextView) convertView.findViewById(R.id.text);
        image = (NetworkImageView) convertView.findViewById(R.id.image);
           text.setText(category1.getProductCategoryName());
        mImageLoader= CustomVolleyRequestQueue.getInstance(context).getImageLoader();

    if (position==0)
    {final String url =category1.getProductCategoryImageUrl();
        mImageLoader.get(url, ImageLoader.getImageListener(image, R.drawable.menu_ham,  R.drawable.menu_ham));
        image.setImageUrl(url,mImageLoader);}
else if(position==1){
        final String url =category1.getProductCategoryImageUrl();
        mImageLoader.get(url, ImageLoader.getImageListener(image, R.drawable.menu_burger,  R.drawable.menu_burger));
        image.setImageUrl(url,mImageLoader);
    }


  }
catch (Exception e){}

        return convertView;
    }



}
