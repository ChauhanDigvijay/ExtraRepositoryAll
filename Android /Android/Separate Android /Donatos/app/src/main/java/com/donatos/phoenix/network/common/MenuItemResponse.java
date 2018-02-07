package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class MenuItemResponse {
    @JsonField(name = {"content"})
    private MenuItemResponseContent content = null;
    @JsonField(name = {"meta"})
    private Meta meta = null;

    public MenuItemResponse content(MenuItemResponseContent menuItemResponseContent) {
        this.content = menuItemResponseContent;
        return this;
    }

    public MenuItemResponseContent getContent() {
        return this.content;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public MenuItemResponse meta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public void setContent(MenuItemResponseContent menuItemResponseContent) {
        this.content = menuItemResponseContent;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
