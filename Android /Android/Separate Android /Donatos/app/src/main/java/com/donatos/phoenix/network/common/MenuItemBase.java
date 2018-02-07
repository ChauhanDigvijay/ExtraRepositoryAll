package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.R;
import com.donatos.phoenix.p134b.C2510n;
import com.donatos.phoenix.ui.checkout.bi;
import com.donatos.phoenix.ui.common.ab;
import com.donatos.phoenix.ui.customizeitem.C2822u;
import com.donatos.phoenix.ui.customizeitem.C2823v;
import p158e.p159a.C3966a;

@JsonObject
public abstract class MenuItemBase {
    private String calories;
    private String externalId = null;
    private Boolean isTopping;
    private Integer quantity = Integer.valueOf(1);
    private Integer selectedToppingCount = Integer.valueOf(1);
    private C2822u toppingAmount = C2822u.REGULAR;
    private C2823v toppingCoverage = C2823v.WHOLE;
    private MenuItemType type = null;
    private Boolean userSelected = Boolean.valueOf(false);

    public void clearSelections() {
        this.userSelected = Boolean.valueOf(false);
        this.type = null;
        this.toppingAmount = C2822u.REGULAR;
        this.toppingCoverage = C2823v.WHOLE;
        this.quantity = Integer.valueOf(0);
        this.selectedToppingCount = Integer.valueOf(0);
        this.isTopping = null;
    }

    public String getCalories() {
        return this.calories;
    }

    public Integer getCommonId() {
        return Integer.valueOf(-1);
    }

    public Integer getDrawableReference() {
        C3966a.m13196a(getName().toLowerCase(), new Object[0]);
        return getName().toLowerCase().equals(bi.CARRYOUT.f8215e.toLowerCase()) ? Integer.valueOf(R.drawable.ic_carryout) : getName().toLowerCase().equals(bi.DELIVERY.f8215e.toLowerCase()) ? Integer.valueOf(R.drawable.ic_delivery) : getName().toLowerCase().equals(bi.WINDOW.f8215e.toLowerCase()) ? Integer.valueOf(R.drawable.ic_pickup) : getName().toLowerCase().equals(bi.EATIN.f8215e.toLowerCase()) ? Integer.valueOf(R.drawable.ic_eatin) : getName().toLowerCase().contains("visa") ? Integer.valueOf(R.drawable.ic_visa) : getName().toLowerCase().contains("american express") ? Integer.valueOf(R.drawable.ic_amex) : getName().toLowerCase().contains("mastercard") ? Integer.valueOf(R.drawable.ic_mastercard) : getName().toLowerCase().contains("disc") ? Integer.valueOf(R.drawable.ic_discover) : getName().toLowerCase().contains("credit card") ? Integer.valueOf(R.drawable.ic_genericcard) : getName().toLowerCase().contains("gift card") ? Integer.valueOf(R.drawable.ic_giftcard) : getName().toLowerCase().contains("cash") ? Integer.valueOf(R.drawable.ic_cash) : null;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public Integer getId() {
        return Integer.valueOf(-1);
    }

    public Boolean getIsCreateYourOwnPizza() {
        boolean z = !C2510n.m7367a(getExternalId()) && getExternalId().contains("cat=1,webcat=1,item=0");
        return Boolean.valueOf(z);
    }

    public Integer getMax() {
        return Integer.valueOf(-1);
    }

    public Integer getMin() {
        return Integer.valueOf(-1);
    }

    public Integer getMultiplier() {
        return Integer.valueOf(-1);
    }

    public String getName() {
        return null;
    }

    public Integer getSelectedToppingCount() {
        return this.selectedToppingCount;
    }

    public C2822u getToppingAmount() {
        return this.toppingAmount;
    }

    public String getToppingAmountString() {
        return this.toppingAmount.f8805d;
    }

    public C2823v getToppingCoverage() {
        return this.toppingCoverage;
    }

    public String getToppingCoverageString() {
        return ab.m8118a(this.toppingCoverage.f8810d);
    }

    public MenuItemType getType() {
        return this.type;
    }

    public Integer getUserQuantity() {
        int i = 0;
        if (isUserSelected().booleanValue()) {
            i = 1;
        }
        if (this.toppingAmount.equals(C2822u.DOUBLE)) {
            i *= 2;
        }
        if (this.quantity.intValue() > i) {
            i = this.quantity.intValue();
        }
        return Integer.valueOf(i);
    }

    public Boolean isTopping() {
        return this.isTopping;
    }

    public Boolean isUserSelected() {
        return this.userSelected;
    }

    public void setCalories(String str) {
        this.calories = str;
    }

    public void setExternalId(String str) {
        this.externalId = str;
    }

    public void setIsTopping(Boolean bool) {
        this.isTopping = bool;
    }

    public void setName(String str) {
    }

    public void setSelectedToppingCount(Integer num) {
        this.selectedToppingCount = num;
    }

    public void setToppingAmount(C2822u c2822u) {
        this.toppingAmount = c2822u;
    }

    public void setToppingCoverage(C2823v c2823v) {
        this.toppingCoverage = c2823v;
    }

    public void setType(MenuItemType menuItemType) {
        this.type = menuItemType;
    }

    public void setUserQuantity(Integer num) {
        this.quantity = num;
    }

    public void setUserSelected(Boolean bool) {
        this.userSelected = bool;
    }

    public void setUserSelections(MenuItemBase menuItemBase) {
        this.userSelected = menuItemBase.isUserSelected();
        this.type = menuItemBase.getType();
        this.toppingAmount = menuItemBase.getToppingAmount();
        this.toppingCoverage = menuItemBase.getToppingCoverage();
        this.quantity = menuItemBase.getUserQuantity();
        this.selectedToppingCount = menuItemBase.getSelectedToppingCount();
    }
}
