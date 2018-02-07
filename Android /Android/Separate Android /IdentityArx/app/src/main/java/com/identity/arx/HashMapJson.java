package com.identity.arx;

import java.util.Map;
import org.json.JSONObject;

public class HashMapJson {
    public static JSONObject getJsonObject(Map<String, String> stringMap) {
        return new JSONObject(stringMap);
    }
}
