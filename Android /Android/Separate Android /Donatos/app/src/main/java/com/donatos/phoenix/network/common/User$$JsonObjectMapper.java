package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class User$$JsonObjectMapper extends JsonMapper<User> {
    private static final JsonMapper<Address> COM_DONATOS_PHOENIX_NETWORK_COMMON_ADDRESS__JSONOBJECTMAPPER = LoganSquare.mapperFor(Address.class);
    private static final JsonMapper<PaymentResponseContent> f7694x8f6d7d8a = LoganSquare.mapperFor(PaymentResponseContent.class);
    protected static final PhoneTypeConverter COM_DONATOS_PHOENIX_NETWORK_COMMON_PHONETYPECONVERTER = new PhoneTypeConverter();

    public final User parse(JsonParser jsonParser) throws IOException {
        User user = new User();
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
            parseField(user, currentName, jsonParser);
            jsonParser.skipChildren();
        }
        return user;
    }

    public final void parseField(User user, String str, JsonParser jsonParser) throws IOException {
        Integer num = null;
        if ("companyName".equals(str)) {
            user.setCompanyName(jsonParser.getValueAsString(null));
        } else if ("deliveryAddresses".equals(str)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                r1 = new ArrayList();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    r1.add((Address) COM_DONATOS_PHOENIX_NETWORK_COMMON_ADDRESS__JSONOBJECTMAPPER.parse(jsonParser));
                }
                user.setDeliveryAddresses(r1);
                return;
            }
            user.setDeliveryAddresses(null);
        } else if ("email".equals(str)) {
            user.setEmail(jsonParser.getValueAsString(null));
        } else if ("firstName".equals(str)) {
            user.setFirstName(jsonParser.getValueAsString(null));
        } else if ("id".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                num = Integer.valueOf(jsonParser.getValueAsInt());
            }
            user.setId(num);
        } else if ("lastName".equals(str)) {
            user.setLastName(jsonParser.getValueAsString(null));
        } else if ("phone".equals(str)) {
            user.setPhone((Phone) COM_DONATOS_PHOENIX_NETWORK_COMMON_PHONETYPECONVERTER.parse(jsonParser));
        } else if (!"storedPayments".equals(str)) {
        } else {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                r1 = new ArrayList();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    r1.add((PaymentResponseContent) f7694x8f6d7d8a.parse(jsonParser));
                }
                user.setStoredPayments(r1);
                return;
            }
            user.setStoredPayments(null);
        }
    }

    public final void serialize(User user, JsonGenerator jsonGenerator, boolean z) throws IOException {
        if (z) {
            jsonGenerator.writeStartObject();
        }
        if (user.getCompanyName() != null) {
            jsonGenerator.writeStringField("companyName", user.getCompanyName());
        }
        List<Address> deliveryAddresses = user.getDeliveryAddresses();
        if (deliveryAddresses != null) {
            jsonGenerator.writeFieldName("deliveryAddresses");
            jsonGenerator.writeStartArray();
            for (Address address : deliveryAddresses) {
                if (address != null) {
                    COM_DONATOS_PHOENIX_NETWORK_COMMON_ADDRESS__JSONOBJECTMAPPER.serialize(address, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (user.getEmail() != null) {
            jsonGenerator.writeStringField("email", user.getEmail());
        }
        if (user.getFirstName() != null) {
            jsonGenerator.writeStringField("firstName", user.getFirstName());
        }
        if (user.getId() != null) {
            jsonGenerator.writeNumberField("id", user.getId().intValue());
        }
        if (user.getLastName() != null) {
            jsonGenerator.writeStringField("lastName", user.getLastName());
        }
        COM_DONATOS_PHOENIX_NETWORK_COMMON_PHONETYPECONVERTER.serialize(user.getPhone(), "phone", true, jsonGenerator);
        List<PaymentResponseContent> storedPayments = user.getStoredPayments();
        if (storedPayments != null) {
            jsonGenerator.writeFieldName("storedPayments");
            jsonGenerator.writeStartArray();
            for (PaymentResponseContent paymentResponseContent : storedPayments) {
                if (paymentResponseContent != null) {
                    f7694x8f6d7d8a.serialize(paymentResponseContent, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (z) {
            jsonGenerator.writeEndObject();
        }
    }
}
