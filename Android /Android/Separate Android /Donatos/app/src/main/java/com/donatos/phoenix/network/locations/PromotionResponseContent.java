package com.donatos.phoenix.network.locations;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class PromotionResponseContent {
    @JsonField(name = {"banner_image"})
    private String bannerImage = null;
    @JsonField(name = {"href"})
    private String href = null;
    @JsonField(name = {"slug"})
    private String slug = null;

    public PromotionResponseContent bannerImage(String str) {
        this.bannerImage = str;
        return this;
    }

    public String getBannerImage() {
        return this.bannerImage;
    }

    public String getHref() {
        return this.href;
    }

    public String getSlug() {
        return this.slug;
    }

    public PromotionResponseContent href(String str) {
        this.href = str;
        return this;
    }

    public void setBannerImage(String str) {
        this.bannerImage = str;
    }

    public void setHref(String str) {
        this.href = str;
    }

    public void setSlug(String str) {
        this.slug = str;
    }

    public PromotionResponseContent slug(String str) {
        this.slug = str;
        return this;
    }
}
