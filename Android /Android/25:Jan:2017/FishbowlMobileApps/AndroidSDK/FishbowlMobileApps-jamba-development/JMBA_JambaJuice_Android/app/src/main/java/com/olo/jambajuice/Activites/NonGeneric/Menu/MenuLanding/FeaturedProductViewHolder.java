package com.olo.jambajuice.Activites.NonGeneric.Menu.MenuLanding;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.koushikdutta.ion.Ion;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail.ProductDetailViewPagerActivity;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.StringUtilities;

import java.util.List;

/**
 * Created by Nauman Afzaal on 13/05/15.
 */
public class FeaturedProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final Activity activity;
    public ImageView productImage;
    public TextView productName;
    public JambaApplication _app;
    private List<Product> productsData;
    private Product product;
    private boolean isRecentOrder;

    public FeaturedProductViewHolder(Activity activity, View itemView, List<Product> productsData, boolean isRecentOrder) {
        super(itemView);
        this.activity = activity;
        this.isRecentOrder = isRecentOrder;
        _app = JambaApplication.getAppContext();
        this.productsData = productsData;
        productImage = (ImageView) itemView.findViewById(R.id.productImage);
        productName = (TextView) itemView.findViewById(R.id.productName);
        ImageView productImageGradient = (ImageView) itemView.findViewById(R.id.productImageGradient);
        BitmapUtils.loadBitmapResourceWithViewSize(productImageGradient, R.drawable.product_detail_gradient, true);
        itemView.setOnClickListener(this);
    }

//    private void closeAnimation() {
//        if (StringUtilities.isValidString(MenuActivity.e)) {
//            View menuStoreChangeHeaderInnerLeft1 = activity.findViewById(R.id.menuStoreChangeHeader1);
//            JambaApplication appContext = JambaApplication.getAppContext();
//            Animation animdown = AnimationUtils.loadAnimation(appContext, R.anim.slide_down_activity);
//
//            // animdown.setFillAfter(true);
//
//            animdown.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//
//            menuStoreChangeHeaderInnerLeft1.setVisibility(View.GONE);
//            MenuActivity.e = "";
//            menuStoreChangeHeaderInnerLeft1.startAnimation(animdown);
//        }

    //   }

    @Override
    public void onClick(View v) {


//        closeAnimation();
//        MenuActivity.e = "";


        int position = productsData.indexOf(product);
        AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.UX.value, "tap_featured_product", productsData.get(position).getName(), 0, "FeaturedProductViewHolder");
        //Tracking Clyp Analytic

        if (product != null && product.getStoreMenuProduct() != null) {
            int oloProductId = product.getStoreMenuProduct().getId();
            JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(oloProductId), product.getName(), FBEventSettings.PRODUCT_CLICK);
            if (!isRecentOrder) {
                JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(oloProductId), product.getName(), FBEventSettings.FEATURE_PRODUCT_CLICK);
            }
        }

        ProductDetailViewPagerActivity.show(activity, productsData, position);


    }

    public void invalidate(Product product) {
        this.product = product;
        productName.setText(product.getName());
        Ion.with(productImage).placeholder(R.drawable.product_placeholder).load(product.getThumbImageUrl());
    }
}
