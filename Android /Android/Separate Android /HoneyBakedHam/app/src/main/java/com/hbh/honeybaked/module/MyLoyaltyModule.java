package com.hbh.honeybaked.module;

public class MyLoyaltyModule {
    String points = null;
    int[] shop_img = null;

    public MyLoyaltyModule(String points, int[] shop_img) {
        this.points = points;
        this.shop_img = shop_img;
    }

    public String getPoints() {
        return this.points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public int[] getShop_img() {
        return this.shop_img;
    }

    public void setShop_img(int[] shop_img) {
        this.shop_img = shop_img;
    }
}
