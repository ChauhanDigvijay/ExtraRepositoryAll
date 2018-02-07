package com.hbh.honeybaked.module;

public class RecipeModule {
    public String recipe_direction = null;
    public int recipe_img;
    public String recipe_ingredient = null;

    public RecipeModule(String recipe_ingredient, String recipe_direction, int recipe_img) {
        this.recipe_ingredient = recipe_ingredient;
        this.recipe_direction = recipe_direction;
        this.recipe_img = recipe_img;
    }

    public String getRecipe_ingredient() {
        return this.recipe_ingredient;
    }

    public void setRecipe_ingredient(String recipe_ingredient) {
        this.recipe_ingredient = recipe_ingredient;
    }

    public String getRecipe_direction() {
        return this.recipe_direction;
    }

    public void setRecipe_direction(String recipe_direction) {
        this.recipe_direction = recipe_direction;
    }

    public int getRecipe_img() {
        return this.recipe_img;
    }

    public void setRecipe_img(int recipe_img) {
        this.recipe_img = recipe_img;
    }
}
