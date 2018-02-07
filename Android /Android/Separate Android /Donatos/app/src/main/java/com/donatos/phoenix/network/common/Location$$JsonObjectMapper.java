package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Location$$JsonObjectMapper extends JsonMapper<Location> {
    private static final JsonMapper<LocationCreditCards> f7684xfc12162d = LoganSquare.mapperFor(LocationCreditCards.class);
    private static final JsonMapper<LocationHours> f7685x547ef232 = LoganSquare.mapperFor(LocationHours.class);
    private static final JsonMapper<LocationOrderType> f7686x16db0eb = LoganSquare.mapperFor(LocationOrderType.class);
    private static final JsonMapper<Menu> COM_DONATOS_PHOENIX_NETWORK_COMMON_MENU__JSONOBJECTMAPPER = LoganSquare.mapperFor(Menu.class);

    public final Location parse(JsonParser jsonParser) throws IOException {
        Location location = new Location();
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
            parseField(location, currentName, jsonParser);
            jsonParser.skipChildren();
        }
        return location;
    }

    public final void parseField(Location location, String str, JsonParser jsonParser) throws IOException {
        Integer num = null;
        if ("accepts_stored_payments".equals(str)) {
            Boolean valueOf;
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                valueOf = Boolean.valueOf(jsonParser.getValueAsBoolean());
            }
            location.setAcceptsStoredPayments(valueOf);
        } else if ("address1".equals(str)) {
            location.setAddress1(jsonParser.getValueAsString(null));
        } else if ("address2".equals(str)) {
            location.setAddress2(jsonParser.getValueAsString(null));
        } else if ("city".equals(str)) {
            location.setCity(jsonParser.getValueAsString(null));
        } else if ("creditCards".equals(str)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                r1 = new ArrayList();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    r1.add((LocationCreditCards) f7684xfc12162d.parse(jsonParser));
                }
                location.setCreditCards(r1);
                return;
            }
            location.setCreditCards(null);
        } else if ("delivery".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                r0 = Double.valueOf(jsonParser.getValueAsDouble());
            }
            location.setDelivery(r0);
        } else if ("distance".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                r0 = Double.valueOf(jsonParser.getValueAsDouble());
            }
            location.setDistance(r0);
        } else if ("hours".equals(str)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                r1 = new ArrayList();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    r1.add((LocationHours) f7685x547ef232.parse(jsonParser));
                }
                location.setHours(r1);
                return;
            }
            location.setHours(null);
        } else if ("id".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                num = Integer.valueOf(jsonParser.getValueAsInt());
            }
            location.setId(num);
        } else if ("latitude".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                r0 = Double.valueOf(jsonParser.getValueAsDouble());
            }
            location.setLatitude(r0);
        } else if ("longitude".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                r0 = Double.valueOf(jsonParser.getValueAsDouble());
            }
            location.setLongitude(r0);
        } else if ("menu".equals(str)) {
            location.setMenu((Menu) COM_DONATOS_PHOENIX_NETWORK_COMMON_MENU__JSONOBJECTMAPPER.parse(jsonParser));
        } else if ("name".equals(str)) {
            location.setName(jsonParser.getValueAsString(null));
        } else if ("orderTypes".equals(str)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                r1 = new ArrayList();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    r1.add((LocationOrderType) f7686x16db0eb.parse(jsonParser));
                }
                location.setOrderTypes(r1);
                return;
            }
            location.setOrderTypes(null);
        } else if ("paymentTypes".equals(str)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                r1 = new ArrayList();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    r1.add((LocationOrderType) f7686x16db0eb.parse(jsonParser));
                }
                location.setPaymentTypes(r1);
                return;
            }
            location.setPaymentTypes(null);
        } else if ("phone".equals(str)) {
            location.setPhone(jsonParser.getValueAsString(null));
        } else if ("pos".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                num = Integer.valueOf(jsonParser.getValueAsInt());
            }
            location.setPos(num);
        } else if ("state".equals(str)) {
            location.setState(jsonParser.getValueAsString(null));
        } else if ("zip".equals(str)) {
            location.setZip(jsonParser.getValueAsString(null));
        }
    }

    public final void serialize(Location location, JsonGenerator jsonGenerator, boolean z) throws IOException {
        if (z) {
            jsonGenerator.writeStartObject();
        }
        if (location.getAcceptsStoredPayments() != null) {
            jsonGenerator.writeBooleanField("accepts_stored_payments", location.getAcceptsStoredPayments().booleanValue());
        }
        if (location.getAddress1() != null) {
            jsonGenerator.writeStringField("address1", location.getAddress1());
        }
        if (location.getAddress2() != null) {
            jsonGenerator.writeStringField("address2", location.getAddress2());
        }
        if (location.getCity() != null) {
            jsonGenerator.writeStringField("city", location.getCity());
        }
        List<LocationCreditCards> creditCards = location.getCreditCards();
        if (creditCards != null) {
            jsonGenerator.writeFieldName("creditCards");
            jsonGenerator.writeStartArray();
            for (LocationCreditCards locationCreditCards : creditCards) {
                if (locationCreditCards != null) {
                    f7684xfc12162d.serialize(locationCreditCards, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (location.getDelivery() != null) {
            jsonGenerator.writeNumberField("delivery", location.getDelivery().doubleValue());
        }
        if (location.getDistance() != null) {
            jsonGenerator.writeNumberField("distance", location.getDistance().doubleValue());
        }
        List<LocationHours> hours = location.getHours();
        if (hours != null) {
            jsonGenerator.writeFieldName("hours");
            jsonGenerator.writeStartArray();
            for (LocationHours locationHours : hours) {
                if (locationHours != null) {
                    f7685x547ef232.serialize(locationHours, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (location.getId() != null) {
            jsonGenerator.writeNumberField("id", location.getId().intValue());
        }
        if (location.getLatitude() != null) {
            jsonGenerator.writeNumberField("latitude", location.getLatitude().doubleValue());
        }
        if (location.getLongitude() != null) {
            jsonGenerator.writeNumberField("longitude", location.getLongitude().doubleValue());
        }
        if (location.getMenu() != null) {
            jsonGenerator.writeFieldName("menu");
            COM_DONATOS_PHOENIX_NETWORK_COMMON_MENU__JSONOBJECTMAPPER.serialize(location.getMenu(), jsonGenerator, true);
        }
        if (location.getName() != null) {
            jsonGenerator.writeStringField("name", location.getName());
        }
        List<LocationOrderType> orderTypes = location.getOrderTypes();
        if (orderTypes != null) {
            jsonGenerator.writeFieldName("orderTypes");
            jsonGenerator.writeStartArray();
            for (LocationOrderType locationOrderType : orderTypes) {
                if (locationOrderType != null) {
                    f7686x16db0eb.serialize(locationOrderType, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        orderTypes = location.getPaymentTypes();
        if (orderTypes != null) {
            jsonGenerator.writeFieldName("paymentTypes");
            jsonGenerator.writeStartArray();
            for (LocationOrderType locationOrderType2 : orderTypes) {
                if (locationOrderType2 != null) {
                    f7686x16db0eb.serialize(locationOrderType2, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (location.getPhone() != null) {
            jsonGenerator.writeStringField("phone", location.getPhone());
        }
        if (location.getPos() != null) {
            jsonGenerator.writeNumberField("pos", location.getPos().intValue());
        }
        if (location.getState() != null) {
            jsonGenerator.writeStringField("state", location.getState());
        }
        if (location.getZip() != null) {
            jsonGenerator.writeStringField("zip", location.getZip());
        }
        if (z) {
            jsonGenerator.writeEndObject();
        }
    }
}
