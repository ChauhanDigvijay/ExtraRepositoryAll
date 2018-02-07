package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@JsonObject
public class MenuSize extends MenuItemBase {
    @JsonField(name = {"default_price"})
    private Double defaultPrice = null;
    @JsonField(name = {"dressings"})
    private List<MenuTopping> dressings = new ArrayList();
    @JsonField(name = {"external_id"})
    private String externalId = null;
    @JsonField(name = {"flavors"})
    private List<MenuTopping> flavors = new ArrayList();
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"name"})
    private String name = null;
    @JsonField(name = {"sauces"})
    private List<MenuTopping> sauces = new ArrayList();
    @JsonField(name = {"selected"})
    private Boolean selected = null;
    @JsonField(name = {"selected_toppings"})
    private List<Integer> selectedToppings = new ArrayList();
    @JsonField(name = {"toppings"})
    private List<MenuTopping> toppings = new ArrayList();

    private List<Integer> getSelectedToppingIds(List<MenuTopping> list) {
        List<Integer> arrayList = new ArrayList();
        for (MenuTopping menuTopping : list) {
            if (menuTopping.isUserSelected().booleanValue()) {
                for (int i = 0; i < menuTopping.getUserQuantity().intValue(); i++) {
                    arrayList.add(menuTopping.getId());
                }
            }
        }
        return arrayList;
    }

    private List<CartItemToppings> getSelectedToppings(List<MenuTopping> list) {
        List<CartItemToppings> arrayList = new ArrayList();
        for (MenuTopping menuTopping : list) {
            if (menuTopping.isUserSelected().booleanValue()) {
                arrayList.add(new CartItemToppings().id(menuTopping.getId()).multiplier(menuTopping.getUserQuantity()).placement(Integer.valueOf(menuTopping.getToppingCoverage().f8811e)));
            }
        }
        return arrayList;
    }

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public MenuSize addDressingsItem(MenuTopping menuTopping) {
        this.dressings.add(menuTopping);
        return this;
    }

    public MenuSize addFlavorsItem(MenuTopping menuTopping) {
        this.flavors.add(menuTopping);
        return this;
    }

    public MenuSize addSaucesItem(MenuTopping menuTopping) {
        this.sauces.add(menuTopping);
        return this;
    }

    public MenuSize addSelectedToppingsItem(Integer num) {
        this.selectedToppings.add(num);
        return this;
    }

    public MenuSize addToppingsItem(MenuTopping menuTopping) {
        this.toppings.add(menuTopping);
        return this;
    }

    public MenuSize defaultPrice(Double d) {
        this.defaultPrice = d;
        return this;
    }

    public MenuSize dressings(List<MenuTopping> list) {
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
        MenuSize menuSize = (MenuSize) obj;
        return C2507k.m7346a(this.id, menuSize.id) && C2507k.m7346a(this.externalId, menuSize.externalId) && C2507k.m7346a(this.name, menuSize.name) && C2507k.m7346a(this.selected, menuSize.selected) && C2507k.m7346a(this.selectedToppings, menuSize.selectedToppings) && C2507k.m7346a(this.toppings, menuSize.toppings) && C2507k.m7346a(this.dressings, menuSize.dressings) && C2507k.m7346a(this.flavors, menuSize.flavors) && C2507k.m7346a(this.sauces, menuSize.sauces) && C2507k.m7346a(this.defaultPrice, menuSize.defaultPrice);
    }

    public MenuSize externalId(String str) {
        this.externalId = str;
        return this;
    }

    public MenuSize flavors(List<MenuTopping> list) {
        this.flavors = list;
        return this;
    }

    public Integer getCommonId() {
        return Integer.valueOf(Integer.parseInt(getExternalId()));
    }

    public Double getDefaultPrice() {
        return this.defaultPrice;
    }

    public MenuTopping getDressingByCommonId(int i) {
        for (MenuTopping menuTopping : this.dressings) {
            if (menuTopping.getCommonId().equals(Integer.valueOf(i))) {
                return menuTopping;
            }
        }
        return null;
    }

    public List<MenuTopping> getDressings() {
        return this.dressings;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public List<MenuTopping> getFlavors() {
        return this.flavors;
    }

    public Integer getId() {
        return this.id;
    }

    public List<MenuTopping> getMeats() {
        List<MenuTopping> arrayList = new ArrayList();
        for (MenuTopping menuTopping : this.toppings) {
            if (menuTopping.getMeat().booleanValue()) {
                menuTopping.setIsTopping(Boolean.valueOf(true));
                arrayList.add(menuTopping);
            }
        }
        return arrayList;
    }

    public String getName() {
        return this.name;
    }

    public List<MenuTopping> getNonMeats() {
        List<MenuTopping> arrayList = new ArrayList();
        for (MenuTopping menuTopping : this.toppings) {
            if (!menuTopping.getMeat().booleanValue()) {
                menuTopping.setIsTopping(Boolean.valueOf(true));
                arrayList.add(menuTopping);
            }
        }
        return arrayList;
    }

    public MenuTopping getSauceByCommonId(int i) {
        for (MenuTopping menuTopping : this.sauces) {
            if (menuTopping.getCommonId().equals(Integer.valueOf(i))) {
                return menuTopping;
            }
        }
        return null;
    }

    public List<MenuTopping> getSauces() {
        return this.sauces;
    }

    public Boolean getSelected() {
        return this.selected;
    }

    public List<Integer> getSelectedDressingsCartItems() {
        return getSelectedToppingIds(this.dressings);
    }

    public HashSet<Integer> getSelectedDressingsCommonIds() {
        HashSet<Integer> hashSet = new HashSet();
        for (MenuTopping menuTopping : this.dressings) {
            if (menuTopping.isUserSelected().booleanValue() && !this.selectedToppings.contains(menuTopping.getCommonId())) {
                hashSet.add(menuTopping.getCommonId());
            }
        }
        return hashSet;
    }

    public int getSelectedDressingsCount() {
        int i = 0;
        for (MenuTopping userQuantity : getDressings()) {
            i = userQuantity.getUserQuantity().intValue() + i;
        }
        return i;
    }

    public int getSelectedFlavorIndex() {
        for (MenuTopping menuTopping : this.flavors) {
            if (menuTopping.isUserSelected().booleanValue()) {
                return this.flavors.indexOf(menuTopping);
            }
        }
        return 0;
    }

    public List<Integer> getSelectedFlavorsCartItems() {
        return getSelectedToppingIds(this.flavors);
    }

    public List<Integer> getSelectedSaucesCartItems() {
        return getSelectedToppingIds(this.sauces);
    }

    public HashSet<Integer> getSelectedSaucesCommonIds() {
        HashSet<Integer> hashSet = new HashSet();
        for (MenuTopping menuTopping : this.sauces) {
            if (menuTopping.isUserSelected().booleanValue() && !this.selectedToppings.contains(menuTopping.getCommonId())) {
                hashSet.add(menuTopping.getCommonId());
            }
        }
        return hashSet;
    }

    public int getSelectedSaucesCount() {
        int i = 0;
        for (MenuTopping userQuantity : getSauces()) {
            i = userQuantity.getUserQuantity().intValue() + i;
        }
        return i;
    }

    public List<Integer> getSelectedToppings() {
        return this.selectedToppings;
    }

    public List<CartItemToppings> getSelectedToppingsCartItems() {
        return getSelectedToppings(this.toppings);
    }

    public HashSet<Integer> getSelectedToppingsCommonIds() {
        HashSet<Integer> hashSet = new HashSet();
        for (MenuTopping menuTopping : this.toppings) {
            if (menuTopping.isUserSelected().booleanValue() && !hashSet.contains(menuTopping.getCommonId())) {
                hashSet.add(menuTopping.getCommonId());
            }
        }
        return hashSet;
    }

    public int getSelectedToppingsCount() {
        int i = 0;
        for (MenuTopping userQuantity : getToppings()) {
            i = userQuantity.getUserQuantity().intValue() + i;
        }
        return i;
    }

    public MenuTopping getToppingByCommonId(int i) {
        for (MenuTopping menuTopping : this.toppings) {
            if (menuTopping.getCommonId().equals(Integer.valueOf(i))) {
                return menuTopping;
            }
        }
        return null;
    }

    public List<MenuTopping> getToppings() {
        return this.toppings;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.externalId, this.name, this.selected, this.selectedToppings, this.toppings, this.dressings, this.flavors, this.sauces, this.defaultPrice});
    }

    public MenuSize id(Integer num) {
        this.id = num;
        return this;
    }

    public MenuSize name(String str) {
        this.name = str;
        return this;
    }

    public MenuSize sauces(List<MenuTopping> list) {
        this.sauces = list;
        return this;
    }

    public MenuSize selected(Boolean bool) {
        this.selected = bool;
        return this;
    }

    public MenuSize selectedToppings(List<Integer> list) {
        this.selectedToppings = list;
        return this;
    }

    public void setDefaultPrice(Double d) {
        this.defaultPrice = d;
    }

    public void setDressings(List<MenuTopping> list) {
        this.dressings = list;
    }

    public void setExternalId(String str) {
        this.externalId = str;
    }

    public void setFlavors(List<MenuTopping> list) {
        this.flavors = list;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setSauces(List<MenuTopping> list) {
        this.sauces = list;
    }

    public void setSelected(Boolean bool) {
        this.selected = bool;
    }

    public void setSelectedCountOnDressings(Integer num) {
        for (MenuTopping selectedToppingCount : getDressings()) {
            selectedToppingCount.setSelectedToppingCount(num);
        }
    }

    public void setSelectedCountOnSauces(Integer num) {
        for (MenuTopping selectedToppingCount : getSauces()) {
            selectedToppingCount.setSelectedToppingCount(num);
        }
    }

    public void setSelectedCountOnToppings(Integer num) {
        for (MenuTopping selectedToppingCount : getToppings()) {
            selectedToppingCount.setSelectedToppingCount(num);
        }
    }

    public void setSelectedToppings(List<Integer> list) {
        this.selectedToppings = list;
    }

    public void setToppings(List<MenuTopping> list) {
        this.toppings = list;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class MenuSize {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    externalId: ").append(toIndentedString(this.externalId)).append("\n");
        stringBuilder.append("    name: ").append(toIndentedString(this.name)).append("\n");
        stringBuilder.append("    selected: ").append(toIndentedString(this.selected)).append("\n");
        stringBuilder.append("    selectedToppings: ").append(toIndentedString(this.selectedToppings)).append("\n");
        stringBuilder.append("    toppings: ").append(toIndentedString(this.toppings)).append("\n");
        stringBuilder.append("    dressings: ").append(toIndentedString(this.dressings)).append("\n");
        stringBuilder.append("    flavors: ").append(toIndentedString(this.flavors)).append("\n");
        stringBuilder.append("    sauces: ").append(toIndentedString(this.sauces)).append("\n");
        stringBuilder.append("    defaultPrice: ").append(toIndentedString(this.defaultPrice)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public MenuSize toppings(List<MenuTopping> list) {
        this.toppings = list;
        return this;
    }
}
