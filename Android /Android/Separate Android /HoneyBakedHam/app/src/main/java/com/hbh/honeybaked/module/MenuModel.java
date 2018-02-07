package com.hbh.honeybaked.module;

import java.io.Serializable;

public class MenuModel implements Serializable {
    String menu_img = null;
    String menu_link = null;
    String menu_title = null;
    int number = -1;

    public MenuModel(String menu_title, String menu_img, String menu_link) {
        this.menu_title = menu_title;
        this.menu_img = menu_img;
        this.menu_link = menu_link;
    }

    public MenuModel(String menu_title, int number) {
        this.menu_title = menu_title;
        this.number = number;
    }

    public int getValue() {
        return this.number;
    }

    public String getMenu_title() {
        return this.menu_title;
    }

    public void setMenu_title(String menu_title) {
        this.menu_title = menu_title;
    }

    public String getMenu_link() {
        return this.menu_link;
    }

    public void setMenu_link(String menu_link) {
        this.menu_link = menu_link;
    }

    public String getMenu_img() {
        return this.menu_img;
    }

    public void setMenu_img(String menu_img) {
        this.menu_img = menu_img;
    }
}
