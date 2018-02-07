package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olo.jambajuice.Activites.NonGeneric.Menu.MenuLanding.FeaturedProductViewHolder;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.R;

import java.util.List;

/**
 * Created by Nauman Afzaal on 13/05/15.
 */
public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductViewHolder> {

    private final Activity activity;
    public List<Product> data;
    private boolean isRecentOrder;

    public FeaturedProductAdapter(Activity activity, List<Product> data,boolean isRecentOrder) {
        this.data = data;
        this.activity = activity;
        this.isRecentOrder = isRecentOrder;
    }

    @Override
    public FeaturedProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(getRowXMLId(), viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        FeaturedProductViewHolder vh = new FeaturedProductViewHolder(activity, v, data,isRecentOrder);
        return vh;
    }

    @Override
    public void onBindViewHolder(FeaturedProductViewHolder featuredProductViewHolder, int position) {
        Product product = data.get(position);
        featuredProductViewHolder.invalidate(product);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getRowXMLId() {
        return R.layout.row_product;
    }

    public List<Product> getData() {
        return data;
    }


}
