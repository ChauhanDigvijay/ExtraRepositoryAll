package com.olo.jambajuice.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
import com.olo.jambajuice.Activites.Generic.WebViewActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductCategoryDetail.ProductCategoryDetailActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail.ProductDetailViewPagerActivity;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductAdDetail;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.BusinessLogic.Services.ProductService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.StringUtilities;

import java.util.ArrayList;
import java.util.List;

import static com.olo.jambajuice.Utils.Constants.B_PRODUCTS;
import static com.olo.jambajuice.Utils.Constants.B_PRODUCT_DETAIL_POSITION;

public class RecentOrderFragment extends Fragment implements View.OnClickListener {
    List<ProductAdDetail> orderDetails;
    int position;

    public static RecentOrderFragment getInstance(List<ProductAdDetail> orderDetails, int position) {
        RecentOrderFragment fragment = new RecentOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(B_PRODUCTS, new ArrayList<ProductAdDetail>(orderDetails));
        bundle.putInt(B_PRODUCT_DETAIL_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setUpIntentData();
        View view = inflater.inflate(R.layout.fragment_product_pager, container, false);
        ProductAdDetail orderDetail = orderDetails.get(position);
//        TextView productName = (TextView) view.findViewById(R.id.productName);
//        TextView orderedAgo = (TextView) view.findViewById(R.id.orderedAgo);
        ImageView image = (ImageView) view.findViewById(R.id.productImage);
        ImageView productImageGradient = (ImageView) view.findViewById(R.id.productImageGradient);
        productImageGradient.setVisibility(View.GONE);
        //BitmapUtils.loadBitmapResourceWithViewSize(productImageGradient, R.drawable.product_detail_gradient, true);
        Ion.with(image).placeholder(R.drawable.product_placeholder).load(orderDetail.getImageUrl());
//        productName.setText(orderDetail.getStoreCode());
//        if (orderDetail.getTimePlaced() == null) {
//            orderedAgo.setVisibility(View.GONE);
//        } else {
//            orderedAgo.setText(Utils.getOrderPlacedTimeStatement(orderDetail.getTimePlaced()));
//        }
        view.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        //AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.UX.value, "tap_recent_ordered_product", orderDetails.get(position).getName(), 0, "RecentOrderFragment");
        List<Product> products = new ArrayList<Product>();
        List<ProductCategory> productCategories = new ArrayList<ProductCategory>();
        if (orderDetails.get(position).getProductId() != 0) {
            Product selectedProduct = ProductService.getProductWithProductId(orderDetails.get(position).getProductId());
            products.add(selectedProduct);
            if (DataManager.getInstance().getCurrentSelectedStore() != null && selectedProduct.getStoreMenuProduct() != null) {
                ProductDetailViewPagerActivity.show(getActivity(), products, 0);
            }
        } else if (StringUtilities.isValidString(orderDetails.get(position).getCategoryId())) {
            ProductCategory selectedProductCategory = ProductService.getCategoryWithCategoryId(orderDetails.get(position).getCategoryId());
            productCategories.add(selectedProductCategory);
            boolean isAvailableInThisStore = false;
            if (DataManager.getInstance().getSelectedStoreCategories() != null && selectedProductCategory != null) {
                for (ProductCategory productCategory : DataManager.getInstance().getSelectedStoreCategories()) {
                    if (productCategory != null) {
                        if (productCategory.getObjectID().equalsIgnoreCase(selectedProductCategory.getObjectID())) {
                            isAvailableInThisStore = true;
                            continue;
                        }
                    }
                }
            }

            if (DataManager.getInstance().getCurrentSelectedStore() != null
                    && isAvailableInThisStore) {
                ProductCategoryDetailActivity.show(getActivity(), productCategories, 0);
            }
        } else if ((orderDetails.get(position).getLinkUrl() != null)
                && !(orderDetails.get(position).getLinkUrl().equalsIgnoreCase(""))) {
            WebViewActivity.show(getActivity(), orderDetails.get(position).getAdName(), orderDetails.get(position).getLinkUrl());
        }

    }

    private void setUpIntentData() {
        Bundle bundle = getArguments();
        orderDetails = (ArrayList<ProductAdDetail>) bundle.getSerializable(B_PRODUCTS);
        position = bundle.getInt(B_PRODUCT_DETAIL_POSITION);
    }
}
