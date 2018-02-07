package com.identity.arx.httpconnection;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class HttpUrlConnection {
    public static ResponseEntity<?> setHttpUrlConnection(Object pojoObject, String url) {
        try {
            ClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();

            RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
            return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity(pojoObject, HttpHeadersData.getHeaders()), Object.class, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
