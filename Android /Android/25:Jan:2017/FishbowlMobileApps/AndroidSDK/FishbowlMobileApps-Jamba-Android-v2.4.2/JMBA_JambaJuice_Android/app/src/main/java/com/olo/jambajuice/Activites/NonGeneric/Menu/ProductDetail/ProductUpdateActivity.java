package com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail;

import android.os.Bundle;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.BusinessLogic.Models.BasketProduct;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.Fragments.ProductDetailFragment;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;

public class ProductUpdateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);
        isShowBasketIcon = false;
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                Product product = (Product) bundle.getSerializable(Constants.B_PRODUCT);
                BasketProduct basketProduct = (BasketProduct) bundle.getSerializable(Constants.B_BASKET_PRODUCT);
                getSupportFragmentManager().beginTransaction().replace(R.id.rootView, ProductDetailFragment.getInstance(product, basketProduct)).commit();
            }
        }
    }

}
