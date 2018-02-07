package com.BasicApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.NewMenuDrawer;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by schaudhary_ic on 14-Feb-17.
 */

public class DrawerGridViewAdapter  extends BaseAdapter {
    public   List<NewMenuDrawer> mItems = new ArrayList<NewMenuDrawer>();
    public   LayoutInflater mInflater;
    Context context;
    public ImageLoader mImageLoader;

    public DrawerGridViewAdapter(Context context, List<NewMenuDrawer> mItems) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mItems =mItems;

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;



        if (v == null) {
            v = mInflater.inflate(R.layout.layout_grid_item, viewGroup, false);

        }

        NetworkImageView picture = (NetworkImageView) v.findViewById(R.id.image);
        TextView textView = (TextView) v.findViewById(R.id.info_text);

        NewMenuDrawer item = mItems.get(i);
        mImageLoader = CustomVolleyRequestQueue.getInstance(context)
                .getImageLoader();
        final String url = item.getProductImageUrl();
       mImageLoader.get(url, ImageLoader.getImageListener(picture, R.drawable.bgimage,  R.drawable.bgimage));
        picture.setImageUrl(url, mImageLoader);

       textView.setText(item.getProductName());

        return v;
    }
}
