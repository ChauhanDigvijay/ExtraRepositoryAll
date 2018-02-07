package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.Meta;
import java.util.ArrayList;
import java.util.List;

@JsonObject
public class PromotionResponse {
    @JsonField(name = {"content"})
    private List<PromotionResponseContent> content = new ArrayList();
    @JsonField(name = {"meta"})
    private Meta meta = null;

    public PromotionResponse content(List<PromotionResponseContent> list) {
        this.content = list;
        return this;
    }

    public List<PromotionResponseContent> getContent() {
        return this.content;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public PromotionResponse meta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public void setContent(List<PromotionResponseContent> list) {
        this.content = list;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
