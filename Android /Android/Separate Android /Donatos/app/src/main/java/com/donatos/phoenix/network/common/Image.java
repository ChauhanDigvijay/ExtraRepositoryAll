package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class Image {
    @JsonField(name = {"alt_text"})
    private String altText = null;
    @JsonField(name = {"extension"})
    private String extension = null;
    @JsonField(name = {"filepath"})
    private String filepath = null;
    @JsonField(name = {"height"})
    private Integer height = null;
    @JsonField(name = {"id"})
    private Integer id = null;
    @JsonField(name = {"width"})
    private Integer width = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public Image altText(String str) {
        this.altText = str;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Image image = (Image) obj;
        return C2507k.m7346a(this.id, image.id) && C2507k.m7346a(this.filepath, image.filepath) && C2507k.m7346a(this.extension, image.extension) && C2507k.m7346a(this.width, image.width) && C2507k.m7346a(this.height, image.height) && C2507k.m7346a(this.altText, image.altText);
    }

    public Image extension(String str) {
        this.extension = str;
        return this;
    }

    public Image filepath(String str) {
        this.filepath = str;
        return this;
    }

    public String getAltText() {
        return this.altText;
    }

    public String getExtension() {
        return this.extension;
    }

    public String getFilepath() {
        return this.filepath;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getWidth() {
        return this.width;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.id, this.filepath, this.extension, this.width, this.height, this.altText});
    }

    public Image height(Integer num) {
        this.height = num;
        return this;
    }

    public Image id(Integer num) {
        this.id = num;
        return this;
    }

    public void setAltText(String str) {
        this.altText = str;
    }

    public void setExtension(String str) {
        this.extension = str;
    }

    public void setFilepath(String str) {
        this.filepath = str;
    }

    public void setHeight(Integer num) {
        this.height = num;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public void setWidth(Integer num) {
        this.width = num;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class Image {\n");
        stringBuilder.append("    id: ").append(toIndentedString(this.id)).append("\n");
        stringBuilder.append("    filepath: ").append(toIndentedString(this.filepath)).append("\n");
        stringBuilder.append("    extension: ").append(toIndentedString(this.extension)).append("\n");
        stringBuilder.append("    width: ").append(toIndentedString(this.width)).append("\n");
        stringBuilder.append("    height: ").append(toIndentedString(this.height)).append("\n");
        stringBuilder.append("    altText: ").append(toIndentedString(this.altText)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public Image width(Integer num) {
        this.width = num;
        return this;
    }
}
