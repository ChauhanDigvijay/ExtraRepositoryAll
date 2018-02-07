package com.donatos.phoenix.network.dashboard;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.donatos.phoenix.network.common.Meta;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

public final class DashboardResponse$$JsonObjectMapper extends JsonMapper<DashboardResponse> {
    private static final JsonMapper<Meta> COM_DONATOS_PHOENIX_NETWORK_COMMON_META__JSONOBJECTMAPPER = LoganSquare.mapperFor(Meta.class);
    private static final JsonMapper<DashboardResponseContent> f7695xdb634d5b = LoganSquare.mapperFor(DashboardResponseContent.class);

    public final DashboardResponse parse(JsonParser jsonParser) throws IOException {
        DashboardResponse dashboardResponse = new DashboardResponse();
        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String currentName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            parseField(dashboardResponse, currentName, jsonParser);
            jsonParser.skipChildren();
        }
        return dashboardResponse;
    }

    public final void parseField(DashboardResponse dashboardResponse, String str, JsonParser jsonParser) throws IOException {
        if ("content".equals(str)) {
            dashboardResponse.setContent((DashboardResponseContent) f7695xdb634d5b.parse(jsonParser));
        } else if ("meta".equals(str)) {
            dashboardResponse.setMeta((Meta) COM_DONATOS_PHOENIX_NETWORK_COMMON_META__JSONOBJECTMAPPER.parse(jsonParser));
        }
    }

    public final void serialize(DashboardResponse dashboardResponse, JsonGenerator jsonGenerator, boolean z) throws IOException {
        if (z) {
            jsonGenerator.writeStartObject();
        }
        if (dashboardResponse.getContent() != null) {
            jsonGenerator.writeFieldName("content");
            f7695xdb634d5b.serialize(dashboardResponse.getContent(), jsonGenerator, true);
        }
        if (dashboardResponse.getMeta() != null) {
            jsonGenerator.writeFieldName("meta");
            COM_DONATOS_PHOENIX_NETWORK_COMMON_META__JSONOBJECTMAPPER.serialize(dashboardResponse.getMeta(), jsonGenerator, true);
        }
        if (z) {
            jsonGenerator.writeEndObject();
        }
    }
}
