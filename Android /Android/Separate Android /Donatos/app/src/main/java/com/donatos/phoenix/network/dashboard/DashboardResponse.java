package com.donatos.phoenix.network.dashboard;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.donatos.phoenix.network.common.Meta;
import com.donatos.phoenix.p134b.C2507k;
import java.util.Arrays;

@JsonObject
public class DashboardResponse {
    @JsonField(name = {"content"})
    private DashboardResponseContent content = null;
    @JsonField(name = {"meta"})
    private Meta meta = null;

    private String toIndentedString(Object obj) {
        return obj == null ? "null" : obj.toString().replace("\n", "\n    ");
    }

    public DashboardResponse content(DashboardResponseContent dashboardResponseContent) {
        this.content = dashboardResponseContent;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DashboardResponse dashboardResponse = (DashboardResponse) obj;
        return C2507k.m7346a(this.meta, dashboardResponse.meta) && C2507k.m7346a(this.content, dashboardResponse.content);
    }

    public DashboardResponseContent getContent() {
        return this.content;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.meta, this.content});
    }

    public DashboardResponse meta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public void setContent(DashboardResponseContent dashboardResponseContent) {
        this.content = dashboardResponseContent;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class DashboardResponse {\n");
        stringBuilder.append("    meta: ").append(toIndentedString(this.meta)).append("\n");
        stringBuilder.append("    content: ").append(toIndentedString(this.content)).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
