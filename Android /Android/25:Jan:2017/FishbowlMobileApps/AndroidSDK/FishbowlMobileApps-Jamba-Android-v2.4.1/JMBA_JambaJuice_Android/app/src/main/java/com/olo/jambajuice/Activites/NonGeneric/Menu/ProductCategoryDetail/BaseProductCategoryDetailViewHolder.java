package com.olo.jambajuice.Activites.NonGeneric.Menu.ProductCategoryDetail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.R;

/**
 * Created by Ihsanulhaq on 5/19/2015.
 */
public abstract class BaseProductCategoryDetailViewHolder extends RecyclerView.ViewHolder {

    private ImageView productImage;
    private TextView productName;
    private Button continueShoppingBtn;

    public BaseProductCategoryDetailViewHolder(final View itemView) {
        super(itemView);

        productImage = (ImageView) itemView.findViewById(R.id.productImage);
        productName = (TextView) itemView.findViewById(R.id.productName);
        continueShoppingBtn = (Button)itemView.findViewById(R.id.continueShoppingBtn);
    }

    public abstract void invalidate(Object data);

    public TextView getProductName() {
        return productName;
    }

    public ImageView getProductImage() {
        return productImage;
    }

    public Button getContinueShoppingBtn() {
        return continueShoppingBtn;
    }
}
