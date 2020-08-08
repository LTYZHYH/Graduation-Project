package com.example.travelapplication.exception;

import retrofit2.Response;

public class TokenRefreshFailedException extends RuntimeException{
    private Response response;

    public TokenRefreshFailedException(Response response){
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
