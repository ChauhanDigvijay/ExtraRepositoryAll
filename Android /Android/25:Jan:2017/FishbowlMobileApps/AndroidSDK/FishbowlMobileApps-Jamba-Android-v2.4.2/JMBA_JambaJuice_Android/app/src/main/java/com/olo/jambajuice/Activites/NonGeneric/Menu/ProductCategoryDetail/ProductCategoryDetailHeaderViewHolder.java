package com.olo.jambajuice.Activites.NonGeneric.Menu.ProductCategoryDetail;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.R;
import com.koushikdutta.ion.Ion;

/**
 * Created by Ihsanulhaq on 5/19/2015.
 */
public class ProductCategoryDetailHeaderViewHolder extends BaseProductCategoryDetailViewHolder {

    private TextView tagLine;
    private ImageView productImage;

    public ProductCategoryDetailHeaderViewHolder(View itemView) {
        super(itemView);
        tagLine = (TextView) itemView.findViewById(R.id.productTagLine);
        productImage = (ImageView) itemView.findViewById(R.id.productImage);
    }

    @Override
    public void invalidate(Object data) {
        ProductCategory category = (ProductCategory) data;
        getProductName().setText(category.getName());
        tagLine.setText(category.getDesc());
        Ion.with(productImage).placeholder(R.drawable.product_placeholder).load(category.getImageUrl());
    }
}
