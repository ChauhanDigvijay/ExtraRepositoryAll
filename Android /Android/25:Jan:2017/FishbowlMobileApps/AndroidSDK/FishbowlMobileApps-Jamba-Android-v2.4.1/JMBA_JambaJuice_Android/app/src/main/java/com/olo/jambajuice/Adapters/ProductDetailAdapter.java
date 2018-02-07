package com.olo.jambajuice.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.olo.jambajuice.Fragments.ProductDetailFragment;
import com.olo.jambajuice.BusinessLogic.Models.Product;

import java.util.List;

/**
 * Created by Nauman Afzaal on 28/05/15.
 */
public class ProductDetailAdapter extends FragmentStatePagerAdapter {

    private List<Product> products;

    public ProductDetailAdapter(FragmentManager manager, List<Product> products) {
        super(manager);
        this.products = products;
    }

    @Override
    public Fragment getItem(int position) {
        return ProductDetailFragment.getInstance(products.get(position));
    }

    @Override
    public int getCount() {
        return products.size();
    }
}
