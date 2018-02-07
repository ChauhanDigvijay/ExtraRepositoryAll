package com.olo.jambajuice.BusinessLogic.Models.GiftCardModels;

import com.wearehathway.apps.incomm.Models.InCommCard;

/**
 * Created by Ananad on 14-Sep-16.
 */
public class PayGiftCard {
    private Boolean isSelected;
    private InCommCard eGiftCardList;

    public PayGiftCard(Boolean isSelected, InCommCard eGiftCardList) {
        this.isSelected = isSelected;
        this.eGiftCardList = eGiftCardList;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public InCommCard geteGiftCardList() {
        return eGiftCardList;
    }

    public void seteGiftCardList(InCommCard eGiftCardList) {
        this.eGiftCardList = eGiftCardList;
    }
}
