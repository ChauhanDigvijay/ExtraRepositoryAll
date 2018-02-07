package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.olo.jambajuice.Activites.NonGeneric.Menu.BrowseMenu.BaseCategoryViewHolder;
import com.olo.jambajuice.Activites.NonGeneric.Menu.BrowseMenu.ProductFamiliyCategoryViewHolder;
import com.olo.jambajuice.Activites.NonGeneric.Menu.BrowseMenu.ProductFamilyHeaderViewHolder;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.BusinessLogic.Models.ProductFamily;
import com.olo.jambajuice.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ihsanulhaq on 5/15/2015.
 */
public class ProductFamiliesAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SECTION = 1;
    private final Activity activity;
    private List<ProductFamily> mData = new ArrayList<ProductFamily>();
    private LayoutInflater mInflater;
    private List<ProductCategory> allCategories;


    public ProductFamiliesAdapter(Context context) {
        activity = (Activity) context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addData(List<ProductFamily> newData) {
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseCategoryViewHolder holder = null;

        int rowType = getItemViewType(position);

        if (convertView == null) {
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.row_product_family_category, null);
                    holder = new ProductFamiliyCategoryViewHolder(convertView, activity, getAllCategories(), this);
                    break;
                case TYPE_SECTION:
                    convertView = mInflater.inflate(R.layout.row_product_family_section, null);
                    holder = new ProductFamilyHeaderViewHolder(convertView);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (BaseCategoryViewHolder) convertView.getTag();
        }
        holder.invalidate(mData.get(position));
        return convertView;
    }

    public List<ProductCategory> getAllCategories() {
        return this.allCategories;
    }

    public void setAllCategories(List<ProductCategory> allCategories) {
        this.allCategories = allCategories;
    }

    public ProductFamily getFamilyById(String id) {
        ProductFamily productFamily = null;
        for (int i = 0; i < mData.size(); i++) {
            productFamily = mData.get(i);
            String str1 = productFamily.getObjectID();
            String str2 = id;
            if (str1.equals(str2)) {

                return productFamily;
            }
        }
        return null;
    }

}
