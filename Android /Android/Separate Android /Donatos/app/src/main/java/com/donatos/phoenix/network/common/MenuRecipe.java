package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonObject
public class MenuRecipe extends MenuItemBase {
    @JsonField(name = {"external_id"})
    private String externalId = null;
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"legal_description"})
    private String legalDescription = null;
    @JsonField(name = {"legal_more_info"})
    private String legalMoreInfo = null;
    @JsonField(name = {"name"})
    private String name = null;
    @JsonField(name = {"required"})
    private List<MenuTopping> required = new ArrayList();
    @JsonField(name = {"selected"})
    private Boolean selected = null;
    @JsonField(name = {"sizes"})
    private List<MenuSize> sizes = new ArrayList();

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public MenuRecipe addRequiredItem(MenuTopping menuTopping) {
        this.required.add(menuTopping);
        return this;
    }

    public MenuRecipe addSizesItem(MenuSize menuSize) {
        this.sizes.add(menuSize);
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MenuRecipe menuRecipe = (MenuRecipe) obj;
        return C2507k.m7346a(this.id, menuRecipe.id) && C2507k.m7346a(this.externalId, menuRecipe.externalId) && C2507k.m7346a(this.name, menuRecipe.name) && C2507k.m7346a(this.selected, menuRecipe.selected) && C2507k.m7346a(this.legalDescription, menuRecipe.legalDescription) && C2507k.m7346a(this.legalMoreInfo, menuRecipe.legalMoreInfo) && C2507k.m7346a(this.required, menuRecipe.required) && C2507k.m7346a(this.sizes, menuRecipe.sizes);
    }

    public MenuRecipe externalId(String str) {
        this.externalId = str;
        return this;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public Integer getId() {
        return this.id;
    }

    public String getLegalDescription() {
        return this.legalDescription;
    }

    public String getLegalMoreInfo() {
        return this.legalMoreInfo;
    }

    public String getName() {
        return this.name;
    }

    public List<MenuTopping> getRequired() {
        return this.required;
    }

    public Boolean getSelected() {
        return this.selected;
    }

    public List<Integer> getSelectedRequiredCartItems() {
        List<Integer> arrayList = new ArrayList();
        for (MenuTopping menuTopping : getRequired()) {
            if (menuTopping.isUserSelected().booleanValue()) {
                arrayList.add(menuTopping.getId());
            }
        }
        return arrayList;
    }

    public int getSelectedSizeIndex() {
        for (MenuSize menuSize : this.sizes) {
            if (menuSize.isUserSelected().booleanValue()) {
                return this.sizes.indexOf(menuSize);
            }
        }
        return 0;
    }

    public int getSelectedSizeIndex(int i) {
        for (MenuSize menuSize : this.sizes) {
            if (menuSize.getId().equals(Integer.valueOf(i))) {
                return this.sizes.indexOf(menuSize);
            }
        }
        return 0;
    }

    public List<MenuSize> getSizes() {
        return this.sizes;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.externalId, this.name, this.selected, this.legalDescription, this.legalMoreInfo, this.required, this.sizes});
    }

    public MenuRecipe id(Integer num) {
        this.id = num;
        return this;
    }

    public MenuRecipe legalDescription(String str) {
        this.legalDescription = str;
        return this;
    }

    public MenuRecipe legalMoreInfo(String str) {
        this.legalMoreInfo = str;
        return this;
    }

    public MenuRecipe name(String str) {
        this.name = str;
        return this;
    }

    public MenuRecipe required(List<MenuTopping> list) {
        this.required = list;
        return this;
    }

    public MenuRecipe selected(Boolean bool) {
        this.selected = bool;
        return this;
    }

    public void setExternalId(String str) {
        this.externalId = str;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public void setLegalDescription(String str) {
        this.legalDescription = str;
    }

    public void setLegalMoreInfo(String str) {
        this.legalMoreInfo = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setRequired(List<MenuTopping> list) {
        this.required = list;
    }

    public void setSelected(Boolean bool) {
        this.selected = bool;
    }

    public void setSizes(List<MenuSize> list) {
        this.sizes = list;
    }

    public MenuRecipe sizes(List<MenuSize> list) {
        this.sizes = list;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class MenuRecipe {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    externalId: ").append(toIndentedString(this.externalId)).append("\n");
        stringBuilder.append("    name: ").append(toIndentedString(this.name)).append("\n");
        stringBuilder.append("    selected: ").append(toIndentedString(this.selected)).append("\n");
        stringBuilder.append("    legalDescription: ").append(toIndentedString(this.legalDescription)).append("\n");
        stringBuilder.append("    legalMoreInfo: ").append(toIndentedString(this.legalMoreInfo)).append("\n");
        stringBuilder.append("    required: ").append(toIndentedString(this.required)).append("\n");
        stringBuilder.append("    sizes: ").append(toIndentedString(this.sizes)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
