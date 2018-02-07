package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonObject
public class MenuItem extends MenuItemBase {
    private String categoryName = null;
    @JsonField(name = {"configurable"})
    private Boolean configurable = null;
    @JsonField(name = {"description"})
    private String description = null;
    @JsonField(name = {"external_id"})
    private String externalId = null;
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"image"})
    private Image image = null;
    @JsonField(name = {"isLTO"})
    private Boolean isLTO = null;
    @JsonField(name = {"name"})
    private String name = null;
    private Integer quantity = Integer.valueOf(1);
    @JsonField(name = {"recipes"})
    private List<MenuRecipe> recipes = new ArrayList();

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public MenuItem addRecipesItem(MenuRecipe menuRecipe) {
        this.recipes.add(menuRecipe);
        return this;
    }

    public MenuItem categoryName(String str) {
        this.categoryName = str;
        return this;
    }

    public MenuItem configurable(Boolean bool) {
        this.configurable = bool;
        return this;
    }

    public MenuItem description(String str) {
        this.description = str;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MenuItem menuItem = (MenuItem) obj;
        return C2507k.m7346a(this.id, menuItem.id) && C2507k.m7346a(this.externalId, menuItem.externalId) && C2507k.m7346a(this.name, menuItem.name) && C2507k.m7346a(this.description, menuItem.description) && C2507k.m7346a(this.configurable, menuItem.configurable) && C2507k.m7346a(this.image, menuItem.image) && C2507k.m7346a(this.recipes, menuItem.recipes) && C2507k.m7346a(this.isLTO, menuItem.isLTO);
    }

    public MenuItem externalId(String str) {
        this.externalId = str;
        return this;
    }

    public String getCalorieKeyString() {
        Object num;
        String str = "%1$s::%2$s::%3$s::%4$s::%5$s";
        MenuRecipe menuRecipe = (MenuRecipe) getRecipes().get(getSelectedRecipeIndex());
        String num2 = menuRecipe != null ? menuRecipe.getId().toString() : "null";
        MenuSize menuSize = menuRecipe.getSizes().size() > 0 ? (MenuSize) menuRecipe.getSizes().get(menuRecipe.getSelectedSizeIndex()) : null;
        if (menuSize != null) {
            num = menuSize.getId().toString();
        } else {
            String str2 = "null";
        }
        MenuTopping menuTopping = menuSize.getFlavors().size() > 0 ? (MenuTopping) menuSize.getFlavors().get(menuSize.getSelectedFlavorIndex()) : null;
        String num3 = menuTopping != null ? menuTopping.getId().toString() : "null";
        return String.format(str, new Object[]{getId(), num2, num, "null", num3});
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public Boolean getConfigurable() {
        return this.configurable;
    }

    public String getDescription() {
        return this.description;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public Integer getId() {
        return this.id;
    }

    public Image getImage() {
        return this.image;
    }

    public Boolean getIsLTO() {
        return this.isLTO;
    }

    public MenuItemType getMenuItemType() {
        if (this.categoryName != null) {
            if (this.categoryName.toLowerCase().contains(MenuItemType.PIZZA.getCategoryName())) {
                return MenuItemType.PIZZA;
            }
            if (this.categoryName.toLowerCase().contains(MenuItemType.SUB.getCategoryName())) {
                return MenuItemType.SUB;
            }
            if (this.categoryName.toLowerCase().contains(MenuItemType.SALAD.getCategoryName())) {
                return MenuItemType.SALAD;
            }
        }
        return MenuItemType.OTHER;
    }

    public String getName() {
        return this.name;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public List<MenuRecipe> getRecipes() {
        return this.recipes;
    }

    public int getSelectedRecipeIndex() {
        for (MenuRecipe menuRecipe : this.recipes) {
            if (menuRecipe.isUserSelected().booleanValue()) {
                return this.recipes.indexOf(menuRecipe);
            }
        }
        return 0;
    }

    public int getSelectedRecipeIndex(int i) {
        for (MenuRecipe menuRecipe : this.recipes) {
            if (menuRecipe.getId().equals(Integer.valueOf(i))) {
                return this.recipes.indexOf(menuRecipe);
            }
        }
        return 0;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.externalId, this.name, this.description, this.configurable, this.image, this.recipes, this.isLTO});
    }

    public MenuItem id(Integer num) {
        this.id = num;
        return this;
    }

    public MenuItem image(Image image) {
        this.image = image;
        return this;
    }

    public MenuItem isLTO(Boolean bool) {
        this.isLTO = bool;
        return this;
    }

    public MenuItem name(String str) {
        this.name = str;
        return this;
    }

    public MenuItem recipes(List<MenuRecipe> list) {
        this.recipes = list;
        return this;
    }

    public void setConfigurable(Boolean bool) {
        this.configurable = bool;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setExternalId(String str) {
        this.externalId = str;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setIsLTO(Boolean bool) {
        this.isLTO = bool;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setQuantity(Integer num) {
        this.quantity = num;
    }

    public void setRecipes(List<MenuRecipe> list) {
        this.recipes = list;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class MenuItem {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    externalId: ").append(toIndentedString(this.externalId)).append("\n");
        stringBuilder.append("    name: ").append(toIndentedString(this.name)).append("\n");
        stringBuilder.append("    description: ").append(toIndentedString(this.description)).append("\n");
        stringBuilder.append("    configurable: ").append(toIndentedString(this.configurable)).append("\n");
        stringBuilder.append("    image: ").append(toIndentedString(this.image)).append("\n");
        stringBuilder.append("    recipes: ").append(toIndentedString(this.recipes)).append("\n");
        stringBuilder.append("    isLTO: ").append(toIndentedString(this.isLTO)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
