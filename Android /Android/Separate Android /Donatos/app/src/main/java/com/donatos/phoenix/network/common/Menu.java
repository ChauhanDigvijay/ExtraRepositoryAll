package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@JsonObject
public class Menu {
    @JsonField(name = {"calories"})
    private HashMap<String, String> calories = new HashMap();
    @JsonField(name = {"categories"})
    private List<MenuCategory> categories = new ArrayList();
    @JsonField(name = {"id"})
    private Integer id = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public Menu addCategoriesItem(MenuCategory menuCategory) {
        this.categories.add(menuCategory);
        return this;
    }

    public Menu calories(HashMap<String, String> hashMap) {
        this.calories = hashMap;
        return this;
    }

    public Menu categories(List<MenuCategory> list) {
        this.categories = list;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Menu menu = (Menu) obj;
        return C2507k.m7346a(this.id, menu.id) && C2507k.m7346a(this.categories, menu.categories);
    }

    public HashMap<String, String> getCalories() {
        return this.calories;
    }

    public List<MenuCategory> getCategories() {
        return this.categories;
    }

    public Integer getId() {
        return this.id;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.categories});
    }

    public Menu id(Integer num) {
        this.id = num;
        return this;
    }

    public void setCalories(HashMap<String, String> hashMap) {
        this.calories = hashMap;
    }

    public void setCategories(List<MenuCategory> list) {
        this.categories = list;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class Menu {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    categories: ").append(toIndentedString(this.categories)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
