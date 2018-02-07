package com.hbh.honeybaked.module;

public class OfferModel {
    private String favrt;
    private String go_to;
    private String inform;
    private String name;
    private String name2;
    private int recipe_img;

    public OfferModel(String name, String name2, String inform, String go_to, String favrt, int recipe_img) {
        this.name = name;
        this.name2 = name2;
        this.inform = inform;
        this.go_to = go_to;
        this.favrt = favrt;
        this.recipe_img = recipe_img;
    }

    public String getName() {
        return this.name;
    }

    public String getName2() {
        return this.name2;
    }

    public String getInform() {
        return this.inform;
    }

    public String getGo_to() {
        return this.go_to;
    }

    public String getFavrt() {
        return this.favrt;
    }

    public int getRecipe_img() {
        return this.recipe_img;
    }
}
