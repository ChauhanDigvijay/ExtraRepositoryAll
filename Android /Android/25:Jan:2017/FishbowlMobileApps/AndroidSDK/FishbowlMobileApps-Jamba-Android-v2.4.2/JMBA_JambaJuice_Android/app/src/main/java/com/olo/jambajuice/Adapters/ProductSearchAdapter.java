package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail.ProductDetailViewPagerActivity;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductSearch;
import com.olo.jambajuice.R;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ProductSearchViewHolder> {

    private List<ProductSearch> productSearches;

    public ProductSearchAdapter(List<ProductSearch> productSearches) {
        this.productSearches = productSearches;
    }

    @Override
    public ProductSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_search, parent, false);
        return new ProductSearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductSearchViewHolder holder, final int position) {
        Ion.with(holder.productSearchImage).placeholder(R.drawable.product_placeholder).load(productSearches.get(position).getProduct().getThumbImageUrl());
        holder.productSearchHeading.setText(productSearches.get(position).getProduct().getName());
        holder.productSearchSubHeading.setText(productSearches.get(position).getProductCategory());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Product> products = new ArrayList<>();
                for (ProductSearch productSearch : productSearches) {
                    products.add(productSearch.getProduct());
                }
                ProductDetailViewPagerActivity.show((Activity) view.getContext(), products, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productSearches.size();
    }

    public class ProductSearchViewHolder extends RecyclerView.ViewHolder {
        ImageView productSearchImage;
        TextView productSearchHeading;
        TextView productSearchSubHeading;
        View view;

        ProductSearchViewHolder(View view) {
            super(view);
            this.view = view;
            productSearchImage = (ImageView) view.findViewById(R.id.productSearchImage);
            productSearchHeading = (TextView) view.findViewById(R.id.productSearchHeading);
            productSearchSubHeading = (TextView) view.findViewById(R.id.productSearchSubHeading);
        }
    }
}
