package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.locations.MenuInstruction;
import com.donatos.phoenix.p134b.C2507k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonObject
public class MenuCategory {
    @JsonField(name = {"description"})
    private String description = null;
    @JsonField(name = {"external_id"})
    private String externalId = null;
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"instructions"})
    private List<MenuInstruction> instructions = new ArrayList();
    @JsonField(name = {"items"})
    private List<MenuItem> items = new ArrayList();
    @JsonField(name = {"name"})
    private String name = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public MenuCategory addInstructionsItem(MenuInstruction menuInstruction) {
        this.instructions.add(menuInstruction);
        return this;
    }

    public MenuCategory addItemsItem(MenuItem menuItem) {
        this.items.add(menuItem);
        return this;
    }

    public MenuCategory description(String str) {
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
        MenuCategory menuCategory = (MenuCategory) obj;
        return C2507k.m7346a(this.id, menuCategory.id) && C2507k.m7346a(this.externalId, menuCategory.externalId) && C2507k.m7346a(this.name, menuCategory.name) && C2507k.m7346a(this.description, menuCategory.description) && C2507k.m7346a(this.instructions, menuCategory.instructions) && C2507k.m7346a(this.items, menuCategory.items);
    }

    public MenuCategory externalId(String str) {
        this.externalId = str;
        return this;
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

    public List<MenuInstruction> getInstructions() {
        return this.instructions;
    }

    public List<MenuItem> getItems() {
        return this.items;
    }

    public String getName() {
        return this.name;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.externalId, this.name, this.description, this.instructions, this.items});
    }

    public MenuCategory id(Integer num) {
        this.id = num;
        return this;
    }

    public MenuCategory instructions(List<MenuInstruction> list) {
        this.instructions = list;
        return this;
    }

    public MenuCategory items(List<MenuItem> list) {
        this.items = list;
        return this;
    }

    public MenuCategory name(String str) {
        this.name = str;
        return this;
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

    public void setInstructions(List<MenuInstruction> list) {
        this.instructions = list;
    }

    public void setItems(List<MenuItem> list) {
        this.items = list;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class MenuCategory {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    externalId: ").append(toIndentedString(this.externalId)).append("\n");
        stringBuilder.append("    name: ").append(toIndentedString(this.name)).append("\n");
        stringBuilder.append("    description: ").append(toIndentedString(this.description)).append("\n");
        stringBuilder.append("    instructions: ").append(toIndentedString(this.instructions)).append("\n");
        stringBuilder.append("    items: ").append(toIndentedString(this.items)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
