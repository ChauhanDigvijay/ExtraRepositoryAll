package com.olo.jambajuice.Activites.NonGeneric.Menu.ProductCategoryDetail;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail.ProductDetailViewPagerActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Ihsanulhaq on 5/19/2015.
 */
public class ProductCategoryDetailViewHolder extends BaseProductCategoryDetailViewHolder implements View.OnClickListener {
    private List<Product> productsData;
    private Product product;
    private Activity activity;

    public ProductCategoryDetailViewHolder(Activity activity, View itemView, List<Product> products) {
        super(itemView);
        this.activity = activity;
        this.productsData = products;
        ImageView productImageGradient = (ImageView) itemView.findViewById(R.id.productImageGradient);
        BitmapUtils.loadBitmapResourceWithViewSize(productImageGradient, R.drawable.product_detail_gradient, true);
        itemView.setOnClickListener(this);
    }

    @Override
    public void invalidate(Object data) {
        product = (Product) data;
        getProductName().setText(product.getName());
        Ion.with(getProductImage()).placeholder(R.drawable.product_placeholder).load(product.getThumbImageUrl());
    }

    @Override
    public void onClick(View v) {
        if (product.getStoreMenuProduct() != null) {
            int oloProductId = product.getStoreMenuProduct().getId();
            JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(oloProductId), product.getName(), FBEventSettings.PRODUCT_CLICK);
        }
        ProductDetailViewPagerActivity.show(activity, productsData, productsData.indexOf(product));
    }
}
