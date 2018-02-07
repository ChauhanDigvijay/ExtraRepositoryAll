package com.identity.arx.httpconnection;

import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpHeadersData {
    public static HttpHeaders getHeaders() {
        String base64Credentials = new String(Base64.encodeBase64("arx:arx".getBytes()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        return headers;
    }
}
