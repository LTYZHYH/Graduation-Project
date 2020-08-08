package com.example.travelapplication.service.valueobject;

import com.example.travelapplication.exception.ThrowableException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import retrofit2.Response;

public class ErrorResponse {
    private JsonNode responseBody;

    public ErrorResponse(Response response) {
        try {
            this.responseBody = new ObjectMapper().readValue(response.errorBody().string(), JsonNode.class);
        } catch (IOException e) {
            throw new ThrowableException(e);
        }
    }

    public String getErrorResponseMessage() {
        return responseBody.get("message").textValue();
    }
}
