package com.identity.arx.httpasynctask;

import org.springframework.http.ResponseEntity;

public interface AsyncResponse {
    void asyncResponse(ResponseEntity<?> responseEntity);
}
