package com.olo.jambajuice.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.Fragments.ProductCategoryDetailFragment;
import com.olo.jambajuice.Fragments.ProductDetailFragment;

import java.util.List;

/**
 * Created by Nauman Afzaal on 28/05/15.
 */
public class ProductCategoryDetailViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<ProductCategory> products;

    public ProductCategoryDetailViewPagerAdapter(FragmentManager manager, List<ProductCategory> products) {
        super(manager);
        this.products = products;
    }

    @Override
    public Fragment getItem(int position) {
        return ProductCategoryDetailFragment.getInstance(products.get(position));
    }

    @Override
    public int getCount() {
        return products.size();
    }
}
