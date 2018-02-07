package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2500d;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@JsonObject
public class CartItem {
    @JsonField(name = {"dressings"})
    private List<Integer> dressings = new ArrayList();
    @JsonField(name = {"flavors"})
    private List<Integer> flavors = new ArrayList();
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"instructions"})
    private List<Integer> instructions = new ArrayList();
    @JsonField(name = {"internalId"})
    private String interalId = UUID.randomUUID().toString();
    private Boolean isCustomized = null;
    @JsonField(name = {"price"})
    private Double price = null;
    @JsonField(name = {"quantity"})
    private Integer quantity = null;
    @JsonField(name = {"recipe"})
    private CartItemRecipe recipe = null;
    @JsonField(name = {"required"})
    private List<Integer> required = new ArrayList();
    @JsonField(name = {"sauces"})
    private List<Integer> sauces = new ArrayList();
    @JsonField(name = {"toppings"})
    private List<CartItemToppings> toppings = new ArrayList();

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public CartItem addDressingsItem(Integer num) {
        this.dressings.add(num);
        return this;
    }

    public CartItem addFlavorsItem(Integer num) {
        this.flavors.add(num);
        return this;
    }

    public CartItem addInstructionsItem(Integer num) {
        this.instructions.add(num);
        return this;
    }

    public CartItem addRequiredItem(Integer num) {
        this.required.add(num);
        return this;
    }

    public CartItem addSaucesItem(Integer num) {
        this.sauces.add(num);
        return this;
    }

    public CartItem addToppingsItem(CartItemToppings cartItemToppings) {
        this.toppings.add(cartItemToppings);
        return this;
    }

    public CartItem dressings(List<Integer> list) {
        this.dressings = list;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CartItem cartItem = (CartItem) obj;
        return C2507k.m7346a(this.id, cartItem.id) && C2507k.m7346a(this.quantity, cartItem.quantity) && C2507k.m7346a(this.instructions, cartItem.instructions) && C2507k.m7346a(this.recipe, cartItem.recipe) && C2507k.m7346a(this.toppings, cartItem.toppings) && C2507k.m7346a(this.required, cartItem.required) && C2507k.m7346a(this.dressings, cartItem.dressings) && C2507k.m7346a(this.flavors, cartItem.flavors) && C2507k.m7346a(this.sauces, cartItem.sauces);
    }

    public CartItem flavors(List<Integer> list) {
        this.flavors = list;
        return this;
    }

    public List<Integer> getDressings() {
        return this.dressings;
    }

    public List<Integer> getFlavors() {
        return this.flavors;
    }

    public Integer getId() {
        return this.id;
    }

    public List<Integer> getInstructions() {
        return this.instructions;
    }

    public String getInteralId() {
        return this.interalId;
    }

    public Boolean getIsCustomized(MenuItem menuItem) {
        if (this.isCustomized != null) {
            return this.isCustomized;
        }
        this.isCustomized = Boolean.valueOf(false);
        CartItemRecipe recipe = getRecipe();
        MenuSize menuSize = (MenuSize) C2500d.m7331a(((MenuRecipe) C2500d.m7331a(menuItem.getRecipes(), CartItem$$Lambda$1.lambdaFactory$(recipe))).getSizes(), CartItem$$Lambda$2.lambdaFactory$(recipe));
        if (getToppings().size() != menuSize.getSelectedToppings().size()) {
            this.isCustomized = Boolean.valueOf(true);
            return this.isCustomized;
        }
        for (CartItemToppings cartItemToppings : getToppings()) {
            MenuTopping menuTopping = (MenuTopping) C2500d.m7331a(menuSize.getToppings(), CartItem$$Lambda$3.lambdaFactory$(cartItemToppings));
            if (!menuTopping.getMultiplier().equals(cartItemToppings.getMultiplier()) || !menuTopping.getPlacement().equals(cartItemToppings.getPlacement())) {
                this.isCustomized = Boolean.valueOf(true);
                return this.isCustomized;
            } else if (!C2500d.m7332a(menuSize.getSelectedToppings(), cartItemToppings.getId())) {
                this.isCustomized = Boolean.valueOf(true);
                return this.isCustomized;
            }
        }
        return this.isCustomized;
    }

    public Double getPrice() {
        return this.price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public CartItemRecipe getRecipe() {
        return this.recipe;
    }

    public List<Integer> getRequired() {
        return this.required;
    }

    public List<Integer> getSauces() {
        return this.sauces;
    }

    public List<CartItemToppings> getToppings() {
        return this.toppings;
    }

    public int getTotalDressings() {
        return this.dressings.size();
    }

    public int getTotalToppings() {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        for (CartItemToppings cartItemToppings : this.toppings) {
            if (cartItemToppings.getPlacement().equals(Integer.valueOf(1))) {
                i2++;
            } else if (cartItemToppings.getPlacement().equals(Integer.valueOf(2))) {
                i3++;
            } else {
                i = cartItemToppings.getPlacement().equals(Integer.valueOf(3)) ? i + 1 : i;
            }
        }
        return Math.min(i3, i2) + i;
    }

    public boolean hasHalfToppingOnly() {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        for (CartItemToppings cartItemToppings : this.toppings) {
            if (cartItemToppings.getPlacement().equals(Integer.valueOf(1))) {
                i2++;
            } else if (cartItemToppings.getPlacement().equals(Integer.valueOf(2))) {
                i3++;
            } else {
                i = cartItemToppings.getPlacement().equals(Integer.valueOf(3)) ? i + 1 : i;
            }
        }
        return i == 0 ? (i3 == 0 && i2 > 0) || (i2 == 0 && i3 > 0) : false;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.quantity, this.instructions, this.recipe, this.toppings, this.required, this.dressings, this.flavors, this.sauces});
    }

    public CartItem id(Integer num) {
        this.id = num;
        return this;
    }

    public CartItem instructions(List<Integer> list) {
        this.instructions = list;
        return this;
    }

    public CartItem interalId(String str) {
        this.interalId = str;
        return this;
    }

    public CartItem price(Double d) {
        this.price = d;
        return this;
    }

    public CartItem quantity(Integer num) {
        this.quantity = num;
        return this;
    }

    public CartItem recipe(CartItemRecipe cartItemRecipe) {
        this.recipe = cartItemRecipe;
        return this;
    }

    public CartItem required(List<Integer> list) {
        this.required = list;
        return this;
    }

    public CartItem sauces(List<Integer> list) {
        this.sauces = list;
        return this;
    }

    public void setDressings(List<Integer> list) {
        this.dressings = list;
    }

    public void setFlavors(List<Integer> list) {
        this.flavors = list;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public void setInstructions(List<Integer> list) {
        this.instructions = list;
    }

    public void setInteralId(String str) {
        this.interalId = str;
    }

    public void setPrice(Double d) {
        this.price = d;
    }

    public void setQuantity(Integer num) {
        this.quantity = num;
    }

    public void setRecipe(CartItemRecipe cartItemRecipe) {
        this.recipe = cartItemRecipe;
    }

    public void setRequired(List<Integer> list) {
        this.required = list;
    }

    public void setSauces(List<Integer> list) {
        this.sauces = list;
    }

    public void setToppings(List<CartItemToppings> list) {
        this.toppings = list;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class CartItem {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    quantity: ").append(toIndentedString(this.quantity)).append("\n");
        stringBuilder.append("    instructions: ").append(toIndentedString(this.instructions)).append("\n");
        stringBuilder.append("    recipe: ").append(toIndentedString(this.recipe)).append("\n");
        stringBuilder.append("    toppings: ").append(toIndentedString(this.toppings)).append("\n");
        stringBuilder.append("    required: ").append(toIndentedString(this.required)).append("\n");
        stringBuilder.append("    dressings: ").append(toIndentedString(this.dressings)).append("\n");
        stringBuilder.append("    flavors: ").append(toIndentedString(this.flavors)).append("\n");
        stringBuilder.append("    sauces: ").append(toIndentedString(this.sauces)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public CartItem toppings(List<CartItemToppings> list) {
        this.toppings = list;
        return this;
    }
}
