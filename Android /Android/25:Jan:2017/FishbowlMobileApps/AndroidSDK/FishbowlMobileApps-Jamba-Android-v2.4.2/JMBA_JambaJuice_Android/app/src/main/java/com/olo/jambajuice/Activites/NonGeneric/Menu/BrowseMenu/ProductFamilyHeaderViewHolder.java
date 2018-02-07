package com.olo.jambajuice.Activites.NonGeneric.Menu.BrowseMenu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Models.ProductFamily;
import com.olo.jambajuice.R;
import com.koushikdutta.ion.Ion;

/**
 * Created by Ihsanulhaq on 5/15/2015.
 */
public class ProductFamilyHeaderViewHolder extends BaseCategoryViewHolder {
    ImageView sectionImage;
    TextView sectionText;

    public ProductFamilyHeaderViewHolder(View view) {
        sectionImage = (ImageView) view.findViewById(R.id.img_section);
        sectionText = (TextView) view.findViewById(R.id.txt_section);
    }

    @Override
    public void invalidate(ProductFamily productFamily) {
        Ion.with(sectionImage).load(productFamily.getImageUrl());
        sectionText.setText(productFamily.getName());
    }
}
