package com.fishbowl.cbc.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.fishbowl.cbc.R;
import com.fishbowl.cbc.activities.nonGeneric.productfamily.ProductFamilyListActivity;
import com.fishbowl.cbc.businesslogic.models.ProductFamilyModel;
import com.fishbowl.cbc.utils.TransitionManager;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by VT027 on 4/24/2017.
 */

public class ProductFamilyAdapter extends RecyclerView.Adapter<ProductFamilyAdapter.ProductFamilyViewHolder> {

    private final Activity mActivity;
    private List<ProductFamilyModel> mProductFamily;
    private int mLastCurrentPosition = -1;

    public ProductFamilyAdapter(Activity activity, List<ProductFamilyModel> list) {
        this.mActivity = activity;
        this.mProductFamily = list;
    }

    @Override
    public ProductFamilyAdapter.ProductFamilyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food_menu_list, parent, false);

        ProductFamilyViewHolder viewHolder = new ProductFamilyViewHolder(mActivity, view, mProductFamily);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductFamilyAdapter.ProductFamilyViewHolder holder, int position) {
        ProductFamilyModel product = mProductFamily.get(position);
        holder.invalidate(product);
        //setAnimation(holder.itemView, position);

    }

    private void setAnimation(View itemView, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > mLastCurrentPosition) {
            Animation animation = AnimationUtils.loadAnimation(mActivity, android.R.anim.slide_in_left);
            animation.setDuration(500);
            itemView.startAnimation(animation);
            mLastCurrentPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mProductFamily.size();
    }

    static class ProductFamilyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mFoodMenuText;
        private ImageView mFoodMenuImage;
        private Activity mActivity;
        private List<ProductFamilyModel> mProductData;
        private ProductFamilyModel mProduct;

        public ProductFamilyViewHolder(Activity activity, View itemView, List<ProductFamilyModel> productData) {
            super(itemView);

            this.mActivity = activity;
            this.mProductData = productData;

            mFoodMenuText = (TextView) itemView.findViewById(R.id.productFamilyText);
            mFoodMenuImage = (ImageView) itemView.findViewById(R.id.productFamilyImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TransitionManager.slideUp(mActivity, ProductFamilyListActivity.class, true);

        }

        public void invalidate(ProductFamilyModel product) {
            this.mProduct = product;
            mFoodMenuText.setText(product.getmTitle());
            Ion.with(mFoodMenuImage).placeholder(R.drawable.product_placeholder).load(product.getmThumbnail());

        }
    }
}
