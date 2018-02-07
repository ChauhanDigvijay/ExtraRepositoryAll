package com.BasicApp.ModelAdapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.Category;
import com.BasicApp.BusinessLogic.Models.Product;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;


/**
 * Created by digvijaychauhan on 22/09/16.
 */

public class SubCategoryHolder extends RecyclerView.ViewHolder
{
    public  TextView productName,productDesc,productCost;
    private Product product;
    private ImageLoader mImageLoader;
    Activity context;
    public static Boolean checkback = true;
    int catposition;
    NetworkImageView productImage;
    Category category;
    public SubCategoryHolder(final Activity context, final View itemView, final Category category, int catposition1) {
        super(itemView);
        this.context=context;
        this.category=category;
        this.catposition=catposition1;
        productName = (TextView) itemView.findViewById(R.id.productName);
        productImage = (NetworkImageView) itemView.findViewById(R.id.productimage);
        //productDesc = (TextView) itemView.findViewById(R.id.productDesc);
        productCost=(TextView)itemView.findViewById(R.id.productCost);

    }


    public void invalidate(Product product) {
        this.product = product;
        productName.setText(product.iname);
       // productDesc.setText(product.idesc);
        productCost.setText("$"+product.icost);
        mImageLoader = CustomVolleyRequestQueue.getInstance(context)
                .getImageLoader();
        mImageLoader.get(product.imageurl, ImageLoader.getImageListener(productImage,R.drawable.ic_launcher, android.R.drawable.ic_dialog_alert));
        productImage.setImageUrl(product.imageurl,mImageLoader);
    }
}
