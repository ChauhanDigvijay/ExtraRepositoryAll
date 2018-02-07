package com.hbh.honeybaked.module;

public class DrawerItem {
    String ItemName;
    int imgResID;

    public DrawerItem(String itemName, int imgResID) {
        this.ItemName = itemName;
        this.imgResID = imgResID;
    }

    public String getItemName() {
        return this.ItemName;
    }

    public void setItemName(String itemName) {
        this.ItemName = itemName;
    }

    public int getImgResID() {
        return this.imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }
}
