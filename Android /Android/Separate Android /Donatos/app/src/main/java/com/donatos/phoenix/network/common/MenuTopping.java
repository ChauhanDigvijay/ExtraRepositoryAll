package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;
import java.util.regex.Pattern;

@JsonObject
public class MenuTopping extends MenuItemBase {
    @JsonField(name = {"calories"})
    private String calories = null;
    @JsonField(name = {"external_id"})
    private String externalId = null;
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"max"})
    private Integer max = null;
    @JsonField(name = {"meat"})
    private Boolean meat = null;
    @JsonField(name = {"min"})
    private Integer min = null;
    @JsonField(name = {"multiplier"})
    private Integer multiplier = null;
    @JsonField(name = {"name"})
    private String name = null;
    @JsonField(name = {"placement"})
    private Integer placement = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public MenuTopping calories(String str) {
        this.calories = str;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MenuTopping menuTopping = (MenuTopping) obj;
        return C2507k.m7346a(this.id, menuTopping.id) && C2507k.m7346a(this.externalId, menuTopping.externalId) && C2507k.m7346a(this.name, menuTopping.name) && C2507k.m7346a(this.meat, menuTopping.meat) && C2507k.m7346a(this.min, menuTopping.min) && C2507k.m7346a(this.max, menuTopping.max) && C2507k.m7346a(this.multiplier, menuTopping.multiplier) && C2507k.m7346a(this.placement, menuTopping.placement);
    }

    public MenuTopping externalId(String str) {
        this.externalId = str;
        return this;
    }

    public String getCalories() {
        return this.calories;
    }

    public Integer getCommonId() {
        String str = "topping";
        for (String split : getExternalId().split(Pattern.quote(","))) {
            String[] split2 = split.split(Pattern.quote("="));
            if (split2[0].toLowerCase().equals(str.toLowerCase())) {
                return Integer.valueOf(Integer.parseInt(split2[1]));
            }
        }
        return null;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getMax() {
        return this.max;
    }

    public Boolean getMeat() {
        return this.meat;
    }

    public Integer getMin() {
        return this.min;
    }

    public Integer getMultiplier() {
        return this.multiplier;
    }

    public String getName() {
        return this.name;
    }

    public Integer getPlacement() {
        return this.placement;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.externalId, this.name, this.meat, this.min, this.max, this.multiplier, this.placement});
    }

    public MenuTopping id(Integer num) {
        this.id = num;
        return this;
    }

    public MenuTopping max(Integer num) {
        this.max = num;
        return this;
    }

    public MenuTopping meat(Boolean bool) {
        this.meat = bool;
        return this;
    }

    public MenuTopping min(Integer num) {
        this.min = num;
        return this;
    }

    public MenuTopping multiplier(Integer num) {
        this.multiplier = num;
        return this;
    }

    public MenuTopping name(String str) {
        this.name = str;
        return this;
    }

    public MenuTopping placement(Integer num) {
        this.placement = num;
        return this;
    }

    public void setCalories(String str) {
        this.calories = str;
    }

    public void setExternalId(String str) {
        this.externalId = str;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public void setMax(Integer num) {
        this.max = num;
    }

    public void setMeat(Boolean bool) {
        this.meat = bool;
    }

    public void setMin(Integer num) {
        this.min = num;
    }

    public void setMultiplier(Integer num) {
        this.multiplier = num;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPlacement(Integer num) {
        this.placement = num;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class MenuTopping {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    externalId: ").append(toIndentedString(this.externalId)).append("\n");
        stringBuilder.append("    name: ").append(toIndentedString(this.name)).append("\n");
        stringBuilder.append("    meat: ").append(toIndentedString(this.meat)).append("\n");
        stringBuilder.append("    min: ").append(toIndentedString(this.min)).append("\n");
        stringBuilder.append("    max: ").append(toIndentedString(this.max)).append("\n");
        stringBuilder.append("    multiplier: ").append(toIndentedString(this.multiplier)).append("\n");
        stringBuilder.append("    placement: ").append(toIndentedString(this.placement)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
