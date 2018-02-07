package com.donatos.phoenix.network.dashboard;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.donatos.phoenix.network.common.Location;
import com.donatos.phoenix.network.common.MenuItem;
import com.donatos.phoenix.network.common.Order;
import com.donatos.phoenix.network.common.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class DashboardResponseContent$$JsonObjectMapper extends JsonMapper<DashboardResponseContent> {
    private static final JsonMapper<Location> COM_DONATOS_PHOENIX_NETWORK_COMMON_LOCATION__JSONOBJECTMAPPER = LoganSquare.mapperFor(Location.class);
    private static final JsonMapper<MenuItem> COM_DONATOS_PHOENIX_NETWORK_COMMON_MENUITEM__JSONOBJECTMAPPER = LoganSquare.mapperFor(MenuItem.class);
    private static final JsonMapper<Order> COM_DONATOS_PHOENIX_NETWORK_COMMON_ORDER__JSONOBJECTMAPPER = LoganSquare.mapperFor(Order.class);
    private static final JsonMapper<User> COM_DONATOS_PHOENIX_NETWORK_COMMON_USER__JSONOBJECTMAPPER = LoganSquare.mapperFor(User.class);

    public final DashboardResponseContent parse(JsonParser jsonParser) throws IOException {
        DashboardResponseContent dashboardResponseContent = new DashboardResponseContent();
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
            parseField(dashboardResponseContent, currentName, jsonParser);
            jsonParser.skipChildren();
        }
        return dashboardResponseContent;
    }

    public final void parseField(DashboardResponseContent dashboardResponseContent, String str, JsonParser jsonParser) throws IOException {
        if ("location".equals(str)) {
            dashboardResponseContent.setLocation((Location) COM_DONATOS_PHOENIX_NETWORK_COMMON_LOCATION__JSONOBJECTMAPPER.parse(jsonParser));
        } else if ("ltos".equals(str)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                r1 = new ArrayList();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    r1.add((MenuItem) COM_DONATOS_PHOENIX_NETWORK_COMMON_MENUITEM__JSONOBJECTMAPPER.parse(jsonParser));
                }
                dashboardResponseContent.setLtos(r1);
                return;
            }
            dashboardResponseContent.setLtos(null);
        } else if ("orders".equals(str)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                r1 = new ArrayList();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    r1.add((Order) COM_DONATOS_PHOENIX_NETWORK_COMMON_ORDER__JSONOBJECTMAPPER.parse(jsonParser));
                }
                dashboardResponseContent.setOrders(r1);
                return;
            }
            dashboardResponseContent.setOrders(null);
        } else if ("quickpicks".equals(str)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                r1 = new ArrayList();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    r1.add((MenuItem) COM_DONATOS_PHOENIX_NETWORK_COMMON_MENUITEM__JSONOBJECTMAPPER.parse(jsonParser));
                }
                dashboardResponseContent.setQuickpicks(r1);
                return;
            }
            dashboardResponseContent.setQuickpicks(null);
        } else if ("user".equals(str)) {
            dashboardResponseContent.setUser((User) COM_DONATOS_PHOENIX_NETWORK_COMMON_USER__JSONOBJECTMAPPER.parse(jsonParser));
        }
    }

    public final void serialize(DashboardResponseContent dashboardResponseContent, JsonGenerator jsonGenerator, boolean z) throws IOException {
        if (z) {
            jsonGenerator.writeStartObject();
        }
        if (dashboardResponseContent.getLocation() != null) {
            jsonGenerator.writeFieldName("location");
            COM_DONATOS_PHOENIX_NETWORK_COMMON_LOCATION__JSONOBJECTMAPPER.serialize(dashboardResponseContent.getLocation(), jsonGenerator, true);
        }
        List<MenuItem> ltos = dashboardResponseContent.getLtos();
        if (ltos != null) {
            jsonGenerator.writeFieldName("ltos");
            jsonGenerator.writeStartArray();
            for (MenuItem menuItem : ltos) {
                if (menuItem != null) {
                    COM_DONATOS_PHOENIX_NETWORK_COMMON_MENUITEM__JSONOBJECTMAPPER.serialize(menuItem, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        List<Order> orders = dashboardResponseContent.getOrders();
        if (orders != null) {
            jsonGenerator.writeFieldName("orders");
            jsonGenerator.writeStartArray();
            for (Order order : orders) {
                if (order != null) {
                    COM_DONATOS_PHOENIX_NETWORK_COMMON_ORDER__JSONOBJECTMAPPER.serialize(order, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        ltos = dashboardResponseContent.getQuickpicks();
        if (ltos != null) {
            jsonGenerator.writeFieldName("quickpicks");
            jsonGenerator.writeStartArray();
            for (MenuItem menuItem2 : ltos) {
                if (menuItem2 != null) {
                    COM_DONATOS_PHOENIX_NETWORK_COMMON_MENUITEM__JSONOBJECTMAPPER.serialize(menuItem2, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (dashboardResponseContent.getUser() != null) {
            jsonGenerator.writeFieldName("user");
            COM_DONATOS_PHOENIX_NETWORK_COMMON_USER__JSONOBJECTMAPPER.serialize(dashboardResponseContent.getUser(), jsonGenerator, true);
        }
        if (z) {
            jsonGenerator.writeEndObject();
        }
    }
}
