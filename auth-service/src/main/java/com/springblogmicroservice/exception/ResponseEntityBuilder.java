package com.springblogmicroservice.exception;

import com.springblogmicroservice.dto.payload.response.ApiErrorResponse;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {

    public static ResponseEntity<Object> build(ApiErrorResponse apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}