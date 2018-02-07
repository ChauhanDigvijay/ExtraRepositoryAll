package com.olo.jambajuice.Activites.NonGeneric.Menu.BrowseMenu;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.NonGeneric.Menu.MenuLanding.MenuActivity;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductCategoryDetail.ProductCategoryDetailActivity;
import com.olo.jambajuice.Adapters.ProductFamiliesAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.BusinessLogic.Models.ProductFamily;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.StringUtilities;

import java.util.List;

/**
 * Created by Ihsanulhaq on 5/15/2015.
 */
public class ProductFamiliyCategoryViewHolder extends BaseCategoryViewHolder implements View.OnClickListener {
    private final List<ProductCategory> categories;
    private TextView title;
    private TextView text;
    private Activity activity;
    private ProductCategory category;
    private ProductFamiliesAdapter adapter;

    public ProductFamiliyCategoryViewHolder(View view, Activity activity, List<ProductCategory> categories, BaseAdapter _adapter) {
        title = (TextView) view.findViewById(R.id.txt_item_title);
        text = (TextView) view.findViewById(R.id.txt_item_text);
        this.activity = activity;
        this.categories = categories;
        this.adapter = (ProductFamiliesAdapter) _adapter;
        view.setOnClickListener(this);
    }

    @Override
    public void invalidate(ProductFamily family) {
        ProductCategory cat = (ProductCategory) family;
        title.setText(cat.getName());
        text.setText(cat.getTagLine());
        this.category = cat;
    }


    @Override
    public void onClick(View v) {

        ProductFamily productFamily = this.adapter.getFamilyById(category.getFamily());
        JambaAnalyticsManager.sharedInstance().track_ItemWith(category.getObjectID(), category.getName(), FBEventSettings.CATEGORY_CLICK);
        // JambaAnalyticsManager.sharedInstance().track_ItemWith(category.getObjectID(), category.getName(), FBEventSettings.SUB_CATEGORY_CLICK);
        ProductCategoryDetailActivity.show(activity, categories, categories.indexOf(category));

    }
}
