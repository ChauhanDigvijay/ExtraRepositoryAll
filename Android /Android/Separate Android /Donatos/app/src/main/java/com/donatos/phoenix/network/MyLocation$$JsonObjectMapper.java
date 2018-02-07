package com.donatos.phoenix.network;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

public final class MyLocation$$JsonObjectMapper extends JsonMapper<MyLocation> {
    public final MyLocation parse(JsonParser jsonParser) throws IOException {
        MyLocation myLocation = new MyLocation();
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
            parseField(myLocation, currentName, jsonParser);
            jsonParser.skipChildren();
        }
        return myLocation;
    }

    public final void parseField(MyLocation myLocation, String str, JsonParser jsonParser) throws IOException {
        Long l = null;
        Float f;
        if ("accuracy".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                f = new Float(jsonParser.getValueAsDouble());
            }
            myLocation.setAccuracy(f);
        } else if ("altitude".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                r0 = Double.valueOf(jsonParser.getValueAsDouble());
            }
            myLocation.setAltitude(r0);
        } else if ("bearing".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                f = new Float(jsonParser.getValueAsDouble());
            }
            myLocation.setBearing(f);
        } else if ("complete".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                r0 = Boolean.valueOf(jsonParser.getValueAsBoolean());
            }
            myLocation.setComplete(r0);
        } else if ("elapsedRealtimeNanos".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                l = Long.valueOf(jsonParser.getValueAsLong());
            }
            myLocation.setElapsedRealtimeNanos(l);
        } else if ("fromMockProvider".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                r0 = Boolean.valueOf(jsonParser.getValueAsBoolean());
            }
            myLocation.setFromMockProvider(r0);
        } else if ("latitude".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                r0 = Double.valueOf(jsonParser.getValueAsDouble());
            }
            myLocation.setLatitude(r0);
        } else if ("locationId".equals(str)) {
            myLocation.setLocationId(jsonParser.getValueAsInt());
        } else if ("longitude".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                r0 = Double.valueOf(jsonParser.getValueAsDouble());
            }
            myLocation.setLongitude(r0);
        } else if ("provider".equals(str)) {
            myLocation.setProvider(jsonParser.getValueAsString(null));
        } else if ("speed".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                f = new Float(jsonParser.getValueAsDouble());
            }
            myLocation.setSpeed(f);
        } else if ("time".equals(str)) {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                l = Long.valueOf(jsonParser.getValueAsLong());
            }
            myLocation.setTime(l);
        }
    }

    public final void serialize(MyLocation myLocation, JsonGenerator jsonGenerator, boolean z) throws IOException {
        if (z) {
            jsonGenerator.writeStartObject();
        }
        if (myLocation.getAccuracy() != null) {
            jsonGenerator.writeNumberField("accuracy", myLocation.getAccuracy().floatValue());
        }
        if (myLocation.getAltitude() != null) {
            jsonGenerator.writeNumberField("altitude", myLocation.getAltitude().doubleValue());
        }
        if (myLocation.getBearing() != null) {
            jsonGenerator.writeNumberField("bearing", myLocation.getBearing().floatValue());
        }
        if (myLocation.getComplete() != null) {
            jsonGenerator.writeBooleanField("complete", myLocation.getComplete().booleanValue());
        }
        if (myLocation.getElapsedRealtimeNanos() != null) {
            jsonGenerator.writeNumberField("elapsedRealtimeNanos", myLocation.getElapsedRealtimeNanos().longValue());
        }
        if (myLocation.getFromMockProvider() != null) {
            jsonGenerator.writeBooleanField("fromMockProvider", myLocation.getFromMockProvider().booleanValue());
        }
        if (myLocation.getLatitude() != null) {
            jsonGenerator.writeNumberField("latitude", myLocation.getLatitude().doubleValue());
        }
        jsonGenerator.writeNumberField("locationId", myLocation.getLocationId());
        if (myLocation.getLongitude() != null) {
            jsonGenerator.writeNumberField("longitude", myLocation.getLongitude().doubleValue());
        }
        if (myLocation.getProvider() != null) {
            jsonGenerator.writeStringField("provider", myLocation.getProvider());
        }
        if (myLocation.getSpeed() != null) {
            jsonGenerator.writeNumberField("speed", myLocation.getSpeed().floatValue());
        }
        if (myLocation.getTime() != null) {
            jsonGenerator.writeNumberField("time", myLocation.getTime().longValue());
        }
        if (z) {
            jsonGenerator.writeEndObject();
        }
    }
}
