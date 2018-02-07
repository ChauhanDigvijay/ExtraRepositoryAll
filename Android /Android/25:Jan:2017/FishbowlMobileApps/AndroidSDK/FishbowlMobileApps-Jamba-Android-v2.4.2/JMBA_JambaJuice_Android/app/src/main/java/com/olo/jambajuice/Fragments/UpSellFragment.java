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
import com.olo.jambajuice.BusinessLogic.Models.UpSell;
import com.olo.jambajuice.BusinessLogic.Services.ProductService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.StringUtilities;

import java.util.ArrayList;
import java.util.List;

import static com.olo.jambajuice.Utils.Constants.B_PRODUCTS;
import static com.olo.jambajuice.Utils.Constants.B_PRODUCT_DETAIL_POSITION;

public class UpSellFragment extends Fragment {
    List<UpSell> upSellDetails;
    int position;

    public static UpSellFragment getInstance(List<UpSell> upSellDetails, int position) {
        UpSellFragment fragment = new UpSellFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(B_PRODUCTS, new ArrayList<UpSell>(upSellDetails));
        bundle.putInt(B_PRODUCT_DETAIL_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setUpIntentData();
        View view = inflater.inflate(R.layout.fragment_product_pager, container, false);
        UpSell currentUpSell = upSellDetails.get(position);
        ImageView image = (ImageView) view.findViewById(R.id.productImage);
        ImageView productImageGradient = (ImageView) view.findViewById(R.id.productImageGradient);
        productImageGradient.setVisibility(View.GONE);
        if(StringUtilities.isValidString(currentUpSell.getCampaign_image_url())) {
            Ion.with(image).placeholder(R.drawable.product_placeholder).load(currentUpSell.getCampaign_image_url());
        }else{
            Ion.with(image).placeholder(R.drawable.product_placeholder).load(currentUpSell.getDefault_image_url());
        }
        return view;
    }


    private void setUpIntentData() {
        Bundle bundle = getArguments();
        upSellDetails = (ArrayList<UpSell>) bundle.getSerializable(B_PRODUCTS);
        position = bundle.getInt(B_PRODUCT_DETAIL_POSITION);
    }
}
