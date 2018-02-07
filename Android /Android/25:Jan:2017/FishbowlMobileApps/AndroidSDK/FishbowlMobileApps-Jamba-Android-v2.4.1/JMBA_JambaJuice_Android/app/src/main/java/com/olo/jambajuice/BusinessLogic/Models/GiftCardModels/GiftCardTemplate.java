package com.olo.jambajuice.BusinessLogic.Models.GiftCardModels;


import com.wearehathway.apps.incomm.Models.InCommBrandCardImage;

/**
 * Created by kalai on 24-05-2016.
 */
public class GiftCardTemplate {
    private Boolean isSelected;
    private InCommBrandCardImage inCommBrandCardImage;


    public GiftCardTemplate(Boolean isSelected, InCommBrandCardImage inCommBrandCardImage) {
        this.isSelected = isSelected;
        this.inCommBrandCardImage = inCommBrandCardImage;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public InCommBrandCardImage getInCommBrandCardImage() {
        return inCommBrandCardImage;
    }

    public void setInCommBrandCardImage(InCommBrandCardImage inCommBrandCardImage) {
        this.inCommBrandCardImage = inCommBrandCardImage;
    }
}
