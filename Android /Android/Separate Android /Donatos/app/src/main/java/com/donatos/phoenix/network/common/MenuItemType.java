package com.donatos.phoenix.network.common;

public enum MenuItemType {
    PIZZA("pizza"),
    SALAD("salad"),
    SUB("sub"),
    OTHER("other");
    
    String mCategoryName;

    private MenuItemType(String str) {
        this.mCategoryName = str;
    }

    public final String getCategoryName() {
        return this.mCategoryName;
    }
}
